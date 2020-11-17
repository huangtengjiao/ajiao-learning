package com.yundasys.member.alipay.template.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.Api;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//使用原生ui 访问 http://localhost:8000/swagger-ui.html 即可访问swagger页面
//使用bootstrap-ui后 访问 http://localhost:8000/doc.html ，即可访问swagger页面
@Configuration
// 启用Swagger2
@EnableSwagger2
public class Swagger2Config {
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
    			//只有加了api注释的才会被swagger显示
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
				.build();
	}

	// 创建该Api的基本信息
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("支付宝模板项目接口说明").description("提供支付宝服务窗模版消息服务").version("1.0")
				.build();
	}
}
