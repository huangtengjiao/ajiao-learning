package com.yundasys.member.alipay.template.bo.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

import com.yundasys.member.alipay.template.annonation.ValidMobile;

//自定义校验器，手机号验证
//例子 :在bo类需要是手机号码字段上 @ValidMobileNo
public class MobileValidator implements ConstraintValidator<ValidMobile, String> {

	@Override
	public void initialize(ValidMobile constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isEmpty(value)) {
			return false;
		}
		// 11位，开通为1
		return Pattern.compile("^1[3|4|5|7|8][0-9]{9}$").matcher(value).matches();
	}
}