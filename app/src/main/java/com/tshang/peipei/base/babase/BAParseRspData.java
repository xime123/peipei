package com.tshang.peipei.base.babase;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ibm.asn1.ASN1Exception;
import com.ibm.asn1.BERDecoder;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.protocol.asn.gogirl.AlbumFeedInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GiftFeedInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.storage.SharedPreferencesTools;

/**
 * @Title: BAParseRspData.java 
 *
 * @Description: 解析公共的一些回包数据
 *
 * @author vactor
 *
 * @date 2014-4-14 上午10:36:17 
 *
 * @version V1.0   
 */
public class BAParseRspData {

	//解析帖子
	public ContentData parseTopicInfo(Context context, GoGirlDataInfoList list, int sex) {
		ContentData contentData = new ContentData();
		for (int i = 0, len = list.size(); i < len; i++) {
			GoGirlDataInfo dataInfo = (GoGirlDataInfo) list.get(i);
			int type = dataInfo.type.intValue();
			//文本信息只有一条
			if (type == BAConstants.MessageType.TEXT.getValue()) {
				contentData.content = new String(dataInfo.data);
			}
			//图片
			if (type == BAConstants.MessageType.IMAGE_KEY.getValue()) {
				contentData.imageList.add(new String(dataInfo.data));
			}
			//语音信息只有一条
			if (type == BAConstants.MessageType.VOICE_KEY.getValue()) {
				contentData.audioKey = new String(dataInfo.data);
			}
			//上传相片
			if (type == BAConstants.MessageType.UPLOAD_PHOTO.getValue()) {
				contentData.content = "我新上传了照片～";
				BERDecoder dec = new BERDecoder(dataInfo.data);
				AlbumFeedInfo info = new AlbumFeedInfo();
				try {
					info.decode(dec);
				} catch (ASN1Exception e1) {
					e1.printStackTrace();
					return null;
				}
				contentData.type = type;
				contentData.imageList.clear();
				contentData.imageList.add(0, new String(info.albuminfo.coverpickey));
				BaseLog.i("vactor_log", "type:" + type + "url:" + new String(info.albuminfo.coverpickey));
			}
			//礼物
			if (type == BAConstants.MessageType.GIFT.getValue()) {
				GiftFeedInfo info = new GiftFeedInfo();
				BERDecoder dec = new BERDecoder(dataInfo.data);
				try {
					info.decode(dec);
				} catch (ASN1Exception e1) {
					e1.printStackTrace();
					return null;
				}
				contentData.type = type;

				GiftDealInfoList giftInfoList = info.giftdeallist;
				for (Object object : giftInfoList) {
					GiftDealInfo giftDealInfo = (GiftDealInfo) object;
					if (sex == Gender.FEMALE.getValue()) {
						String alias = SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
								giftDealInfo.from.intValue());
						String sendUserName = TextUtils.isEmpty(alias) ? new String(giftDealInfo.fromnick) : alias;

						contentData.content = new String("我收到来自\"" + sendUserName + "\"" + "等" + info.total.intValue() + "位帅哥美女赠送的礼物，我们之间的亲密度又上涨了～");
					} else {
						String alias = SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
								giftDealInfo.to.intValue());
						String sendUserName = TextUtils.isEmpty(alias) ? new String(giftDealInfo.tonick) : alias;

						contentData.content = new String("我送给了\"" + sendUserName + "\"" + "等" + info.total.intValue() + "位美女礼物，我们之间的亲密度又上涨了～");
					}
					int giftNum = giftDealInfo.giftnum.intValue();
					GiftInfo giftInfo = giftDealInfo.gift;
					for (int num = 0; num < giftNum; num++) {
						contentData.imageList.add(new String(giftInfo.pickey));
					}

				}

			}

		}
		return contentData;
	}

	public class ContentData {
		//文本
		String content;
		int type;
		//语音
		String audioKey;
		//图片集合
		ArrayList<String> imageList = new ArrayList<String>();

		public String getContent() {
			return content;
		}

		public ArrayList<String> getImageList() {
			return imageList;
		}

		public String getAudioKey() {
			return audioKey;
		}

		public int getType() {
			return type;
		}

	}

}
