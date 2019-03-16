package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.ChatAdapter;
import com.tshang.peipei.activity.reward.RewardListActivity;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.ChatManageBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackSentChatMessage;
import com.tshang.peipei.model.entity.ChatMessageReceiptEntity;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;
import com.tshang.peipei.vender.imageloader.core.display.RoundedBitmapDisplayer;
import com.tshang.peipei.vender.micode.soundrecorder.Recorder;

/**
 * @Title: ChatVoiceDialog.java 
 *
 * @Description: 私聊语音阅后即焚对话框
 *
 * @author allen  
 *
 * @date 2014-7-17 下午1:42:54 
 *
 * @version V1.0   
 */
public class ChatVoiceDialog extends Dialog implements OnClickListener, BizCallBackSentChatMessage {

	private Button btn_ok;
	private ImageButton iamgeButton;

	private Activity context;
	private String text;
	private boolean isLeft;
	private String head;
	private String filePath;
	private Recorder mVoiceRecod;
	private AnimationDrawable mAnimDrawable;
	private boolean isBurn;
	private String burnId;
	private int mFriendUid, mFriendSex;
	private String mFriendNick;
	private BAHandler mHandler;
	private ChatManageBiz mChatManageBiz;
	private ChatAdapter mChatListAdpater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private int from;

	public ChatVoiceDialog(Activity context, String text, String head, String content, Recorder mVoiceRecod, AnimationDrawable mAnimDrawable,
			String burnId, int mFriendUid, int mFriendSex, String mFriendNick, BAHandler mHandler, ChatManageBiz mChatManageBiz,
			ChatAdapter mChatListAdpater, boolean isLeft, int from) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		this.text = text;
		this.head = head;
		this.isLeft = isLeft;
		this.filePath = content;
		this.mVoiceRecod = mVoiceRecod;
		this.mAnimDrawable = mAnimDrawable;
		this.burnId = burnId;
		this.mFriendUid = mFriendUid;
		this.mFriendSex = mFriendSex;
		this.mFriendNick = mFriendNick;
		this.mHandler = mHandler;
		this.mChatManageBiz = mChatManageBiz;
		this.mChatListAdpater = mChatListAdpater;
		this.from = from;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_voice_play);
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.main_img_defaulthead_un).cacheOnDisk(false)
				.considerExifParams(true).displayer(new RoundedBitmapDisplayer(40)).bitmapConfig(Bitmap.Config.RGB_565).build();

		btn_ok = (Button) findViewById(R.id.btn_grade_ok);

		((TextView) findViewById(R.id.chat_item_left_voice_text)).setText(text + "");
		iamgeButton = (ImageButton) findViewById(R.id.chat_item_left_voice_image);
		findViewById(R.id.chat_item_left_voice).setOnClickListener(this);

		// 添加物理返回键对话框不消失
		OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
					return true;
				} else {
					return false;
				}
			}
		};

		setOnKeyListener(keylistener);
		// 点击对话框外不消失
		setCanceledOnTouchOutside(false);
		btn_ok.setOnClickListener(this);

		ImageView headView = (ImageView) findViewById(R.id.iv_chat_voice_head_type);
		if (from == RewardListActivity.CHAT_FROM_REWARD) {
			if (mFriendSex == Gender.MALE.getValue()) {
				headView.setImageResource(R.drawable.dynamic_defalut_man);
			} else {
				headView.setImageResource(R.drawable.dynamic_defalut_woman);
			}
		} else
			imageLoader.displayImage("http://" + head, headView, options);

		Window w = getWindow();
		WindowManager.LayoutParams wlps = w.getAttributes();
		wlps.width = context.getResources().getDisplayMetrics().widthPixels * 3 / 4;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		wlps.gravity = Gravity.CENTER;
		w.setAttributes(wlps);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_item_left_voice:
			if (isLeft)
				btn_ok.setText(R.string.burn);
			iamgeButton.setOnClickListener(this);
			iamgeButton.performClick();
			break;
		case R.id.chat_item_left_voice_image:
			if (isLeft)
				isBurn = true;
			if (mVoiceRecod.isPlaying()) {
				mVoiceRecod.stopPlayback();
				if (mAnimDrawable != null) {
					mAnimDrawable.stop();
				}
				v.clearAnimation();
				v.setBackgroundResource(R.drawable.message_img_voice3_white);
			} else {
				v.setBackgroundResource(R.drawable.message_img_voice_white);
				mAnimDrawable = (AnimationDrawable) v.getBackground();
				mAnimDrawable.start();
				mVoiceRecod.setShow(false);
				mVoiceRecod.startPlayback(filePath, -1);
			}
			break;
		case R.id.btn_grade_ok:
			dismiss();

			if (mVoiceRecod.isPlaying()) {
				mVoiceRecod.stopPlayback();
			}
			if (mAnimDrawable != null) {
				mAnimDrawable.stop();
			}

			if (!TextUtils.isEmpty(burnId) && !burnId.equals("0") && isBurn) {
				GoGirlUserInfo userInfo = UserUtils.getUserEntity(context);
				if (null != userInfo) {
					if (from == RewardListActivity.CHAT_FROM_REWARD) {
						mChatManageBiz.sentMsg(userInfo.auth, BAApplication.app_version_code, userInfo.uid.intValue(), burnId.getBytes(),
								MessageType.GOGIRL_DATA_TYPE_ANONYM_RECEIPT.getValue(), -1, mFriendUid, burnId, new String(userInfo.nick),
								mFriendNick, userInfo.sex.intValue(), mFriendSex, this, 0, from);
					} else
						mChatManageBiz.sentMsg(userInfo.auth, BAApplication.app_version_code, userInfo.uid.intValue(), burnId.getBytes(),
								MessageType.RECEIPT.getValue(), -1, mFriendUid, burnId, new String(userInfo.nick), mFriendNick,
								userInfo.sex.intValue(), mFriendSex, this, 0, 0);
				}

				mChatManageBiz.changeMessageStatusBySerID(context, mFriendUid, ChatStatus.READED_BURN.getValue(), burnId, false);
				mChatListAdpater.setStatusByBurnId(ChatStatus.READED_BURN.getValue(), burnId);
				mHandler.sendEmptyMessageAtTime(HandlerType.CHAT_REFRUSH, 1000);
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void getSentChatMessageCallBack(int retcode, ChatMessageReceiptEntity recepit) {
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerType.SENT_MESSAGE_CALLBACK;
		msg.arg1 = retcode;
		msg.obj = recepit;
		mHandler.sendMessage(msg);
	}

	public void showDialog() {
		try {
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
