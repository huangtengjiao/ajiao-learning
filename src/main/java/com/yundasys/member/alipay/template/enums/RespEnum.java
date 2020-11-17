package com.yundasys.member.alipay.template.enums;

public enum RespEnum {
	/**
	 * 请求成功
	 */
	SUCCESS(200, "请求成功"),
	/**
	 * 请求数据错误
	 */
	ERROR_REQ_MISSPARAM(700, "确少请求参数错误"),
	/**
	 * 请求数据错误
	 */
	ERROR_REQ_PARAM(701, "请求参数解析错误"),
	/**
	 * 请求头错误
	 */
	ERROR_REQ_HEADER(702, "请求头错误"),
	/**
	 * 请求过期
	 */
	ERROR_REQ_EXPIRED(703, "请求过期"),
	/**
	 * 请求方法错误
	 */
	ERROR_REQ_METHOD(704, "请求方法错误"),
	/**
	 * 请求参数valid错误
	 */
	ERROR_REQ_BADPARAM(705, "请求参数valid错误"),
	/**
	 * 请求方法错误
	 */
	ERROR_REQ_CONTENTTYPE_UNSPPORT(706, "不支持当前媒体类型"),
	
	/**
	 * 请求方法错误
	 */
	ERROR_REQ_AGAIN(707, "业务正在处理中，请勿重复请求"),
	
	/**
	 * 业务逻辑错误
	 */
	ERROR_BUSINESS_OPERATE(800, "业务逻辑错误"),
	/**
	 * 请先登录
	 */
	ERROR_BUSINESS_NOT_LOGIN(801, "请先登录"),
	/**
	 * 用户不存在
	 */
	ERROR_BUSINESS_NO_USER(802, "用户不存在"),
	/**
	 * 验证码错误!
	 */
	ERROR_VERIFY_CODE(803, "验证码错误!"),
	/**
	 * 验签错误!
	 */
	ERROR_VERIFY_SIGN_PARAM(804, "验签错误，缺少签名参数!"),
	/**
	 * 验签错误!
	 */
	ERROR_VERIFY_SIGN_KEY(805, "验签错误，无法获取签名key!"),
	/**
	 * 验签错误!
	 */
	ERROR_VERIFY_SIGN_CHECK(806, "验签错误!"),
	/**
	 * TOKEN失效!
	 */
	ERROR_BAD_TOKEN(807, "TOKEN失效!"),
	/**
	 * 请求次数超过上限
	 */
	REQUEST_EXCEED(808, "请求次数超过上限"),
	/**
	 * 接口关闭
	 */
	API_OFF(809, "接口关闭"),
	/**
	 * SQL错误!
	 */
	ERROR_SQL(900, "SQL异常");

	private int code;
	private String message;

	private RespEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public static String getMessage(int code) {
		for (RespEnum p : RespEnum.values()) {
			if (p.getCode() == code) {
				return p.message;
			}
		}
		return "";
	}
}
