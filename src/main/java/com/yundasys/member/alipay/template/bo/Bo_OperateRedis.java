package com.yundasys.member.alipay.template.bo;

import java.io.Serializable;

import com.yundasys.member.alipay.template.annonation.ValidNullString;

import lombok.Data;

@Data
public class Bo_OperateRedis implements Serializable {

	private static final long serialVersionUID = 1L;

	@ValidNullString(message = "The type is null or empty!!")
	private String type;
	
	@ValidNullString(message = "The key is null or empty!!")
	private String key;
	
	private String value;

}