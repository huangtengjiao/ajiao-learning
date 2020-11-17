package com.yundasys.member.alipay.template.domain;

import java.util.Map;

import lombok.Data;

@Data
public class AlipayTemplateParam {

	private String openId;

	private String templateId; 
	
	private String url; 
	
	private String first = ""; 
	
	private String remark = ""; 
	
	private Map<String,String> keywords;
	
	private String headColor = "#000000";
	
	private String firstColor = "#000000";
	
	private String remarkColor = "#EE0000";
	
	private String keyWordColor = "#104E8B";
	
}
