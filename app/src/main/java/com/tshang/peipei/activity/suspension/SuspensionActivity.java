package com.tshang.peipei.activity.suspension;

import java.io.File;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.main.MainRankActivity;
import com.tshang.peipei.activity.mine.MineFaqActivity;
import com.tshang.peipei.activity.store.StoreH5RechargeActivity;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseHttpUtils;
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
import com.tshang.peipei.model.xutils.DownloadManager;
import com.tshang.peipei.model.xutils.DownloadService;
import com.tshang.peipei.network.socket.ThreadPoolService;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;

/**
 * @Title: SuspensionActivity.java 
 *
 * @Description:悬浮窗界面 
 *
 * @author DYH  
 *
 * @date 2015-9-24 下午1:49:53 
 *
 * @version V1.0   
 */
public class SuspensionActivity extends BaseActivity implements BizCallBackGetUserInfo {

	public static final int Handler_userinfo = 1;

	private WebView mWebView;
	private Button btn_close;
	private String url;
	public static final String URL = "loadUrl";

	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initData() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			url = bundle.getString(URL);
		}
		setWebView();
	}

	public static void openMineFaqActivity(Activity activity, String url) {
		Bundle bundle = new Bundle();
		bundle.putString(URL, url);
		BaseUtils.openActivity(activity, SuspensionActivity.class, bundle);
	}

	private void setWebView() {
		mWebView.addJavascriptInterface(new PeipeiInJavaScript(), "peipeiinjs");
		BaseWebView.setWebView(this, mWebView);
		if (!TextUtils.isEmpty(url)) {
			BaseUtils.showDialog(this, R.string.loading);
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

	final class PeipeiInJavaScript {
		@JavascriptInterface
		public void runPeiPeiJavaScript(final String str) {
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					try {
						JSONObject js = new JSONObject(str);
						if (js.has("url")) {
							String ppurl = js.getString("url");
							if (ppurl.startsWith(BAConstants.PP_TO_PUBLIC)) {//自定义跳转
								String loadUrl = js.getString("link");
								String isShowNavigationBar = js.getString("isShowNavigationBar");
								Intent intent = new Intent(SuspensionActivity.this, MineFaqActivity.class);
								Bundle bundle = new Bundle();
								bundle.putInt(MineFaqActivity.WHERE_FROM, MineFaqActivity.PUBLIC_VALUE);
								bundle.putString(MineFaqActivity.LOADURL, loadUrl);
								bundle.putInt(MineFaqActivity.ISHOWTITLEBAR, Integer.parseInt(isShowNavigationBar));
								intent.putExtras(bundle);
								startActivity(intent);
							} else if (ppurl.startsWith(BAConstants.PP_BROADCAST)) {//排行榜
								MainRankActivity.openMineFaqActivity(SuspensionActivity.this);
							} else if (ppurl.startsWith(BAConstants.PP_RECHARGE)) {//充值
								BaseUtils.openActivity(SuspensionActivity.this, StoreH5RechargeActivity.class);
							} else if (ppurl.startsWith(BAConstants.PP_PERSONAL)) {//个人主页
								if (js.has("uid")) {
									int uid = js.getInt("uid");
									if (js.has("goodsid")) {//有道具商城
										int goodsid = js.getInt("goodsid");
										if (BAApplication.mLocalUserInfo != null) {
											SpaceUtils.toSpaceCustomByGoodsid(SuspensionActivity.this, BAApplication.mLocalUserInfo.uid.intValue(),
													BAApplication.mLocalUserInfo.sex.intValue(), goodsid);
										}
									} else {
										MainHallBiz.getInstance().getUserInfo(SuspensionActivity.this, uid, SuspensionActivity.this);
									}
								}
							} else if (ppurl.startsWith(BAConstants.PP_TO_NIUNIU_GAME)) {//跳转牛牛游戏厅
								/**
								 * 32位的CPU直接跳转
								 * 64位的CPU跳转牛牛APK
								 */
								Log.d("Aaron", "result=="+js.toString());
								String pkg = js.getString("pkg");
								String cls = js.getString("cls");
								final String apkUrl = js.getString("apkUrl");
								int versionCode = Integer.parseInt(js.getString("versionCode"));
								if (IAPKInfoUtil.isInstallApk(SuspensionActivity.this, pkg)) {
									BaseFile.delete(new File(IFileUtils.getSDCardRootDirectory() + BAConstants.PEIPEI_GAME_NIUNIUT_PATH));//删除下载的牛牛APK
									if (versionCode > IAPKInfoUtil.getVersionCode(SuspensionActivity.this, pkg)) {
										DownLoadAPKDialog(apkUrl, "有新版，请更新下载");
									} else {
										String auth = SharedPreferencesTools.getInstance(SuspensionActivity.this,
												BAApplication.mLocalUserInfo.uid.intValue() + "").getStringValueByKey(
												SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL);
										Bundle bundle = new Bundle();
										bundle.putString("auth", auth);
										bundle.putString("login_info", "4asfasfasfaaasfasf48");
										bundle.putString("silver", "0");
										ComponentName componentName = new ComponentName(pkg, cls);
										IAPKInfoUtil.startApk(SuspensionActivity.this, componentName, bundle);
									}
								} else {
									File file=new File(IFileUtils.getSDCardRootDirectory() + BAConstants.PEIPEI_GAME_NIUNIUT_PATH);
									if (file.exists()) {
										IAPKInfoUtil.InstallAPk(SuspensionActivity.this, IFileUtils.getSDCardRootDirectory() + BAConstants.PEIPEI_GAME_NIUNIUT_PATH);
									}else {
										DownLoadAPKDialog(apkUrl, "游戏未安装,请下载安装");
									}
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
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
		dialog = DialogFactory.showMsgDialog(SuspensionActivity.this, "", msg, "下载", "取消", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DialogFactory.dimissDialog(dialog);
				IAPKInfoUtil.APkDownLoad(SuspensionActivity.this, path, IFileUtils.getSDCardRootDirectory() + BAConstants.PEIPEI_GAME_NIUNIUT_PATH);
			}
		}, null);
	}

	@Override
	public void getUserInfoCallBack(int retCode, GoGirlUserInfo userinfo) {
		sendHandlerMessage(mHandler, Handler_userinfo, retCode, userinfo);
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case Handler_userinfo:
			if (msg.arg1 == 0) {
				GoGirlUserInfo info = (GoGirlUserInfo) msg.obj;
				SpaceUtils.toSpaceCustom(this, info.uid.intValue(), info.sex.intValue());
			}
		default:
			BaseUtils.cancelDialog();
			break;
		}
	}

	@SuppressWarnings("deprecation")
	private void setWidth() {
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); //为获取屏幕宽、高    

		LayoutParams p = getWindow().getAttributes(); //获取对话框当前的参数值    
		p.height = (int) (d.getHeight() * 0.8); //高度设置为屏幕的0.7   
		p.width = (int) (d.getWidth() * 0.9); //宽度设置为屏幕的0.9  
		getWindow().setAttributes(p); //设置生效  
		getWindow().setGravity(Gravity.CENTER); //设置靠右对齐 

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
				(int) (d.getHeight() * 0.6));
		params.bottomMargin = 15;
		mWebView.setLayoutParams(params);
	}

	@Override
	protected void initRecourse() {
		mWebView = (WebView) findViewById(R.id.faq_webview);
		btn_close = (Button) findViewById(R.id.btn_close);

		setWidth();
		btn_close.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_close:
			finish();
			break;
		}
	}

	@Override
	protected int initView() {
		return R.layout.activity_suspension;
	}

}
