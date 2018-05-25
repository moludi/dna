package com.jiuqi.eip.external.function;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import com.jiuqi.dna.ui.wt.InfomationException;
import com.jiuqi.expression.DataType;
import com.jiuqi.expression.ExpressionException;
import com.jiuqi.expression.base.DataContext;
import com.jiuqi.expression.data.AbstractData;
import com.jiuqi.expression.functions.Function;
import com.jiuqi.expression.nodes.NodeList;
import com.jiuqi.vacomm.utils.StringUtil;

public class ChineseToPinYinFunction extends Function {

	public ChineseToPinYinFunction() {
		super("ChineseToPinYin", "����ת��ƴ��", "EIP����", "", "");
		appendParameter("hanZi", "���������֣�", DataType.String);
		// �������������������Ϣ
		this.setDescription("������Ϣ��������ת����ƴ�� ��ȫƴ��");
	}

	@Override
	public int judgeResultType(NodeList parameters) {
		if (parameters.size() != 1) {
			return DataType.Error;
		}
		return DataType.Bool;
	}

	@Override
	public AbstractData callFunction(DataContext context, NodeList parameters)
			throws ExpressionException {
		String chineseName = parameters.get(0).computeResult(context).getAsString();
		if (StringUtil.isEmpty(chineseName)) {										// �ж��Ƿ�Ϊ��
			throw new InfomationException("��ʽHanZiToPinYinFunction����   ���� �����֣�  ����Ϊ�գ����鹫ʽ�����á�");
		}
		return AbstractData.valueOf(toHanyuPinyin(chineseName));
	}
 
	/**
	 * ������תΪ����ƴ��
	 * 
	 * @param chineselanguage
	 *            Ҫת��ƴ��������
	 */
	private String toHanyuPinyin(String ChineseLanguage) {
		char[] cl_chars = ChineseLanguage.trim().toCharArray();
		String hanyupinyin = "";
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);					// ���ƴ��ȫ��Сд
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);				// ��������
		defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		for (int i = 0; i < cl_chars.length; i++) {
			if (String.valueOf(cl_chars[i]).matches("[\u4e00-\u9fa5]+")) {			// ����ַ�������,������תΪ����ƴ��
				try {
					hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0];
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				hanyupinyin += cl_chars[i];											// ����ַ���������,��ת��
			}
		}
		return hanyupinyin;
	}
}
