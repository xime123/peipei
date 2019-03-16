package com.tshang.peipei.model.biz.baseviewoperate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.skill.SkillCreateActivity;
import com.tshang.peipei.activity.skill.SkillDetailActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.protocol.asn.gogirl.RetGGSkillInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;

/**
 *  SkillUtils.java 
 *
 * (用一句话描述该文件做什么) 
 *
 * @author Jeff 
 *
 * @date 2014年7月14日 下午8:02:36 
 *
 * @version V1.0   
 */
public class SkillUtils {

	public static final String IS_ADD_OR_UPDATE_SKILL = "is_add_or_update_skill";

	public static final String SKILL_USER_HEADKEY = "skill_user_headkey";
	public static final String SKILL_TITLE = "skill_title";
	public static final String SKILL_DESCRIBE = "skill_describe";
	public static final String SKILL_GIFT_NAME = "skill_gift_name";
	public static final String SKILL_GIFT_KEY = "skill_gift_key";
	public static final String SKILL_GIFT_ID = "skill_gift_id";
	public static final String SKILL_GIFT_NUM = "skill_gift_num";
	public static final String SKILL_USER_TYPE = "skill_user_type";
	public static final String SKILL_ID = "skill_id";
	public static final String SKILL_RATING = "skill_rating";
	public static final String SKILL_USER_NAME = "skill_user_name";
	public static final String SKILL_USER_ID = "skill_user_id";
	public static final String SkILL_JOIN_PEOPLE_COUNT = "skill_join_people_count";
	public static final String SKILL_USER_ONTIME = "skill_user_ontime";
	public static final String SKILL_GIFT_CHARM_VALUE = "skill_gift_charm_value";
	public static final String SKILL_GIFT_PRICEGOLD = "skill_gift_pricegold";
	public static final String SKILL_GIFT_PRICESILVER = "skill_gift_pricesilver";
	public static final String SKILL_GIFT_LOYALTY_VALUE = "skill_gift_loyalty_value";
	public static final String SKILL_TYPE = "skill_type";
	public static final String SKILL_FROM = "skill_from";
	public static final int REQUESTCODE = 1000;

	public static void intentSkillDetail(Activity activity, RetGGSkillInfo retGGSkillInfo) {
		Bundle bundle = new Bundle();
		bundle.putString(SKILL_USER_HEADKEY, new String(retGGSkillInfo.hostuserinfo.headpickey));
		bundle.putString(SKILL_TITLE, new String(retGGSkillInfo.skillinfo.title));
		bundle.putString(SKILL_DESCRIBE, new String(retGGSkillInfo.skillinfo.desc));
		bundle.putString(SKILL_GIFT_KEY, new String(retGGSkillInfo.giftinfo.pickey));

		String alias = SharedPreferencesTools.getInstance(activity, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
				retGGSkillInfo.hostuserinfo.uid.intValue());

		bundle.putString(SKILL_USER_NAME, TextUtils.isEmpty(alias) ? new String(retGGSkillInfo.hostuserinfo.nick) : alias);
		bundle.putString(SKILL_GIFT_NAME, new String(retGGSkillInfo.giftinfo.name));
		bundle.putInt(SKILL_GIFT_NUM, retGGSkillInfo.skillinfo.giftnum.intValue());
		bundle.putInt(SKILL_TYPE, retGGSkillInfo.skillinfo.type.intValue());
		bundle.putInt(SKILL_GIFT_ID, retGGSkillInfo.giftinfo.id.intValue());
		bundle.putInt(SKILL_ID, retGGSkillInfo.skillinfo.id.intValue());
		bundle.putInt(SKILL_GIFT_PRICEGOLD, retGGSkillInfo.giftinfo.pricegold.intValue());
		bundle.putInt(SKILL_GIFT_PRICESILVER, retGGSkillInfo.giftinfo.pricesilver.intValue());
		bundle.putInt(SKILL_GIFT_CHARM_VALUE, retGGSkillInfo.giftinfo.charmeffect.intValue());
		bundle.putInt(SKILL_GIFT_LOYALTY_VALUE, retGGSkillInfo.giftinfo.loyaltyeffect.intValue());
		bundle.putString(SKILL_RATING, String.valueOf(retGGSkillInfo.skillinfo.averagepoint.intValue()));//评分
		bundle.putInt(SKILL_USER_ID, retGGSkillInfo.hostuserinfo.uid.intValue());
		bundle.putInt(SkILL_JOIN_PEOPLE_COUNT, retGGSkillInfo.skillinfo.participantnum.intValue());
		bundle.putLong(SKILL_USER_ONTIME, retGGSkillInfo.hostuserinfo.lastlogtime.longValue());
		BaseUtils.openResultActivity(activity, SkillDetailActivity.class, bundle, REQUESTCODE);
	}

	public static void intentAddSkill(Activity activity, int skillId, String title, String desc, String giftName, int giftNum, int giftId, int type) {
		Bundle bundle = new Bundle();
		bundle.putInt(SKILL_ID, skillId);
		bundle.putString(SKILL_TITLE, title);
		bundle.putString(SKILL_DESCRIBE, desc);
		bundle.putString(SKILL_GIFT_NAME, giftName);
		bundle.putInt(SKILL_GIFT_NUM, giftNum);
		bundle.putInt(SKILL_GIFT_ID, giftId);
		bundle.putInt(SKILL_USER_TYPE, type);
		bundle.putBoolean(IS_ADD_OR_UPDATE_SKILL, true);
		BaseUtils.openResultActivity(activity, SkillCreateActivity.class, bundle, REQUESTCODE);
	}

	public static void resultEditSkill(Activity activity, String title, String desc, String giftName, int giftNum, int giftId) {
		Intent intent = new Intent();
		intent.putExtra(SKILL_TITLE, title);
		intent.putExtra(SKILL_DESCRIBE, desc);
		intent.putExtra(SKILL_GIFT_NAME, giftName);
		intent.putExtra(SKILL_GIFT_NUM, giftNum);
		intent.putExtra(SKILL_GIFT_ID, giftId);
		activity.setResult(Activity.RESULT_OK, intent);
	}

}
