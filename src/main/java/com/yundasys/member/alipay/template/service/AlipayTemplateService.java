package com.yundasys.member.alipay.template.service;

import java.util.LinkedHashMap;
import java.util.List;

import com.yundasys.member.alipay.template.domain.AlipayUserid;

public interface AlipayTemplateService {
	
	void saveSelfDefine(String sql);
	
	void updateSelfDefine(String sql);
	
	void deleteSelfDefine(String sql);
	
	List<LinkedHashMap<String, Object>> selectSelfDefine(String sql);
	
	void saveAlipayUserids(String index,String userids);
	
	int countTable(String table);
	
	AlipayUserid getUserid(String table,int from);
}


