package com.tshang.peipei.base;

import java.text.DecimalFormat;

/**
 * @Title: FormatUtil.java 
 *
 * @Description: 格式化的工具类
 *
 * @author DYH  
 *
 * @date 2015-12-15 下午6:56:36 
 *
 * @version V1.0   
 */
public class FormatUtil {
	private static final String NUMBER_FORMAT = "###,###,###";
	public static String formatNumber(long number){
		 DecimalFormat df = new DecimalFormat(NUMBER_FORMAT); 
		 return df.format(number);
	}
}
