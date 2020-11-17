package com.yundasys.member.alipay.template.config;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis多实例配置
 */
@Configuration
public class RedisConfigBase extends CachingConfigurerSupport {
	
    @Value("${spring.redis.lettuce.pool.max-active}")
    private int redisPoolMaxActive;
    @Value("${spring.redis.lettuce.pool.max-idle}")
    private int redisPoolMaxIdle;
    @Value("${spring.redis.lettuce.pool.min-idle}")
    private int redisPoolMinIdle;
    @Value("${spring.redis.lettuce.pool.max-wait}")
    private int redisPoolMaxWait;
    @Value("${spring.redis.cluster.nodes}")
    private String nodes;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.cluster.max-redirects}")
    private int maxRedirects;
    
    
    @Bean
    public LettuceConnectionFactory connectionFactory() {
    	GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
    	poolConfig.setMaxIdle(redisPoolMaxIdle);
    	poolConfig.setMinIdle(redisPoolMinIdle);
    	poolConfig.setMaxWaitMillis(redisPoolMaxWait);
    	poolConfig.setMaxTotal(redisPoolMaxActive);
    	
    	Map<String, Object> source = new HashMap<>(8);
    	source.put("spring.redis.cluster.nodes", nodes);
    	RedisClusterConfiguration redisConfig = new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
    	redisConfig.setPassword(RedisPassword.of(password));
    	redisConfig.setMaxRedirects(maxRedirects);
    	LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(poolConfig).build();
    	return new LettuceConnectionFactory(redisConfig, clientConfiguration);
    }
    
    
    @Bean
    public RedisTemplate<Object,Object> redisTemplate() {
    	RedisTemplate<Object,Object> template = new RedisTemplate<Object,Object>();
        template.setConnectionFactory(connectionFactory());
        
        // 把RedisTemplate和StringRedisTemplate
 		// 就算可以相同也不能互相删除，因为采用的key序列化方法不一样，此处统一将两个模版的key序列方法统一
 		template.setKeySerializer(new StringRedisSerializer());
 		template.setHashKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
    
    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory());
        return template;
    }
    

}