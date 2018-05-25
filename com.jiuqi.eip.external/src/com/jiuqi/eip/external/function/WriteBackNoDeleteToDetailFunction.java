package com.jiuqi.eip.external.function;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuqi.dna.bap.model.common.define.base.BusinessObject;
import com.jiuqi.dna.bap.model.common.define.intf.ITable;
import com.jiuqi.dna.bap.model.common.expression.ModelDataContext;
import com.jiuqi.dna.bap.model.common.runtime.base.BusinessModel;
import com.jiuqi.dna.core.Context;
import com.jiuqi.dna.core.def.query.InsertStatementDeclare;
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
import com.jiuqi.vacomm.utils.StringUtil;

public class WriteBackNoDeleteToDetailFunction extends Function {

	public WriteBackNoDeleteToDetailFunction() {
		super("WriteBackNoDeleteToDetail", "��д�������ӱ�Ŀ���ӱ�", "EIP����", "", "");
		
		appendParameter("detailTablesMapping", "��ӳ��", DataType.String);
		appendParameter("fieldsMapping", "�ֶ�ӳ��", DataType.String);
		appendParameter("relationRecid", "Ŀ���ӱ��Ӧ���������", DataType.String);
		StringBuffer description = new StringBuffer();
		description.append("����ʾ����WriteBackNoDeleteToDetail(\"HT_SJYFBM=XS_YJFPDITEM\",");
		description.append("\"YANFBM=JFSYB,YANFBMZB=ZB,FPXS=FPXS,AFSBUJE=HTHDJE\",");
		description.append("XS_YJFPD[CONTRACTID]) \n");
		description.append("�������壺��д�������ӱ����ݵ�Ŀ���ӱ�  \n");
		description.append("����˵����\n");
		description.append("����һ�����ӳ�䣬Ŀ���ӱ�=�������ӱ�  \n");
		description.append("���������ֶε�ӳ�䣬����ֶ�ӳ����á�,���ָĿ���ӱ��ֶ�=�������ӱ��ֶ�  \n");
		description.append("��������Ŀ���ӱ��Ӧ���������  \n");
		this.setDescription(description.toString());
	}

	@Override
	public int judgeResultType(NodeList parameters) {
		if (parameters.size() != 3) {
			return DataType.Error;
		}
		return DataType.Bool;
	}

	@Override
	public AbstractData callFunction(DataContext context, NodeList parameters) throws ExpressionException {
		String detailTablesMapping = parameters.get(0).computeResult(context).getAsString();
		if (StringUtil.isEmpty(detailTablesMapping)) {
			throw new InfomationException("���ݹ�ʽWriteBackToDetailTable��������ӳ�䣩����Ϊ�գ����鹫ʽ�����á�");
		}
		detailTablesMapping = detailTablesMapping.replaceAll(" ", "").toUpperCase();
		String[] detailTables = detailTablesMapping.split("=");
		if (detailTables.length != 2) {
			throw new InfomationException("���ݹ�ʽWriteBackToDetailTable��������ӳ�䣩�����ϸò���Ҫ�����鹫ʽ�����á�");
		}
		String fieldsMapping = parameters.get(1).computeResult(context).getAsString();
		if (StringUtil.isEmpty(fieldsMapping)) {
			throw new InfomationException("���ݹ�ʽWriteBackToDetailTable�������ֶ�ӳ�䣩����Ϊ�գ����鹫ʽ�����á�");
		}
		if (fieldsMapping.contains("��")) {
			throw new InfomationException("���ݹ�ʽWriteBackToDetailTable�������ֶ�ӳ�䣩��֧�����ķ��š����������鹫ʽ�����á�");
		}
		String relationRecid = parameters.get(2).computeResult(context).getAsString();
		if (StringUtil.isEmpty(relationRecid)) {
			throw new InfomationException("���ݹ�ʽWriteBackToDetailTable������Ŀ���ӱ��Ӧ���������ֵ������Ϊ�գ����鹫ʽ�����á�");
		}

		ModelDataContext modelContext = (ModelDataContext) context;
		BusinessModel model = modelContext.model;
		Context cxt = model.getContext();
		String aimDetailTableName = detailTables[0];
		String sourceDetailTableName = detailTables[1];
		if (StringUtil.isEmpty(aimDetailTableName)) {
			throw new InfomationException("���ݹ�ʽWriteBackToDetailTable��������ӳ�䣩Ŀ���ӱ�Ϊ�գ����鹫ʽ�����á�");
		}
		ITable sourceDetailTable = model.getDefine().findDetailTable(sourceDetailTableName);
		if (sourceDetailTable == null) {
			throw new InfomationException("��ǰ����δ�ҵ���ʶΪ" + sourceDetailTable + "���ӱ����鵥�ݹ�ʽWriteBackToDetailTable�����á�");
		}
		TableDefine aimTableDefine = cxt.find(TableDefine.class, aimDetailTableName);
		if (aimTableDefine == null) {
			throw new InfomationException("δ�ҵ���ʶΪ" + aimDetailTableName + "���ӱ����鵥�ݹ�ʽWriteBackToDetailTable�����á�");
		}
		List<BusinessObject> detailDataList = model.getDetailTableDataByName(sourceDetailTableName);
		if (detailDataList == null || detailDataList.size() == 0) {
			throw new InfomationException("��ǰ����" + sourceDetailTableName + "�ӱ��¼Ϊ�գ��޷���ɹ�ʽWriteBackToDetailTableҪʵ�ֵĻ�д��");
		}
		Map<TableFieldDefine, TableFieldDefine> fieldsMap = analysisFieldsMapping(model, aimTableDefine,
				sourceDetailTable, fieldsMapping);
		GUID relationID = GUID.valueOf(relationRecid);
		for (BusinessObject businessObject : detailDataList) {
			// ��������
			InsertStatementDeclare insert = model.getContext().newInsertStatement(aimTableDefine);
			for (TableFieldDefine aimFieldDefine : fieldsMap.keySet()) {
				Object value = businessObject.getFieldValue(fieldsMap.get(aimFieldDefine).getName());
				insert.assignConst(aimFieldDefine, value);
			}
			insert.assignConst(aimTableDefine.findColumn("RECID"), GUID.randomID());
			insert.assignConst(aimTableDefine.findColumn("MRECID"), relationID);
			model.getContext().executeUpdate(insert);
		}
		return AbstractData.valueOf(1);
	}

	/**
	 * ͨ����ʽ���õĲ��������ֶ�ӳ���ϵ
	 * 
	 * @param model
	 * @param aimDetailTableDefine
	 *            Ŀ���ӱ���
	 * @param sourceDetailTable
	 *            ��Դ�ӱ�
	 * @param paramater
	 *            ��ʽ���õĲ������ֶ�ӳ�䣩
	 */
	private Map<TableFieldDefine, TableFieldDefine> analysisFieldsMapping(BusinessModel model,
			TableDefine aimDetailTableDefine, ITable sourceDetailTable, String paramater) {
		String fieldsMapping = paramater.replaceAll(" ", "").toUpperCase();
		String[] fieldsMappingList = fieldsMapping.split(",");
		if(fieldsMappingList.length == 0){
			throw new InfomationException("���ݹ�ʽWriteBackToDetailTable�������ֶ�ӳ�䣩�����ϸò���Ҫ�����鹫ʽ�����á�");
		}
		Map<TableFieldDefine, TableFieldDefine> fieldsMap = new HashMap<TableFieldDefine, TableFieldDefine>();
		for (String item : fieldsMappingList) {
			String[] fields = item.split("=");
			if (fields.length != 2) {
				throw new InfomationException("���ݹ�ʽWriteBackToDetailTable�������ֶ�ӳ�䣩�����ϸò���Ҫ�����鹫ʽ�����á�");
			}
			String aimField = fields[0];
			if (StringUtil.isEmpty(aimField)) {
				throw new InfomationException("���ݹ�ʽWriteBackToDetailTable�������ֶ�ӳ�䣩�����ϸò���Ҫ�����鹫ʽ�����á�");
			}
			String sourceField = fields[1];
			TableFieldDefine sourceFieldDefine = sourceDetailTable.findField(sourceField).getField();
			if (sourceFieldDefine == null) {
				throw new InfomationException("�����ӱ�" + sourceDetailTable.getName() + "��δ�ҵ���ʶΪ" + sourceField
						+ "���ֶΣ����鵥�ݹ�ʽWriteBackToDetailTable�����á�");
			}
			TableFieldDefine aimFieldDefine = aimDetailTableDefine.findColumn(aimField);
			if (aimFieldDefine == null) {
				throw new InfomationException(aimDetailTableDefine.getName() + "����δ�ҵ���ʶΪ" + aimField
						+ "���ֶΣ����鵥�ݹ�ʽWriteBackToDetailTable�����á�");
			}
			fieldsMap.put(aimFieldDefine, sourceFieldDefine);
		}
		return fieldsMap;
	}
}