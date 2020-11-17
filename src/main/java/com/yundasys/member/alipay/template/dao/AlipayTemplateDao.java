package com.yundasys.member.alipay.template.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yundasys.member.alipay.template.domain.AlipayUserid;

public interface AlipayTemplateDao {
	
	void saveSelfDefine(String sql);
	
	void updateSelfDefine(String sql);
	
	void deleteSelfDefine(String sql);
	
	List<LinkedHashMap<String, Object>> selectSelfDefine(String sql);
	
	void saveAlipayUserids(@Param("index")String index,@Param("userids")String userids);
	
	int countTable(@Param("table")String table);
	
	AlipayUserid getUserid(@Param("table")String table,@Param("from")int from);
	
}
