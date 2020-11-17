package com.yundasys.member.alipay.template.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@MapperScan(basePackages = {"com.yundasys.member.alipay.template.dao"},sqlSessionTemplateRef = "alipaySqlSessionTemplate" )
public class AlipayMyBatisConfig extends MybatisConfigBase {
	
	@Value("${alipay.datasource.url}")
	private String dbUrl;

	@Value("${alipay.datasource.username}")
	private String username;

	@Value("${alipay.datasource.password}")
	private String password;

	@Value("${alipay.datasource.driverClassName}")
	private String driverClassName;

	@Bean 
	@Primary
	public DataSource alipayDataSource(){
		DruidDataSource datasource = new DruidDataSource();

		datasource.setUrl(this.dbUrl);
		datasource.setUsername(username);
		datasource.setPassword(password);
		datasource.setDriverClassName(driverClassName);

		// configuration
		datasource.setInitialSize(initialSize);
		datasource.setMinIdle(minIdle);
		datasource.setMaxActive(maxActive);
		datasource.setMaxWait(maxWait);
		datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		datasource.setValidationQuery(validationQuery);
		datasource.setTestWhileIdle(testWhileIdle);
		datasource.setTestOnBorrow(testOnBorrow);
		datasource.setTestOnReturn(testOnReturn);
		datasource.setPoolPreparedStatements(poolPreparedStatements);
		datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		return datasource;
	}
	
	
	@Bean
	@Primary
	public SqlSessionFactory alipaySqlSessionFactory()throws Exception{
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(alipayDataSource());
		bean.setTypeAliasesPackage("com.yundasys.member.alipay.template.domain");
		// 配置mybatis运行时参数
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        // 自动将数据库中的下划线转换为驼峰格式
        configuration.setMapUnderscoreToCamelCase(true);
        bean.setConfiguration(configuration);
		//添加xml目录
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try{
			bean.setMapperLocations(resolver.getResources("classpath*:mapper/*Mapper.xml"));
			return bean.getObject();
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	@Bean
	@Primary
	public SqlSessionTemplate alipaySqlSessionTemplate() throws Exception {
		//使用上面配置的Factory
		SqlSessionTemplate template = new SqlSessionTemplate(alipaySqlSessionFactory());
		return template;
	}
	
	
	
}
