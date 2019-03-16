package com.tshang.peipei.base.json;

import java.math.BigInteger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.tshang.peipei.protocol.asn.gogirl.ApplyJoinGroupInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

/**
 * 
 * @author Jeff
 *		解析ApplyJoinGroupInfo数据
 */
public class ApplyJoinGroupInfoBuilder extends JSONBuilder<ApplyJoinGroupInfo> {

	@Override
	public ApplyJoinGroupInfo build(JSONObject jsonObject) throws JSONException {
		if (jsonObject == null) {
			return null;
		}
		ApplyJoinGroupInfo groupInfo = new ApplyJoinGroupInfo();
		if (!jsonObject.isNull("introduce")) {
			String introduce = jsonObject.getString("introduce");
			if (TextUtils.isEmpty(introduce)) {
				introduce = "";
			}
			groupInfo.introduce = introduce.getBytes();
		}
		if (!jsonObject.isNull("secret")) {
			String secret = jsonObject.getString("secret");
			if (TextUtils.isEmpty(secret)) {
				secret = "";
			}
			groupInfo.secret = secret.getBytes();
		}
		if (!jsonObject.isNull("joinuser")) {
			JSONArray jArray = jsonObject.getJSONArray("joinuser");
			if (jArray != null) {
				GoGirlUserInfo[] infos = GoGirlUserInfoFunctions.getGoGirlUserInfo(jArray);
				if (infos != null && infos.length > 0) {
					groupInfo.joinuser = infos[0];
				}
			}
		}
		if (!jsonObject.isNull("groupid")) {
			groupInfo.groupid = BigInteger.valueOf(jsonObject.getInt("groupid"));
		}
		if (!jsonObject.isNull("createtime")) {
			groupInfo.createtime = BigInteger.valueOf(jsonObject.getInt("createtime"));
		}
		return groupInfo;
	}
}
