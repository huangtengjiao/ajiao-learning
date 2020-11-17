package com.yundasys.member.alipay.template.annonation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.yundasys.member.alipay.template.bo.validator.MobileValidator;

@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
// 指定验证器
@Constraint(validatedBy = MobileValidator.class)
@Documented
public @interface ValidMobile {

	// 默认错误消息
	String message() default "手机号码不合法";

	// 分组
	Class<?>[] groups() default {};

	// 负载
	Class<? extends Payload>[] payload() default {};

}
