package com.rjsoft.magina.component.query.jpa.configuration;

import com.rjsoft.magina.component.query.jpa.GenericJpaRepository;
import com.rjsoft.magina.component.query.jpa.GenericJpaRepositoryFactoryBean;
import com.rjsoft.magina.component.query.jpa.configuration.JpaQueryConfiguration;
import com.rjsoft.magina.component.query.sql.configuration.SqlRepositoryConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = {"com.rjsoft"},
        includeFilters = {
                @Filter(value = GenericJpaRepository.class, type = FilterType.ASSIGNABLE_TYPE)
        },
        repositoryImplementationPostfix = "Impl",
        repositoryFactoryBeanClass = GenericJpaRepositoryFactoryBean.class,
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
@EnableJpaAuditing
@EnableTransactionManagement(proxyTargetClass = true)
@Import(SqlRepositoryConfigBean.class)
public class JpaRepositoryConfigBean extends JpaQueryConfiguration {

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(Boolean.TRUE);
        vendorAdapter.setShowSql(Boolean.TRUE);

        factory.setDataSource(dataSource);
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.rjsoft.**.entity");
        factory.setJpaDialect(new HibernateJpaDialect());

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.id.new_generator_mappings", false);
        jpaProperties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        jpaProperties.put("hibernate.dialect", hibernateDialect);
        jpaProperties.put("hibernate.show_sql", hibernateShowSql);
        jpaProperties.put("hibernate.format_sql", hibernateFormatSql);
        jpaProperties.put("hibernate.current_session_context_class", hibernateCurrentSessionContextClass);


        //jpaProperties.put("hibernate.default_schema", hibernateDefaultSchema);
        jpaProperties.put("javax.persistence.validation.mode", javaxPersistenceValidationMode);
        jpaProperties.put("hibernate.query.substitutions", hibernateQuerySubstitutions);
        jpaProperties.put("hibernate.jdbc.batch_size", hibernateJDBCBatchSize);
        jpaProperties.put("hibernate.default_batch_fetch_size", hibernateDefaultBatchFetchSize);
        jpaProperties.put("hibernate.max_fetch_depth", hibernateMaxFetchDepth);
        jpaProperties.put("hibernate.enable_lazy_load_no_trans", hibernateEnableLazyLoadNoTrans);
        jpaProperties.put("hibernate.bytecode.use_reflection_optimizer", hibernateBytecodeUseReflectionOptimizer);

        jpaProperties.put("hibernate.cache.use_second_level_cache", hibernateCacheUseSecondLevelCache);
        jpaProperties.put("hibernate.cache.region.factory_class", hibernateCacheInfinispanRegionFactoryClass);
        jpaProperties.put("hibernate.cache.infinispan.cfg", hibernateCacheInfinispanCfg);
        jpaProperties.put("javax.persistence.sharedCache.mode", javaxPersistenceSharedCacheMode);
        jpaProperties.put("hibernate.generate_statistics", hibernateGenerateStatistics);

        jpaProperties.put("hibernate.cache.use_query_cache", hibernateCacheUseQueryCache);
        jpaProperties.put("hibernate.temp.use_jdbc_metatdata_defaults", hibernateUseJdbcMetadataDefaults);
        factory.setJpaProperties(jpaProperties);

        return factory;
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

}
