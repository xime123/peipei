package com.tshang.peipei.model.skill;

import android.app.Activity;
import android.text.TextUtils;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetParticipantInfoList;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.request.RequestAddSkillDeal;
import com.tshang.peipei.model.request.RequestAddSkillDeal.IAddSkillDeal;
import com.tshang.peipei.model.request.RequestGetSkillInterestUserList;
import com.tshang.peipei.model.request.RequestGetSkillInterestUserList.IGetSkillInterestUserList;
import com.tshang.peipei.model.request.RequestInterestInSkill;
import com.tshang.peipei.model.request.RequestInterestInSkill.IInterestInSkill;

/**
 * @Title: SkillJoin.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Jeff  
 *
 * @date 2014年10月21日 下午8:37:51 
 *
 * @version V1.4.0   
 */
public class SkillJoin implements IInterestInSkill, IGetSkillInterestUserList, IAddSkillDeal {
	private BAHandler handler;

	private SkillJoin() {

	}

	private static SkillJoin instance = null;

	public static SkillJoin getInstance() {
		if (instance == null) {
			synchronized (SkillJoin.class) {
				if (instance == null) {
					instance = new SkillJoin();
				}
			}
		}
		return instance;
	}

	public void reqInterestinSkill(Activity activity, String desc, int skillId, int skilluid, BAHandler handler) {//技能留言
		RequestInterestInSkill req = new RequestInterestInSkill();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}
		if (TextUtils.isEmpty(desc)) {
			BaseUtils.showTost(activity, R.string.str_leave_messave_not_empty);
			return;
		}
		BaseUtils.showDialog(activity, R.string.str_leaving_message);
		this.handler = handler;
		req.interestInSkill(info.auth, BAApplication.app_version_code, desc, info.uid.intValue(), skillId, skilluid, this);

	}

	public void reqInterestinSkillList(Activity activity, int skillId, int skilluid, BAHandler handler) {//请求感兴趣列表
		RequestGetSkillInterestUserList req = new RequestGetSkillInterestUserList();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}
		this.handler = handler;
		req.getSkillInterestUserlist(info.auth, BAApplication.app_version_code, info.uid.intValue(), skillId, skilluid, 0, 50, this);

	}

	public void reqaddSkillDeal(Activity activity, int skillId, int skilluid, int participateuid, BAHandler handler) {//请求下单
		RequestAddSkillDeal req = new RequestAddSkillDeal();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}
		BaseUtils.showDialog(activity, "正在下单...");
		this.handler = handler;
		req.addSkillDeal(info.auth, BAApplication.app_version_code, info.uid.intValue(), skillId, skilluid, participateuid, this);

	}

	@Override
	public void interestInSkillCallBack(int retCode, String msg) {//感兴趣打赏回调
		if (retCode == 0) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.SKILL_INTERESTIN_JOIN_SUCCESS_VALUE);
		} else if (retCode == -28062) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.SKILL_INTERESTIN_JOIN_REPEAT_VALUE);
		} else {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.SKILL_INTERESTIN_JOIN_FAILED_VALUE);
		}
	}

	@Override
	public void reqGetSkillInterestUserList(int retCode, int isend, int total, RetParticipantInfoList list) {//感兴趣的技能列表
		if (retCode == 0) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.SKILL_GET_SKILL_INTERESTIN_LIST_SUCCESS_VALUE, isend, total, list);
		} else {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.SKILL_GET_SKILL_INTERESTIN_LIST_FAILED_VALUE, isend, total, list);
		}
	}

	@Override
	public void reqAddSkillDeal(int retCode) {//男人技能下单回来
		if (retCode == 0) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.SKILL_ADD_SKILL_DEAL_SUCCESS_VALUE);//下单成功
		} else if (retCode == BAConstants.rspContMsgType.E_GG_PROPERTY_LACK) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.SKILL_ADD_SKILL_DEAL_MONEY_NOT_ENGOUH);
		} else {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.SKILL_ADD_SKILL_DEAL_FAILED_VALUE);
		}
	}

}
