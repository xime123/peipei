package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.os.Message;
import android.view.View;

import com.tshang.peipei.protocol.asn.gogirl.SkillDealInfo;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.space.SkillUtilsBiz;
import com.tshang.peipei.model.request.RequestAppealSkillDeal.iAppealSkillDeal;

/**
 * @Title: ConfirmSkillDialog.java 
 *
 * @Description: 申诉提示
 *
 * @author allen  
 *
 * @date 2014-10-28 下午7:59:24 
 *
 * @version V1.0   
 */
public class ConfirmSkillDialog extends HintToastDialog implements iAppealSkillDeal {

	private int skillDealid;
	private SkillUtilsBiz sBiz;
	private BAHandler mHandler;

	public ConfirmSkillDialog(Activity context, int title, int sure, int skillDealid, BAHandler mHandler) {
		super(context, title, sure);
		sBiz = new SkillUtilsBiz(context);
		this.skillDealid = skillDealid;
		this.mHandler = mHandler;
	}

	@Override
	public void onClick(View v) {
		dismiss();
		sBiz.appealSkillDeal(skillDealid, this);
	}

	@Override
	public void resultAppealSkillDeal(int retCode, SkillDealInfo skilldeal) {
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.SKILL_DEAL_ORDER_APPEAL;
		msg.arg1 = retCode;
		msg.obj = skilldeal;
		mHandler.sendMessage(msg);
	}
}
