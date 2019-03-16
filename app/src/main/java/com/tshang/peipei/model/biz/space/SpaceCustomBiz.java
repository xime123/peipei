package com.tshang.peipei.model.biz.space;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.t.Weibo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.dialog.HintToastDialog;
import com.tshang.peipei.base.BaseBitmap;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAConstants.ShareType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.bizcallback.BizCallBackGetShareData;
import com.tshang.peipei.model.bizcallback.BizCallBackTipoffTopic;
import com.tshang.peipei.model.bizcallback.BizCallBackTipoffUser;
import com.tshang.peipei.model.request.RequestAddFootprint;
import com.tshang.peipei.model.request.RequestAddVoiceDesc;
import com.tshang.peipei.model.request.RequestAddVoiceDesc.IAddVoice;
import com.tshang.peipei.model.request.RequestFinishShareTask;
import com.tshang.peipei.model.request.RequestGetShareData;
import com.tshang.peipei.model.request.RequestGetShareData.GetShareData;
import com.tshang.peipei.model.request.RequestGetVoice;
import com.tshang.peipei.model.request.RequestGetVoice.IGetVocie;
import com.tshang.peipei.model.request.RequestTipoffTopic;
import com.tshang.peipei.model.request.RequestTipoffTopic.iTIPOFFTOPIC;
import com.tshang.peipei.model.request.RequestTipoffUser;
import com.tshang.peipei.model.request.RequestTipoffUser.ITipoffUser;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

/**
 * @Title: SpaceCustomBiz.java 
 *
 * @Description: 个人空间操作类，举报，分享等操作
 *
 * @author allen  
 *
 * @date 2014-4-22 下午4:41:53 
 *
 * @version V1.0   
 */
public class SpaceCustomBiz implements ITipoffUser, GetShareData, iTIPOFFTOPIC {

	private BizCallBackTipoffUser mTipoffUser;
	private BizCallBackGetShareData mGetShare;
	private BizCallBackTipoffTopic mTipoffTopic;

	/**
	 * 分享到微信
	 * 
	 * @param text
	 *            文本
	 * @param bmp
	 *            图片
	 * @param index
	 *            好友或朋友圈
	 */
	public void shareToWX(Context mContext, String text, String url, byte[] bmp, int index, String title, IWXAPI api) {
		try {
			WXWebpageObject webpage = new WXWebpageObject();
			webpage.webpageUrl = url;

			// 用WXTextObject对象初始化一个WXMediaMessage对象
			WXMediaMessage msg = new WXMediaMessage();
			msg.mediaObject = webpage;
			// 发送文本类型的消息时，title字段不起作用
			if (index == SendMessageToWX.Req.WXSceneSession) {
				msg.title = title;
			} else {
				msg.title = title;
			}
			msg.description = text;
			Bitmap thumbBmp = null;
			if (bmp == null) {
				thumbBmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.logo_108_108);
				bmp = BaseBitmap.bmpToByteArray(thumbBmp, true);
			}
			msg.thumbData = bmp;// BitmapTools.bmpToByteArray(thumbBmp, true);
			// 构造一个Req
			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = "peipei" + System.currentTimeMillis(); // transaction字段用于唯一标识一个请求
			req.message = msg;
			req.scene = index;

			// 调用api接口发送数据到微信
			if (!api.sendReq(req)) {
				if (!api.openWXApp()) {
					new HintToastDialog(mContext, R.string.no_weixin, R.string.i_know).showDialog();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 第三方应用发送请求消息到微博，唤起微博分享界面。
	 * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
	 * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
	 * 
	 * @param text    分享的内容是否有文本
	 * @param image   分享的内容是否有图片
	 * @param hasWebpage 分享的内容是否有网页
	 * @param hasMusic   分享的内容是否有音乐
	 * @param hasVideo   分享的内容是否有视频
	 * @param hasVoice   分享的内容是否有声音
	 */
	public SendMultiMessageToWeiboRequest sendMultiMessage(String text, Bitmap image, String url, String des) {

		// 1. 初始化微博的分享消息
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		//		if (!TextUtils.isEmpty(text)) {
		//			weiboMessage.textObject = getTextObj(text);
		//		}
		//
		//		if (image != null) {
		//			weiboMessage.imageObject = getImageObj(image);
		//		}

		// 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
		if (!TextUtils.isEmpty(url)) {
			weiboMessage.mediaObject = getWebpageObj(url, text, des, image);
		}

		// 2. 初始化从第三方到微博的消息请求
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		// 用transaction唯一标识一个请求
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;

		return request;
	}

	/**
	 * 第三方应用发送请求消息到微博，唤起微博分享界面。
	 * 当{@link IWeiboShareAPI#getWeiboAppSupportAPI()} < 10351 时，只支持分享单条消息，即
	 * 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
	 * 
	 * @param hasText    分享的内容是否有文本
	 * @param hasImage   分享的内容是否有图片
	 * @param hasWebpage 分享的内容是否有网页
	 * @param hasMusic   分享的内容是否有音乐
	 * @param hasVideo   分享的内容是否有视频
	 */
	public SendMessageToWeiboRequest sendSingleMessage(String text, Bitmap image, String url, String des) {

		// 1. 初始化微博的分享消息
		// 用户可以分享文本、图片、网页、音乐、视频中的一种
		WeiboMessage weiboMessage = new WeiboMessage();
		//		if (!TextUtils.isEmpty(text)) {
		//			weiboMessage.mediaObject = getTextObj(text);
		//		}
		//		if (image != null) {
		//			weiboMessage.mediaObject = getImageObj(image);
		//		}
		if (!TextUtils.isEmpty(url)) {
			weiboMessage.mediaObject = getWebpageObj(text, url, des, image);
		}

		/*if (hasVoice) {
		    weiboMessage.mediaObject = getVoiceObj();
		}*/

		// 2. 初始化从第三方到微博的消息请求
		SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
		// 用transaction唯一标识一个请求
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.message = weiboMessage;

		return request;
	}

	//	/**
	//	* 创建文本消息对象。
	//	* 
	//	* @return 文本消息对象。
	//	*/
	//	private TextObject getTextObj(String text) {
	//		TextObject textObject = new TextObject();
	//		textObject.text = text;
	//		return textObject;
	//	}
	//
	//	/**
	//	* 创建图片消息对象。
	//	* 
	//	* @return 图片消息对象。
	//	*/
	//	private ImageObject getImageObj(Bitmap bmp) {
	//		ImageObject imageObject = new ImageObject();
	//		imageObject.setImageObject(bmp);
	//		return imageObject;
	//	}

	/**
	 * 创建多媒体（网页）消息对象。
	 * 
	 * @return 多媒体（网页）消息对象。
	 */
	private WebpageObject getWebpageObj(String url, String title, String des, Bitmap bmp) {
		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = Utility.generateGUID();
		mediaObject.title = title;
		mediaObject.description = des;

		// 设置 Bitmap 类型的图片到视频对象里
		mediaObject.setThumbImage(bmp);
		mediaObject.actionUrl = url;
		mediaObject.defaultText = des;
		return mediaObject;
	}

	public void tipoffUser(Activity activity, int tipuid, int reasonid, BizCallBackTipoffUser callback) {
		GoGirlUserInfo userEntity = BAApplication.mLocalUserInfo;
		if (userEntity == null) {
			return;
		}
		BaseUtils.showDialog(activity, R.string.submitting);
		RequestTipoffUser tipoffUser = new RequestTipoffUser();
		mTipoffUser = callback;
		tipoffUser.tipoffUser(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), tipuid, reasonid, this);
	}

	@Override
	public void tipoffUserCallBack(int retCode) {
		if (mTipoffUser != null) {
			mTipoffUser.tipoffUserCallBack(retCode);
		}

	}

	public void tipoffTop(Activity activity, int topicuid, int topicid, int reasonid, BizCallBackTipoffTopic callback) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}

		RequestTipoffTopic req = new RequestTipoffTopic();
		mTipoffTopic = callback;
		req.tipoffTopic(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), topicuid, topicid, reasonid, this);
	}

	@Override
	public void tipoffback(int retCode) {
		if (mTipoffTopic != null) {
			mTipoffTopic.tipoffTopic(retCode);
		}

	}

	//分享到QQ
	public void shareToQQ(Activity context, Tencent mTencent, String url, String title, String targetUrl, String summary, BAHandler mHandler) {
		if (mTencent == null)
			mTencent = Tencent.createInstance(BAConstants.QQ_APP_KEY, context);

		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, url);

		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, context.getString(R.string.app_name));
		mTencent.shareToQQ(context, params, new QQApiListener(mHandler));
	}

	//分享到QQ
	public void shareToQZone(Activity context, Tencent mTencent, String url, String title, String targetUrl, String summary, BAHandler mHandler) {
		if (mTencent == null)
			mTencent = Tencent.createInstance(BAConstants.QQ_APP_KEY, context);

		final Bundle params = new Bundle();

		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
		params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
		ArrayList<String> imageUrls = new ArrayList<String>();
		imageUrls.add(url);
		params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);

		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, context.getString(R.string.app_name));
		mTencent.shareToQzone(context, params, new QQApiListener(mHandler));

	}

	//分享到腾讯微博
	public void shareToTencent(final Activity context, Tencent mTencent, final String url, final BAHandler mHandler) {
		if (mTencent == null) {
			mTencent = Tencent.createInstance(BAConstants.QQ_APP_KEY, context);
		}
		if (mTencent.getQQToken() != null && mTencent.getQQToken().isSessionValid() && mTencent.getQQToken().getOpenId() != null) {
			Weibo mWeibo = new Weibo(context, mTencent.getQQToken());
			mWeibo.sendText(url, new TQQApiListener("add_t", false, context, mHandler));
		} else {
			final QQToken token = mTencent.getQQToken();
			IUiListener listener = new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject values) {
					Weibo mWeibo = new Weibo(context, token);
					mWeibo.sendText(url, new TQQApiListener("add_t", false, context, mHandler));
				}
			};

			mTencent.login(context, BAConstants.QQ_SCOPE, listener);
		}

	}

	private class TQQApiListener extends BaseUiListener {
		private String mScope = "get_info,add_t del_t add_pic_t,get_repost_list";
		private Boolean mNeedReAuth = false;
		private Activity context;
		private BAHandler mHandler;

		public TQQApiListener(String scope, boolean needReAuth, Activity activty, BAHandler handler) {
			this.mScope = scope;
			this.mNeedReAuth = needReAuth;
			this.context = activty;
			this.mHandler = handler;
		}

		@Override
		public void onComplete(Object response) {
			final Activity activity = context;
			try {
				JSONObject json = (JSONObject) response;
				int ret = json.getInt("ret");
				if (json.has("data")) {
					JSONObject data = json.getJSONObject("data");
					if (data.has("id")) {
						BaseLog.w("TQQShare", "share id=" + data.getString("id"));
					}
				}
				if (ret == 0) {
					Message msg = mHandler.obtainMessage(HandlerType.OPERATE_SUCCESS, mScope);
					Bundle data = new Bundle();
					data.putString("response", response.toString());
					msg.setData(data);
					msg.arg1 = ShareType.SHARE_TO_TWB.getValue();
					mHandler.sendMessage(msg);
				} else if (ret == 100030) {
					if (mNeedReAuth) {
						Runnable r = new Runnable() {
							public void run() {
								BAApplication.mQQAuth.reAuth(activity, mScope, new BaseUiListener());
							}
						};
						context.runOnUiThread(r);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private class QQApiListener extends BaseUiListener {
		private BAHandler mHandler;

		public QQApiListener(BAHandler handler) {
			this.mHandler = handler;
		}

		@Override
		public void onComplete(Object response) {
			try {
				JSONObject json = (JSONObject) response;
				int ret = json.getInt("ret");
				if (ret == 0) {
					Message msg = mHandler.obtainMessage(HandlerType.OPERATE_SUCCESS);
					msg.arg1 = ShareType.SHARE_TO_QQ.getValue();
					Bundle data = new Bundle();
					data.putString("response", response.toString());
					msg.setData(data);
					mHandler.sendMessage(msg);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private class BaseUiListener implements IUiListener {

		public BaseUiListener() {}

		protected void doComplete(JSONObject values) {}

		@Override
		public void onError(UiError e) {
			BaseLog.w("Tencent", "error");
		}

		@Override
		public void onCancel() {
			BaseLog.w("Tencent", "cancel");
		}

		@Override
		public void onComplete(Object arg0) {
			doComplete((JSONObject) arg0);

		}
	}

	public void getShareUrl(byte[] auth, int ver, int uid, int fuid, int type, BizCallBackGetShareData callback) {
		RequestGetShareData req = new RequestGetShareData();
		mGetShare = callback;
		req.getShareData(auth, ver, uid, fuid, type, this);
	}

	@Override
	public void getShare(int retCode, int type, String url) {
		if (mGetShare != null) {
			mGetShare.getShareUrl(retCode, type, url);
		}
	}

	//我看过的人
	public void addFootprint(byte[] auth, int ver, int uid, int hostuid) {
		RequestAddFootprint req = new RequestAddFootprint();
		req.addFootPrint(auth, ver, uid, hostuid);
	}

	//分享上报
	public void shareTask(byte[] auth, int ver, int uid, int shareto) {
		RequestFinishShareTask req = new RequestFinishShareTask();
		req.finishShareTask(auth, ver, uid, shareto);
	}

	//上报语音
	public void addVoiceDes(byte[] auth, int ver, int uid, byte[] data, int len, IAddVoice callback) {
		RequestAddVoiceDesc reqAddVoiceDesc = new RequestAddVoiceDesc();
		reqAddVoiceDesc.addVoiceDesc(auth, ver, uid, data, len, callback);
	}

	//获取广播
	public void getVoiceDes(byte[] auth, int ver, byte[] key, IGetVocie voice) {
		RequestGetVoice requestGetVoice = new RequestGetVoice();
		requestGetVoice.getVoice(auth, BAApplication.app_version_code, key, voice);
	}
}
