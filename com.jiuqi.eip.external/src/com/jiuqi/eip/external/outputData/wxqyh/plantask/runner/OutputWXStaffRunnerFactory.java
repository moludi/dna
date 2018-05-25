package com.jiuqi.eip.external.outputData.wxqyh.plantask.runner;

import java.util.ArrayList;
import java.util.List;

import com.jiuqi.dna.bap.plantask.intf.runner.Runner;
import com.jiuqi.dna.bap.plantask.intf.runner.RunnerFactory;
import com.jiuqi.dna.bap.plantask.intf.runner.RunnerParameter;
import com.jiuqi.dna.core.situation.Situation;
import com.jiuqi.dna.core.type.GUID;
import com.jiuqi.dna.ui.common.constants.JWT;
import com.jiuqi.dna.ui.template.launch.TemplateLauncher;
import com.jiuqi.dna.ui.template.launch.TemplateWindow;
import com.jiuqi.dna.ui.wt.widgets.Control;

public class OutputWXStaffRunnerFactory extends RunnerFactory{
	public static final String RUNNER_GUID = "6TT8B8391012300113TT6826B897BD6C";
	public static final String RUNNER_TITLE = "微信企业号职员同步";
	public static final String RUNNER_DESCRIPTION = "从EIP获得相应职员数据，同步到微信企业号";
	public List<GUID> deleteRyztList = new ArrayList<GUID>();
	public List<GUID> ryztList = new ArrayList<GUID>();
	public List<GUID> personTypeList = new ArrayList<GUID>();

	public Runner createRunner() {
		return new OutputWXStaffRunner();
	}

	public String getDescription() {
		return RUNNER_DESCRIPTION;
	}

	public String getGuid() {
		return RUNNER_GUID;
	}

	public String getTitle() {
		return RUNNER_TITLE;
	}

	public boolean getTaskAssgined() {
		return true;
	}

	public String getTemplate() {
		return null;
	}

	public boolean isNewRunner() {
		return true;
	}

	public boolean hasSettingPage() {
		return true;
	}

	public void openSettingPage(Situation context, Control owner,RunnerParameter param) {
		TemplateWindow t = TemplateLauncher.openTemplateWindow(owner,
				"OutputWXStaffPage", JWT.MODAL | JWT.MINIMUM | JWT.MAXIMUM | JWT.CLOSE, 0, param);
		t.setSize(350, 400);
		t.setTitle("计划任务配置");
	}
}
