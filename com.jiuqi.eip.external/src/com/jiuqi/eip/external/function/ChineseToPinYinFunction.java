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
		super("ChineseToPinYin", "汉字转换拼音", "EIP函数", "", "");
		appendParameter("hanZi", "姓名（汉字）", DataType.String);
		// 设置这个函数的描述信息
		this.setDescription("描述信息：将汉字转换成拼音 （全拼）");
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
		if (StringUtil.isEmpty(chineseName)) {										// 判断是否为空
			throw new InfomationException("公式HanZiToPinYinFunction参数   姓名 （汉字）  不能为空，请检查公式的配置。");
		}
		return AbstractData.valueOf(toHanyuPinyin(chineseName));
	}
 
	/**
	 * 将文字转为汉语拼音
	 * 
	 * @param chineselanguage
	 *            要转成拼音的中文
	 */
	private String toHanyuPinyin(String ChineseLanguage) {
		char[] cl_chars = ChineseLanguage.trim().toCharArray();
		String hanyupinyin = "";
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);					// 输出拼音全部小写
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);				// 不带声调
		defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		for (int i = 0; i < cl_chars.length; i++) {
			if (String.valueOf(cl_chars[i]).matches("[\u4e00-\u9fa5]+")) {			// 如果字符是中文,则将中文转为汉语拼音
				try {
					hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0];
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				hanyupinyin += cl_chars[i];											// 如果字符不是中文,则不转换
			}
		}
		return hanyupinyin;
	}
}
