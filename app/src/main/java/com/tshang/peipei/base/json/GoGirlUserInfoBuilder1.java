package com.tshang.peipei.base.json;

import java.math.BigInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.tshang.peipei.base.BaseString;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

/**
 * 
 * @author Jeff
 * 对字符串进行了16进制解码
 */
public class GoGirlUserInfoBuilder1 extends JSONBuilder<GoGirlUserInfo> {

	@Override
	public GoGirlUserInfo build(JSONObject jsonObject) throws JSONException {
		if (jsonObject == null) {
			return null;
		}
		GoGirlUserInfo userInfo = new GoGirlUserInfo();

		if (!jsonObject.isNull("uid")) {
			userInfo.uid = BigInteger.valueOf(jsonObject.getLong("uid"));
		}
		if (!jsonObject.isNull("username")) {
			String username = jsonObject.getString("username");
			if (TextUtils.isEmpty(username)) {
				username = "";
			}
			userInfo.username = BaseString.decode(username).getBytes();
		}
		if (!jsonObject.isNull("createtime")) {
			userInfo.createtime = BigInteger.valueOf(jsonObject.getLong("createtime"));
		}
		if (!jsonObject.isNull("nick")) {
			String nick = jsonObject.getString("nick");
			if (TextUtils.isEmpty(nick)) {
				nick = "";
			}
			userInfo.nick = BaseString.decode(nick).getBytes();
		}
		if (!jsonObject.isNull("pwd")) {
			String pwd = jsonObject.getString("pwd");
			if (TextUtils.isEmpty(pwd)) {
				pwd = "";
			}
			userInfo.pwd = pwd.getBytes();
		}
		if (!jsonObject.isNull("email")) {
			String email = jsonObject.getString("email");
			if (TextUtils.isEmpty(email)) {
				email = "";
			}
			userInfo.email = BaseString.decode(email).getBytes();
		}
		if (!jsonObject.isNull("phone")) {
			String phone = jsonObject.getString("phone");
			if (TextUtils.isEmpty(phone)) {
				phone = "";
			}
			userInfo.phone = BaseString.decode(phone).getBytes();
		}
		if (!jsonObject.isNull("sex")) {
			userInfo.sex = BigInteger.valueOf(jsonObject.getLong("sex"));
		}
		if (!jsonObject.isNull("imei")) {
			String imei = jsonObject.getString("imei");
			if (TextUtils.isEmpty(imei)) {
				imei = "";
			}
			userInfo.imei = BaseString.decode(imei).getBytes();
		}
		if (!jsonObject.isNull("osver")) {
			String osver = jsonObject.getString("osver");
			if (TextUtils.isEmpty(osver)) {
				osver = "";
			}
			userInfo.osver = BaseString.decode(osver).getBytes();
		}
		if (!jsonObject.isNull("phoneos")) {
			userInfo.phoneos = BigInteger.valueOf(jsonObject.getLong("phoneos"));
		}
		if (!jsonObject.isNull("phonebrand")) {
			String phonebrand = jsonObject.getString("phonebrand");
			if (TextUtils.isEmpty(phonebrand)) {
				phonebrand = "";
			}
			userInfo.phonebrand = BaseString.decode(phonebrand).getBytes();
		}
		if (!jsonObject.isNull("appver")) {
			userInfo.appver = BigInteger.valueOf(jsonObject.getLong("appver"));
		}
		if (!jsonObject.isNull("lognum")) {
			userInfo.lognum = BigInteger.valueOf(jsonObject.getLong("lognum"));
		}
		if (!jsonObject.isNull("lastlogtime")) {
			userInfo.lastlogtime = BigInteger.valueOf(jsonObject.getLong("lastlogtime"));
		}
		if (!jsonObject.isNull("token")) {
			String token = jsonObject.getString("token");
			if (TextUtils.isEmpty(token)) {
				token = "";
			}
			userInfo.token = BaseString.decode(token).getBytes();
		}
		if (!jsonObject.isNull("auth")) {
			String auth = jsonObject.getString("auth");
			if (TextUtils.isEmpty(auth)) {
				auth = "";
			}
			userInfo.auth = auth.getBytes();
		}
		if (!jsonObject.isNull("authexpiretime")) {
			userInfo.authexpiretime = BigInteger.valueOf(jsonObject.getLong("authexpiretime"));
		}
		if (!jsonObject.isNull("type")) {
			userInfo.type = BigInteger.valueOf(jsonObject.getLong("type"));
		}
		if (!jsonObject.isNull("birthday")) {
			userInfo.birthday = BigInteger.valueOf(jsonObject.getLong("birthday"));
		}
		if (!jsonObject.isNull("userstatus")) {
			userInfo.userstatus = BigInteger.valueOf(jsonObject.getLong("userstatus"));
		}
		if (!jsonObject.isNull("headpickey")) {
			String headpickey = jsonObject.getString("headpickey");
			if (TextUtils.isEmpty(headpickey)) {
				headpickey = "";
			}
			userInfo.headpickey = headpickey.getBytes();
		}
		if (!jsonObject.isNull("verifycode")) {
			String verifycode = jsonObject.getString("verifycode");
			if (TextUtils.isEmpty(verifycode)) {
				verifycode = "";
			}
			userInfo.verifycode = BaseString.decode(verifycode).getBytes();
		}
		if (!jsonObject.isNull("nobreaklogs")) {
			userInfo.nobreaklogs = BigInteger.valueOf(jsonObject.getLong("nobreaklogs"));
		}
		if (!jsonObject.isNull("mailcode")) {
			String mailcode = jsonObject.getString("mailcode");
			if (TextUtils.isEmpty(mailcode)) {
				mailcode = "";
			}
			userInfo.mailcode = BaseString.decode(mailcode).getBytes();
		}
		if (!jsonObject.isNull("city")) {
			String city = jsonObject.getString("city");
			if (TextUtils.isEmpty(city)) {
				city = "";
			}
			userInfo.city = BaseString.decode(city).getBytes();
		}
		if (!jsonObject.isNull("forbidtime")) {
			userInfo.forbidtime = BigInteger.valueOf(jsonObject.getLong("forbidtime"));
		}
		if (!jsonObject.isNull("showpickey")) {
			String showpickey = jsonObject.getString("showpickey");
			if (TextUtils.isEmpty(showpickey)) {
				showpickey = "";
			}
			userInfo.showpickey = showpickey.getBytes();
		}
		if (!jsonObject.isNull("chatthreshold")) {
			userInfo.chatthreshold = BigInteger.valueOf(jsonObject.getLong("chatthreshold"));
		}
		if (!jsonObject.isNull("ranknum")) {
			userInfo.ranknum = BigInteger.valueOf(jsonObject.getLong("ranknum"));
		}
		if (!jsonObject.isNull("islocale")) {
			userInfo.islocale = BigInteger.valueOf(jsonObject.getLong("islocale"));
		}
		if (!jsonObject.isNull("istop")) {
			userInfo.islocale = BigInteger.valueOf(jsonObject.getLong("istop"));
		}
		if (!jsonObject.isNull("inviteuid")) {
			userInfo.inviteuid = BigInteger.valueOf(jsonObject.getLong("inviteuid"));
		}
		if (!jsonObject.isNull("channel")) {
			String channel = jsonObject.getString("channel");
			if (TextUtils.isEmpty(channel)) {
				channel = "";
			}
			userInfo.channel = BaseString.decode(channel).getBytes();
		}

		if (!jsonObject.isNull("gradeinfo")) {
			String gradeinfo = jsonObject.getString("gradeinfo");
			if (TextUtils.isEmpty(gradeinfo)) {
				gradeinfo = "";
			}
			userInfo.gradeinfo = gradeinfo.getBytes();
		}

		return userInfo;
	}

}
