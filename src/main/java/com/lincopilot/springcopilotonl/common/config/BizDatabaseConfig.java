package com.lincopilot.springcopilotonl.common.config;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.lincopilot.springcopilotonl.common.component.SystemUtils;
import com.lincopilot.springcopilotonl.common.constants.Constants;



@Configuration
@MapperScan(value="com.lincopilot.springcopilotonl.**.dao", sqlSessionFactoryRef="bizSqlSessionFactory")
public class BizDatabaseConfig {

	@Autowired
	private Environment env;
	
	//mybatis 설정
	private String mapperLocations = "classpath:mapper/**/*.xml";
	@Value("${mybatis.type-aliases-package:com.lincopilot.springcopilotonl.**.dto*}")
	private String typeAliasesPackage;
	@Value("${mybatis.config-location:classpath:mybatis/mybatis-config.xml}")
	private String configLocation;

    @Bean(name = "bizDataSource", destroyMethod = "")
    public DataSource bizJndiDataSource() throws IllegalArgumentException, NamingException {
    	JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName(env.getProperty(Constants.JDBC_JNDI_LOAN));
        bean.setResourceRef(SystemUtils.isLocal());
        bean.setProxyInterface(DataSource.class);
        bean.setLookupOnStartup(false);
        bean.afterPropertiesSet();
        return (DataSource)bean.getObject();
    }
    
    // @Bean(name = "infoDataSource", destroyMethod = "")
    // public DataSource infoJndiDataSource() throws IllegalArgumentException, NamingException {
    // 	JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
    //     bean.setJndiName(env.getProperty(Constants.JDBC_JNDI_INFO));
    //     bean.setResourceRef(SystemUtils.isLocal());
    //     bean.setProxyInterface(DataSource.class);
    //     bean.setLookupOnStartup(false);
    //     bean.afterPropertiesSet();
    //     return (DataSource)bean.getObject();
    // }
    
    // @Bean(name = "fcubsDataSource", destroyMethod = "")
    // public DataSource fcubsJndiDataSource() throws IllegalArgumentException, NamingException {
    // 	JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
    //     bean.setJndiName(env.getProperty(Constants.JDBC_JNDI_FCUB));
    //     bean.setResourceRef(SystemUtils.isLocal());
    //     bean.setProxyInterface(DataSource.class);
    //     bean.setLookupOnStartup(false);
    //     bean.afterPropertiesSet();
    //     return (DataSource)bean.getObject();
    // }


    @Bean(name = "bizSqlSessionFactory")
    public SqlSessionFactory bizSqlSessionFactory(@Qualifier("bizDataSource") DataSource bizDataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(bizDataSource);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources(mapperLocations));
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
		sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource(configLocation));

        sqlSessionFactoryBean.setPlugins( new MybatisInterceptor());

        return sqlSessionFactoryBean.getObject();
    }
    
    // @Bean(name = "infoSqlSessionFactory")
    // public SqlSessionFactory infoSqlSessionFactory(@Qualifier("infoDataSource") DataSource bizDataSource, ApplicationContext applicationContext) throws Exception {
    //     SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    //     sqlSessionFactoryBean.setDataSource(bizDataSource);
    //     sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources(mapperLocations));
    //     sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
	// 	sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource(configLocation));

    //     sqlSessionFactoryBean.setPlugins( new MybatisInterceptor());

    //     return sqlSessionFactoryBean.getObject();
    // }
    
    // @Bean(name = "fcubsSqlSessionFactory")
    // public SqlSessionFactory fcubsSqlSessionFactory(@Qualifier("fcubsDataSource") DataSource bizDataSource, ApplicationContext applicationContext) throws Exception {
    //     SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    //     sqlSessionFactoryBean.setDataSource(bizDataSource);
    //     sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources(mapperLocations));
    //     sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
	// 	sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource(configLocation));

    //     sqlSessionFactoryBean.setPlugins( new MybatisInterceptor());

    //     return sqlSessionFactoryBean.getObject();
    // }

    @Bean(name = "bizSqlSessionTemplate")
    public SqlSessionTemplate bizSqlSessionTemplate(SqlSessionFactory bizSqlSessionFactory) {

        return new SqlSessionTemplate(bizSqlSessionFactory);
    }

    @Bean
    public PlatformTransactionManager bizTxManager(DataSource bizDataSource) {
        return new DataSourceTransactionManager(bizDataSource);
    }
}
