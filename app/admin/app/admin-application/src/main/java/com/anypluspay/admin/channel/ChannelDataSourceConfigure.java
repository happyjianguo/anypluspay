package com.anypluspay.admin.channel;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
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
 * 2024/12/26
 */
@Configuration
@MapperScan(basePackages = {"com.anypluspay.channel.infra.persistence.mapper", "com.anypluspay.admin.channel.dao.mapper"}, sqlSessionFactoryRef = "channelSqlSessionFactory")
public class ChannelDataSourceConfigure {

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    @Bean(name = "channelDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.channel")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "channelSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("channelDataSource") DataSource dataSource, MybatisPlusInterceptor mybatisPlusInterceptor,
                                              @Qualifier("autoFillMetaObjectHandler") MetaObjectHandler autoFillMetaObjectHandler) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(autoFillMetaObjectHandler); // 手动注入 MetaObjectHandler
        factoryBean.setGlobalConfig(globalConfig);

        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        mybatisConfiguration.addInterceptor(mybatisPlusInterceptor);

        factoryBean.setConfiguration(mybatisConfiguration);
        factoryBean.setMapperLocations(resourceResolver.getResources("classpath*:/mapper/channel/*.xml"));

        return factoryBean.getObject();
    }

    @Bean(name = "channelTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("channelDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "channelTransactionTemplate")
    public TransactionTemplate transactionTemplate(@Qualifier("channelTransactionManager") PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}
