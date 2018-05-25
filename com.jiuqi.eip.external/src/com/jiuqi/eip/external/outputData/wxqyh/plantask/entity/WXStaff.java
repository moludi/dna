package com.jiuqi.eip.external.outputData.wxqyh.plantask.entity;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author liute
 * ÿ�������µĲ��š���Ա�������ܳ���3��������鱣֤����department��Ӧ�Ĳ��źʹ�����Ա�Ǵ��л�����
 * @date 2018��5��21��
 */
public class WXStaff {
	/**
	 * eip�ڲ���ʶ
	 */
	private String stdcode;
	/**
	 * ��ԱUserID����Ӧ����˵��ʺţ���ҵ�ڱ���Ψһ�������ִ�Сд������Ϊ1~64���ֽ�
	 */
	private String userid;
	/**
	 * ��Ա���ơ�����Ϊ1~64���ַ�
	 */
	private String name;
	/**
	 * Ӣ����������Ϊ1-64���ֽڣ�����ĸ�����֡���(.)������(-)���ո���»���(_)���
	 */
	private String english_name;
	/**
	 * �ֻ����롣��ҵ�ڱ���Ψһ��mobile/email���߲���ͬʱΪ��
	 */
	private String mobile;
	/**
	 * ��Ա��������id�б�,������20��
	 */
	private String[] department;
	/**
	 * �����ڵ�����ֵ��Ĭ��Ϊ0����Ա�����Դ���ʱ���С�������С����������departmentһ�£���ֵԽ������Խǰ�档��Ч��ֵ��Χ��[0, 2^32)
	 */
	private int[] order;
	/**
	 * ְλ��Ϣ������Ϊ0~128���ַ�
	 */
	private String position;
	/**
	 * �Ա�1��ʾ���ԣ�2��ʾŮ��
	 */
	private String gender;
	/**
	 * ���䡣���Ȳ�����64���ֽڣ���Ϊ��Ч��email��ʽ����ҵ�ڱ���Ψһ��mobile/email���߲���ͬʱΪ��
	 */
	private String email;
	/**
	 * �ϼ��ֶΣ���ʶ�Ƿ�Ϊ�ϼ�����������Ӧ�������������ʶ�ϼ�������
	 */
	private int isleader;
	/**
	 * ����/���ó�Ա��1��ʾ���ó�Ա��0��ʾ���ó�Ա
	 */
	private int enable;
	/**
	 * ��Աͷ���mediaid��ͨ���زĹ���ӿ��ϴ�ͼƬ��õ�mediaid
	 */
	private String avatar_mediaid;
	/**
	 * ��������1-32λ�Ĵ����ֻ�-������ɡ�
	 */
	private String telephone;
	/**
	 * �Զ����ֶΡ��Զ����ֶ���Ҫ����WEB�������ӣ�����չ����<http://work.weixin.qq.com/api/doc#10138/��չ���Ե���ӷ���>��ӷ�����
	 * �������δ֪���Եĸ�ֵ���Զ����ֶγ���Ϊ0~32���ַ������������ض�
	 */
	private JSONArray extattr;
	/**
	 * �Ƿ�����ó�Աʹ����ҵ΢�ţ���ͨ��΢�ŷ���֪ͨ����Ż��ʼ��·����룬ÿ���Զ��·�һ�Σ�������3�������գ���Ĭ��ֵΪtrue��
	 */
	private boolean to_invite;
	/** 
	 * ��Ա�������ԣ��ֶ��������������<http://work.weixin.qq.com/api/doc#13450> 
	 */
	private JSONArray external_profile;
	
	public JSONObject toJSONObject(){
		JSONObject json = new JSONObject();
		json.put("userid", userid);
		json.put("name", name);
		json.put("mobile", mobile);
		json.put("department", department);
		json.put("gender", gender);
		return json;
	}
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEnglish_name() {
		return english_name;
	}
	public void setEnglish_name(String english_name) {
		this.english_name = english_name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String[] getDepartment() {
		return department;
	}
	public void setDepartment(String[] department) {
		this.department = department;
	}
	public int[] getOrder() {
		return order;
	}
	public void setOrder(int[] order) {
		this.order = order;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getIsleader() {
		return isleader;
	}
	public void setIsleader(int isleader) {
		this.isleader = isleader;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public String getAvatar_mediaid() {
		return avatar_mediaid;
	}
	public void setAvatar_mediaid(String avatar_mediaid) {
		this.avatar_mediaid = avatar_mediaid;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public JSONArray getExtattr() {
		return extattr;
	}
	public void setExtattr(JSONArray extattr) {
		this.extattr = extattr;
	}
	public boolean isTo_invite() {
		return to_invite;
	}
	public void setTo_invite(boolean to_invite) {
		this.to_invite = to_invite;
	}
	public JSONArray getExternal_profile() {
		return external_profile;
	}
	public void setExternal_profile(JSONArray external_profile) {
		this.external_profile = external_profile;
	}
	public String getStdcode() {
		return stdcode;
	}
	public void setStdcode(String stdcode) {
		this.stdcode = stdcode;
	}
}
