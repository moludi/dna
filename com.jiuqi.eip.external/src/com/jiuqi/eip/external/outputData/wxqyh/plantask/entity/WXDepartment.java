package com.jiuqi.eip.external.outputData.wxqyh.plantask.entity;

import org.json.JSONObject;

/**
 * 向微信同步部门的部门实体类
 * @author liute
 * 注意，部门的最大层级为15层；部门总数不能超过3万个；每个部门下的节点不能超过3万个。建议保证创建的部门和对应部门成员是串行化处理。
 * @date 2018年5月21日
 */
public class WXDepartment {
	//部门名称。长度限制为1~32个字符，字符不能包括\:?”<>｜ 
	private String name;
	//父部门id，32位整型
	private String parentid;
	//在父部门中的次序值。order值大的排序靠前。有效的值范围是[0, 2^32)
	private int orderid;
	//部门id，32位整型，指定时必须大于1。若不填该参数，将自动生成id
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
