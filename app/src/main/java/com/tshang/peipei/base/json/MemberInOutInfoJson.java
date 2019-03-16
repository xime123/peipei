package com.tshang.peipei.base.json;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.tshang.peipei.protocol.asn.gogirl.MemberInOutInfo;

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
public class MemberInOutInfoJson {

	public static String changeObjectDateToJson(MemberInOutInfo info) {//@的用户  
		try {
			if (info == null) {
				return null;
			}
			JSONObject stoneObject = new JSONObject();
			stoneObject.put("nick", new String(info.nick));
			stoneObject.put("act", info.act.intValue());
			stoneObject.put("createtime", info.createtime.intValue());
			stoneObject.put("groupid", info.groupid.intValue());
			stoneObject.put("uid", info.uid.intValue());

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
	public static MemberInOutInfo getMemberInOutInfo(String strInfo) {
		if (TextUtils.isEmpty(strInfo)) {
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(strInfo);
			MemberInOutInfoBuilder builder = new MemberInOutInfoBuilder();
			MemberInOutInfo infos = builder.build(jsonObject);
			return infos;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
