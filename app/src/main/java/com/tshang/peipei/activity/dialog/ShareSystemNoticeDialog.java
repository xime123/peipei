package com.tshang.peipei.activity.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.tauth.Tencent;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseBitmap;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ShareType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.biz.space.SpaceCustomBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetShareData;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;
import com.tshang.peipei.vender.imageloader.core.assist.FailReason;
import com.tshang.peipei.vender.imageloader.core.listener.ImageLoadingListener;

public class ShareSystemNoticeDialog extends Dialog implements android.view.View.OnClickListener, BizCallBackGetShareData {

	private Activity context;

	private BAHandler mHandler;
	private Tencent mTencent;
	private IWXAPI mWXapi;
	private ChatMessageEntity chatMessageEntity;
	private String shareUrl = "http://www.tshang.com";
	private String title = "分享";

	public ShareSystemNoticeDialog(Activity context, ChatMessageEntity chatMessageEntity, BAHandler mHandler, Tencent mTencent, IWXAPI mWXapi) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		this.chatMessageEntity = chatMessageEntity;
		this.mHandler = mHandler;
		this.mTencent = mTencent;
		this.mWXapi = mWXapi;
		String type = chatMessageEntity.getType();
		if (!TextUtils.isEmpty(type)) {
			if (type.equals("0")) {
				title = "分享积分兑换";
			} else if (type.equals("1")) {
				title = "分享猜拳";
			} else if (type.equals("2")) {
				title = "分享时时彩";
			} else if (type.equals("3")) {
				title = "分享排行";
			} else if (type.equals("4")) {
				title = "分享排行";
			} else if (type.equals("5")) {
				title = "分享排行";
			} else if (type.equals("6")) {
				title = "分享活动";
			}
		}
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
		this.dismiss();
		switch (v.getId()) {
		case R.id.share_qq:
			handler.sendMessage(handler.obtainMessage(0, ShareType.SHARE_TO_QQ.getValue(), -1, shareUrl));
			break;
		case R.id.share_qqzone:
			handler.sendMessage(handler.obtainMessage(0, ShareType.SHARE_TO_QQZONE.getValue(), -1, shareUrl));
			break;
		case R.id.share_weixin:
			handler.sendMessage(handler.obtainMessage(0, ShareType.SHARE_TO_WX.getValue(), -1, shareUrl));
			break;
		case R.id.share_weixinline:
			handler.sendMessage(handler.obtainMessage(0, ShareType.SHARE_TO_LINE.getValue(), -1, shareUrl));
			break;
		case R.id.share_sina:
			handler.sendMessage(handler.obtainMessage(0, ShareType.SHARE_TO_SINA.getValue(), -1, shareUrl));
			break;
		case R.id.share_tencent:
			handler.sendMessage(handler.obtainMessage(0, ShareType.SHARE_TO_TWB.getValue(), -1, shareUrl));
			break;
		case R.id.share_cancel:
			break;

		default:
			break;
		}
	}

	@Override
	public void getShareUrl(int retCode, int type, String url) {
		handler.sendMessage(handler.obtainMessage(retCode, type, -1, url));
	}

	//分享到新浪微博
	private void shareToSina(final String url) {
		// 创建微博 SDK 接口实例
		final IWeiboShareAPI mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, BAConstants.SINA_APP_KEY);

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
				final int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
				final SpaceCustomBiz spaceCustomBiz2 = new SpaceCustomBiz();
				boolean b = false;
				if (chatMessageEntity.getType().equals("0")) {//说明是积分
					ImageLoader.getInstance().loadImage("php_img://" + chatMessageEntity.getCoverpickey(),
							ImageOptionsUtils.getShareOptions(context), new ImageLoadingListener() {

								@Override
								public void onLoadingStarted(String imageUri, View view) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
									boolean b = false;
									if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
										b = mWeiboShareAPI.sendRequest(context,
												spaceCustomBiz2.sendMultiMessage(title, loadedImage, url, chatMessageEntity.getDesc()));
									} else {
										b = mWeiboShareAPI.sendRequest(context,
												spaceCustomBiz2.sendSingleMessage(title, loadedImage, url, chatMessageEntity.getDesc()));
									}

									if (b) {
										//TODO 分享
										if (BAApplication.mLocalUserInfo != null) {
											SpaceCustomBiz sBiz = new SpaceCustomBiz();
											if (BAApplication.mLocalUserInfo != null) {
												sBiz.shareTask(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
														BAApplication.mLocalUserInfo.uid.intValue(), ShareType.SHARE_TO_SINA.getValue());
											}
										}
									}
								}

								@Override
								public void onLoadingCancelled(String imageUri, View view) {

								}
							});
				} else {

					if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
						b = mWeiboShareAPI.sendRequest(context, spaceCustomBiz2.sendMultiMessage(title,
								BitmapFactory.decodeResource(context.getResources(), R.drawable.logo), url, chatMessageEntity.getDesc()));
					} else {
						b = mWeiboShareAPI.sendRequest(context, spaceCustomBiz2.sendSingleMessage(title,
								BitmapFactory.decodeResource(context.getResources(), R.drawable.logo), url, chatMessageEntity.getDesc()));
					}

					if (b) {
						//TODO 分享
						if (BAApplication.mLocalUserInfo != null) {
							SpaceCustomBiz sBiz = new SpaceCustomBiz();
							if (BAApplication.mLocalUserInfo != null) {
								sBiz.shareTask(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
										BAApplication.mLocalUserInfo.uid.intValue(), ShareType.SHARE_TO_SINA.getValue());
							}
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
			int retCode = msg.what;
			if (retCode != 0) {
				return;
			}
			final String url = (String) msg.obj;
			final SpaceCustomBiz space = new SpaceCustomBiz();
			super.handleMessage(msg);
			switch (msg.arg1) {
			case 0:
				if (chatMessageEntity.getType().equals("0")) {//说明是积分
					String picKey = chatMessageEntity.getCoverpickey();
					if (TextUtils.isEmpty(picKey)) {
						picKey = "";
					}
					space.shareToQQ(context, mTencent, picKey, title, url, chatMessageEntity.getDesc(), mHandler);
				} else {
					space.shareToQQ(context, mTencent, BAConstants.SHARE_IMG, title, url, chatMessageEntity.getDesc(), mHandler);
				}
				break;
			case 1:
				if (isWXAppInstalledAndSupported(mWXapi)) {

					if (chatMessageEntity.getType().equals("0")) {//说明是积分
						ImageLoader.getInstance().loadImage("php_img://" + chatMessageEntity.getCoverpickey(),
								ImageOptionsUtils.getShareOptions(context), new ImageLoadingListener() {

									@Override
									public void onLoadingStarted(String imageUri, View view) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
										byte[] bitmapBytes = null;
										if (loadedImage != null) {
											bitmapBytes = BaseBitmap.bmpToByteArray(loadedImage, true);
										}
										space.shareToWX(context, chatMessageEntity.getDesc(), url, bitmapBytes, SendMessageToWX.Req.WXSceneSession,
												title, mWXapi);
									}

									@Override
									public void onLoadingCancelled(String imageUri, View view) {

									}
								});
					} else {
						space.shareToWX(context, chatMessageEntity.getDesc(), url, null, SendMessageToWX.Req.WXSceneSession, title, mWXapi);
					}

				} else {
					BaseUtils.showTost(context, R.string.str_wetchat_install);
				}
				break;
			case 2:
				if (isWXAppInstalledAndSupported(mWXapi)) {

					if (chatMessageEntity.getType().equals("0")) {//说明是积分
						ImageLoader.getInstance().loadImage("php_img://" + chatMessageEntity.getCoverpickey(),
								ImageOptionsUtils.getShareOptions(context), new ImageLoadingListener() {

									@Override
									public void onLoadingStarted(String imageUri, View view) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
										byte[] bitmapBytes = null;
										if (loadedImage != null) {
											bitmapBytes = BaseBitmap.bmpToByteArray(loadedImage, true);
										}
										space.shareToWX(context, chatMessageEntity.getDesc(), url, bitmapBytes, SendMessageToWX.Req.WXSceneTimeline,
												title, mWXapi);
									}

									@Override
									public void onLoadingCancelled(String imageUri, View view) {

									}
								});
					} else {
						space.shareToWX(context, chatMessageEntity.getDesc(), url, null, SendMessageToWX.Req.WXSceneTimeline, title, mWXapi);
					}

				} else {
					BaseUtils.showTost(context, R.string.str_wetchat_install);
				}
				break;
			case 3:
				shareToSina(url);
				break;
			case 4:
				space.shareToTencent(context, mTencent, url, mHandler);
				break;
			case 5:
				if (chatMessageEntity.getType().equals("0")) {//说明是积分
					String picKey = chatMessageEntity.getCoverpickey();
					if (TextUtils.isEmpty(picKey)) {
						picKey = "";
					}
					space.shareToQZone(context, mTencent, picKey, title, url, chatMessageEntity.getDesc(), mHandler);
				} else {
					space.shareToQZone(context, mTencent, BAConstants.SHARE_IMG, title, url, chatMessageEntity.getDesc(), mHandler);
				}
				break;
			default:
				break;
			}
		}

	};

	private boolean isWXAppInstalledAndSupported(IWXAPI api) {
		return api.isWXAppInstalled() && api.isWXAppSupportAPI();
	}
}
