package com.supervisions.framework.web.mapper;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description= "返回响应数据")
public class Result implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static final String SUCCESS = "0";
	
	private static final String ERROR = "1";

	/**
	 * 状态码 (0成功  1失败)
	 */
	@ApiModelProperty(value = "状态码 (0成功  1失败)")
	private String code;
	
	/**
	 * 返回数据
	 */
	@ApiModelProperty(value = "成功返回的数据")
	private Object data;
	
	/**
	 * 错误信息
	 */
	@ApiModelProperty(value = "错误信息")
	private String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Result(String code, Object data, String msg) 
	{
		this.code = code;
		this.data = data;
		this.msg = msg;
	}

	public Result() 
	{
		
	}
	
	/**
	 * 操作成功结果.
	 */
	public static Result successResult(Object data,String msg) {
		return new Result(SUCCESS, data, msg);
	}

	/**
	 * 默认操作成功结果.
	 */
	public static Result successResult() {
		return new Result(SUCCESS, null, "success");
	}

	/**
	 * 默认操作成功结果.
	 */
	public static Result successResult(Object data) {
		return new Result(SUCCESS, data, "success");
	}
	
	/**
	 * 操作失败结果.
	 */
	public static Result errorResult(String msg) {
		return new Result(ERROR, null, msg);
	}
	
	/**
	 * 默认操作失败结果.
	 */
	public static Result errorResult() {
		return new Result(ERROR, null, "服务器开小差啦，请稍后再试！");
	}
	
	/**
	 * 验证登录状态失败结果.
	 */
	public static Result errorAuthResult() {
		return new Result(ERROR, null, "登录信息异常，请重新登录");
	}
	
	/**
	 * 验证公司秘钥失败结果.
	 */
	public static Result errorKeyResult() {
		return new Result(ERROR, null, "用户key有误");
	}
	
	/**
	 * 验证请求Content-Type失败结果.
	 */
	public static Result errorRequestResult() {
		return new Result(ERROR, null, "请求格式有误，请使用 application/x-www-form-urlencoded");
	}
}
