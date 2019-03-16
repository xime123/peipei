package com.tshang.peipei.model.biz.chat;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.path.android.jobqueue.JobManager;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.bean.HaremEmotionUtil;
import com.tshang.peipei.base.ILog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatDes;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.json.ApplyJoinGroupInfoJson;
import com.tshang.peipei.base.json.MemberInOutInfoJson;
import com.tshang.peipei.model.biz.PeiPeiGetVoiceBiz;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.ChatManageBiz.IPersistListener;
import com.tshang.peipei.model.biz.jobs.DownLoadImageJob;
import com.tshang.peipei.model.entity.ChatAlbumEntity;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.protocol.asn.gogirl.ApplyJoinGroupInfo;
import com.tshang.peipei.protocol.asn.gogirl.AwardInteractiveInfo;
import com.tshang.peipei.protocol.asn.gogirl.DareGameInfo;
import com.tshang.peipei.protocol.asn.gogirl.DareInfo;
import com.tshang.peipei.protocol.asn.gogirl.FingerGuessingInfo;
import com.tshang.peipei.protocol.asn.gogirl.GGSkillInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlChatData;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.MemberInOutInfo;
import com.tshang.peipei.protocol.asn.gogirl.NotifySndAwardInfo;
import com.tshang.peipei.protocol.asn.gogirl.NotifySndAwardInfoV2;
import com.tshang.peipei.protocol.asn.gogirl.ParticipateInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqGoGirlTransChatData;
import com.tshang.peipei.protocol.asn.gogirl.SkillDealInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillInteractiveInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillResultInfo;
import com.tshang.peipei.protocol.asn.gogirl.SystemNotifyInfo;
import com.tshang.peipei.protocol.asn.gogirl.TruthAnswer;
import com.tshang.peipei.protocol.asn.gogirl.TruthAnswerList;
import com.tshang.peipei.protocol.asn.gogirl.TruthInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.operate.ChatOperate;
import com.tshang.peipei.storage.database.operate.RedpacketOperate;

import de.greenrobot.event.EventBus;

/**
 * @Title: SaveChatData.java 
 *
 * @Description:(用一句话描述该文件做什么) 保存聊天数据
 *
 * @author Jeff 
 *
 * @date 2014年10月17日 下午4:45:29 
 *
 * @version V1.0   
 */
public class SaveChatData {

	private static final String TAG = "SaveChatData";
	public static final int CAN_GRAP_SOLITAIRE_REDPACKET = 10000;

	public static boolean saveBaseChatData() {
		return true;
	}

	public synchronized static long saveAgreeJoinHaremMsg(Context context, int type, int sex, String nick, ChatDatabaseEntity chatDatabaseEntity,
			MemberInOutInfo memberInOutInfo) {//存储文本信息
		if (memberInOutInfo == null) {
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}
		String sessionMsg = "";
		int act = memberInOutInfo.act.intValue();
		if (act == 1) {//主动离开
			sessionMsg = "[退出后宫]";
		} else if (act == 0) {//0进来
			sessionMsg = "[加入后宫]";
		} else if (act == 2) {//2-被踢
			GoGirlUserInfo info = UserUtils.getUserEntity(context);
			if (info != null) {
				if (info.uid.intValue() == memberInOutInfo.uid.intValue()) {
					ChatSessionManageBiz.removeChatSessionWithUserID(context, memberInOutInfo.groupid.intValue(), 1);//删除掉个人群的会话列表
					sessionMsg = "[被移除后宫]";
				}
			}

		}
		String message = MemberInOutInfoJson.changeObjectDateToJson(memberInOutInfo);
		chatDatabaseEntity.setMessage(message);
		saveSessionMessage(context, type, chatDatabaseEntity, sessionMsg, nick, sex);
		return insertSql(context, type, chatDatabaseEntity);
	}

	/**
	 * 
	 * @author Jeff
	 *
	 * @param context 上下文
	 * @param type 0私聊 ，1群聊
	 * @param entity 存储数据实体
	 * @return
	 */
	private synchronized static long insertSql(Context context, int type, ChatDatabaseEntity entity) {
		ChatOperate chatDatabase = getChatOperate(context, type, entity);
		if (chatDatabase != null) {
			if (chatDatabase.selectDataByTime(entity.getCreateTime())) {
				return -1;
			}
			return chatDatabase.insert(entity);
		}
		return -1;
	}

	public synchronized static long saveGiftDealInfoListMsg(Context context, int type, int sex, String nick, ChatDatabaseEntity chatDatabaseEntity,
			GiftDealInfoList list) {//存储文本信息
		if (list == null || list.isEmpty()) {
			return -1;
		}
		GiftDealInfo info = (GiftDealInfo) list.get(0);
		if (info == null) {
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(context);
		String sessionMsg = "";
		if (userInfo != null) {

			if (info.gift.id.intValue() != 100) {
				sessionMsg = String.format(context.getString(R.string.chat_gift_context_message), new String(BAApplication.mLocalUserInfo.nick));
				if (userInfo.sex.intValue() == Gender.FEMALE.getValue()) {
					int num = SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKeyToZero(
							BAConstants.GIFTS_NUM);
					SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "").saveIntKeyValue(num + 1,
							BAConstants.GIFTS_NUM);

					NoticeEvent noticeEvent = new NoticeEvent();
					noticeEvent.setFlag(NoticeEvent.NOTICE28);
					EventBus.getDefault().postSticky(noticeEvent);
				}
			} else {
				sessionMsg = context.getString(R.string.chat_gift_context_message2);
			}
		}

		String message = ChatMessageBiz.saveGiftMessage(list);

		chatDatabaseEntity.setMessage(message);
		saveSessionMessage(context, type, chatDatabaseEntity, sessionMsg, nick, sex);
		return insertSql(context, type, chatDatabaseEntity);
	}

	public synchronized static long saveGiftDealInfoListMsgV2(Context context, int type, int sex, String nick, ChatDatabaseEntity chatDatabaseEntity,
			GiftDealInfoList list, ReqGoGirlTransChatData req) {//存储文本信息
		if (list == null || list.isEmpty()) {
			return -1;
		}
		GiftDealInfo info = (GiftDealInfo) list.get(0);
		if (info == null) {
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(context);
		String sessionMsg = "";
		if (userInfo != null) {
			if (info.gift.id.intValue() != 100) {
				sessionMsg = String.format(context.getString(R.string.chat_gift_context_message), new String(req.chatdata.tonick));
				if (userInfo.sex.intValue() == Gender.FEMALE.getValue()) {
					int num = SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKeyToZero(
							BAConstants.GIFTS_NUM);
					SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "").saveIntKeyValue(num + 1,
							BAConstants.GIFTS_NUM);

					NoticeEvent noticeEvent = new NoticeEvent();
					noticeEvent.setFlag(NoticeEvent.NOTICE28);
					EventBus.getDefault().postSticky(noticeEvent);
				}
			} else {
				sessionMsg = context.getString(R.string.chat_gift_context_message2);
			}
		}
		String message = ChatMessageBiz.saveGiftMessage(list);
		chatDatabaseEntity.setRevStr3("1");
		chatDatabaseEntity.setMessage(message);
		saveSessionMessage(context, type, chatDatabaseEntity, sessionMsg, nick, sex);
		chatDatabaseEntity.setRevStr2(new String(req.chatdata.tonick));
		return insertSql(context, type, chatDatabaseEntity);
	}

	public synchronized static long saveJoinHaremMsg(Context context, int type, int sex, String nick, ChatDatabaseEntity chatDatabaseEntity,
			ApplyJoinGroupInfo applyJoinGroupInfo) {//存储文本信息
		if (applyJoinGroupInfo == null) {
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}
		String message = ApplyJoinGroupInfoJson.changeObjectDateToJson(applyJoinGroupInfo);
		chatDatabaseEntity.setMessage(message);
		saveSessionMessage(context, type, chatDatabaseEntity, "[申请加入后宫]", nick, sex);
		return insertSql(context, type, chatDatabaseEntity);
	}

	public synchronized static long saveRedPacketInfoMsg(Context context, int type, int sex, String nick, ChatDatabaseEntity chatDatabaseEntity,
			RedPacketInfo redPacketInfo) {//存储文本信息
		if (redPacketInfo == null) {//ReqUnpackRedPacket
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}
		String message = ChatMessageBiz.saveRedPacketInfo(redPacketInfo);
		chatDatabaseEntity.setMessage(message);
		saveSessionMessage(context, type, chatDatabaseEntity, "[恩赐红包]", nick, sex);
		return insertSql(context, type, chatDatabaseEntity);
	}

	public synchronized static long saveSystemNotifyInfoMsg(Context context, int type, int sex, String nick, ChatDatabaseEntity chatDatabaseEntity,
			SystemNotifyInfo systemNotifyInfo) {//存储文本信息
		if (systemNotifyInfo == null) {
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}
		String message = ChatMessageBiz.saveSystemNotifyInfo(systemNotifyInfo);
		chatDatabaseEntity.setMessage(message);
		saveSessionMessage(context, type, chatDatabaseEntity, new String(systemNotifyInfo.title), nick, sex);
		return insertSql(context, type, chatDatabaseEntity);
	}

	public synchronized static long saveSkillDealInfoMsg(Context context, int type, int sex, String nick, ChatDatabaseEntity chatDatabaseEntity,
			SkillDealInfo skillDealInfo) {//存储文本信息
		if (skillDealInfo == null) {//ReqUnpackRedPacket
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}
		String message = ChatMessageBiz.saveSkillDealInfo(skillDealInfo);
		chatDatabaseEntity.setMessage(message);
		String session = "[打赏技能]";
		if (skillDealInfo.skillinfo.type.intValue() == 0) {//求赏
			session = "[求赏技能]";
		} else if (skillDealInfo.skillinfo.type.intValue() == 1) {//打赏
			session = "[打赏技能]";
		}
		saveSessionMessage(context, type, chatDatabaseEntity, session, nick, sex);
		return insertSql(context, type, chatDatabaseEntity);
	}

	/**
	 * 悬赏私聊
	 * @author Aaron
	 *
	 * @param context
	 * @param type
	 * @param sex
	 * @param nick
	 * @param req
	 * @param chatDatabaseEntity
	 * @param awardInfo
	 * @return
	 */
	public synchronized static long saveAwardInfo(Context context, int type, int sex, String nick, ReqGoGirlTransChatData req,
			ChatDatabaseEntity chatDatabaseEntity, AwardInteractiveInfo awardInfo) {
		if (awardInfo == null) {
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}

		long endTime = awardInfo.endtime.intValue();
		long leftTime = awardInfo.lefttime.intValue();
		long currentTime = System.currentTimeMillis() / 1000;
		if (currentTime < endTime) {
			endTime = currentTime + leftTime;
		}
		int anonym = req.chatdata.revint0.intValue();

		String message = ChatMessageBiz.saveAwardInfo(context, awardInfo.awardTextInfo, awardInfo.awardGiftInfo, awardInfo.createtime.intValue(),
				endTime, anonym);
		chatDatabaseEntity.setMessage(message);
		chatDatabaseEntity.setMesSvrID(String.valueOf(awardInfo.createtime.intValue()));
		chatDatabaseEntity.setToUid(req.chatdata.to.intValue());

		String session = context.getResources().getString(R.string.reward_session);

		if (anonym == 1) {//匿名悬赏
			if (req.chatdata.to.intValue() != req.chatdata.revint1.intValue()) {//赏主
				chatDatabaseEntity.setDes(ChatDes.TO_ME.getValue());
			} else {//受赏主
				chatDatabaseEntity.setRevStr1(req.chatdata.tosex + "");
				chatDatabaseEntity.setDes(ChatDes.TO_FRIEDN.getValue());
				chatDatabaseEntity.setProgress(req.chatdata.to.intValue());
				chatDatabaseEntity.setFromID(req.chatdata.to.intValue());
				chatDatabaseEntity.setToUid(req.chatdata.from.intValue());
				chatDatabaseEntity.setRevStr2(new String(req.chatdata.tonick));
				nick = new String(req.chatdata.tonick);
				sex = req.chatdata.tosex.intValue();
			}
			saveSessionMessage(context, type, chatDatabaseEntity, session, nick, sex);
		} else {
			if (BAApplication.mLocalUserInfo.uid.intValue() == req.chatdata.to.intValue()) {//赏主
				ChatDatabaseEntity newChatDatabaseEntity = chatDatabaseEntity;
				newChatDatabaseEntity.setFromID(req.fromuid.intValue());
				newChatDatabaseEntity.setToUid(req.touid.intValue());
				chatDatabaseEntity.setProgress(req.touid.intValue());
				saveSessionMessage(context, type, newChatDatabaseEntity, session, nick, sex);
			} else {//受赏主
				ChatDatabaseEntity newChatDatabaseEntity = chatDatabaseEntity;
				newChatDatabaseEntity.setFromID(req.chatdata.to.intValue());
				newChatDatabaseEntity.setToUid(req.chatdata.to.intValue());
				chatDatabaseEntity.setProgress(req.chatdata.to.intValue());
				chatDatabaseEntity.setFromID(req.chatdata.to.intValue());
				chatDatabaseEntity.setToUid(req.chatdata.from.intValue());
				saveSessionMessage(context, type, newChatDatabaseEntity, session, new String(awardInfo.nick), awardInfo.sex.intValue());
			}
			chatDatabaseEntity.setMesLocalID(-1);
		}
		SharedPreferencesTools.getInstance(context).saveLongKeyValue(System.currentTimeMillis(),
				String.valueOf(chatDatabaseEntity.getFromID()) + "_endRewardTime");
		return insertSql(context, type, chatDatabaseEntity);
	}

	/**
	 * 更新悬赏结束 
	 * @author Aaron
	 *
	 * @param content
	 * @param info
	 */
	public synchronized static void updateAwardInfo(Context context, NotifySndAwardInfo info, boolean isGroupChat) {
		int frindUid;
		if (BAApplication.mLocalUserInfo.uid.intValue() == info.awarduid.intValue()) {//赏主
			frindUid = info.participateuid.intValue();
		} else {
			frindUid = info.awarduid.intValue();
		}
		ChatOperate chatOperateTruth = ChatOperate.getInstance(context, frindUid, isGroupChat);
		if (chatOperateTruth != null) {
			chatOperateTruth.updateStr3ByCreateTime(String.valueOf(info.createtime.intValue()), 1 + "");
		}
	}

	/**
	 * 更新悬赏结束 
	 * @author Aaron
	 *
	 * @param context
	 * @param info
	 * @param isGroupChat
	 * @param toType
	 */
	public synchronized static void updateAwardInfo(Context context, NotifySndAwardInfoV2 info, boolean isGroupChat, int toType,
			GoGirlChatData chatData) {
		int frindUid;
		if (toType == 3) {//匿名
			frindUid = info.awarduid.intValue();
			ChatOperate chatOperateTruth = ChatOperate.getInstance(context, frindUid, isGroupChat);
			if (chatOperateTruth != null) {
				chatOperateTruth.updateStr3ByCreateTime(String.valueOf(info.createtime.intValue()), 1 + "");
			}

			if (info.participateInfoList.size() <= 0) {
				return;
			}

			for (int i = 0; i < info.participateInfoList.size(); i++) {
				ParticipateInfo participateInfo = (ParticipateInfo) info.participateInfoList.get(i);
				frindUid = participateInfo.userinfo.uid.intValue();
				Log.d("Aaron", "userinfo.uid=" + participateInfo.userinfo.uid.intValue());
				chatOperateTruth = ChatOperate.getInstance(context, frindUid, isGroupChat);
				if (chatOperateTruth != null) {
					chatOperateTruth.updateStr3ByCreateTime(String.valueOf(info.createtime.intValue()), 1 + "");
				}
			}
		} else {//非匿名
			if (BAApplication.mLocalUserInfo.uid.intValue() == info.awarduid.intValue()) {//赏主
				if (info.participateInfoList.size() > 0) {
					for (int i = 0; i < info.participateInfoList.size(); i++) {
						ParticipateInfo participateInfo = (ParticipateInfo) info.participateInfoList.get(i);
						frindUid = participateInfo.userinfo.uid.intValue();
						ChatOperate chatOperateTruth = ChatOperate.getInstance(context, frindUid, isGroupChat);
						if (chatOperateTruth != null) {
							chatOperateTruth.updateStr3ByCreateTime(String.valueOf(info.createtime.intValue()), 1 + "");
						}
					}
				}
			} else {
				frindUid = info.awarduid.intValue();
				ChatOperate chatOperateTruth = ChatOperate.getInstance(context, frindUid, isGroupChat);
				if (chatOperateTruth != null) {
					chatOperateTruth.updateStr3ByCreateTime(String.valueOf(info.createtime.intValue()), 1 + "");
				}
			}
		}
	}

	/**
	 * 保存女神技能信息
	 * @author DYH
	 *
	 * @param context
	 * @param type
	 * @param sex
	 * @param nick
	 * @param req
	 * @param chatDatabaseEntity
	 * @param awardInfo
	 * @return
	 */
	public synchronized static long saveGoddessSkillInfo(Context context, int type, int sex, String nick, ReqGoGirlTransChatData req,
			ChatDatabaseEntity chatDatabaseEntity, SkillInteractiveInfo skillInfo) {
		if (skillInfo == null) {
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}

		int lefttime = skillInfo.lefttime.intValue();

		String message = ChatMessageBiz.saveGoddessSkillInfo(context, skillInfo.skillTextInfo, skillInfo.skillGiftInfo, lefttime, sex,
				skillInfo.skilllistid.intValue());
		chatDatabaseEntity.setMessage(message);
		chatDatabaseEntity.setMesSvrID(String.valueOf(skillInfo.createtime.longValue()));
		long leftTime = System.currentTimeMillis() / 1000;
		leftTime = leftTime + skillInfo.lefttime.intValue();
		chatDatabaseEntity.setRevStr3(String.valueOf(leftTime));
		chatDatabaseEntity.setProgress(2); //2为自定义的状态，代表用户还没有做操作, 3为等待超时
		chatDatabaseEntity.setRevStr2(new String(skillInfo.additionalword));

		String session = context.getResources().getString(R.string.str_skill_session);

		if (BAApplication.mLocalUserInfo.uid.intValue() == req.chatdata.to.intValue()) {//赏主

			ChatDatabaseEntity newChatDatabaseEntity = chatDatabaseEntity;
			newChatDatabaseEntity.setFromID(req.fromuid.intValue());
			newChatDatabaseEntity.setToUid(req.touid.intValue());
			saveSessionMessage(context, type, newChatDatabaseEntity, session, nick, sex);
		} else {//受赏主
			ChatDatabaseEntity newChatDatabaseEntity = chatDatabaseEntity;
			newChatDatabaseEntity.setFromID(req.fromuid.intValue());
			newChatDatabaseEntity.setToUid(req.touid.intValue());
			saveSessionMessage(context, type, newChatDatabaseEntity, session, new String(skillInfo.nick), skillInfo.sex.intValue());
		}
		return insertSql(context, type, chatDatabaseEntity);
	}

	public synchronized static void updateGoddessSkillInfo(Context context, int friendUid, SkillResultInfo info, boolean isGroupChat) {
		if (info == null) {
			return;
		}

		ChatOperate chatOperateSkill = ChatOperate.getInstance(context, friendUid, isGroupChat);
		if (chatOperateSkill != null) {
			chatOperateSkill.updateProgressByMesSvrID(String.valueOf(info.createtime.longValue()), info.invitedstatus.intValue());
			//通知界面实时更新
			NoticeEvent n = new NoticeEvent();
			n.setFlag(NoticeEvent.NOTICE95);
			n.setNum4(info.createtime.longValue());
			n.setNum2(info.invitedstatus.intValue());
			EventBus.getDefault().post(n);
		}
	}

	public synchronized static void updateGoddessSkillStatus(Context context, ChatDatabaseEntity entity, int progress, boolean isGroupChat) {
		ChatOperate skillOperate = ChatOperate.getInstance(context, entity.getFromID(), isGroupChat);
		if (skillOperate != null) {
			if (entity != null) {
				ChatMessageEntity chatMessageEntity = ChatMessageBiz.decodeMessage(entity.getMessage());
				skillOperate.updateProgressByMesSvrID(String.valueOf(chatMessageEntity.getCreatetime()), progress);
			}
		}
	}

	public synchronized static long saveSolitaireRedPacketInfo(Context context, int type, int sex, String nick,
			ChatDatabaseEntity chatDatabaseEntity, RedPacketBetCreateInfo solitaireRedPacket) {
		if (solitaireRedPacket == null) {//ReqUnpackRedPacket
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}
		String message = ChatMessageBiz.saveSolitaireRedPacketInfo(context, solitaireRedPacket);
		chatDatabaseEntity.setMessage(message);
		chatDatabaseEntity.setCreateTime(solitaireRedPacket.clicktime.longValue() * 1000);
		saveSessionMessage(context, type, chatDatabaseEntity, "[红包接龙]", nick, sex);
		return insertSql(context, type, chatDatabaseEntity);
	}

	public synchronized static long saveCanGrapSolitaireInfo(Context context, int type, int sex, String nick, ChatDatabaseEntity chatDatabaseEntity,
			RedPacketBetCreateInfo solitaireRedPacket) {
		if (solitaireRedPacket == null) {
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}

		String message = ChatMessageBiz.saveSolitaireRedPacketInfo(context, solitaireRedPacket);
		chatDatabaseEntity.setMessage(message);
		chatDatabaseEntity.setCreateTime(solitaireRedPacket.clicktime.longValue() * 1000);
		chatDatabaseEntity.setMesSvrID(solitaireRedPacket.id.intValue() + "");
		chatDatabaseEntity.setProgress(solitaireRedPacket.redpacketstatus.intValue());
		return RedpacketOperate.getInstance(context, chatDatabaseEntity.getToUid()).insert(chatDatabaseEntity);
	}

	public synchronized static void updateCanGrapSolitaireInfo(Context context, int type, int sex, String nick, ChatDatabaseEntity entity,
			RedPacketBetCreateInfo solitaireRedPacket) {
		if (solitaireRedPacket == null) {
			return;
		}
		if (entity == null) {
			return;
		}

		RedpacketOperate operate = RedpacketOperate.getInstance(context, entity.getToUid());
		if (operate != null) {
			int isHave = operate.getCountById(solitaireRedPacket.id.intValue() + "");
			if (isHave > 0) {
				if (solitaireRedPacket.redpacketstatus.intValue() == 1 || solitaireRedPacket.redpacketstatus.intValue() == 2
						|| solitaireRedPacket.redpacketstatus.intValue() == 4) {
					operate.delete(solitaireRedPacket.id.intValue());
				} else {
					String message = ChatMessageBiz.saveSolitaireRedPacketInfo(context, solitaireRedPacket);
					operate.updateMessageByMesSvrID(String.valueOf(solitaireRedPacket.id.intValue()), message);
					operate.updateProgressByMesSvrID(String.valueOf(solitaireRedPacket.id.intValue()), solitaireRedPacket.redpacketstatus.intValue());
				}
				NoticeEvent event = new NoticeEvent();
				event.setObj(entity);
				event.setFlag(NoticeEvent.NOTICE99);
				EventBus.getDefault().post(event);
			} else {
				long saveSuccess = saveCanGrapSolitaireInfo(context, type, sex, nick, entity, solitaireRedPacket);
				if (saveSuccess > 0) {
					NoticeEvent event = new NoticeEvent();
					event.setObj(entity);
					event.setFlag(NoticeEvent.NOTICE99);
					EventBus.getDefault().post(event);
				}
			}
		}
	}

	public synchronized static long saveGGSkillInfoMsg(Context context, int type, int sex, String nick, ChatDatabaseEntity chatDatabaseEntity,
			GGSkillInfo ggSkillInfo) {
		if (ggSkillInfo == null) {
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}
		String message = ChatMessageBiz.saveGGSkillInfo(ggSkillInfo);
		chatDatabaseEntity.setMessage(message);
		String session = "[感兴趣打赏]";
		saveSessionMessage(context, type, chatDatabaseEntity, session, nick, sex);
		return insertSql(context, type, chatDatabaseEntity);
	}

	public synchronized static long saveStartDareInfoMsg(Context context, int type, int sex, String nick, ChatDatabaseEntity chatDatabaseEntity,
			DareGameInfo ggSkillInfo) {
		if (ggSkillInfo == null) {
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}

		chatDatabaseEntity.setMessage(ggSkillInfo.dicenum.intValue() + "");
		String session = "[大冒险]";
		saveSessionMessage(context, type, chatDatabaseEntity, session, nick, sex);
		return insertSql(context, type, chatDatabaseEntity);
	}

	public synchronized static long saveDareInfoMsg(Context context, int type, int sex, String nick, ChatDatabaseEntity chatDatabaseEntity,
			DareInfo ggSkillInfo) {
		if (ggSkillInfo == null) {
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}
		chatDatabaseEntity.setProgress(ggSkillInfo.status.intValue());
		chatDatabaseEntity.setMesSvrID(new String(ggSkillInfo.globalid));
		String session = "[大冒险]";
		saveSessionMessage(context, type, chatDatabaseEntity, session, nick, sex);
		return insertSql(context, type, chatDatabaseEntity);
	}

	/**
	 * 真心话信息保存
	 * @author Aaron
	 *
	 * @param context
	 * @param type
	 * @param sex
	 * @param nick
	 * @param chatDatabaseEntity
	 * @param truthInfo
	 * @return
	 */
	public synchronized static long saveTruthInfo(Context context, int type, int sex, String nick, ReqGoGirlTransChatData req,
			ChatDatabaseEntity chatDatabaseEntity, TruthInfo truthInfo) {
		if (truthInfo == null) {
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}
		Log.e("Aaron", "fromuid=" + req.chatdata.from.intValue() + ", touid=" + req.chatdata.to.intValue());
		//		//组装消息
		if (BAApplication.mLocalUserInfo != null && req.chatdata.from.intValue() == BAApplication.mLocalUserInfo.uid.intValue()) {
			chatDatabaseEntity.setDes(ChatDes.TO_ME.getValue());
			chatDatabaseEntity.setRevStr2(new String(req.chatdata.tonick));
			chatDatabaseEntity.setFromID(req.chatdata.to.intValue());
			chatDatabaseEntity.setToUid(req.chatdata.from.intValue());
			nick = (new String(req.chatdata.tonick));
			chatDatabaseEntity.setRevStr2(nick);
		} else {
			chatDatabaseEntity.setDes(ChatDes.TO_FRIEDN.getValue());
			chatDatabaseEntity.setRevStr2(new String(req.chatdata.fromnick));
			chatDatabaseEntity.setFromID(req.chatdata.from.intValue());
			chatDatabaseEntity.setToUid(req.chatdata.to.intValue());

		}
		String seesion = "[真心话]";
		chatDatabaseEntity.setMessage(new String(truthInfo.strquestion));
		chatDatabaseEntity.setMesSvrID(new String(truthInfo.truthid));
		chatDatabaseEntity.setRevStr3(pareTruthAnswerlist(truthInfo.answerlist));
		saveSessionMessage(context, type, chatDatabaseEntity, seesion, nick, sex);
		return insertSql(context, type, chatDatabaseEntity);
	}

	private static String pareTruthAnswerlist(TruthAnswerList list) {
		StringBuffer ans = new StringBuffer();
		TruthAnswer answer;
		for (int i = 0; i < list.size(); i++) {
			answer = (TruthAnswer) list.get(i);
			String a = answer.answerid.intValue() + "+" + new String(answer.answermemo);
			ans.append(a).append(",");
		}
		ILog.d(TAG, "answerResult=" + ans.toString());
		return ans.toString().substring(0, ans.length());
	}

	/**
	 * 
	 * @author 保存真心话答案
	 *
	 * @param context
	 * @param type
	 * @param sex
	 * @param nick
	 * @param chatDatabaseEntity
	 * @param answerInfo
	 * @return
	 */
	public synchronized static long saveAnswerInfo(Context context, int type, int sex, String nick, ReqGoGirlTransChatData req,
			ChatDatabaseEntity chatDatabaseEntity, TruthAnswer answerInfo) {
		if (answerInfo == null) {
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}
		Log.e("Aaron", "fromuid=" + req.chatdata.from.intValue() + ", touid=" + req.chatdata.to.intValue());
		//组装消息
		if (BAApplication.mLocalUserInfo != null && req.chatdata.from.intValue() == BAApplication.mLocalUserInfo.uid.intValue()) {
			chatDatabaseEntity.setDes(ChatDes.TO_FRIEDN.getValue());
			chatDatabaseEntity.setRevStr2(new String(req.chatdata.tonick));
			chatDatabaseEntity.setFromID(req.chatdata.to.intValue());
			chatDatabaseEntity.setToUid(req.chatdata.from.intValue());
			nick = (new String(req.chatdata.tonick));
			chatDatabaseEntity.setRevStr2(nick);
		} else {
			chatDatabaseEntity.setDes(ChatDes.TO_ME.getValue());
			chatDatabaseEntity.setRevStr2(new String(req.chatdata.fromnick));
			chatDatabaseEntity.setFromID(req.chatdata.from.intValue());
			chatDatabaseEntity.setToUid(req.chatdata.to.intValue());

		}
		String seesion = "[真心话]";
		chatDatabaseEntity.setMessage(new String(answerInfo.answermemo));
		chatDatabaseEntity.setMesSvrID(String.valueOf(answerInfo.answerid.intValue()));
		saveSessionMessage(context, type, chatDatabaseEntity, seesion, nick, type);
		return insertSql(context, type, chatDatabaseEntity);
	}

	public synchronized static long saveFinishDareInfoMsg(Context context, int type, int sex, String nick, ChatDatabaseEntity chatDatabaseEntity,
			DareInfo ggSkillInfo) {
		if (ggSkillInfo == null) {
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}
		String content = new String(ggSkillInfo.memo);

		chatDatabaseEntity.setProgress(ggSkillInfo.status.intValue());
		chatDatabaseEntity.setRevStr3(content);
		chatDatabaseEntity.setMesSvrID(new String(ggSkillInfo.globalid));

		ChatOperate chatOperate = ChatOperate.getInstance(context, chatDatabaseEntity.getToUid(), true);
		if (chatOperate != null)
			chatOperate.updateProgressByMesSvrID(new String(ggSkillInfo.globalid), ggSkillInfo.status.intValue());

		chatOperate = null;

		String session = "[大冒险]";
		saveSessionMessage(context, type, chatDatabaseEntity, session, nick, sex);
		return insertSql(context, type, chatDatabaseEntity);
	}

	/**
	 * 保存真心话结果圣诞框
	 * @author Aaron
	 *
	 * @param context
	 * @param type
	 * @param sex
	 * @param nick
	 * @param chatDatabaseEntity
	 * @param ggSkillInfo
	 * @return
	 */
	public synchronized static long saveDialogDareInfoMsg(Context context, int type, int sex, String nick, ChatDatabaseEntity chatDatabaseEntity,
			DareInfo ggSkillInfo) {
		if (ggSkillInfo == null) {
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}

		String message = ChatMessageBiz.saveDareInfo(context, ggSkillInfo);
		String content = new String(ggSkillInfo.memo);

		chatDatabaseEntity.setMessage(message);
		chatDatabaseEntity.setRevStr3(content);
		chatDatabaseEntity.setProgress(ggSkillInfo.status.intValue());
		chatDatabaseEntity.setMesSvrID(new String(ggSkillInfo.globalid));

		String session = "[大冒险]";
		saveSessionMessage(context, type, chatDatabaseEntity, session, nick, sex);
		return insertSql(context, type, chatDatabaseEntity);
	}

	public synchronized static long saveChatAlbumEntityMsg(Context context, int type, int sex, String nick, ChatDatabaseEntity chatDatabaseEntity,
			ChatAlbumEntity chatAlbumEntity) {//存储文本信息
		if (chatAlbumEntity == null) {
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}
		String message = ChatMessageBiz.savePrivateMessage(chatAlbumEntity);
		String sessionMsg = String.format(context.getString(R.string.chat_album_content), new String(chatAlbumEntity.albumname),
				chatAlbumEntity.accessloyalty.intValue());
		chatDatabaseEntity.setMessage(message);
		saveSessionMessage(context, type, chatDatabaseEntity, sessionMsg, nick, sex);
		return insertSql(context, type, chatDatabaseEntity);
	}

	public synchronized static String getFingerSessionMsg(Context context, FingerGuessingInfo fingerInfo) {
		if (fingerInfo == null) {
			return "";
		}
		String sessionMsg = "";
		if (fingerInfo.winuid.intValue() >= 0) {
			if (fingerInfo.winuid.intValue() == 0) {
				sessionMsg = context.getString(R.string.finger_winner_content2);
			} else if (fingerInfo.winuid.intValue() == fingerInfo.uid1.intValue()) {
				if (fingerInfo.ante.intValue() != 0) {
					String monkey = fingerInfo.ante.intValue() + "金币";
					if (fingerInfo.antetype.intValue() == 1) {
						monkey = fingerInfo.ante.intValue() + "银币";
					}
					sessionMsg = String.format(context.getString(R.string.str_finger_winner_content), new String(fingerInfo.nick1), monkey);
				} else {
					sessionMsg = String.format(context.getString(R.string.finger_winner_content1), new String(fingerInfo.nick1),
							fingerInfo.ante.intValue());
				}
			} else {
				if (fingerInfo.ante.intValue() != 0) {
					String monkey = fingerInfo.ante.intValue() + "金币";
					if (fingerInfo.antetype.intValue() == 1) {
						monkey = fingerInfo.ante.intValue() + "银币";
					}
					sessionMsg = String.format(context.getString(R.string.str_finger_winner_content), new String(fingerInfo.nick2), monkey);
				} else {
					sessionMsg = String.format(context.getString(R.string.finger_winner_content1), new String(fingerInfo.nick2),
							fingerInfo.ante.intValue());
				}
			}
		} else {
			String str = "";
			if (fingerInfo.ante.intValue() != 0) {
				String monkey = fingerInfo.ante.intValue() + "金币";
				if (fingerInfo.antetype.intValue() == 1) {
					monkey = fingerInfo.ante.intValue() + "银币";
				}
				str = String.format(context.getString(R.string.str_finger_ante), monkey);
			}
			sessionMsg = String.format(context.getString(R.string.finger_left_content1), str, "");
		}
		return sessionMsg;

	}

	public synchronized static long saveFingerGuessInfoMsg(Context context, int type, int sex, String nick, ChatDatabaseEntity chatDatabaseEntity,
			FingerGuessingInfo fingerInfo) {

		if (chatDatabaseEntity == null) {
			return -1;
		}
		if (fingerInfo == null) {
			return -1;
		}
		String message = ChatMessageBiz.saveFingerMessage(fingerInfo);
		chatDatabaseEntity.setMessage(message);
		if (fingerInfo.winuid.intValue() >= 0) {
			chatDatabaseEntity.setCreateTime(fingerInfo.playtime2.longValue() * 1000);
		} else {
			chatDatabaseEntity.setCreateTime(fingerInfo.createtime.longValue() * 1000);
		}

		saveSessionMessage(context, type, chatDatabaseEntity, getFingerSessionMsg(context, fingerInfo), nick, sex);
		return insertSql(context, type, chatDatabaseEntity);

	}

	public synchronized static long saveMsg(Context context, int type, byte[] data, int length, int sex, String nick,
			ChatDatabaseEntity chatDatabaseEntity) {
		if (data == null) {
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}
		MessageType chatType = MessageType.getMessage(chatDatabaseEntity.getType());
		if (chatType == null) {
			return -1;
		}
		switch (chatType) {
		case TEXT:
		case GOGIRL_DARE_PASS:
			return saveTextMsg(context, type, data, sex, nick, chatDatabaseEntity);
		case GOGIRL_DATA_TYPE_SMILE://特殊表情
			return saveHaremFaseMsg(context, type, data, sex, nick, chatDatabaseEntity);
		case IMAGE:
		case BURN_IMAGE:
			return saveImageMsg(context, type, data, sex, nick, chatDatabaseEntity, false);
		case BURN_IMAGE_KEY:
		case IMAGE_KEY:
			return saveImageMsg(context, type, data, sex, nick, chatDatabaseEntity, true);
		case VOICE:
		case BURN_VOICE:
			return saveVoiceMsg(context, type, data, length, sex, nick, chatDatabaseEntity, false);
		case BURN_VOICE_KEY:
		case VOICE_KEY:
			return saveVoiceMsg(context, type, data, length, sex, nick, chatDatabaseEntity, true);
		case VIDEO:
			return saveVideoMsg(context, type, data, sex, nick, chatDatabaseEntity);
		default:
			return saveTextMsg(context, type, data, sex, nick, chatDatabaseEntity);
		}
	}

	/**
	 * 匿名 私聊消息处理
	 * @author Aaron
	 *
	 * @param context
	 * @param type
	 * @param data
	 * @param length
	 * @param sex
	 * @param nick
	 * @param chatDatabaseEntity
	 * @return
	 */
	public synchronized static long saveMsgV2(Context context, int type, byte[] data, int length, int sex, String nick,
			ChatDatabaseEntity chatDatabaseEntity, ReqGoGirlTransChatData req) {
		if (data == null) {
			return -1;
		}
		if (chatDatabaseEntity == null) {
			return -1;
		}
		MessageType chatType = MessageType.getMessage(chatDatabaseEntity.getType());
		if (chatType == null) {
			return -1;
		}
		switch (chatType) {
		case GOGIRL_DATA_TYPE_ANONYM_TEXT:
			return saveTextMsgV2(context, type, data, sex, nick, chatDatabaseEntity, req);
		case GOGIRL_DATA_TYPE_ANONYM_SMILE://特殊表情
			return saveHaremFaseMsgV2(context, type, data, sex, nick, chatDatabaseEntity);
		case GOGIRL_DATA_TYPE_ANONYM_PIC:
		case GOGIRL_DATA_TYPE_TRANSITORY_ANONYM_PIC:
			return saveImageMsgV2(context, type, data, sex, nick, chatDatabaseEntity, false);
		case GOGIRL_DATA_TYPE_ANONYM_VOICEKEY:
		case DATA_TYPE_TRANSITORY_ANONYM_VOICEKEY:
			return saveVoiceMsgV2(context, type, data, length, sex, nick, chatDatabaseEntity, true);
		case GOGIRL_DATA_TYPE_ANONYM_VOICE:
		case GOGIRL_DATA_TYPE_TRANSITORY_ANONYM_VOICE:
			return saveVoiceMsgV2(context, type, data, length, sex, nick, chatDatabaseEntity, false);
		case GOGIRL_DATA_TYPE_ANONYM_PICKEY:
		case GOGIRL_DATA_TYPE_TRANSITORY_ANONYM_PICKEY:
			return saveImageMsgV2(context, type, data, sex, nick, chatDatabaseEntity, true);
		case GOGIRL_DATA_TYPE_ANONYM_VEDIO:
			return saveVideoMsgV2(context, type, data, sex, nick, chatDatabaseEntity);
		default:
			return saveTextMsgV2(context, type, data, sex, nick, chatDatabaseEntity, req);
		}
	}

	public static boolean isStayCurrentChatView(int type, boolean isGroup, int userUid, ChatDatabaseEntity chatDatabaseEntity) {//判断是否停留在当前的聊天界面
		if (type == BaseChatSendMessage.PRIVATE_CHAT_TYPE && !isGroup && userUid == chatDatabaseEntity.getFromID()) {//是私聊界面
			return true;
		}
		if (type == BaseChatSendMessage.GROUP_CHAT_TYPE && isGroup && userUid == chatDatabaseEntity.getToUid()) {//是私聊界面
			return true;
		}
		if (type == BaseChatSendMessage.PRIVATE_CHAT_ANONYM_TYPE && !isGroup) {
			return true;
		}
		return false;
	}

	public static void notifyChatViewUpdate(Context context, boolean isGroupChat, int userUid, IPersistListener persistListener,
			ChatDatabaseEntity chatDatabaseEntity, int type) {
		int fromId = chatDatabaseEntity.getFromID();
		if (type == BaseChatSendMessage.GROUP_CHAT_TYPE) {
			fromId = chatDatabaseEntity.getToUid();
		}
		if (persistListener != null && isStayCurrentChatView(type, isGroupChat, userUid, chatDatabaseEntity)) {
			persistListener.decodeMsg(chatDatabaseEntity, type);
		} else {//增加未读消息
			ChatRecordBiz.saveNoticationMessage(context, fromId, type);
		}
	}

	private synchronized static long saveTextMsg(Context context, int type, byte[] data, int sex, String nick, ChatDatabaseEntity chatDatabaseEntity) {//存储文本信息
		String message = new String(data);
		//判断是否有@我的
		if (message.contains("@" + new String(BAApplication.mLocalUserInfo.nick))) {
			SharedPreferencesTools.getInstance(context).saveBooleanKeyValue(true,
					"@" + BAApplication.mLocalUserInfo.uid + "_" + chatDatabaseEntity.getToUid());
		}
		chatDatabaseEntity.setMessage(message);
		saveSessionMessage(context, type, chatDatabaseEntity, message, nick, sex);
		return insertSql(context, type, chatDatabaseEntity);
	}

	/**
	 *   匿名 消息存储
	 * @author Aaron
	 *
	 * @param context
	 * @param type
	 * @param data
	 * @param sex
	 * @param nick
	 * @param chatDatabaseEntity
	 * @return
	 */
	private synchronized static long saveTextMsgV2(Context context, int type, byte[] data, int sex, String nick,
			ChatDatabaseEntity chatDatabaseEntity, ReqGoGirlTransChatData req) {//存储文本信息
		String message = new String(data);
		chatDatabaseEntity.setMessage(message);
		saveSessionMessage(context, type, chatDatabaseEntity, message, nick, sex);
		chatDatabaseEntity.setRevStr3("1");
		return insertSql(context, type, chatDatabaseEntity);
	}

	private synchronized static long saveHaremFaseMsg(Context context, int type, byte[] data, int sex, String nick,
			ChatDatabaseEntity chatDatabaseEntity) {//存储文本信息
		String message = new String(data);
		chatDatabaseEntity.setMessage(message);
		String sessionMessage = message;
		String strEmotion = HaremEmotionUtil.haremFaceMaps.get(message);
		if (!TextUtils.isEmpty(strEmotion)) {
			sessionMessage = strEmotion;
		}
		saveSessionMessage(context, type, chatDatabaseEntity, sessionMessage, nick, sex);
		return insertSql(context, type, chatDatabaseEntity);
	}

	private synchronized static long saveHaremFaseMsgV2(Context context, int type, byte[] data, int sex, String nick,
			ChatDatabaseEntity chatDatabaseEntity) {//存储文本信息
		String message = new String(data);
		chatDatabaseEntity.setMessage(message);
		String sessionMessage = message;
		String strEmotion = HaremEmotionUtil.haremFaceMaps.get(message);
		if (!TextUtils.isEmpty(strEmotion)) {
			sessionMessage = strEmotion;
		}
		saveSessionMessage(context, type, chatDatabaseEntity, sessionMessage, nick, sex);
		chatDatabaseEntity.setRevStr3("1");
		return insertSql(context, type, chatDatabaseEntity);
	}

	private synchronized static long saveVideoMsg(Context context, int type, byte[] data, int sex, String nick, ChatDatabaseEntity chatDatabaseEntity) {//存储文本信息
		String message = new String(data);
		chatDatabaseEntity.setMessage(message);
		saveSessionMessage(context, type, chatDatabaseEntity, "[视频]", nick, sex);
		return insertSql(context, type, chatDatabaseEntity);
	}

	private synchronized static long saveVideoMsgV2(Context context, int type, byte[] data, int sex, String nick,
			ChatDatabaseEntity chatDatabaseEntity) {//存储文本信息
		String message = new String(data);
		chatDatabaseEntity.setMessage(message);
		saveSessionMessage(context, type, chatDatabaseEntity, "[视频]", nick, sex);
		chatDatabaseEntity.setRevStr3("1");
		return insertSql(context, type, chatDatabaseEntity);
	}

	private synchronized static long saveImageMsg(Context context, int type, byte[] data, int sex, String nick,
			ChatDatabaseEntity chatDatabaseEntity, boolean isOffLine) {//存储图片信息
		String message = ChatMessageBiz.saveImageMessage(data.length, "");
		chatDatabaseEntity.setMessage(message);
		saveSessionMessage(context, type, chatDatabaseEntity, "[图片]", nick, sex);

		ChatOperate chatDatabase = getChatOperate(context, type, chatDatabaseEntity);
		if (chatDatabase != null) {
			if (chatDatabase.selectDataByTime(chatDatabaseEntity.getCreateTime())) {
				return -1;
			}
			long lastDataId = chatDatabase.insert(chatDatabaseEntity);
			chatDatabaseEntity.setMesLocalID(lastDataId);
			int fromId = chatDatabaseEntity.getFromID();
			if (type == BaseChatSendMessage.GROUP_CHAT_TYPE) {
				fromId = chatDatabaseEntity.getToUid();
			}
			if (isOffLine) {
				JobManager jobManager = BAApplication.getInstance().getJobManager();
				jobManager.addJobInBackground(new DownLoadImageJob(context, new String(data), fromId, lastDataId, sex, type));
			} else {
				if (TextUtils.isEmpty(ChatRecordBiz.saveFile(context, fromId, lastDataId, data, true))) {
					chatDatabase.delete(lastDataId);
				}
			}
			return lastDataId;
		}
		return -1;
	}

	private synchronized static long saveImageMsgV2(Context context, int type, byte[] data, int sex, String nick,
			ChatDatabaseEntity chatDatabaseEntity, boolean isOffLine) {//存储图片信息
		String message = ChatMessageBiz.saveImageMessage(data.length, "");
		chatDatabaseEntity.setMessage(message);
		chatDatabaseEntity.setRevStr3("1");
		saveSessionMessage(context, type, chatDatabaseEntity, "[图片]", nick, sex);

		ChatOperate chatDatabase = getChatOperate(context, type, chatDatabaseEntity);
		if (chatDatabase != null) {
			if (chatDatabase.selectDataByTime(chatDatabaseEntity.getCreateTime())) {
				return -1;
			}
			long lastDataId = chatDatabase.insert(chatDatabaseEntity);
			chatDatabaseEntity.setMesLocalID(lastDataId);
			int fromId = chatDatabaseEntity.getFromID();
			if (type == BaseChatSendMessage.GROUP_CHAT_TYPE) {
				fromId = chatDatabaseEntity.getToUid();
			}
			if (isOffLine) {
				JobManager jobManager = BAApplication.getInstance().getJobManager();
				jobManager.addJobInBackground(new DownLoadImageJob(context, new String(data), fromId, lastDataId, sex, type));
			} else {
				if (TextUtils.isEmpty(ChatRecordBiz.saveFile(context, fromId, lastDataId, data, true))) {
					chatDatabase.delete(lastDataId);
				}
			}
			return lastDataId;
		}
		return -1;
	}

	private synchronized static long saveVoiceMsg(Context context, int type, byte[] data, int length, int sex, String nick,
			ChatDatabaseEntity chatDatabaseEntity, boolean isOffLine) {//存储图片信息
		String message = ChatMessageBiz.saveAudioMessage(data.length, length);
		chatDatabaseEntity.setMessage(message);
		saveSessionMessage(context, type, chatDatabaseEntity, "[语音]", nick, sex);
		ChatOperate chatDatabase = getChatOperate(context, type, chatDatabaseEntity);
		if (chatDatabase != null) {
			if (chatDatabase.selectDataByTime(chatDatabaseEntity.getCreateTime())) {
				return -1;
			}
			long lastDataId = chatDatabase.insert(chatDatabaseEntity);
			chatDatabaseEntity.setMesLocalID(lastDataId);
			int fromId = chatDatabaseEntity.getFromID();
			if (type == BaseChatSendMessage.GROUP_CHAT_TYPE) {
				fromId = chatDatabaseEntity.getToUid();
			}
			if (isOffLine) {
				new PeiPeiGetVoiceBiz().getVoiceByKey(data, lastDataId, fromId);
			} else {
				if (TextUtils.isEmpty(ChatRecordBiz.saveFile(context, fromId, lastDataId, data, false))) {
					chatDatabase.delete(lastDataId);
				}
			}
			return lastDataId;
		}
		return -1;
	}

	private synchronized static long saveVoiceMsgV2(Context context, int type, byte[] data, int length, int sex, String nick,
			ChatDatabaseEntity chatDatabaseEntity, boolean isOffLine) {//存储图片信息
		String message = ChatMessageBiz.saveAudioMessage(data.length, length);
		chatDatabaseEntity.setMessage(message);
		saveSessionMessage(context, type, chatDatabaseEntity, "[语音]", nick, sex);
		ChatOperate chatDatabase = getChatOperate(context, type, chatDatabaseEntity);
		if (chatDatabase != null) {
			if (chatDatabase.selectDataByTime(chatDatabaseEntity.getCreateTime())) {
				return -1;
			}
			chatDatabaseEntity.setRevStr3("1");
			long lastDataId = chatDatabase.insert(chatDatabaseEntity);
			chatDatabaseEntity.setMesLocalID(lastDataId);
			int fromId = chatDatabaseEntity.getFromID();
			if (type == BaseChatSendMessage.GROUP_CHAT_TYPE) {
				fromId = chatDatabaseEntity.getToUid();
			}
			if (isOffLine) {
				new PeiPeiGetVoiceBiz().getVoiceByKey(data, lastDataId, fromId);
			} else {
				if (TextUtils.isEmpty(ChatRecordBiz.saveFile(context, fromId, lastDataId, data, false))) {
					chatDatabase.delete(lastDataId);
				}
			}
			return lastDataId;
		}
		return -1;
	}

	private static ChatOperate getChatOperate(Context context, int type, ChatDatabaseEntity chatDatabaseEntity) {
		ChatOperate chatDatabase = null;

		if (type == 0 || type == 3) {
			chatDatabase = ChatOperate.getInstance(context, chatDatabaseEntity.getFromID(), false);
		} else if (type == 1) {
			chatDatabase = ChatOperate.getInstance(context, chatDatabaseEntity.getToUid(), true);
		}
		return chatDatabase;
	}

	public static void saveSessionMessage(Context context, int type, ChatDatabaseEntity chatDatabaseEntity, String sessionMsg, String nick, int sex) {
		int fromId = chatDatabaseEntity.getFromID();
		if (type == BaseChatSendMessage.GROUP_CHAT_TYPE) {
			fromId = chatDatabaseEntity.getToUid();
		}
		boolean isHaveSession = ChatSessionManageBiz.isHaveSession(context, fromId, type);
		if (isHaveSession) {//存在就更新最后一条数据
			ChatSessionManageBiz.upDataSession(context, sessionMsg, nick, fromId, chatDatabaseEntity.getCreateTime(), type);
		} else {//不存在就插入一条新的数据
			ChatManageBiz.haveNotSession(context, fromId, nick, sex, type, sessionMsg);
		}
	}
}
