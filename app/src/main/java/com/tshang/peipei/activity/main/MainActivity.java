package com.tshang.peipei.activity.main;

import java.io.File;
import java.util.List;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.baidu.autoupdatesdk.CPUpdateDownloadCallback;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.FragmentTabHost;
import com.tshang.peipei.activity.account.LoginActivity;
import com.tshang.peipei.activity.dialog.DayTaskDialog;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.dialog.UpdateApkDialog;
import com.tshang.peipei.activity.dialog.UpdateApkForceDialog;
import com.tshang.peipei.activity.main.message.MainMessageFragment;
import com.tshang.peipei.activity.mine.MineFaqActivity;
import com.tshang.peipei.activity.suspension.SuspensionActivity;
import com.tshang.peipei.base.BaseBitmap;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.ISystemTool;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAConstants.PushMessageType;
import com.tshang.peipei.base.babase.BAConstants.UpdateType;
import com.tshang.peipei.base.babase.UserSharePreference;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.base.json.GoGirlUserJson;
import com.tshang.peipei.model.biz.PeiPeiAppStartBiz;
import com.tshang.peipei.model.biz.PeiPeiPersistBiz;
import com.tshang.peipei.model.biz.baseviewoperate.OperateViewUtils;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.ChatManageBiz;
import com.tshang.peipei.model.biz.chat.ChatRecordBiz;
import com.tshang.peipei.model.biz.chat.ChatSessionManageBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetLastestAppInfo;
import com.tshang.peipei.model.bizcallback.BizCallBackSentChatMessage;
import com.tshang.peipei.model.entity.ChatMessageReceiptEntity;
import com.tshang.peipei.model.entity.SuspensionActEntity;
import com.tshang.peipei.model.entity.SuspensionEntity;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.showrooms.RoomsGetBiz;
import com.tshang.peipei.model.suspension.RequestSuspInfo;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.network.socket.ThreadPoolService;
import com.tshang.peipei.protocol.Gogirl.ShowRoomInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.LoginRewardInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetLatestAppInfo;
import com.tshang.peipei.protocol.protobuf.InvalidProtocolBufferException;
import com.tshang.peipei.service.PeipeiFloatingService;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ReceiptEntity;
import com.tshang.peipei.storage.db.DBHelper;
import com.tshang.peipei.vender.common.util.ListUtils;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.assist.FailReason;
import com.tshang.peipei.vender.imageloader.core.listener.ImageLoadingListener;

import de.greenrobot.event.EventBus;

/**
 * @Title: MainActivity
 *
 * @Description: 主界面类，包含4个fragment 
 *     测试帐号 j123@163.com  j1234@163.com  j12345@163.com kl@163.com
 * @author Jeff
 *
 * @version V1.0   
 */
public class MainActivity extends BaseActivity implements BizCallBackGetLastestAppInfo, BizCallBackSentChatMessage {

	private final static int RELOADACTIVITY = 0x52;
	private final static int BOTTOM_APHLA_IMAGE_VISIBLE = 0x57;
	private final static int BOTTOM_APHLA_IMAGE_INVISIBLE = 0x58;
	private final static int MESSAGE_FINISH = 0X59;

	private FragmentTabHost mTabHost;
	private static final String TAB_MINE = "tabmine";
	private static final String TAB_MESSAGE = "tabmessage";
	private static final String TAB_BROADCAST = "tabbroadcast";
	private static final String TAB_RANK = "tabrank";
	private static final String TAB_HALL = "tabhall";

	private LinearLayout ll_bottom;
	private RelativeLayout rl_bottom_mine;
	private RelativeLayout rl_bottom_message;
	private RelativeLayout rl_bottom_broadcast;
	private RelativeLayout rl_bottom_rank;
	private RelativeLayout rl_bottom_hall;
	private TextView tv_bottom_mine;
	private TextView tv_bottom_message;
	private TextView tv_bottom_broadcast;
	private TextView tv_bottom_rank;
	private TextView tv_bottom_hall;
	private ImageView iv_bottom_aphla;

	private long mExit = 0;

	private TextView mTextViewMsg;
	private ImageView mTextViewGift;
	private ImageView mTextViewActivity;
	private TextView tvBroadcastCount;
	private int selectBottomCurrentPos = 0;

	private int cur_view; //当前MainActivity显示的View，用来处理显示悬浮窗的逻辑
	public static final int NON_SUSPENSION = -1; //无悬浮穿显示的界面

	private ImageView iv_susp_icon;
	private ImageView iv_close;
	private FrameLayout rl_susp;
	private float density;
	private SuspensionActEntity actEntify;
	private boolean enterOpen = true;

	private Dialog dialog;

	@SuppressWarnings("unchecked")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerType.CREATE_GETDATA_BACK:
			if (msg.arg1 == 0) {
				if (msg.obj instanceof RspGetLatestAppInfo) {
					RspGetLatestAppInfo rsp = (RspGetLatestAppInfo) msg.obj;
					String url = new String(rsp.updateurl);
					String title = new String(rsp.updatedesc);
					title = title.replaceAll("#", "\n");
					SharedPreferencesTools.getInstance(MainActivity.this).saveIntKeyValue(rsp.latestappver.intValue(), BAConstants.UPDATE_VER);
					if (!BAApplication.Channel.equals("and-baidu")) {
						if (rsp.updatelevel.intValue() == UpdateType.UPDATE_LEVEL_NO.getValue()) {
						} else if (rsp.updatelevel.intValue() == UpdateType.UPDATE_LEVEL_OPTIONAL.getValue()) {
							new UpdateApkDialog(this, 0, R.string.ok, R.string.cancel, url, title).showDialog();
						} else if (rsp.updatelevel.intValue() == UpdateType.UPDATE_LEVEL_FORCE.getValue()) {
							new UpdateApkForceDialog(MainActivity.this, url, title).showDialog();
						}
					}
					SharedPreferencesTools.getInstance(this).saveIntKeyValue(rsp.latestappver.intValue(), "latestappver");
					SharedPreferencesTools.getInstance(this).saveStringKeyValue(url, "updataurl");
					SharedPreferencesTools.getInstance(this).saveStringKeyValue(title, "updatatitle");
					SharedPreferencesTools.getInstance(this).saveIntKeyValue(rsp.updatelevel.intValue(), "updatalevel");
				}
			}
			break;
		case HandlerValue.MAIN_LOGIN_SHOW_REWARD:
			int loginDay = msg.arg1;
			if (loginDay > 0) {
				LoginRewardInfoList lists = (LoginRewardInfoList) msg.obj;
				if (!ListUtils.isEmpty(lists)) {
					if (ListUtils.getSize(lists) == 7) {
						DayTaskDialog dialog = new DayTaskDialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar, loginDay, lists);
						dialog.showDialog();
						BAApplication.lists = null;
						BAApplication.loginDay = 0;
					}
				}
			}
			break;
		case HandlerType.LOGIN_OTHER:
			BaseUtils.showTost(this, "您已在其他机器上登录");
			UserSharePreference.getInstance(MainActivity.this).removeUserByKey();
			BAApplication.mLocalUserInfo = null;
			try {
				if (BAApplication.mQQAuth != null) {
					BAApplication.mQQAuth.logout(MainActivity.this);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			BaseUtils.openActivity(this, LoginActivity.class);
			finish();
			break;
		case HandlerValue.MAIN_LOGIN_OUT_VALUE:
			//			ThreadPoolService.getInstance().execute(new Runnable() {
			//
			//				@Override
			//				public void run() {
			//					PeiPeiPersistBiz.getInstance().closePersist(MainActivity.this);
			//					ThreadPoolService.getInstance().shutdown(1000);
			//					PeiPeiRequest.close();
			//					//关闭线程池
			//					AppQueueManager.getInstance().shutdownWork();
			//					BAApplication.isOnLine = false;
			//					BAApplication.isCreateLongConnectedSuccess = false;
			//				}
			//			});

			selectBottomCurrentPos = 0;
			BAApplication.isCreateLongConnectedSuccess = false;
			setBottomSlect(selectBottomCurrentPos);
			mTabHost.setCurrentTabByTag(TAB_HALL);
			setBroadCastNum();
			setGiftNum();
			setActivityNum();
			setMessageCount();
			BAApplication.getInstance().exit();
			BaseUtils.openActivity(this, LoginActivity.class);
			//			finish();
			break;
		case HandlerType.GIFT_MESSAGE:
			setGiftNum();
			break;
		case HandlerValue.MAIN_CHAT_MESSAGE_OTHER:
			mTabHost.setCurrentTabByTag(TAB_MESSAGE);
			selectBottomCurrentPos = 1;
			setBottomSlect(selectBottomCurrentPos);
			break;
		case HandlerType.SENT_RECEIPT_CHECK:
			List<ReceiptEntity> list = ChatRecordBiz.getReceiptList(MainActivity.this);
			if (list.size() > 0) {
				if (BAApplication.mLocalUserInfo != null) {
					for (ReceiptEntity entity : list) {
						ChatManageBiz.getInManage(this).sentMsg(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
								BAApplication.mLocalUserInfo.uid.intValue(), "".getBytes(), MessageType.RECEIPT.getValue(), 0, entity.getFromID(),
								entity.getMesSvrID(), entity.getNick(), entity.getFNick(), entity.getSex(), entity.getFSex(), this, 0,0);
					}
				}
			}
			break;
		case HandlerValue.MAIN_RECEIVE_BROADCAST_COUNT:
			setBroadCastNum();
			break;
		case RELOADACTIVITY://
			restoreUI(true);
			sendNoticeEvent(NoticeEvent.NOTICE58);
			break;
		case HandlerValue.MAIN_LOGIN_BACK_VALUE:
			ll_bottom.setVisibility(View.INVISIBLE);
			mTextViewGift.setVisibility(View.GONE);
			break;
		case MESSAGE_FINISH:
			ll_bottom.setVisibility(View.VISIBLE);
			setGiftNum();
			break;
		case HandlerValue.MAIN_MESSAGE_COUNT:
			int count = (Integer) msg.obj;
			int unread = SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey(
					BAConstants.PEIPEI_DYNAMIC_UNREAD_NUM, 0);
			int fansNum = SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey(
					BAConstants.PEIPEI_FANS_UNREAD_NUM, 0);

			OperateViewUtils.setTextViewShowCount(mTextViewMsg, count + unread + fansNum, true, selectBottomCurrentPos == 1);
			break;
		case BOTTOM_APHLA_IMAGE_VISIBLE:
			iv_bottom_aphla.setVisibility(View.VISIBLE);
			break;
		case BOTTOM_APHLA_IMAGE_INVISIBLE:
			iv_bottom_aphla.setVisibility(View.GONE);
			break;
		case HandlerType.REFRESH_MESSAGE_NUM:
			setMessageCount();
			break;
		case HandlerValue.SHOW_ROOM_GET_SINGLE_ROOM:
			if (msg.arg1 == 0) {
				ShowRoomInfo roomInfo = (ShowRoomInfo) msg.obj;
				if (roomInfo.getLefttime() > 0) {
					BAApplication.showRoomInfo = roomInfo;
				} else {
//					Intent intent = new Intent(this, PeipeiFloatingService.class);
//					stopService(intent);
					BAApplication.clearShow();
				}
			}
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		density = metric.density;
		GoGirlUserInfo info = UserUtils.getUserEntity(this);
		if (info == null) {
			BaseUtils.openActivity(this, LoginActivity.class);
			SuccessFinish();
			return;
		}

		if (!BAApplication.isLogin && BAConstants.IS_ACTIVITY && BAApplication.Channel.equals("and-xiaomisd")
				&& !SharedPreferencesTools.getInstance(this).getBooleanKeyValue(BAConstants.PEIPEI_WDJ_HUODONG_REGISTER)) {
			SharedPreferencesTools.getInstance(this).saveBooleanKeyValue(true, BAConstants.PEIPEI_WDJ_HUODONG_REGISTER);
			//新用户&豌豆荚活动&配置为显示
			MineFaqActivity.openMineFaqActivity(this, MineFaqActivity.ACTIVITY_XIAOMI);
		}

		initDefault();
		setBottomShow();

		mHandler.sendEmptyMessage(HandlerType.SENT_RECEIPT_CHECK);
		mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.MAIN_LOGIN_SHOW_REWARD, BAApplication.loginDay, BAApplication.loginDay,
				BAApplication.lists));//每日任务领取
		//百度渠道静默更新
		if (BAApplication.Channel.equals("and-baidu")) {
			if (ISystemTool.isWiFi(this)) {
				BDAutoUpdateSDK.silenceUpdateAction(this);
			} else {
				BDAutoUpdateSDK.cpUpdateCheck(this, new CPCheckUpdateCallback() {

					@Override
					public void onCheckUpdateCallback(final AppUpdateInfo info, AppUpdateInfoForInstall infoForInstall) {
						if (infoForInstall != null && !TextUtils.isEmpty(infoForInstall.getInstallPath())) {//安装包已经下载,下载安装
							BDAutoUpdateSDK.cpUpdateInstall(getApplicationContext(), infoForInstall.getInstallPath());
						} else if (info != null) {//有新版本
							dialog = DialogFactory.showMsgDialog(MainActivity.this, "版本信息", info.getAppChangeLog().replaceAll("<br>", "\n")
									.replaceAll(" ", "").trim(), "更新", "取消", new OnClickListener() {

								@Override
								public void onClick(View v) {
									DialogFactory.dimissDialog(dialog);
									BDAutoUpdateSDK.cpUpdateDownload(MainActivity.this, info, new UpdateDownloadCallback());
								}
							}, null);
						}
					}
				});
			}
		}
	}

	private class UpdateDownloadCallback implements CPUpdateDownloadCallback {
		private View view;
		private ProgressBar progressBar;

		boolean isCancel = false;

		public UpdateDownloadCallback() {
			view = LayoutInflater.from(MainActivity.this).inflate(R.layout.down_progress_bar_layout, null);
			progressBar = (ProgressBar) view.findViewById(R.id.down_progress_bar);
		}

		@Override
		public void onDownloadComplete(String apkPath) {
			DialogFactory.dimissDialog(dialog);
			if (isCancel)
				return;
			BDAutoUpdateSDK.cpUpdateInstall(getApplicationContext(), apkPath);

		}

		@Override
		public void onStart() {
			dialog = DialogFactory.showMsgDialog(MainActivity.this, "", view, false, "取消", "", null, new OnClickListener() {

				@Override
				public void onClick(View v) {
					isCancel = true;
					DialogFactory.dimissDialog(dialog);
				}
			});
		}

		@Override
		public void onPercent(int percent, long rcvLen, long fileSize) {
			Log.d("Aaron", "p===" + percent + "%");
			progressBar.setProgress(percent);
		}

		@Override
		public void onFail(Throwable error, String content) {
			BaseUtils.showTost(MainActivity.this, "下载失败");
		}

		@Override
		public void onStop() {}

	}

	/**
	 * 获取悬浮配制数据
	 *
	 */
	private void requstSuspensionData() {
		RequestSuspInfo.requstSuspensionData(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			//			if (BAApplication.mLocalUserInfo != null)
			//				DMOfferWall.getInstance(this).setUserId(BAApplication.mLocalUserInfo.uid.intValue() + "");

			NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancelAll();
			SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").saveIntKeyValue(0,
					BAConstants.PEIPEI_NOTIFICATION_PUSH_CHAT_NUM);

			File file = ConfigCacheUtil.getCacheFile(this, "roominfo");
			if (file != null) {
				byte[] contents = BaseFile.getBytesByFilePath(file);
				if (contents != null && contents.length != 0) {
					try {
						final ShowRoomInfo showRoomInfo = ShowRoomInfo.parseFrom(contents);

						if (showRoomInfo != null) {
							RoomsGetBiz roomsGetBiz = new RoomsGetBiz(this, mHandler);
							roomsGetBiz.getSingleRoomInfo(showRoomInfo.getRoomid(), showRoomInfo.getOwneruserinfo().getUid());

							imageLoader.loadImage("http://" + showRoomInfo.getOwneruserinfo().getUid() + BAConstants.LOAD_HEAD_UID_APPENDSTR,
									new ImageLoadingListener() {

										@Override
										public void onLoadingStarted(String imageUri, View view) {}

										@Override
										public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
											//											BAApplication.showImage = BitmapFactory.decodeResource(getResources(), R.drawable.casino_xiu_moren);
											//
											//											BAApplication.showRoomInfo = showRoomInfo;
											//											Intent intent = new Intent(MainActivity.this, PeipeiFloatingService.class);
											//											startService(intent);
										}

										@Override
										public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
											if (loadedImage == null) {
												BAApplication.showImage = BitmapFactory.decodeResource(getResources(), R.drawable.casino_xiu_moren);
											} else {
												BAApplication.showImage = BaseBitmap.toRoundCorner(loadedImage,
														BaseUtils.dip2px(MainActivity.this, 44));
											}

											BAApplication.showRoomInfo = showRoomInfo;
//											Intent intent = new Intent(MainActivity.this, PeipeiFloatingService.class);
//											startService(intent);
										}

										@Override
										public void onLoadingCancelled(String imageUri, View view) {}
									});

						}
					} catch (InvalidProtocolBufferException e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		restoreUI(false);
//		requstSuspensionData();
	}

	private void restoreUI(boolean isShowBottom) {
		if (isShowBottom) {
			setBottomShow();
		}
		Intent intent = getIntent();
		if (intent.getBooleanExtra("pushmessage", false)) {
			if (PushMessageType.CHAT.getValue() == intent.getIntExtra("pushtype", -1)) {
				mTabHost.setCurrentTabByTag(TAB_MESSAGE);
				setBottomSlect(1);
			}
			intent.putExtra("pushmessage", false);
		}

		setIntent(intent);
		setMessageCount();
		setBroadCastNum();
		setGiftNum();
		setActivityNum();
	}

	private void setBroadCastNum() {
		if (tvBroadcastCount != null) {
			if (BAApplication.mLocalUserInfo != null) {
				int count = SharedPreferencesTools.getInstance(this).getIntValueByKeyToZero(
						GoGirlUserJson.BRAODCAST + BAApplication.mLocalUserInfo.uid.intValue());
				OperateViewUtils.setTextViewShowCount(tvBroadcastCount, count, true, selectBottomCurrentPos == 2);
			} else {
				tvBroadcastCount.setVisibility(View.GONE);
			}
		}
	}

	private void setGiftNum() {
		if (BAApplication.mLocalUserInfo != null) {
			OperateViewUtils.setTextViewShowCount(this, mTextViewGift, selectBottomCurrentPos == 4);
		} else {
			mTextViewGift.setVisibility(View.GONE);
		}
	}

	private void setActivityNum() {
		if (BAApplication.mLocalUserInfo != null) {
			OperateViewUtils.setTextViewActivityCount(this, mTextViewActivity, selectBottomCurrentPos == 3);
		} else {
			mTextViewActivity.setVisibility(View.GONE);
		}
	}

	private void setMessageCount() {
		//如果该对象存在,则表示已经登录,若在其它地方登录后,要删除该对象
		boolean loginFlag = BAApplication.mLocalUserInfo == null ? false : true;

		if (loginFlag) {
			int num = ChatSessionManageBiz.isExistUnreadMessage(this); //刷新界面
			int unread = SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey(
					BAConstants.PEIPEI_DYNAMIC_UNREAD_NUM, 0);
			int fansNum = SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey(
					BAConstants.PEIPEI_FANS_UNREAD_NUM, 0);

			OperateViewUtils.setTextViewShowCount(mTextViewMsg, num + unread + fansNum, true, selectBottomCurrentPos == 1);
		} else {
			mTextViewMsg.setVisibility(View.GONE);
		}
	}

	private void setBottomShow() {
		setMessageCount();
		setBottomSlect(selectBottomCurrentPos);
		switch (selectBottomCurrentPos) {
		case 0:
			mTabHost.setCurrentTabByTag(TAB_HALL);
			break;
		case 1:
			mTabHost.setCurrentTabByTag(TAB_MESSAGE);
			break;
		case 2:
			mTabHost.setCurrentTabByTag(TAB_BROADCAST);
			break;
		case 3:
			mTabHost.setCurrentTabByTag(TAB_RANK);
			break;
		case 4:
			mTabHost.setCurrentTabByTag(TAB_MINE);
			break;
		default:
			mTabHost.setCurrentTabByTag(TAB_HALL);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			long time = System.currentTimeMillis();
			if (time - mExit > 2000) {
				mExit = time;
				BaseUtils.showTost(this, R.string.toast_exit_txt);
			} else {
				try {
					imageLoader.clearMemoryCache();
				} catch (Exception e) {
					e.printStackTrace();
				}
				ThreadPoolService.getInstance().execute(new Runnable() {

					@Override
					public void run() {
						PeiPeiPersistBiz.getInstance().closePersist(MainActivity.this);
						ThreadPoolService.getInstance().shutdown(1000);
						PeiPeiRequest.close();
						//关闭线程池
						AppQueueManager.getInstance().shutdownWork();
						BAApplication.isOnLine = false;
						BAApplication.isCreateLongConnectedSuccess = false;
					}
				});
				//清空所有event
				EventBus.getDefault().removeAllStickyEvents();
//				Intent intent = new Intent(this, PeipeiFloatingService.class);
//				stopService(intent);
				DBHelper.getInstance(MainActivity.this).closeDb();
				System.gc();
				BAApplication.getInstance().exit();
				System.exit(0);
			}

		}
		return false;
	}

	private void initDefault() {
		if (BaseTimes
				.isTimeDistanceNow(SharedPreferencesTools.getInstance(this).getLongKeyValue(BAConstants.PEIPEI_UPADTE_TIME), 60 * 60 * 24 * 1000)) {
			SharedPreferencesTools.getInstance(this).saveLongKeyValue(System.currentTimeMillis(), BAConstants.PEIPEI_UPADTE_TIME);
			PeiPeiAppStartBiz appStartBiz = new PeiPeiAppStartBiz();
			appStartBiz.checkAppInfo(this, this);
		} else {
			int code = SharedPreferencesTools.getInstance(this).getIntValueByKey("latestappver");
			int level = SharedPreferencesTools.getInstance(this).getIntValueByKey("updatalevel");
			String title = SharedPreferencesTools.getInstance(this).getStringValueByKey("updatatitle");
			String url = SharedPreferencesTools.getInstance(this).getStringValueByKey("updataurl");
			//百度渠道不处理
			if (!BAApplication.Channel.equals("and-baidu")) {
				if (code > BAApplication.app_version_code) {
					if (level == UpdateType.UPDATE_LEVEL_NO.getValue()) {
					} else if (level == UpdateType.UPDATE_LEVEL_OPTIONAL.getValue()) {
						new UpdateApkDialog(this, 0, R.string.ok, R.string.cancel, url, title).showDialog();
					} else if (level == UpdateType.UPDATE_LEVEL_FORCE.getValue()) {
						new UpdateApkForceDialog(MainActivity.this, url, title).showDialog();
					}
				}
			}
		}

		if (BaseTimes.isTimeDistanceNow(SharedPreferencesTools.getInstance(this).getLongKeyValue(BAConstants.PEIPEI_BAIDU_TIME), 60 * 60 * 24 * 1000)) {
			String token = SharedPreferencesTools.getInstance(this).getStringValueByKey(BAConstants.PEIPEI_BAIDU_USERID);
			new PeiPeiAppStartBiz().reportAppInfoToSer(this, token, null);
		}

		GoGirlUserInfo mEntity = UserUtils.getUserEntity(this);
		if (mEntity != null) {
			if (SharedPreferencesTools.getInstance(this, mEntity.uid.intValue() + "").getLongKeyValue(BAConstants.PEIPEI_INTERESTED) == 0) {
				SharedPreferencesTools.getInstance(this, mEntity.uid.intValue() + "").saveLongKeyValue(System.currentTimeMillis(),
						BAConstants.PEIPEI_INTERESTED);
			}
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		case R.id.rl_bottom_mine:
			selectBottomCurrentPos = 4;
			mTabHost.setCurrentTabByTag(TAB_MINE);
			setBottomSlect(4);
			setNumBg();
			break;
		case R.id.rl_bottom_message:
			selectBottomCurrentPos = 1;
			mTabHost.setCurrentTabByTag(TAB_MESSAGE);
			setBottomSlect(1);
			setNumBg();
			break;
		case R.id.rl_bottom_broadcast:
			selectBottomCurrentPos = 2;
			mTabHost.setCurrentTabByTag(TAB_BROADCAST);
			setBottomSlect(2);
			setNumBg();
			break;
		case R.id.rl_bottom_rank:
			selectBottomCurrentPos = 3;
			mTabHost.setCurrentTabByTag(TAB_RANK);
			setBottomSlect(3);
			setNumBg();
			break;
		case R.id.rl_bottom_hall:
			selectBottomCurrentPos = 0;
			setBottomSlect(0);
			mTabHost.setCurrentTabByTag(TAB_HALL);
			setNumBg();
			break;
		case R.id.iv_main_bottom_alpha:
			sendNoticeEvent(NoticeEvent.NOTICE61);
			break;
		case R.id.iv_susp_icon:
			enterInEnter();
			break;
		case R.id.iv_close:
			hideEnter();
			break;

		default:
			break;
		}
	}

	private void setNumBg() {
		setMessageCount();
		setBroadCastNum();
		setGiftNum();
		setActivityNum();
	}

	private void enterInEnter() {
		if (enterOpen) {
			if (actEntify != null) {
				SuspensionActivity.openMineFaqActivity(this, actEntify.getUrl());
			}
		} else {
			showEnter();
		}
	}

	private void showEnter() {
		hideSuspensionEnter(rl_susp, 0);
		enterOpen = true;
	}

	private void hideEnter() {
		hideSuspensionEnter(rl_susp, -32);
		enterOpen = false;
	}

	private void hideSuspensionEnter(View v, int margin) {
		ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
		lp.rightMargin = (int) (density * margin);
		v.setLayoutParams(lp);
	}

	@SuppressWarnings("unchecked")
	public void onEvent(NoticeEvent event) {
		super.onEvent(event);
		if (event.getFlag() == NoticeEvent.NOTICE10) {
			mHandler.sendEmptyMessage(HandlerValue.MAIN_LOGIN_BACK_VALUE);
		}
		if (event.getFlag() == NoticeEvent.NOTICE11) {
			mHandler.sendEmptyMessage(MESSAGE_FINISH);
		}
		if (event.getFlag() == NoticeEvent.NOTICE27) {
			mHandler.sendEmptyMessage(HandlerType.LOGIN_OTHER);
		}

		if (event.getFlag() == NoticeEvent.NOTICE28) {
			mHandler.sendEmptyMessage(HandlerType.GIFT_MESSAGE);
		}
		if (event.getFlag() == NoticeEvent.INOTICE_01) {//登录注册回来
			mHandler.sendEmptyMessage(RELOADACTIVITY);
		}
		if (event.getFlag() == NoticeEvent.NOTICE52) {//收到了@我的广播
			mHandler.sendEmptyMessage(HandlerValue.MAIN_RECEIVE_BROADCAST_COUNT);
		}
		if (event.getFlag() == NoticeEvent.NOTICE59) {//底部菜单变灰
			mHandler.sendEmptyMessage(BOTTOM_APHLA_IMAGE_VISIBLE);
		}
		if (event.getFlag() == NoticeEvent.NOTICE60) {//去掉底部菜单变灰
			mHandler.sendEmptyMessage(BOTTOM_APHLA_IMAGE_INVISIBLE);
		}
		if (event.getFlag() == NoticeEvent.NOTICE63) {//注销了用户
			mHandler.sendEmptyMessage(HandlerValue.MAIN_LOGIN_OUT_VALUE);
		}
		if (event.getFlag() == NoticeEvent.NOTICE50) {
			int loginDay = event.getNum();
			List<ReceiptEntity> list = (List<ReceiptEntity>) event.getObj();
			mHandler.sendMessage(mHandler.obtainMessage(HandlerType.LOGIN_SHOW_REWARD, loginDay, loginDay, list));
		}
		if (event.getFlag() == NoticeEvent.NOTICE67) {
			mHandler.sendEmptyMessage(HandlerType.REFRESH_MESSAGE_NUM);
		}
		if (event.getFlag() == NoticeEvent.NOTICE68) {
			mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.MAIN_MESSAGE_COUNT, event.getNum()));
		}
		if (event.getFlag() == NoticeEvent.NOTICE93) {
			if (event.getNum() != 0) {
				cur_view = event.getNum();
			}
			setSuspensionView();
		}
		if (event.getFlag() == NoticeEvent.NOTICE88) {//取消
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					mTextViewActivity.setVisibility(View.GONE);
					setActivityNum();
				}
			});

		} else if (event.getFlag() == NoticeEvent.NOTICE87 || event.getFlag() == NoticeEvent.NOTICE89) {//显示
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					mTextViewActivity.setVisibility(View.VISIBLE);
				}
			});
		}
	}

	@Override
	public void checkLatestAppInfo(int retCode, RspGetLatestAppInfo rsp) {
		sendHandlerMessage(mHandler, HandlerType.CREATE_GETDATA_BACK, retCode, rsp);
	}

	@Override
	protected void onNewIntent(Intent intent) {//
		super.onNewIntent(intent);
		if (intent.getBooleanExtra("pushmessage", false)) {
			if (PushMessageType.CHAT.getValue() == intent.getIntExtra("pushtype", -1)) {
				mHandler.sendEmptyMessage(HandlerValue.MAIN_CHAT_MESSAGE_OTHER);
			}
		}
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			selectBottomCurrentPos = bundle.getInt("bottom");
		}
		setBottomShow();
	}

	@Override
	protected void initData() {}

	@Override
	protected void initRecourse() {
		ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
		rl_bottom_mine = (RelativeLayout) findViewById(R.id.rl_bottom_mine);
		rl_bottom_mine.setOnClickListener(this);
		rl_bottom_message = (RelativeLayout) findViewById(R.id.rl_bottom_message);
		rl_bottom_message.setOnClickListener(this);
		rl_bottom_broadcast = (RelativeLayout) findViewById(R.id.rl_bottom_broadcast);
		rl_bottom_broadcast.setOnClickListener(this);
		rl_bottom_rank = (RelativeLayout) findViewById(R.id.rl_bottom_rank);
		rl_bottom_rank.setOnClickListener(this);
		rl_bottom_hall = (RelativeLayout) findViewById(R.id.rl_bottom_hall);
		rl_bottom_hall.setOnClickListener(this);
		tv_bottom_mine = (TextView) findViewById(R.id.tv_tabhost_mine);
		tv_bottom_message = (TextView) findViewById(R.id.tv_tabhost_message);
		tv_bottom_broadcast = (TextView) findViewById(R.id.tv_tabhost_broadcast);
		tv_bottom_rank = (TextView) findViewById(R.id.tv_tabhost_rank);
		tv_bottom_hall = (TextView) findViewById(R.id.tv_tabhost_hall);

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		mTabHost.getTabWidget().setVisibility(View.GONE);
		tabHostAddTab();
		mTextViewMsg = (TextView) findViewById(R.id.tabText_new);

		mTextViewGift = (ImageView) findViewById(R.id.tabText_new_gift);
		mTextViewActivity = (ImageView) findViewById(R.id.tabText_new_activity);

		tvBroadcastCount = (TextView) findViewById(R.id.tv_broadcast_num);
		iv_bottom_aphla = (ImageView) findViewById(R.id.iv_main_bottom_alpha);
		iv_bottom_aphla.setOnClickListener(this);

		rl_susp = (FrameLayout) findViewById(R.id.rl_susp);
		iv_susp_icon = (ImageView) findViewById(R.id.iv_susp_icon);
		iv_close = (ImageView) findViewById(R.id.iv_close);
		iv_susp_icon.setOnClickListener(this);
		iv_close.setOnClickListener(this);

	}

	private void tabHostAddTab() {
		mTabHost.addTab(mTabHost.newTabSpec(TAB_HALL).setIndicator(TAB_HALL), MainHallFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(TAB_MESSAGE).setIndicator(TAB_MESSAGE), MainMessageFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(TAB_BROADCAST).setIndicator(TAB_BROADCAST), MainBroadcastFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(TAB_RANK).setIndicator(TAB_RANK), MainFindFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(TAB_MINE).setIndicator(TAB_MINE), MainMineFragment.class, null);
	}

	@Override
	protected int initView() {
		return R.layout.activity_main;
	}

	private void setBottomSlect(int pos) {//设置底部导航栏
		if (pos == 0) {
			cur_view = SuspensionEntity.ACTIVITY_HALL;
			setBottomSourceChange(rl_bottom_hall, tv_bottom_hall, R.drawable.main_icon_broadcast_pr, true);
		} else {
			setBottomSourceChange(rl_bottom_hall, tv_bottom_hall, R.drawable.main_icon_broadcast_un, false);
		}
		if (pos == 1) {
			cur_view = NON_SUSPENSION;
			setBottomSourceChange(rl_bottom_message, tv_bottom_message, R.drawable.main_icon_message_pr, true);
		} else {
			setBottomSourceChange(rl_bottom_message, tv_bottom_message, R.drawable.main_icon_message_un, false);
		}
		if (pos == 2) {//
			cur_view = MainBroadcastFragment.curView;
			setBottomSourceChange(rl_bottom_broadcast, tv_bottom_broadcast, R.drawable.main_icon_hall_pr, true);
		} else {
			setBottomSourceChange(rl_bottom_broadcast, tv_bottom_broadcast, R.drawable.main_icon_hall_un, false);
		}
		if (pos == 3) {
			cur_view = NON_SUSPENSION;
			setBottomSourceChange(rl_bottom_rank, tv_bottom_rank, R.drawable.main_icon_find_pr, true);
		} else {
			setBottomSourceChange(rl_bottom_rank, tv_bottom_rank, R.drawable.main_icon_find_un, false);
		}
		if (pos == 4) {
			cur_view = NON_SUSPENSION;
			setBottomSourceChange(rl_bottom_mine, tv_bottom_mine, R.drawable.main_icon_mine_pr, true);
		} else {
			setBottomSourceChange(rl_bottom_mine, tv_bottom_mine, R.drawable.main_icon_mine_un, false);
		}
		if (BAApplication.mLocalUserInfo != null) {
			PeiPeiPersistBiz.getInstance().openPersist(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
					BAApplication.mLocalUserInfo.uid.intValue(), ChatManageBiz.getInManage(this));
		}
		setSuspensionView();
	}

	private void setSuspensionView() {
		SuspensionEntity entity = BAApplication.getInstance().getSuspEntity();
		if (entity != null) {
			actEntify = entity.getActEntifyForNumber(cur_view);
			if (actEntify != null) {
				if (actEntify.getStatus() == SuspensionActEntity.SHOW_SUSP_ICON) {
					rl_susp.setVisibility(View.VISIBLE);
					if (!TextUtils.isEmpty(actEntify.getImage())) {
						DisplayImageOptions options = ImageOptionsUtils.GetFloatIconKeySmallRounded(this);
						imageLoader.displayImage("third://" + actEntify.getImage(), iv_susp_icon, options);
					}
				} else {
					rl_susp.setVisibility(View.GONE);
				}
			} else {
				rl_susp.setVisibility(View.GONE);
			}
		}
	}

	private void setBottomSourceChange(RelativeLayout rl, TextView tv, int source, boolean isSelect) {
		if (isSelect) {
			rl.setBackgroundColor(getResources().getColor(R.color.main_bottom_bg_pre));
			tv.setTextColor(getResources().getColor(R.color.white));
		} else {
			rl.setBackgroundResource(R.drawable.main_bar_menu_un);
			tv.setTextColor(getResources().getColor(R.color.gray));
		}
		OperateViewUtils.setTextViewTopDrawable(this, source, tv);
	}

	@Override
	public void getSentChatMessageCallBack(int retcode, ChatMessageReceiptEntity recepit) {
		if (retcode == 0)
			ChatRecordBiz.deletReceipte(this, recepit.getfUid(), recepit.getBurnId());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {//activity被销毁时，恢复数据
		super.onSaveInstanceState(outState);
		if (outState != null) {
			outState.putInt("selectBottomCurrentPos", selectBottomCurrentPos);
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {//当按home按钮是保存数据
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			selectBottomCurrentPos = savedInstanceState.getInt("selectBottomCurrentPos");
			setBottomSlect(selectBottomCurrentPos);
			setBottomShow();

		}

	}

}
