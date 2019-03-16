package com.tshang.peipei.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.iapppay.sdk.main.IAppPay;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.account.LoginActivity;
import com.tshang.peipei.activity.main.MainActivity;
import com.tshang.peipei.activity.mine.MineFaqActivity;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.NumericUtils;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.PeiPeiAppStartBiz;
import com.tshang.peipei.model.request.RequestGetAppConf.IGetAppConf;
import com.tshang.peipei.model.request.RequestGetAppConfV2.IGetAppConfV2;
import com.tshang.peipei.model.request.RequestLoadingActionPic;
import com.tshang.peipei.model.request.RequestLoadingPic;
import com.tshang.peipei.network.socket.ThreadPoolService;
import com.tshang.peipei.protocol.asn.gogirl.GGConfInfo;
import com.tshang.peipei.protocol.asn.gogirl.GGConfInfoList;
import com.tshang.peipei.service.GpsLocationThread;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;
import com.tshang.peipei.vender.imageloader.core.assist.FailReason;
import com.tshang.peipei.vender.imageloader.core.assist.ImageScaleType;
import com.tshang.peipei.vender.imageloader.core.listener.ImageLoadingListener;

/**
 * @Title: WelcomeActivity.java 
 *
 * @Description: 闪屏 
 *
 * @author allen  
 *
 * @date 2014-5-17 下午4:19:02 
 *
 * @version V1.0   
 */
@SuppressLint("HandlerLeak")
public class WelcomeActivity extends BaseActivity implements IGetAppConf, IGetAppConfV2 {
	private ImageView iv_bg;
	private ImageView action_bg;
	private boolean isXiaomiActivity = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//判断是否模拟器运行
		//		if (BaseUtils.isEmulator(this) || BaseUtils.CheckEmulatorBuild(this) || BaseUtils.CheckOperatorNameAndroid(this)
		//				|| BaseUtils.CheckDeviceIDS(this) || BaseUtils.CheckPhoneNumber(this)) {
		//			BaseUtils.showTost(this, R.string.emulator_login_toast);
		//			finish();
		//			return;
		//		}
		// 防止重复打开应用
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) == Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) {
			finish();
			return;
		}

//		MobclickAgent.openActivityDurationTrack(false);
		if (!BAConstants.IS_TEST) {
//			MobclickAgent.updateOnlineConfig(this);
		}

		//		MobclickAgent.setDebugMode(true);

		mHandler.postDelayed(new Runnable() {
			public void run() {
				getAppConf();
			}
		}, 10);
		isXiaomiActivity = SharedPreferencesTools.getInstance(this).getBooleanKeyValue(BAConstants.PEIPEI_WDJ_HUODONG_REGISTER1);

		initDefault();
		getLocation();
	}

	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);

			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void initDefault() {
		IAppPay.init(this, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, BAConstants.APP_ID);
		BAConstants.PEIPEI_AIBEI_INIT_TIME = System.currentTimeMillis();

		//		DMOfferWall.init(this, BAConstants.DOMD_ID);
		initBaidu();
		//		initAibei();
	}

	@SuppressWarnings("unused")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.DESTROY_WELCOME_VALUE:
			SharedPreferencesTools.getInstance(this).saveBooleanKeyValue(true, BAConstants.PEIPEI_WDJ_HUODONG_REGISTER1);
			defaultFinish();
			break;
		case HandlerValue.CLEAR_MEMORY_VALUE:
			if (!isXiaomiActivity && BAApplication.Channel.equals("and-xiaomisd") && BAConstants.IS_ACTIVITY) {
				mHandler.sendEmptyMessage(HandlerValue.DESTROY_WELCOME_VALUE);
				MineFaqActivity.openMineFaqActivity(WelcomeActivity.this, MineFaqActivity.ACTIVITY_XIAOMI);
			} else {
				startActivity();
			}
			break;
		default:
			break;
		}
	}

	private void initBaidu() {
		//PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY, BAConstants.BAIDU_APP_KEY);
	}

	private void startActivity() {
		//		String actionPicUrL = SharedPreferencesTools.getInstance(this).getStringValueByKey(BAConstants.PEIPEI_APP_CONFIG_ACTION_LOAD_PIC);
		//		if (!TextUtils.isEmpty(actionPicUrL)) {
		//			loadActionPic();
		//		} else {
		Intent intent = null;
		if (BAApplication.mLocalUserInfo != null) {
			BAApplication.isLogin = true;
			intent = new Intent(this, MainActivity.class);
		} else {
			intent = new Intent(this, LoginActivity.class);
		}
		startActivity(intent);
		overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out);
		mHandler.sendEmptyMessage(HandlerValue.DESTROY_WELCOME_VALUE);
		//		}
	}

	@Override
	protected void initData() {
		//		loadStartPic();
		startFristPage();
		loadStartPic();
	}

	private void startFristPage() {
		action_bg.setVisibility(View.VISIBLE);
		iv_bg.setVisibility(View.GONE);
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				startSecondPage();
			}
		}, 3000);
	}

	private void startSecondPage() {
		iv_bg.setVisibility(View.VISIBLE);
		action_bg.setVisibility(View.GONE);
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				startActivity();
			}
		}, 3000);
	}

	/**
	 * peipei闪屏
	 * @author Aaron
	 *
	 */
	private void loadStartPic() {
		String loadPicUrL = SharedPreferencesTools.getInstance(this).getStringValueByKey(BAConstants.PEIPEI_APP_CONFIG_LOAD_PIC);
		if (!TextUtils.isEmpty(loadPicUrL)) {
			String[] urls = loadPicUrL.trim().split(",");
			if (urls != null && urls.length == 3) {
				String usrlName = urls[0];
				long nowTime = System.currentTimeMillis() / 1000;
				try {
					long startTime = Long.parseLong(urls[1]);
					long endTime = Long.parseLong(urls[2] + "");
					if (nowTime >= startTime && endTime >= endTime) {
						int index = usrlName.indexOf("/", 7);
						String url = usrlName.substring(index);
						String saveImageName = url.substring(url.lastIndexOf("/"));
						File file = new File(SdCardUtils.getInstance().getLoadPicDir(), saveImageName);
						if (file != null && file.exists()) {//说明闪屏存在
							DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(false).cacheInMemory(false)
									.considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565)
									.build();
//							ImageLoader.getInstance().displayImage("file://" + file.getAbsolutePath(), iv_bg, options, new ImageLoadingListener() {
//
//								@Override
//								public void onLoadingStarted(String imageUri, View view) {}
//
//								@Override
//								public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}
//
//								@Override
//								public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//									if (loadedImage != null) {
//										iv_bg.setScaleType(ScaleType.CENTER_CROP);
//										iv_bg.setImageBitmap(loadedImage);
//									}
//								}
//
//								@Override
//								public void onLoadingCancelled(String imageUri, View view) {
//
//								}
//							});
						} else {
							RequestLoadingPic req = new RequestLoadingPic();
							req.getLoadingPic(this, loadPicUrL, 0, null);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 获取闪屏
	 * @author Aaron
	 *
	 */
	//	private void loadActionPic() {
	//		String loadPicUrL = SharedPreferencesTools.getInstance(this).getStringValueByKey(BAConstants.PEIPEI_APP_CONFIG_ACTION_LOAD_PIC);
	//		File file = new File(SdCardUtils.getInstance().getLoadPicDir(), loadPicUrL.substring(loadPicUrL.lastIndexOf("/")));
	//		if (file != null && file.exists()) {//说明闪屏存在
	//			DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(false).cacheInMemory(false).considerExifParams(true)
	//					.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).build();
	//			ImageLoader.getInstance().displayImage("file://" + file.getAbsolutePath(), iv_bg, options, new ImageLoadingListener() {
	//
	//				@Override
	//				public void onLoadingStarted(String imageUri, View view) {
	//
	//				}
	//
	//				@Override
	//				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
	//
	//				}
	//
	//				@Override
	//				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
	//					if (loadedImage != null) {
	//						iv_bg.setVisibility(View.GONE);
	//						action_bg.setVisibility(View.VISIBLE);
	//						action_bg.setScaleType(ScaleType.CENTER_CROP);
	//						action_bg.setImageBitmap(loadedImage);
	//					}
	//				}
	//
	//				@Override
	//				public void onLoadingCancelled(String imageUri, View view) {
	//
	//				}
	//			});
	//		} else {
	//			RequestLoadingActionPic req = new RequestLoadingActionPic();
	//			req.getLoadingPic(this, loadPicUrL, 0, null);
	//		}
	//	}

	@Override
	protected void initRecourse() {
		iv_bg = (ImageView) findViewById(R.id.iv_load_bg);
		action_bg = (ImageView) findViewById(R.id.logo_360);
	}

	@Override
	protected int initView() {
		return R.layout.activity_welcome;
	}

	//获取经纬度
	private void getLocation() {
		long time = SharedPreferencesTools.getInstance(this).getLongKeyValue(BAConstants.PEIPEI_GPS_TIME);
		long currTime = SharedPreferencesTools.getInstance(this).getLongKeyValue(BAConstants.PEIPEI_APP_CONFIG_LOTION_TIME);
		if (BaseTimes.isTimeDistanceNow(time, 1000 * currTime)) {
			new GpsLocationThread(getApplicationContext()).start();
			SharedPreferencesTools.getInstance(this).saveLongKeyValue(System.currentTimeMillis(), BAConstants.PEIPEI_GPS_TIME);
		}
	}

	/**
	 * 拉取配制文件
	 * @author Aaron
	 *
	 */
	private void getAppConf() {
		//		long time = SharedPreferencesTools.getInstance(this).getLongKeyValue(BAConstants.PEIPEI_APP_CONFIG);
		//		PeiPeiAppStartBiz pBiz = new PeiPeiAppStartBiz();
		//		if (BaseTimes.isTimeDistanceNow(time, 1000 * 60 * 60 * 6)) {
		//			if (BAApplication.mLocalUserInfo != null) {
		//				pBiz.getAppConf(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(), this);
		//			} else {
		//				pBiz.getAppConf("".getBytes(), BAApplication.app_version_code, 0, this);
		//			}
		//
		//			SharedPreferencesTools.getInstance(this).saveLongKeyValue(System.currentTimeMillis(), BAConstants.PEIPEI_APP_CONFIG);
		//
		//			pBiz.getSmileConf("".getBytes(), -1, 0);//拉去配置语音
		//		}

		PeiPeiAppStartBiz pBiz = new PeiPeiAppStartBiz();
		if (BAApplication.mLocalUserInfo != null) {
			pBiz.getAppConfV2(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
					BAApplication.Channel, this);
		} else {
			pBiz.getAppConfV2("".getBytes(), BAApplication.app_version_code, 0, BAApplication.Channel, this);
		}
		pBiz.getSmileConf("".getBytes(), -1, 0);//拉去配置语音
	}

	private void postStartActivity() {
		mHandler.postDelayed(new Runnable() {

			@SuppressWarnings("unused")
			@Override
			public void run() {
				if (!isXiaomiActivity && BAApplication.Channel.equals("and-xiaomisd") && BAConstants.IS_ACTIVITY) {
					mHandler.sendEmptyMessage(HandlerValue.DESTROY_WELCOME_VALUE);
					MineFaqActivity.openMineFaqActivity(WelcomeActivity.this, MineFaqActivity.ACTIVITY_XIAOMI);
				} else {
					startActivity();
				}
			}
		}, 2500);
	}

	private void clearImgCache() {
		ThreadPoolService.getInstance().execute(new Runnable() {

			@Override
			public void run() {
				try {
					if (SdCardUtils.isExistSdCard()) {//清理掉之前的语音文件存错了
						File dirFile = new File(Environment.getExternalStorageDirectory() + "/PeiPei");
						if (dirFile != null) {
							File[] files = dirFile.listFiles();
							if (files != null) {
								for (File file : files) {
									if (file.isFile()) {
										file.delete();
									}
								}
							}
						}
					}
					imageLoader.clearDiskCache();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.CLEAR_MEMORY_VALUE);
				}
			}
		});
	}

	@Override
	public void getAppConfig(int retCode, GGConfInfoList list) {
		if (retCode == 0) {
			if (list != null && !list.isEmpty()) {

				for (Object object : list) {
					GGConfInfo info = (GGConfInfo) object;
					if (info != null) {
						String confitem = new String(info.confitem);
						if (!TextUtils.isEmpty(confitem)) {
							if (confitem.equals("lola_timegap")) {//拉取位置的时间差
								try {
									SharedPreferencesTools.getInstance(this).saveLongKeyValue(Long.parseLong(new String(info.confvalue)),
											BAConstants.PEIPEI_APP_CONFIG_LOTION_TIME);
								} catch (NumberFormatException e) {
									e.printStackTrace();
								}
							} else if (confitem.equals("female_create_group")) {//女性是否可以创建后宫
								try {
									SharedPreferencesTools.getInstance(this).saveIntKeyValue(Integer.parseInt(new String(info.confvalue)),
											BAConstants.PEIPEI_APP_CONFIG_IS_CREATE_HAREM_PESSIMION);
								} catch (NumberFormatException e) {
									e.printStackTrace();
								}
							} else if (confitem.equals("top_broadcast_cost_gold_coin")) {
								try {
									SharedPreferencesTools.getInstance(this).saveIntKeyValue(Integer.parseInt(new String(info.confvalue)),
											BAConstants.PEIPEI_APP_CONFIG_TOP_TEXT_BROADCAST_COIN);
								} catch (NumberFormatException e) {
									e.printStackTrace();
								}
							} else if (confitem.equals("loading_pic")) {
								String url = new String(info.confvalue);
								SharedPreferencesTools.getInstance(this).saveStringKeyValue(url, BAConstants.PEIPEI_APP_CONFIG_LOAD_PIC);
								RequestLoadingPic req = new RequestLoadingPic();
								req.getLoadingPic(this, url, 0, null);
							} else if (confitem.equals("fg_win_ratio")) {
								try {
									SharedPreferencesTools.getInstance(this).saveIntKeyValue(Integer.parseInt(new String(info.confvalue)),
											BAConstants.PEIPEI_WIN_RATIO);
								} catch (NumberFormatException e) {
									e.printStackTrace();
								}

							} else if (confitem.equals("ard_appver_switch")) {
								//预留字段
							} else if (confitem.equals("ard_cache_switch")) {
								if (info.confvalue != null) {
									String cache = new String(info.confvalue);
									int newGiftVer = NumericUtils.parseInt(cache, 0);
									int localGiftVer = SharedPreferencesTools.getInstance(this).getIntValueByKey(
											BAConstants.PEIPEI_APP_CONFIG_GIFT_VERSION, 0);
									if (newGiftVer > localGiftVer) {
										clearImgCache();
										SharedPreferencesTools.getInstance(this).saveBooleanKeyValue(true,
												BAConstants.PEIPEI_APP_CONFIG_GIFT_NEED_UPDATE);
										SharedPreferencesTools.getInstance(this).saveIntKeyValue(newGiftVer,
												BAConstants.PEIPEI_APP_CONFIG_GIFT_VERSION);
										SharedPreferencesTools.getInstance(this).saveLongKeyValue(0, BAConstants.PEIPEI_UPADTE_TIME);
									} else {
										postStartActivity();
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void getAppConfigV2(int retCode, GGConfInfoList list) {
		if (retCode == 0) {
			if (list != null && !list.isEmpty()) {

				for (Object object : list) {
					GGConfInfo info = (GGConfInfo) object;
					if (info != null) {
						String confitem = new String(info.confitem);
						if (!TextUtils.isEmpty(confitem)) {
							if (confitem.equals("lola_timegap")) {//拉取位置的时间差
								try {
									SharedPreferencesTools.getInstance(this).saveLongKeyValue(Long.parseLong(new String(info.confvalue)),
											BAConstants.PEIPEI_APP_CONFIG_LOTION_TIME);
								} catch (NumberFormatException e) {
									e.printStackTrace();
								}
							} else if (confitem.equals("female_create_group")) {//女性是否可以创建后宫
								try {
									SharedPreferencesTools.getInstance(this).saveIntKeyValue(Integer.parseInt(new String(info.confvalue)),
											BAConstants.PEIPEI_APP_CONFIG_IS_CREATE_HAREM_PESSIMION);
								} catch (NumberFormatException e) {
									e.printStackTrace();
								}
							} else if (confitem.equals("top_broadcast_cost_gold_coin")) {
								try {
									SharedPreferencesTools.getInstance(this).saveIntKeyValue(Integer.parseInt(new String(info.confvalue)),
											BAConstants.PEIPEI_APP_CONFIG_TOP_TEXT_BROADCAST_COIN);
								} catch (NumberFormatException e) {
									e.printStackTrace();
								}
							} else if (confitem.equals("loading_pic")) {
								String url = new String(info.confvalue);
								SharedPreferencesTools.getInstance(this).saveStringKeyValue(url, BAConstants.PEIPEI_APP_CONFIG_LOAD_PIC);
								RequestLoadingPic req = new RequestLoadingPic();
								req.getLoadingPic(this, url, 0, null);

							} else if (confitem.equals("fg_win_ratio")) {
								try {
									SharedPreferencesTools.getInstance(this).saveIntKeyValue(Integer.parseInt(new String(info.confvalue)),
											BAConstants.PEIPEI_WIN_RATIO);
								} catch (NumberFormatException e) {
									e.printStackTrace();
								}

							} else if (confitem.equals("ard_appver_switch")) {
								//预留字段
							} else if (confitem.equals("ard_cache_switch")) {
								if (info.confvalue != null) {
									String cache = new String(info.confvalue);
									int newGiftVer = NumericUtils.parseInt(cache, 0);
									int localGiftVer = SharedPreferencesTools.getInstance(this).getIntValueByKey(
											BAConstants.PEIPEI_APP_CONFIG_GIFT_VERSION, 0);
									if (newGiftVer > localGiftVer) {
										clearImgCache();
										SharedPreferencesTools.getInstance(this).saveBooleanKeyValue(true,
												BAConstants.PEIPEI_APP_CONFIG_GIFT_NEED_UPDATE);
										SharedPreferencesTools.getInstance(this).saveIntKeyValue(newGiftVer,
												BAConstants.PEIPEI_APP_CONFIG_GIFT_VERSION);
										SharedPreferencesTools.getInstance(this).saveLongKeyValue(0, BAConstants.PEIPEI_UPADTE_TIME);
									} else {
										//										postStartActivity();
									}
								}
							} else if (confitem.equals("pic_key")) {//活动闪屏
								String url = new String(info.confvalue);
								SharedPreferencesTools.getInstance(this).saveStringKeyValue(url, BAConstants.PEIPEI_APP_CONFIG_ACTION_LOAD_PIC);
								RequestLoadingActionPic req = new RequestLoadingActionPic();
								req.getLoadingPic(this, url, 0, null);
							}
						}
					}
				}
			}
		}
	}
}
