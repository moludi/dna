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
		super("SetOtherTableFieldValue", "�ѵ�ǰ���ݵ�ֵ��д��Ŀ����Ŀ���ֶ�", "EIP����", "", "");
	    appendParameter("aimtable[aimfield]", "��д����[��д�ֶ���]", DataType.Void);
	    appendParameter("sourcetable[connectid,sourcefield]", "������Դ��[�����ֶΣ�������Դ�ֶ�]", DataType.String);
	    appendParameter("value", "д�����ֵ", DataType.String);
	    StringBuffer description = new StringBuffer();
	    description.append("����ʾ����SetOtherTableFieldValue(AIMTABLE[AIMFIELD],");
	    description.append(" \"SOURCETABLE[CONNECTID,SOURCEFIELD])\", fieldtype \n");
	    description.append("�������壺�ѵ�ǰ���ݵ�ֵ��д��Ŀ����Ŀ���ֶΡ�\n");
	    description.append("����˵����\n");
	    description.append("����һ����д����[��д�ֶ���]��\n");
	    description.append("��������������Դ��[�����ֶΣ�������Դ�ֶ�]��\n");
	    description.append("����������д��ֵ�����ͣ���֧��boolean��guid��string��int��date��double��ע��Сд \n");
	    description.append("����ֵ˵��������boolean���͡�\n");
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
		// Ŀ����Ŀ���ֶ�
		ModelDataNode node0 = (ModelDataNode) parameters.get(0);
		ITable aimTable = node0.getTable();
		IField aimField = node0.getField();
		// Ŀ����������
		String connect = parameters.get(1).computeResult(context).getAsString();
		String sourceTableName = connect.substring(0, connect.indexOf("["));
		TableDefine sourceTableDefine = cxt.find(TableDefine.class,
				sourceTableName);
		if (sourceTableDefine == null) {
			throw new InfomationException("δ�ҵ���ʶΪ" + sourceTableName
					+ "�����鵥�ݹ�ʽSetOtherTableFieldValue�����á�");
		}
		String connectField = connect.substring(connect.indexOf("[") + 1,
				connect.indexOf(","));
		TableFieldDefine connectFieldDefine = sourceTableDefine.getFields().get(connectField.trim());
		if (connectFieldDefine == null) {
			throw new InfomationException("��" + sourceTableName + "��δ�ҵ���ʶΪ"
					+ connectField + "���ֶΣ����鵥�ݹ�ʽSetOtherTableFieldValue�����á�");
		}
		String sourceField = connect.substring(connect.indexOf(",") + 1,
				connect.indexOf("]"));
		TableFieldDefine sourceFieldDefine = sourceTableDefine.getFields().get(sourceField.trim());
		if (sourceFieldDefine == null) {
			throw new InfomationException("��" + sourceTableName + "��δ�ҵ���ʶΪ"
					+ sourceField + "���ֶΣ����鵥�ݹ�ʽSetOtherTableFieldValue�����á�");
		}
		String fieldType = parameters.get(2).computeResult(context)
				.getAsString();
		if (!fieldType.equals("guid") && !fieldType.equals("date")
				&& !fieldType.equals("int") && !fieldType.equals("double")
				&& !fieldType.equals("string") && !fieldType.equals("boolean")) {
			throw new InfomationException("��д��ֵ������" + fieldType
					+ "δʶ�����鵥�ݹ�ʽSetOtherTableFieldValue�����á�");
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
				throw new InfomationException("��ǰ���ݲ�����" + sourceTableName
						+ "���޷����SetOtherTableFieldValue��ִ�У����鹫ʽ���á�");
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
