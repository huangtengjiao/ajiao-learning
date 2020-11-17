package com.yundasys.member.alipay.template.jobhandler;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenPublicFollowBatchqueryRequest;
import com.alipay.api.response.AlipayOpenPublicFollowBatchqueryResponse;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.yundasys.member.alipay.template.constant.GlobalConstant;
import com.yundasys.member.alipay.template.service.AlipayTemplateService;


/**
 * 
 * @Description: 日更新alipay用户id 
 * @author htj 
 * @date 2020年7月6日
 */
@JobHandler(value="updateAlipayUseridHandler")
@Component
public class UpdateAlipayUseridHandler extends IJobHandler {
	
	@Autowired
	StringRedisTemplate srt;
	
    @Autowired
    AlipayTemplateService templateService;
    
	@Value("${appId}")
	private String  appId;
	
	@Value("${privateKey}")
	private String  privateKey;
	
	@Value("${alipayPublicKey}")
	private String  alipayPublicKey;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			String tempId = srt.opsForValue().get(GlobalConstant.NEXT_USER_ID_KEY);
			if(StringUtils.isBlank(tempId)) {
				XxlJobLogger.log("没有初始化alipay用户,不执行日更新任务....................");
				return FAIL;
			}
			XxlJobLogger.log("日更新alipay用户id任务开始执行 ....................");
			long l = System.currentTimeMillis();
			
			//if next_user_id is null ,stop
			while(true){
				String nextUserId = srt.opsForValue().get(GlobalConstant.NEXT_USER_ID_KEY);
				AlipayClient alipayClient = new DefaultAlipayClient(GlobalConstant.ALIPAY_GATEWAY, appId, 
						privateKey, "json","GBK" ,alipayPublicKey, "RSA");
				AlipayOpenPublicFollowBatchqueryRequest request = new AlipayOpenPublicFollowBatchqueryRequest();
				request.setBizContent("{\"next_user_id\":\""+nextUserId+"\"}");
				AlipayOpenPublicFollowBatchqueryResponse response = alipayClient.execute(request);
				if (response.isSuccess()) {
					JSONObject resJson = JSONObject.parseObject(response.getBody());
					JSONArray userids = ((JSONObject)((JSONObject)resJson.get("alipay_open_public_follow_batchquery_response"))
					.get("data")).getJSONArray("user_id_list");
					if(userids != null && userids.size()>0) {
						List<String> arr = JSONObject.parseArray(userids.toJSONString(), String.class);
						int start = 0;
						while(true){
							int interval = 100;
							int size = arr.size();
							if(start < size) {
								int end = start + interval;
								if(end > size) {
									end = size;
								}
								List<String> sublist = arr.subList(start, end);
								String userid = sublist.get(0);
								String index = String.valueOf(userid.charAt(userid.length()-1));
								templateService.saveAlipayUserids(index, "'" + String.join(",",sublist) + "'");
								start = start + interval;
							}else {
								break;
							}
						}
						if(StringUtils.isNotBlank(response.getNextUserId())) {
							srt.opsForValue().set(GlobalConstant.NEXT_USER_ID_KEY,response.getNextUserId());
						}else {
							//手动更新next_user_id.防止下次更新拿的是上次的userid,造成数据重复
							//已验证获取的userid有序,手动userid为集合最后一位
							srt.opsForValue().set(GlobalConstant.NEXT_USER_ID_KEY,arr.get(arr.size()-1));
							XxlJobLogger.log("next userid is null,result = {}",response.getBody());
				        	break;
						}
					}else {
						XxlJobLogger.log("last userid,result = {}",response.getBody());
			        	break;
					}
		        } else {
		        	XxlJobLogger.log("get alipay userid fail,code = {},msg = {},sub_msg = {},sub_code = {}",response.getCode(),
		        			response.getMsg(),response.getSubMsg(),response.getSubCode());
		        	break;
		        }
			}
			long dt = (System.currentTimeMillis() - l)/1000;
			XxlJobLogger.log("任务完美成功........耗时 dt = {}秒",dt);
		}catch (Exception e) {
			XxlJobLogger.log("任务失败.............",e);
			return FAIL;
		}
		return SUCCESS;
	}
	
	
	/**
	 * 
	 *@Title: getAlipayUserids
	 *@Description: 日更新alipay关注者userid,并存储
	 *@author: htj
	 * @throws AlipayApiException 
	 *@date: 2020年7月6日
	 */
	public void getAlipayUserids() throws AlipayApiException {
    	String nextUserId = srt.opsForValue().get(GlobalConstant.NEXT_USER_ID_KEY);
		AlipayClient alipayClient = new DefaultAlipayClient(GlobalConstant.ALIPAY_GATEWAY, appId, 
				privateKey, "json","GBK" ,alipayPublicKey, "RSA");
		AlipayOpenPublicFollowBatchqueryRequest request = new AlipayOpenPublicFollowBatchqueryRequest();
		request.setBizContent("{\"next_user_id\":\""+nextUserId+"\"}");
		AlipayOpenPublicFollowBatchqueryResponse response = alipayClient.execute(request);
		if (response.isSuccess()) {
			JSONObject resJson = JSONObject.parseObject(response.getBody());
			JSONArray userids = ((JSONObject)((JSONObject)resJson.get("alipay_open_public_follow_batchquery_response"))
			.get("data")).getJSONArray("user_id_list");
			if(userids != null && userids.size()>0) {
				List<String> arr = JSONObject.parseArray(userids.toJSONString(), String.class);
				saveAlipayUserids(0, arr);
				if(StringUtils.isNotBlank(response.getNextUserId())) {
					srt.opsForValue().set(GlobalConstant.NEXT_USER_ID_KEY,response.getNextUserId());
					getAlipayUserids();
				}
			}
        } else {
        	XxlJobLogger.log("get alipay userid fail,code = {},msg = {},sub_msg = {},sub_code = {}",response.getCode(),
        			response.getMsg(),response.getSubMsg(),response.getSubCode());
        }
	}
    
    /**
     * 
     *@Title: saveAlipayUserids
     *@Description: 以100为单位分割大集合分库存储
     *@param start
     *@param arr
     *@author: htj
     *@date: 2020年7月6日
     */
	public void saveAlipayUserids(int start, List<String> arr) {
		int interval = 100;
		int size = arr.size();
		if(start < size) {
			int end = start + interval;
			if(end > size) {
				end = size;
			}
			List<String> sublist = arr.subList(start, end);
			String userid = sublist.get(0);
			String index = String.valueOf(userid.charAt(userid.length()-1));
			templateService.saveAlipayUserids(index, "'" + String.join(",",sublist) + "'");
			saveAlipayUserids(start + interval,arr);
		}
	}
    


}
