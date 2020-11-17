package com.yundasys.member.alipay.template.bo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.yundasys.member.alipay.template.annonation.ValidNullString;


//字符串不能为null（指的是字符串的内容）
public class NullStringValidator implements ConstraintValidator<ValidNullString, String> {

	@Override
	public void initialize(ValidNullString constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value) || value.equalsIgnoreCase("null")) {
			return false;
		}

		return true;
	}
	
}