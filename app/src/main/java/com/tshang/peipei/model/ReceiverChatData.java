package com.tshang.peipei.model;

import android.content.Context;

import com.ibm.asn1.ASN1Exception;
import com.ibm.asn1.BERDecoder;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatDes;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAParseRspData;
import com.tshang.peipei.base.babase.BAParseRspData.ContentData;
import com.tshang.peipei.base.json.GoGirlUserJson;
import com.tshang.peipei.model.biz.chat.BaseChatSendMessage;
import com.tshang.peipei.model.biz.chat.ChatManageBiz.IGetPushBroadcastListener;
import com.tshang.peipei.model.biz.chat.ChatSessionManageBiz;
import com.tshang.peipei.model.entity.ChatAlbumEntity;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.protocol.asn.gogirl.AboutMeReplyInfo;
import com.tshang.peipei.protocol.asn.gogirl.AlbumChatInfo;
import com.tshang.peipei.protocol.asn.gogirl.AlbumInfo;
import com.tshang.peipei.protocol.asn.gogirl.ApplyJoinGroupInfo;
import com.tshang.peipei.protocol.asn.gogirl.AwardInteractiveInfo;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastRedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.CommentInfo;
import com.tshang.peipei.protocol.asn.gogirl.DareGameInfo;
import com.tshang.peipei.protocol.asn.gogirl.DareInfo;
import com.tshang.peipei.protocol.asn.gogirl.FingerGuessingInfo;
import com.tshang.peipei.protocol.asn.gogirl.GGSkillInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.protocol.asn.gogirl.MemberInOutInfo;
import com.tshang.peipei.protocol.asn.gogirl.NewReceiveGiftPushInfo;
import com.tshang.peipei.protocol.asn.gogirl.NotifySndAwardInfo;
import com.tshang.peipei.protocol.asn.gogirl.NotifySndAwardInfoV2;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqGoGirlTransChatData;
import com.tshang.peipei.protocol.asn.gogirl.SkillDealInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillInteractiveInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillResultInfo;
import com.tshang.peipei.protocol.asn.gogirl.SystemNotifyInfo;
import com.tshang.peipei.protocol.asn.gogirl.TopicFeedInfo;
import com.tshang.peipei.protocol.asn.gogirl.TruthAnswer;
import com.tshang.peipei.protocol.asn.gogirl.TruthInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.entity.DynamicEntity;
import com.tshang.peipei.storage.database.entity.NewDynamicReplyEntity;
import com.tshang.peipei.storage.database.operate.DynamicOperate;
import com.tshang.peipei.storage.database.operate.NewDynamicReplyOperate;
import com.tshang.peipei.storage.db.BroadCastColumn;
import com.tshang.peipei.storage.db.DBHelper;
import com.tshang.peipei.vender.common.util.ListUtils;

import de.greenrobot.event.EventBus;

/**
 * @Title: ReceiverChatData.java 
 *
 * @Description: 长连接接收数据处理
 *
 * @author Administrator  
 *
 * @date 2014年10月17日 下午3:55:19 
 *
 * @version V1.0   
 */
public class ReceiverChatData {

	public static GoGirlDataInfo getGoGirlDataInfo(ReqGoGirlTransChatData req) {//聊天的GoGirlDataInfo数据
		if (req == null) {
			return null;
		}
		GoGirlDataInfoList list = req.chatdata.chatdatalist;
		GoGirlDataInfo dataInfo = null;

		if (list != null && !list.isEmpty()) {
			dataInfo = (GoGirlDataInfo) list.get(0);
		}
		return dataInfo;
	}

	public static ChatDatabaseEntity getChatDatabaseEntity(ReqGoGirlTransChatData req, GoGirlDataInfo dataInfo) {//返回收到的聊天数据
		if (req == null) {
			return null;
		}
		ChatDatabaseEntity chatEntity = new ChatDatabaseEntity();
		int type = req.chatdata.totype.intValue();
		//组装消息
		if (BAApplication.mLocalUserInfo != null && req.chatdata.from.intValue() == BAApplication.mLocalUserInfo.uid.intValue()) {
			chatEntity.setDes(ChatDes.TO_FRIEDN.getValue());
		} else {
			chatEntity.setDes(ChatDes.TO_ME.getValue());
		}
		chatEntity.setCreateTime(req.chatdata.createtimes.longValue() * 1000 + req.chatdata.createtimeus.longValue() / 1000);
		//		System.out.println("shijian1======="+req.chatdata.createtimes.longValue() * 1000+"=========="+req.chatdata.createtimeus.longValue() / 1000);
		//		System.out.println("创建时间====" + chatEntity.getCreateTime() + "data==" + dataInfo.type.intValue());
		chatEntity.setFromID(req.chatdata.from.intValue());
		chatEntity.setTOID(req.chatdata.to.intValue());
		if (type == BaseChatSendMessage.PRIVATE_CHAT_TYPE) {//私聊
			chatEntity.setToUid(-1);//私聊群id为-1;
		} else if (type == BaseChatSendMessage.GROUP_CHAT_TYPE) {//收到群消息
			chatEntity.setToUid(req.chatdata.to.intValue());
		}
		chatEntity.setStatus(ChatStatus.UNREAD.getValue());
		chatEntity.setType(dataInfo.type.intValue());
		chatEntity.setMesSvrID(new String(dataInfo.dataid));
		int sex = req.chatdata.fromsex.intValue();
		String nick = new String(req.chatdata.fromnick);
		chatEntity.setRevStr1(String.valueOf(sex));
		chatEntity.setRevStr2(nick);

		chatEntity.setMesLocalID(-1);
		return chatEntity;
	}

	public static ChatAlbumEntity getChatAlbumEntity(ReqGoGirlTransChatData req, GoGirlDataInfo dataInfo) {//私密相册
		try {
			AlbumChatInfo ydmxMsg = new AlbumChatInfo();
			BERDecoder dec = new BERDecoder(dataInfo.data);
			ydmxMsg.decode(dec);
			AlbumInfo info = ydmxMsg.albuminfo;

			ChatAlbumEntity chatAlbumEntity = new ChatAlbumEntity();
			chatAlbumEntity.accessloyalty = info.accessloyalty;
			chatAlbumEntity.albumdesc = info.albumdesc;
			chatAlbumEntity.albumname = info.albumname;
			chatAlbumEntity.coverpic = info.coverpic;
			chatAlbumEntity.coverpicid = info.coverpicid;
			chatAlbumEntity.coverpickey = info.coverpickey;
			chatAlbumEntity.createtime = info.createtime;
			chatAlbumEntity.id = info.id;
			chatAlbumEntity.lastupdatetime = info.lastupdatetime;
			chatAlbumEntity.photototal = info.photototal;
			return chatAlbumEntity;
		} catch (ASN1Exception e1) {
			e1.printStackTrace();
			return null;
		}

	}

	public static NewReceiveGiftPushInfo getNewReceiveGiftPushInfo(ReqGoGirlTransChatData req, GoGirlDataInfo dataInfo) {//返回收到的礼物数据

		try {
			NewReceiveGiftPushInfo info = new NewReceiveGiftPushInfo();
			BERDecoder dec = new BERDecoder(dataInfo.data);
			info.decode(dec);
			return info;

		} catch (ASN1Exception e1) {
			e1.printStackTrace();
			return null;
		}

	}

	public static NewReceiveGiftPushInfo getGiftDealInfoList(Context context, ReqGoGirlTransChatData req, GoGirlDataInfo dataInfo) {//返回收到的礼物数据
		return getNewReceiveGiftPushInfo(req, dataInfo);

	}

	public static BroadcastInfo getBroadcastInfo(GoGirlDataInfo dataInfo) {//收到广播数据

		try {
			BroadcastInfo broadcastInfo = new BroadcastInfo();
			BERDecoder dec = new BERDecoder(dataInfo.data);
			broadcastInfo.decode(dec);
			return broadcastInfo;

		} catch (ASN1Exception e1) {
			e1.printStackTrace();

		}
		return null;
	}

	public static void operateBraodCastInfo(Context context, GoGirlDataInfo dataInfo, IGetPushBroadcastListener listener) {//返回收到的礼物数据
		BroadcastInfo broadcastInfo = getBroadcastInfo(dataInfo);
		if (BAApplication.mLocalUserInfo != null && broadcastInfo != null) {

			GoGirlUserInfoList userInfoList = broadcastInfo.tousers;
			boolean isAddMe = false;
			if (!ListUtils.isEmpty(userInfoList)) {//处理有 @我的广播
				for (Object object : userInfoList) {
					GoGirlUserInfo info = (GoGirlUserInfo) object;
					if (info != null && info.uid.intValue() == BAApplication.mLocalUserInfo.uid.intValue()) {
						isAddMe = true;
					}
				}
			}
			if (isAddMe) {//判断是不是@我的
				SharedPreferencesTools spfTools = SharedPreferencesTools.getInstance(context);
				int count = spfTools.getIntValueByKeyToZero(GoGirlUserJson.BRAODCAST + BAApplication.mLocalUserInfo.uid.intValue());
				count++;
				spfTools.saveIntKeyValue(count, GoGirlUserJson.BRAODCAST + BAApplication.mLocalUserInfo.uid.intValue());
				DBHelper.getInstance(context).insert(BroadCastColumn.TABLE_NAME, GoGirlUserJson.insertBroadValues(broadcastInfo));
				NoticeEvent noticeEvent = new NoticeEvent();
				noticeEvent.setFlag(NoticeEvent.NOTICE52);
				EventBus.getDefault().postSticky(noticeEvent);
			}
			
			if (listener != null) {
				listener.getPushBroadcastData(broadcastInfo, isAddMe);
			}
		}
	}
	
	public static MemberInOutInfo getMemberInOutInfo(GoGirlDataInfo dataInfo) {//群成员进出
		try {
			MemberInOutInfo memberInOutInfo = new MemberInOutInfo();
			BERDecoder dec = new BERDecoder(dataInfo.data);

			memberInOutInfo.decode(dec);
			return memberInOutInfo;
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ApplyJoinGroupInfo getApplyJoinGroupInfo(GoGirlDataInfo dataInfo) {//同意进入群
		try {
			ApplyJoinGroupInfo applyJoinGroupInfo = new ApplyJoinGroupInfo();
			BERDecoder dec = new BERDecoder(dataInfo.data);

			applyJoinGroupInfo.decode(dec);
			return applyJoinGroupInfo;
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static RedPacketInfo getRedPacketInfo(GoGirlDataInfo dataInfo) {//同意进入群
		try {
			RedPacketInfo redPacketInfo = new RedPacketInfo();
			BERDecoder dec = new BERDecoder(dataInfo.data);

			redPacketInfo.decode(dec);
			return redPacketInfo;
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static SystemNotifyInfo getSystemNotifyInfo(GoGirlDataInfo dataInfo) {//同意进入群
		try {
			SystemNotifyInfo systemNotifyInfo = new SystemNotifyInfo();
			BERDecoder dec = new BERDecoder(dataInfo.data);

			systemNotifyInfo.decode(dec);
			return systemNotifyInfo;
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static SkillDealInfo getSkillDealInfo(GoGirlDataInfo dataInfo) {//同意进入群
		try {
			SkillDealInfo skillDealInfo = new SkillDealInfo();
			BERDecoder dec = new BERDecoder(dataInfo.data);

			skillDealInfo.decode(dec);
			return skillDealInfo;
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static DareGameInfo getStartDareInfo(GoGirlDataInfo dataInfo) {//骰子
		try {
			DareGameInfo skillDealInfo = new DareGameInfo();
			BERDecoder dec = new BERDecoder(dataInfo.data);

			skillDealInfo.decode(dec);
			return skillDealInfo;
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static DareInfo getDareInfo(GoGirlDataInfo dataInfo) {//骰子
		try {
			DareInfo skillDealInfo = new DareInfo();
			BERDecoder dec = new BERDecoder(dataInfo.data);

			skillDealInfo.decode(dec);
			return skillDealInfo;
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 真心话解密
	 * @author Aaron
	 *
	 * @param dataInfo
	 * @return
	 */
	public static TruthInfo getTruthInfo(GoGirlDataInfo dataInfo) {
		TruthInfo truthInfo = new TruthInfo();
		BERDecoder dec = new BERDecoder(dataInfo.data);
		try {
			truthInfo.decode(dec);
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return truthInfo;
	}

	/**
	 * f
	 * @author Aaron
	 *
	 * @param dataInfo
	 * @return
	 */
	public static TruthAnswer getTruthAnswer(GoGirlDataInfo dataInfo) {
		TruthAnswer answer = new TruthAnswer();
		BERDecoder dec = new BERDecoder(dataInfo.data);
		try {
			answer.decode(dec);
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return answer;
	}

	/**
	 * 解密动态回复
	 * @author Administrator
	 *
	 * @param dataInfo
	 * @return
	 */
	public static AboutMeReplyInfo getAboutMeReplyInfo(GoGirlDataInfo dataInfo) {
		AboutMeReplyInfo info = new AboutMeReplyInfo();
		BERDecoder dec = new BERDecoder(dataInfo.data);
		try {
			info.decode(dec);
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * 解密悬赏私聊
	 * @author Aaron
	 *
	 * @param dataInfo
	 * @return
	 */
	public static AwardInteractiveInfo getAwardChatInfo(GoGirlDataInfo dataInfo) {
		AwardInteractiveInfo info = new AwardInteractiveInfo();
		BERDecoder dec = new BERDecoder(dataInfo.data);
		try {
			info.decode(dec);
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * 解密悬赏结束信息
	 * @author Aaron
	 *
	 * @param dataInfo
	 * @return
	 */
	public static NotifySndAwardInfo getSndAwardInfo(GoGirlDataInfo dataInfo) {
		NotifySndAwardInfo info = new NotifySndAwardInfo();
		BERDecoder dec = new BERDecoder(dataInfo.data);
		try {
			info.decode(dec);
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	public static NotifySndAwardInfoV2 getSndWardInfo2(GoGirlDataInfo dataInfo) {
		NotifySndAwardInfoV2 info = new NotifySndAwardInfoV2();
		BERDecoder dec = new BERDecoder(dataInfo.data);
		try {
			info.decode(dec);
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return info;
	}
	
	/**
	 * 解密女神技能私聊
	 * @author DYH
	 *
	 * @param dataInfo
	 * @return
	 */
	public static SkillInteractiveInfo getGoddessSkillChatInfo(GoGirlDataInfo dataInfo) {
		SkillInteractiveInfo info = new SkillInteractiveInfo();
		BERDecoder dec = new BERDecoder(dataInfo.data);
		try {
			info.decode(dec);
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return info;
	}
	
	public static SkillResultInfo getGoddessSkillInfo(GoGirlDataInfo dataInfo){
		SkillResultInfo info = new SkillResultInfo();
		BERDecoder dec = new BERDecoder(dataInfo.data);
		try {
			info.decode(dec);
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return info;
	}
	
	public static RedPacketBetCreateInfo getSolitaireRedpacketInfo(GoGirlDataInfo dataInfo){
		RedPacketBetCreateInfo info = new RedPacketBetCreateInfo();
		BERDecoder dec = new BERDecoder(dataInfo.data);
		try {
			info.decode(dec);
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return info;
	}
	
	public static BroadcastRedPacketInfo getHallRedpacketInfo(GoGirlDataInfo dataInfo){
		BroadcastRedPacketInfo info = new BroadcastRedPacketInfo();
		BERDecoder dec = new BERDecoder(dataInfo.data);
		try {
			info.decode(dec);
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	public static GGSkillInfo getGGSkillInfo(GoGirlDataInfo dataInfo) {//女性对男性技能感兴趣
		try {
			GGSkillInfo ggSkillInfo = new GGSkillInfo();
			BERDecoder dec = new BERDecoder(dataInfo.data);

			ggSkillInfo.decode(dec);
			return ggSkillInfo;
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static FingerGuessingInfo getFingerGuessingInfo(GoGirlDataInfo dataInfo) {//收到猜拳数据
		try {
			FingerGuessingInfo fingerInfo = new FingerGuessingInfo();
			BERDecoder dec = new BERDecoder(dataInfo.data);
			fingerInfo.decode(dec);
			return fingerInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void saveTopicFeedInfo(Context context, GoGirlDataInfo dataInfo) {//处理动态数据
		TopicFeedInfo feedinfo = getTopicFeedInfo(dataInfo);
		if (BAApplication.mLocalUserInfo != null && feedinfo != null) {
			DynamicEntity dynamicEntity = new DynamicEntity();
			dynamicEntity.setLatestUpdateTime(feedinfo.createtime.longValue() * 1000);
			dynamicEntity.setTopicId(feedinfo.topicinfo.topicid.intValue());
			dynamicEntity.setTopicUid(feedinfo.topicinfo.uid.intValue());
			dynamicEntity.setSex(feedinfo.actuserinfo.sex.intValue());
			dynamicEntity.setUserID(feedinfo.actuserinfo.uid.intValue());
			dynamicEntity.setType(0);
			dynamicEntity.setVerStr("");
			dynamicEntity.setNick(new String(feedinfo.actuserinfo.nick));
			if (feedinfo.type.intValue() == 0) {
				GoGirlDataInfoList list = feedinfo.commentinfo.commentcontentlist;
				GoGirlDataInfo contentInfo = (GoGirlDataInfo) list.get(0);
				dynamicEntity.setSessionData(new String(contentInfo.data));
			} else if (feedinfo.type.intValue() == 1) {
				if (feedinfo.commentinfo.replylist.size() > 0) {
					CommentInfo contentInfo = (CommentInfo) feedinfo.commentinfo.replylist.get(0);
					GoGirlDataInfoList list = contentInfo.commentcontentlist;
					GoGirlDataInfo contentInfo11 = (GoGirlDataInfo) list.get(0);
					dynamicEntity.setSessionData(new String(contentInfo11.data));
				}
			}
			DynamicOperate.getInstance(context).insert(dynamicEntity);
			//修改老动态消息提示未读信息
			int unread = SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey(
					BAConstants.PEIPEI_DYNAMIC_UNREAD_NUM, 0);
			SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "").saveIntKeyValue(0,
					BAConstants.PEIPEI_DYNAMIC_UNREAD_NUM);

			//			NoticeEvent noticeEvent = new NoticeEvent();
			//			int num = ChatSessionManageBiz.isExistUnreadMessage(context);
			//			noticeEvent.setNum(num);
			//			noticeEvent.setFlag(NoticeEvent.NOTICE68);
			//			EventBus.getDefault().post(noticeEvent);
		}

	}

	/**
	 * 保存新动态评论列表
	 * @author Aaron
	 *
	 * @param context
	 * @param dataInfo
	 */
	public static void saveDynamicReply(Context context, GoGirlDataInfo dataInfo) {
		AboutMeReplyInfo info = getAboutMeReplyInfo(dataInfo);
		BAParseRspData parser = new BAParseRspData();
		if (BAApplication.mLocalUserInfo != null && info != null) {
			NewDynamicReplyEntity entity = new NewDynamicReplyEntity();
			entity.setType(info.type.intValue());
			entity.setFromuid(info.fromuid.intValue());
			entity.setTopicuid(info.topicuid.intValue());
			entity.setTopicid(info.topicid.intValue());
			entity.setCommentuid(info.commentuid.intValue());
			entity.setAuditstatus(info.auditstatus.intValue());
			entity.setNick(new String(info.nick));
			entity.setHeadpickey(new String(info.headpickey));
			entity.setSex(info.sex.intValue());
			entity.setCreatetime(info.createtime.intValue());
			ContentData replyData = parser.parseTopicInfo(context, info.commentcontentlist, info.sex.intValue());
			ContentData dynamicData = parser.parseTopicInfo(context, info.dynamicscontentlist, info.sex.intValue());
			entity.setReplyContent(replyData.getContent());
			entity.setDynamicContent(dynamicData.getContent());
			entity.setImageKey(dynamicData.getImageList().get(0));
			entity.setImei(new String(info.imei));
			entity.setRevint0(info.revint0.intValue());
			entity.setRevint1(info.revint1.intValue());
			entity.setRevint2(info.revint2.intValue());
			entity.setRevstr0(new String(info.revstr0));
			entity.setRevstr1(new String(info.revstr1));
			entity.setRevstr2(new String(info.revstr2));
			entity.setStatus(1);
			entity.setGlobalid(info.globalid.intValue());
			entity.setColor(new String(info.color));
			entity.setFonttype(info.fonttype.intValue());

			NewDynamicReplyOperate.getInstance(context).insert(entity);

			NoticeEvent event = new NoticeEvent();
			event.setFlag(NoticeEvent.NOTICE87);
			EventBus.getDefault().post(event);
		}
	}

	public static TopicFeedInfo getTopicFeedInfo(GoGirlDataInfo dataInfo) {//收到动态数据

		try {
			TopicFeedInfo broadcastInfo = new TopicFeedInfo();
			BERDecoder dec = new BERDecoder(dataInfo.data);
			broadcastInfo.decode(dec);
			return broadcastInfo;

		} catch (ASN1Exception e1) {
			e1.printStackTrace();

		}
		return null;
	}

	public static void setFansNum(Context context) {
		int fansNum = SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey(
				BAConstants.PEIPEI_FANS_UNREAD_NUM, 0);

		SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "").saveIntKeyValue(fansNum + 1,
				BAConstants.PEIPEI_FANS_UNREAD_NUM);

		NoticeEvent noticeEvent = new NoticeEvent();
		int num = ChatSessionManageBiz.isExistUnreadMessage(context);
		noticeEvent.setNum(num);
		noticeEvent.setFlag(NoticeEvent.NOTICE68);
		EventBus.getDefault().postSticky(noticeEvent);
	}
}
