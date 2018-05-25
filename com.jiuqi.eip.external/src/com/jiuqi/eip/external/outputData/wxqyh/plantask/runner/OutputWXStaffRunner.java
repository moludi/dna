package com.jiuqi.eip.external.outputData.wxqyh.plantask.runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jiuqi.dna.bap.plantask.intf.runner.Runner;
import com.jiuqi.dna.bap.plantask.intf.runner.RunnerParameter;
import com.jiuqi.dna.core.Context;
import com.jiuqi.dna.core.da.DBCommand;
import com.jiuqi.dna.core.da.RecordSet;
import com.jiuqi.dna.core.da.RecordSetField;
import com.jiuqi.dna.core.da.RecordSetFieldContainer;
import com.jiuqi.dna.core.type.GUID;
import com.jiuqi.eip.external.outputData.utils.HttpResultUtil;
import com.jiuqi.eip.external.outputData.wxqyh.plantask.config.WXQyhConfigInfo;
import com.jiuqi.eip.external.outputData.wxqyh.plantask.entity.WXDepartment;
import com.jiuqi.eip.external.outputData.wxqyh.plantask.entity.WXStaff;

public class OutputWXStaffRunner extends Runner{
	private static final String DELETE_FLAG = "delete";
	private static final String CREATE_FLAG = "create";

	@Override
	public boolean excute(RunnerParameter param, Context context) {
		String corpID = param.get("corpID");
		String secret = param.get("secret");
		String url = WXQyhConfigInfo.GET_ACCESS_TOKEN_URL + "?corpid=" + corpID + "&corpsecret=" + secret;
		String accessToken = "";
		try {
			String result = HttpResultUtil.sendGet(url);
			JSONObject sendGetResult = new JSONObject(result);
			accessToken = sendGetResult.getString("access_token");
			if(accessToken.equals("")){
				throw new Exception("返回ACCESS_TOKEN发生错误，请检查corpID和secret是否准确。");
			}
		} catch (Exception e) {
			this.appendLog("返回ACCESS_TOKEN发生错误，请检查corpID和secret是否准确。");
			return false;
		}
		Map<String, String> pushedDepartmentCode = getPushedDepartmentCode(context);
		List<String> willCreateOrUpdCode = new ArrayList<String>();
		List<WXDepartment> departmentMess = getDepartmentMess(context);
//		for (WXDepartment wxDepartment : departmentMess) {
//			String id = wxDepartment.getId();
//			try {
//				HttpResultUtil.sendGet(WXQyhConfigInfo.DELETE_DEPARTMENT_URL + accessToken + "&id=" + id);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		for (WXDepartment wxDepartment : departmentMess) {
			String id = wxDepartment.getId();
			willCreateOrUpdCode.add(id);
			if(!pushedDepartmentCode.containsKey(id)){//1.创建部门 
				try {
					String sendPost = HttpResultUtil.sendPost(WXQyhConfigInfo.CREATE_DEPARTMENT_URL + accessToken, wxDepartment.toJSONObject().toString());
					JSONObject returnMess = new JSONObject(sendPost);
					if("0".equals(returnMess.get("errcode")+"")){
						this.appendLog("部门ID为" + id + ", 部门名称为" + wxDepartment.getName() + ", 父部门ID为" + wxDepartment.getParentid() + "创建成功！");
					}else{
						this.appendLog("部门ID为" + id + "创建失败！错误信息：" + returnMess.get("errmsg"));
					}
				} catch (Exception e) {
					this.appendLog("部门ID为" + id + "创建失败！错误信息：" + e.getMessage());
				}
			}else{//2.更新部门
				String wxcode = pushedDepartmentCode.get(id);
				wxDepartment.setId(wxcode);
				try {
					String sendPost = HttpResultUtil.sendPost(WXQyhConfigInfo.UPDATE_DEPARTMENT_URL + accessToken, wxDepartment.toJSONObject().toString());
					JSONObject returnMess = new JSONObject(sendPost);
					if("0".equals(returnMess.get("errcode")+"")){
						this.appendLog("部门ID为" + id + ", 部门名称为" + wxDepartment.getName() + ", 父部门ID为" + wxDepartment.getParentid() + "更新成功！");
					}else{
						this.appendLog("部门ID为" + id + "更新失败！错误信息：" + returnMess.get("errmsg"));
					}
				} catch (Exception e) {
					this.appendLog("部门ID为" + id + "更新失败！错误信息：" + e.getMessage());
				}
			}
		}
		//获取列表 存入部门记录表
		try {
			String sendGet = HttpResultUtil.sendGet(WXQyhConfigInfo.GET_DEPARTMENT_LIST_URL + accessToken);
			String array = sendGet.substring(sendGet.indexOf("["));
			JSONArray jarray = new JSONArray(array.toString());
			for(int i = 0; i < jarray.length(); i++){
				JSONObject wxdeptJson = jarray.getJSONObject(i);
				String id = wxdeptJson.getString("id");
				String name = wxdeptJson.getString("name");
				String pid = wxdeptJson.getString("parentid");
				for (WXDepartment wxDepartment : departmentMess) {
					String stdname = wxDepartment.getName();
					if(stdname.equals(name) && pid.equals(wxDepartment.getParentid())){
						String stdcode = wxDepartment.getId();
						if(pushedDepartmentCode.containsKey(stdcode)){
							if(!id.equals(stdcode)){
								String realStdcode = stdcode.substring(1);
								//更新数据库
								int count = updateDeptRecord(context, realStdcode, id);
								if(count > 0){
									this.appendLog("部门编号为" + realStdcode + "更新成功，原wxcode为" + stdcode + ",更新后为" + id);
								}else{
									this.appendLog("部门编号为" + realStdcode + "更新失败，wxcode应为" + id);
								}
							}
						}else{
							//插入数据库
							String realStdcode = stdcode.substring(1);
							int count = insertDeptRecord(context, realStdcode, id);
							if(count > 0){
								this.appendLog("部门编号为" + realStdcode + "插入成功 ");
							}else{
								this.appendLog("部门编号为" + realStdcode + "插入失败");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			this.appendLog("获取部门列表失败");
		}
//		//3.更新成员
//		Map<String, String> pushedStaffCode = getPushedStaffCode(context);
//		List<WXStaff> updStaffMess = getStaffMess(CREATE_FLAG, param, context);
//		for (WXStaff wxStaff : updStaffMess) {
//			String stdcode = wxStaff.getStdcode();
//			if(pushedStaffCode.containsKey(stdcode)){
//				//更新
//				try {
//					String sendPost = HttpResultUtil.sendPost(WXQyhConfigInfo.UPDATE_STAFF_URL + accessToken, wxStaff.toJSONObject().toString());
//					JSONObject returnMess = new JSONObject(sendPost);
//					if("0".equals(returnMess.get("errcode"))){
//						this.appendLog("职员编号为" + stdcode + ", 姓名为" + wxStaff.getName() + "更新成功！");
//					}else{
//						this.appendLog("职员编号为" + stdcode + "更新失败！错误信息：" + returnMess.get("errmsg"));
//					}
//				} catch (Exception e) {
//					this.appendLog("职员编号为" + stdcode + "更新失败！错误信息：" + e.getMessage());
//				}
//			}else{
//				//5.创建成员
//				try {
//					String sendPost = HttpResultUtil.sendPost(WXQyhConfigInfo.CREATE_STAFF_URL + accessToken, wxStaff.toJSONObject().toString());
//					JSONObject returnMess = new JSONObject(sendPost);
//					if("0".equals(returnMess.get("errcode"))){
//						this.appendLog("职员编号为" + stdcode + ", 姓名为" + wxStaff.getName() + "创建成功！");
//						//插入员工推送记录表
//						
//					}else{
//						this.appendLog("职员编号为" + stdcode + "创建失败！错误信息：" + returnMess.get("errmsg"));
//					}
//				} catch (Exception e) {
//					this.appendLog("职员编号为" + stdcode + "创建失败！错误信息：" + e.getMessage());
//				}
//			}
//		}
//		
//		List<WXStaff> delStaffMess = getStaffMess(DELETE_FLAG, param, context);
//		for (WXStaff wxStaff : delStaffMess) {
//			String stdcode = wxStaff.getStdcode();
//			if(pushedStaffCode.containsKey(stdcode)){//4.删除成员
//				try {
//					JSONObject sendGet = HttpResultUtil.sendGet(WXQyhConfigInfo.DELETE_STAFF_URL + accessToken + "&userid=" + pushedStaffCode.get(stdcode));
//					JSONObject returnMess = new JSONObject(sendGet);
//					if("0".equals(returnMess.get("errcode"))){
//						this.appendLog("职员编号为" + stdcode + ", 姓名为" + wxStaff.getName() + "删除成功！");
//					}else{
//						this.appendLog("职员编号为" + stdcode + "删除失败！错误信息：" + returnMess.get("errmsg"));
//					}
//				} catch (Exception e) {
//					this.appendLog("职员编号为" + stdcode + "删除失败！错误信息：" + e.getMessage());
//				}
//			}
//		}
//		//6.删除部门
//		for (String willDeleteCode : pushedDepartmentCode) {
//			if(!willCreateOrUpdCode.contains(willDeleteCode)){
//				try {
//					JSONObject sendGet = HttpResultUtil.sendGet(WXQyhConfigInfo.DELETE_DEPARTMENT_URL + accessToken + "&id=" + willDeleteCode);
//					JSONObject returnMess = new JSONObject(sendGet);
//					if("0".equals(returnMess.get("errcode"))){
//						this.appendLog("部门编号为" + willDeleteCode + "删除成功！");
//					}else{
//						this.appendLog("部门编号为" + willDeleteCode + "删除失败！错误信息：" + returnMess.get("errmsg"));
//					}
//				} catch (Exception e) {
//					this.appendLog("部门编号为" + willDeleteCode + "删除失败！错误信息：" + e.getMessage());
//				}
//			}
//		}
		return true;
	}

	private int updateDeptRecord(Context context, String stdcode, String id) {
		StringBuffer updateDeptSql = new StringBuffer();
		updateDeptSql.append("define update updateDeptRecord(@id string, @stdcode string)");
		updateDeptSql.append("begin");
		updateDeptSql.append("  update MD_DEPARTMENT_PUSHRECORD as t set t.wxcode = @id where t.stdcode = @stdcode");
		updateDeptSql.append("end");
		DBCommand dbCommand = null;
		int result = 0;
		try {
			dbCommand = context.prepareStatement(updateDeptSql);
			dbCommand.setArgumentValues(id, stdcode);
			result = dbCommand.executeUpdate();
		} finally {
			dbCommand.unuse();
		}
		return result;
	}

	private int insertDeptRecord(Context context, String stdcode, String id) {
		StringBuffer insertDeptSql = new StringBuffer();
		insertDeptSql.append("define insert createDeptRecord(@recid guid, @stdcode string, @id string)");
		insertDeptSql.append("begin");
		insertDeptSql.append("  insert into MD_DEPARTMENT_PUSHRECORD(RECID, DEPARTMENT, WXCODE, ISPUSHWXFLAG) ");
		insertDeptSql.append("                                values(@recid, @stdcode, @id, true) ");
		insertDeptSql.append("end");
		DBCommand dbCommand = null;
		int result = 0;
		try {
			dbCommand = context.prepareStatement(insertDeptSql);
			dbCommand.setArgumentValues(GUID.randomID(), stdcode, id);
			result = dbCommand.executeUpdate();
		} finally {
			dbCommand.unuse();
		}
		return result;
	}
	
	private int insertStaffRecord(Context context, String stdcode, String yuzh) {
		StringBuffer insertStaffSql = new StringBuffer();
		insertStaffSql.append("define insert createStaffRecord(@recid guid, @stdcode string, @yuzh string)");
		insertStaffSql.append("begin");
		insertStaffSql.append("  insert into MD_STAFF_PUSHRECORD(RECID, STAFF, YUZH, ISPUSHWXFLAG) ");
		insertStaffSql.append("                           values(@recid, @stdcode, @yuzh, true) ");
		insertStaffSql.append("end");
		DBCommand dbCommand = null;
		int result = 0;
		try {
			dbCommand = context.prepareStatement(insertStaffSql);
			dbCommand.setArgumentValues(GUID.randomID(), stdcode, yuzh);
			result = dbCommand.executeUpdate();
		} finally {
			dbCommand.unuse();
		}
		return result;
	}

	/**
	 * 
	 * @param str 传入格式为  01, 02, 05
	 * @return ('01','02','05')
	 */
	private String getCodeRange(String str){
		StringBuffer codeRange = new StringBuffer("(");
		String[] split = str.split(",");
		for (String string : split) {
			if(!codeRange.toString().equals("(")){
				codeRange.append(",");
			}
			codeRange.append("'").append(string.trim()).append("'");
		}
		codeRange.append(")");
		return codeRange.toString();
	}
	/**
	 * 获得已推送的部门编号
	 * @return
	 */
	public Map<String,String> getPushedDepartmentCode(Context context){
		StringBuffer getCodeSql = new StringBuffer();
		getCodeSql.append("define query getDepartmentCode() \n");
		getCodeSql.append("begin \n");
		getCodeSql.append("  select '1'+re.department as stdcode, \n");
		getCodeSql.append("         re.wxcode     as wxcode \n");
		getCodeSql.append("    from md_department_pushrecord as re\n");
		getCodeSql.append("   where 1 = 1 \n");
		getCodeSql.append("     and re.ispushwxflag = true \n");
		getCodeSql.append("  order by re.department desc\n");
		getCodeSql.append("end \n");
		DBCommand dbCommand = context.prepareStatement(getCodeSql);
		RecordSet rs = dbCommand.executeQuery();
		Map<String,String> codeMap = new HashMap<String,String>();
		while (rs.next()) {
			codeMap.put(rs.getFields().get(0).getString(), rs.getFields().get(1).getString());
		}
		return codeMap;
	}
	
	/**
	 * 获得待推送的部门信息
	 * @param context
	 * @return
	 */
	public List<WXDepartment> getDepartmentMess(Context context){
		StringBuffer getMessSql = new StringBuffer();
		getMessSql.append("define query getDepartmentMess() \n");
		getMessSql.append("begin \n");
		getMessSql.append("  select bm.stdname as name, \n");
		getMessSql.append("         '1'+p.stdcode  as parentid, \n");
		getMessSql.append("         1          as orderid, \n");
		getMessSql.append("         '1'+bm.stdcode as id \n");
		getMessSql.append("    from md_department as bm \n");
		getMessSql.append("    left join md_department as p \n");
		getMessSql.append("      on replace(to_char(bm.parents), '00' + to_char(bm.objectid), '') = to_char(p.parents) \n");
		getMessSql.append("   where 1 = 1 \n");
		getMessSql.append("     and bm.startflag = true \n");
		getMessSql.append("  order by bm.stdcode \n");
		getMessSql.append("end \n");
		DBCommand dbCommand = context.prepareStatement(getMessSql);
		RecordSet rs = dbCommand.executeQuery();
		List<WXDepartment> wxDepartmentList = new ArrayList<WXDepartment>();
		while (rs.next()) {
			RecordSetFieldContainer<? extends RecordSetField> fields = rs.getFields();
			WXDepartment wdept = new WXDepartment();
			wdept.setName(fields.get(0).getString());
			wdept.setParentid(fields.get(1).getString());
			wdept.setOrderid(fields.get(2).getInt());
			wdept.setId(fields.get(3).getString());
			wxDepartmentList.add(wdept);
		}
		return wxDepartmentList;
	}
	
	/**
	 * 获得已推送的职员编号
	 * @return
	 */
	public Map<String, String> getPushedStaffCode(Context context){
		StringBuffer getCodeSql = new StringBuffer();
		getCodeSql.append("define query getStaffCode() \n");
		getCodeSql.append("begin \n");
		getCodeSql.append("  select re.staff as code, \n");
		getCodeSql.append("         re.yuzh as yuzh \n");
		getCodeSql.append("    from md_staff_pushrecord as re\n");
		getCodeSql.append("   where 1 = 1 \n");
		getCodeSql.append("     and re.ispushwxflag = true \n");
		getCodeSql.append("end \n");
		DBCommand dbCommand = context.prepareStatement(getCodeSql);
		RecordSet rs = dbCommand.executeQuery();
		Map<String, String> pushedCodeMap = new HashMap<String, String>();
		while (rs.next()) {
			pushedCodeMap.put(rs.getFields().get(0).getString(), rs.getFields().get(1).getString());
		}
		return pushedCodeMap;
	}

	/**
	 * 获得待推送的职员信息
	 * @param context
	 * @return
	 */
	public List<WXStaff> getStaffMess(String actionFlag, RunnerParameter param, Context context){
		StringBuffer getMessSql = new StringBuffer();
		getMessSql.append("define query getStaffMess() \n");
		getMessSql.append("begin \n");
		getMessSql.append("  select zy.yuzh    as userid,\n");
		getMessSql.append("         zy.stdcode as stdcode, \n");
		getMessSql.append("         zy.stdname as name, \n");
		getMessSql.append("         zy.tel     as mobile, \n");
		getMessSql.append("         '1'+bm.stdcode as department, \n");
		getMessSql.append("         case when xb.stdcode = '01' then 2 when xb.stdcode = '02' then 1 end as gender \n");
		getMessSql.append("    from md_staff as zy \n");
		getMessSql.append("    left join md_department as bm \n");
		getMessSql.append("      on zy.departmentid = bm.recid \n");
		getMessSql.append("    left join md_sex as xb \n");
		getMessSql.append("      on xb.recid = zy.sex \n");
		getMessSql.append("    left join MD_RYZT as zt \n");
		getMessSql.append("      on zy.ryzt = zt.recid \n");
		getMessSql.append("    left join MD_PERSONTYPE as lb \n");
		getMessSql.append("      on zy.persontype = lb.recid \n");
		getMessSql.append("   where 1 = 1 \n");
		String str_personTypeList ;
		if(actionFlag.equals(DELETE_FLAG)){
			if (param.get("deleteRyztList") != null && param.get("deleteRyztList").length() > 2) {
				str_personTypeList = param.get("deleteRyztList").substring(1, param.get("deleteRyztList").length() - 1);
				getMessSql.append("     and zt.stdcode in" + getCodeRange(str_personTypeList) + " \n");
			}
		}
		if(actionFlag.equals(CREATE_FLAG)){
			getMessSql.append("     and zy.startflag = true \n");
			if (param.get("ryztList") != null && param.get("ryztList").length() > 2) {
				str_personTypeList = param.get("ryztList").substring(1, param.get("ryztList").length() - 1);
				getMessSql.append("     and zt.stdcode in" + getCodeRange(str_personTypeList) + " \n");
			}
			if (param.get("personTypeList") != null && param.get("personTypeList").length() > 2) {
				str_personTypeList = param.get("personTypeList").substring(1, param.get("personTypeList").length() - 1);
				getMessSql.append("     and lb.stdcode in" + getCodeRange(str_personTypeList) + " \n");
			}
		}
		getMessSql.append("end \n");
		DBCommand dbCommand = context.prepareStatement(getMessSql);
		RecordSet rs = dbCommand.executeQuery();
		List<WXStaff> wxStaffList = new ArrayList<WXStaff>();
		while (rs.next()) {
			RecordSetFieldContainer<? extends RecordSetField> fields = rs.getFields();
			WXStaff wxstaff = new WXStaff();
			wxstaff.setUserid(fields.get(0).getString());
			wxstaff.setStdcode(fields.get(1).getString());
			wxstaff.setName(fields.get(2).getString());
			wxstaff.setMobile(fields.get(3).getString());
			wxstaff.setDepartment(new String[]{fields.get(4).getString()});
			wxstaff.setGender(fields.get(5).getString());
			wxStaffList.add(wxstaff);
		}
		return wxStaffList;
	}
	
}
