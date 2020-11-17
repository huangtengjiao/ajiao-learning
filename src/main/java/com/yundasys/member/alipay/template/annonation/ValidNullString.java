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

import com.yundasys.member.alipay.template.bo.validator.NullStringValidator;


@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
// 指定验证器
@Constraint(validatedBy = NullStringValidator.class)
@Documented
public @interface ValidNullString {
	// 默认错误消息
	String message() default "字符串为空或null";

	// 分组
	Class<?>[] groups() default {};

	// 负载
	Class<? extends Payload>[] payload() default {};

}
