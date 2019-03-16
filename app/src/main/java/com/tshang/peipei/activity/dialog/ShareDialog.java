package com.tshang.peipei.activity.dialog;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.tauth.Tencent;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseBitmap;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ShareType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.biz.space.SpaceCustomBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetShareData;
import com.tshang.peipei.model.request.RequestHeadToHttp;
import com.tshang.peipei.model.request.RequestHeadToHttp.IHeadToHttp;
import com.tshang.peipei.model.request.RequestLoadingPic;
import com.tshang.peipei.model.request.RequestLoadingPic.ILoadingPicHttp;

public class ShareDialog extends Dialog implements android.view.View.OnClickListener, BizCallBackGetShareData, IHeadToHttp, ILoadingPicHttp {
	private Activity context;
	private int otherUid = -1;
	private BAHandler mHandler;
	private Tencent mTencent;
	private IWXAPI mWXapi;
	private String nick;
	private Bitmap bitmap;
	private String qString;

	public ShareDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public ShareDialog(Activity context, int theme, int otherUid, String nick, BAHandler mHandler, Tencent mTencent, IWXAPI mWXapi) {
		super(context, theme);
		this.context = context;
		this.otherUid = otherUid;
		this.mHandler = mHandler;
		this.mTencent = mTencent;
		this.mWXapi = mWXapi;
		this.nick = nick;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.popupwindow_share);
		findViewById(R.id.share_qq).setOnClickListener(this);
		findViewById(R.id.share_qqzone).setOnClickListener(this);
		findViewById(R.id.share_weixin).setOnClickListener(this);
		findViewById(R.id.share_weixinline).setOnClickListener(this);
		findViewById(R.id.share_sina).setOnClickListener(this);
		findViewById(R.id.share_tencent).setOnClickListener(this);
		findViewById(R.id.share_cancel).setOnClickListener(this);
	}

	public void showDialog(int x, int y) {
		try {
			windowDeploy(x, y);

			// 设置触摸对话框意外的地方取消对话框
			setCanceledOnTouchOutside(true);
			show();
		} catch (Exception e) {
			try {
//				MobclickAgent.reportError(BAApplication.getInstance().getApplicationContext(), "自定义对话框显示");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	// 设置窗口显示
	public void windowDeploy(int x, int y) {

		final Window w = getWindow();
		w.setWindowAnimations(R.style.anim_popwindow_bottombar); // 设置窗口弹出动画
		final WindowManager.LayoutParams wlps = w.getAttributes();
		wlps.width = WindowManager.LayoutParams.MATCH_PARENT;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.gravity = Gravity.BOTTOM;
		w.setAttributes(wlps);
	}

	@Override
	public void onClick(View v) {
		//		if (BAApplication.mUserEntity != null)
		//			MobclickAgent.onEvent(context, "DiJiFenXiangRenShu", BAApplication.mUserEntity.getUid() + "");
		this.dismiss();
		RequestHeadToHttp req = new RequestHeadToHttp();
		switch (v.getId()) {
		case R.id.share_qq:
			req.loadHead(otherUid, ShareType.SHARE_TO_QQ.getValue(), this);
			break;
		case R.id.share_qqzone:
			req.loadHead(otherUid, ShareType.SHARE_TO_QQZONE.getValue(), this);
			break;
		case R.id.share_weixin:
			req.loadHead(otherUid, ShareType.SHARE_TO_WX.getValue(), this);
			break;
		case R.id.share_weixinline:
			req.loadHead(otherUid, ShareType.SHARE_TO_LINE.getValue(), this);
			break;
		case R.id.share_sina:
			req.loadHead(otherUid, ShareType.SHARE_TO_SINA.getValue(), this);
			break;
		case R.id.share_tencent:
			req.loadHead(otherUid, ShareType.SHARE_TO_TWB.getValue(), this);
			break;
		case R.id.share_cancel:
			break;

		default:
			break;
		}
	}

	private void getShareUrlToSvr(int type) {
		if (BAApplication.mLocalUserInfo != null)
			new SpaceCustomBiz().getShareUrl(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
					BAApplication.mLocalUserInfo.uid.intValue(), otherUid, type, this);
	}

	@Override
	public void getShareUrl(int retCode, int type, String url) {
		handler.sendMessage(handler.obtainMessage(type, retCode, -1, url));
	}

	//分享到新浪微博
	private void shareToSina(String url) {
		// 创建微博 SDK 接口实例
		IWeiboShareAPI mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, BAConstants.SINA_APP_KEY);

		// 获取微博客户端相关信息，如是否安装、支持 SDK 的版本
		boolean isInstalledWeibo = mWeiboShareAPI.isWeiboAppInstalled();

		// 如果未安装微博客户端，设置下载微博对应的回调
		if (!isInstalledWeibo) {
//			mWeiboShareAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
//				@Override
//				public void onCancel() {}
//			});
		}

		mWeiboShareAPI.registerApp();

		try {
			if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
				int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
				SpaceCustomBiz spaceCustomBiz2 = new SpaceCustomBiz();
				boolean b = false;
				if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
					b = mWeiboShareAPI.sendRequest(context,spaceCustomBiz2.sendMultiMessage(context.getString(R.string.share_mm), bitmap, url,
							String.format(context.getString(R.string.share_content), nick)));
				} else {
					b = mWeiboShareAPI.sendRequest(context,spaceCustomBiz2.sendSingleMessage(context.getString(R.string.share_mm), bitmap, url,
							String.format(context.getString(R.string.share_content), nick)));
				}

				if (b) {
					if (BAApplication.mLocalUserInfo != null) {
						SpaceCustomBiz sBiz = new SpaceCustomBiz();
						if (BAApplication.mLocalUserInfo != null) {
							sBiz.shareTask(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
									BAApplication.mLocalUserInfo.uid.intValue(), ShareType.SHARE_TO_SINA.getValue());
						}
					}
				}
			} else {
				//				BaseUtils.showTost(context, R.string.weibosdk_demo_not_support_api_hint);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			SpaceCustomBiz space = new SpaceCustomBiz();
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				space.shareToQQ(context, mTencent, TextUtils.isEmpty(qString) ? BAConstants.SHARE_IMG : qString,
						context.getString(R.string.share_mm), (String) msg.obj, String.format(context.getString(R.string.share_content), nick),
						mHandler);
				break;
			case 1:
				if (isWXAppInstalledAndSupported(mWXapi)) {
					space.shareToWX(context, String.format(context.getString(R.string.share_content), nick), (String) msg.obj,
							BaseBitmap.bmpToByteArray(bitmap, true), SendMessageToWX.Req.WXSceneSession, context.getString(R.string.share_mm), mWXapi);
				} else {
					BaseUtils.showTost(context, R.string.str_wetchat_install);
				}
				break;
			case 2:
				if (isWXAppInstalledAndSupported(mWXapi)) {
					space.shareToWX(context, String.format(context.getString(R.string.share_content), nick), (String) msg.obj,
							BaseBitmap.bmpToByteArray(bitmap, true), SendMessageToWX.Req.WXSceneTimeline, context.getString(R.string.share_mm),
							mWXapi);
				} else {
					BaseUtils.showTost(context, R.string.str_wetchat_install);
				}
				break;
			case 3:
				shareToSina((String) msg.obj);
				break;
			case 4:
				space.shareToTencent(context, mTencent, (String) msg.obj, mHandler);
				break;
			case 5:
				space.shareToQZone(context, mTencent, TextUtils.isEmpty(qString) ? BAConstants.SHARE_IMG : qString,
						context.getString(R.string.share_mm), (String) msg.obj, String.format(context.getString(R.string.share_content), nick),
						mHandler);
				break;
			case 20:
				if (msg.arg1 == 0) {
					qString = (String) msg.obj;
					if (msg.arg2 == 0 || msg.arg2 == 5) {
						getShareUrlToSvr(msg.arg2);
					} else {
						String saveImageName = qString.substring(qString.lastIndexOf("/"));
						File file = new File(SdCardUtils.getInstance().getLoadPicDir(), saveImageName);
						if (file != null && file.exists()) {
							bitmap = BaseFile.getImageFromFile(file.getAbsolutePath());
							getShareUrlToSvr(msg.arg2);
						} else {
							RequestLoadingPic req = new RequestLoadingPic();
							req.getLoadingPicShow(context, (String) msg.obj, 0, msg.arg2, ShareDialog.this);
						}
					}
				} else {
					getShareUrlToSvr(msg.arg2);
				}
				break;
			case 21:
				if (msg.arg1 == 0) {
					String path = (String) msg.obj;
					if (!TextUtils.isEmpty(path)) {
						bitmap = BaseFile.getImageFromFile(path);
					}
				}
				getShareUrlToSvr(msg.arg2);
				break;
			default:
				break;
			}
		}

	};

	private boolean isWXAppInstalledAndSupported(IWXAPI api) {
		return api.isWXAppInstalled() && api.isWXAppSupportAPI();
	}

	@Override
	public void resultLoadHead(int retCode, int type, String url) {
		handler.sendMessage(handler.obtainMessage(20, retCode, type, url));
	}

	@Override
	public void loadingPic(int retCode, int type, String str) {
		handler.sendMessage(handler.obtainMessage(21, retCode, type, str));
	}
}
