package com.alipay.yundasys.test;



import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenPublicFollowBatchqueryRequest;
import com.alipay.api.request.AlipayOpenPublicMessageSingleSendRequest;
import com.alipay.api.response.AlipayOpenPublicFollowBatchqueryResponse;
import com.alipay.api.response.AlipayOpenPublicMessageSingleSendResponse;
import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import com.xxl.job.core.log.XxlJobLogger;
import com.yundasys.member.alipay.template.AlipayTemplateApplication;
import com.yundasys.member.alipay.template.constant.GlobalConstant;
import com.yundasys.member.alipay.template.domain.AlipayTemplateParam;
import com.yundasys.member.alipay.template.domain.AlipayUserid;
import com.yundasys.member.alipay.template.service.AlipayTemplateService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AlipayTemplateApplication.class)
@WebAppConfiguration
public class AlipayTest {
	
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
	
	@ApolloJsonValue("${templateInfo}")
	private AlipayTemplateParam templateInfo;
	
	
	
    @Test
    public void testThread()  {
    	try {
    		Long initsize = srt.opsForList().size(GlobalConstant.SEND_TEMPLATE_LIST_KEY);
    		log.info("初始化size === "+initsize);
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
    		log.info("最终size === "+finalsize);
    		List<String> range = srt.opsForList().range(GlobalConstant.SEND_TEMPLATE_LIST_KEY, 0, -1);
    		log.info("range === "+range.toString());
    		Long lastsize = srt.opsForList().size(GlobalConstant.SEND_TEMPLATE_LIST_KEY);
    		log.info("lastsize === "+lastsize);
    		
    		
//    		String tableName = srt.opsForList().rightPop(GlobalConstant.SEND_TEMPLATE_LIST_KEY);
//    		int count = templateService.countTable(tableName);
//    		log.info("count === "+count);
//    		AlipayUserid au = templateService.getUserid(tableName, 1);
//    		log.info("au === "+au.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    
    
    
    
 
    @Test
    public void testRedis()  {
    	try {
    		
    		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIZXQKAtaC4mSrbamOScjyyKHpT2j0dPp5tbsoDxpQOsErQQr/PFSoim6Ue9odO208+sDqiDwfzAQULzyU/D+YkT1DxZIaJfoDPybfACEgT4ls1Z5iCGtQzrIZczyUCGj5UZi62cSGXABxv3cydN35pbHpDVofKUE+YmnKnC8TbvAgMBAAECgYAD61e/VJPNhOB5JTeAs/CZZA4wWmEju3cwWSSEDoi56rNA/ZukiQT7p6L2rNmjy5myXVqwH+fw78r3oRUmdpk5E7WgCkUvEGCg6KEBr5bJ+PF/Icb2nAcoPnxV390seAtVE2ou5FaqQKNAhwPzWHGRui7gLpowqKWyPeZZbHu3AQJBAMrvp9B5ZuGWJfXl84xxx/FmSu67GbcuDVa9xH6UZBn4FlOM0XGaWXDpAynsQUdZ/D0mT95eo56mDfK2RnGQll8CQQCpd+Th7r+RXFwCs5exi62IVOrzdOH94oskYRjXExNsnk69hpWriuOzERZbYzwy2fI74XGBHv0sRuWlXE2EGolxAkEAhDVt1tvAsubnBDQzXyQhZpuF5dHvBu/xsLkg8nYqYODHatcq/B/adTzY2s8YGCv/sLbtAaoWXp1AKQenDQVtcQJAJ0dov40sza5QjTe/EyHCyPSVuHQA5W2avoXa0g7T07slmPwWuLnqaNivC+OGUmr9oC9ytXDPUXHlTFyGgvX7oQJAIwxz1IY7fFRor7/wyVVfLP8wntF962dqW3mWkE1SSDBFkVR1c1sBPXh88qp32KFBr6+YJCqSmYX39NUPzHPJ6Q==";
        	String alipayPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
    		AlipayClient alipayClient = new DefaultAlipayClient(GlobalConstant.ALIPAY_GATEWAY, "2015082500231676", 
    				privateKey, "json","GBK" ,alipayPublicKey, "RSA");
    		
//    		AlipayClient alipayClient = new DefaultAlipayClient(GlobalConstant.ALIPAY_GATEWAY, appId, 
//    				privateKey, "json","GBK" ,alipayPublicKey, "RSA");
    		AlipayOpenPublicMessageSingleSendRequest request = new AlipayOpenPublicMessageSingleSendRequest();
    		templateInfo.setOpenId("20880022979341043380999070610170");
    								
    		request.setBizContent(createTemplateContent(templateInfo));
    		AlipayOpenPublicMessageSingleSendResponse response = alipayClient.execute(request); 
            if (null != response && response.isSuccess()) {
                log.debug("异步发送成功，结果为：" + response.getBody());
                System.out.println("success"); 
            } else {
            	log.error("异步发送失败 code=" + response.getCode() + "msg："+ response.getMsg());
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
	public String createTemplateContent(AlipayTemplateParam po){
		String content = "";
		try {
			content = "{\"to_user_id\":\""+po.getOpenId()+"\"," +
				"\"template\":{\"template_id\":\""+po.getTemplateId()+"\"," +
				"\"context\":{\"head_color\":\""+po.getHeadColor()+"\"," +
				"\"url\":\""+po.getUrl()+"\"," +
				"\"action_name\":\"查看详情\",\"first\":{" +
				"\"color\":\""+po.getFirstColor()+"\",\"value\":\""+po.getFirst()+"\"" +
				"},\"remark\":{\"color\":\""+po.getRemarkColor()+"\",\"value\":\""+po.getRemark()+"\"},";
				for(int i=1;i<=po.getKeywords().size();i++){
		        	content = content + "\"keyword"+i+"\":{\"color\":\""+po.getKeyWordColor()+"\",\"value\":\""+po.getKeywords().get("keyword"+i)+"\"},";
		        }
				content = content.substring(0,content.length()-1)+"}}}";
		} catch (Exception e) {
			log.error("build template into fail ,po = {}",po.toString(),e);
		}
		return content;
	}
    
    
    /**
	 * 
	 *@Title: getAlipayUserids
	 *@Description: 获取alipay关注者userid,并存储
	 *@author: htj
	 * @throws AlipayApiException 
	 *@date: 2020年7月6日
	 */
	public void getAlipayUserids() throws AlipayApiException {
    	String nextUserId = "";
    	String tempId = srt.opsForValue().get(GlobalConstant.NEXT_USER_ID_KEY);
    	if(StringUtils.isNotBlank(tempId)) {
    		nextUserId = tempId;
    	}
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