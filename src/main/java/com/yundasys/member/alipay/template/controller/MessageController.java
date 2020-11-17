package com.yundasys.member.alipay.template.controller;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenPublicFollowBatchqueryRequest;
import com.alipay.api.request.AlipayOpenPublicMessageSingleSendRequest;
import com.alipay.api.response.AlipayOpenPublicFollowBatchqueryResponse;
import com.alipay.api.response.AlipayOpenPublicMessageSingleSendResponse;
import com.xxl.job.core.log.XxlJobLogger;
import com.yundasys.member.alipay.template.bo.Bo_OperateDatabase;
import com.yundasys.member.alipay.template.bo.Bo_OperateRedis;
import com.yundasys.member.alipay.template.constant.GlobalConstant;
import com.yundasys.member.alipay.template.enums.OperateTypeEnum;
import com.yundasys.member.alipay.template.service.AlipayTemplateService;
import com.yundasys.member.alipay.template.vo.RspBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

//公众号模版消息相关的controller
@Slf4j
@Api
@RestController
@RequestMapping(value = "/alipayTemplate")
public class MessageController extends BaseController {

	@Autowired
	RedisTemplate<String,String> redisTemplate;
	
    @Autowired
    AlipayTemplateService templateService;


//	@ApiOperation(value = "发送模版消息", notes = "普通方式传参")
//	@RequestMapping(value = "/sendTemplate", method = RequestMethod.POST)
//	public RspBean<String> sendTemplate(@Validated @RequestBody Bo_SendTemplate bo, BindingResult bindingResult)
//			throws Exception {
//		if (bindingResult.hasErrors()) {
//			FieldError fieldError = bindingResult.getFieldError();
//			return failure(fieldError.getDefaultMessage());
//		}
//
//		//智能模板切换 
//		SvcUtil.changeAdUrl(bo);
//		
//		//构建支付宝模板体
//		String content = SvcUtil.createTemplateContent(bo);
//		
//		// 30秒内不允许对同一人发送了同样的信息
//		String cacheKey = bo.getOpenId() + MD5.MD5Encode(content);
//		if (redisTemplate.hasKey(cacheKey)) {
//			return failure("30秒内不允许对同一人发送了同样的信息");
//		}
//		redisTemplate.opsForValue().set(cacheKey, true, 30, TimeUnit.SECONDS);
//		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
//		AlipayOpenPublicMessageSingleSendRequest request = new AlipayOpenPublicMessageSingleSendRequest();
//		request.setBizContent(content);
//		AlipayOpenPublicMessageSingleSendResponse response = alipayClient.execute(request); 
//        if (null != response && response.isSuccess()) {
//            log.debug("异步发送成功，结果为：" + response.getBody());
//            return success("success");
//        } else {
//        	log.error("异步发送失败 code=" + response.getCode() + "msg："+ response.getMsg());
//        	return failure(response.getMsg());
//        }
//
//	}
	
	
	
    /**
	 * 
	 * String类型通用处理
	 * 其他类型使用xxl-job在线编译代码解决
	 * 
	 *@Title: operateOnlineRedis
	 *@Description: 修改线上缓存
	 *@param bo
	 *@param bindingResult
	 *@return
	 *@author: htj
	 *@date: 2020年1月5日
	 */
	@ApiIgnore
	@ApiOperation(value = "用于线上问题排查")
	@RequestMapping(value = "/operateOnlineRedis", method = RequestMethod.POST)
	public RspBean<String> operateOnlineRedis(@Validated @RequestBody Bo_OperateRedis bo,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			return failure(fieldError.getDefaultMessage());
		}
		OperateTypeEnum opt = OperateTypeEnum.getEnum(bo.getType());
		if(opt == null){
			return failure("the request is invalid !!!");
		}
		String result = null;
		switch(opt){
		case search:
			result = redisTemplate.opsForValue().get(bo.getKey());
			break;
		case update:
			if(StringUtils.isNotBlank(bo.getValue())){
				redisTemplate.opsForValue().set(bo.getKey(), bo.getValue());
			}else{
				return failure("Update operation has no value");
			}
			break;
		case delete:
			redisTemplate.delete(bo.getKey());
			break;
		default:
			return failure("what are you doing !!!");
		}
		return success(result);
	}
	
	
	
	
	
	
	
	
	/**
	 * 
	 *@Title: operateOnlineData
	 *@Description: 修改线上数据
	 *@param bo
	 *@param bindingResult
	 *@return
	 *@author: htj
	 *@date: 2020年1月5日
	 */
	@ApiIgnore
	@ApiOperation(value = "操纵Online数据库")
	@RequestMapping(value = "/operateOnlineData", method = RequestMethod.POST)
	public RspBean<List<LinkedHashMap<String, Object>>> operateOnlineData(@Validated @RequestBody Bo_OperateDatabase bo,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			return failure(fieldError.getDefaultMessage());
		}
		if (!bo.getSign().equals(GlobalConstant.ONLINE_DATABASE_SIGN)) {
			return failure("the sign is invalid !!");
		}
		OperateTypeEnum opt = OperateTypeEnum.getEnum(bo.getType());
		if(opt == null){
			return failure("the request is invalid !!!");
		}
		List<LinkedHashMap<String, Object>> list = null;
		String sql = bo.getSql();
		switch(opt){
		case search:
			list = templateService.selectSelfDefine(sql);
			break;
		case update:
			if (sql.indexOf("WHERE") == -1 && sql.indexOf("where") == -1) {
				return failure("UPDATE statement must add condition!");
			}
			templateService.updateSelfDefine(sql);
			break;
		case delete:
			if (sql.indexOf("WHERE") == -1 && sql.indexOf("where") == -1) {
				return failure("DELETE statement must add condition!");
			}
			templateService.deleteSelfDefine(sql);
			break;
		case insert:
			templateService.saveSelfDefine(sql);
			break;
		default:
			return failure("what are you doing !!!");
		}
		return success(list);
	}
	
	
	
    

	
	
	
	
	
	
	public static void  main(String[] args) {
        try {
        	
        	
        	
        	String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIZXQKAtaC4mSrbamOScjyyKHpT2j0dPp5tbsoDxpQOsErQQr/PFSoim6Ue9odO208+sDqiDwfzAQULzyU/D+YkT1DxZIaJfoDPybfACEgT4ls1Z5iCGtQzrIZczyUCGj5UZi62cSGXABxv3cydN35pbHpDVofKUE+YmnKnC8TbvAgMBAAECgYAD61e/VJPNhOB5JTeAs/CZZA4wWmEju3cwWSSEDoi56rNA/ZukiQT7p6L2rNmjy5myXVqwH+fw78r3oRUmdpk5E7WgCkUvEGCg6KEBr5bJ+PF/Icb2nAcoPnxV390seAtVE2ou5FaqQKNAhwPzWHGRui7gLpowqKWyPeZZbHu3AQJBAMrvp9B5ZuGWJfXl84xxx/FmSu67GbcuDVa9xH6UZBn4FlOM0XGaWXDpAynsQUdZ/D0mT95eo56mDfK2RnGQll8CQQCpd+Th7r+RXFwCs5exi62IVOrzdOH94oskYRjXExNsnk69hpWriuOzERZbYzwy2fI74XGBHv0sRuWlXE2EGolxAkEAhDVt1tvAsubnBDQzXyQhZpuF5dHvBu/xsLkg8nYqYODHatcq/B/adTzY2s8YGCv/sLbtAaoWXp1AKQenDQVtcQJAJ0dov40sza5QjTe/EyHCyPSVuHQA5W2avoXa0g7T07slmPwWuLnqaNivC+OGUmr9oC9ytXDPUXHlTFyGgvX7oQJAIwxz1IY7fFRor7/wyVVfLP8wntF962dqW3mWkE1SSDBFkVR1c1sBPXh88qp32KFBr6+YJCqSmYX39NUPzHPJ6Q==";
        	String alipayPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
    		AlipayClient alipayClient = new DefaultAlipayClient(GlobalConstant.ALIPAY_GATEWAY, "2015082500231676", 
    				privateKey, "json","GBK" ,alipayPublicKey, "RSA");
    		AlipayOpenPublicFollowBatchqueryRequest request = new AlipayOpenPublicFollowBatchqueryRequest();
    		request.setBizContent("{\"next_user_id\":\"2088932140570195\"}");
    		AlipayOpenPublicFollowBatchqueryResponse response = alipayClient.execute(request);
    		if (response.isSuccess()) {
    			JSONObject resJson = JSONObject.parseObject(response.getBody());
    			JSONArray userids = ((JSONObject)((JSONObject)resJson.get("alipay_open_public_follow_batchquery_response"))
    			.get("data")).getJSONArray("user_id_list");
    			if(userids != null && userids.size()>0) {
    				List<String> arr = JSONObject.parseArray(userids.toJSONString(), String.class);
    				System.out.println("arr == "+arr.toString()+",size = "+arr.size());
    				
    			}else {
    				System.out.println("last userid,result = "+response.getBody());
				}
            } 
    		int count = 112611;
			
			//预定开启100个线程,计算线程处理数据的区间
			int cycleCount = (int)Math.ceil((double)count/100);
			System.out.println("last userid,cycleCount = "+cycleCount);
        	
//        	String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIZXQKAtaC4mSrbamOScjyyKHpT2j0dPp5tbsoDxpQOsErQQr/PFSoim6Ue9odO208+sDqiDwfzAQULzyU/D+YkT1DxZIaJfoDPybfACEgT4ls1Z5iCGtQzrIZczyUCGj5UZi62cSGXABxv3cydN35pbHpDVofKUE+YmnKnC8TbvAgMBAAECgYAD61e/VJPNhOB5JTeAs/CZZA4wWmEju3cwWSSEDoi56rNA/ZukiQT7p6L2rNmjy5myXVqwH+fw78r3oRUmdpk5E7WgCkUvEGCg6KEBr5bJ+PF/Icb2nAcoPnxV390seAtVE2ou5FaqQKNAhwPzWHGRui7gLpowqKWyPeZZbHu3AQJBAMrvp9B5ZuGWJfXl84xxx/FmSu67GbcuDVa9xH6UZBn4FlOM0XGaWXDpAynsQUdZ/D0mT95eo56mDfK2RnGQll8CQQCpd+Th7r+RXFwCs5exi62IVOrzdOH94oskYRjXExNsnk69hpWriuOzERZbYzwy2fI74XGBHv0sRuWlXE2EGolxAkEAhDVt1tvAsubnBDQzXyQhZpuF5dHvBu/xsLkg8nYqYODHatcq/B/adTzY2s8YGCv/sLbtAaoWXp1AKQenDQVtcQJAJ0dov40sza5QjTe/EyHCyPSVuHQA5W2avoXa0g7T07slmPwWuLnqaNivC+OGUmr9oC9ytXDPUXHlTFyGgvX7oQJAIwxz1IY7fFRor7/wyVVfLP8wntF962dqW3mWkE1SSDBFkVR1c1sBPXh88qp32KFBr6+YJCqSmYX39NUPzHPJ6Q==";
//        	String alipayPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
//    		AlipayClient alipayClient = new DefaultAlipayClient(GlobalConstant.ALIPAY_GATEWAY, "2015082500231676", 
//    				privateKey, "json","GBK" ,alipayPublicKey, "RSA");
//    		AlipayOpenPublicMessageSingleSendRequest request = new AlipayOpenPublicMessageSingleSendRequest();
//    		request.setBizContent("");
//    		AlipayOpenPublicMessageSingleSendResponse response = alipayClient.execute(request); 
//            if (null != response && response.isSuccess()) {
//                log.debug("异步发送成功，结果为：" + response.getBody());
//                System.out.println("success"); 
//            } else {
//            	log.error("异步发送失败 code=" + response.getCode() + "msg："+ response.getMsg());
//            }

        } catch (Exception e) {
        	log.error("消息发送失败,请求异常",e);
        }
	}
	
	
	
   





	

}
