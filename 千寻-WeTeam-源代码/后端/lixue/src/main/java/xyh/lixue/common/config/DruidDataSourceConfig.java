package xyh.lixue.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author XiangYida
 * @version 2019/5/17 9:27
 */
@Configuration
public class DruidDataSourceConfig {
    @Bean(name = "frameworkDruidDS")
    @ConfigurationProperties(prefix="spring.datasource.druid")
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(com.alibaba.druid.pool.DruidDataSource.class).build();
    }
}
