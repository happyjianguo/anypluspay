package com.anypluspay.admin.account;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * @author wxj
 * 04/1/6
 */
@Configuration
@MapperScan(basePackages = {"com.anypluspay.account.infra.persistence.mapper", "com.anypluspay.admin.account.mapper"}, sqlSessionFactoryRef = "accountSqlSessionFactory")
public class AccountDataSourceConfigure {

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();


    @Bean(name = "accountDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.account")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "accountSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("accountDataSource") DataSource dataSource, MybatisPlusInterceptor mybatisPlusInterceptor) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        mybatisConfiguration.addInterceptor(mybatisPlusInterceptor);

        factoryBean.setConfiguration(mybatisConfiguration);
        factoryBean.setMapperLocations(resourceResolver.getResources("classpath*:/mapper/account/*.xml"));

        return factoryBean.getObject();
    }

    @Bean(name = "accountTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("accountDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "accountTransactionTemplate")
    public TransactionTemplate transactionTemplate(@Qualifier("accountTransactionManager") PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }

}
