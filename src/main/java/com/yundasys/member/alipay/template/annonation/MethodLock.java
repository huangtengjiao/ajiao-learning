package com.yundasys.member.alipay.template.annonation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//防止重复提交
@Target(ElementType.METHOD) // 这个注解是应用在方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodLock {
	// 用于识别重复的key,必填，否则不加锁
	String key();

	// 锁时间
	int lockSeconds() default 60;
}
