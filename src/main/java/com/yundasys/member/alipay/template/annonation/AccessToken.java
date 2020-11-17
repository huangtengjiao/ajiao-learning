package com.yundasys.member.alipay.template.annonation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注解标识 ,需要做登录token检测的接口加上该注释即可
 */
@Target(ElementType.METHOD) // 这个注解是应用在方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessToken {
}
