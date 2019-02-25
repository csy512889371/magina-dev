package com.rjsoft.magina.component.query.sql.configuration;

import com.mysql.jdbc.StringUtils;
import com.rjsoft.magina.component.query.sql.configuration.base.OrderedStep;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * JDBC 数据源配置
 */
@Configuration
@ImportResource({
        "classpath:/spring/jpa/spring-aspect.xml",
        "classpath*:/spring/jpa/dataSource/**.xml"
})
@Order(OrderedStep.TENTH)
public class SqlRepositoryConfigBean extends SqlQueryConfiguration {

    private static Map<Object, Object> targetDataSource = new HashMap<>();

    /**
     * 增加新的数据源配置
     * @param name
     * @param dataSource
     */
    public static void addDataSource(String name, DataSource dataSource){
        if (StringUtils.isNullOrEmpty(name) || null == dataSource){
            return;
        }
        targetDataSource.put(name, dataSource);
    }


    /**
     * 主数据源，由多数据源配置管理
     * @return
     */
    @Bean(name = "dataSource")
    @Primary
    public DataSource dataSource() {
        HikariDataSource mainDataSource = defaultDataSource();
        if (multipleDataSource){
            targetDataSource.put(ThreadLocalDynamicDataSource.KEY_DEFAULT_DATASOURCE, mainDataSource);

            ThreadLocalDynamicDataSource dynamicDataSource = new ThreadLocalDynamicDataSource();
            dynamicDataSource.setTargetDataSources(targetDataSource);
            dynamicDataSource.setDefaultTargetDataSource(mainDataSource);

            return dynamicDataSource;
        }
        return new LazyConnectionDataSourceProxy(mainDataSource);
    }

    /**
     * 默认数据源
     * @return
     */
    @Bean(destroyMethod = "close", name = "defaultDataSource")
    public HikariDataSource defaultDataSource() {
        HikariDataSource dataSource = new HikariDataSource(defaultDataConfig());
        return dataSource;
    }

    /**
     * 默认数据源 配置
     * @return
     */
    @Bean
    public HikariConfig defaultDataConfig(){
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(jdbcDriverClass);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUsername);
        config.setPassword(jdbcPassword);
        config.setConnectionTestQuery(connectionTestQuery);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumIdle);
        config.setAutoCommit(autoCommit);
        return config;
    }
}
