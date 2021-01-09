package zz.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import zz.enums.MyConstants;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzheng
 * @date 2021-01-08
 **/
@Lazy
@Service
public class DataTransferService {
    @Resource(name = "askdata5JdbcTemplate")
    private JdbcTemplate askdata5JdbcTemplate;

    @Resource(name = "askdata4JdbcTemplate")
    private JdbcTemplate askdata4JdbcTemplate;

    private final Logger log = LoggerFactory.getLogger(DataTransferService.class);

    private String sql;
    private FileOutputStream output;
    private int total = 1;

    private void before() throws IOException {
        String outputFile = System.getProperty("outputFile");
        output = new FileOutputStream(outputFile);
        String sqlFile = System.getProperty("sqlFile");
        log.info("sqlFile：[{}]，outputFile：[{}]", sqlFile, outputFile);
        sql = FileUtils.readFileToString(new File(sqlFile), StandardCharsets.UTF_8);
    }

    private void getTids(){
        askdata4JdbcTemplate.query("select tid from AuthTopics", new ResultSetExtractor<Object>() {
            @Override
            public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                List<Integer> tids = new ArrayList<>(1000);
                for (int i = 1,k = 0; resultSet.next(); i++) {
                    int tid = resultSet.getInt(1);
                    tids.add(tid);
                    if(i % 1000 == 0){
                        k++;
                        log.info("开始第[{}]个批次的写入", k);
                        try {
                            export(tids);
                            tids = new ArrayList<>(1000);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    export(tids);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    public void export() throws IOException {
        before();
        getTids();
        log.info("共[{}]条回复记录", total);
    }

    public void export(List<Integer> tids) throws SQLException, IOException {
        StringBuilder stringBuilder = new StringBuilder(sql).append(" and t1.tid in (");
        for (int tid:tids){
            stringBuilder.append(tid).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(")");
        askdata5JdbcTemplate.query(stringBuilder.toString(), new ResultSetExtractor<Object>() {
            @Override
            public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int j = 0;
                int columnCount = metaData.getColumnCount();
                for (int k = 0; resultSet.next(); j++) {
                    List<Object> data = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String value = resultSet.getString(i);
                        if(i == 10 && !StringUtils.isEmpty(value)){
                            data.add(value.replaceAll("\n", MyConstants.SUB));
                        }else{
                            data.add(value);
                        }
                    }
                    try {
                        IOUtils.writeLines(Lists.newArrayList(Joiner.on(MyConstants.ESC).useForNull("").join(data)), System.getProperty("line.separator"), output, MyConstants.CHART_SET);
                        j++;
                        total++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                log.info("导入[{}]条回复", j);
                return null;
            }
        });
    }
}
