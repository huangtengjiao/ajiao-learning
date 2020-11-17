package com.yundasys.member.alipay.template.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yundasys.member.alipay.template.dao.AlipayTemplateDao;
import com.yundasys.member.alipay.template.domain.AlipayUserid;
import com.yundasys.member.alipay.template.service.AlipayTemplateService;

@Service
public class AlipayTemplateServiceImpl implements AlipayTemplateService {

	
    @Autowired
    AlipayTemplateDao dao;
    
	@Override
	public void saveSelfDefine(String sql) {
		dao.saveSelfDefine(sql);
	}


	@Override
	public void updateSelfDefine(String sql) {
		dao.updateSelfDefine(sql);
	}


	@Override
	public void deleteSelfDefine(String sql) {
		dao.deleteSelfDefine(sql);
	}


	@Override
	public List<LinkedHashMap<String, Object>> selectSelfDefine(String sql) {
		return dao.selectSelfDefine(sql);
	}


	@Override
	public void saveAlipayUserids(String index, String userids) {
		dao.saveAlipayUserids(index, userids);
	}


	@Override
	public int countTable(String table) {
		return dao.countTable(table);
	}


	@Override
	public AlipayUserid getUserid(String table, int from) {
		return dao.getUserid(table, from);
	}

}
