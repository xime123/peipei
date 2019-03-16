package com.tshang.peipei.model.biz.reward;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.model.request.RequestBindAnonymNice;
import com.tshang.peipei.model.request.RequestBindAnonymNice.BindAnonymNickCallBack;
import com.tshang.peipei.model.request.RequestGetAnonymNiceInfo;
import com.tshang.peipei.model.request.RequestGetAnonymNiceInfo.GetAnonyNickInfoCallBack;
import com.tshang.peipei.model.request.RequestParticipatorInfo;
import com.tshang.peipei.model.request.RequestParticipatorInfo.GetRewardParticipatorCallBack;
import com.tshang.peipei.model.request.RequestRewardChat;
import com.tshang.peipei.model.request.RequestRewardChat.GetRewardChatCallBack;
import com.tshang.peipei.model.request.RequestRewardChatV2.GetRewardChatCallBackV2;
import com.tshang.peipei.model.request.RequestRewardGiftInfo;
import com.tshang.peipei.model.request.RequestRewardGiftInfo.GetRewardGiftInfoCallBack;
import com.tshang.peipei.model.request.RequestRewardJoin2;
import com.tshang.peipei.model.request.RequestRewardJoin2.GetRewardJoinCallBack2;
import com.tshang.peipei.model.request.RequestRewardListInfo;
import com.tshang.peipei.model.request.RequestRewardListInfo.GetRewardListInfoCallBack;
import com.tshang.peipei.model.request.RequestRewardPublish;
import com.tshang.peipei.model.request.RequestRewardPublish.publishRewardCallBack;
import com.tshang.peipei.model.request.RequestRewardChatV2;
import com.tshang.peipei.model.request.RequestRewardPublish2;
import com.tshang.peipei.model.request.RequestRewardSkillInfo;
import com.tshang.peipei.model.request.RequestRewardSkillInfo.GetRewardSkillInfoCallBack;
import com.tshang.peipei.model.request.RequestRewardTipInfo;
import com.tshang.peipei.model.request.RequestRewardTipInfo.GetRewardTipInfoCallBack;
import com.tshang.peipei.model.request.RequestSendParticipator;
import com.tshang.peipei.model.request.RequestSendParticipator.GetSendRewardParticipatorCallBack;
import com.tshang.peipei.model.request.RequestUserAnonymNiceInfo;
import com.tshang.peipei.model.request.RequestUserAnonymNiceInfo.GetUserAnonyNickCallBack;

/**
 * @Title: RewardRequestControl.java 
 *
 * @Description: 悬赏请求
 *
 * @author Aaron  
 *
 * @date 2015-10-8 下午5:48:48 
 *
 * @version V1.0   
 */
public class RewardRequestControl {
	/**
	 * 获取悬赏技能
	 * @author Aaron
	 *
	 * @param callBack
	 */
	public void requestRewardSkillInfo(GetRewardSkillInfoCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestRewardSkillInfo req = new RequestRewardSkillInfo();
		req.requestRewardSkillInfo(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
				BAApplication.mLocalUserInfo.sex.intValue(), callBack);
	}

	/**
	 * 获取悬赏礼物
	 * @author Aaron
	 *
	 * @param callBack
	 */
	public void requestRewardGift(GetRewardGiftInfoCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}

		RequestRewardGiftInfo req = new RequestRewardGiftInfo();
		req.requestRewardGiftInfo(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
				BAApplication.mLocalUserInfo.sex.intValue(), callBack);
	}

	/**
	 * 获取悬赏提示语
	 * @author Aaron
	 *
	 * @param callBack
	 */
	public void requestRewardTip(GetRewardTipInfoCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestRewardTipInfo req = new RequestRewardTipInfo();
		req.requestRewardTipInfo(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
				BAApplication.mLocalUserInfo.sex.intValue(), callBack);
	}

	/**
	 * 发布悬赏
	 * @author Aaron
	 *
	 * @param awardid 技能id
	 * @param giftid 礼物id
	 * @param callBack
	 */
	public void publishReward(int awardid, int giftid, int isanonymous, publishRewardCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestRewardPublish req = new RequestRewardPublish();
		req.requestRewardPublish(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
				BAApplication.mLocalUserInfo.sex.intValue(), awardid, giftid, isanonymous, callBack);
	}

	/**
	 * 发布悬赏
	 * @author Aaron
	 *
	 * @param type--1 自定义技能 0 系统技能
	 * @param skillName
	 * @param skillDesc
	 * @param giftid
	 * @param isanonymous 0:不匿名 ，1：匿名
	 * @param callBack
	 */
	public void publishReward(int uid, int type, String skillName, String skillDesc, int awardid, int giftid, int isanonymous,
			com.tshang.peipei.model.request.RequestRewardPublish2.publishRewardCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestRewardPublish2 req = new RequestRewardPublish2();
		req.requestRewardPublish(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, uid, type, skillName, skillDesc, awardid, giftid,
				isanonymous, callBack);
	}

	/**
	 * 拉取悬赏列表
	 * @author Aaron
	 *
	 * @param type --0 全部悬赏, 1 正在进行  2 结束悬赏 3 我参加的悬赏 4 我发布的悬赏 5 我赢得的悬赏
	 * @param start
	 * @param num
	 * @param callBack
	 */
	public void requestListInfo(int anonymNickId, int type, int start, int num, GetRewardListInfoCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestRewardListInfo req = new RequestRewardListInfo();
		req.requestRewardListInfo(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
				anonymNickId, type, start, num, callBack);
	}

	/**
	 * 参加悬赏
	 * @author Aaron
	 *
	 * @param type
	 * @param awarduid 赏主id
	 * @param selfuid 参加者id
	 * @param awardid 悬赏id
	 * @param callBack
	 */
	public void requestJoinReward(int type, int awarduid, int selfuid, int awardid, int anonym, GetRewardJoinCallBack2 callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestRewardJoin2 req = new RequestRewardJoin2();
		req.requestJoinReward(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(), type,
				awarduid, selfuid, awardid, anonym, callBack);
	}

	/**
	 * 
	 * @author 请求私聊
	 *
	 * @param type
	 * @param awarduid
	 * @param selfuid
	 * @param awardid
	 * @param callBack
	 */
	public void requestChat(int type, int awarduid, int selfuid, int awardid, GetRewardChatCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestRewardChat req = new RequestRewardChat();
		req.requestJoinReward(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(), type,
				awarduid, selfuid, awardid, callBack);
	}

	/**
	 * 
	 * @author 请求私聊
	 *
	 * @param type
	 * @param awarduid
	 * @param selfuid
	 * @param awardid
	 * @param callBack
	 */
	public void requestChatV2(int type, int awarduid, int selfuid, int awardid, int anonym, GetRewardChatCallBackV2 callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestRewardChatV2 req = new RequestRewardChatV2();
		req.requestRewardChatV2(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(), type,
				awarduid, selfuid, awardid, anonym, callBack);
	}

	/**
	 * 获取参与者列表
	 * @author Aaron
	 *
	 * @param type
	 * @param awarduid
	 * @param awardid
	 * @param callBack
	 */
	public void getParticipatorInfoList(int type, int awarduid, int awardid, int anonym, GetRewardParticipatorCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestParticipatorInfo req = new RequestParticipatorInfo();
		req.requestRewardParticipatorInfo(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
				BAApplication.mLocalUserInfo.uid.intValue(), type, awarduid, awardid, anonym, callBack);
	}

	/**
	 * 悬赏
	 * @author Aaron
	 *
	 * @param type
	 * @param awarduid
	 * @param otheruid
	 * @param awardid
	 * @param callBack
	 */
	public void requestParticipator(int type, int awarduid, int otheruid, int awardid, int awardType, int anonym,
			GetSendRewardParticipatorCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestSendParticipator req = new RequestSendParticipator();
		req.requestSendRewardParticipator(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
				BAApplication.mLocalUserInfo.uid.intValue(), type, awarduid, otheruid, awardid, awardType, anonym, callBack);
	}

	/**
	 * 检测匿名绑定
	 *
	 * @param callBack
	 */
	public void requestBindAnonymNick(BindAnonymNickCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestBindAnonymNice req = new RequestBindAnonymNice();
		req.requestJoinReward(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
				callBack);
	}

	/**
	 * 获取Nick
	 *
	 * @param callBack
	 */
	public void requestGetAnonymNickInfo(GetAnonyNickInfoCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestGetAnonymNiceInfo req = new RequestGetAnonymNiceInfo();
		req.requestJoinReward(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
				callBack);
	}

	/**
	 * 确定使用匿名Nick
	 *
	 * @param id
	 * @param callBack
	 */
	public void requestUserAnonyNickInfo(int id, GetUserAnonyNickCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestUserAnonymNiceInfo req = new RequestUserAnonymNiceInfo();
		req.requestJoinReward(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(), id,
				callBack);
	}
}
