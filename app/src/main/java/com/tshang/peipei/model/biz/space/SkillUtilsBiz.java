package com.tshang.peipei.model.biz.space;

import android.app.Activity;
import android.text.TextUtils;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestAddSkill;
import com.tshang.peipei.model.request.RequestAddSkill.IAddSkill;
import com.tshang.peipei.model.request.RequestAppealSkillDeal;
import com.tshang.peipei.model.request.RequestAppealSkillDeal.iAppealSkillDeal;
import com.tshang.peipei.model.request.RequestConfirmSkillDeal;
import com.tshang.peipei.model.request.RequestConfirmSkillDeal.iConfirmSkillDeal;
import com.tshang.peipei.model.request.RequestEditSkill;
import com.tshang.peipei.model.request.RequestEditSkill.IEditSkill;
import com.tshang.peipei.model.request.RequestGetGiftList;
import com.tshang.peipei.model.request.RequestGetGiftList.IGetGiftListCallBack;
import com.tshang.peipei.model.request.RequestGetSingleSkillInfo;
import com.tshang.peipei.model.request.RequestGetSingleSkillInfo.BizCallBackGetSingleSkillInfo;
import com.tshang.peipei.model.request.RequestMarkSkillDeal;
import com.tshang.peipei.model.request.RequestMarkSkillDeal.IMarkSkillDeal;
import com.tshang.peipei.model.request.RequestParticipate;
import com.tshang.peipei.model.request.RequestParticipate.IParticipate;
import com.tshang.peipei.model.request.RequestReclainGift;
import com.tshang.peipei.model.request.RequestReclainGift.iReclainGift;

import de.greenrobot.event.EventBus;

/**
 *
 * (用一句话描述该文件做什么) 处理所有有关技能接口数据的逻辑类
 *
 * @author Jeff  
 *
 * @date 2014年7月11日 下午5:37:56 
 *
 * @version V1.0   
 */
public class SkillUtilsBiz {
	private Activity activity;

	public SkillUtilsBiz(Activity activity) {
		this.activity = activity;
	}

	/**
	 *  增加技能
	 * @author Administrator
	 *
	 * @param ver
	 * @param giftId
	 * @param giftNum
	 * @param desc
	 * @param title
	 * @param callBack
	 */
	public void getAddSkillCallBack(int giftId, int giftNum, String desc, String title, IAddSkill callBack) {
		RequestAddSkill req = new RequestAddSkill();
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			BaseUtils.showTost(activity, R.string.str_submit_failed);
			return;
		}
		if (TextUtils.isEmpty(title)) {
			BaseUtils.showTost(activity, R.string.str_skill_name_not_empty);
			return;
		}

		if (TextUtils.isEmpty(desc)) {
			BaseUtils.showTost(activity, R.string.str_describe_not_lower_ten_length);
			return;
		}
		if (desc.length() < 10) {
			BaseUtils.showTost(activity, R.string.str_describe_not_lower_ten_length);
			return;
		}

		if (giftId == -1) {
			BaseUtils.showTost(activity, R.string.str_select_need_gift);
			return;
		}
		BaseUtils.showDialog(activity, R.string.submitting);
		req.addSkill(userEntity.auth, BAApplication.app_version_code, desc, giftId, giftNum, userEntity.uid.intValue(), title, callBack);
	}

	/**
	 * 获取礼物列表
	 * @author Administrator
	 *
	 * @param ver
	 * @param callBack
	 */
	public void getGiftList(IGetGiftListCallBack callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		BaseUtils.showDialog(activity, R.string.loading);
		RequestGetGiftList requestGetGiftList = new RequestGetGiftList();
		requestGetGiftList.getGiftList(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), callBack);
	}

	/**
	 * 修改技能
	 * @author Administrator
	 *
	 * @param ver
	 * @param title
	 * @param giftNum
	 * @param desc
	 * @param giftid
	 * @param skillid
	 * @param callBack
	 */
	public void editSkill(String title, int giftNum, String desc, int giftid, int skillid, IEditSkill callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		if (TextUtils.isEmpty(title)) {
			BaseUtils.showTost(activity, R.string.str_skill_name_not_empty);
			return;
		}

		if (TextUtils.isEmpty(desc)) {
			BaseUtils.showTost(activity, R.string.str_describe_not_lower_ten_length);
			return;
		}
		if (desc.length() < 10) {
			BaseUtils.showTost(activity, R.string.str_describe_not_lower_ten_length);
			return;
		}

		if (giftid == -1) {
			BaseUtils.showTost(activity, R.string.str_select_need_gift);
			return;
		}
		BaseUtils.showDialog(activity, R.string.submitting);
		RequestEditSkill req = new RequestEditSkill();
		req.editSkill(userEntity.auth, BAApplication.app_version_code, desc, title, skillid, giftid, giftNum, userEntity.uid.intValue(), callBack);
	}

	/**
	 * 技能送礼接口
	 * @author Administrator
	 *
	 * @param ver
	 * @param otherUid
	 * @param skillId
	 * @param callBack
	 */
	public void participate(int otherUid, int skillId, IParticipate callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		BaseUtils.showDialog(activity, R.string.str_send_skill_gift);
		RequestParticipate req = new RequestParticipate();
		req.participate(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), otherUid, skillId, callBack);
	}

	/**
	 * 技能评分接口
	 * @author Jeff
	 *
	 * @param otherUid
	 * @param skillId
	 * @param point
	 * @param callBack
	 */
	public void markSkill(int otherUid, int skillId, int point, IMarkSkillDeal callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		BaseUtils.showDialog(activity, R.string.submitting);
		RequestMarkSkillDeal req = new RequestMarkSkillDeal();
		req.markSkillDeal(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), skillId, point, callBack);
	}

	public void getSingleSkillInfo(int otherUid, int skillId, BizCallBackGetSingleSkillInfo callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		BaseUtils.showDialog(activity, R.string.loading);
		NoticeEvent event = new NoticeEvent();
		event.setFlag(NoticeEvent.NOTICE57);
		EventBus.getDefault().postSticky(event);
		RequestGetSingleSkillInfo req = new RequestGetSingleSkillInfo();
		req.getSingleSkillInfo(userEntity.auth, BAApplication.app_version_code, skillId, userEntity.uid.intValue(), otherUid, callBack);
	}

	/**
	 * 
	 * @param act yes no
	 * @param skilldealid 订单id
	 * @param callback
	 */
	public void confirmSkillDeal(String act, int skilldealid, iConfirmSkillDeal callback) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		RequestConfirmSkillDeal req = new RequestConfirmSkillDeal();
		req.confirmSkillDeal(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), act, skilldealid, callback);
	}

	/**
	 * 
	 * 退礼物
	 *
	 * @param skilldealid
	 * @param callback
	 */
	public void reclainGift(int skilldealid, iReclainGift callback) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		RequestReclainGift req = new RequestReclainGift();
		req.reclainGift(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), skilldealid, callback);
	}

	/**
	 * 
	 * 申诉
	 *
	 * @param skilldealid
	 * @param callback
	 */
	public void appealSkillDeal(int skilldealid, iAppealSkillDeal callback) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		RequestAppealSkillDeal req = new RequestAppealSkillDeal();
		req.appealSkillDeal(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), skilldealid, callback);
	}
}
