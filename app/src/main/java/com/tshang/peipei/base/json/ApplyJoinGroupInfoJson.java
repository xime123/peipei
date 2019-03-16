package com.tshang.peipei.base.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.tshang.peipei.protocol.asn.gogirl.ApplyJoinGroupInfo;

/**
 * @Title: GoGirlUserJson.java 
 *
 * @Description:(用一句话描述该文件做什么) 用来拼接广播数据成json数据
 *
 * @author Jeff 
 *
 * @date 2014年9月20日 上午10:26:25 
 *
 * @version V1.3.0   
 */
public class ApplyJoinGroupInfoJson {

	public static String changeObjectDateToJson(ApplyJoinGroupInfo info) {//@的用户  
		try {
			if (info == null) {
				return null;
			}
			JSONObject stoneObject = new JSONObject();
			stoneObject.put("introduce", new String(info.introduce));
			stoneObject.put("secret", new String(info.secret));
			JSONArray jArray = GoGirlUserJson.changeJsonArray(info.joinuser);
			if (jArray != null) {
				stoneObject.put("joinuser", jArray);
			}
			stoneObject.put("createtime", info.createtime.intValue());
			stoneObject.put("groupid", info.groupid.intValue());

			return stoneObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e2) {

		}
		return null;
	}

	/**
	 * 
	 * @author Jeff
	 *
	 * @param strInfo 保存的GoGirlUserInfo json格式字符串
	 * @return GoGirlUserInfo 返回这个对象
	 */
	public static ApplyJoinGroupInfo getApplyJoinGroupInfo(String strInfo) {
		if (TextUtils.isEmpty(strInfo)) {
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(strInfo);
			ApplyJoinGroupInfoBuilder builder = new ApplyJoinGroupInfoBuilder();
			ApplyJoinGroupInfo infos = builder.build(jsonObject);
			return infos;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
