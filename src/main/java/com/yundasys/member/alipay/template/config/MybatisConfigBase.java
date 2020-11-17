package com.yundasys.member.alipay.template.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfigBase {
	
	@Value("${common.datasource.initialSize}")
	public int initialSize;

	@Value("${common.datasource.minIdle}")
	public int minIdle;

	@Value("${common.datasource.maxActive}")
	public int maxActive;

	@Value("${common.datasource.maxWait}")
	public int maxWait;

	@Value("${common.datasource.timeBetweenEvictionRunsMillis}")
	public int timeBetweenEvictionRunsMillis;

	@Value("${common.datasource.minEvictableIdleTimeMillis}")
	public int minEvictableIdleTimeMillis;

	@Value("${common.datasource.validationQuery}")
	public String validationQuery;

	@Value("${common.datasource.testWhileIdle}")
	public boolean testWhileIdle;

	@Value("${common.datasource.testOnBorrow}")
	public boolean testOnBorrow;

	@Value("${common.datasource.testOnReturn}")
	public boolean testOnReturn;

	@Value("${common.datasource.poolPreparedStatements}")
	public boolean poolPreparedStatements;

	@Value("${common.datasource.maxPoolPreparedStatementPerConnectionSize}")
	public int maxPoolPreparedStatementPerConnectionSize;
	
}
