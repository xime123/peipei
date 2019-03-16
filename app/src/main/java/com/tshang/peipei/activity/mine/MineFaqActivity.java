package com.tshang.peipei.activity.mine;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.platformtools.Log;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.account.LoginActivity;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.dialog.PhotoSetDialog;
import com.tshang.peipei.activity.dialog.UpdateVoiceDialog;
import com.tshang.peipei.activity.dialog.WebViewPlayVoiceDialog;
import com.tshang.peipei.activity.main.MainActivity;
import com.tshang.peipei.activity.main.MainRankActivity;
import com.tshang.peipei.activity.store.StoreH5RechargeActivity;
import com.tshang.peipei.activity.suspension.SuspensionActivity;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseFileLog;
import com.tshang.peipei.base.BaseHttpUtils;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseTools;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.BaseWebView;
import com.tshang.peipei.base.IAPKInfoUtil;
import com.tshang.peipei.base.IFileUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.bizcallback.BizCallBackGetUserInfo;
import com.tshang.peipei.model.hall.MainHallBiz;
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.network.socket.ThreadPoolService;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.common.util.ImageUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.assist.FailReason;
import com.tshang.peipei.vender.imageloader.core.assist.ImageScaleType;
import com.tshang.peipei.vender.imageloader.core.assist.ImageSize;
import com.tshang.peipei.vender.imageloader.core.listener.ImageLoadingListener;

/**
 * @Title: FaqActivity.java 
 *
 * @Description: webview界面
 *
 * @author allen  
 *
 * @date 2014-12-6 下午5:06:05 
 *
 * @version V2.0   
 */
public class MineFaqActivity extends BaseActivity implements BizCallBackGetUserInfo {

	public static final int Handler_userinfo = 1;

	private WebView mWebView;
	//	public static final int QUEEN_VALUE = 0;
	public static final int KING_VALLUE = 1;
	public static final int LEVEL_VALUE = 2;
	public static final int GROUP_VALUE = 3;
	public static final int FORBIT_VALUE = 4;
	public static final int ACTIVITY_VALUE = 5;
	public static final int GAMES_VALUE = 6;
	public static final int SEARCH_VALUE = 7;
	public static final int MAIN_HALL_VALUE = 8;
	public static final int FINGER_VALUE = 9;
	public static final int SCENE_SHOP_VALUE = 10;
	public static final int FIND_APPS = 11;
	public static final int ACTIVITY_XIAOMI = 12;
	public static final int PUBLIC_VALUE = 13;

	public int current_show_url_value = KING_VALLUE;
	public static final String WHERE_FROM = "where_from";
	public static final String LOADURL = "loadUrl";
	public static final String ISHOWTITLEBAR = "isShowNavigationBar";

	private String act;

	private String lodUrl;
	private int isShowTitleBar = 0;//0:不显示，1：显示

	private Dialog dialog;

	final class PeipeiInJavaScript {
		@JavascriptInterface
		public void runPeiPeiJavaScript(final String str) {
			BaseFileLog.saveLogByFile(BaseFileLog.getTraceInfo() + ";\n back =" + str + " \n", BAConstants.PEIPEI_AIBEI_LOG);
			mHandler.post(new Runnable() {
				public void run() {
					try {
						JSONObject js = new JSONObject(str);
						if (js.has("url")) {
							String ppurl = js.getString("url");
							if (ppurl.startsWith(BAConstants.PP_PERSONAL)) {
								if (js.has("uid")) {
									int uid = js.getInt("uid");
									if (js.has("goodsid")) {//有道具商城
										int goodsid = js.getInt("goodsid");
										if (BAApplication.mLocalUserInfo != null) {
											SpaceUtils.toSpaceCustomByGoodsid(MineFaqActivity.this, BAApplication.mLocalUserInfo.uid.intValue(),
													BAApplication.mLocalUserInfo.sex.intValue(), goodsid);
										}
									} else {
										MainHallBiz.getInstance().getUserInfo(MineFaqActivity.this, uid, MineFaqActivity.this);
									}
								}
							} else if (ppurl.startsWith(BAConstants.PP_RECHARGE)) {
								BaseUtils.openActivity(MineFaqActivity.this, StoreH5RechargeActivity.class);
							} else if (ppurl.startsWith(BAConstants.PP_MAINHALL)) {
								Bundle b = new Bundle();
								b.putInt("bottom", 0);
								BaseUtils.openActivity(MineFaqActivity.this, MainActivity.class, b);
							} else if (ppurl.startsWith(BAConstants.PP_BROADCAST)) {
								//								Bundle b = new Bundle();
								//								b.putInt("bottom", 2);
								//								BaseUtils.openActivity(MineFaqActivity.this, MainActivity.class, b);
								MainRankActivity.openMineFaqActivity(MineFaqActivity.this);
							} else if (ppurl.startsWith(BAConstants.PP_MAINRANK)) {
								Bundle b = new Bundle();
								b.putInt("bottom", 3);
								BaseUtils.openActivity(MineFaqActivity.this, MainActivity.class, b);
							} else if (ppurl.startsWith(BAConstants.PP_VOICE)) {
								if (js.has("act")) {
									act = js.getString("act");
									String content = "";
									if (js.has("content")) {
										content = js.getString("content");
									}
									new UpdateVoiceDialog(MineFaqActivity.this, mWebView, act, content).showDialog();
								}
							} else if (ppurl.startsWith(BAConstants.PP_PHOTO)) {
								if (js.has("act")) {
									act = js.getString("act");
									new PhotoSetDialog(MineFaqActivity.this, android.R.style.Theme_Translucent_NoTitleBar).showDialog(0, 0);
								}
							} else if (ppurl.startsWith(BAConstants.PP_PLAY_VOICE)) {
								if (js.has("voice_url")) {
									String voice_url = js.getString("voice_url");

									if (!TextUtils.isEmpty(voice_url)) {
										new WebViewPlayVoiceDialog(MineFaqActivity.this, voice_url).showDialog();
									}
								}
							} else if (ppurl.startsWith(BAConstants.PP_MAKEPROFIT)) {
								BaseUtils.openActivity(MineFaqActivity.this, MineMissionsActivity.class);
							} else if (ppurl.startsWith(BAConstants.PP_SHARE)) {
								//TODO 分享
							} else if (ppurl.startsWith(BAConstants.PP_MYHOME)) {//返回个人主页
								finish();
							} else if (ppurl.startsWith(BAConstants.PP_TO_NIUNIU_GAME)) {//跳转牛牛游戏厅
								/**
								 * 32位的CPU直接跳转
								 * 64位的CPU跳转牛牛APK
								 */
								String pkg = js.getString("pkg");
								String cls = js.getString("cls");
								final String apkUrl = js.getString("apkUrl");
								int versionCode = Integer.parseInt(js.getString("versionCode"));
								if (IAPKInfoUtil.isInstallApk(MineFaqActivity.this, pkg)) {
									BaseFile.delete(new File(IFileUtils.getSDCardRootDirectory() + BAConstants.PEIPEI_GAME_NIUNIUT_PATH));//删除下载的牛牛APK
									if (versionCode > IAPKInfoUtil.getVersionCode(MineFaqActivity.this, pkg)) {
										DownLoadAPKDialog(apkUrl, "有新版，请更新下载");
									} else {
										String auth = SharedPreferencesTools.getInstance(MineFaqActivity.this,
												BAApplication.mLocalUserInfo.uid.intValue() + "").getStringValueByKey(
												SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL);
										Bundle bundle = new Bundle();
										bundle.putString("auth", auth);
										bundle.putString("login_info", "4asfasfasfaaasfasf48");
										bundle.putString("silver", "0");
										ComponentName componentName = new ComponentName(pkg, cls);
										IAPKInfoUtil.startApk(MineFaqActivity.this, componentName, bundle);
									}
								} else {
									File file=new File(IFileUtils.getSDCardRootDirectory() + BAConstants.PEIPEI_GAME_NIUNIUT_PATH);
									if (file.exists()) {
										IAPKInfoUtil.InstallAPk(MineFaqActivity.this, IFileUtils.getSDCardRootDirectory() + BAConstants.PEIPEI_GAME_NIUNIUT_PATH);
									}else {
										DownLoadAPKDialog(apkUrl, "游戏未安装,请下载安装");
									}
								}
							} else if (ppurl.startsWith("pp://showcenter")) {//小米活动跳转到主页
								//								BaseUtils.openActivity(MineFaqActivity.this, LoginActivity.class);
								MineFaqActivity.this.finish();
							} else if (ppurl.startsWith("pp://login")) {//小米活动跳转到登陆界面
								BaseUtils.openActivity(MineFaqActivity.this, LoginActivity.class);
								MineFaqActivity.this.finish();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						BaseFileLog.saveLogByFile(BaseFileLog.getTraceInfo() + ";\n " + e.getMessage() + " \n", BAConstants.PEIPEI_AIBEI_LOG);
					}
				}
			});
		}
	}

	/**
	 * 下载提示框
	 * @author Aaron
	 *
	 * @param path
	 * @param msg
	 */
	private void DownLoadAPKDialog(final String path, String msg) {
		dialog = DialogFactory.showMsgDialog(MineFaqActivity.this, "", msg, "下载", "取消", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DialogFactory.dimissDialog(dialog);
				IAPKInfoUtil.APkDownLoad(MineFaqActivity.this, path, IFileUtils.getSDCardRootDirectory() + BAConstants.PEIPEI_GAME_NIUNIUT_PATH);
			}
		}, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			int temp = bundle.getInt(WHERE_FROM);
			if (temp != current_show_url_value) {
				current_show_url_value = temp;
				setWebView();
				mWebView.clearHistory();
			}
		}
	}

	private void setWebView() {
		Log.d("Aaron", "auth=="+SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getStringValueByKey(
							SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL));
		String url = "";
		if (current_show_url_value == KING_VALLUE) {
			mTitle.setText(R.string.setting_help);
			mBackText.setText(R.string.mine);
			url = BAConstants.FAQ_URL;
		} else if (current_show_url_value == LEVEL_VALUE) {
			mBackText.setText(R.string.private_page);
			mTitle.setText(R.string.str_level);
			url = BAConstants.HEAD_LEVEL_URL + BAApplication.mLocalUserInfo.uid.intValue();
		} else if (current_show_url_value == GROUP_VALUE) {
			mBackText.setText(R.string.back);
			mTitle.setText(R.string.str_harem_rule);
			url = BAConstants.GROUP_URL;
		} else if (current_show_url_value == FORBIT_VALUE) {
			mBackText.setText(R.string.back);
			mTitle.setText(R.string.str_forbit_speak);
			url = BAConstants.FORBIDSPEAK_URL
					+ SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getStringValueByKey(
							SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL) + "&p=android";
		} else if (current_show_url_value == ACTIVITY_VALUE) {//活动
			//2.3.3活动去掉标题栏
			mTitleLayout.setVisibility(View.GONE);
			mBackText.setText(R.string.back);
			mTitle.setText(R.string.str_activities);
			SharedPreferencesTools.getInstance(this).saveBooleanKeyValue(false, BAConstants.PEIPEI_NEW_URL);
			if (!BAConstants.IS_TEST) {
				url = BAConstants.ACTIVITIES_URL
						+ SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getStringValueByKey(
								SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL) + "&p=android&v=" + BaseTools.getAppVersionName(this);
			} else {
				url = "http://pptest.yidongmengxiang.com/activities?u="
						+ SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getStringValueByKey(
								SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL) + "&p=android&v=" + BaseTools.getAppVersionName(this);
			}
		} else if (current_show_url_value == GAMES_VALUE) {//游戏专区
			mTitleLayout.setVisibility(View.GONE);
			mBackText.setText(R.string.back);
			mTitle.setText(R.string.str_title_three);
			String str = BAConstants.GAMES_URL;
			if (BAConstants.IS_TEST) {
				str = BAConstants.TEST_GAMES_URL;
				url = str
						+ SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getStringValueByKey(
								SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL) + "&p=android&v=" + BaseTools.getAppVersionName(this) + "&debug";
			} else {
				url = str
						+ SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getStringValueByKey(
								SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL) + "&p=android&v=" + BaseTools.getAppVersionName(this);
			}
		} else if (current_show_url_value == SEARCH_VALUE) {
			mBackText.setText(R.string.back);
			url = BAConstants.SEARCH_URL
					+ SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getStringValueByKey(
							SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL) + "&p=android";
		} else if (current_show_url_value == MAIN_HALL_VALUE) {//广告位
			//2.3.3活动去掉标题栏
			mTitleLayout.setVisibility(View.GONE);
			mBackText.setText(R.string.back);
			String title = getIntent().getExtras().getString("title");
			mTitle.setText(title);
			url = getIntent().getExtras().getString("url");
		} else if (current_show_url_value == FINGER_VALUE) {
			mBackText.setText(R.string.back);
			mTitle.setText(R.string.str_finger_record);
			url = BAConstants.FINGER_URL
					+ SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getStringValueByKey(
							SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL) + "&p=android";
		} else if (current_show_url_value == SCENE_SHOP_VALUE) {
			mBackText.setText(R.string.back);
			mTitle.setText(R.string.str_scene_shop);
			String str_url = BAConstants.SCENE_SHOP_URL;
			if (BAConstants.IS_TEST) {
				str_url = BAConstants.SCENE_TEST_SHOP_URL;
			} else {
				str_url = BAConstants.SCENE_SHOP_URL;
			}
			url = str_url
					+ SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getStringValueByKey(
							SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL) + "&p=android";
		} else if (current_show_url_value == FIND_APPS) {
			mBackText.setText(R.string.back);
			mTitle.setText(R.string.str_find_apps);
			String str_url = BAConstants.FIND_APP_URL;
			url = str_url
					+ SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getStringValueByKey(
							SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL) + "&p=android";
		} else if (current_show_url_value == ACTIVITY_XIAOMI) {//小米活动
			mTitleLayout.setVisibility(View.GONE);
			String str_url = "";
			if (BAConstants.IS_TEST) {
				str_url = BAConstants.XIAOMI_ACTIVITY_TEST;
				if (BAApplication.mLocalUserInfo != null) {
					url = str_url
							+ SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getStringValueByKey(
									SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL);
				} else
					url = str_url;
			} else {
				str_url = BAConstants.XIAOMI_ACTIVITY_PRD;
				if (BAApplication.mLocalUserInfo != null) {
					url = str_url
							+ SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getStringValueByKey(
									SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL);
				} else
					url = str_url;
			}
		} else if (current_show_url_value == PUBLIC_VALUE) {
			if (isShowTitleBar == 0) {
				mTitleLayout.setVisibility(View.GONE);
			} else {
				mTitleLayout.setVisibility(View.VISIBLE);
			}
			url = lodUrl
					+ "&u="
					+ SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getStringValueByKey(
							SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL) + "&p=android&v=" + BaseTools.getAppVersionName(this);
		}
		BaseUtils.showDialog(this, R.string.loading);
		// 这样就可以使用window.injs来调用它的方法
		mWebView.addJavascriptInterface(new PeipeiInJavaScript(), "peipeiinjs");

		BaseWebView.setWebView(this, mWebView);
		if (!TextUtils.isEmpty(url)) {
			mWebView.loadUrl(url);

			final String ss = url;

			ThreadPoolService.getInstance().execute(new Runnable() {

				@Override
				public void run() {

					String sss = BaseHttpUtils.getContent(ss);
					if (!TextUtils.isEmpty(sss)) {
						if (sss.indexOf("<title>") > 0) {
							String title = sss.substring(sss.indexOf("<title>") + 7, sss.indexOf("</title>"));
							HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.GET_TITLE_FOR_HTML, title);
						}
					}
				}
			});
		}
	}

	@Override
	protected void initData() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			current_show_url_value = bundle.getInt(WHERE_FROM);
			if (current_show_url_value == PUBLIC_VALUE) {
				lodUrl = bundle.getString(LOADURL);
				isShowTitleBar = bundle.getInt(ISHOWTITLEBAR);
			}
		}
		setWebView();
	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitleLayout = (LinearLayout) findViewById(R.id.webview_title_layout);
		mWebView = (WebView) findViewById(R.id.faq_webview);

	}

	@Override
	protected int initView() {
		return R.layout.activity_faq;
	}

	public static void openMineFaqActivity(Activity activity, int fromValue) {
		Bundle bundle = new Bundle();
		bundle.putInt(WHERE_FROM, fromValue);
		BaseUtils.openActivity(activity, MineFaqActivity.class, bundle);
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);

		BaseUtils.cancelDialog();
		switch (msg.what) {
		case ACTIVITY_VALUE:
			if (msg.arg1 == 0) {
				String url = BAConstants.ACTIVITIES_URL + msg.obj + "&p=android";
				mWebView.loadUrl(url);
			} else {
				BaseUtils.showTost(this, R.string.operate_faile);
			}
			break;
		case Handler_userinfo:
			if (msg.arg1 == 0) {
				GoGirlUserInfo info = (GoGirlUserInfo) msg.obj;
				SpaceUtils.toSpaceCustom(this, info.uid.intValue(), info.sex.intValue());
			}
			break;
		case HandlerValue.GET_TITLE_FOR_HTML:
			if (!TextUtils.isEmpty((String) msg.obj))
				mTitle.setText((String) msg.obj);
			break;
		case HandlerValue.RESULT_UPDATE_BY_WEBVIEW:
			JSONObject jo = new JSONObject();
			try {
				jo.put("type", BAConstants.PP_PHOTO);
				jo.put("photo", (String) msg.obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			mWebView.loadUrl("javascript:fromandroidrun(" + jo + ")");
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_tv_left:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void getUserInfoCallBack(int retCode, GoGirlUserInfo userinfo) {
		sendHandlerMessage(mHandler, Handler_userinfo, retCode, userinfo);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg1 != RESULT_OK) {
			return;
		}

		switch (arg0) {
		case PhotoSetDialog.GET_PHOTO_BY_CAMERA:
			String path = BaseFile.getTempFile().getAbsolutePath();

			sendChatImage(path);
			break;
		case PhotoSetDialog.GET_PHOTO_BY_GALLERY:
			if (arg2 != null) {
				Uri uri = arg2.getData(); // 读取相册图片
				if (uri != null) {
					String path1 = BaseFile.getFilePathFromContentUri(uri, getContentResolver());
					sendChatImage(path1);
				}
			}
			break;
		default:
			break;
		}

	}

	private void sendChatImage(String path) {
		if (!TextUtils.isEmpty(path)) {
			try {
				DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).considerExifParams(true)
						.cacheInMemory(false).cacheOnDisk(false).bitmapConfig(Bitmap.Config.RGB_565).build();
				ImageSize imageSize = new ImageSize(BasePhone.getScreenWidth(this), BasePhone.getScreenHeight(this));
				imageLoader.loadImage("file://" + path, imageSize, options, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						if (loadedImage != null) {
							final byte[] bitmapBytes = ImageUtils.bitmapToByte(loadedImage);
							final File directory_pic = BaseFile.getStoregeDirectory("", MineFaqActivity.this);
							if (!directory_pic.exists()) {
								directory_pic.mkdirs();
							}

							new Thread() {
								public void run() {
									BaseHttpUtils.uploadFile(BAConstants.WEBVIEW_UPDATE_PHOTO_URL,
											BaseFile.saveByteToFile(bitmapBytes, directory_pic.getAbsolutePath() + "/temp.jpg"), act,
											BAApplication.mLocalUserInfo.uid.intValue(), mHandler);
								}
							}.start();
						}
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			BaseUtils.showTost(this, R.string.msg_rechoice_gallery);
		}
	}
}
