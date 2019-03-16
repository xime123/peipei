package com.tshang.peipei.model.biz.chat;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ibm.asn1.ASN1Exception;
import com.ibm.asn1.BERDecoder;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.reward.RewardListActivity;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatDes;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.UserSharePreference;
import com.tshang.peipei.model.ISentMessageCallBack;
import com.tshang.peipei.model.ReceiverChatData;
import com.tshang.peipei.model.bizcallback.BizCallBackPersist;
import com.tshang.peipei.model.bizcallback.BizCallBackSentChatMessage;
import com.tshang.peipei.model.entity.ChatAlbumEntity;
import com.tshang.peipei.model.entity.ChatMessageReceiptEntity;
import com.tshang.peipei.model.entity.ShowChatEntity;
import com.tshang.peipei.model.event.ChatEvent;
import com.tshang.peipei.model.event.MainEvent;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestHeartBeat;
import com.tshang.peipei.model.request.RequestPersist;
import com.tshang.peipei.model.request.RequestPersistResponse;
import com.tshang.peipei.model.request.RequestSentMessage;
import com.tshang.peipei.model.request.RequestSentMessageV2;
import com.tshang.peipei.protocol.Gogirl.GoGirlChatDataP;
import com.tshang.peipei.protocol.Gogirl.GoGirlDataInfoP;
import com.tshang.peipei.protocol.asn.gogirl.ApplyJoinGroupInfo;
import com.tshang.peipei.protocol.asn.gogirl.AwardInteractiveInfo;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.DareGameInfo;
import com.tshang.peipei.protocol.asn.gogirl.DareInfo;
import com.tshang.peipei.protocol.asn.gogirl.FingerGuessingInfo;
import com.tshang.peipei.protocol.asn.gogirl.GGSkillInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.LoginRewardInfoList;
import com.tshang.peipei.protocol.asn.gogirl.MemberInOutInfo;
import com.tshang.peipei.protocol.asn.gogirl.NewFansInfo;
import com.tshang.peipei.protocol.asn.gogirl.NewReceiveGiftPushInfo;
import com.tshang.peipei.protocol.asn.gogirl.NewVisitorInfo;
import com.tshang.peipei.protocol.asn.gogirl.NotifySndAwardInfo;
import com.tshang.peipei.protocol.asn.gogirl.NotifySndAwardInfoV2;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqGoGirlHeartBeat;
import com.tshang.peipei.protocol.asn.gogirl.ReqGoGirlTransChatData;
import com.tshang.peipei.protocol.asn.gogirl.ReqGoGirlTransOtherData;
import com.tshang.peipei.protocol.asn.gogirl.RspGoGirlConnSvrV2;
import com.tshang.peipei.protocol.asn.gogirl.ShowRoomLatestStatus;
import com.tshang.peipei.protocol.asn.gogirl.ShowRoomRoleChangeInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillDealInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillInteractiveInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillResultInfo;
import com.tshang.peipei.protocol.asn.gogirl.SystemNotifyInfo;
import com.tshang.peipei.protocol.asn.gogirl.TruthAnswer;
import com.tshang.peipei.protocol.asn.gogirl.TruthInfo;
import com.tshang.peipei.protocol.protobuf.ByteString;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.entity.SessionDatabaseEntity;
import com.tshang.peipei.storage.database.operate.ChatOperate;
import com.tshang.peipei.storage.database.operate.PeipeiSessionOperate;
import com.tshang.peipei.storage.database.operate.ShowsOperate;
import com.tshang.peipei.vender.micode.soundrecorder.Recorder;

import de.greenrobot.event.EventBus;

/**
 * @Title: 聊天消息管理类
 *
 * @Description: 处理长链接获取到的聊天信息，进行数据解析，并刷新界面。发送聊天消息的入口方法可能也放在这
 *
 * @author allen  
 *
 * @date 2014-3-31 下午2:18:12 
 *
 * @version V1.0   
 */
public class ChatManageBiz implements ISentMessageCallBack, BizCallBackPersist {

	private BizCallBackSentChatMessage sentChatMessageCallBack;

	private static ChatManageBiz mChatManageBiz;
	private IPersistListener persistListener;
	private Context context;

	private ChatManageBiz(Context context) {
		this.context = context;
	}

	public static ChatManageBiz getInManage(Context context) {
		if (mChatManageBiz == null) {
			synchronized (ChatManageBiz.class) {
				if (mChatManageBiz == null) {
					mChatManageBiz = new ChatManageBiz(context);
				}
			}
		}
		return mChatManageBiz;
	}

	/**
	 * 解析长链接收到的聊天内容，解析成可用数据和xml
	 */
	public synchronized void receiveMessage(ReqGoGirlTransChatData req, int seq) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		GoGirlDataInfoList list = req.chatdata.chatdatalist;
		GoGirlDataInfo info = (GoGirlDataInfo) list.get(0);
		setPersistFeed(BAApplication.mLocalUserInfo.auth, 0, seq);//长连接回包
		long isSaveSuccess = -1;
		GoGirlDataInfo dataInfo = ReceiverChatData.getGoGirlDataInfo(req);
		if (dataInfo != null) {
			int dataInfoType = dataInfo.type.intValue();
			//			System.out.println("dtan==========="+dataInfoType);
			MessageType mType = MessageType.getMessage(dataInfoType);
			ChatDatabaseEntity entity = null;
			if (mType != MessageType.BROADCAST_MESSAGE && mType != MessageType.RECEIPT && mType != MessageType.GOGIRL_DATA_TYPE_TOPIC_FEED
					&& mType != MessageType.GOGIRL_DATA_TYPE_FANS) {
				entity = ReceiverChatData.getChatDatabaseEntity(req, dataInfo);
			}
			int type = req.chatdata.totype.intValue();
			int sex = req.chatdata.fromsex.intValue();
			String nick = new String(req.chatdata.fromnick);
			if (type == BaseChatSendMessage.GROUP_CHAT_TYPE) {//后宫的名字
				nick = new String(req.chatdata.tonick);

				//处理用户聊天背景
				String key = "Group_" + String.valueOf(req.chatdata.to) + "#" + String.valueOf(req.chatdata.from);
				int value = req.chatdata.revint3.intValue();
				//				int saveValue=SharedPreferencesTools.getInstance(context).getIntValueByKey(key, -1);
				//				if (saveValue==-1) {
				SharedPreferencesTools.getInstance(context).saveIntKeyValue(value, key);
				//				}
			}
			if (type == BaseChatSendMessage.SHOW_CHAT_TYPE) {//秀场消息
				if (mType != null && BAApplication.showRoomInfo != null) {
					disShowChatMessage(req, dataInfo, mType);
				}
			} else {
				if (mType != null) {
					disChatMessage(req, isSaveSuccess, dataInfo, mType, entity, type, sex, nick);
				}
			}
		}
	}

	private void disChatMessage(ReqGoGirlTransChatData req, long isSaveSuccess, GoGirlDataInfo dataInfo, MessageType mType,
			ChatDatabaseEntity entity, int type, int sex, String nick) {
		switch (mType) {
		case RECEIPT://阅后即焚回执
		case GOGIRL_DATA_TYPE_ANONYM_RECEIPT:
			mChatManageBiz.changeMessageStatusBySerID(context, req.chatdata.from.intValue(), ChatStatus.READED_BURN.getValue(), new String(
					dataInfo.dataid), false);

			ChatEvent chatEvent = new ChatEvent();
			chatEvent.setBurnId(new String(dataInfo.dataid));
			EventBus.getDefault().postSticky(chatEvent);
			break;
		case PRIVATE_ALBUM://私密相册处理
			ChatAlbumEntity chatAlbumEntity = ReceiverChatData.getChatAlbumEntity(req, dataInfo);
			isSaveSuccess = SaveChatData.saveChatAlbumEntityMsg(context, type, sex, nick, entity, chatAlbumEntity);
			break;
		case NEW_GIFT://收到礼物解析
			GiftDealInfoList giftDealInfoList = ReceiverChatData.getGiftDealInfoList(context, req, dataInfo).giftdeallist;
			isSaveSuccess = SaveChatData.saveGiftDealInfoListMsg(context, type, sex, nick, entity, giftDealInfoList);
			GiftDealInfo info = (GiftDealInfo) giftDealInfoList.get(0);
			if (info != null) {
				if (info.gift.id.intValue() >= 1000) {
					isSaveSuccess = 0;
				}
			}
			break;
		case GOGIRL_DATA_TYPE_NEW_RECEIVE_ANONYM_GIFT_PUSH_DATA://匿名私聊实物
			GiftDealInfoList giftDealInfoList2 = ReceiverChatData.getGiftDealInfoList(context, req, dataInfo).giftdeallist;
			isSaveSuccess = SaveChatData.saveGiftDealInfoListMsgV2(context, type, sex, nick, entity, giftDealInfoList2, req);
			GiftDealInfo info2 = (GiftDealInfo) giftDealInfoList2.get(0);
			if (info2 != null) {
				if (info2.gift.id.intValue() >= 1000) {
					isSaveSuccess = 0;
				}
			}
			break;
		case BROADCAST_MESSAGE://收到了广播数据
			ReceiverChatData.operateBraodCastInfo(context, dataInfo, broadcastListener);
			break;
		case FINGER://猜拳数据
		case WITHANTEFINGER://私聊会推送猜拳结果过来，群聊就不会的
		case NEWFINGER://新的猜拳，可选银币
			FingerGuessingInfo guessingInfo = ReceiverChatData.getFingerGuessingInfo(dataInfo);
			isSaveSuccess = SaveChatData.saveFingerGuessInfoMsg(context, type, sex, nick, entity, guessingInfo);
			break;
		case JOINHAREM://收到申请加入后宫消息
			ApplyJoinGroupInfo applyJoinGroupInfo = ReceiverChatData.getApplyJoinGroupInfo(dataInfo);
			isSaveSuccess = SaveChatData.saveJoinHaremMsg(context, type, sex, nick, entity, applyJoinGroupInfo);
			break;
		case AGREEJOINHAREM://同意加入后宫
			MemberInOutInfo memberInOutInfo = ReceiverChatData.getMemberInOutInfo(dataInfo);
			isSaveSuccess = SaveChatData.saveAgreeJoinHaremMsg(context, type, sex, nick, entity, memberInOutInfo);
			break;
		case TEXT:
		case IMAGE:
		case IMAGE_KEY:
		case VOICE:
		case VOICE_KEY:
		case VIDEO:
		case BURN_IMAGE:
		case BURN_IMAGE_KEY:
		case BURN_VOICE:
		case BURN_VOICE_KEY:
		case GOGIRL_DATA_TYPE_SMILE:
			isSaveSuccess = SaveChatData.saveMsg(context, type, dataInfo.data, dataInfo.datainfo.intValue(), sex, nick, entity);
			break;
		case SYSTEMNOTIFYINFO://系统通知
			SystemNotifyInfo systemInfo = ReceiverChatData.getSystemNotifyInfo(dataInfo);
			isSaveSuccess = SaveChatData.saveSystemNotifyInfoMsg(context, type, sex, nick, entity, systemInfo);
			break;
		case CREATEREDPACKET:
		case UNPACKETREDPACKET:
			RedPacketInfo redPacketInfo = ReceiverChatData.getRedPacketInfo(dataInfo);
			isSaveSuccess = SaveChatData.saveRedPacketInfoMsg(context, type, sex, nick, entity, redPacketInfo);
			break;
		case SKILLDEALINFO://技能下单信息
			SkillDealInfo skillDealInfo = ReceiverChatData.getSkillDealInfo(dataInfo);
			isSaveSuccess = SaveChatData.saveSkillDealInfoMsg(context, type, sex, nick, entity, skillDealInfo);
			break;
		case GGSKILLINFO:
			GGSkillInfo ggSkillInfo = ReceiverChatData.getGGSkillInfo(dataInfo);
			isSaveSuccess = SaveChatData.saveGGSkillInfoMsg(context, type, sex, nick, entity, ggSkillInfo);
			break;
		case GOGIRL_DATA_TYPE_TOPIC_FEED:
			ReceiverChatData.saveTopicFeedInfo(context, dataInfo);
			break;
		case GOGIRL_DATA_TYPE_FANS:
			ReceiverChatData.setFansNum(context);
			break;
		case GOGIRL_DICE_TYPE://骰子
			DareGameInfo dareInfo = ReceiverChatData.getStartDareInfo(dataInfo);
			isSaveSuccess = SaveChatData.saveStartDareInfoMsg(context, type, sex, nick, entity, dareInfo);
			break;
		case GOGIRL_TEXT_IMAGE:
			DareInfo dinfo = ReceiverChatData.getDareInfo(dataInfo);
			isSaveSuccess = SaveChatData.saveDareInfoMsg(context, type, sex, nick, entity, dinfo);
			break;
		case GOGIRL_DICE_FINISH://大冒险结束
			DareInfo finish = ReceiverChatData.getDareInfo(dataInfo);
			isSaveSuccess = SaveChatData.saveFinishDareInfoMsg(context, type, sex, nick, entity, finish);
			break;
		case GOGIRL_ADVENTURE_DIALOG:
			DareInfo dialogInfo = ReceiverChatData.getDareInfo(dataInfo);
			isSaveSuccess = SaveChatData.saveDialogDareInfoMsg(context, type, sex, nick, entity, dialogInfo);
			break;
		case GOGIRL_DARE_PASS:
			DareInfo passinfo = ReceiverChatData.getDareInfo(dataInfo);
			isSaveSuccess = SaveChatData.saveMsg(context, type, passinfo.memo, dataInfo.datainfo.intValue(), sex, nick, entity);
			break;
		case GOGIRL_ADVENTURE_GIFT://大冒险礼物
			DareInfo giftinfo = ReceiverChatData.getDareInfo(dataInfo);
			if (persistListener != null) {
				entity.setMessage(new String(giftinfo.memo));
				persistListener.decodeMsg(entity, type);
			}
			break;
		case GOGIRL_TRUTH://真心话信息
			TruthInfo truthInfo = ReceiverChatData.getTruthInfo(dataInfo);
			isSaveSuccess = SaveChatData.saveTruthInfo(context, type, sex, nick, req, entity, truthInfo);
			break;
		case GOGIRL_DATA_TYPE_TRUTH_ANSWER://真心话答案
			TruthAnswer answer = ReceiverChatData.getTruthAnswer(dataInfo);
			isSaveSuccess = SaveChatData.saveAnswerInfo(context, type, sex, nick, req, entity, answer);
			break;
		case GOGIRL_DATA_TYPE_DYNAMICS_FEED://动态点赞回复
			ReceiverChatData.saveDynamicReply(context, dataInfo);
			break;
		case GOGIRL_DATA_TYPE_PARTICIPATE_AWARD://悬赏私聊
			AwardInteractiveInfo awardinfo = ReceiverChatData.getAwardChatInfo(dataInfo);
			isSaveSuccess = SaveChatData.saveAwardInfo(context, type, sex, nick, req, entity, awardinfo);
			break;
		case GOGIRL_DATA_TYPE_NOTIFY_SNDAWARD_INFO://悬赏结束
			NotifySndAwardInfo sndAwardInfo = ReceiverChatData.getSndAwardInfo(dataInfo);
			SaveChatData.updateAwardInfo(context, sndAwardInfo, isGroupChat);
			break;
		case GOGIRL_DATA_TYPE_NOTIFY_SNDAWARD_INFOV2://悬赏结束
			NotifySndAwardInfoV2 sndAwardInfoV2 = ReceiverChatData.getSndWardInfo2(dataInfo);
			SaveChatData.updateAwardInfo(context, sndAwardInfoV2, isGroupChat, req.chatdata.totype.intValue(), req.chatdata);
			break;
		case GOGIRL_DATA_TYPE_SKILL_INVITED: //女神技私聊
			SkillInteractiveInfo skillInfo = ReceiverChatData.getGoddessSkillChatInfo(dataInfo);
			isSaveSuccess = SaveChatData.saveGoddessSkillInfo(context, type, sex, nick, req, entity, skillInfo);
			break;
		case GOGIRL_DATA_TYPE_SKILL_INVITED_RESULT: //女神技邀请返回
			SkillResultInfo skillResultInfo = ReceiverChatData.getGoddessSkillInfo(dataInfo);
			SaveChatData.updateGoddessSkillInfo(context, entity.getTOID(), skillResultInfo, isGroupChat);
			break;
		case GOGIRL_DATA_TYPE_HAREM_SOLITAIRE_RED_PACKET: //后宫红包接龙
			RedPacketBetCreateInfo solitaireRedPacket = ReceiverChatData.getSolitaireRedpacketInfo(dataInfo);
			isSaveSuccess = SaveChatData.saveSolitaireRedPacketInfo(context, type, sex, nick, entity, solitaireRedPacket);
			//更新可以抢的红包消息
			SaveChatData.updateCanGrapSolitaireInfo(context, type, sex, nick, entity, solitaireRedPacket);
			break;
		case GOGIRL_DATA_TYPE_HAREM_GRAP_SOLITAIRE_RED_PACKET: //后宫抢接龙红包
			RedPacketBetCreateInfo grapSolitaireRedPacket = ReceiverChatData.getSolitaireRedpacketInfo(dataInfo);
			isSaveSuccess = SaveChatData.saveSolitaireRedPacketInfo(context, type, sex, nick, entity, grapSolitaireRedPacket);
			//更新可以抢的红包消息
			SaveChatData.updateCanGrapSolitaireInfo(context, type, sex, nick, entity, grapSolitaireRedPacket);
			break;
		case GOGIRL_DATA_TYPE_RED_PACKET_BET_TIMEOUT:
			RedPacketBetCreateInfo timeSolitaireRedPacket = ReceiverChatData.getSolitaireRedpacketInfo(dataInfo);
			isSaveSuccess = SaveChatData.saveSolitaireRedPacketInfo(context, type, sex, nick, entity, timeSolitaireRedPacket);
			//更新超时的红包消息
			SaveChatData.updateCanGrapSolitaireInfo(context, type, sex, nick, entity, timeSolitaireRedPacket);
			break;
		case GOGIRL_DATA_TYPE_ANONYM_TEXT:
		case GOGIRL_DATA_TYPE_ANONYM_PIC:
		case GOGIRL_DATA_TYPE_ANONYM_PICKEY:
		case GOGIRL_DATA_TYPE_ANONYM_VOICE:
		case GOGIRL_DATA_TYPE_ANONYM_VOICEKEY:
		case GOGIRL_DATA_TYPE_TRANSITORY_ANONYM_PIC:
		case GOGIRL_DATA_TYPE_TRANSITORY_ANONYM_PICKEY:
		case GOGIRL_DATA_TYPE_TRANSITORY_ANONYM_VOICE:
		case DATA_TYPE_TRANSITORY_ANONYM_VOICEKEY:
		case GOGIRL_DATA_TYPE_ANONYM_SMILE:
		case GOGIRL_DATA_TYPE_ANONYM_VEDIO:
			isSaveSuccess = SaveChatData.saveMsgV2(context, type, dataInfo.data, dataInfo.datainfo.intValue(), sex, nick, entity, req);
			break;
		default:
			if (!TextUtils.isEmpty(new String(req.chatdata.fromnick)) && !TextUtils.isEmpty(new String(req.chatdata.tonick))) {
				isSaveSuccess = SaveChatData.saveMsg(context, type, "版本过低，需要升级才能显示此功能".getBytes(), dataInfo.datainfo.intValue(), sex, nick, entity);
			}
			break;
		}
		if (isSaveSuccess > 0) {//说明数据存储成功
			entity.setMesLocalID(isSaveSuccess);//修改下刚刚数据的id
			SaveChatData.notifyChatViewUpdate(context, isGroupChat, chatUserUid, persistListener, entity, req.chatdata.totype.intValue());
		}
	}

	private void disShowChatMessage(ReqGoGirlTransChatData req, GoGirlDataInfo dataInfo, MessageType mType) {
		//		if (req.touid.intValue() != BAApplication.showRoomInfo.getOwneruserinfo().getUid()) {
		//			return;
		//		}
		ShowChatEntity chatEntity = new ShowChatEntity();
		NoticeEvent event = new NoticeEvent();
		switch (mType) {
		case TEXT:
			if (req.chatdata.to.intValue() == BAApplication.showRoomInfo.getRoomid()) {
				chatEntity.type = dataInfo.type.intValue();
				chatEntity.data = new String(dataInfo.data);
				chatEntity.nick = new String(req.chatdata.fromnick);
				chatEntity.uid = req.chatdata.from.intValue();
				chatEntity.sex = req.chatdata.fromsex.intValue();
				chatEntity.receiveName = new String(req.chatdata.tonick);
				event.setFlag(NoticeEvent.NOTICE72);
				event.setObj(chatEntity);
				EventBus.getDefault().post(event);
			}
			break;
		case VOICE:
			if (req.chatdata.to.intValue() == BAApplication.showRoomInfo.getRoomid()) {
				chatEntity.type = 45;
				chatEntity.voiceLength = dataInfo.datainfo.intValue();
				chatEntity.nick = new String(req.chatdata.fromnick);
				chatEntity.uid = req.chatdata.from.intValue();
				chatEntity.sex = req.chatdata.fromsex.intValue();

				String time = req.chatdata.createtimes.intValue() + "" + req.chatdata.createtimeus.intValue();

				File file = new File(SdCardUtils.getInstance().getDirectory(0), time);
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}

				BaseFile.saveByteToFile(dataInfo.data, file.getAbsolutePath());
				chatEntity.voiceFile = file.getAbsolutePath();

				BAApplication.showVoiceList.add(chatEntity);
				BAApplication.showTempList.add(chatEntity);

				ShowsOperate operate = ShowsOperate.getInstance(context);
				String[] str = chatEntity.voiceFile.split("/");
				if (!operate.isHaveSession(str[str.length - 1], chatEntity.type)) {
					operate.insert(str[str.length - 1], chatEntity.type, 0);
				}
				if (BAApplication.isShowRomm) {
					event.setFlag(NoticeEvent.NOTICE73);
					event.setObj(chatEntity);
					EventBus.getDefault().post(event);
				} else {
					if (BAApplication.mVoiceRecod.state() != Recorder.RECORDING_STATE) {
						BAApplication.getInstance().playVoice();
					}
				}
			}
			break;
		case GOGIRL_DATA_TYPE_SHOW_GIFT_PUSH:
			NewReceiveGiftPushInfo giftPushInfo = ReceiverChatData.getGiftDealInfoList(context, req, dataInfo);
			chatEntity.series = giftPushInfo.series.intValue();
			if (giftPushInfo.boxnum.intValue() >= 0 && giftPushInfo.deliveruid.intValue() == BAApplication.mLocalUserInfo.uid.intValue()) {
				BAApplication.showBoxNum = giftPushInfo.boxnum.intValue();
				BAApplication.isShowBox = true;
				NoticeEvent event2 = new NoticeEvent();
				event2.setFlag(NoticeEvent.NOTICE82);
				EventBus.getDefault().post(event2);
			}
			GiftDealInfoList giftDealInfoList = giftPushInfo.giftdeallist;
			if (!giftDealInfoList.isEmpty()) {
				GiftDealInfo dealInfo = (GiftDealInfo) giftDealInfoList.get(0);//礼物只有一个
				if (dealInfo.to.intValue() == BAApplication.showRoomInfo.getOwneruserinfo().getUid()) {
					GiftInfo info = dealInfo.gift;
					chatEntity.type = 48;
					chatEntity.giftId = info.id.intValue();
					chatEntity.giftKey = new String(info.pickey);
					chatEntity.giftName = new String(info.name);
					chatEntity.giftNum = dealInfo.giftnum.intValue();
					chatEntity.uid = dealInfo.from.intValue();
					chatEntity.nick = new String(dealInfo.fromnick);
					chatEntity.sex = dealInfo.fromsex.intValue();
					event.setFlag(NoticeEvent.NOTICE72);
					event.setObj(chatEntity);
					EventBus.getDefault().post(event);
				}
			}
			break;
		case GOGIRL_DATA_TYPE_SHOW_ROOM_ROLE://角色变化
			ShowRoomRoleChangeInfo roleInfo = new ShowRoomRoleChangeInfo();
			BERDecoder dec = new BERDecoder(dataInfo.data);

			try {
				roleInfo.decode(dec);

				if (roleInfo.owneruid.intValue() == BAApplication.showRoomInfo.getOwneruserinfo().getUid()) {
					event.setFlag(NoticeEvent.NOTICE74);
					event.setNum(roleInfo.torole.intValue());
					event.setNum2(roleInfo.uid.intValue());
					event.setObj(roleInfo);
					EventBus.getDefault().post(event);
				}
			} catch (ASN1Exception e) {
				e.printStackTrace();
			}
			break;
		case GOGIRL_DATA_TYPE_ROOM_INFO:
			ShowRoomLatestStatus lastestStatus = new ShowRoomLatestStatus();

			BERDecoder dec1 = new BERDecoder(dataInfo.data);

			try {
				lastestStatus.decode(dec1);

				if (lastestStatus.owneruid.intValue() == BAApplication.showRoomInfo.getOwneruserinfo().getUid()) {
					event.setFlag(NoticeEvent.NOTICE75);
					event.setObj(lastestStatus);
					EventBus.getDefault().post(event);

					event.setFlag(NoticeEvent.NOTICE77);
					event.setObj(lastestStatus);
					EventBus.getDefault().post(event);
				}
			} catch (ASN1Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	private GoGirlChatDataP setShowChatData(ReqGoGirlTransChatData req, GoGirlDataInfo dataInfo) {
		GoGirlChatDataP.Builder chatdataPBuilder = GoGirlChatDataP.newBuilder();
		chatdataPBuilder.setCreatetimes(req.chatdata.createtimes.intValue());
		chatdataPBuilder.setCreatetimeus(req.chatdata.createtimeus.intValue());
		chatdataPBuilder.setFromnick(ByteString.copyFrom(req.chatdata.fromnick));
		chatdataPBuilder.setFromsex(req.chatdata.fromsex.intValue());
		chatdataPBuilder.setFromtype(req.chatdata.fromtype.intValue());
		chatdataPBuilder.setFrom(req.chatdata.from.intValue());
		chatdataPBuilder.setTo(req.chatdata.to.intValue());
		chatdataPBuilder.setTonick(ByteString.copyFrom(req.chatdata.tonick));
		chatdataPBuilder.setTotype(req.chatdata.totype.intValue());
		chatdataPBuilder.setTosex(req.chatdata.tosex.intValue());

		GoGirlDataInfoP.Builder info = GoGirlDataInfoP.newBuilder();
		info.setData(ByteString.copyFrom(dataInfo.data));
		info.setDataid(ByteString.copyFrom(dataInfo.dataid));
		info.setDatainfo(dataInfo.datainfo.intValue());
		info.setType(dataInfo.type.intValue());

		chatdataPBuilder.addChatdatalist(info);

		GoGirlChatDataP chatdatap = chatdataPBuilder.build();
		return chatdatap;
	}

	/**
	 * 发送消息
	 *
	 * @param data 数据
	 * @param type 类型
	 * @param lenght 如果是语音，语音的长度
	 * @param localId 聊天消息本地id
	 */
	public void sentMsg(byte[] auth, int ver, int uid, byte[] data, int type, int lenght, int friendUid, String burnID, String nick, String fnick,
			int sex, int fsex, BizCallBackSentChatMessage callBack, int msgLocalId, int from) {
		sentChatMessageCallBack = callBack;
		if (from == RewardListActivity.CHAT_FROM_REWARD) {
			RequestSentMessageV2 requestSentMessage = new RequestSentMessageV2();
			requestSentMessage.sentMessage(auth, ver, uid, data, type, lenght, friendUid, burnID, nick, fnick, sex, fsex, msgLocalId, null, this);
		} else {
			RequestSentMessage requestSentMessage = new RequestSentMessage();
			requestSentMessage.sentMessage(auth, ver, uid, data, type, lenght, friendUid, burnID, nick, fnick, sex, fsex, msgLocalId, null, this);
		}
	}

	/**
	 * 获取本地聊天信息
	 *
	 * @param context
	 * @param friendUid
	 * @param start
	 * @param num
	 * @param chatList
	 */
	public List<ChatDatabaseEntity> getChatList(Context context, int friendUid, int start, int num, boolean isGroup) {
		return ChatOperate.getInstance(context, friendUid, isGroup).selectChatList(start, num);
	}

	public void deleteChatList(Context context, int friendUid, boolean isGroup, long localId) {
		ChatOperate.getInstance(context, friendUid, isGroup).deletelimit(localId);
	}

	/**
	 * 删除消息
	 *
	 */
	public static void deleteMessage(Context context, int fuid, String mesSerid, boolean isGroupChat) {
		ChatOperate.getInstance(context, fuid, isGroupChat).deleteByMesSerId(mesSerid);

	}

	/**
	 * 返回聊天总条数
	 */
	public int getNumChatRecords(Context context, int fuid, boolean isGroup) {
		ChatOperate chatOperate = ChatOperate.getInstance(context, fuid, isGroup);
		if (chatOperate != null)
			return chatOperate.getCount();
		else
			return 0;
	}

	/**
	 * 修改状态
	 */
	public void changeMessageStatusBySerID(Context context, int fuid, int status, String mesSerID, boolean isGroupChat) {
		ChatOperate chatOperate = ChatOperate.getInstance(context, fuid, isGroupChat);
		if (chatOperate != null)
			chatOperate.updateStatus(status, mesSerID);

		chatOperate = null;
	}

	/**
	 * 修改状态
	 */
	public void changeMessageStatusByLocalID(Context context, int fuid, int status, long localId, long time, boolean isGroupChat) {
		ChatOperate chatOperate = ChatOperate.getInstance(context, fuid, isGroupChat);
		if (chatOperate != null)
			chatOperate.updataStatusById(status, localId, time);

		chatOperate = null;
	}

	public void changeMessageDesByLocalID(Context context, int fuid, int status, long localId, boolean isGroupChat) {
		ChatOperate chatOperate = ChatOperate.getInstance(context, fuid, isGroupChat);
		if (chatOperate != null)
			chatOperate.updateDesById(status, localId);
		chatOperate = null;
	}

	/**
	 * 超时重发
	 *
	 */
	public void resendTimeoutMessagesWithFriend() {}

	@Override
	public void sentMessageCallBack(byte[] auth, int retCode, ChatMessageReceiptEntity receipt, ChatDatabaseEntity chatDatabaseEntity) {
		if (null != sentChatMessageCallBack) {
			sentChatMessageCallBack.getSentChatMessageCallBack(retCode, receipt);
		}
	}

	@Override
	public void openPersistSer(int retCode, Object obj, int seq) {
		BAApplication.isCreateLongConnectedSuccess = true;
		if (obj instanceof ReqGoGirlTransChatData) {//聊天之类 
			ReqGoGirlTransChatData req = (ReqGoGirlTransChatData) obj;
			receiveMessage(req, seq);
		} else if (obj instanceof ReqGoGirlHeartBeat) {//心跳包
			//			System.out.println("---------心跳回包--------");
			if (BAApplication.mLocalUserInfo != null) {
				setHeartBeat(BAApplication.mLocalUserInfo.auth, retCode, seq);
			}
		} else if (obj instanceof RspGoGirlConnSvrV2) {
			RspGoGirlConnSvrV2 rsp = (RspGoGirlConnSvrV2) obj;
			GoGirlUserInfo info = rsp.userinfo;

			if (BAApplication.mLocalUserInfo != null) {
				BAApplication.mLocalUserInfo.gradeinfo = info.gradeinfo;
				BAApplication.mLocalUserInfo.userstatus = info.userstatus;
				UserSharePreference.getInstance(context).saveUserByKey(BAApplication.mLocalUserInfo);
			}

			if (rsp.retcode.intValue() == BAConstants.rspContMsgType.E_GG_LOGIN) {//这个提醒用户在其他地方登录
				if (BAApplication.mLocalUserInfo != null) {
					NoticeEvent event = new NoticeEvent();
					event.setFlag(NoticeEvent.NOTICE27);
					EventBus.getDefault().post(event);
				}
			} else {
				SharedPreferencesTools.getInstance(context).saveLongKeyValue(System.currentTimeMillis(), BAConstants.PEIPEI_NOTIFICATION_CHAT_TIME);
				BAApplication.isOnLine = true;
				LoginRewardInfoList lists = rsp.loginrewards;
				int firstconn = rsp.isfirstconn.intValue();
				if (firstconn > 0 && BAApplication.mLocalUserInfo != null) {
					BAApplication.loginDay = firstconn;
					BAApplication.lists = lists;
					NoticeEvent event = new NoticeEvent();
					event.setNum(firstconn);
					event.setObj(lists);
					event.setFlag(NoticeEvent.NOTICE50);
					EventBus.getDefault().post(event);
				}
			}
		} else if (obj instanceof ReqGoGirlTransOtherData) {
			ReqGoGirlTransOtherData req = (ReqGoGirlTransOtherData) obj;
			GoGirlDataInfoList list = req.chatdata.chatdatalist;
			GoGirlDataInfo dataInfo = null;
			if (BAApplication.mLocalUserInfo != null) {
				setPersistFeed(BAApplication.mLocalUserInfo.auth, 0, seq);
			}
			if (list != null && list.size() != 0) {
				dataInfo = (GoGirlDataInfo) list.get(0);
			}
			if (dataInfo == null) {
				return;
			}
			if (dataInfo.type.intValue() == MessageType.FOOTPRINT.getValue()) {
				if (BAApplication.mLocalUserInfo != null) {
					SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "").saveBooleanKeyValue(true,
							BAConstants.PEIPEI_INTERESTED_NEW);
				}

				try {
					NewVisitorInfo info = new NewVisitorInfo();
					BERDecoder dec = new BERDecoder(dataInfo.data);

					info.decode(dec);

					NoticeEvent event = new NoticeEvent();
					event.setFlag(NoticeEvent.NOTICE69);
					event.setNum(info.charmnum.intValue());
					EventBus.getDefault().post(event);
				} catch (ASN1Exception e) {
					e.printStackTrace();
				}
				return;
			} else if (dataInfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_TOPIC_FEED.getValue()) {
				ReceiverChatData.saveTopicFeedInfo(context, dataInfo);
			} else if (dataInfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_FANS.getValue()) {
				NewFansInfo faninfo = new NewFansInfo();
				BERDecoder dec = new BERDecoder(dataInfo.data);
				try {
					faninfo.decode(dec);
					String str = SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "").getStringValueByKey(
							BAConstants.PEIPEI_FANS_UNREAD_UID);
					if (TextUtils.isEmpty(str)) {
						str = faninfo.fansuid.intValue() + "";
					} else {
						if (!str.equals(faninfo.fansuid.intValue() + "")) {
							if (!str.startsWith(faninfo.fansuid.intValue() + ",") && !str.contains("," + faninfo.fansuid.intValue() + ","))
								str = str + "," + faninfo.fansuid.intValue();
						}
					}
					SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "").saveStringKeyValue(str,
							BAConstants.PEIPEI_FANS_UNREAD_UID);
				} catch (Exception e) {
					e.printStackTrace();
				}

				ReceiverChatData.setFansNum(context);
			} else if (dataInfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_BEGIN_TRANS_OFFLINE_MSG.getValue()) {//传输离线消息开始
				ChatRecordBiz.currentTimeMillis = System.currentTimeMillis();
				ChatRecordBiz.isOnLineMessageStart = true;
				NoticeEvent noticeEvent = new NoticeEvent();
				noticeEvent.setFlag(NoticeEvent.NOTICE70);
				EventBus.getDefault().post(noticeEvent);
			} else if (dataInfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_END_TRANS_OFFLINE_MSG.getValue()) {//传输离线消息结束
				ChatRecordBiz.isOnLineMessageStart = false;
				NoticeEvent noticeEvent = new NoticeEvent();
				noticeEvent.setFlag(NoticeEvent.NOTICE71);
				EventBus.getDefault().post(noticeEvent);
				//刷新界面
				int num = ChatSessionManageBiz.isExistUnreadMessage(context);
				if (num > 0) {
					NoticeEvent event = new NoticeEvent();
					event.setNum(num);
					event.setFlag(NoticeEvent.NOTICE68);
					EventBus.getDefault().post(event);
				}
			}
		}

	}

	public interface IGetLoginRewardInfo {//登录领取奖励
		public void getLoginRewardInfo(int loginDay, LoginRewardInfoList lists);
	};

	public void setPersistFeed(byte[] auth, int retcode, int seq) {
		RequestPersistResponse req = new RequestPersistResponse();
		req.feedPersist(auth, BAApplication.app_version_code, retcode, seq, RequestPersist.getReqRessist());
	}

	public void setHeartBeat(byte[] auth, int retcode, int seq) {
		RequestHeartBeat req = new RequestHeartBeat();
		req.sentHeartBeat(auth, BAApplication.app_version_code, retcode, seq, RequestPersist.getReqRessist());
	}

	public interface IPersistListener {
		public void decodeMsg(ChatDatabaseEntity chatEntity, int type);
	};

	public interface IGetPushBroadcastListener {//广播数据来了
		public void getPushBroadcastData(BroadcastInfo broadcastInfo, boolean isAddMe);
	};

	private IGetPushBroadcastListener broadcastListener;

	public void setBroadcastListener(IGetPushBroadcastListener broadcastListener) {
		this.broadcastListener = broadcastListener;
	}

	public interface IGetHaremMemberOperaterListener {//群成员变动监听
		public void getHaremMemberOperater(MemberInOutInfo memberInOutInfo);
	};

	private int chatUserUid = 0;
	private boolean isGroupChat = false;

	public void setPersistListener(IPersistListener iplistener, int chatUserUid, boolean isGroupChat) {
		persistListener = iplistener;
		this.chatUserUid = chatUserUid;
		this.isGroupChat = isGroupChat;
	}

	/**
	 * 没有聊天记录
	 * @author allen
	 *
	 * @param context
	 * @param friendUid
	 * @param friendNick
	 * @param sex
	 */
	public static void haveNotSession(Context context, int friendUid, String friendNick, int sex, int type, String sessionContent) {
		SessionDatabaseEntity sessionEntity = new SessionDatabaseEntity();
		sessionEntity.setUserID(friendUid);
		sessionEntity.setLatestUpdateTime(System.currentTimeMillis());
		sessionEntity.setUnreadCount(0);
		sessionEntity.setSex(sex);
		sessionEntity.setType(type);
		sessionEntity.setNick(friendNick);
		sessionEntity.setSessionData(sessionContent);

		ChatSessionManageBiz.addChatSessionWithUserID(context, sessionEntity);
	}

	/**
	 * 刷新聊天记录
	 * @author allen
	 *
	 * @param context
	 * @param friendUid
	 */
	public static int refrushSession(Context context, int friendUid, int type) {
		//增加未读消息
		PeipeiSessionOperate sessionOperate = PeipeiSessionOperate.getInstance(context);
		if (sessionOperate != null)
			sessionOperate.updateUnreadCount(0, friendUid, type);
		sessionOperate = null;

		//刷新界面
		int num = ChatSessionManageBiz.isExistUnreadMessage(context);
		MainEvent mainEvent = new MainEvent();
		mainEvent.setMainStr("message");
		mainEvent.setNum(num);
		EventBus.getDefault().postSticky(mainEvent);
		return num;
	}

	/**
	 * 插入黑名单数据
	 * 
	 * @param context
	 * @param dbfriend 
	 */
	public static ChatDatabaseEntity insertMessage(Context context, int dbfriend, String message, long time, boolean isGroupChat) {
		ChatDatabaseEntity chatDatabaseEntity = new ChatDatabaseEntity();
		chatDatabaseEntity.setStatus(ChatStatus.SUCCESS.getValue());
		chatDatabaseEntity.setDes(ChatDes.TO_FRIEDN.getValue());
		chatDatabaseEntity.setFromID(dbfriend);
		chatDatabaseEntity.setMesSvrID("");
		chatDatabaseEntity.setProgress(0);
		chatDatabaseEntity.setType(MessageType.SYSTEM.getValue());
		chatDatabaseEntity.setCreateTime(time);

		chatDatabaseEntity.setMessage(message);
		ChatOperate chatDatabase = ChatOperate.getInstance(context, dbfriend, isGroupChat);
		chatDatabase.insert(chatDatabaseEntity);
		return chatDatabaseEntity;
	}
}
