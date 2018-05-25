package com.jiuqi.eip.external.outputData.wxqyh.plantask.config;

public class WXQyhConfigInfo {	
	/**
	 * ��ҵ΢�� HOST
	 */
	public static final String HOST = "https://qyapi.weixin.qq.com/cgi-bin/";
	/**
	 * ��ȡAccess_Token��URL
	 * ?corpid=ID&corpsecret=SECRECT
	 */
	public static String GET_ACCESS_TOKEN_URL = HOST + "gettoken";
	/**
	 * ��������  POST
	 */
	public static final String CREATE_DEPARTMENT_URL = HOST + "department/create?access_token=";
	/**
	 * ���²���  POST
	 */
	public static final String UPDATE_DEPARTMENT_URL = HOST + "department/update?access_token=";
	/**
	 * ɾ������  GET 
	 * ACCESS_TOKEN&id=ID  
	 * ����id����ע������ɾ�������ţ�����ɾ�������Ӳ��š���Ա�Ĳ��ţ�
	 */
	public static final String DELETE_DEPARTMENT_URL = HOST + "department/delete?access_token=";
	/**
	 * ��ȡ�����б�  GET 
	 * ACCESS_TOKEN&id=ID 
	 * ����id����ȡָ�����ż����µ��Ӳ��š� ������Ĭ�ϻ�ȡȫ����֯�ܹ�
	 */
	public static final String GET_DEPARTMENT_LIST_URL = HOST + "department/list?access_token=";
	/**
	 * ������Ա POST
	 */
	public static final String CREATE_STAFF_URL = HOST + "user/create?access_token=";
	/**
	 * ��ȡ��Ա  GET 
	 * ACCESS_TOKEN&userid=USERID    
	 * ��ԱUserID����Ӧ����˵��ʺţ���ҵ�ڱ���Ψһ�������ִ�Сд������Ϊ1~64���ֽ�
	 */
	public static final String GET_STAFF_URL = HOST + "user/get?access_token=";
	/**
	 * ���³�Ա  POST
	 */
	public static final String UPDATE_STAFF_URL = HOST + "user/update?access_token=";
	/**
	 * ɾ����Ա  GET 
	 * ACCESS_TOKEN&userid=USERID
	 */
	public static final String DELETE_STAFF_URL = HOST + "user/delete?access_token=";
	/**
	 * ����ɾ����Ա POST
	 */
	public static final String DELETE_STAFF_LIST_URL = HOST + "user/batchdelete?access_token=";
}
