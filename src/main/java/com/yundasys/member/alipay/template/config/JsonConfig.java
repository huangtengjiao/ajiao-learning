package com.yundasys.member.alipay.template.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

//对 Jackson 的序列化行为进行定制
@Configuration
public class JsonConfig {
	@Bean
	public ObjectMapper ObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		// 忽略json字符串中不识别的属性
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 忽略无法转换的对象 “No serializer found for class com.xxx.xxx”
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// 不显示为null的字段
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

/*		// 驼峰命名法转换为小写加下划线，例如cityName输入为city_name
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
*/
		return objectMapper;
	}
}
