package com.tshang.peipei.activity.chat.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: VideoItemView.java 
 *
 * @Description: 语音模块的选项
 *
 * @author Jeff 
 *
 * @date 2014年9月1日 下午1:39:45 
 *
 * @version V1.0   
 */
public class ViewVoiceChatItemAdapter extends ViewBaseChatItemAdapter {
	public ViewVoiceChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, ChatClickVoiceInterface callBack, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
		this.callBack = callBack;
	}

	private ChatClickVoiceInterface callBack;

	public View getView(int position, View convertView, ViewGroup parent, ChatDatabaseEntity chatEntity) {
		ViewHolder mViewholer;
		HeadClickListener leftHeadListener = null;
		HeadClickListener rightHeadListener = null;
		int des = chatEntity.getDes();
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_voice_type, parent, false);
			mViewholer.llLeft = (LinearLayout) convertView.findViewById(R.id.ll_chat_left_voice);
			mViewholer.chatLeftHead = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left);
			mViewholer.leftImageButton = (ImageButton) convertView.findViewById(R.id.chat_item_voice_left_imagebutton);
			mViewholer.tv_voice_left_length = (TextView) convertView.findViewById(R.id.chat_item_voice_left_text_length);
			mViewholer.chatLeftTime = (TextView) convertView.findViewById(R.id.chat_item_voice_receive_time_left);
			mViewholer.llClickLeftVoice = (LinearLayout) convertView.findViewById(R.id.chat_item_voice_left);
			mViewholer.leftNick = (TextView) convertView.findViewById(R.id.chat_item_nick_voice_left);

			mViewholer.llRight = (LinearLayout) convertView.findViewById(R.id.ll_chat_right_voice);
			mViewholer.chatRightHead = (ImageView) convertView.findViewById(R.id.chat_item_head_right);
			mViewholer.rightImageButton = (ImageButton) convertView.findViewById(R.id.chat_item_voice_right_image);
			mViewholer.chatRightTime = (TextView) convertView.findViewById(R.id.chat_item_send_voice_time_right);
			mViewholer.ibtnResend = (ImageButton) convertView.findViewById(R.id.chat_item_right_sendflag_ibtn);
			mViewholer.tv_voice_right_length = (TextView) convertView.findViewById(R.id.chat_item_voice_right_text_length);
			mViewholer.progressBar = (ProgressBar) convertView.findViewById(R.id.chat_item_right_send_pb);
			mViewholer.llClickRightVoice = (LinearLayout) convertView.findViewById(R.id.chat_item_right_voice);
			mViewholer.rightNick = (TextView) convertView.findViewById(R.id.chat_item_nick_voice_right);

			mViewholer.rootLayout = (RelativeLayout) convertView.findViewById(R.id.chat_voice_root_layout);

			leftHeadListener = new HeadClickListener(true, activity);
			mViewholer.chatLeftHead.setOnClickListener(leftHeadListener);
			rightHeadListener = new HeadClickListener(false, activity);
			mViewholer.chatRightHead.setOnClickListener(rightHeadListener);

			convertView.setTag(mViewholer);
			convertView.setTag(mViewholer.chatLeftHead.getId(), leftHeadListener);
			convertView.setTag(mViewholer.chatRightHead.getId(), rightHeadListener);

		} else {
			mViewholer = (ViewHolder) convertView.getTag();
			leftHeadListener = (HeadClickListener) convertView.getTag(mViewholer.chatLeftHead.getId());
			rightHeadListener = (HeadClickListener) convertView.getTag(mViewholer.chatRightHead.getId());

		}
		int sendStatus = chatEntity.getStatus();
		long time = chatEntity.getCreateTime();
		String message = chatEntity.getMessage();
		if (!TextUtils.isEmpty(message)) {
			ChatMessageEntity messageEntity = ChatMessageBiz.decodeMessage(message);
			if (messageEntity != null) {
				String voicelen = messageEntity.getVoicelength();
				if (TextUtils.isEmpty(voicelen)) {
					voicelen = "0";
				}
				if (des == BAConstants.ChatDes.TO_ME.getValue()) {
					mViewholer.llLeft.setVisibility(View.VISIBLE);
					mViewholer.llRight.setVisibility(View.GONE);
					setTimeShow(mViewholer.chatLeftTime, time);
					if (!TextUtils.isEmpty(chatEntity.getRevStr3()) && Integer.parseInt(chatEntity.getRevStr3()) == 1) {
						if (Integer.parseInt(chatEntity.getRevStr1()) == Gender.MALE.getValue()) {
							mViewholer.chatLeftHead.setImageResource(R.drawable.dynamic_defalut_man);
						} else {
							mViewholer.chatLeftHead.setImageResource(R.drawable.dynamic_defalut_woman);
						}
					} else {
						setHeadImage(mViewholer.chatLeftHead, chatEntity.getFromID());
						leftHeadListener.setEntity(chatEntity);
					}
					mViewholer.tv_voice_left_length.setText(voicelen);
					setDataToVoice(mViewholer.llClickLeftVoice, position, mViewholer.leftImageButton, messageEntity, chatEntity, position, true);
				} else {
					mViewholer.llLeft.setVisibility(View.GONE);
					mViewholer.llRight.setVisibility(View.VISIBLE);
					setTimeShow(mViewholer.chatRightTime, time);
					mViewholer.tv_voice_left_length.setText(messageEntity.getVoicelength());
					setHeadImage(mViewholer.chatRightHead, selfuid);
					mViewholer.tv_voice_right_length.setText(voicelen);
					setResendData(mViewholer.ibtnResend, chatEntity, position);
					//					mViewholer.chatRightHead.setOnClickListener(new RightHeadClickListener());
					setDataToVoice(mViewholer.llClickRightVoice, position, mViewholer.rightImageButton, messageEntity, chatEntity, position, false);
					if (ChatStatus.SENDING.getValue() == sendStatus) {
						mViewholer.progressBar.setVisibility(View.VISIBLE);
						mViewholer.ibtnResend.setVisibility(View.GONE);
					} else if (ChatStatus.FAILED.getValue() == sendStatus) {
						mViewholer.progressBar.setVisibility(View.GONE);
						mViewholer.ibtnResend.setVisibility(View.VISIBLE);
					} else {
						mViewholer.progressBar.setVisibility(View.GONE);
						mViewholer.ibtnResend.setVisibility(View.GONE);
					}
				}
			}
		}
		if (isGroupChat) {
			String key = "Group_" + String.valueOf(chatEntity.getToUid()) + "#" + String.valueOf(chatEntity.getFromID());
			int bgIndex = SharedPreferencesTools.getInstance(activity).getIntValueByKey(key, 0);

			mViewholer.chatLeftTime.setBackgroundResource(R.drawable.chat_item_time_group_bg);
			mViewholer.chatRightTime.setBackgroundResource(R.drawable.chat_item_time_group_bg);

			setGroupVoiceLeftItemBackground(mViewholer.llClickLeftVoice, bgIndex,chatEntity.getToUid(), chatEntity.getFromID());
			setGroupVoiceRightItemBackground(mViewholer.llClickRightVoice, bgIndex,chatEntity.getToUid(), chatEntity.getFromID());

			mViewholer.rightNick.setVisibility(View.VISIBLE);
			mViewholer.leftNick.setVisibility(View.VISIBLE);
			mViewholer.rightNick.setText(new String(BAApplication.mLocalUserInfo.nick));
			mViewholer.leftNick.setText(chatEntity.getRevStr2());

			setNickListener(mViewholer.leftNick, mViewholer.leftNick.getText().toString());

			mViewholer.leftImageButton.setBackgroundResource(R.drawable.message_img_voice3_white);
			mViewholer.tv_voice_left_length.setTextColor(activity.getResources().getColor(R.color.white));
		}
		return convertView;
	}

	private final class ViewHolder {
		//左边布局元素
		LinearLayout llLeft;
		ImageView chatLeftHead;//头像
		ImageButton leftImageButton;//左边语音闪烁图标
		TextView tv_voice_left_length;//语音时长
		TextView chatLeftTime;//聊天时间
		LinearLayout llClickLeftVoice;//左边播放语音
		TextView leftNick;

		//右边布局元素
		LinearLayout llRight;
		ImageView chatRightHead;//头像
		ImageButton rightImageButton;//右边语音闪烁图标
		TextView chatRightTime;//聊天时间
		ProgressBar progressBar;//发送消息进度条
		ImageButton ibtnResend;
		TextView tv_voice_right_length;//语音时长
		LinearLayout llClickRightVoice;//右边播放语音
		TextView rightNick;

		RelativeLayout rootLayout;

	}

	//设置语音内容
	private void setDataToVoice(LinearLayout llLayout, int pos, ImageButton btn, ChatMessageEntity messageEntity,
			final ChatDatabaseEntity chatEntity, final int position, boolean isLeft) {
		if (Integer.parseInt(messageEntity.getVoicelength()) < 10) {
			llLayout.setPadding(BaseUtils.dip2px(activity, 15), 5, BaseUtils.dip2px(activity, 15), 5);
		} else if (Integer.parseInt(messageEntity.getVoicelength()) < 30) {
			llLayout.setPadding(BaseUtils.dip2px(activity, 40), 5, BaseUtils.dip2px(activity, 40), 5);
		} else {
			llLayout.setPadding(BaseUtils.dip2px(activity, 60), 5, BaseUtils.dip2px(activity, 60), 5);
		}
		llLayout.setOnClickListener(new ClickVoiceListener(chatEntity, pos, btn, isLeft));
	}

	public class ClickVoiceListener implements OnClickListener {//语音点击播放
		private ChatDatabaseEntity chatEntity;
		private int pos;
		private ImageButton view;
		private boolean isLeft;

		public ClickVoiceListener(ChatDatabaseEntity chatEntity, int pos, ImageButton view, boolean isLeft) {
			this.chatEntity = chatEntity;
			this.pos = pos;
			this.view = view;
			this.isLeft = isLeft;
		}

		@Override
		public void onClick(View v) {
			if (callBack != null && chatEntity != null) {
				callBack.onClickVoice(chatEntity, pos, view, isLeft);
			}
		}
	}

	public interface ChatClickVoiceInterface {
		public void onClickVoice(ChatDatabaseEntity chatEntity, int pos, ImageButton view, boolean isLeft);
	}

}
