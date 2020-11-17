package com.yundasys.member.alipay.template.bo;

import java.io.Serializable;

import com.yundasys.member.alipay.template.annonation.ValidNullString;

import lombok.Data;

@Data
public class Bo_OperateDatabase implements Serializable {

	private static final long serialVersionUID = 1L;

	@ValidNullString(message = "The type is null or empty!!")
	private String type;
	
	@ValidNullString(message = "The sql is null or empty!!")
	private String sql;
	
	@ValidNullString(message = "The sign is null or empty!!")
	private String sign;

}
