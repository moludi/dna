package com.jiuqi.eip.external.outputData.wxqyh.plantask.entity;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author liute
 * 每个部门下的部门、成员总数不能超过3万个。建议保证创建department对应的部门和创建成员是串行化处理。
 * @date 2018年5月21日
 */
public class WXStaff {
	/**
	 * eip内部标识
	 */
	private String stdcode;
	/**
	 * 成员UserID。对应管理端的帐号，企业内必须唯一。不区分大小写，长度为1~64个字节
	 */
	private String userid;
	/**
	 * 成员名称。长度为1~64个字符
	 */
	private String name;
	/**
	 * 英文名。长度为1-64个字节，由字母、数字、点(.)、减号(-)、空格或下划线(_)组成
	 */
	private String english_name;
	/**
	 * 手机号码。企业内必须唯一，mobile/email二者不能同时为空
	 */
	private String mobile;
	/**
	 * 成员所属部门id列表,不超过20个
	 */
	private String[] department;
	/**
	 * 部门内的排序值，默认为0，成员次序以创建时间从小到大排列。数量必须和department一致，数值越大排序越前面。有效的值范围是[0, 2^32)
	 */
	private int[] order;
	/**
	 * 职位信息。长度为0~128个字符
	 */
	private String position;
	/**
	 * 性别。1表示男性，2表示女性
	 */
	private String gender;
	/**
	 * 邮箱。长度不超过64个字节，且为有效的email格式。企业内必须唯一，mobile/email二者不能同时为空
	 */
	private String email;
	/**
	 * 上级字段，标识是否为上级。在审批等应用里可以用来标识上级审批人
	 */
	private int isleader;
	/**
	 * 启用/禁用成员。1表示启用成员，0表示禁用成员
	 */
	private int enable;
	/**
	 * 成员头像的mediaid，通过素材管理接口上传图片获得的mediaid
	 */
	private String avatar_mediaid;
	/**
	 * 座机。由1-32位的纯数字或’-‘号组成。
	 */
	private String telephone;
	/**
	 * 自定义字段。自定义字段需要先在WEB管理端添加，见扩展属性<http://work.weixin.qq.com/api/doc#10138/扩展属性的添加方法>添加方法，
	 * 否则忽略未知属性的赋值。自定义字段长度为0~32个字符，超过将被截断
	 */
	private JSONArray extattr;
	/**
	 * 是否邀请该成员使用企业微信（将通过微信服务通知或短信或邮件下发邀请，每天自动下发一次，最多持续3个工作日），默认值为true。
	 */
	private boolean to_invite;
	/** 
	 * 成员对外属性，字段详情见对外属性<http://work.weixin.qq.com/api/doc#13450> 
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
