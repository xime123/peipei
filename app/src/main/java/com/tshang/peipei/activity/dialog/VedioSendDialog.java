package com.tshang.peipei.activity.dialog;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.reward.RewardListActivity;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatDes;
import com.tshang.peipei.base.babase.BAConstants.ChatFromType;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.chat.BaiduCloudUtils;
import com.tshang.peipei.model.biz.chat.ChatRecordBiz;
import com.tshang.peipei.model.biz.chat.VideoUtils;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.operate.ChatOperate;

/**
 * 压缩完成后提示发送对话框
 * @author Jeff
 *
 */
public class VedioSendDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener {

	private Activity context;
	private String strSize;//视频大小
	private String filePath;//视频本地路径
	private BAHandler mHandler;
	private boolean isCancelUpload = false;//取消上传
	private int mFriendUid;
	private String mFriendNick;
	private int mFriendSex;
	private boolean isGroupChat;
	private int from;

	public boolean isCancelUpload() {
		return isCancelUpload;
	}

	public void setCancelUpload(boolean isCancelUpload) {
		this.isCancelUpload = isCancelUpload;
	}

	public VedioSendDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public VedioSendDialog(Activity context, int theme, String strSize, String filePath, int mFriendUid, String mFriendNick, int mFriendSex,
			BAHandler mHandler, boolean isGroupChat, int from) {
		super(context, theme);
		this.context = context;
		this.strSize = strSize;
		this.filePath = filePath;
		this.mFriendUid = mFriendUid;
		this.mFriendNick = mFriendNick;
		this.mFriendSex = mFriendSex;
		this.mHandler = mHandler;
		this.isGroupChat = isGroupChat;
		this.from = from;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_view);
		TextView tvMsg = (TextView) findViewById(R.id.msg);
		String strToast = context.getString(R.string.str_video_send_toast);
		if (TextUtils.isEmpty(strSize)) {
			strSize = "0KB";
		}

		tvMsg.setText(String.format(strToast, strSize));

		Button btn = (Button) findViewById(R.id.ok_sure);
		btn.setText(R.string.ok);
		btn.setOnClickListener(this);
		findViewById(R.id.ok_cancel).setOnClickListener(this);
		//		showDialog();
	}

	public void showDialog() {
		try {
			windowDeploy();
			// 设置触摸对话框意外的地方取消对话框
			setCanceledOnTouchOutside(true);
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 设置窗口显示
	public void windowDeploy() {
		Window window = getWindow(); // 得到对话框
		final WindowManager.LayoutParams wlps = window.getAttributes();
		wlps.width = context.getResources().getDisplayMetrics().widthPixels * 3 / 4;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		wlps.gravity = Gravity.CENTER;
		window.setAttributes(wlps);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok_cancel:
			this.dismiss();
			break;
		case R.id.ok_sure://发送
			this.dismiss();
			if (!TextUtils.isEmpty(filePath)) {
				sendThread();
			}
			break;
		default:
			break;
		}

	}

	private void sendThread() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				File file = new File(filePath);

				if (file != null && file.exists()) {
					String name = VideoUtils.getMd5ByFile(file);
					if (!TextUtils.isEmpty(name)) {
						long time = System.currentTimeMillis();
						boolean isUploadSuccess = false;
						if (isGroupChat) {
							String path = "peipei";
							if (BAConstants.IS_TEST) {
								path = "peipeitest2";
							}
							if (BaiduCloudUtils.getObjectMetaFromCloud("/" + path + "/" + name)) {
								isUploadSuccess = true;
							} else {//文件不存在
								try {
									BaiduCloudUtils.putFileToCloud(file);
									isUploadSuccess = true;
								} catch (Exception e1) {
									isUploadSuccess = false;
									e1.printStackTrace();
								}

							}
						} else {
							boolean b = ChatRecordBiz.save1Msg(name.getBytes(),
									from == RewardListActivity.CHAT_FROM_REWARD ? MessageType.GOGIRL_DATA_TYPE_ANONYM_VEDIO.getValue()
											: MessageType.VIDEO.getValue(), 0, mFriendUid, context, ChatStatus.SENDING.getValue(), ChatDes.TO_FRIEDN
											.getValue(), "0", time, mFriendUid, mFriendNick, mFriendSex, null, null, null, "", null, null, 0,
									from != RewardListActivity.CHAT_FROM_REWARD ? ChatRecordBiz.PRIVATECHATTYPE : 3, 0);
							if (!b) {
								return;
							}
							ChatOperate chatDatabase = ChatOperate.getInstance(context, mFriendUid, isGroupChat);
							if (chatDatabase != null) {
								ChatDatabaseEntity cdbe = chatDatabase.getLastestMessage();
								mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.CHAT_VEDIO_SEND_APPEND_VALUE, cdbe));
								String path = "peipei";
								if (BAConstants.IS_TEST) {
									path = "peipeitest2";
								}
								if (BaiduCloudUtils.getObjectMetaFromCloud("/" + path + "/" + name)) {
									isUploadSuccess = true;
								} else {//文件不存在
									try {
										BaiduCloudUtils.putFileToCloud(file);
										isUploadSuccess = true;
									} catch (Exception e) {
										isUploadSuccess = false;
										e.printStackTrace();
									}
								}
								if (isUploadSuccess) {//上传成功
									mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.CHAT_VEDIO_SEND_SUCCESS_VALUE, 0, 0, cdbe));
								} else {//上传失败
									mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.CHAT_VEDIO_SEND_FAILED_VALUE, 0, 0, cdbe));
								}
							}
						}
					}
				}
			}
		}).start();
	}
}
