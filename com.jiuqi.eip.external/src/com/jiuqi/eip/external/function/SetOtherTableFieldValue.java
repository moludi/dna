package com.jiuqi.eip.external.function;

import java.util.List;

import com.jiuqi.dna.bap.model.common.define.base.BusinessObject;
import com.jiuqi.dna.bap.model.common.define.intf.IField;
import com.jiuqi.dna.bap.model.common.define.intf.ITable;
import com.jiuqi.dna.bap.model.common.expression.ModelDataContext;
import com.jiuqi.dna.bap.model.common.expression.ModelDataNode;
import com.jiuqi.dna.bap.model.common.runtime.base.BusinessModel;
import com.jiuqi.dna.core.Context;
import com.jiuqi.dna.core.da.DBCommand;
import com.jiuqi.dna.core.def.table.TableDefine;
import com.jiuqi.dna.core.def.table.TableFieldDefine;
import com.jiuqi.dna.core.type.GUID;
import com.jiuqi.dna.ui.wt.InfomationException;
import com.jiuqi.expression.DataType;
import com.jiuqi.expression.ExpressionException;
import com.jiuqi.expression.base.DataContext;
import com.jiuqi.expression.data.AbstractData;
import com.jiuqi.expression.functions.Function;
import com.jiuqi.expression.nodes.NodeList;

public class SetOtherTableFieldValue extends Function {

	public SetOtherTableFieldValue() {
		super("SetOtherTableFieldValue", "把当前单据的值回写到目标表的目标字段", "EIP函数", "", "");
	    appendParameter("aimtable[aimfield]", "回写表名[回写字段名]", DataType.Void);
	    appendParameter("sourcetable[connectid,sourcefield]", "数据来源表[关联字段，数据来源字段]", DataType.String);
	    appendParameter("value", "写入参数值", DataType.String);
	    StringBuffer description = new StringBuffer();
	    description.append("函数示例：SetOtherTableFieldValue(AIMTABLE[AIMFIELD],");
	    description.append(" \"SOURCETABLE[CONNECTID,SOURCEFIELD])\", fieldtype \n");
	    description.append("函数意义：把当前单据的值回写到目标表的目标字段。\n");
	    description.append("参数说明：\n");
	    description.append("参数一：回写表名[回写字段名]；\n");
	    description.append("参数二：数据来源表[关联字段，数据来源字段]；\n");
	    description.append("参数三：回写的值的类型，仅支持boolean、guid、string、int、date、double，注意小写 \n");
	    description.append("返回值说明：返回boolean类型。\n");
	    setDescription(description.toString());
	}

	@Override
	public int judgeResultType(NodeList parameters) {
		if (parameters.size() != 3) {
			return DataType.Error;
		}
		return DataType.Bool;
	}

	@Override
	public AbstractData callFunction(DataContext context, NodeList parameters)
			throws ExpressionException {
		ModelDataContext modelContext = (ModelDataContext) context;
		BusinessModel model = modelContext.model;
		Context cxt = model.getContext();
		// 目标表和目标字段
		ModelDataNode node0 = (ModelDataNode) parameters.get(0);
		ITable aimTable = node0.getTable();
		IField aimField = node0.getField();
		// 目标表的主键列
		String connect = parameters.get(1).computeResult(context).getAsString();
		String sourceTableName = connect.substring(0, connect.indexOf("["));
		TableDefine sourceTableDefine = cxt.find(TableDefine.class,
				sourceTableName);
		if (sourceTableDefine == null) {
			throw new InfomationException("未找到标识为" + sourceTableName
					+ "表，请检查单据公式SetOtherTableFieldValue的配置。");
		}
		String connectField = connect.substring(connect.indexOf("[") + 1,
				connect.indexOf(","));
		TableFieldDefine connectFieldDefine = sourceTableDefine.getFields().get(connectField.trim());
		if (connectFieldDefine == null) {
			throw new InfomationException("表" + sourceTableName + "中未找到标识为"
					+ connectField + "的字段，请检查单据公式SetOtherTableFieldValue的配置。");
		}
		String sourceField = connect.substring(connect.indexOf(",") + 1,
				connect.indexOf("]"));
		TableFieldDefine sourceFieldDefine = sourceTableDefine.getFields().get(sourceField.trim());
		if (sourceFieldDefine == null) {
			throw new InfomationException("表" + sourceTableName + "中未找到标识为"
					+ sourceField + "的字段，请检查单据公式SetOtherTableFieldValue的配置。");
		}
		String fieldType = parameters.get(2).computeResult(context)
				.getAsString();
		if (!fieldType.equals("guid") && !fieldType.equals("date")
				&& !fieldType.equals("int") && !fieldType.equals("double")
				&& !fieldType.equals("string") && !fieldType.equals("boolean")) {
			throw new InfomationException("回写的值的类型" + fieldType
					+ "未识别，请检查单据公式SetOtherTableFieldValue的配置。");
		}
		String billMasterTable = model.getModelData().getMaster().getTable()
				.getName();
		if (billMasterTable.equals(sourceTableName)) {
			GUID recid = (GUID) model.getModelData().getMaster()
					.getFieldValue(connectField);
			Object fieldValue = model.getModelData().getMaster()
					.getFieldValue(sourceField);
			updateAimField(cxt, aimTable, aimField, recid, fieldValue,
					fieldType);
		} else {
			List<BusinessObject> detail = model.getModelData().getDetail(
					model.getDefine(), sourceTableName);
			if (detail == null) {
				throw new InfomationException("当前单据不包含" + sourceTableName
						+ "表，无法完成SetOtherTableFieldValue的执行，请检查公式配置。");
			}
			dealDetails(cxt, aimTable, aimField, sourceTableName, connectField,
					detail, sourceField, fieldType);
		}
		return AbstractData.valueOf(true);
	}

	private void dealDetails(Context cxt, ITable aimTable, IField aimField,
			String sourceTableName, String connectField,
			List<BusinessObject> detail, String sourceField, String fieldType) {
		for (int i = 0; i < detail.size(); i++) {
			BusinessObject record = detail.get(i);
			GUID recid = record.getValueAsGUID(connectField);
			Object fieldValue = record.getFieldValue(sourceField);
			updateAimField(cxt, aimTable, aimField, recid, fieldValue,
					fieldType);
		}
	}

	private void updateAimField(Context context, ITable aimTable,
			IField aimField, GUID recid, Object fieldValue, String fieldType) {
		StringBuffer updateSql = new StringBuffer();
		updateSql.append("define update updateAimField(@fieldValue "
				+ fieldType + ", @recid guid) \n");
		updateSql.append("begin \n");
		updateSql.append("  update " + aimTable.getName() + " as t \n");
		updateSql.append("    set " + aimField.getName() + " = @fieldValue \n");
		updateSql.append("  where t.recid = @recid \n");
		updateSql.append("end \n");
		DBCommand dbCommand = context.prepareStatement(updateSql);
		dbCommand.setArgumentValues(fieldValue, recid);
		dbCommand.executeUpdate();
		dbCommand.unuse();
	}
}
