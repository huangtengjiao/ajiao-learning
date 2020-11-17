package com.yundasys.member.alipay.template.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 
    /**
     * 忽略安全配置, 添加匹配的地址
     * 为解决Spring Security 报 403的问题添加
     * @param web 安全设置对象
     * @throws Exception 异常
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 忽略匹配项
        web.ignoring().antMatchers("/", "/alipayTemplate/**");
        super.configure(web);
    }
}
