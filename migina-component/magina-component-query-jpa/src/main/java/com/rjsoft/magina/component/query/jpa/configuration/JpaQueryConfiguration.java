package com.rjsoft.magina.component.query.jpa.configuration;


import com.rjsoft.magina.component.query.sql.configuration.base.AbstractConfiguration;
import com.rjsoft.magina.component.query.sql.configuration.base.OrderedStep;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;

@Configuration
@Order(OrderedStep.FIRST)
@PropertySource("classpath:com/rjsoft/magina/config/jpa.properties")
public class JpaQueryConfiguration extends AbstractConfiguration {

    /**
     * 实体类生成数据库模式
     * create:表示启动先drop，然后create
     * create-update:与create一致，关闭前执行drop
     * update:检查schema是否一致，不一致做更新
     * calidate:检查schema是否一致，不一致抛出异常
     * 默认：update
     */
    @Value("${magina.query.jpa.hibernate.hbm2ddl.auto}")
    protected String hibernateHbm2ddlAuto;

    /**
     * Hibernate 使用的hql方言
     * 默认：org.hibernate.dialect.MySQL5Dialect
     */
    @Value("${magina.query.jpa.hibernate.dialect}")
    protected String hibernateDialect;

    /**
     * 是否打印sql或hql
     * 默认：true
     */
    @Value("${magina.query.jpa.hibernate.showSql}")
    protected Boolean hibernateShowSql;

    /**
     * 是否格式化sql或hql
     * 默认：true
     */
    @Value("${magina.query.jpa.hibernate.formatSql}")
    protected Boolean hibernateFormatSql;

    /**
     *
     * 默认：org.springframework.orm.hibernate5.SpringSessionContext
     */
    @Value("${magina.query.jpa.hibernate.currentSessionContextClass}")
    protected String hibernateCurrentSessionContextClass;

    /**
     *
     * 默认：none
     */
    @Value("${magina.query.jpa.javax.persistence.validation.mode}")
    protected String javaxPersistenceValidationMode;

    /**
     *
     * 默认：true 1, false 0
     */
    @Value("${magina.query.jpa.hibernate.query.substitutions}")
    protected String hibernateQuerySubstitutions;

    /**
     *
     * 默认：magina
     */
    @Value("${magina.query.jpa.hibernate.defaultSchema}")
    protected String hibernateDefaultSchema;

    /**
     *
     * 默认：50
     */
    @Value("${magina.query.jpa.hibernate.jdbc.batchSize}")
    protected Integer hibernateJDBCBatchSize;

    /**
     *
     * 默认：16
     */
    @Value("${magina.query.jpa.hibernate.defaultBatchFetchSize}")
    protected Integer hibernateDefaultBatchFetchSize;

    /**
     *
     * 默认：4
     */
    @Value("${magina.query.jpa.hibernate.maxFetchDepth}")
    protected Integer hibernateMaxFetchDepth;

    /**
     *
     * 默认：true
     */
    @Value("${magina.query.jpa.hibernate.enableLazyLoadNoTrans}")
    protected Boolean hibernateEnableLazyLoadNoTrans;

    /**
     *
     * 默认：true
     */
    @Value("${magina.query.jpa.hibernate.bytecode.useReflectionOptimizer}")
    protected Boolean hibernateBytecodeUseReflectionOptimizer;

    /**
     *
     * 默认：false
     */
    @Value("${magina.query.jpa.hibernate.cache.useSecondLevelCache}")
    protected Boolean hibernateCacheUseSecondLevelCache;

    /**
     *
     * 默认：org.hibernate.cache.infinispan.InfinispanRegionFactory
     */
    @Value("${magina.query.jpa.hibernate.cache.region.factoryClass}")
    protected String hibernateCacheInfinispanRegionFactoryClass;

    /**
     *
     * 默认：/infinispan/infinispan.xml
     */
    @Value("${magina.query.jpa.hibernate.cache.infinispan.cfg}")
    protected String hibernateCacheInfinispanCfg;

    /**
     *
     * 默认：ALL
     */
    @Value("${magina.query.jpa.javax.persistence.sharedCache.mode}")
    protected String javaxPersistenceSharedCacheMode;

    /**
     *
     * 默认：false
     */
    @Value("${magina.query.jpa.hibernate.generateStatistics}")
    protected Boolean hibernateGenerateStatistics;

    /**
     *
     * 默认：false
     */
    @Value("${magina.query.jpa.hibernate.cache.useQueryCache}")
    protected Boolean hibernateCacheUseQueryCache;

    /**
     *
     * 默认：false
     */
    @Value("${magina.query.jpa.hibernate.temp.useJdbcMetadataDefaults}")
    protected Boolean hibernateUseJdbcMetadataDefaults;

    @Value("${magina.query.jpa.multiTenant}")
    protected Boolean multiTenant;
}
