package zz.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author zhangzheng
 * @date 2021-01-04
 **/
@Configuration
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.askdata5")
    @Qualifier("askdata5DataSource")
    DataSource askdata5DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.askdata4")
    @Qualifier("askdata4DataSource")
    DataSource askdata4DataSource() {
        return DataSourceBuilder.create().build();
    }
}
