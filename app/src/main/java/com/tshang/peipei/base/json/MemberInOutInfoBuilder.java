package com.tshang.peipei.base.json;

import java.math.BigInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.tshang.peipei.protocol.asn.gogirl.MemberInOutInfo;

/**
 * 
 * @author Jeff
 *		解析MemberInOutInfo数据
 */
public class MemberInOutInfoBuilder extends JSONBuilder<MemberInOutInfo> {

	@Override
	public MemberInOutInfo build(JSONObject jsonObject) throws JSONException {
		if (jsonObject == null) {
			return null;
		}
		MemberInOutInfo info = new MemberInOutInfo();
		if (!jsonObject.isNull("nick")) {
			String nick = jsonObject.getString("nick");
			if (TextUtils.isEmpty(nick)) {
				nick = "";
			}
			info.nick = nick.getBytes();
		}
		if (!jsonObject.isNull("groupid")) {
			info.groupid = BigInteger.valueOf(jsonObject.getInt("groupid"));
		}
		if (!jsonObject.isNull("createtime")) {
			info.createtime = BigInteger.valueOf(jsonObject.getInt("createtime"));
		}
		if (!jsonObject.isNull("act")) {
			info.act = BigInteger.valueOf(jsonObject.getInt("act"));
		}
		if (!jsonObject.isNull("uid")) {
			info.uid = BigInteger.valueOf(jsonObject.getInt("uid"));
		}
		return info;
	}
}
