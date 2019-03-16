package com.tshang.peipei.storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.Gogirl.ShowRoomInfo;

/**
 * @Title: SharePreferences操作类
 *
 * @Description: SharePreferences的一些插入、查找方式
 *
 * @author allen
 *
 * @version V1.0   
 */
public class SharedPreferencesTools {
	public static final String SP_NAME = "BA_SP";
	public static final String IS_SHOW_ACCOUNT_SAFE = "is_show_account_safe";
	public static final String PEI_PEI_ADV_ACTION_URL = "pei_pei_adv_action_url";
	public static final String BROADCAST_TEXT_COLOR = "broadcast_text_color";
	public static final String PEI_PEI_ADV_AUTH_URL = "pei_pei_adv_auth_url";
	public static final String BROADCAST_COLOR_PRIVILEGE = "broadcast_color_privilege";

	private Context mContext;

	private static SharedPreferencesTools mSharedPreferencesTools = null;
	private static SharedPreferences mSharedPreferences;
	private static String share_name = "";

	private SharedPreferencesTools(Context context, String name) {
		mContext = context;
		if (mSharedPreferences == null) {
			mSharedPreferences = mContext.getSharedPreferences(share_name, Context.MODE_PRIVATE);
		}
	}

	public static SharedPreferencesTools getInstance(Context context, String name) {
		if (TextUtils.isEmpty(name)) {
			return getInstance(context);
		}

		if (mSharedPreferencesTools == null) {
			share_name = name;
			mSharedPreferencesTools = new SharedPreferencesTools(context, name);
		} else {
			if (!share_name.equals(name)) {
				share_name = name;
				if(context != null){
					mSharedPreferences = context.getSharedPreferences(share_name, Context.MODE_PRIVATE);
				}else{
					mSharedPreferences = BAApplication.getInstance().getSharedPreferences(share_name, Context.MODE_PRIVATE);
				}
			}
		}

		return mSharedPreferencesTools;
	}

	public static SharedPreferencesTools getInstance(Context context) {
		return getInstance(context, SP_NAME);
	}

	/**
	 * 通过key得到Int
	 * 
	 * @param key
	 * @return int value
	 */
	public int getIntValueByKey(String key) {
		try {
			return mSharedPreferences.getInt(key, -1);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public int getIntValueByKey(String key, int defaultValue) {
		try {
			return mSharedPreferences.getInt(key, defaultValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public int getIntValueByKeyToZero(String key) {
		try {
			return mSharedPreferences.getInt(key, 0);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	//获取经纬度
	public int getIntValueByKeyToLation(String key) {
		try {
			return mSharedPreferences.getInt(key, 19000000);
		} catch (Exception e) {
			e.printStackTrace();
			return 19000000;
		}
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

	/**
	 * 保存Int key-value
	 * 
	 * @param keyValue
	 *            保存的value
	 * @param key
	 *            保存的key
	 */
	public void saveIntKeyValue(int keyValue, String key) {
		Editor mEditor = mSharedPreferences.edit();
		mEditor.putInt(key, keyValue);
		mEditor.commit();
	}

	public void saveLongKeyValue(long keyValue, String key) {
		Editor mEditor = mSharedPreferences.edit();
		mEditor.putLong(key, keyValue);
		mEditor.commit();
	}

	public long getLongKeyValue(String key) {
		return mSharedPreferences.getLong(key, 0);
	}

	public long getLongKeyValueV2(String key) {
		try {
			return mSharedPreferences.getLong(key, 0);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public void saveBooleanKeyValue(Boolean keyValue, String key) {
		Editor mEditor = mSharedPreferences.edit();
		mEditor.putBoolean(key, keyValue);
		mEditor.commit();
	}

	public Boolean getBooleanKeyValue(String key) {
		return mSharedPreferences.getBoolean(key, false);
	}

	public void remove(String key) {
		Editor mEditor = mSharedPreferences.edit();
		mEditor.remove(key);
		mEditor.commit();
	}

	public String getAlias(int Fuid) {
		return mSharedPreferences.getString(Fuid + "", "");
	}

}
