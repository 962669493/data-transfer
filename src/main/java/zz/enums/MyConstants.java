package zz.enums;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author zhangzheng
 * @date 2021-01-03
 **/
public class MyConstants {
    /** 统一用esc作为分隔符 */
    public static final String ESC = String.valueOf((char)27);
    /** 统一用sub替换换行符 */
    public static final String SUB = String.valueOf((char)26);
    /** 统一用ht作为分隔符 */
    public static final String HT = String.valueOf((char)9);
    /** 一次查询获取的数据量 */
    public static final int FETCH_SIZE = 10000;
    /** IO编码格式 */
    public static final Charset CHART_SET = StandardCharsets.UTF_8;
}
