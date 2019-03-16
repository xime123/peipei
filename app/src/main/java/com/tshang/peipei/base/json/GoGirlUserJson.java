package com.tshang.peipei.base.json;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.text.TextUtils;

import com.ibm.asn1.ASN1Exception;
import com.ibm.asn1.BERDecoder;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.mine.MineWriteBroadCastActivity;
import com.tshang.peipei.base.BaseString;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.storage.db.BroadCastColumn;

/**
 * @Title: GoGirlUserJson.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 用来拼接广播数据成json数据
 *
 * @author Jeff 
 *
 * @date 2014年7月1日 上午10:26:25 
 *
 * @version V1.3.0   
 */
public class GoGirlUserJson {

	public static final String BRAODCAST = "braodcast";//用来存储@我的广播的数量

	/** 
	* 将数组转换为JSON格式的数据。 
	* @param stoneList 数据源 
	* @return JSON格式的数据 
	*/
	public static String changeArrayDateToJson(List<GoGirlUserInfo> userInfoList) {//@的用户  
		try {
			JSONArray array = new JSONArray();
			JSONObject object = new JSONObject();
			for (Object object2 : userInfoList) {
				GoGirlUserInfo info = (GoGirlUserInfo) object2;
				JSONObject stoneObject = new JSONObject();
				stoneObject.put("nick", new String(info.nick));
				stoneObject.put("uid", info.uid.intValue());
				stoneObject.put("sex", info.sex.intValue());
				stoneObject.put("headpickey", new String(info.headpickey));
				stoneObject.put("auth", new String(info.auth));
				stoneObject.put("userstatus", info.userstatus.intValue());
				stoneObject.put("ranknum", info.ranknum.intValue());
				stoneObject.put("lastlogtime", info.lastlogtime.longValue());
				stoneObject.put("showpickey", new String(info.showpickey));
				stoneObject.put("birthday", info.birthday.longValue());
				stoneObject.put("chatthreshold", info.chatthreshold.intValue());
				stoneObject.put("gradeinfo", new String(info.gradeinfo));
				stoneObject.put("username", new String(info.username));
				stoneObject.put("createtime", info.createtime.intValue());
				stoneObject.put("pwd", new String(info.pwd));
				stoneObject.put("email", new String(info.email));
				stoneObject.put("phone", new String(info.phone));
				stoneObject.put("imei", new String(info.imei));
				stoneObject.put("osver", new String(info.osver));
				stoneObject.put("phoneos", info.phoneos.intValue());
				stoneObject.put("phonebrand", new String(info.phonebrand));
				stoneObject.put("appver", info.appver.intValue());
				stoneObject.put("lognum", info.lognum.intValue());
				stoneObject.put("token", new String(info.token));
				stoneObject.put("authexpiretime", info.authexpiretime.intValue());
				stoneObject.put("type", info.type.intValue());
				stoneObject.put("verifycode", new String(info.verifycode));
				stoneObject.put("mailcode", new String(info.mailcode));
				stoneObject.put("nobreaklogs", info.nobreaklogs.intValue());
				stoneObject.put("city", new String(info.city));
				stoneObject.put("forbidtime", info.forbidtime.intValue());
				stoneObject.put("islocale", info.islocale.intValue());
				stoneObject.put("inviteuid", info.inviteuid.intValue());
				stoneObject.put("channel", new String(info.channel));
				array.put(stoneObject);
			}
			object.put("user", array);
			return object.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e2) {

		}
		return null;
	}

	public static JSONArray changeJsonArray(GoGirlUserInfo info) {
		try {
			JSONArray array = new JSONArray();
			JSONObject stoneObject = new JSONObject();
			stoneObject.put("nick", new String(info.nick));
			stoneObject.put("uid", info.uid.intValue());
			stoneObject.put("sex", info.sex.intValue());
			stoneObject.put("headpickey", new String(info.headpickey));
			stoneObject.put("auth", new String(info.auth));
			stoneObject.put("userstatus", info.userstatus.intValue());
			stoneObject.put("ranknum", info.ranknum.intValue());
			stoneObject.put("lastlogtime", info.lastlogtime.longValue());
			stoneObject.put("showpickey", new String(info.showpickey));
			stoneObject.put("birthday", info.birthday.longValue());
			stoneObject.put("chatthreshold", info.chatthreshold.intValue());
			stoneObject.put("gradeinfo", new String(info.gradeinfo));
			stoneObject.put("username", new String(info.username));
			stoneObject.put("createtime", info.createtime.intValue());
			stoneObject.put("pwd", new String(info.pwd));
			stoneObject.put("email", new String(info.email));
			stoneObject.put("phone", new String(info.phone));
			stoneObject.put("imei", new String(info.imei));
			stoneObject.put("osver", new String(info.osver));
			stoneObject.put("phoneos", info.phoneos.intValue());
			stoneObject.put("phonebrand", new String(info.phonebrand));
			stoneObject.put("appver", info.appver.intValue());
			stoneObject.put("lognum", info.lognum.intValue());
			stoneObject.put("token", new String(info.token));
			stoneObject.put("authexpiretime", info.authexpiretime.intValue());
			stoneObject.put("type", info.type.intValue());
			stoneObject.put("verifycode", new String(info.verifycode));
			stoneObject.put("mailcode", new String(info.mailcode));
			stoneObject.put("nobreaklogs", info.nobreaklogs.intValue());
			stoneObject.put("city", new String(info.city));
			stoneObject.put("forbidtime", info.forbidtime.intValue());
			stoneObject.put("islocale", info.islocale.intValue());
			stoneObject.put("inviteuid", info.inviteuid.intValue());
			stoneObject.put("channel", new String(info.channel));
			array.put(stoneObject);
			return array;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String changeObjectDateToJson(GoGirlUserInfo info) {//@的用户  
		try {
			JSONObject stoneObject = new JSONObject();
			stoneObject.put("nick", new String(info.nick));
			stoneObject.put("uid", info.uid.intValue());
			stoneObject.put("sex", info.sex.intValue());
			stoneObject.put("headpickey", new String(info.headpickey));
			stoneObject.put("auth", new String(info.auth));
			stoneObject.put("userstatus", info.userstatus.intValue());
			stoneObject.put("ranknum", info.ranknum.intValue());
			stoneObject.put("lastlogtime", info.lastlogtime.longValue());
			stoneObject.put("showpickey", new String(info.showpickey));
			stoneObject.put("birthday", info.birthday.longValue());
			stoneObject.put("chatthreshold", info.chatthreshold.intValue());
			stoneObject.put("gradeinfo", new String(info.gradeinfo));
			stoneObject.put("username", new String(info.username));
			stoneObject.put("createtime", info.createtime.intValue());
			stoneObject.put("pwd", "");
			stoneObject.put("email", new String(info.email));
			stoneObject.put("phone", new String(info.phone));
			stoneObject.put("imei", new String(info.imei));
			stoneObject.put("osver", new String(info.osver));
			stoneObject.put("phoneos", info.phoneos.intValue());
			stoneObject.put("phonebrand", new String(info.phonebrand));
			stoneObject.put("appver", info.appver.intValue());
			stoneObject.put("lognum", info.lognum.intValue());
			stoneObject.put("token", new String(info.token));
			stoneObject.put("authexpiretime", info.authexpiretime.intValue());
			stoneObject.put("type", info.type.intValue());
			stoneObject.put("verifycode", new String(info.verifycode));
			stoneObject.put("mailcode", new String(info.mailcode));
			stoneObject.put("nobreaklogs", info.nobreaklogs.intValue());
			stoneObject.put("city", new String(info.city));
			stoneObject.put("forbidtime", info.forbidtime.intValue());
			stoneObject.put("islocale", info.islocale.intValue());
			stoneObject.put("inviteuid", info.inviteuid.intValue());
			stoneObject.put("channel", new String(info.channel));

			return stoneObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e2) {

		}
		return null;
	}

	public static ContentValues insertBroadValues(BroadcastInfo broadcastInfo) {
		ContentValues values = new ContentValues();
		GoGirlUserInfo sendUserInfo = broadcastInfo.senduser;
		values.put(BroadCastColumn.TOUSER, changeArrayDateToJson(broadcastInfo.tousers));
		values.put(BroadCastColumn.SENDUSER, changeObjectDateToJson(sendUserInfo));
		values.put(BroadCastColumn.CREATETIME, broadcastInfo.createtime + "");
		values.put(BroadCastColumn.USERUID, BAApplication.mLocalUserInfo.uid.intValue() + "");
		values.put(BroadCastColumn.STAUTS, "2");
		String content = new String(broadcastInfo.contenttxt);
		values.put(BroadCastColumn.TYPE, broadcastInfo.broadcasttype.intValue());
		values.put(BroadCastColumn.REVINT, new String(broadcastInfo.voiceinfo));

		byte[] datalist = broadcastInfo.datalist;
		if (datalist != null && datalist.length != 0) {
			GoGirlDataInfoList infoList = new GoGirlDataInfoList();
			BERDecoder dec = new BERDecoder(datalist);

			try {
				infoList.decode(dec);
			} catch (ASN1Exception e1) {
				e1.printStackTrace();
			}
			if (!infoList.isEmpty()) {
				GoGirlDataInfo datainfo = (GoGirlDataInfo) infoList.get(0);
				if (datainfo != null) {
					values.put(BroadCastColumn.REVINT0, datainfo.datainfo.intValue());
					values.put(BroadCastColumn.REVINT1, datainfo.type.intValue());
					if (TextUtils.isEmpty(content)) {
						content = new String(datainfo.data);
					}
				}
			}

		} else {

			if (broadcastInfo.contenttxt.length != 0) {
				values.put(BroadCastColumn.REVINT0, MineWriteBroadCastActivity.BROADCAST_TEXT_COLOR_BLACK);
				values.put(BroadCastColumn.REVINT1, MessageType.BROADCASTCOLOR.getValue());
			}
		}
		values.put(BroadCastColumn.CONTNET, content);
		return values;
	}

	/**
	 * 
	 * @author Jeff
	 *
	 * @param strInfo 保存的GoGirlUserInfo json格式字符串
	 * @return GoGirlUserInfo 返回这个对象
	 */
	public static GoGirlUserInfo getGoGirlUserInfo(String strInfo) {
		if (TextUtils.isEmpty(strInfo)) {
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(strInfo);
			GoGirlUserInfoBuilder builder = new GoGirlUserInfoBuilder();
			GoGirlUserInfo infos = builder.build(jsonObject);
			return infos;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	//-----------------------------以下用户对象有进行字符串转解码-----------------------------------------------------

	private static final String GOGIRLUSERINFO_KEY = "gogirluserinfo";

	public static String changeEncodeListObjectDataToJson(GoGirlUserInfoList list) {
		try {
			if (list == null || list.isEmpty()) {
				return null;
			}
			JSONObject jObject = new JSONObject();
			JSONArray array = new JSONArray();
			for (Object object : list) {
				GoGirlUserInfo info = (GoGirlUserInfo) object;
				JSONObject stoneObject = new JSONObject();

				stoneObject.put("nick", BaseString.encode(info.nick));//字符串全部转换成16进制再存
				stoneObject.put("uid", info.uid.intValue());
				stoneObject.put("sex", info.sex.intValue());
				stoneObject.put("headpickey", new String(info.headpickey));
				stoneObject.put("auth", new String(info.auth));
				stoneObject.put("userstatus", info.userstatus.intValue());
				stoneObject.put("ranknum", info.ranknum.intValue());
				stoneObject.put("lastlogtime", info.lastlogtime.longValue());
				stoneObject.put("showpickey", new String(info.showpickey));
				stoneObject.put("birthday", info.birthday.longValue());
				stoneObject.put("chatthreshold", info.chatthreshold.intValue());
				stoneObject.put("gradeinfo", new String(info.gradeinfo));
				stoneObject.put("username", BaseString.encode(info.username));
				stoneObject.put("createtime", info.createtime.intValue());
				stoneObject.put("pwd", "");
				stoneObject.put("email", BaseString.encode(info.email));
				stoneObject.put("phone", BaseString.encode(info.phone));
				stoneObject.put("imei", BaseString.encode(info.imei));
				stoneObject.put("osver", BaseString.encode(info.osver));
				stoneObject.put("phoneos", info.phoneos.intValue());
				stoneObject.put("phonebrand", BaseString.encode(info.phonebrand));
				stoneObject.put("appver", info.appver.intValue());
				stoneObject.put("lognum", info.lognum.intValue());
				stoneObject.put("token", BaseString.encode(info.token));
				stoneObject.put("authexpiretime", info.authexpiretime.intValue());
				stoneObject.put("type", info.type.intValue());
				stoneObject.put("verifycode", BaseString.encode(info.verifycode));
				stoneObject.put("mailcode", BaseString.encode(info.mailcode));
				stoneObject.put("nobreaklogs", info.nobreaklogs.intValue());
				stoneObject.put("city", BaseString.encode(info.city));
				stoneObject.put("forbidtime", info.forbidtime.intValue());
				stoneObject.put("islocale", info.islocale.intValue());
				stoneObject.put("inviteuid", info.inviteuid.intValue());
				stoneObject.put("channel", BaseString.encode(info.channel));
				array.put(stoneObject);
			}
			jObject.put(GOGIRLUSERINFO_KEY, array);
			return jObject.toString();
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
	 * @param strInfo 保存的GiftInfo json格式字符串
	 * @return GiftInfo 返回这个对象
	 */
	public static GoGirlUserInfo[] getDecodeGoGirlUserInfos(String strInfo) {
		if (TextUtils.isEmpty(strInfo)) {
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(strInfo);
			if (!jsonObject.isNull(GOGIRLUSERINFO_KEY)) {
				JSONArray jsonArray = jsonObject.getJSONArray(GOGIRLUSERINFO_KEY);
				GoGirlUserInfo[] infos = GoGirlUserInfoFunctions.getGoGirlUserInfo1(jsonArray);
				return infos;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
