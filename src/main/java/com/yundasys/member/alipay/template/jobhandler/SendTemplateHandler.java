package com.yundasys.member.alipay.template.jobhandler;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenPublicMessageSingleSendRequest;
import com.alipay.api.response.AlipayOpenPublicMessageSingleSendResponse;
import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.yundasys.member.alipay.template.constant.GlobalConstant;
import com.yundasys.member.alipay.template.domain.AlipayTemplateParam;
import com.yundasys.member.alipay.template.domain.AlipayUserid;
import com.yundasys.member.alipay.template.service.AlipayTemplateService;

import lombok.extern.slf4j.Slf4j;


/**
 * 
 * @Description: 一次性模板发送 
 * @author htj 
 * @date 2020年7月6日
 */
@Slf4j
@JobHandler(value="sendTemplateHandler")
@Component
public class SendTemplateHandler extends IJobHandler {
	
	
	@Autowired
	private StringRedisTemplate srt;
	
    @Autowired
    AlipayTemplateService templateService;
	
	//线程池
	private static ExecutorService pool;
	
	@Value("${appId}")
	private String  appId;
	
	@Value("${privateKey}")
	private String  privateKey;
	
	@Value("${alipayPublicKey}")
	private String  alipayPublicKey;
	
	@Value("${concurrentNumber:100}")
	private int concurrentNumber;
	
	@ApolloJsonValue("${templateInfo}")
	private AlipayTemplateParam templateInfo;
	
	@Override
	public void init() {
		
		//初始化线程池,使用无界任务队列
		pool = new ThreadPoolExecutor(200, 300, 30L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(),
				//纯属好玩,此处可使用Executors.defaultThreadFactory()
				new ThreadFactory(){

					@Override
					public Thread newThread(Runnable r) {
						XxlJobLogger.log("线程 ajiao_send_template "+r.hashCode()+"创建");
						Thread th = new Thread(r,"ajiao_send_template"+r.hashCode());
						return th;
					}
			
		},
				new ThreadPoolExecutor.AbortPolicy()) {
			
			@Override
			protected void beforeExecute(Thread t, Runnable r) { }
			
			@Override
		    protected void afterExecute(Runnable r, Throwable t) { }

			@Override
		    protected void terminated() { 
				XxlJobLogger.log("ajiao创建的线程池退出");
			}
		};
	}
	
	@Override
	public void destroy() {
		
		//任务跑完关闭连接池
		if(pool != null) {
			pool.shutdown();
		}
	}
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			String tableName = srt.opsForList().rightPop(GlobalConstant.SEND_TEMPLATE_LIST_KEY);
    		if(StringUtils.isNotBlank(tableName)) {
    			XxlJobLogger.log(tableName+"发送模板任务开始执行...............");
    			int count = templateService.countTable(tableName);
    			
    			//预定开启100个线程,计算线程处理数据的区间
    			int cycleCount = (int)Math.ceil((double)count/concurrentNumber);
    			for(int i=0;i<concurrentNumber;i++) {
    				int start = i*cycleCount;
					Runnable r = new Runnable() {
						@Override
						public void run() {
							int threadStart = start;
							try {
								XxlJobLogger.log("tableName = {},threadStart = {},cycleCount = {},开始执行......",tableName,threadStart,cycleCount);
								long tl = System.currentTimeMillis();
								for(int j=0;j<cycleCount;j++) {
									AlipayUserid au = templateService.getUserid(tableName, threadStart);
									if(au == null) {
										break;
									}
									String[] arr = au.getUserIds().split(",");
									for(String userid : arr) {
										sendTemplate(userid);
									}
									threadStart = threadStart+1;
									Thread.sleep(10);
								}
								long etl = (System.currentTimeMillis() - tl)/1000;
								XxlJobLogger.log("tableName = {},threadStart = {},cycleCount = {},etl = {},完美结束......",tableName,threadStart,cycleCount,etl);
							} catch (Exception e1) {
								XxlJobLogger.log("tableName = {},threadStart = {},cycleCount = {},异常......",tableName,threadStart,cycleCount,e1);
							}
						}
					};
					pool.submit(r);
    			}
    			XxlJobLogger.log("tableName = {},concurrentNumber = {},模板发送任务执行完毕...............",tableName,concurrentNumber);
    		}else {
    			XxlJobLogger.log("模板发送任务执行完毕...............");
    		}
		}catch (Exception e) {
			XxlJobLogger.log("模板发送任务任务失败.............",e);
			return FAIL;
		}
		return SUCCESS;
	}
	
	
	public void sendTemplate(String userid) {
		try {
			//初始支付宝客户端
			AlipayClient alipayClient = new DefaultAlipayClient(GlobalConstant.ALIPAY_GATEWAY, appId, 
					privateKey, "json","GBK" ,alipayPublicKey, "RSA");
			AlipayOpenPublicMessageSingleSendRequest request = new AlipayOpenPublicMessageSingleSendRequest();
			request.setBizContent(createTemplateContent(userid));
			AlipayOpenPublicMessageSingleSendResponse response = alipayClient.execute(request); 
			if (null != response && response.isSuccess()) {
			    log.info("send template success,userid = {},result = {}",userid,response.getBody());
			} else {
				log.error("send template fail,userid = {},code = {},msg = {},sub_msg = {},sub_code = {}",userid,response.getCode(),
	        			response.getMsg(),response.getSubMsg(),response.getSubCode());
			}
		} catch (Exception e) {
			log.error("send template fail,userid = {}",userid,e);
		}
	}
	
	public String createTemplateContent(String userid){
		String content = "{\"to_user_id\":\""+userid+"\"," +
				"\"template\":{\"template_id\":\""+templateInfo.getTemplateId()+"\"," +
				"\"context\":{\"head_color\":\""+templateInfo.getHeadColor()+"\"," +
				"\"url\":\""+templateInfo.getUrl()+"\"," +
				"\"action_name\":\"查看详情\",\"first\":{" +
				"\"color\":\""+templateInfo.getFirstColor()+"\",\"value\":\""+templateInfo.getFirst()+"\"" +
				"},\"remark\":{\"color\":\""+templateInfo.getRemarkColor()+"\",\"value\":\""+templateInfo.getRemark()+"\"},";
		for(int i=1;i<=templateInfo.getKeywords().size();i++){
        	content = content + "\"keyword"+i+"\":{\"color\":\""+templateInfo.getKeyWordColor()+"\",\"value\":\""+templateInfo.getKeywords().get("keyword"+i)+"\"},";
        }
		content = content.substring(0,content.length()-1)+"}}}";
		return content;
	}
	
	
	
	
    public static void main(String[] args) {
    	
    	String[] arr = "3456".split(",");
		System.out.println(Arrays.toString(arr));
	}


}
