package com.tshang.peipei.model.space;

import java.math.BigInteger;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.tshang.peipei.activity.dialog.BuyGiftDialog;
import com.tshang.peipei.activity.space.SpaceActivity;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.PhotoInfo;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.IntentType;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;

/**
 * @Title: SpaceUtils.java 
 *
 * 跳转到个人主页的一些复用
 *
 * @author Jeff
 *
 * @date 2014年8月8日 下午2:07:26 
 *
 * @version V1.0   
 */
public class SpaceUtils {
	public static final String SHOWKEY = "showkey";
	public static final String BIRTHDAY = "birthday";
	public static final String SPACE_FROM = "space_from";//0:没有什么状态，1：从大厅竟然，2：从排行进入

	public static void toSpaceCustom(Activity activity, GoGirlUserInfo useinfo, int from) {

		try {
			if (useinfo == null) {
				return;
			}
			Bundle bundle = new Bundle();
			bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, useinfo.uid.intValue());
			bundle.putString(BAConstants.IntentType.MAINHALLFRAGMENT_USERNICK, new String(useinfo.nick));
			bundle.putString(BAConstants.IntentType.MAINHALLFRAGMENT_HEADPIC, new String(useinfo.headpickey));
			String auth = "";
			if (useinfo.auth != null) {
				auth = new String(useinfo.auth);
			}
			if (TextUtils.isEmpty(auth)) {
				auth = "";
			}
			bundle.putString("auth", auth);
			int sex = useinfo.sex.intValue();
			bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, sex);
			bundle.putLong(BAConstants.IntentType.MAINHALLFRAGMENT_USERGLAMOUR, useinfo.ranknum.longValue());
			bundle.putLong(BAConstants.IntentType.MAINHALLFRAGMENT_USERLATESTTIME, useinfo.lastlogtime.longValue());
			String showpickey = "";
			if (useinfo.showpickey != null) {
				showpickey = new String(useinfo.showpickey);
			}
			if (TextUtils.isEmpty(showpickey)) {
				showpickey = "";
			}
			bundle.putString(SHOWKEY, showpickey);
			if (useinfo.birthday.longValue() != -1) {
				bundle.putString(BIRTHDAY, new String(BaseTimes.getFormatTime(useinfo.birthday.longValue() * 1000)));
			}
			bundle.putInt(SPACE_FROM, from);

			BaseUtils.openActivity(activity, SpaceActivity.class, bundle);//女号

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void toSpaceCustom(Activity activity, long glamour) {
		try {
			GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
			if (userEntity == null) {
				return;
			}
			Bundle bundle = new Bundle();
			bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, userEntity.uid.intValue());
			bundle.putString(BAConstants.IntentType.MAINHALLFRAGMENT_USERNICK, new String(userEntity.nick));
			bundle.putString(BAConstants.IntentType.MAINHALLFRAGMENT_HEADPIC, new String(userEntity.headpickey));
			bundle.putLong(BAConstants.IntentType.MAINHALLFRAGMENT_USERGLAMOUR, glamour);//在线
			bundle.putLong(BAConstants.IntentType.MAINHALLFRAGMENT_USERLATESTTIME, 0);
			int sex = userEntity.sex.intValue();
			bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, sex);
			bundle.putString(SpaceUtils.SHOWKEY, new String(userEntity.showpickey));
			bundle.putString(SpaceUtils.BIRTHDAY, userEntity.birthday.intValue() + "");
			bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_LOYALTY, 0);
			BaseUtils.openActivity(activity, SpaceActivity.class, bundle);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void toSpaceCustom(Activity activity, int uid, int sex) {
		Bundle bundle = new Bundle();
		bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, uid);
		bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, sex);
		BaseUtils.openActivityByNew(activity, SpaceActivity.class, bundle);
	}

	public static void toSpaceCustomByGoodsid(Activity activity, int uid, int sex, int goodsid) {
		Bundle bundle = new Bundle();
		bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, uid);
		bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, sex);
		bundle.putInt(IntentType.MINE_GOODSID, goodsid);
		BaseUtils.openActivityByNew(activity, SpaceActivity.class, bundle);
	}

	public static PhotoInfo GetPhotoInfo() {//拼接默认数据
		PhotoInfo info = new PhotoInfo();
		info.attr = BigInteger.valueOf(0);
		info.createtime = BigInteger.valueOf(System.currentTimeMillis() / 1000);
		info.id = BigInteger.valueOf(-1);
		info.key = "".getBytes();
		info.photodesc = "".getBytes();
		info.picset = "".getBytes();
		info.revint0 = BigInteger.valueOf(0);
		info.revint1 = BigInteger.valueOf(0);
		info.revint2 = BigInteger.valueOf(0);
		info.revint3 = BigInteger.valueOf(0);
		info.revstr0 = "".getBytes();
		info.revstr1 = "".getBytes();
		info.revstr2 = "".getBytes();
		info.revstr3 = "".getBytes();
		return info;
	}

}
