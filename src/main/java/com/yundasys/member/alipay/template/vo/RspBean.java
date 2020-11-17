package com.yundasys.member.alipay.template.vo;

import java.io.Serializable;

import com.yundasys.member.alipay.template.enums.RespEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author tyb
 * @param <T>
 * @date 2016年7月10日上午10:32:54
 * @desc
 */
@ApiModel(description = "封装返回数据")
public final class RspBean<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "状态码", example = "200：表示成功；其他皆为错误")
	private int code = 200;
	@ApiModelProperty(value = "错误透传消息", example = "message信息")
	private String message = "";
	@ApiModelProperty(value = "返回数据", example = "请求成功返回的数据类型T", dataType = "T")
	private T data = null;

	public String toString() {
		return "code" + code + ",message=" + message + ",data=" + data;
	}

	public RspBean() {
		this.code = RespEnum.SUCCESS.getCode();
	}

	public RspBean(T data) {
		this.code = RespEnum.SUCCESS.getCode();
		this.data = data;
	}

	public RspBean(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public RspBean(RespEnum res) {
		this.code = res.getCode();
		this.message = res.getMessage();
	}

	// 根据状态码返回
	public RspBean<T> feedback(int code) {
		this.code = code;
		this.message = RespEnum.getMessage(code);
		return this;
	}

	public RspBean<T> failure(int code) {
		return feedback(code);
	}

	public RspBean<T> failure(RespEnum res) {
		return feedback(res.getCode());
	}

	public RspBean<T> success() {
		return feedback(RespEnum.SUCCESS.getCode());
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
