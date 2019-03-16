package com.tshang.peipei.model.biz.chat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Base64;
import android.util.Xml;

import com.tencent.mm.sdk.platformtools.Log;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseString;
import com.tshang.peipei.model.entity.ChatAlbumEntity;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.protocol.asn.gogirl.AwardGiftInfo;
import com.tshang.peipei.protocol.asn.gogirl.AwardTextInfo;
import com.tshang.peipei.protocol.asn.gogirl.DareGameInfo;
import com.tshang.peipei.protocol.asn.gogirl.DareInfo;
import com.tshang.peipei.protocol.asn.gogirl.FingerGuessingInfo;
import com.tshang.peipei.protocol.asn.gogirl.GGSkillInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillDealInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillGiftInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillTextInfo;
import com.tshang.peipei.protocol.asn.gogirl.SystemNotifyInfo;
import com.tshang.peipei.protocol.asn.gogirl.UserSimpleInfo;
import com.tshang.peipei.protocol.asn.gogirl.UserSimpleInfoList;

/**
 * @Title: 聊天消息
 *
 * @Description: 处理聊天消息中的xml文件
 *
 * @author allen  
 *
 * @date 2014-3-31 下午6:09:18 
 *
 * @version V1.0   
 */
public class ChatMessageBiz {

	/**
	 * 组装消息，用于本地保存,保存语音
	 *
	 */
	public static String saveAudioMessage(int length, int voicelength) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, "msg");

			serializer.startTag(null, "voicemsg");
			serializer.attribute(null, "length", length + "");
			serializer.attribute(null, "voicelength", voicelength + "");
			serializer.endTag(null, "voicemsg");

			serializer.endTag(null, "msg");
			serializer.endDocument();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();

	}

	/**
	 * 组装消息，用于本地保存,保存图片
	 *
	 */
	public static String saveImageMessage(int length, String bigimgurl) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, "msg");

			serializer.startTag(null, "img");
			serializer.attribute(null, "length", length + "");

			serializer.attribute(null, "bigimgurl", bigimgurl);

			serializer.endTag(null, "img");

			serializer.endTag(null, "msg");
			serializer.endDocument();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	/**
	 * 组装消息，用于本地保存,保存私密相册入口
	 *
	 */
	public static String savePrivateMessage(ChatAlbumEntity chatEntity) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, "msg");

			serializer.startTag(null, "album");

			serializer.attribute(null, "accessloyalty", chatEntity.accessloyalty.intValue() + "");
			serializer.attribute(null, "albumdesc", new String(chatEntity.albumdesc));
			serializer.attribute(null, "albumname", new String(chatEntity.albumname));
			serializer.attribute(null, "coverpic", new String(chatEntity.coverpic));
			serializer.attribute(null, "coverpickey", new String(chatEntity.coverpickey));
			serializer.attribute(null, "coverpicid", chatEntity.coverpicid.intValue() + "");
			serializer.attribute(null, "createtime", chatEntity.createtime.intValue() + "");
			serializer.attribute(null, "id", chatEntity.id.intValue() + "");
			serializer.attribute(null, "lastupdatetime", chatEntity.lastupdatetime.intValue() + "");
			serializer.attribute(null, "photototal", chatEntity.photototal.intValue() + "");

			serializer.endTag(null, "album");

			serializer.endTag(null, "msg");
			serializer.endDocument();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();

	}

	public static String saveRedPacketInfo(RedPacketInfo redPacketInfo) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, "msg");

			serializer.startTag(null, "redpacketinfo");

			serializer.attribute(null, "createtime", String.valueOf(redPacketInfo.createtime.intValue()));
			serializer.attribute(null, "createuid", String.valueOf(redPacketInfo.createuid.intValue()));
			serializer.attribute(null, "desc", BaseString.encode(redPacketInfo.desc));
			serializer.attribute(null, "id", String.valueOf(redPacketInfo.id));
			serializer.attribute(null, "totalgoldcoin", String.valueOf(redPacketInfo.totalgoldcoin.intValue()));
			serializer.attribute(null, "totalportionnum", String.valueOf(redPacketInfo.totalportionnum.intValue()));
			serializer.attribute(null, "leftgoldcoin", String.valueOf(redPacketInfo.leftgoldcoin.intValue()));
			serializer.attribute(null, "leftportionnum", String.valueOf(redPacketInfo.leftportionnum.intValue()));

			UserSimpleInfoList list = redPacketInfo.records;
			if (list != null && !list.isEmpty()) {
				UserSimpleInfo info = (UserSimpleInfo) list.get(0);
				if (info != null) {
					serializer.attribute(null, "intdataname", BaseString.encode(info.strdata));
					serializer.attribute(null, "intdata", String.valueOf(info.intdata.intValue()));
				} else {
					serializer.attribute(null, "intdataname", BaseString.encode("0".getBytes()));
					serializer.attribute(null, "intdata", String.valueOf(0));
				}
			} else {
				serializer.attribute(null, "intdataname", BaseString.encode("0".getBytes()));
				serializer.attribute(null, "intdata", String.valueOf(0));
			}

			serializer.endTag(null, "redpacketinfo");

			serializer.endTag(null, "msg");
			serializer.endDocument();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}
	
	public static String saveHallSolitaireRedPacketInfo(Context context, RedPacketBetCreateInfo solitaireRedPacket, GoGirlUserInfo user) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, "msg");
			
			serializer.startTag(null, "hallSolitaireedpacketinfo");
			
			serializer.attribute(null, "id", String.valueOf(solitaireRedPacket.id.intValue()));
			serializer.attribute(null, "createtime", String.valueOf(solitaireRedPacket.createtime.longValue()));
			serializer.attribute(null, "createuid", String.valueOf(solitaireRedPacket.createuid.intValue()));
			serializer.attribute(null, "togroupid", String.valueOf(solitaireRedPacket.togroupid.intValue()));
			serializer.attribute(null, "orggoldcoin", String.valueOf(solitaireRedPacket.orggoldcoin.intValue()));
			serializer.attribute(null, "orgsilvercoin", String.valueOf(solitaireRedPacket.orgsilvercoin.intValue()));
			serializer.attribute(null, "totalgoldcoin", String.valueOf(solitaireRedPacket.totalgoldcoin.intValue()));
			
			serializer.attribute(null, "comsume", String.valueOf(solitaireRedPacket.comsume.intValue()));
			serializer.attribute(null, "comsumedesc", BaseString.encode(solitaireRedPacket.comsumedesc));
			serializer.attribute(null, "endtime", String.valueOf(solitaireRedPacket.endtime.longValue()));
			serializer.attribute(null, "redpacketstatus", String.valueOf(solitaireRedPacket.redpacketstatus.intValue()));
			if(user != null){
				serializer.attribute(null, "sendnick", BaseString.encode(user.nick));
				serializer.attribute(null, "headpickey", BaseString.encode(user.headpickey));
				serializer.attribute(null, "gradeinfo", BaseString.encode(user.gradeinfo));
			}
			
			if(solitaireRedPacket.winuserInfo != null){
				serializer.attribute(null, "winnick", BaseString.encode(solitaireRedPacket.winuserInfo.nick));
				serializer.attribute(null, "winheadpickey", BaseString.encode(solitaireRedPacket.winuserInfo.headpickey));
			}
			serializer.attribute(null, "orgtotalgoldcoin", String.valueOf(solitaireRedPacket.orgtotalgoldcoin.intValue()));
			if(solitaireRedPacket.userInfo != null){
				serializer.attribute(null, "createnick", BaseString.encode(solitaireRedPacket.userInfo.nick));
			}
			serializer.endTag(null, "hallSolitaireedpacketinfo");
			
			GoGirlUserInfoList list = solitaireRedPacket.records;
			if (list != null && !list.isEmpty()) {
				for(int i=0; i<list.size(); i++){
					serializer.startTag(null, "hallRecords");
					GoGirlUserInfo info = (GoGirlUserInfo) list.get(i);
					if (info != null) {
						serializer.attribute(null, "intuid", String.valueOf(info.uid.intValue()));
						serializer.attribute(null, "createtime", String.valueOf(info.createtime.longValue()));
						serializer.attribute(null, "nick", BaseString.encode(info.nick));
						serializer.attribute(null, "headpickey", BaseString.encode(info.headpickey));
						serializer.attribute(null, "gradeinfo", BaseString.encode(info.gradeinfo));
					} else {
						serializer.attribute(null, "intuid", String.valueOf(0));
						serializer.attribute(null, "createtime", String.valueOf(0));
						serializer.attribute(null, "nick", BaseString.encode("0".getBytes()));
						serializer.attribute(null, "headpickey", BaseString.encode("0".getBytes()));
						serializer.attribute(null, "gradeinfo", BaseString.encode("0".getBytes()));
					}
					serializer.endTag(null, "hallRecords");
				}
			} 
			
			serializer.endTag(null, "msg");
			serializer.endDocument();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}
	
	public static String saveSolitaireRedPacketInfo(Context context, RedPacketBetCreateInfo solitaireRedPacket) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, "msg");
			
			serializer.startTag(null, "solitaireedpacketinfo");
			
			serializer.attribute(null, "id", String.valueOf(solitaireRedPacket.id.intValue()));
			serializer.attribute(null, "createtime", String.valueOf(solitaireRedPacket.createtime.longValue()));
			serializer.attribute(null, "createuid", String.valueOf(solitaireRedPacket.createuid.intValue()));
			serializer.attribute(null, "togroupid", String.valueOf(solitaireRedPacket.togroupid.intValue()));
			serializer.attribute(null, "orggoldcoin", String.valueOf(solitaireRedPacket.orggoldcoin.intValue()));
			serializer.attribute(null, "orgsilvercoin", String.valueOf(solitaireRedPacket.orgsilvercoin.intValue()));
			serializer.attribute(null, "totalgoldcoin", String.valueOf(solitaireRedPacket.totalgoldcoin.intValue()));
			
			serializer.attribute(null, "comsume", String.valueOf(solitaireRedPacket.comsume.intValue()));
			serializer.attribute(null, "comsumedesc", BaseString.encode(solitaireRedPacket.comsumedesc));
			serializer.attribute(null, "endtime", String.valueOf(solitaireRedPacket.endtime.longValue()));
			serializer.attribute(null, "redpacketstatus", String.valueOf(solitaireRedPacket.redpacketstatus.intValue()));
			if(solitaireRedPacket.userInfo != null){
				serializer.attribute(null, "createnick", BaseString.encode(solitaireRedPacket.userInfo.nick));
				serializer.attribute(null, "headpickey", BaseString.encode(solitaireRedPacket.userInfo.headpickey));
				serializer.attribute(null, "gradeinfo", BaseString.encode(solitaireRedPacket.userInfo.gradeinfo));
			}
			
			if(solitaireRedPacket.winuserInfo != null){
				serializer.attribute(null, "winnick", BaseString.encode(solitaireRedPacket.winuserInfo.nick));
				serializer.attribute(null, "winheadpickey", BaseString.encode(solitaireRedPacket.winuserInfo.headpickey));
			}
			serializer.attribute(null, "orgtotalgoldcoin", String.valueOf(solitaireRedPacket.orgtotalgoldcoin.intValue()));
			serializer.endTag(null, "solitaireedpacketinfo");
			
			GoGirlUserInfoList list = solitaireRedPacket.records;
			if (list != null && !list.isEmpty()) {
				for(int i=0; i<list.size(); i++){
					serializer.startTag(null, "records");
					GoGirlUserInfo info = (GoGirlUserInfo) list.get(i);
					if (info != null) {
						serializer.attribute(null, "intuid", String.valueOf(info.uid.intValue()));
						serializer.attribute(null, "createtime", String.valueOf(info.createtime.longValue()));
						serializer.attribute(null, "nick", BaseString.encode(info.nick));
						serializer.attribute(null, "headpickey", BaseString.encode(info.headpickey));
						serializer.attribute(null, "gradeinfo", BaseString.encode(info.gradeinfo));
					} else {
						serializer.attribute(null, "intuid", String.valueOf(0));
						serializer.attribute(null, "createtime", String.valueOf(0));
						serializer.attribute(null, "nick", BaseString.encode("0".getBytes()));
						serializer.attribute(null, "headpickey", BaseString.encode("0".getBytes()));
						serializer.attribute(null, "gradeinfo", BaseString.encode("0".getBytes()));
					}
					serializer.endTag(null, "records");
				}
			} 
			
			serializer.endTag(null, "msg");
			serializer.endDocument();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}
	
	public static String saveSkillDealInfo(SkillDealInfo skillDealInfo) {//存储技能下单信息
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, "msg");

			serializer.startTag(null, "skilldealinfo");

			serializer.attribute(null, "createtime", String.valueOf(skillDealInfo.createtime.intValue()));
			serializer.attribute(null, "id", String.valueOf(skillDealInfo.id));
			serializer.attribute(null, "step", String.valueOf(skillDealInfo.step));
			serializer.attribute(null, "type", String.valueOf(skillDealInfo.skillinfo.type.intValue()));
			serializer.attribute(null, "title", BaseString.encode(skillDealInfo.skillinfo.title));
			serializer.attribute(null, "skilluid", String.valueOf(skillDealInfo.skilluid.intValue()));

			serializer.endTag(null, "skilldealinfo");

			serializer.endTag(null, "msg");
			serializer.endDocument();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	/**
	 * 保存悬赏
	 * @author Aaron
	 *
	 * @param textInfo
	 * @param giftInfo
	 * @return
	 */
	public static String saveAwardInfo(Context context, AwardTextInfo textInfo, AwardGiftInfo giftInfo, int createtime, long endtime, int anonym) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, "msg");

			serializer.startTag(null, "awardinfo");
			serializer.attribute(null, "content", new String(textInfo.content));
			if (giftInfo.pricegold.intValue() > 0) {
				serializer.attribute(null, "gold",
						String.valueOf(giftInfo.pricegold.intValue()) + context.getResources().getString(R.string.gold_money));
			} else {
				serializer.attribute(null, "gold",
						String.valueOf(giftInfo.pricesilver.intValue()) + context.getResources().getString(R.string.silver_money));
			}
			serializer.attribute(null, "giftname", new String(giftInfo.name));
			serializer.attribute(null, "giftKey", new String(giftInfo.pickey));
			serializer.attribute(null, "charm", String.valueOf(giftInfo.charmeffect.intValue()));
			serializer.attribute(null, "createtime", String.valueOf(createtime));
			serializer.attribute(null, "endtime", String.valueOf(endtime));
			serializer.attribute(null, "integral", String.valueOf(giftInfo.scoreeffect.intValue()));
			serializer.attribute(null, "skillDesc", new String(textInfo.revstr0));
			serializer.attribute(null, "anonym", anonym + "");

			serializer.endTag(null, "awardinfo");

			serializer.endTag(null, "msg");
			serializer.endDocument();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	/**
	 * 保存女神技能
	 * @author DYH
	 *
	 * @param context
	 * @param textInfo
	 * @param giftInfo
	 * @param createtime
	 * @param endtime
	 * @return
	 */
	public static String saveGoddessSkillInfo(Context context, SkillTextInfo textInfo, SkillGiftInfo giftInfo, int lefttime, int sex, int skilllistid) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, "msg");

			serializer.startTag(null, "skillinfo");
			serializer.attribute(null, "content", new String(textInfo.content));
			if (giftInfo.pricegold.intValue() > 0) {
				serializer.attribute(null, "gold",
						String.valueOf(giftInfo.pricegold.intValue()) + context.getResources().getString(R.string.gold_money));
			} else {
				serializer.attribute(null, "gold",
						String.valueOf(giftInfo.pricesilver.intValue()) + context.getResources().getString(R.string.silver_money));
			}
			serializer.attribute(null, "giftid", String.valueOf(giftInfo.id.intValue()));
			serializer.attribute(null, "giftname", new String(giftInfo.name));
			serializer.attribute(null, "giftKey", new String(giftInfo.pickey));
			serializer.attribute(null, "lefttime", String.valueOf(lefttime));
			//			serializer.attribute(null, "additionalword", new String(additionalword));
			serializer.attribute(null, "charmeffect", String.valueOf(giftInfo.charmeffect.intValue()));
			serializer.attribute(null, "scoreeffect", String.valueOf(giftInfo.scoreeffect.intValue()));
			serializer.attribute(null, "skillDesc", new String(textInfo.desc));
			serializer.attribute(null, "sex", String.valueOf(sex));
			serializer.attribute(null, "skilllistid", String.valueOf(skilllistid));
			serializer.attribute(null, "id", String.valueOf(textInfo.id.intValue()));

			serializer.endTag(null, "skillinfo");

			serializer.endTag(null, "msg");
			serializer.endDocument();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	public static String saveSystemNotifyInfo(SystemNotifyInfo systemNotifyInfo) {//存储技能下单信息
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, "msg");

			serializer.startTag(null, "systemnotifyinfo");
			serializer.attribute(null, "createtime", String.valueOf(systemNotifyInfo.createtime.intValue()));
			serializer.attribute(null, "detail", BaseString.encode(systemNotifyInfo.detail));
			serializer.attribute(null, "type", String.valueOf(systemNotifyInfo.type.intValue()));
			serializer.attribute(null, "title", BaseString.encode(systemNotifyInfo.title));
			serializer.attribute(null, "sharedetail", BaseString.encode(systemNotifyInfo.sharedetail));
			serializer.attribute(null, "picurl", BaseString.encode(systemNotifyInfo.picurl));

			serializer.endTag(null, "systemnotifyinfo");

			serializer.endTag(null, "msg");
			serializer.endDocument();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	public static String saveGGSkillInfo(GGSkillInfo ggSkillInfo) {//存储技能下单信息
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, "msg");

			serializer.startTag(null, "ggskillinfo");

			serializer.attribute(null, "id", String.valueOf(ggSkillInfo.id));
			serializer.attribute(null, "title", BaseString.encode(ggSkillInfo.title));
			serializer.attribute(null, "nick", BaseString.encode(ggSkillInfo.title));

			serializer.endTag(null, "ggskillinfo");

			serializer.endTag(null, "msg");
			serializer.endDocument();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	/**
	 * 组装消息，用于本地保存,保存礼物列表
	 *
	 */
	public static String saveGiftMessage(GiftDealInfoList giftEntity) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, "msg");

			serializer.startTag(null, "gift");

			serializer.attribute(null, "giftkey", new String(((GiftDealInfo) giftEntity.get(0)).gift.pickey));
			serializer.attribute(null, "gifttotal", ((GiftDealInfo) giftEntity.get(0)).giftnum + "");
			serializer.attribute(null, "giftname", new String(((GiftDealInfo) giftEntity.get(0)).gift.name));
			serializer.attribute(null, "giftid", ((GiftDealInfo) giftEntity.get(0)).gift.id + "");

			serializer.endTag(null, "gift");

			serializer.endTag(null, "msg");
			serializer.endDocument();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	/**
	 * 组装消息，用于本地保存,保存礼物列表
	 *
	 */
	public static String saveFingerMessage(FingerGuessingInfo info) {
		if (info == null) {
			return "";
		}
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, "msg");

			serializer.startTag(null, "fingerguessinginfo");

			serializer.attribute(null, "createtime", String.valueOf(info.createtime.intValue()));
			serializer.attribute(null, "finger1", String.valueOf(info.finger1.intValue()));
			serializer.attribute(null, "finger2", String.valueOf(info.finger2.intValue()));
			serializer.attribute(null, "uid1", String.valueOf(info.uid1.intValue()));
			serializer.attribute(null, "uid2", String.valueOf(info.uid2.intValue()));
			serializer.attribute(null, "fingerwinid", String.valueOf(info.winuid));
			serializer.attribute(null, "bet", String.valueOf(info.ante.intValue()));
			serializer.attribute(null, "nick1", BaseString.encode(info.nick1));
			serializer.attribute(null, "nick2", BaseString.encode(info.nick2));
			serializer.attribute(null, "globalid", new String(info.globalid));
			serializer.attribute(null, "playtime2", String.valueOf(info.playtime2.intValue()));
			serializer.attribute(null, "id", String.valueOf(info.id.intValue()));
			serializer.attribute(null, "antetype", info.antetype.intValue() + "");

			serializer.endTag(null, "fingerguessinginfo");

			serializer.endTag(null, "msg");
			serializer.endDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	public static String saveDareInfo(Context context, DareInfo ggSkillInfo) {//大冒险结果内容
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			//保存参加大冒险的人的UID，处理没参加的不弹出送花，等处理结果
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < ggSkillInfo.joinuserlist.size(); i++) {
				sb.append(((DareGameInfo) ggSkillInfo.joinuserlist.get(i)).userid).append(",");
			}
			BAApplication.dareUidList = sb;

			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, "msg");

			serializer.startTag(null, "dareinfo");

			serializer.attribute(null, "globalid", new String(ggSkillInfo.globalid));
			serializer.attribute(null, "memo", new String(ggSkillInfo.memo));
			DareGameInfo gInfo1 = (DareGameInfo) ggSkillInfo.loseruserlist.get(0);
			//			Log.i("Aaron", "gInfo1 nick==" + Base64.encodeToString(new String(gInfo1.nick).getBytes(), Base64.DEFAULT));
			serializer.attribute(null, "nick1", Base64.encodeToString(gInfo1.nick, Base64.DEFAULT));
			serializer.attribute(null, "uid1", gInfo1.userid.intValue() + "");
			if (ggSkillInfo.loseruserlist.size() > 1 && ggSkillInfo.loseruserlist.size() < 3) {
				DareGameInfo gInfo2 = (DareGameInfo) ggSkillInfo.loseruserlist.get(1);
				serializer.attribute(null, "nick2", Base64.encodeToString(gInfo2.nick, Base64.DEFAULT));
				serializer.attribute(null, "uid2", gInfo2.userid.intValue() + "");
			}
			serializer.endTag(null, "dareinfo");

			serializer.endTag(null, "msg");
			serializer.endDocument();

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Aaron", "error==" + e.getMessage());
		}
		return writer.toString();
	}

	public static String encodeBase64File(String path) throws Exception {
		File file = new File(path);
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return Base64.encodeToString(buffer, Base64.DEFAULT);
	}

	/**
	 * 解析消息，用于界面显示
	 *
	 */
	public static ChatMessageEntity decodeMessage(String message) {
		ChatMessageEntity messageEntity = null;
		try {
			ByteArrayInputStream xml = new ByteArrayInputStream(message.getBytes());
			XmlPullParser pullParser = Xml.newPullParser();

			pullParser.setInput(xml, "UTF-8");

			int event = pullParser.getEventType();

			while (event != XmlPullParser.END_DOCUMENT) {

				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					messageEntity = new ChatMessageEntity();
					break;
				case XmlPullParser.START_TAG:
					if ("img".equals(pullParser.getName())) {
						messageEntity.setLength(pullParser.getAttributeValue(0));
						messageEntity.setBigimgurl(pullParser.getAttributeValue(1));
					}
					if ("voicemsg".equals(pullParser.getName())) {
						messageEntity.setLength(pullParser.getAttributeValue(0));
						messageEntity.setVoicelength(pullParser.getAttributeValue(1));
					}
					if ("album".equals(pullParser.getName())) {
						messageEntity.setAccessloyalty(pullParser.getAttributeValue(0));
						messageEntity.setAlbumdesc(pullParser.getAttributeValue(1));
						messageEntity.setAlbumname(pullParser.getAttributeValue(2));
						messageEntity.setCoverpic(pullParser.getAttributeValue(3));
						messageEntity.setCoverpickey(pullParser.getAttributeValue(4));
						messageEntity.setCoverpicid(pullParser.getAttributeValue(5));
						messageEntity.setCreatetime(pullParser.getAttributeValue(6));
						messageEntity.setId(pullParser.getAttributeValue(7));
						messageEntity.setLastupdatetime(pullParser.getAttributeValue(8));
						messageEntity.setPhotototal(pullParser.getAttributeValue(9));
					}
					if ("gift".equals(pullParser.getName())) {
						messageEntity.setGiftKey(pullParser.getAttributeValue(0));
						messageEntity.setGiftSize(pullParser.getAttributeValue(1));
						messageEntity.setGiftName(pullParser.getAttributeValue(2));
						messageEntity.setGiftId(pullParser.getAttributeValue(3));
					}

					if ("fingerguessinginfo".equals(pullParser.getName())) {
						messageEntity.setCreatetime(pullParser.getAttributeValue(0));
						messageEntity.setFinger1(pullParser.getAttributeValue(1));
						messageEntity.setFinger2(pullParser.getAttributeValue(2));
						messageEntity.setFingerUid1(pullParser.getAttributeValue(3));
						messageEntity.setFingerUid2(pullParser.getAttributeValue(4));
						messageEntity.setFingerWinId(pullParser.getAttributeValue(5));
						messageEntity.setBet(pullParser.getAttributeValue(6));
						messageEntity.setFingerNick1(BaseString.decode(pullParser.getAttributeValue(7)));
						messageEntity.setFingerNick2(BaseString.decode(pullParser.getAttributeValue(8)));
						messageEntity.setGlobid(pullParser.getAttributeValue(9));
						messageEntity.setPlaytime2(pullParser.getAttributeValue(10));
						messageEntity.setId(pullParser.getAttributeValue(11));
						messageEntity.setAntetype(pullParser.getAttributeValue(12));
					}

					if ("redpacketinfo".equals(pullParser.getName())) {
						messageEntity.setCreatetime(pullParser.getAttributeValue(0));
						messageEntity.setCreateuid(pullParser.getAttributeValue(1));
						messageEntity.setDesc(BaseString.decode(pullParser.getAttributeValue(2)));
						messageEntity.setId(pullParser.getAttributeValue(3));
						messageEntity.setTotalgoldcoin(pullParser.getAttributeValue(4));
						messageEntity.setTotalportionnum(pullParser.getAttributeValue(5));
						messageEntity.setLeftgoldcoin(pullParser.getAttributeValue(6));
						messageEntity.setLeftportionnum(pullParser.getAttributeValue(7));
						messageEntity.setHaveGetRedPacketCoinUserName(BaseString.decode(pullParser.getAttributeValue(8)));
						messageEntity.setIntdata(pullParser.getAttributeValue(9));

					}
					if ("skilldealinfo".equals(pullParser.getName())) {
						messageEntity.setCreatetime(pullParser.getAttributeValue(0));
						messageEntity.setId(pullParser.getAttributeValue(1));
						messageEntity.setStep(pullParser.getAttributeValue(2));
						messageEntity.setType(pullParser.getAttributeValue(3));
						messageEntity.setTitle(BaseString.decode(pullParser.getAttributeValue(4)));
						messageEntity.setSkilluid(pullParser.getAttributeValue(5));
					}
					if ("ggskillinfo".equals(pullParser.getName())) {
						messageEntity.setId(pullParser.getAttributeValue(0));
						messageEntity.setTitle(BaseString.decode(pullParser.getAttributeValue(1)));
						messageEntity.setNick(BaseString.decode(pullParser.getAttributeValue(2)));
					}
					if ("systemnotifyinfo".equals(pullParser.getName())) {
						messageEntity.setCreatetime(pullParser.getAttributeValue(0));
						messageEntity.setDetail(BaseString.decode(pullParser.getAttributeValue(1)));
						messageEntity.setType(pullParser.getAttributeValue(2));
						messageEntity.setTitle(BaseString.decode(pullParser.getAttributeValue(3)));
						messageEntity.setDesc(BaseString.decode(pullParser.getAttributeValue(4)));
						messageEntity.setCoverpickey(BaseString.decode(pullParser.getAttributeValue(5)));
					}
					if ("dareinfo".equals(pullParser.getName())) {
						messageEntity.setGlobalid(pullParser.getAttributeValue(0));
						messageEntity.setMemo(pullParser.getAttributeValue(1));
						messageEntity.setDarenick1(pullParser.getAttributeValue(2));
						messageEntity.setDareuid1(pullParser.getAttributeValue(3));
						if (pullParser.getAttributeCount() == 6) {
							messageEntity.setDarenick2(pullParser.getAttributeValue(4));
							messageEntity.setDareuid2(pullParser.getAttributeValue(5));
						}
					}

					if ("awardinfo".equals(pullParser.getName())) {
						messageEntity.setAccessloyalty(pullParser.getAttributeValue(0));
						messageEntity.setGlobid(pullParser.getAttributeValue(1));
						messageEntity.setGiftName(pullParser.getAttributeValue(2));
						messageEntity.setGiftKey(pullParser.getAttributeValue(3));
						messageEntity.setAlbumname(pullParser.getAttributeValue(4));
						messageEntity.setCreatetime(pullParser.getAttributeValue(5));
						messageEntity.setLastupdatetime(pullParser.getAttributeValue(6));
						messageEntity.setCancelflag(pullParser.getAttributeValue(7));
						messageEntity.setCoverpic(pullParser.getAttributeValue(8));
						messageEntity.setDesc(pullParser.getAttributeValue(9));
					}

					if ("skillinfo".equals(pullParser.getName())) {
						messageEntity.setContent(pullParser.getAttributeValue(0));
						messageEntity.setGold(pullParser.getAttributeValue(1));
						messageEntity.setGiftId(pullParser.getAttributeValue(2));
						messageEntity.setGiftName(pullParser.getAttributeValue(3));
						messageEntity.setGiftKey(pullParser.getAttributeValue(4));
						messageEntity.setLefttime(pullParser.getAttributeValue(5));
						//						messageEntity.setAdditionalword(pullParser.getAttributeValue(6));
						messageEntity.setCharmeffect(pullParser.getAttributeValue(6));
						messageEntity.setScoreeffect(pullParser.getAttributeValue(7));
						messageEntity.setSkillDesc(pullParser.getAttributeValue(8));
						messageEntity.setSex(pullParser.getAttributeValue(9));
						messageEntity.setSkilllistid(pullParser.getAttributeValue(10));
						messageEntity.setId(pullParser.getAttributeValue(11));
					}
					
					//红宫红包接龙
					if("hallSolitaireedpacketinfo".equals(pullParser.getName())){
						messageEntity.setId(pullParser.getAttributeValue(0));
						messageEntity.setCreatetime(pullParser.getAttributeValue(1));
						messageEntity.setCreateuid(pullParser.getAttributeValue(2));
						messageEntity.setTogroupid(pullParser.getAttributeValue(3));
						messageEntity.setOrggoldcoin(pullParser.getAttributeValue(4));
						messageEntity.setOrgsilvercoin(pullParser.getAttributeValue(5));
						messageEntity.setTotalgoldcoin(pullParser.getAttributeValue(6));
						messageEntity.setComsume(pullParser.getAttributeValue(7));
						messageEntity.setComsumedesc(BaseString.decode(pullParser.getAttributeValue(8)));
						messageEntity.setEndtime(pullParser.getAttributeValue(9));
						messageEntity.setRedpacketstatus(pullParser.getAttributeValue(10));
						messageEntity.setCreatenick(BaseString.decode(pullParser.getAttributeValue(11)));
						messageEntity.setCreateAvatar(BaseString.decode(pullParser.getAttributeValue(12)));
						messageEntity.setGradeinfo(BaseString.decode(pullParser.getAttributeValue(13)));
						messageEntity.setWinnick(BaseString.decode(pullParser.getAttributeValue(14)));
						messageEntity.setWinAvatar(BaseString.decode(pullParser.getAttributeValue(15)));
						messageEntity.setOrgtotalgoldcoin(pullParser.getAttributeValue(16));
						messageEntity.setNick(BaseString.decode(pullParser.getAttributeValue(17)));
					}else if("hallRecords".equals(pullParser.getName())){
						ChatMessageEntity childEntity = new ChatMessageEntity();
						childEntity.setId(pullParser.getAttributeValue(0));
						childEntity.setCreatetime(pullParser.getAttributeValue(1));
						childEntity.setHaveGetRedPacketCoinUserName(BaseString.decode(pullParser.getAttributeValue(2)));
						childEntity.setCreateAvatar(BaseString.decode(pullParser.getAttributeValue(3)));
						childEntity.setGradeinfo(BaseString.decode(pullParser.getAttributeValue(4)));
						messageEntity.getJionPersons().add(childEntity);
					}
					
					//大厅红包接龙
					if("solitaireedpacketinfo".equals(pullParser.getName())){
						messageEntity.setId(pullParser.getAttributeValue(0));
						messageEntity.setCreatetime(pullParser.getAttributeValue(1));
						messageEntity.setCreateuid(pullParser.getAttributeValue(2));
						messageEntity.setTogroupid(pullParser.getAttributeValue(3));
						messageEntity.setOrggoldcoin(pullParser.getAttributeValue(4));
						messageEntity.setOrgsilvercoin(pullParser.getAttributeValue(5));
						messageEntity.setTotalgoldcoin(pullParser.getAttributeValue(6));
						messageEntity.setComsume(pullParser.getAttributeValue(7));
						messageEntity.setComsumedesc(BaseString.decode(pullParser.getAttributeValue(8)));
						messageEntity.setEndtime(pullParser.getAttributeValue(9));
						messageEntity.setRedpacketstatus(pullParser.getAttributeValue(10));
						messageEntity.setCreatenick(BaseString.decode(pullParser.getAttributeValue(11)));
						messageEntity.setCreateAvatar(BaseString.decode(pullParser.getAttributeValue(12)));
						messageEntity.setGradeinfo(BaseString.decode(pullParser.getAttributeValue(13)));
						messageEntity.setWinnick(BaseString.decode(pullParser.getAttributeValue(14)));
						messageEntity.setWinAvatar(BaseString.decode(pullParser.getAttributeValue(15)));
						messageEntity.setOrgtotalgoldcoin(pullParser.getAttributeValue(16));
					}else if("records".equals(pullParser.getName())){
						ChatMessageEntity childEntity = new ChatMessageEntity();
						childEntity.setId(pullParser.getAttributeValue(0));
						childEntity.setCreatetime(pullParser.getAttributeValue(1));
						childEntity.setHaveGetRedPacketCoinUserName(BaseString.decode(pullParser.getAttributeValue(2)));
						childEntity.setCreateAvatar(BaseString.decode(pullParser.getAttributeValue(3)));
						childEntity.setGradeinfo(BaseString.decode(pullParser.getAttributeValue(4)));
						messageEntity.getJionPersons().add(childEntity);
					}
					
					break;
				}
				event = pullParser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return messageEntity;
	}
}
