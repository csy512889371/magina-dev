package com.rjsoft.magina.component.query.sql.configuration;


import com.rjsoft.magina.component.query.sql.configuration.base.AbstractConfiguration;
import com.rjsoft.magina.component.query.sql.configuration.base.OrderedStep;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;


@Configuration
@Order(OrderedStep.FIRST)
@PropertySource("classpath:com/rjsoft/magina/config/sql.properties")
public class SqlQueryConfiguration extends AbstractConfiguration {

    /**
     * 使用多数据源
     * 默认：false
     */
    @Value("${magina.query.sql.jdbc.multipleDataSource}")
    protected Boolean multipleDataSource;

    /**
     * JDBC驱动包
     * 默认：com.mysql.jdbc.Driver
     */
    @Value("${magina.query.sql.jdbc.driverClass}")
    protected String jdbcDriverClass;

    /**
     * JDBC连接字
     * 默认：jdbc:mysql://127.0.0.1:3306/magina?useUnicode=true&characterEncoding=utf-8&useSSL=false
     */
    @Value("${magina.query.sql.jdbc.url}")
    protected String jdbcUrl;

    /**
     * 用户名
     * 默认：magina
     */
    @Value("${magina.query.sql.jdbc.username}")
    protected String jdbcUsername;

    /**
     * 密码
     * 默认：12345678
     */
    @Value("${magina.query.sql.jdbc.password}")
    protected String jdbcPassword;

    /**
     * 测试语句
     * 默认：SELECT 1
     */
    @Value("${magina.query.sql.connectionTestQuery}")
    protected String connectionTestQuery;

    /**
     * 连接超时时间
     * 默认：30000
     */
    @Value("${magina.query.sql.connectionTimeout}")
    protected Long connectionTimeout;

    /**
     * 空闲超时时间
     * 默认：600000
     */
    @Value("${magina.query.sql.idleTimeout}")
    protected Long idleTimeout;

    /**
     * 最大生存周期
     * 默认：1800000
     */
    @Value("${magina.query.sql.maxLifetime}")
    protected Long maxLifetime;

    /**
     * 连接池最大数
     * 默认：15
     */
    @Value("${magina.query.sql.maximumPoolSize}")
    protected Integer maximumPoolSize;

    /**
     * 最小空余数
     * 默认：15
     */
    @Value("${magina.query.sql.minimumIdle}")
    protected Integer minimumIdle;

    /**
     * 自动提交
     * 默认：true
     */
    @Value("${magina.query.sql.autoCommit}")
    protected Boolean autoCommit;
}
