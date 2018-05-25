package com.jiuqi.eip.external.function;

import java.util.ArrayList;
import java.util.List;

import com.jiuqi.dna.bap.model.common.define.base.BusinessObject;
import com.jiuqi.dna.bap.model.common.define.intf.ITable;
import com.jiuqi.dna.bap.model.common.expression.ModelDataContext;
import com.jiuqi.dna.bap.model.common.runtime.base.BusinessModel;
import com.jiuqi.dna.core.Context;
import com.jiuqi.dna.core.da.RecordSet;
import com.jiuqi.dna.core.da.RecordSetField;
import com.jiuqi.dna.core.da.RecordSetFieldContainer;
import com.jiuqi.dna.core.def.query.QueryColumnDefine;
import com.jiuqi.dna.core.def.query.QueryStatementDefine;
import com.jiuqi.dna.core.type.GUID;
import com.jiuqi.dna.ui.wt.InfomationException;
import com.jiuqi.expression.DataType;
import com.jiuqi.expression.ExpressionException;
import com.jiuqi.expression.base.DataContext;
import com.jiuqi.expression.data.AbstractData;
import com.jiuqi.expression.functions.Function;
import com.jiuqi.expression.nodes.NodeList;

public class InsertAimDetailTableFunction extends Function {

	public InsertAimDetailTableFunction() {
		super("InsertAimDetailTable", "�������ӱ��������", "EIP����", "","");
		
		this.appendParameter("aimDetailTable", "�����ӱ�����", DataType.String);

		this.appendParameter("selectSQL", "���뵥���ӱ�����ݲ�ѯDNA-SQL���", DataType.String);
		
		this.setDescription("����ʾ����InsertDetailTableBySqlFunction(\"DetailTable\", \" select SubDataTable.RECID as PERSON, "
				+ "SubDataTable.STDNAME as PERSONNAME from SubDataTable as SubDataTable where 1 = 1 \" + \" and "
				+ "SubDataTable.unitId = guid'\" + Str(GetCurrOrgID()) + \"'\")�ر�ע�⣺��ѯDNA-SQL�����б�����Ҫ��ģ�Ͳ����ӱ���ֶ�����һ�¡�");
	}
	
	@Override
	public int judgeResultType(NodeList parameters) {
		if (parameters.size() != 2) {
			return DataType.Error;
		}
		return DataType.Bool;
	}

	@Override
	public AbstractData callFunction(DataContext context, NodeList parameters)throws ExpressionException {
		
		String aimDetailTable = parameters.get(0).computeResult(context).getAsString();
		
		ModelDataContext modelContext = (ModelDataContext) context;
		BusinessModel model = modelContext.model;
		Context cxt = model.getContext();
		String selectSQL =  parameters.get(1).computeResult(context).getAsString();
		//�������
		StringBuffer sql = new StringBuffer();
		sql.append("define query queryDetailData()\n");
		sql.append("begin \n");
		sql.append(selectSQL).append(" \n");
		sql.append("end \n");
		QueryStatementDefine query = (QueryStatementDefine) cxt.parseStatement(sql);
		RecordSet rs = cxt.openQuery(query);
		List<String> fieldsList = new ArrayList<String>();
		//�����ֶ�
		getAimTableFields(selectSQL, fieldsList);
		if(rs == null){
			throw new InfomationException("��ѯ���õ����Ϊ�ա�");
		}
		List<BusinessObject> detailTableRecordList = model.getDetailTableDataByName(aimDetailTable);
		if(detailTableRecordList.size() > 1){
			return AbstractData.valueOf(1);
		}
		
		int recordCount = rs.getRecordCount();
		ITable table = model.getITableByName(aimDetailTable);
		for(int i = detailTableRecordList.size(); i < recordCount; i++){
			BusinessObject businessObject = new BusinessObject(table);
			businessObject.setRECID(GUID.randomID());
			businessObject.setMRECID(model.getModelData().getMaster().getRECID());
			detailTableRecordList.add(businessObject);			
		}
		int curCursor = 0;
		while (rs.next()) {
			RecordSetFieldContainer<? extends RecordSetField> c = rs.getFields();
			for (String field : fieldsList) {
				String value = c.get((QueryColumnDefine) query.getColumn(field)).getString();
				model.getFieldVisitor().setGridFieldValue(aimDetailTable, field, curCursor, value);
			}
			curCursor++;
		}
		return AbstractData.valueOf(1);
	}

	private  void getAimTableFields(String selectSQL, List<String> fieldsList) {
		String[] split = selectSQL.substring(selectSQL.indexOf("select "), selectSQL.indexOf(" from")).split(" as ");
		for(int i = 0; i < split.length ; i++){
			if(i == 0){
				continue;
			}
			if(i == split.length - 1){
				fieldsList.add(split[i].trim());
			}else{
				fieldsList.add(split[i].substring(0, split[i].indexOf(",")).trim());
			}
		}
	}
	
	
}
