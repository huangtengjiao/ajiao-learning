package com.yundasys.member.alipay.template.bo.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

import com.yundasys.member.alipay.template.annonation.ValidPhone;

//自定义校验器，电话号验证，座机号
//例子 :在bo类需要是手机号码字段上 @ValidPhone
public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

	@Override
	public void initialize(ValidPhone constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isEmpty(value)) {
			return false;
		}
		// 11位，开通为1
		return Pattern.compile("^0\\d{2,3}-\\d{7,8}$").matcher(value).matches();
	}

}
