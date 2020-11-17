package com.yundasys.member.alipay.template.jobhandler;




import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.yundasys.member.alipay.template.constant.GlobalConstant;


/**
 * 用于xxl-job中编写在线glue代码,直接线上编码容易忘导包,线上执行编译失败
 * @Description: 
 * @author htj 
 * @date 2020年1月19日
 */
public class GlueHandler extends IJobHandler {
	
	@Autowired
	StringRedisTemplate srt;
	
	@Override
	public ReturnT<String> execute(String date) throws Exception {
		try {
			Long initsize = srt.opsForList().size(GlobalConstant.SEND_TEMPLATE_LIST_KEY);
			XxlJobLogger.log("初始化size === "+initsize);
    		srt.opsForList().leftPushAll(GlobalConstant.SEND_TEMPLATE_LIST_KEY, 
    				"alipay_userid0",
    				"alipay_userid1",
    				"alipay_userid2",
    				"alipay_userid3",
    				"alipay_userid4",
    				"alipay_userid5",
    				"alipay_userid6",
    				"alipay_userid7",
    				"alipay_userid8",
    				"alipay_userid9"
    				);
    		Long finalsize = srt.opsForList().size(GlobalConstant.SEND_TEMPLATE_LIST_KEY);
    		XxlJobLogger.log("最终size === "+finalsize);
    		List<String> range = srt.opsForList().range(GlobalConstant.SEND_TEMPLATE_LIST_KEY, 0, -1);
    		XxlJobLogger.log("range === "+range.toString());
    		Long lastsize = srt.opsForList().size(GlobalConstant.SEND_TEMPLATE_LIST_KEY);
    		XxlJobLogger.log("lastsize === "+lastsize);
			
			
		}catch (Exception e) {
			XxlJobLogger.log("缓存值或者代码执行失败 ---------- "+ e.getMessage(),e);
			return FAIL;
		}
		return SUCCESS;
	}
	


}
