package com.jiuqi.eip.external.outputData.wxqyh.plantask.runner;

import java.util.ArrayList;
import java.util.List;

import com.jiuqi.dna.bap.basedata.common.control.BaseDataInputControl;
import com.jiuqi.dna.bap.basedata.common.util.BaseDataCenter;
import com.jiuqi.dna.bap.basedata.intf.facade.FBaseDataInfo;
import com.jiuqi.dna.bap.basedata.intf.facade.FBaseDataObject;
import com.jiuqi.dna.bap.basedata.intf.type.AuthType;
import com.jiuqi.dna.bap.basedata.intf.type.InputStyle;
import com.jiuqi.dna.bap.basedata.intf.type.SelectMode;
import com.jiuqi.dna.bap.basedata.intf.util.IMultiSelectCallBack;
import com.jiuqi.dna.bap.plantask.intf.message.M0801_PlantaskSet;
import com.jiuqi.dna.bap.plantask.intf.runner.RunnerParameter;
import com.jiuqi.dna.ui.template.launch.TemplateWindow;
import com.jiuqi.dna.ui.wt.events.ActionEvent;
import com.jiuqi.dna.ui.wt.graphics.Color;
import com.jiuqi.dna.ui.wt.layouts.FillLayout;
import com.jiuqi.dna.ui.wt.widgets.Composite;
import com.jiuqi.dna.ui.wt.widgets.Form;
import com.jiuqi.dna.ui.wt.widgets.Label;
import com.jiuqi.dna.ui.wt.widgets.MessageDialog;

@SuppressWarnings("deprecation")
public class OutputWXStaffPage<TControls extends OutputWXStaffPageControls> extends Form<OutputWXStaffPageControls> {

	private BaseDataInputControl cmpRyzt;
	private BaseDataInputControl cmpPersonType;
	private List<String> ryztList = new ArrayList<String>();
	private List<String> personTypeList = new ArrayList<String>();
	private BaseDataInputControl cmpDeleteRyzt;
	private List<String> deleteRyztList = new ArrayList<String>();
	private RunnerParameter parameter;

	public OutputWXStaffPage(Composite parent, RunnerParameter parameter) {
		super(parent);
		this.parameter = parameter;
		this.initPage(parameter);
	}
	
	private void initPage(RunnerParameter parameter) {
		ArrayList<FBaseDataObject> list;
		int n;
		String stdcode;
		FBaseDataObject baseDataObject;
		String[] arrstring;
		int n2;
		String[] guids;
		String str;
		this.controls.cmp_2.setLayout(new FillLayout(0));
		this.controls.cmp_4.setLayout(new FillLayout(0));
		this.controls.cmp_5.setLayout(new FillLayout(0));
		this.controls.cmp_3.setLayout(new FillLayout());
		Label label1 = new Label(this.controls.cmp_3, 64);
		label1.setForeground(Color.COLOR_GRAY);
		label1.setText("    此条件为控制任务中删除指定职员的关联用户和清空该职员的域账号。系统会根据指定的条件，删除符合条件的职员的关联用户并且清空域账号；如果不填写，系统不会执行任何操作。");
		this.controls.cmp_6.setLayout(new FillLayout());
		Label label2 = new Label(this.controls.cmp_6, 64);
		label2.setForeground(Color.COLOR_GRAY);
		label2.setText("    此条件为系统自动生成用户的筛选条件。系统会根据筛选条件自动生成符合条件的职员用户。必选，如果不填写，系统不会执行任何操作。");
		this.controls.txt_corp.setText(parameter.get("corpID"));
		this.controls.txt_secret.setText(parameter.get("secret"));
		this.cmpDeleteRyzt = BaseDataCenter.newInputControl(this.controls.cmp_2);
		this.cmpDeleteRyzt.setTableName("MD_RYZT");
		this.cmpDeleteRyzt.setInputStyle(InputStyle.POPUP);
		this.cmpDeleteRyzt.setSelectMode(SelectMode.MULTI);
		this.cmpDeleteRyzt.setAuthType(AuthType.ACCESS);
		this.cmpDeleteRyzt.setShowSelectAll(true);
		this.cmpDeleteRyzt.setCanInputBranchObject(false);
		this.cmpDeleteRyzt.setMultiSelectCallBack(new IMultiSelectCallBack() {

			public void doMultiSelect(FBaseDataObject[] arg0) {
				OutputWXStaffPage.this.deleteRyztList.clear();
				if (arg0 != null && arg0.length > 0) {
					FBaseDataObject[] arrfBaseDataObject = arg0;
					int n = arrfBaseDataObject.length;
					int n2 = 0;
					while (n2 < n) {
						FBaseDataObject tFBaseDataObject = arrfBaseDataObject[n2];
						OutputWXStaffPage.this.deleteRyztList.add(tFBaseDataObject.getStdCode());
						++n2;
					}
				}
			}

			public boolean canList(FBaseDataObject object) {
				return true;
			}

			public boolean canListGroup(FBaseDataInfo arg0) {
				return true;
			}

			public boolean canListGroup(FBaseDataObject arg0) {
				return true;
			}

			public boolean canSelect(FBaseDataObject arg0) {
				return true;
			}

			public void doSelect(FBaseDataObject arg0) {
			}
		});
		this.cmpDeleteRyzt.buildControl();
		this.cmpRyzt = BaseDataCenter.newInputControl(this.controls.cmp_4);
		this.cmpRyzt.setTableName("MD_RYZT");
		this.cmpRyzt.setInputStyle(InputStyle.POPUP);
		this.cmpRyzt.setSelectMode(SelectMode.MULTI);
		this.cmpRyzt.setAuthType(AuthType.ACCESS);
		this.cmpRyzt.setShowSelectAll(true);
		this.cmpRyzt.setCanInputBranchObject(false);
		this.cmpRyzt.setMultiSelectCallBack(new IMultiSelectCallBack() {

			public void doMultiSelect(FBaseDataObject[] arg0) {
				OutputWXStaffPage.this.ryztList.clear();
				if (arg0 != null && arg0.length > 0) {
					FBaseDataObject[] arrfBaseDataObject = arg0;
					int n = arrfBaseDataObject.length;
					int n2 = 0;
					while (n2 < n) {
						FBaseDataObject tFBaseDataObject = arrfBaseDataObject[n2];
						OutputWXStaffPage.this.ryztList.add(tFBaseDataObject.getStdCode());
						++n2;
					}
				}
			}

			public boolean canList(FBaseDataObject object) {
				return true;
			}

			public boolean canListGroup(FBaseDataInfo arg0) {
				return true;
			}

			public boolean canListGroup(FBaseDataObject arg0) {
				return true;
			}

			public boolean canSelect(FBaseDataObject arg0) {
				return true;
			}

			public void doSelect(FBaseDataObject arg0) {
			}
		});
		this.cmpRyzt.buildControl();
		this.cmpPersonType = BaseDataCenter.newInputControl(this.controls.cmp_5);
		this.cmpPersonType.setTableName("MD_PERSONTYPE");
		this.cmpPersonType.setInputStyle(InputStyle.POPUP);
		this.cmpPersonType.setSelectMode(SelectMode.MULTI);
		this.cmpPersonType.setAuthType(AuthType.ACCESS);
		this.cmpPersonType.setShowSelectAll(true);
		this.cmpPersonType.setCanInputBranchObject(false);
		this.cmpPersonType.setMultiSelectCallBack(new IMultiSelectCallBack() {

			public void doMultiSelect(FBaseDataObject[] arg0) {
				OutputWXStaffPage.this.personTypeList.clear();
				if (arg0 != null && arg0.length > 0) {
					FBaseDataObject[] arrfBaseDataObject = arg0;
					int n = arrfBaseDataObject.length;
					int n2 = 0;
					while (n2 < n) {
						FBaseDataObject tFBaseDataObject = arrfBaseDataObject[n2];
						OutputWXStaffPage.this.personTypeList.add(tFBaseDataObject.getStdCode());
						++n2;
					}
				}
			}

			public boolean canList(FBaseDataObject object) {
				return true;
			}

			public boolean canListGroup(FBaseDataInfo arg0) {
				return true;
			}

			public boolean canListGroup(FBaseDataObject arg0) {
				return true;
			}

			public boolean canSelect(FBaseDataObject arg0) {
				return true;
			}

			public void doSelect(FBaseDataObject arg0) {
			}
		});
		this.cmpPersonType.buildControl();
		if (parameter.get("deleteRyztList") != null && parameter.get("deleteRyztList").length() > 2) {
			str = parameter.get("deleteRyztList");
			guids = str.substring(1, str.length() - 1).split(", ");
			list = new ArrayList<FBaseDataObject>();
			arrstring = guids;
			n2 = arrstring.length;
			n = 0;
			while (n < n2) {
				stdcode = arrstring[n];
				baseDataObject = BaseDataCenter.findObject(this.getContext(), "MD_RYZT", stdcode);
				if (baseDataObject != null) {
					list.add(baseDataObject);
				}
				++n;
			}
			this.cmpDeleteRyzt.setSelections(list);
		}
		if (parameter.get("ryztList") != null && parameter.get("ryztList").length() > 2) {
			str = parameter.get("ryztList");
			guids = str.substring(1, str.length() - 1).split(", ");
			list = new ArrayList<FBaseDataObject>();
			arrstring = guids;
			n2 = arrstring.length;
			n = 0;
			while (n < n2) {
				stdcode = arrstring[n];
				baseDataObject = BaseDataCenter.findObject(this.getContext(), "MD_RYZT",stdcode);
				if (baseDataObject != null) {
					list.add(baseDataObject);
				}
				++n;
			}
			this.cmpRyzt.setSelections(list);
		}
		if (parameter.get("personTypeList") != null && parameter.get("personTypeList").length() > 2) {
			str = parameter.get("personTypeList");
			guids = str.substring(1, str.length() - 1).split(", ");
			list = new ArrayList<FBaseDataObject>();
			arrstring = guids;
			n2 = arrstring.length;
			n = 0;
			while (n < n2) {
				stdcode = arrstring[n];
				baseDataObject = BaseDataCenter.findObject(this.getContext(), "MD_PERSONTYPE",stdcode);
				if (baseDataObject != null) {
					list.add(baseDataObject);
				}
				++n;
			}
			this.cmpPersonType.setSelections(list);
		}
	}

	protected void on_btn_1_Action(ActionEvent actionEvent) {
		if (this.ryztList.size() > 0 && this.personTypeList.size() > 0) {
			this.parameter.put("corpID", this.controls.txt_corp.getText());
			this.parameter.put("secret", this.controls.txt_secret.getText());
			this.parameter.put("deleteRyztList", this.deleteRyztList.toString());
			this.parameter.put("ryztList", this.ryztList.toString());
			this.parameter.put("personTypeList", this.personTypeList.toString());
			M0801_PlantaskSet msg = new M0801_PlantaskSet(this.parameter.toString());
			this.getContext().bubbleMessage(msg);
			this.getContext().bubbleMessage(new TemplateWindow.CloseMessage());
		} else {
			MessageDialog.alert("生成用户的职员的人员状态和人员类别未选择！");
		}
	}

	protected void on_btn_2_Action(ActionEvent actionEvent) {
		this.getContext().bubbleMessage(new TemplateWindow.CloseMessage());
	}
	
}