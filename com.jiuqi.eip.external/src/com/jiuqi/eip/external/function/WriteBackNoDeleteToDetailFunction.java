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
		super("WriteBackNoDeleteToDetail", "回写本单据子表到目标子表", "EIP函数", "", "");
		
		appendParameter("detailTablesMapping", "表映射", DataType.String);
		appendParameter("fieldsMapping", "字段映射", DataType.String);
		appendParameter("relationRecid", "目标子表对应主表的主键", DataType.String);
		StringBuffer description = new StringBuffer();
		description.append("函数示例：WriteBackNoDeleteToDetail(\"HT_SJYFBM=XS_YJFPDITEM\",");
		description.append("\"YANFBM=JFSYB,YANFBMZB=ZB,FPXS=FPXS,AFSBUJE=HTHDJE\",");
		description.append("XS_YJFPD[CONTRACTID]) \n");
		description.append("函数意义：回写本单据子表数据到目标子表。  \n");
		description.append("参数说明：\n");
		description.append("参数一：表的映射，目标子表=本单据子表  \n");
		description.append("参数二：字段的映射，多个字段映射可用“,”分割，目标子表字段=本单据子表字段  \n");
		description.append("参数三：目标子表对应主表的主键  \n");
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
			throw new InfomationException("单据公式WriteBackToDetailTable参数（表映射）不能为空，请检查公式的配置。");
		}
		detailTablesMapping = detailTablesMapping.replaceAll(" ", "").toUpperCase();
		String[] detailTables = detailTablesMapping.split("=");
		if (detailTables.length != 2) {
			throw new InfomationException("单据公式WriteBackToDetailTable参数（表映射）不符合该参数要求，请检查公式的配置。");
		}
		String fieldsMapping = parameters.get(1).computeResult(context).getAsString();
		if (StringUtil.isEmpty(fieldsMapping)) {
			throw new InfomationException("单据公式WriteBackToDetailTable参数（字段映射）不能为空，请检查公式的配置。");
		}
		if (fieldsMapping.contains("，")) {
			throw new InfomationException("单据公式WriteBackToDetailTable参数（字段映射）不支持中文符号“，”，请检查公式的配置。");
		}
		String relationRecid = parameters.get(2).computeResult(context).getAsString();
		if (StringUtil.isEmpty(relationRecid)) {
			throw new InfomationException("单据公式WriteBackToDetailTable参数（目标子表对应主表的主键值）不能为空，请检查公式的配置。");
		}

		ModelDataContext modelContext = (ModelDataContext) context;
		BusinessModel model = modelContext.model;
		Context cxt = model.getContext();
		String aimDetailTableName = detailTables[0];
		String sourceDetailTableName = detailTables[1];
		if (StringUtil.isEmpty(aimDetailTableName)) {
			throw new InfomationException("单据公式WriteBackToDetailTable参数（表映射）目标子表为空，请检查公式的配置。");
		}
		ITable sourceDetailTable = model.getDefine().findDetailTable(sourceDetailTableName);
		if (sourceDetailTable == null) {
			throw new InfomationException("当前单据未找到标识为" + sourceDetailTable + "的子表，请检查单据公式WriteBackToDetailTable的配置。");
		}
		TableDefine aimTableDefine = cxt.find(TableDefine.class, aimDetailTableName);
		if (aimTableDefine == null) {
			throw new InfomationException("未找到标识为" + aimDetailTableName + "的子表，请检查单据公式WriteBackToDetailTable的配置。");
		}
		List<BusinessObject> detailDataList = model.getDetailTableDataByName(sourceDetailTableName);
		if (detailDataList == null || detailDataList.size() == 0) {
			throw new InfomationException("当前单据" + sourceDetailTableName + "子表记录为空，无法完成公式WriteBackToDetailTable要实现的回写。");
		}
		Map<TableFieldDefine, TableFieldDefine> fieldsMap = analysisFieldsMapping(model, aimTableDefine,
				sourceDetailTable, fieldsMapping);
		GUID relationID = GUID.valueOf(relationRecid);
		for (BusinessObject businessObject : detailDataList) {
			// 插入数据
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
	 * 通过公式配置的参数分析字段映射关系
	 * 
	 * @param model
	 * @param aimDetailTableDefine
	 *            目标子表定义
	 * @param sourceDetailTable
	 *            来源子表
	 * @param paramater
	 *            公式配置的参数（字段映射）
	 */
	private Map<TableFieldDefine, TableFieldDefine> analysisFieldsMapping(BusinessModel model,
			TableDefine aimDetailTableDefine, ITable sourceDetailTable, String paramater) {
		String fieldsMapping = paramater.replaceAll(" ", "").toUpperCase();
		String[] fieldsMappingList = fieldsMapping.split(",");
		if(fieldsMappingList.length == 0){
			throw new InfomationException("单据公式WriteBackToDetailTable参数（字段映射）不符合该参数要求，请检查公式的配置。");
		}
		Map<TableFieldDefine, TableFieldDefine> fieldsMap = new HashMap<TableFieldDefine, TableFieldDefine>();
		for (String item : fieldsMappingList) {
			String[] fields = item.split("=");
			if (fields.length != 2) {
				throw new InfomationException("单据公式WriteBackToDetailTable参数（字段映射）不符合该参数要求，请检查公式的配置。");
			}
			String aimField = fields[0];
			if (StringUtil.isEmpty(aimField)) {
				throw new InfomationException("单据公式WriteBackToDetailTable参数（字段映射）不符合该参数要求，请检查公式的配置。");
			}
			String sourceField = fields[1];
			TableFieldDefine sourceFieldDefine = sourceDetailTable.findField(sourceField).getField();
			if (sourceFieldDefine == null) {
				throw new InfomationException("单据子表" + sourceDetailTable.getName() + "中未找到标识为" + sourceField
						+ "的字段，请检查单据公式WriteBackToDetailTable的配置。");
			}
			TableFieldDefine aimFieldDefine = aimDetailTableDefine.findColumn(aimField);
			if (aimFieldDefine == null) {
				throw new InfomationException(aimDetailTableDefine.getName() + "表中未找到标识为" + aimField
						+ "的字段，请检查单据公式WriteBackToDetailTable的配置。");
			}
			fieldsMap.put(aimFieldDefine, sourceFieldDefine);
		}
		return fieldsMap;
	}
}