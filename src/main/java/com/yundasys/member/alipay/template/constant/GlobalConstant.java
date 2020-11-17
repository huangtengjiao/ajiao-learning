package com.yundasys.member.alipay.template.constant;


//从数据库中，动态注入静态变量，方便非spring托管的普通类使用常量
//数据库config表中需要存在同名的参数，数值会被自动注入对应的静态值
public class GlobalConstant {

	/** 支付宝网关, 支付宝网关，支付宝线上网关为 */
	public static final String ALIPAY_GATEWAY = "https://openapi.alipay.com/gateway.do";
	
	//操作线上数据库固定签名
	public static final String ONLINE_DATABASE_SIGN = "@alipay_template_online_database@ajiao";
	
	//获取关注者userid参数nextUserid
	public static final String NEXT_USER_ID_KEY = "YAT_ATNUIK";
	
	//使用redis的list队列结构,实现分布式定时任务,达到多进程多线程执行消息发送
	public static final String SEND_TEMPLATE_LIST_KEY = "YAT_STLK";
	
	
	
    
    

}
