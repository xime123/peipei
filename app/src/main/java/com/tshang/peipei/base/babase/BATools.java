package com.tshang.peipei.base.babase;


/*
 *类        名 : BATools.java
 *功能描述 : 陪陪专用工具类
 *作　    者 : vactor
 *设计日期 :2014 2014-3-24 下午5:25:17
 *修改日期 : 	
 *修  改   人: 
 *修 改内容: 
 */
public class BATools {

	/**
	 * 获取dbname
	 *
	 * @param mContext
	 * @return
	 */
	//	public static String getDB(Context mContext) {
	//		GoGirlUserInfo userInfo = UserSharePreference.getInstance(mContext).getGoGirlUserInfo();
	//		if (userInfo != null) {
	//			return userInfo.uid.intValue() + "";
	//		}
	//		return "";
	//	}

	public static byte[] getMergeArray(byte[] al, byte[] bl) {
		byte[] a = al;
		byte[] b = bl;
		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}

	public static byte[] intToByteArray1(int i) {
		byte[] result = new byte[4];
		result[3] = (byte) ((i >> 24) & 0xFF);
		result[2] = (byte) ((i >> 16) & 0xFF);
		result[1] = (byte) ((i >> 8) & 0xFF);
		result[0] = (byte) (i & 0xFF);
		return result;
	}

}
