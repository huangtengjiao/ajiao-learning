package com.yundasys.member.alipay.template.aop;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.yundasys.member.alipay.template.enums.RespEnum;
import com.yundasys.member.alipay.template.vo.RspBean;

import lombok.extern.slf4j.Slf4j;

//全局异常处理
@Slf4j
@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {

	/**
	 * 400 - Bad Request
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public RspBean<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
		log.error("缺少请求参数", e);
		return new RspBean<String>().failure(RespEnum.ERROR_REQ_MISSPARAM);
	}

	/**
	 * 400 - Bad Request
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public RspBean<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.error("参数解析失败", e);
		return new RspBean<String>().failure(RespEnum.ERROR_REQ_PARAM);
	}

	/**
	 * 400 - Bad Request
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public RspBean<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error("参数验证失败", e);
		return new RspBean<String>().failure(RespEnum.ERROR_REQ_BADPARAM);
	}

	/**
	 * 405 - Method Not Allowed
	 */
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public RspBean<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		log.error("不支持当前请求方法", e);
		return new RspBean<String>().failure(RespEnum.ERROR_REQ_METHOD);
	}

	/**
	 * 415 - Unsupported Media Type
	 */
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public RspBean<String> handleHttpMediaTypeNotSupportedException(Exception e) {
		log.error("不支持当前媒体类型", e);
		return new RspBean<String>().failure(RespEnum.ERROR_REQ_CONTENTTYPE_UNSPPORT);
	}

	/**
	 * 500 - Internal Server Error
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public RspBean<String> handleException(Exception e) {
		log.error("通用异常", e);
		return new RspBean<String>(RespEnum.ERROR_BUSINESS_OPERATE.getCode(), "异常："+e.getMessage());
	}

}