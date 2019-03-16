package com.tshang.peipei.model.broadcast;

import java.io.File;
import java.math.BigInteger;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.ProgressBar;

import com.ibm.asn1.ASN1Exception;
import com.ibm.asn1.BERDecoder;
import com.ibm.asn1.DEREncoder;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.base.json.GoGirlUserInfoFunctions;
import com.tshang.peipei.base.json.GoGirlUserJson;
import com.tshang.peipei.model.BaseLogic;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.request.RequestGetBroadcastListById;
import com.tshang.peipei.model.request.RequestGetBroadcastListById.IgetBroadcastListById;
import com.tshang.peipei.model.request.RequestGetVoice;
import com.tshang.peipei.model.request.RequestGetVoice.IGetVocie;
import com.tshang.peipei.model.request.RequestSetBroadcastPushSwitch;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil;
import com.tshang.peipei.network.socket.ThreadPoolService;
import com.tshang.peipei.storage.db.BroadCastColumn;
import com.tshang.peipei.storage.db.DBHelper;
import com.tshang.peipei.vender.common.util.ListUtils;

/**
 * @Title:广播逻辑
 *
 * (用一句话描述该文件做什么) 
 *
 * @author Jeff
 *
 * @date 2014年7月18日 下午3:52:13 
 *
 * @version V1.0   
 */
public class BroadCastBiz extends BaseLogic implements IgetBroadcastListById, IGetVocie {
	private static final String BRAODCAST_TOP_FEMALE_FILE_NAME_START = "BroadcastInfoListTop";
	private static final String BRAODCAST_ALL_MALE_FILE_NAME_START = "BroadcastInfoListAll";
	private static final String BROADCAST_GAME_FILE_NAME_START = "BroadcastInfoListGame";

	public static final int REFRESH_CODE = 1;
	public static final int LOADMORE_CODE = 2;

	public static final int ALL_BROADCAST_TYPE = -1;//所有广播
	public static final int TOP_BROADCAST_TYPE = 2;//置顶广播
	public static final int MY_BROADCAST_TYPE = 3;//我的广播  都从本地数据库取
	public static final int ABOUT_ME_BROADCAST_TYPE = 4;//@我的广播 都从本地数据库取
	public static final int GAME_BROADCAST_TYPE = 5;//游戏场

	public static final int MAX_INTEGER = 2100000000;
	private int startBroadcastId = MAX_INTEGER;//记录广播的加载位置
	private int startGameId = MAX_INTEGER;//记录游戏场的加载位置

	private static final int OPEN_BROADCAST_PUSH = 1;//打开广播消息推送
	private static final int CLOSE_BROADCAST_PUSH = 0;//关闭广播消息推送

	public BroadCastBiz(Activity activity, BAHandler mHandler) {
		super(activity, mHandler);
	}

	public void pushSwitch(boolean isOpen) {//type 0 表示广播开关，act 0:关闭；1：打开
		int act = OPEN_BROADCAST_PUSH;
		if (!isOpen) {
			act = CLOSE_BROADCAST_PUSH;
		}
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int uid = 0;
		if (userEntity != null) {
			auth = userEntity.auth;
			uid = userEntity.uid.intValue();
		}
		RequestSetBroadcastPushSwitch req = new RequestSetBroadcastPushSwitch();
		req.setBraodcastPushSwitch(auth, BAApplication.app_version_code, act, 0, uid);
	}

	private BroadcastInfoList getBroadcastInfoListCacheData(Activity activity, String fileNameStart) {
		File file = ConfigCacheUtil.getCacheFile(activity, fileNameStart);
		if (file != null) {
			byte[] contents = BaseFile.getBytesByFilePath(file);
			if (contents != null && contents.length != 0) {
				BroadcastInfoList list = new BroadcastInfoList();
				BERDecoder dec = new BERDecoder(contents);
				try {
					list.decode(dec);
					return list;
				} catch (ASN1Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		return null;
	}

	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 拉取广播列表
	 */
	public void getBroadCastList(final int type, int loadCount, boolean isRefresh) {
		if (!isNetworkConnected(activity)) {//网络不可用的时候使用本地数据
			ThreadPoolService.getInstance().execute(new Runnable() {

				@Override
				public void run() {
					if (type == TOP_BROADCAST_TYPE) {
						BroadcastInfoList toplists = getBroadcastInfoListCacheData(getActivity(), BRAODCAST_TOP_FEMALE_FILE_NAME_START);//读取缓存数据
						HandlerUtils.sendHandlerMessage(handler, HandlerValue.CACHE_BROADCAST_TOP_VALUE, 1, REFRESH_CODE, toplists);
					} else if (type == ALL_BROADCAST_TYPE) {
						BroadcastInfoList toplists = getBroadcastInfoListCacheData(getActivity(), BRAODCAST_ALL_MALE_FILE_NAME_START);//读取缓存数据
						HandlerUtils.sendHandlerMessage(handler, HandlerValue.CACHE_BRAODCAST_ALL_VALUE, 1, REFRESH_CODE, toplists);
					} else if (type == GAME_BROADCAST_TYPE) {
						BroadcastInfoList toplists = getBroadcastInfoListCacheData(getActivity(), BROADCAST_GAME_FILE_NAME_START);//读取缓存数据
						HandlerUtils.sendHandlerMessage(handler, HandlerValue.CACHE_BROADCAST_GAME_VALUE, 1, REFRESH_CODE, toplists);
					}

				}
			});
		} else {
			GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
			int uid = 0;
			byte[] auth = "".getBytes();
			if (userEntity != null) {
				uid = userEntity.uid.intValue();
				auth = userEntity.auth;
			}
			RequestGetBroadcastListById reqList = new RequestGetBroadcastListById();
			if (type == TOP_BROADCAST_TYPE) {
				reqList.getBroadcastList(auth, BAApplication.app_version_code, uid, type, MAX_INTEGER, -1, loadCount, isRefresh, this);
			} else if (type == ALL_BROADCAST_TYPE) {
				if (isRefresh) {
					startBroadcastId = MAX_INTEGER;
				}
				reqList.getBroadcastList(auth, BAApplication.app_version_code, uid, type, startBroadcastId, -1, loadCount, isRefresh, this);
			} else if (type == GAME_BROADCAST_TYPE) {
				if (isRefresh) {
					startGameId = MAX_INTEGER;
				}
				reqList.getBroadcastList(auth, BAApplication.app_version_code, uid, type, startGameId, -1, loadCount, isRefresh, this);
			}
		}
	}

	public void queryColumnName(Cursor cursor) {

		for (int i = 0; i < cursor.getColumnCount(); i++) {

			String columnName = cursor.getColumnName(i);

			//			System.out.println("数据库的列名===" + columnName);

		}

	}

	/**
	 * 
	 * @author Jeff
	 *
	 * @param status 1为我发送的，2为@我的
	 * @return
	 */
	public BroadcastInfoList getLocalBroadcast(String[] args, String whereStr, String limit) {
//		new String[] { status, String.valueOf(BAApplication.mLocalUserInfo.uid.intValue()) },
//		BroadCastColumn.STAUTS + "=? and " + BroadCastColumn.USERUID + "=?", limit
		
		
		Cursor cursor = DBHelper.getInstance(activity).query(BroadCastColumn.TABLE_NAME, null, whereStr, args, "createtime desc ", limit);//通过时间倒序
		BroadcastInfoList infoList = new BroadcastInfoList();
		if (cursor != null) {
			//			queryColumnName(cursor);
			try {
				if (cursor.moveToFirst()) {
					do {
						BroadcastInfo info = new BroadcastInfo();
						info.broadcasttype = BigInteger.valueOf(cursor.getInt(cursor.getColumnIndex(BroadCastColumn.TYPE)));
						info.contenttxt = cursor.getString(cursor.getColumnIndex(BroadCastColumn.CONTNET)).getBytes();
						info.createtime = BigInteger.valueOf(Long.parseLong(cursor.getString(cursor.getColumnIndex(BroadCastColumn.CREATETIME))));
						String voiceInfo = cursor.getString(cursor.getColumnIndex(BroadCastColumn.REVINT));
						if (TextUtils.isEmpty(voiceInfo)) {
							voiceInfo = "";
						}
						info.voiceinfo = voiceInfo.getBytes();
						GoGirlDataInfo datainfo = new GoGirlDataInfo();
						int typeColor = MessageType.BROADCASTCOLOR.getValue();
						try {
							typeColor = cursor.getInt(cursor.getColumnIndex(BroadCastColumn.REVINT1));
						} catch (Exception e) {
							e.printStackTrace();

						}
						if (typeColor == 0) {//兼容老数据
							typeColor = MessageType.BROADCASTCOLOR.getValue();
						}

						datainfo.type = BigInteger.valueOf(typeColor);
						int textColor = 0;
						try {
							textColor = cursor.getInt(cursor.getColumnIndex(BroadCastColumn.REVINT0));
						} catch (Exception e) {
							e.printStackTrace();
							textColor = 0;
						}
						datainfo.datainfo = BigInteger.valueOf(textColor);
						datainfo.dataid = "".getBytes();
						datainfo.revint0 = BigInteger.valueOf(0);
						datainfo.revint1 = BigInteger.valueOf(0);
						datainfo.revstr0 = "".getBytes();
						datainfo.revstr1 = "".getBytes();
						datainfo.data = info.contenttxt;
						GoGirlDataInfoList goList = new GoGirlDataInfoList();
						if (TextUtils.isEmpty(voiceInfo)) {
							goList.add(datainfo);
							DEREncoder encoder = new DEREncoder();
							goList.encode(encoder);
							info.datalist = encoder.toByteArray();
						}

						String strTouser = cursor.getString(cursor.getColumnIndex(BroadCastColumn.TOUSER));
						if (!TextUtils.isEmpty(strTouser)) {
							GoGirlUserInfoList userinfoList = new GoGirlUserInfoList();
							JSONArray jsonArray = null;
							if (strTouser.startsWith("[")) {//兼容之前的版本，之前的拼接直接以数组开头的数据
								jsonArray = new JSONArray(strTouser);
							} else {
								JSONObject jObject = new JSONObject(strTouser);
								if (!jObject.isNull("user")) {
									jsonArray = jObject.getJSONArray("user");
								}
							}
							if (jsonArray != null) {
								GoGirlUserInfo[] infos = GoGirlUserInfoFunctions.getGoGirlUserInfo(jsonArray);
								if (infos != null && infos.length != 0) {
									for (GoGirlUserInfo goGirlUserInfo : infos) {
										userinfoList.add(goGirlUserInfo);
									}
									info.tousers = userinfoList;
								}
							}
						}
						String strSenduser = cursor.getString(cursor.getColumnIndex(BroadCastColumn.SENDUSER));
						GoGirlUserInfo goInfo = GoGirlUserJson.getGoGirlUserInfo(strSenduser);
						if (goInfo != null) {
							info.senduser = goInfo;
						}

						infoList.add(info);
					} while (cursor.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null && !cursor.isClosed()) {
					cursor.close();
				}
			}
		}
		if (!ListUtils.isEmpty(infoList)) {
			Collections.reverse(infoList);
		}
		return infoList;
	}

	private void setBroadcastInfoListCacheData(String fileNameStart, BroadcastInfoList infoList) {//缓存广播数据

		if (infoList == null || infoList.isEmpty()) {
			return;
		}
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info != null) {
			DEREncoder edc = new DEREncoder();
			try {
				infoList.encode(edc);
				byte[] data = edc.toByteArray();
				ConfigCacheUtil.setUrlCache(activity, data, fileNameStart + info.uid.intValue());
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
	}

	/**
	 * 拉去到的广播列表数据
	 */
	@Override
	public void getBroadcastList(int retCode, int isEnd, int type, boolean isRefresh, BroadcastInfoList list) {
		if (retCode == 0) {
			if (list != null && !list.isEmpty()) {
				BroadcastInfo info = (BroadcastInfo) list.get(0);
				if (type == ALL_BROADCAST_TYPE) {
					int code = LOADMORE_CODE;
					startBroadcastId = info.id.intValue() - 1;
					if (isRefresh) {//刷新保存数据
						code = REFRESH_CODE;
						setBroadcastInfoListCacheData(BRAODCAST_ALL_MALE_FILE_NAME_START, list);
					}
					sendHandlerMessage(HandlerValue.MAIN_BROADCAST_ALL_LIST_SUCCESS, isEnd, code, list);
				} else if (type == GAME_BROADCAST_TYPE) {
					int code = LOADMORE_CODE;
					if (isRefresh) {//刷新保存数据
						code = REFRESH_CODE;
						setBroadcastInfoListCacheData(BROADCAST_GAME_FILE_NAME_START, list);
					}
					startGameId = info.id.intValue() - 1;
					sendHandlerMessage(HandlerValue.MAIN_BROADCAST_GAME_LIST_SUCCESS, isEnd, code, list);
				} else if (type == TOP_BROADCAST_TYPE) {
					int code = LOADMORE_CODE;
					if (isRefresh) {//刷新保存数据
						code = REFRESH_CODE;
						setBroadcastInfoListCacheData(BRAODCAST_TOP_FEMALE_FILE_NAME_START, list);
					}
					sendHandlerMessage(HandlerValue.MAIN_BROADCAST_TOP_LIST_SUCCESS, isEnd, code, list);
				}
			} else {
				if (type == ALL_BROADCAST_TYPE) {
					sendHandlerMessage(HandlerValue.MAIN_BROADCAST_ALL_LIST_FAILED, isEnd, type, list);
				} else if (type == GAME_BROADCAST_TYPE) {
					sendHandlerMessage(HandlerValue.MAIN_BROADCAST_GAME_LIST_FAILED, isEnd, type, list);
				} else if (type == TOP_BROADCAST_TYPE) {
					sendHandlerMessage(HandlerValue.MAIN_BROADCAST_TOP_LIST_FAILED, isEnd, type, list);
				}
			}
		} else {
			if (type == ALL_BROADCAST_TYPE) {
				sendHandlerMessage(HandlerValue.MAIN_BROADCAST_ALL_LIST_FAILED, isEnd, type, list);
			} else if (type == GAME_BROADCAST_TYPE) {
				sendHandlerMessage(HandlerValue.MAIN_BROADCAST_GAME_LIST_FAILED, isEnd, type, list);
			} else if (type == TOP_BROADCAST_TYPE) {
				sendHandlerMessage(HandlerValue.MAIN_BROADCAST_TOP_LIST_FAILED, isEnd, type, list);
			}
		}
	}

	private int playVoiceType = ItemVoiceClickListener.VOICE_COMMON_BC;//区分播放语音广播的类型
	private ProgressBar pb;//显示下载语音过程中的进度条

	public void getVoiceByKey(ProgressBar pb, byte[] key, int playVoiceType) {
		this.playVoiceType = playVoiceType;
		this.pb = pb;
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		int uid = 0;
		byte[] auth = "".getBytes();
		if (userEntity != null) {
			uid = userEntity.uid.intValue();
			auth = userEntity.auth;
		}
		RequestGetVoice requestGetVoice = new RequestGetVoice();
		requestGetVoice.setData(uid, 0);
		requestGetVoice.getVoice(auth, BAApplication.app_version_code, key, this);
	}

	@Override
	public void getVocie(int retCode, byte[] data, long id, int friendUid, String fileName) {//下载广播语音
		sendHandlerMessage(HandlerValue.BROADCAST_VOIDE_PB_GONE_VALUE, pb);
		if (retCode == 0) {//说明成功
			if (data != null) {
				String path = SdCardUtils.getInstance().saveFile(data, 0, fileName);
				if (!TextUtils.isEmpty(path)) {
					sendHandlerMessage(HandlerValue.BROADCAST_VOIDE_LOAD_COMPLETE_PLAY_VALUE, playVoiceType, path);
				}
			}
		}
	}
}
