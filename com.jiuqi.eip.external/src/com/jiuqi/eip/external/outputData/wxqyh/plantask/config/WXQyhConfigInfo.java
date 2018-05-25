package com.jiuqi.eip.external.outputData.wxqyh.plantask.config;

public class WXQyhConfigInfo {	
	/**
	 * 企业微信 HOST
	 */
	public static final String HOST = "https://qyapi.weixin.qq.com/cgi-bin/";
	/**
	 * 获取Access_Token的URL
	 * ?corpid=ID&corpsecret=SECRECT
	 */
	public static String GET_ACCESS_TOKEN_URL = HOST + "gettoken";
	/**
	 * 创建部门  POST
	 */
	public static final String CREATE_DEPARTMENT_URL = HOST + "department/create?access_token=";
	/**
	 * 更新部门  POST
	 */
	public static final String UPDATE_DEPARTMENT_URL = HOST + "department/update?access_token=";
	/**
	 * 删除部门  GET 
	 * ACCESS_TOKEN&id=ID  
	 * 部门id。（注：不能删除根部门；不能删除含有子部门、成员的部门）
	 */
	public static final String DELETE_DEPARTMENT_URL = HOST + "department/delete?access_token=";
	/**
	 * 获取部门列表  GET 
	 * ACCESS_TOKEN&id=ID 
	 * 部门id。获取指定部门及其下的子部门。 如果不填，默认获取全量组织架构
	 */
	public static final String GET_DEPARTMENT_LIST_URL = HOST + "department/list?access_token=";
	/**
	 * 创建成员 POST
	 */
	public static final String CREATE_STAFF_URL = HOST + "user/create?access_token=";
	/**
	 * 读取成员  GET 
	 * ACCESS_TOKEN&userid=USERID    
	 * 成员UserID。对应管理端的帐号，企业内必须唯一。不区分大小写，长度为1~64个字节
	 */
	public static final String GET_STAFF_URL = HOST + "user/get?access_token=";
	/**
	 * 更新成员  POST
	 */
	public static final String UPDATE_STAFF_URL = HOST + "user/update?access_token=";
	/**
	 * 删除成员  GET 
	 * ACCESS_TOKEN&userid=USERID
	 */
	public static final String DELETE_STAFF_URL = HOST + "user/delete?access_token=";
	/**
	 * 批量删除成员 POST
	 */
	public static final String DELETE_STAFF_LIST_URL = HOST + "user/batchdelete?access_token=";
}
