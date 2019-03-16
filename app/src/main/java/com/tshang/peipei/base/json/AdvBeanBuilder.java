package com.tshang.peipei.base.json;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.tshang.peipei.activity.main.adapter.bean.AdvBean;

/**
 * 
 * @author Jeff
 *		解析广告位数据
 */
public class AdvBeanBuilder extends JSONBuilder<AdvBean> {

	@Override
	public AdvBean build(JSONObject jsonObject) throws JSONException {
		if (jsonObject == null) {
			return null;
		}
		AdvBean advBean = new AdvBean();
		if (!jsonObject.isNull("url")) {
			String url = jsonObject.getString("url");
			if (TextUtils.isEmpty(url)) {
				url = "";
			}
			advBean.setUrl(url);
		}
		if (!jsonObject.isNull("name")) {
			String name = jsonObject.getString("name");
			if (TextUtils.isEmpty(name)) {
				name = "";
			}

			advBean.setName(name);
		}
		if (!jsonObject.isNull("img_url")) {
			String img_url = jsonObject.getString("img_url");
			if (TextUtils.isEmpty(img_url)) {
				img_url = "";
			}

			advBean.setImgUrl(img_url);
		}
		return advBean;
	}
}
