package com.tshang.peipei.model.biz.chat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.path.android.jobqueue.JobManager;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.main.MainActivity;
import com.tshang.peipei.base.BaseBitmap;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseString;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAConstants.NotificationManagerId;
import com.tshang.peipei.base.babase.BAConstants.PushMessageType;
import com.tshang.peipei.base.json.ApplyJoinGroupInfoJson;
import com.tshang.peipei.base.json.MemberInOutInfoJson;
import com.tshang.peipei.model.biz.PeiPeiGetVoiceBiz;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.jobs.DownLoadImageJob;
import com.tshang.peipei.model.entity.ChatAlbumEntity;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestAddBlacklist;
import com.tshang.peipei.model.request.RequestAddBlacklist.IAddBlacklist;
import com.tshang.peipei.protocol.asn.gogirl.ApplyJoinGroupInfo;
import com.tshang.peipei.protocol.asn.gogirl.FingerGuessingInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.MemberInOutInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.entity.ReceiptEntity;
import com.tshang.peipei.storage.database.operate.ChatOperate;
import com.tshang.peipei.storage.database.operate.PeipeiSessionOperate;
import com.tshang.peipei.storage.database.operate.ReceiptOperate;
import com.tshang.peipei.storage.database.operate.RedpacketOperate;
import com.tshang.peipei.vender.common.util.ListUtils;

import de.greenrobot.event.EventBus;

/**
 * @Title: 聊天界面逻辑类
 *
 * @Description: 聊天界面的一些逻辑
 *
 * @author allen
 *
 * @date 2014-3-27 下午6:38:10
 *
 * @version V1.0
 */
public class ChatRecordBiz {
	public static final int GROUPCHATTYPE = 1;//群聊消息
	public static final int PRIVATECHATTYPE = 0;//私聊消息

	/**
	 * 保存聊天消息数据
	 *
	 * @param data 数据
	 * @param lenght 如果是语音，语音的长度
	 *
	 * @return true:保存成功，false：失败
	 */
	public synchronized static boolean save1Msg(byte[] data, int chatType, int lenght, int friendUid, Context context, int status, int des,
			String mesSerId, long time, int dbfriend, String nick, int sex, ChatAlbumEntity chatEntity, GiftDealInfoList giftEntity,
			FingerGuessingInfo fingerInfo, String fingerId, ApplyJoinGroupInfo applyJoinGroupInfo, MemberInOutInfo memberInOutInfo, int bet,
			int type, int groupid) {
		try {
			if (data == null) {
				return false;
			}
			if (chatType == MessageType.VOICE.getValue() && data.length == 0) {
				return false;
			}

			ChatDatabaseEntity chatDatabaseEntity = new ChatDatabaseEntity();
			chatDatabaseEntity.setStatus(status);
			chatDatabaseEntity.setDes(des);
			chatDatabaseEntity.setFromID(friendUid);
			chatDatabaseEntity.setMesSvrID(mesSerId);
			chatDatabaseEntity.setProgress(0);
			chatDatabaseEntity.setToUid(groupid);
			chatDatabaseEntity.setType(chatType);
			chatDatabaseEntity.setCreateTime(time);

			String message = "";
			String sessionMsg = "";

			MessageType chatType1 = MessageType.getMessage(chatType);

			try {
				if (chatType1 != null)
					switch (chatType1) {
					case TEXT:
						sessionMsg = message = new String(data);
						break;
					case BURN_VOICE:
						message = ChatMessageBiz.saveAudioMessage(data.length, lenght);
						sessionMsg = context.getString(R.string.session_voice);
						break;
					case VOICE:
						message = ChatMessageBiz.saveAudioMessage(data.length, lenght);
						sessionMsg = context.getString(R.string.session_voice);
						break;
					case VOICE_KEY:
						message = ChatMessageBiz.saveAudioMessage(0, lenght);
						sessionMsg = context.getString(R.string.session_voice);
						break;
					case IMAGE:
						message = ChatMessageBiz.saveImageMessage(data.length, "");
						sessionMsg = context.getString(R.string.session_image);
						break;
					case IMAGE_KEY:
						message = ChatMessageBiz.saveImageMessage(0, new String(data));
						sessionMsg = context.getString(R.string.session_image);
						break;
					case BURN_IMAGE:
						message = ChatMessageBiz.saveImageMessage(data.length, "");
						sessionMsg = context.getString(R.string.session_image);
						break;
					case BURN_VOICE_KEY:
						message = ChatMessageBiz.saveAudioMessage(0, lenght);
						sessionMsg = context.getString(R.string.session_voice);
						break;
					case BURN_IMAGE_KEY:
						message = ChatMessageBiz.saveImageMessage(0, new String(data));
						sessionMsg = context.getString(R.string.session_image);
						break;
					case PRIVATE_ALBUM:
						if (chatEntity != null) {
							message = ChatMessageBiz.savePrivateMessage(chatEntity);
							sessionMsg = String.format(context.getString(R.string.chat_album_content), new String(chatEntity.albumname),
									chatEntity.accessloyalty.intValue());
						}
						break;
					case NEW_GIFT:
						if (giftEntity != null) {
							if (BAApplication.mLocalUserInfo != null) {
								if (((GiftDealInfo) giftEntity.get(0)).gift.id.intValue() != 100) {
									sessionMsg = String.format(context.getString(R.string.chat_gift_context_message), new String(
											BAApplication.mLocalUserInfo.nick));
								} else {
									sessionMsg = context.getString(R.string.chat_gift_context_message2);
								}
							}

							message = ChatMessageBiz.saveGiftMessage(giftEntity);
						}
						break;
					case FINGER:
					case WITHANTEFINGER:
					case NEWFINGER:
						if (fingerInfo == null) {
							message = ChatMessageBiz.saveFingerMessage(fingerInfo);
							sessionMsg = context.getString(R.string.finger_right_content1);
						} else {
							message = ChatMessageBiz.saveFingerMessage(fingerInfo);
							if (fingerInfo.winuid.intValue() >= 0) {
								if (fingerInfo.winuid.intValue() == 0) {
									sessionMsg = context.getString(R.string.finger_winner_content2);
								} else if (fingerInfo.winuid.intValue() == fingerInfo.uid1.intValue()) {
									if (fingerInfo.ante.intValue() != 0) {
										String monkey = fingerInfo.ante.intValue() + "金币";
										if (fingerInfo.antetype.intValue() == 1) {
											monkey = fingerInfo.ante.intValue() + "银币";
										}
										sessionMsg = String.format(context.getString(R.string.str_finger_winner_content),
												new String(fingerInfo.nick1), monkey);
									} else {
										sessionMsg = String.format(context.getString(R.string.finger_winner_content1), new String(fingerInfo.nick1));
									}
								} else {
									if (fingerInfo.ante.intValue() != 0) {
										String monkey = fingerInfo.ante.intValue() + "金币";
										if (fingerInfo.antetype.intValue() == 1) {
											monkey = fingerInfo.ante.intValue() + "银币";
										}
										sessionMsg = String.format(context.getString(R.string.str_finger_winner_content),
												new String(fingerInfo.nick2), monkey);
									} else {
										sessionMsg = String.format(context.getString(R.string.finger_winner_content1), new String(fingerInfo.nick2));
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
								sessionMsg = String.format(context.getString(R.string.finger_left_content1), str);
							}
						}
						break;
					case VIDEO:
					case GOGIRL_DATA_TYPE_ANONYM_VEDIO:
						sessionMsg = "[视频]";
						message = new String(data);
						break;
					case JOINHAREM:
						sessionMsg = "[申请加入后宫]";
						message = ApplyJoinGroupInfoJson.changeObjectDateToJson(applyJoinGroupInfo);
						break;
					case AGREEJOINHAREM:
						sessionMsg = "";
						if (memberInOutInfo != null) {//
							int act = memberInOutInfo.act.intValue();
							if (act == 1) {//主动离开
								sessionMsg = "[退出群]";
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
						}
						message = MemberInOutInfoJson.changeObjectDateToJson(memberInOutInfo);
						break;
					default:
						break;
					}
			} catch (Exception e) {
				e.printStackTrace();
				sessionMsg = message = context.getResources().getString(R.string.version_low);
			}

			if (TextUtils.isEmpty(message)) {
				return false;
			}
			chatDatabaseEntity.setMessage(message);
			ChatOperate chatDatabase = null;
			if (type == 0 || type == 3) {
				chatDatabase = ChatOperate.getInstance(context, dbfriend, false);
			} else if (type == 1) {
				chatDatabase = ChatOperate.getInstance(context, groupid, true);
			}
			if (chatDatabase == null)
				return false;

			chatDatabase.insert(chatDatabaseEntity);
			ChatDatabaseEntity cdbe = chatDatabase.getLastestMessage();

			try {
				switch (chatType1) {
				case BURN_VOICE:
					if (TextUtils.isEmpty(ChatRecordBiz.saveFile(context, friendUid, cdbe.getMesLocalID(), data, false))) {
						chatDatabase.delete(cdbe.getMesLocalID());
					}
					break;
				case VOICE:
					if (type == 1) {
						if (TextUtils.isEmpty(ChatRecordBiz.saveFile(context, groupid, cdbe.getMesLocalID(), data, false))) {
							chatDatabase.delete(cdbe.getMesLocalID());
						}
					} else if (type == 0) {
						if (TextUtils.isEmpty(ChatRecordBiz.saveFile(context, friendUid, cdbe.getMesLocalID(), data, false))) {
							chatDatabase.delete(cdbe.getMesLocalID());
						}
					}
					break;
				case VOICE_KEY:
					PeiPeiGetVoiceBiz pBiz = new PeiPeiGetVoiceBiz();
					if (type == 1) {
						pBiz.getVoiceByKey(data, cdbe.getMesLocalID(), groupid);
					} else if (type == 0) {
						pBiz.getVoiceByKey(data, cdbe.getMesLocalID(), friendUid);
					}
					break;
				case IMAGE:
					if (type == 1) {
						if (TextUtils.isEmpty(ChatRecordBiz.saveFile(context, groupid, cdbe.getMesLocalID(), data, true))) {
							chatDatabase.delete(cdbe.getMesLocalID());
						}
					} else if (type == 0) {
						if (TextUtils.isEmpty(ChatRecordBiz.saveFile(context, friendUid, cdbe.getMesLocalID(), data, false))) {
							chatDatabase.delete(cdbe.getMesLocalID());
						}
					}
					break;
				case IMAGE_KEY:
					JobManager jobManager = BAApplication.getInstance().getJobManager();
					jobManager.addJobInBackground(new DownLoadImageJob(context, new String(data), dbfriend, cdbe.getMesLocalID(), sex, 0));
					//返回失败，在下载线程中判断是否保存成功
					return false;
				case BURN_IMAGE:
					if (TextUtils.isEmpty(ChatRecordBiz.saveFile(context, friendUid, cdbe.getMesLocalID(), data, true))) {
						chatDatabase.delete(cdbe.getMesLocalID());
					}
					break;
				case BURN_VOICE_KEY:
					PeiPeiGetVoiceBiz pBiz1 = new PeiPeiGetVoiceBiz();
					pBiz1.getVoiceByKey(data, cdbe.getMesLocalID(), friendUid);
					break;
				case BURN_IMAGE_KEY:
					JobManager jobManager1 = BAApplication.getInstance().getJobManager();
					jobManager1.addJobInBackground(new DownLoadImageJob(context, new String(data), dbfriend, cdbe.getMesLocalID(), sex, 0));
					//返回失败，在下载线程中判断是否保存成功
					return false;
				default:
					break;
				}
			} catch (Exception e) {
			}
			chatDatabase = null;

			if (!TextUtils.isEmpty(sessionMsg)) {
				int toUid = dbfriend;
				if (type == 1) {
					toUid = groupid;
					sex = Gender.MALE.getValue();
				}
				boolean b = ChatSessionManageBiz.isHaveSession(context, toUid, type);
				if (b) {//存在就更新最后一条数据
					ChatSessionManageBiz.upDataSession(context, sessionMsg, nick, toUid, time, type);
				} else {//不存在就插入一条新的数据
					ChatManageBiz.haveNotSession(context, toUid, nick, sex, type, sessionMsg);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 获取头像
	 *
	 */
	public static BitmapDrawable getHeadBD(Context context, String headpath, int frienduid) {
		Bitmap tbm = null;
		if (!TextUtils.isEmpty(headpath)) {
			File PHOTO_DIR = new File(BaseString.getFilePath(context, frienduid, MessageType.IMAGE.getValue()) + "/" + headpath + ".jpg");
			if (!PHOTO_DIR.exists()) {
				PHOTO_DIR.mkdirs();
			}
			headpath = PHOTO_DIR.getAbsolutePath();

			tbm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + headpath);
		}
		if (tbm == null) {
			if (frienduid == BAConstants.PEIPEI_XIAOPEI) {
				// 默认头像
				//				tbm = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_chat_user_3);
			} else {
				tbm = BitmapFactory.decodeResource(context.getResources(), R.drawable.main_img_defaulthead_pr);
			}
		}
		BitmapDrawable bdHeadL = new BitmapDrawable(context.getResources(), BaseBitmap.toRoundCorner(tbm, tbm.getWidth() / 2));
		return bdHeadL;
	}

	public synchronized static String saveFile(Context context, int fuid, long mid, byte[] data, boolean isImage) {
		String path_pic = null;
		if (isImage) {
			path_pic = BaseString.getFilePath(context, fuid, MessageType.IMAGE.getValue());
		} else {
			path_pic = BaseString.getFilePath(context, fuid, MessageType.VOICE.getValue());
		}
		path_pic = path_pic + File.separator + mid;
		path_pic = BaseFile.saveByteToFile(data, path_pic);
		return path_pic;
	}

	public static boolean clearDbMessage(Context context, int friendUid, boolean isGroupChat) {
		try {
			ChatOperate chatOperate = ChatOperate.getInstance(context, friendUid, isGroupChat);
			if (chatOperate != null)
				chatOperate.deleteTable();
			chatOperate = null;

			if(isGroupChat){
				RedpacketOperate redOperate = RedpacketOperate.getInstance(context, friendUid);
				if(redOperate != null){
					redOperate.deleteAll();
				}
				redOperate = null;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static void saveReceipt(Activity context, int friendUid, String msgId, String fNick, int fSex) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(context);
		if (userEntity == null) {
			return;
		}
		ReceiptOperate receiptOperate = ReceiptOperate.getInstance(context, userEntity.uid.intValue() + "");
		receiptOperate.insertReceipt(friendUid, msgId, fNick, new String(userEntity.nick), fSex, userEntity.sex.intValue());
	}

	public static List<ReceiptEntity> getReceiptList(Context context) {
		List<ReceiptEntity> list = new ArrayList<ReceiptEntity>();
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(context);
		if (userEntity == null) {
			return list;
		}

		ReceiptOperate receiptOperate = ReceiptOperate.getInstance(context, userEntity.uid.intValue() + "");
		if (!ListUtils.isEmpty(list)) {
			receiptOperate.selectReceiptList(list);
		}
		return list;
	}

	public static void deletReceipte(Context context, int friendUid, String burnId) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(context);
		if (userEntity == null) {
			return;
		}

		ReceiptOperate receiptOperate = ReceiptOperate.getInstance(context, userEntity.uid.intValue() + "");
		receiptOperate.deleteBurnId(friendUid, burnId);

		receiptOperate = null;
	}

	public static long currentTimeMillis = 0;
	public static boolean isOnLineMessageStart = false;

	@SuppressWarnings("deprecation")
	public static void saveNoticationMessage(Context context, int uid, int type) {
		PeipeiSessionOperate sessionOperate = PeipeiSessionOperate.getInstance(context);
		if (sessionOperate == null)
			return;
		int unread = sessionOperate.getUnreadCount(uid, type);
		sessionOperate.updateUnreadCount(unread + 1, uid, type);
		sessionOperate = null;
		NoticeEvent event1 = new NoticeEvent();
		event1.setFlag(NoticeEvent.NOTICE76);
		event1.setNum(ChatSessionManageBiz.isExistUnreadMessage(context));
		EventBus.getDefault().post(event1);
		int notification_num = 1;
		try {
			notification_num = SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKeyToZero(
					BAConstants.PEIPEI_NOTIFICATION_CHAT_NUM) + 1;
			SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "").saveIntKeyValue(notification_num,
					BAConstants.PEIPEI_NOTIFICATION_CHAT_NUM);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long currentTime = System.currentTimeMillis();

		if (!isOnLineMessageStart || currentTime - currentTimeMillis > 20000) {//不管如何都认为离线消息接收完了
			isOnLineMessageStart = false;
			//刷新界面
			int num = ChatSessionManageBiz.isExistUnreadMessage(context);
			if (num > 0) {
				NoticeEvent event = new NoticeEvent();
				event.setNum(num);
				event.setFlag(NoticeEvent.NOTICE68);
				EventBus.getDefault().post(event);
			}

			NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
			builder.setSmallIcon(R.drawable.logo);
			builder.setTicker(context.getString(R.string.chat_noticetion));
			Intent intent = new Intent(context, MainActivity.class);
			intent.putExtra("pushmessage", true);
			intent.putExtra("pushtype", PushMessageType.CHAT.getValue());
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent contentIntent = PendingIntent.getActivity(context, R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			builder.setContentTitle(context.getString(R.string.app_name));
			builder.setContentText(String.format(context.getString(R.string.chat_noticetion), notification_num));
			Notification n = builder.build();
			n.flags = Notification.FLAG_AUTO_CANCEL;
			if (currentTime - SharedPreferencesTools.getInstance(context).getLongKeyValue(BAConstants.PEIPEI_NOTIFICATION_CHAT_TIME) > 5000) {
				SharedPreferencesTools.getInstance(context).saveLongKeyValue(currentTime, BAConstants.PEIPEI_NOTIFICATION_CHAT_TIME);
				boolean sound = SharedPreferencesTools.getInstance(context).getBooleanKeyValue(BAConstants.SOUND);
				boolean shake = SharedPreferencesTools.getInstance(context).getBooleanKeyValue(BAConstants.SHAKE);
				if (!sound && !shake) {
					n.defaults = Notification.DEFAULT_ALL;
				} else if (!sound) {
					n.defaults = Notification.DEFAULT_SOUND;
				} else if (!shake) {
					n.defaults = Notification.DEFAULT_VIBRATE;
				} else {
					n.defaults = Notification.DEFAULT_LIGHTS;
				}
			}


			nm.cancel(NotificationManagerId.OTHER_CHAT);
			nm.notify(NotificationManagerId.OTHER_CHAT, n);
		}
	}

	public void addBlack(byte[] auth, int ver, int uid, int blackuid, IAddBlacklist callback) {
		RequestAddBlacklist req = new RequestAddBlacklist();
		req.addBlacklist(auth, ver, uid, blackuid, callback);
	}

	public List<String> getMessageByTpye(Context context, int friendUid, int type, boolean isGroupChat) {
		ChatOperate chatOperate = ChatOperate.getInstance(context, friendUid, isGroupChat);
		if (chatOperate != null)
			return chatOperate.getMessageList(type);
		else
			return new ArrayList<String>();
	}
}
