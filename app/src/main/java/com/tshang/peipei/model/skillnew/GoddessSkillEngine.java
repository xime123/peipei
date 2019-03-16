package com.tshang.peipei.model.skillnew;

import java.util.List;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.model.request.RequestGetGoddessSkillList;
import com.tshang.peipei.model.request.RequestSkillInviteResult;
import com.tshang.peipei.model.request.RequestSndSkillInvite;
import com.tshang.peipei.model.request.RequestGetGoddessSkillList.GetGoddessSkillListCallBack;
import com.tshang.peipei.model.request.RequestGetSkillGiftList;
import com.tshang.peipei.model.request.RequestGetSkillGiftList.GetSkillGiftListCallBack;
import com.tshang.peipei.model.request.RequestGetSkillTipInfo;
import com.tshang.peipei.model.request.RequestGetSkillTipInfo.GetSkillInfoCallBack;
import com.tshang.peipei.model.request.RequestPersonSkillInfo;
import com.tshang.peipei.model.request.RequestPersonSkillInfo.GetPersonSkillInfo;
import com.tshang.peipei.model.request.RequestSaveGoddessSkill;
import com.tshang.peipei.model.request.RequestSaveGoddessSkill.SaveGoddessSkillCallBack;
import com.tshang.peipei.model.request.RequestSkillInviteResult.SkillInviteResultCallBack;
import com.tshang.peipei.model.request.RequestSndSkillInvite.SendSkillInviteCallBack;
import com.tshang.peipei.protocol.asn.gogirl.SkillInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillInfoList;

/**
 * @Title: GoddessSkillEngine.java 
 *
 * @Description: 女神技请求
 *
 * @author DYH  
 *
 * @date 2015-11-5 下午5:30:41 
 *
 * @version V1.0   
 */
public class GoddessSkillEngine {

	public void requestGoddessSkillList(int type, int start, int num, GetGoddessSkillListCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}

		RequestGetGoddessSkillList req = new RequestGetGoddessSkillList();
		req.requestGoddessSkillList(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
				type, start, num, callBack);
	}

	public void requestSaveGoddessSkill(List<SkillInfo> mList, SaveGoddessSkillCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}

		SkillInfoList list = new SkillInfoList();
		addElement(list, mList);
		RequestSaveGoddessSkill req = new RequestSaveGoddessSkill();
		req.requestSaveGoddessSkill(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
				list, callBack);
	}

	public void requestPersonSkillInfo(int uid, GetPersonSkillInfo callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}

		RequestPersonSkillInfo req = new RequestPersonSkillInfo();
		req.requestPersonSkillInfo(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, uid, callBack);

	}

	public void requestGetSkillGiftList(int type, int mFriendUid, GetSkillGiftListCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestGetSkillGiftList req = new RequestGetSkillGiftList();
		req.requestSkillGiftList(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(), mFriendUid, type,
				callBack);
	}
	
	public void requestGetSkillTipInfo(int type, int mFriendUid, GetSkillInfoCallBack callBack){
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestGetSkillTipInfo req = new RequestGetSkillTipInfo();
		req.requestGetSkillTipInfo(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(), mFriendUid, type, callBack);
	}
	
	public void requestSkillInvite(int touid, int skillid, int giftid, String additionalword, SendSkillInviteCallBack callBack){
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestSndSkillInvite req = new RequestSndSkillInvite();
		req.requestSendSkillInvite(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
				touid, skillid, giftid, additionalword, callBack);
	}
	
	public void requestSkillInviteResult(int touid, int skillid, int giftid, int invitedstatus, int skilllistid, SkillInviteResultCallBack callBack){
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		
		RequestSkillInviteResult req = new RequestSkillInviteResult();
		req.requestSkillInviteResult(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(), touid, skillid, giftid, invitedstatus, skilllistid, callBack);
	}

	private void addElement(SkillInfoList list, List<SkillInfo> mList) {
		if (mList != null) {
			for (SkillInfo item : mList) {
				list.add(item);
			}
		}
	}
}
