package com.tshang.peipei.activity.dialog;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.harem.ManagerHaremActivity;
import com.tshang.peipei.activity.harem.SannomiyaSixHomesActivity;
import com.tshang.peipei.activity.reward.RewardListActivity;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfo;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseString;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.chat.ChatRecordBiz;
import com.tshang.peipei.model.biz.chat.ChatSessionManageBiz;

public class ChatMoreDialog extends Dialog implements android.view.View.OnClickListener {

	private Activity context;
	private int mFriendUid = -1;
	private String mFriendNick = "";
	private BAHandler mHandler;
	private boolean isGroupChat;
	private TextView tvMiddle;
	private GroupInfo info;
	private int from;

	public ChatMoreDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public ChatMoreDialog(Activity context, int theme, int otherUid, String mFriendNick, BAHandler mHandler, boolean isGroupChat, GroupInfo info,
			int from) {
		super(context, theme);
		this.context = context;
		this.mFriendUid = otherUid;
		this.mFriendNick = mFriendNick;
		this.mHandler = mHandler;
		this.isGroupChat = isGroupChat;
		this.info = info;
		this.from = from;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.popupwindow_chat_more);
		findViewById(R.id.more_clean_chat).setOnClickListener(this);
		tvMiddle = (TextView) findViewById(R.id.more_add_black);
		tvMiddle.setOnClickListener(this);
		if (isGroupChat && info != null) {
			if (BAApplication.mLocalUserInfo != null) {
				if (BAApplication.mLocalUserInfo.uid.intValue() == info.owner.intValue()) {
					tvMiddle.setText(R.string.str_sannomiya_six_homes);
				} else {
					tvMiddle.setText(R.string.manage);
				}
			}
		}
		if (mFriendUid == BAConstants.PEIPEI_XIAOPEI || mFriendUid == BAConstants.PEIPEI_CHAT_XIAOPEI
				|| mFriendUid == BAConstants.PEIPEI_CHAT_TONGZHI) {
			tvMiddle.setVisibility(View.GONE);
		}

		findViewById(R.id.more_cancel).setOnClickListener(this);

		if (from == RewardListActivity.CHAT_FROM_REWARD) {
			tvMiddle.setVisibility(View.GONE);
		}
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
		case R.id.more_clean_chat:
			clearMessage();
			break;
		case R.id.more_add_black:
			if (isGroupChat) {
				if (BAApplication.mLocalUserInfo != null) {
					if (BAApplication.mLocalUserInfo.uid.intValue() == info.owner.intValue()) {
						Bundle bundle = new Bundle();
						bundle.putInt("groupid", mFriendUid);
						BaseUtils.openActivity(context, SannomiyaSixHomesActivity.class, bundle);
					} else {
						Bundle bundle = new Bundle();
						bundle.putInt("from", ManagerHaremActivity.FROM_CHAT);
						bundle.putInt("groupid", mFriendUid);
						BaseUtils.openActivity(context, ManagerHaremActivity.class, bundle);
					}
				}

			} else {
				mHandler.sendEmptyMessage(HandlerValue.CHAT_ADD_BLACK_LIST_VALUE);
			}
			break;

		case R.id.more_cancel:
			break;

		default:
			break;
		}
	}

	private void clearMessage() {
		final List<String> videoStrings = new ChatRecordBiz().getMessageByTpye(context, mFriendUid, MessageType.VIDEO.getValue(), isGroupChat);
		new Thread(new Runnable() {//清除本地的视频文件

					@Override
					public void run() {
						if (SdCardUtils.isExistSdCard()) {
							for (String string : videoStrings) {
								File file = new File(SdCardUtils.getInstance().getVedioDirectory() + "/" + string + ".mp4");
								if (file != null && file.exists()) {
									file.delete();
								}
							}
						}

						String path = BaseString.getFilePath(context, mFriendUid, MessageType.IMAGE.getValue());

						File f = new File(path);
						if (f != null && f.exists()) {
							BaseFile.delete(f);
						}

						path = BaseString.getFilePath(context, mFriendUid, MessageType.VOICE.getValue());
						f = new File(path);
						if (f != null && f.exists()) {
							BaseFile.delete(f);
						}
					}
				}).start();
		boolean b = ChatRecordBiz.clearDbMessage(context, mFriendUid, isGroupChat);
		if (b) {
			if (isGroupChat) {
				ChatSessionManageBiz.upDataSession(context, context.getString(R.string.no_message), mFriendNick, mFriendUid,
						System.currentTimeMillis(), 1);
			} else {
				if (from == RewardListActivity.CHAT_FROM_REWARD) {
					ChatSessionManageBiz.upDataSession(context, context.getString(R.string.no_message), mFriendNick, mFriendUid,
							System.currentTimeMillis(), 3);
				} else
					ChatSessionManageBiz.upDataSession(context, context.getString(R.string.no_message), mFriendNick, mFriendUid,
							System.currentTimeMillis(), 0);
			}
			mHandler.sendEmptyMessage(HandlerValue.CHAT_CLEAR_CONTENT_INFO_SUCCESS_VALUE);
		}
	}
}
