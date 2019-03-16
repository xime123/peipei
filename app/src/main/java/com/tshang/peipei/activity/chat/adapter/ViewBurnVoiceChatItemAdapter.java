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
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.activity.chat.adapter.ViewBurnImageChatItemAdapter.ChatBurnImageInterface;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: VideoItemView.java 
 *
 * @Description:阅后即焚语音的选项
 *
 * @author Jeff 
 *
 * @date 2014年9月1日 下午1:39:45 
 *
 * @version V1.0   
 */
public class ViewBurnVoiceChatItemAdapter extends ViewBaseChatItemAdapter {
	public ViewBurnVoiceChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, ChatBurnImageInterface callBack, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
		this.callBack = callBack;
	}

	private ChatBurnImageInterface callBack;

	public View getView(final int position, View convertView, ViewGroup parent, final ChatDatabaseEntity chatEntity) {
		ViewHolder mViewholer;
		HeadClickListener leftHeadListener = null;
		HeadClickListener rightHeadListener = null;
		int des = chatEntity.getDes();
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_burn_voice_type, parent, false);
			mViewholer.llLeft = (LinearLayout) convertView.findViewById(R.id.ll_chat_left_burn_voice);
			mViewholer.chatLeftHead = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left);
			mViewholer.chatVoiceBurnLeftImage = (ImageView) convertView.findViewById(R.id.chat_item_burn_voice_left_iv);
			mViewholer.chatLeftTime = (TextView) convertView.findViewById(R.id.chat_item_burn_voice_time_left_tv);

			mViewholer.llRight = (LinearLayout) convertView.findViewById(R.id.ll_chat_right_burn_voice);
			mViewholer.chatRightHead = (ImageView) convertView.findViewById(R.id.chat_item_head_right);
			mViewholer.chatRightTime = (TextView) convertView.findViewById(R.id.chat_item_time_burn_voice_right_vt);
			mViewholer.ibtnResend = (ImageButton) convertView.findViewById(R.id.chat_item_right_sendflag_ibtn);
			mViewholer.progressBar = (ProgressBar) convertView.findViewById(R.id.chat_item_right_send_pb);
			mViewholer.tvSendStatud = (TextView) convertView.findViewById(R.id.chat_item_right_sentflag_text);
			mViewholer.chatVoiceBurnRightImage = (ImageView) convertView.findViewById(R.id.chat_item_burn_voice_right_iv);
			mViewholer.rightStatusText = (TextView) convertView.findViewById(R.id.chat_item_right_sentflag_text);

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
		if (des == BAConstants.ChatDes.TO_ME.getValue()) {
			mViewholer.llLeft.setVisibility(View.VISIBLE);
			mViewholer.llRight.setVisibility(View.GONE);
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
			setTimeShow(mViewholer.chatLeftTime, time);
			mViewholer.chatVoiceBurnLeftImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					callBack.onClickItem(chatEntity, position, v, true);
				}
			});

			if (chatEntity.getStatus() == ChatStatus.READED_BURN.getValue()) {
				callBack.toDismiss(position + 1, convertView, false);
			}
		} else {
			mViewholer.llLeft.setVisibility(View.GONE);
			mViewholer.llRight.setVisibility(View.VISIBLE);
			setHeadImage(mViewholer.chatRightHead, selfuid);
			setResendData(mViewholer.ibtnResend, chatEntity, position);
			setTimeShow(mViewholer.chatRightTime, time);
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
			mViewholer.chatVoiceBurnRightImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					callBack.onClickItem(chatEntity, position, v, false);
				}
			});

			if (chatEntity.getStatus() == ChatStatus.READED_BURN.getValue()) {
				callBack.toDismiss(position + 1, convertView, true);
			}
		}
		return convertView;
	}

	private final class ViewHolder {
		//左边布局元素
		LinearLayout llLeft;
		ImageView chatLeftHead;//头像
		ImageView chatVoiceBurnLeftImage;//左边图片
		TextView chatLeftTime;//聊天时间

		//右边布局元素
		LinearLayout llRight;
		ImageView chatRightHead;//头像
		TextView chatRightTime;//聊天时间
		ProgressBar progressBar;//发送消息进度条
		ImageButton ibtnResend;
		TextView tvSendStatud;//发送状态
		ImageView chatVoiceBurnRightImage;
		TextView rightStatusText;

	}
}
