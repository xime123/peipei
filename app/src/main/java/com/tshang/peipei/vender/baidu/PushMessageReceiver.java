package com.tshang.peipei.vender.baidu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.main.MainActivity;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.NotificationManagerId;
import com.tshang.peipei.base.babase.BAConstants.PushMessageType;
import com.tshang.peipei.model.biz.PeiPeiAppStartBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackReportAppInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;

/**
 * Push消息处理receiver
 */
public class PushMessageReceiver extends com.baidu.android.pushservice.PushMessageReceiver implements BizCallBackReportAppInfo {
	/** TAG to Log */
	public static final String TAG = "baidu_push";

	/**
	 * 调用PushManager.startWork后，sdk将对push server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。
	 * 如果您需要用单播推送，需要把这里获取的channel id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
	 * 
	 * @param context
	 *          BroadcastReceiver的执行Context
	 * @param errorCode
	 *          绑定接口返回值，0 - 成功
	 * @param appid 
	 *          应用id。errorCode非0时为null
	 * @param userId
	 *          应用user id。errorCode非0时为null
	 * @param channelId
	 *          应用channel id。errorCode非0时为null
	 * @param requestId
	 *          向服务端发起的请求id。在追查问题时有用；
	 * @return
	 *     none
	 */
	@Override
	public void onBind(Context context, int errorCode, String appid, String userId, String channelId, String requestId) {
		String responseString = "onBind errorCode=" + errorCode + " appid=" + appid + " userId=" + userId + " channelId=" + channelId + " requestId="
				+ requestId;
		BaseLog.d(TAG, responseString);

		if (!TextUtils.isEmpty(userId)) {
			SharedPreferencesTools.getInstance(context).saveStringKeyValue(userId, BAConstants.PEIPEI_BAIDU_USERID);
			if (BaseTimes.isTimeDistanceNow(SharedPreferencesTools.getInstance(context).getLongKeyValue(BAConstants.PEIPEI_BAIDU_TIME),
					60 * 60 * 24 * 1000)) {
				PeiPeiAppStartBiz appStartBiz = new PeiPeiAppStartBiz();
				SharedPreferencesTools.getInstance(context).saveLongKeyValue(System.currentTimeMillis(), BAConstants.PEIPEI_BAIDU_TIME);
				appStartBiz.reportAppInfoToSer(context, userId, this);
			}
		}

		// 绑定成功，设置已绑定flag，可以有效的减少不必要的绑定请求
		if (errorCode == 0) {
			BaiduUtils.setBind(context, true);
		}
		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, responseString);
	}

	/**
	 * 接收透传消息的函数。
	 * 
	 * @param context 上下文
	 * @param message 推送的消息
	 * @param customContentString 自定义内容,为空或者json字符串
	 */
	@Override
	public void onMessage(Context context, String message, String customContentString) {
		String messageString = "透传消息 message=\"" + message + "\" customContentString=" + customContentString;
		BaseLog.d(TAG, messageString);

		// 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
		if (customContentString != null & TextUtils.isEmpty(customContentString)) {
			JSONObject customJson = null;
			try {
				customJson = new JSONObject(customContentString);
				String myvalue = null;
				if (customJson.isNull("mykey")) {
					myvalue = customJson.getString("mykey");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		String data = null;
		try {
			JSONObject jo = new JSONObject(message);
			if (jo.has("description")) {
				data = jo.getString("description");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (!TextUtils.isEmpty(data)) {
			pushMessage(context, data);
		}
		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, messageString);
	}

	/**
	 * 接收通知点击的函数。注：推送通知被用户点击前，应用无法通过接口获取通知的内容。
	 * 
	 * @param context 上下文
	 * @param title 推送的通知的标题
	 * @param description 推送的通知的描述
	 * @param customContentString 自定义内容，为空或者json字符串
	 */
	@Override
	public void onNotificationClicked(Context context, String title, String description, String customContentString) {
		String notifyString = "通知点击 title=\"" + title + "\" description=\"" + description + "\" customContent=" + customContentString;
		BaseLog.d(TAG, notifyString);

		// 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
		if (customContentString != null & TextUtils.isEmpty(customContentString)) {
			JSONObject customJson = null;
			try {
				customJson = new JSONObject(customContentString);
				String myvalue = null;
				if (customJson.isNull("mykey")) {
					myvalue = customJson.getString("mykey");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, notifyString);
	}

	/**
	 * setTags() 的回调函数。
	 * 
	 * @param context 上下文
	 * @param errorCode 错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
	 * @param failTags 设置失败的tag
	 * @param requestId 分配给对云推送的请求的id
	 */
	@Override
	public void onSetTags(Context context, int errorCode, List<String> sucessTags, List<String> failTags, String requestId) {
		String responseString = "onSetTags errorCode=" + errorCode + " sucessTags=" + sucessTags + " failTags=" + failTags + " requestId="
				+ requestId;
		BaseLog.d(TAG, responseString);

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, responseString);
	}

	/**
	 * delTags() 的回调函数。
	 * 
	 * @param context 上下文
	 * @param errorCode 错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
	 * @param failTags 删除失败的tag
	 * @param requestId 分配给对云推送的请求的id
	 */
	@Override
	public void onDelTags(Context context, int errorCode, List<String> sucessTags, List<String> failTags, String requestId) {
		String responseString = "onDelTags errorCode=" + errorCode + " sucessTags=" + sucessTags + " failTags=" + failTags + " requestId="
				+ requestId;
		BaseLog.d(TAG, responseString);

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, responseString);
	}

	/**
	 * listTags() 的回调函数。
	 * 
	 * @param context 上下文
	 * @param errorCode  错误码。0表示列举tag成功；非0表示失败。
	 * @param tags 当前应用设置的所有tag。
	 * @param requestId 分配给对云推送的请求的id
	 */
	@Override
	public void onListTags(Context context, int errorCode, List<String> tags, String requestId) {
		String responseString = "onListTags errorCode=" + errorCode + " tags=" + tags;
		BaseLog.d(TAG, responseString);

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, responseString);
	}

	/**
	 * PushManager.stopWork() 的回调函数。
	 * 
	 * @param context 上下文
	 * @param errorCode 错误码。0表示从云推送解绑定成功；非0表示失败。
	 * @param requestId 分配给对云推送的请求的id
	 */
	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		String responseString = "onUnbind errorCode=" + errorCode + " requestId = " + requestId;
		BaseLog.d(TAG, responseString);

		// 解绑定成功，设置未绑定flag，
		if (errorCode == 0) {
			BaiduUtils.setBind(context, false);
		}
		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, responseString);
	}

	private void updateContent(Context context, String content) {
		BaseLog.d(TAG, "updateContent");
		String logText = "" + BaiduUtils.logStringCache;

		if (!logText.equals("")) {
			logText += "\n";
		}

		SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
		logText += sDateFormat.format(new Date()) + ": ";
		logText += content;

		BaiduUtils.logStringCache = logText;

		//		Intent intent = new Intent();
		//		intent.setClass(context.getApplicationContext(), MainActivity.class);
		//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//		context.getApplicationContext().startActivity(intent);
	}

	/**
	 * 处理push消息
	 * 
	 * @param context
	 * @param data
	 *            获取到的push信息字符串
	 */
	@SuppressLint("StringFormatMatches")
	@SuppressWarnings("deprecation")
	private void pushMessage(Context context, String data) {
		if (BAApplication.isOnLine) {
			return;
		}
		// 发送notification
		String[] push = data.split(";");
		String[] temp = new String[2];
		String type = null, msg = null;
		int uid = 0;
		for (int i = 0; i < push.length; i++) {
			temp = push[i].split(":");
			if (temp[0].equals("type")) {
				if (temp.length == 2)
					type = temp[1];
			}
			if (temp[0].equals("msg")) {
				if (temp.length == 2) {
					msg = temp[1];
				}
			}
			if (temp[0].equals("uid")) {
				if (temp.length == 2) {
					uid = Integer.parseInt(temp[1]);
				}
			}
		}

		if (TextUtils.isEmpty(type)) {
			return;
		}

		if (BAApplication.mLocalUserInfo != null) {
			if (uid != BAApplication.mLocalUserInfo.uid.intValue()) {
				return;
			}
		}
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		builder.setSmallIcon(R.drawable.logo);
		builder.setTicker(msg);
		builder.setWhen(System.currentTimeMillis());
		builder.setAutoCancel(true);
		if (System.currentTimeMillis() - SharedPreferencesTools.getInstance(context).getLongKeyValue(BAConstants.PEIPEI_NOTIFICATION_CHAT_TIME) > 5000) {
			SharedPreferencesTools.getInstance(context).saveLongKeyValue(System.currentTimeMillis(), BAConstants.PEIPEI_NOTIFICATION_CHAT_TIME);
			boolean sound = SharedPreferencesTools.getInstance(context).getBooleanKeyValue(BAConstants.SOUND);
			boolean shake = SharedPreferencesTools.getInstance(context).getBooleanKeyValue(BAConstants.SHAKE);
			if (!sound && !shake) {
				builder.setDefaults(Notification.DEFAULT_ALL);
			} else if (!sound) {
				builder.setDefaults(Notification.DEFAULT_SOUND);
			} else if (!shake) {
				builder.setDefaults(Notification.DEFAULT_VIBRATE);
			}
		}
		Intent i = null;
		boolean loginFlag = BAApplication.mLocalUserInfo == null ? false : true;
		if (PushMessageType.DYNAMIC.getValue() == Integer.parseInt(type) && loginFlag) {// 动态
			i = new Intent(context, MainActivity.class);
		} else if (PushMessageType.CHAT.getValue() == Integer.parseInt(type) && loginFlag) {// 私聊
			i = new Intent(context, MainActivity.class);
			i.putExtra("pushmessage", true);
			i.putExtra("pushtype", PushMessageType.CHAT.getValue());
		} else if (PushMessageType.GIFT.getValue() == Integer.parseInt(type) && loginFlag) {//礼物
			i = new Intent(context, MainActivity.class);
		} else if (20 == Integer.parseInt(type) && loginFlag) {//广播
		}
		if (i == null) {
			return;
		}

		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// PendingIntent
		PendingIntent contentIntent = PendingIntent.getActivity(context, R.string.app_name, i, PendingIntent.FLAG_UPDATE_CURRENT);

		if (PushMessageType.DYNAMIC.getValue() == Integer.parseInt(type) && loginFlag) {// 动态
			builder.setContentTitle(context.getString(R.string.app_name));
			builder.setContentText(msg);
			Notification n = builder.build();
			nm.cancel(NotificationManagerId.PUSH_DYNAMIC);
			nm.notify(NotificationManagerId.PUSH_DYNAMIC, n);
		} else if (PushMessageType.CHAT.getValue() == Integer.parseInt(type) && loginFlag) {// 私聊
			int num = SharedPreferencesTools.getInstance(context, uid + "").getIntValueByKeyToZero(BAConstants.PEIPEI_NOTIFICATION_PUSH_CHAT_NUM) + 1;
			SharedPreferencesTools.getInstance(context, uid + "").saveIntKeyValue(num, BAConstants.PEIPEI_NOTIFICATION_PUSH_CHAT_NUM);
			msg = String.format(context.getString(R.string.push_chat_noticetion), num);
			builder.setContentTitle(context.getString(R.string.app_name));
			builder.setContentText(msg);
			Notification n = builder.build();
			nm.cancel(NotificationManagerId.PUSH_CHAT);
			nm.notify(NotificationManagerId.PUSH_CHAT, n);
		} else {
			builder.setContentTitle(context.getString(R.string.app_name));
			builder.setContentText(msg);
			Notification n = builder.build();
			nm.cancel(NotificationManagerId.PUSH_OTHER);
			nm.notify(NotificationManagerId.PUSH_OTHER, n);
		}
	}

	@Override
	public void reportAppInfoBack(int retCode) {
		if (retCode == 0) {
			BaseLog.i(TAG, "report success");
		} else {
			BaseLog.i(TAG, "report failed");
		}

	}

	@Override
	public void onNotificationArrived(Context arg0, String arg1, String arg2, String arg3) {
		// TODO Auto-generated method stub
		
	}
}
