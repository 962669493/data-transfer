package zz.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author zhangzheng
 * @date 2021-01-04
 **/
@Configuration
public class JdbcTemplateConfig {
    @Bean(name = "askdata5JdbcTemplate")
    public JdbcTemplate askdata5JdbcTemplate(
            @Qualifier("askdata5DataSource") DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setFetchSize(10000);
        return jdbcTemplate;
    }

    @Bean(name = "askdata4JdbcTemplate")
    public JdbcTemplate askdata4JdbcTemplate(
            @Qualifier("askdata4DataSource") DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setFetchSize(10000);
        return jdbcTemplate;
    }
}
