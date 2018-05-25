package com.jiuqi.eip.external.function;

import java.util.Calendar;
import java.util.Date;

import com.jiuqi.dna.ui.wt.InfomationException;
import com.jiuqi.expression.DataType;
import com.jiuqi.expression.ExpressionException;
import com.jiuqi.expression.base.DataContext;
import com.jiuqi.expression.data.AbstractData;
import com.jiuqi.expression.functions.Function;
import com.jiuqi.expression.nodes.NodeList;

public class GetWeekDaysDuringPeriodFunction extends Function {

	public GetWeekDaysDuringPeriodFunction() {
		super("GetWeekDaysDuringPeriod","����������ڼ�Ĺ�����","ͨ�ú���","","");
		
		this.appendParameter("day", "������", DataType.Int);

		this.appendParameter("endDate", "ָ������", DataType.Date);
		
		this.appendParameter("includeFlag", "�Ƿ������ĩ��1���������0����������", DataType.Int);

		this.appendParameter("directionFlag", "���㷽��1����ָ���������ϸ������ռ��������0����ָ���������¸������ռ��������", DataType.Int);

		this.setDescription("���������뿪ʼ���ڣ��������ڣ�����������ڼ������");
	}
	
	@Override
	public int judgeResultType(NodeList parameters) {
		if (parameters.size() != 4) {
			return DataType.Error;
		}
		return DataType.Bool;
	}

	@Override
	public AbstractData callFunction(DataContext context, NodeList parameters)
			throws ExpressionException {
		
		int countDay = parameters.get(0).computeResult(context).getAsInt();
		
		long endDate = parameters.get(1).computeResult(context).getAsDate();
		Calendar endCal=Calendar.getInstance();
		endCal.setTime(new Date(endDate));
		
		String includeFlag = parameters.get(2).computeResult(context).getAsString().trim();
		if(!includeFlag.equals("1") && !includeFlag.equals("0")){
			throw new InfomationException("GetWeekDaysDuringPeriod��ʽ�Ƿ������ĩ��ѡ��1��0��");
		}
		int include = Integer.parseInt(includeFlag);
		
		String directionFlag = parameters.get(3).computeResult(context).getAsString();
		if(!directionFlag.equals("1") && !directionFlag.equals("0")){
			throw new InfomationException("GetWeekDaysDuringPeriod��ʽ���㷽����ѡ��1��0��");
		}
		int direction = Integer.parseInt(directionFlag);
		
		return AbstractData.valueOf(getDays(countDay, endCal, include, direction));
	}

	private int getDays(int countDay, Calendar endCal, int include, int direction) {
		int endYear = endCal.get(Calendar.YEAR);
		int endMonth = endCal.get(Calendar.MONTH);//0-11
		int endDay = endCal.get(Calendar.DATE);
		Calendar beginCal = Calendar.getInstance();
		if(endDay < countDay && direction == 1){//ָ����С�ڼ�����
			if(endMonth == 0){
				beginCal.set(endYear - 1, 11, countDay);
			}else{
				beginCal.set(endYear, endMonth - 1, countDay);
			}
		}else if(endDay >= countDay && direction == 0){//ָ���մ��ڵ��ڼ�����
			if(endMonth == 11){
				beginCal.set(endYear + 1, 0, countDay);
			}else{
				beginCal.set(endYear, endMonth + 1, countDay);
			}
		}else{
			beginCal.set(endYear, endMonth, countDay);
		}
		if(direction == 1){
			return countDays(beginCal, endCal, include);
		}else{
			return countDays(endCal, beginCal, include);			
		}
	}

	private int countDays(Calendar beginCal, Calendar endCal, int include) {
		if(include == 0){
			int days = 0;
			while(isNotEqual(beginCal, endCal)){
				int weekday = beginCal.get(Calendar.DAY_OF_WEEK);
				if(weekday != 1 && weekday != 7){
					days++;
				}
				beginCal = getAfterDay(beginCal);
			}
			return days;
		}else{
			long bl = beginCal.getTimeInMillis();
		    long el = endCal.getTimeInMillis();
		    long ei = el - bl;    
		    int days = (int)(ei/(1000*60*60*24));
			return days;
		}
	}
	
	 private boolean isNotEqual(Calendar beginCal, Calendar endCal) {
		int beginYear = beginCal.get(Calendar.YEAR);  
		int beginMonth = beginCal.get(Calendar.MONTH) + 1;  
		int beginDay = beginCal.get(Calendar.DATE);
		int endYear = endCal.get(Calendar.YEAR);  
		int endMonth = endCal.get(Calendar.MONTH) + 1;  
		int endDay = endCal.get(Calendar.DATE);
		if(beginYear == endYear && beginMonth == endMonth && beginDay == endDay){
			return false;
		}
		return true;
	}

	/** 
     * ��ȡ��ǰʱ��ĺ�һ��ʱ�� 
     *  
     * @param cl 
     */  
    private Calendar getAfterDay(Calendar beginCal) {  
        Calendar c = Calendar.getInstance();
		// ʹ��set����ֱ������ʱ��ֵ  
        int beginYear = beginCal.get(Calendar.YEAR);  
		int beginMonth = beginCal.get(Calendar.MONTH);  
		int beginDay = beginCal.get(Calendar.DATE) + 1;
        c.set(Calendar.YEAR, beginYear);
        c.set(Calendar.MONTH, beginMonth);
        c.set(Calendar.DATE, beginDay);
        return c;  
    }  
	
}
