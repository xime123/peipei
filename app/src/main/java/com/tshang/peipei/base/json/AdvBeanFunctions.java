package com.tshang.peipei.base.json;

import org.json.JSONArray;
import org.json.JSONException;

import com.tshang.peipei.activity.main.adapter.bean.AdvBean;

public class AdvBeanFunctions {

	public static AdvBean[] getAdvBean(JSONArray jsonArray) throws JSONException {
		if (jsonArray == null) {
			return null;
		}
		int n = jsonArray.length();
		if (n == 0) {
			return null;
		}
		AdvBean[] infos = new AdvBean[n];
		AdvBeanBuilder builder = new AdvBeanBuilder();

		for (int i = 0; i < n; i++) {
			infos[i] = builder.build(jsonArray.getJSONObject(i));
		}

		return infos;
	}

}
