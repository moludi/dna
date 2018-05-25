package com.jiuqi.eip.external.outputData.wxqyh.plantask.entity;

import org.json.JSONObject;

/**
 * ��΢��ͬ�����ŵĲ���ʵ����
 * @author liute
 * ע�⣬���ŵ����㼶Ϊ15�㣻�����������ܳ���3�����ÿ�������µĽڵ㲻�ܳ���3��������鱣֤�����Ĳ��źͶ�Ӧ���ų�Ա�Ǵ��л�����
 * @date 2018��5��21��
 */
public class WXDepartment {
	//�������ơ���������Ϊ1~32���ַ����ַ����ܰ���\:?��<>�� 
	private String name;
	//������id��32λ����
	private String parentid;
	//�ڸ������еĴ���ֵ��orderֵ�������ǰ����Ч��ֵ��Χ��[0, 2^32)
	private int orderid;
	//����id��32λ���ͣ�ָ��ʱ�������1��������ò��������Զ�����id
	private String id;
	
	public JSONObject toJSONObject(){
		JSONObject json = new JSONObject();
		json.put("name", name);
		json.put("parentid", parentid);
//		json.put("orderid", orderid);
		json.put("id", id);
		return json;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
