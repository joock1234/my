package com.blue.admin.web.config.mybatis;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.pagehelper.PageHelper;

@Configuration
public class MyBatisConfig {

	@Autowired
	private DataSource dataSource;

	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactoryBean sqlSessionFactory(
			ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);

		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		configuration.setLogImpl(org.apache.ibatis.logging.log4j.Log4jImpl.class);//use log4j log
		sessionFactory.setConfiguration(configuration);
		sessionFactory.setMapperLocations(applicationContext.getResources("classpath:mapper/*.xml"));
//
//		Properties prop = new Properties();
//		prop.setProperty("supportMethodsArguments","true");
//		prop.setProperty("rowBoundsWithCount", "true");
//		prop.setProperty("params","pageNum=pageNum;pageSize=pageSize;");
//		PageInterceptor pi = new PageInterceptor();
//		pi.setProperties(prop);
//		sessionFactory.setPlugins(new Interceptor[]{pi});

		PageHelper pageHelper=new PageHelper();
		Properties prop = new Properties();
		prop.setProperty("dialect", "mysql");
		prop.setProperty("offsetAsPageNum", "false");
		prop.setProperty("rowBoundsWithCount", "false");
		prop.setProperty("pageSizeZero", "true");
		prop.setProperty("reasonable", "false");
		prop.setProperty("supportMethodsArguments", "false");
		prop.setProperty("returnPageInfo", "none");

		pageHelper.setProperties(prop);

		sessionFactory.setPlugins(new Interceptor[]{pageHelper});

		return sessionFactory;
	}

}
