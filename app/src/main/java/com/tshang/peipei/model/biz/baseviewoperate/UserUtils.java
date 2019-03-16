package com.tshang.peipei.model.biz.baseviewoperate;

import android.app.Activity;
import android.content.Context;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.UserSharePreference;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

/**
 * @Title: UserUtils.java 
 *
 * @Description: 防止内存不够，用户对象被释放了
 *
 * @author Jeff  
 *
 * @date 2014年7月17日 下午1:45:13 
 *
 * @version V1.0   
 */
public class UserUtils {
	public static final int TOURISTS_STATUS = 0x81;//未登录的游客态
	public static final int HOST_FEMALE_STATUS = 0x82;//登录的女主人态
	public static final int HOST_MALE_STATUS = 0x83;//登录的男主人态
	public static final int GUEST_FEMALE_STATUS = 0x84;//登录的访问女的
	public static final int GUEST_MALE_STATUS = 0x85;//登录的访问男的

	/**
	 * 获取用户的登录信息
	 * @author Jeff
	 *
	 * @param activity
	 * @return
	 */
	public static synchronized GoGirlUserInfo getUserEntity(Context activity) {
		if (BAApplication.mLocalUserInfo == null) {
			try {
				BAApplication.mLocalUserInfo = UserSharePreference.getInstance(activity).getGoGirlUserInfo();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return BAApplication.mLocalUserInfo;
	}

	/**
	 * 
	 * @author Jeff
	 *
	 * @param activity 上下文对象
	 * @param accessUid 访问uid
	 * @return 返回用户的状态
	 */
	public static int getAccessStatus(Context activity, int accessUid, int sex) {
		GoGirlUserInfo userInfo = getUserEntity(activity);
		if (userInfo != null && userInfo.uid.intValue() == accessUid) {
			if (sex == Gender.FEMALE.getValue()) {
				return HOST_FEMALE_STATUS;
			} else {
				return HOST_MALE_STATUS;
			}
		} else {
			if (sex == Gender.FEMALE.getValue()) {
				return GUEST_FEMALE_STATUS;
			} else {
				return GUEST_MALE_STATUS;
			}
		}
	}

	/**
	 * 
	 * @author Jeff
	 *
	 * @param activity
	 * @return
	 */
	public static boolean isLogin(Activity activity) {//判段是否有登陆过
		return getUserEntity(activity) == null ? false : true;
	}
}
