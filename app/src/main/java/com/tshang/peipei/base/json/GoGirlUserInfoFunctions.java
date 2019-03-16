package com.tshang.peipei.base.json;

import org.json.JSONArray;
import org.json.JSONException;

import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

public class GoGirlUserInfoFunctions {

	public static GoGirlUserInfo[] getGoGirlUserInfo(JSONArray jsonArray) throws JSONException {
		if (jsonArray == null) {
			return null;
		}
		int n = jsonArray.length();
		if (n == 0) {
			return null;
		}
		GoGirlUserInfo[] infos = new GoGirlUserInfo[n];
		GoGirlUserInfoBuilder builder = new GoGirlUserInfoBuilder();

		for (int i = 0; i < n; i++) {
			infos[i] = builder.build(jsonArray.getJSONObject(i));
		}

		return infos;
	}

	/** 
	 *  进行了16进制解码
	 * @author Administrator
	 *
	 * @param jsonArray
	 * @return
	 * @throws JSONException
	 */
	public static GoGirlUserInfo[] getGoGirlUserInfo1(JSONArray jsonArray) throws JSONException {
		if (jsonArray == null) {
			return null;
		}
		int n = jsonArray.length();
		if (n == 0) {
			return null;
		}
		GoGirlUserInfo[] infos = new GoGirlUserInfo[n];
		GoGirlUserInfoBuilder1 builder = new GoGirlUserInfoBuilder1();

		for (int i = 0; i < n; i++) {
			infos[i] = builder.build(jsonArray.getJSONObject(i));
		}

		return infos;
	}
}
