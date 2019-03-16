package com.tshang.peipei.base.babase;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.tshang.peipei.base.json.GoGirlUserJson;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

/*
 *类        名 : UserSharePreference.java
 *功能描述 : 存储用户信息
 *作　    者 : vactor
 *设计日期 : 2014 2014-3-24 下午7:16:47
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class UserSharePreference {

	public static final String SP_NAME = "USER_SP";
	public final static String SHAREPREFERENCE_APPRECIATE_FAV_NAME = "s_appreciate_fav_file";
	public final static String SHAREPREFERENCE_REPLY_TOPIC_NAME = "s_reply_topic_file";

	private Context mContext;

	private static UserSharePreference mSharedPreferencesTools = null;
	private static SharedPreferences mSharedPreferences;
	private static String share_name = "base64";

	private UserSharePreference(Context context, String name) {
		mContext = context;
		if (mSharedPreferences == null) {
			mSharedPreferences = mContext.getSharedPreferences(share_name, Context.MODE_PRIVATE);
		}
	}

	public static UserSharePreference getInstance(Context context, String name) {
		if (mSharedPreferencesTools == null) {
			share_name = name;
			mSharedPreferencesTools = new UserSharePreference(context, name);
		} else {
			if (!share_name.equals(name)) {
				share_name = name;
				mSharedPreferences = context.getSharedPreferences(share_name, Context.MODE_PRIVATE);
			}
		}

		return mSharedPreferencesTools;
	}

	public static UserSharePreference getInstance(Context context) {
		return getInstance(context, SP_NAME);
	}

	/**
	 * 存储一个user对象
	 * @param key
	 * @param userEntity
	 */
	public void saveUserByKey(GoGirlUserInfo info) {
		String strInfo = GoGirlUserJson.changeObjectDateToJson(info);
		if (TextUtils.isEmpty(strInfo)) {
			strInfo = "";
		}
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(BAConstants.UID_INFO, strInfo);
		editor.commit();
	}

	/**
	 * 根据KEY取一个USER
	 * @param key
	 * @return
	 */
	public GoGirlUserInfo getGoGirlUserInfo() {
		String strInfo = mSharedPreferences.getString(BAConstants.UID_INFO, "");
		return GoGirlUserJson.getGoGirlUserInfo(strInfo);
	}

	/**
	 * 删除一个user对象
	 * @param key
	 * @param userEntity
	 */
	public void removeUserByKey() {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.remove(BAConstants.UID_INFO);
		editor.commit();
	}

	/**
	 * 通过key得到String value
	 * 
	 * @param key
	 * @return String value
	 */
	public String getStringValueByKey(String key) {
		return mSharedPreferences.getString(key, "");
	}

	/**
	 * 保存Stirng key-value
	 * 
	 * @param keyValue
	 *            保存的value
	 * @param key
	 *            保存的key
	 */
	public void saveStringKeyValue(String keyValue, String key) {
		Editor mEditor = mSharedPreferences.edit();
		mEditor.putString(key, keyValue);
		mEditor.commit();
	}

}
