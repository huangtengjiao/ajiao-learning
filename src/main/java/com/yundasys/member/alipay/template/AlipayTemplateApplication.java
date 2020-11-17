package com.yundasys.member.alipay.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

// 启动swagger注解
@EnableSwagger2
@SpringBootApplication
// 扫包filter，servlet，listener
@ServletComponentScan
public class AlipayTemplateApplication {
	public static void main(String[] args) {
		SpringApplication.run(AlipayTemplateApplication.class, args);
		System.out.println("ヾ(◍°∇°◍)ﾉﾞ    【研二-客服-会员-支付宝模板服务】启动成功      ヾ(◍°∇°◍)ﾉﾞ");
	}
}
