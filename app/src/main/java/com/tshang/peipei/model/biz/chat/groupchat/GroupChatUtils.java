package com.tshang.peipei.model.biz.chat.groupchat;

import java.math.BigInteger;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;

import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.protocol.asn.gogirl.FingerGuessingInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.vender.common.util.ImageUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;
import com.tshang.peipei.vender.imageloader.core.assist.FailReason;
import com.tshang.peipei.vender.imageloader.core.assist.ImageScaleType;
import com.tshang.peipei.vender.imageloader.core.assist.ImageSize;
import com.tshang.peipei.vender.imageloader.core.listener.ImageLoadingListener;

/**
 * @Title: GroupChatText.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2014年9月22日 下午2:09:39 
 *
 * @version V1.0   
 */
public class GroupChatUtils {
	/**
	 * 群聊发送图片
	 * @author Jeff
	 *
	 * @param acitivty
	 * @param friendUid
	 * @param friendNick
	 * @param path
	 * @param handler
	 */
	public static void sendGroupChatImage(final Activity acitivty, final int friendUid, final String friendNick, final String path,
			final BAHandler handler, final boolean isReSend, final ChatDatabaseEntity chatEntity) {//群聊发送图片
		if (!TextUtils.isEmpty(path)) {
			DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).considerExifParams(true)
					.cacheInMemory(false).cacheOnDisk(false).bitmapConfig(Bitmap.Config.RGB_565).build();

			ImageSize imageSize = new ImageSize(BasePhone.getScreenWidth(acitivty), BasePhone.getScreenHeight(acitivty));

			ImageLoader.getInstance().loadImage("file://" + path, imageSize, options, new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String imageUri, View view) {

				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					if (loadedImage != null) {
						byte[] bitmapBytes = ImageUtils.bitmapToByte(loadedImage);
						if (bitmapBytes != null) {
							SendGroupChatImageMessage.getInstance().sendImageGroupMsg(acitivty, bitmapBytes, friendUid, friendNick, handler,
									chatEntity, isReSend);
						}
					}
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {

				}
			});

			//			
			//			Bitmap mBitmap = ImageUtils.getBitmapFromSdCardPath(path);
			//			byte[] bitmapBytes = BaseBitmap.compBitmap2Byte(mBitmap, 640, 100);
			//			if (bitmapBytes != null) {
			//				SendGroupChatImageMessage.getInstance()
			//						.sendImageGroupMsg(acitivty, bitmapBytes, friendUid, friendNick, handler, chatEntity, isReSend);
			//			}
		}
	}

	/**
	 * 发送群语音
	 * @author Administrator
	 *
	 * @param acitivty
	 * @param voicebyte
	 * @param voiceLen
	 * @param friendUid
	 * @param friendNick
	 * @param handler
	 * @param isReSend
	 */
	public static void sendGroupChatVoice(Activity acitivty, byte[] voicebyte, int voiceLen, int friendUid, String friendNick, BAHandler handler,
			boolean isReSend, ChatDatabaseEntity chatDatabaseEntity) {
		if (voicebyte != null) {
			SendGroupChatVoiceMessage.getInstance().sendVoiceGroupMsg(acitivty, voicebyte, voiceLen, friendUid, friendNick, handler,
					chatDatabaseEntity, isReSend);
		}

	}

	public static FingerGuessingInfo getFirstGingerGuessInfo(Activity acitivty, int finger1, int uid2, byte[] nick2, int ante, int antetype) {//第一次发送猜拳
		int time = (int) (System.currentTimeMillis() / 1000);
		return getFingerGuessInfo(acitivty, finger1, 0, 0, uid2, nick2, ante, "", time, time, time, -1, antetype);
	}

	public static FingerGuessingInfo getReplyFingerGuessInfo(Activity acitivty, ChatMessageEntity messageEntity, int finger2) {
		if (messageEntity == null) {
			return null;
		}
		try {
			int finger1 = Integer.parseInt(messageEntity.getFinger1());
			int id = Integer.parseInt(messageEntity.getId());
			int uid1 = Integer.parseInt(messageEntity.getFingerUid1());
			byte[] nick1 = messageEntity.getFingerNick1().getBytes();
			int ante = Integer.parseInt(messageEntity.getBet());
			String globid = messageEntity.getGlobid();
			int createTime = Integer.parseInt(messageEntity.getCreatetime());
			int windid = Integer.parseInt(messageEntity.getFingerWinId());
			int time = (int) (System.currentTimeMillis() / 1000);
			return getReplyFingerGuessInfo(acitivty, finger1, finger2, id, uid1, nick1, ante, globid, createTime, createTime, time, windid,
					Integer.parseInt(messageEntity.getAntetype()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @author Jeff
	 *
	 * @param acitivty 上下文
	 * @param finger1 第一个人出的拳
	 * @param finger2 第二个人出的拳
	 * @param id 这个猜拳的id
	 * @param uid2 发送给对方的uid
	 * @param nick2 对方的昵称
	 * @param ante 赌注
	 * @param globid 回复猜拳的id
	 * @param createTime 创建时间
	 * @param playtime1 玩家一的时间
	 * @param playtime2 玩家2回复的时间
	 * @param windid 赢的用户id
	 * @return
	 */
	public static FingerGuessingInfo getFingerGuessInfo(Activity acitivty, int finger1, int finger2, int id, int uid2, byte[] nick2, int ante,
			String globid, int createTime, int playtime1, int playtime2, int windid, int antetype) {
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(acitivty);
		if (userInfo == null) {
			return null;
		}
		FingerGuessingInfo info = new FingerGuessingInfo();
		info.createtime = BigInteger.valueOf(createTime);
		info.finger1 = BigInteger.valueOf(finger1);
		info.uid1 = userInfo.uid;
		info.nick1 = userInfo.nick;
		info.ante = BigInteger.valueOf(ante);
		info.id = BigInteger.valueOf(id);
		info.finger2 = BigInteger.valueOf(finger2);
		info.globalid = globid.getBytes();
		info.memo = "".getBytes();
		info.placetag = BigInteger.valueOf(0);
		info.uid2 = BigInteger.valueOf(uid2);
		info.nick2 = nick2;
		info.playtime1 = BigInteger.valueOf(playtime1);
		info.playtime2 = BigInteger.valueOf(playtime2);
		info.winuid = BigInteger.valueOf(windid);
		info.antetype = BigInteger.valueOf(antetype);
		info.revint3 = BigInteger.valueOf(0);
		info.revint4 = BigInteger.valueOf(0);
		info.revint5 = BigInteger.valueOf(0);
		info.revint6 = BigInteger.valueOf(0);
		info.revint7 = BigInteger.valueOf(0);
		info.revint8 = BigInteger.valueOf(0);
		info.revint9 = BigInteger.valueOf(0);
		info.revstr0 = "".getBytes();
		info.revstr1 = "".getBytes();
		info.revstr2 = "".getBytes();
		info.revstr3 = "".getBytes();
		info.revstr4 = "".getBytes();
		info.revstr5 = "".getBytes();
		info.revstr6 = "".getBytes();

		return info;

	}

	public static FingerGuessingInfo getReplyFingerGuessInfo(Activity acitivty, int finger1, int finger2, int id, int uid1, byte[] nick1, int ante,
			String globid, int createTime, int playtime1, int playtime2, int windid, int antetype) {
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(acitivty);
		if (userInfo == null) {
			return null;
		}
		FingerGuessingInfo info = new FingerGuessingInfo();
		info.createtime = BigInteger.valueOf(createTime);
		info.finger1 = BigInteger.valueOf(finger1);
		info.uid2 = userInfo.uid;
		info.nick2 = userInfo.nick;
		info.ante = BigInteger.valueOf(ante);
		info.id = BigInteger.valueOf(id);
		info.finger2 = BigInteger.valueOf(finger2);
		info.globalid = globid.getBytes();
		info.memo = "".getBytes();
		info.placetag = BigInteger.valueOf(0);
		info.uid1 = BigInteger.valueOf(uid1);
		info.nick1 = nick1;
		info.playtime1 = BigInteger.valueOf(playtime1);
		info.playtime2 = BigInteger.valueOf(playtime2);
		info.winuid = BigInteger.valueOf(windid);
		info.antetype = BigInteger.valueOf(antetype);
		info.revint3 = BigInteger.valueOf(0);
		info.revint4 = BigInteger.valueOf(0);
		info.revint5 = BigInteger.valueOf(0);
		info.revint6 = BigInteger.valueOf(0);
		info.revint7 = BigInteger.valueOf(0);
		info.revint8 = BigInteger.valueOf(0);
		info.revint9 = BigInteger.valueOf(0);
		info.revstr0 = "".getBytes();
		info.revstr1 = "".getBytes();
		info.revstr2 = "".getBytes();
		info.revstr3 = "".getBytes();
		info.revstr4 = "".getBytes();
		info.revstr5 = "".getBytes();
		info.revstr6 = "".getBytes();

		return info;

	}

}
