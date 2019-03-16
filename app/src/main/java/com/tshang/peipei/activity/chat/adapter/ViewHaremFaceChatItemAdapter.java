package com.tshang.peipei.activity.chat.adapter;

import java.util.Random;

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
import com.tshang.peipei.activity.chat.bean.HaremFaceConversionUtil;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: ViewImageChatItemAdapter.java 
 *
 * @Description: 图片模块的选项
 *
 * @author Jeff 
 *
 * @date 2014年9月1日 下午1:39:45 
 *
 * @version V1.0   
 */
public class ViewHaremFaceChatItemAdapter extends ViewBaseChatItemAdapter {
	private OnClickListenrHaremVoice listener;

	public ViewHaremFaceChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, OnClickListenrHaremVoice listener, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
		this.listener = listener;
	}

	public View getView(final int position, View convertView, ViewGroup parent, ChatDatabaseEntity chatEntity) {
		ViewHolder mViewholer;
		HeadClickListener leftHeadListener = null;
		HeadClickListener rightHeadListener = null;
		HaremFaceClickListener leftHaremListener = null;
		HaremFaceClickListener rightHaremListener = null;

		int des = chatEntity.getDes();
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_haremface_type, parent, false);
			mViewholer.llLeft = (LinearLayout) convertView.findViewById(R.id.ll_chat_left_image);
			mViewholer.chatLeftHead = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left);
			mViewholer.leftImage = (ImageView) convertView.findViewById(R.id.chat_item_left_image_pic);
			mViewholer.chatLeftTime = (TextView) convertView.findViewById(R.id.chat_item_image_receive_time);
			mViewholer.leftBack = (LinearLayout) convertView.findViewById(R.id.chat_item_haremface_left);
			mViewholer.leftNick = (TextView) convertView.findViewById(R.id.chat_item_nick_haremface_left);

			mViewholer.llRight = (LinearLayout) convertView.findViewById(R.id.ll_chat_right_image);
			mViewholer.chatRightHead = (ImageView) convertView.findViewById(R.id.chat_item_head_right);
			mViewholer.rightImage = (ImageView) convertView.findViewById(R.id.chat_item_image_right_pic);
			mViewholer.chatRightTime = (TextView) convertView.findViewById(R.id.chat_item_send_image_time_right);
			mViewholer.ibtnResend = (ImageButton) convertView.findViewById(R.id.chat_item_right_sendflag_ibtn);
			mViewholer.progressBar = (ProgressBar) convertView.findViewById(R.id.chat_item_right_send_pb);
			mViewholer.rightBack = (LinearLayout) convertView.findViewById(R.id.chat_item_haremface_right);
			mViewholer.rightNick = (TextView) convertView.findViewById(R.id.chat_item_nick_haremface_right);

			mViewholer.rootLayout = (RelativeLayout) convertView.findViewById(R.id.chat_haremface_root_layout);

			leftHeadListener = new HeadClickListener(true, activity);
			mViewholer.chatLeftHead.setOnClickListener(leftHeadListener);
			rightHeadListener = new HeadClickListener(false, activity);
			mViewholer.chatRightHead.setOnClickListener(rightHeadListener);
			leftHaremListener = new HaremFaceClickListener();
			mViewholer.leftImage.setOnClickListener(leftHaremListener);
			rightHaremListener = new HaremFaceClickListener();
			mViewholer.rightImage.setOnClickListener(rightHaremListener);

			convertView.setTag(mViewholer);
			convertView.setTag(mViewholer.chatLeftHead.getId(), leftHeadListener);
			convertView.setTag(mViewholer.chatRightHead.getId(), rightHeadListener);
			convertView.setTag(mViewholer.leftImage.getId(), leftHaremListener);
			convertView.setTag(mViewholer.rightImage.getId(), rightHaremListener);

		} else {
			mViewholer = (ViewHolder) convertView.getTag();
			leftHeadListener = (HeadClickListener) convertView.getTag(mViewholer.chatLeftHead.getId());
			rightHeadListener = (HeadClickListener) convertView.getTag(mViewholer.chatRightHead.getId());
			leftHaremListener = (HaremFaceClickListener) convertView.getTag(mViewholer.leftImage.getId());
			rightHaremListener = (HaremFaceClickListener) convertView.getTag(mViewholer.rightImage.getId());
		}
		int sendStatus = chatEntity.getStatus();
		long time = chatEntity.getCreateTime();
		if (des == BAConstants.ChatDes.TO_ME.getValue()) {
			mViewholer.llLeft.setVisibility(View.VISIBLE);
			mViewholer.llRight.setVisibility(View.GONE);
			//判断是否匿名
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
			setDataToImage(mViewholer.leftImage, chatEntity, leftHaremListener);
		} else {
			mViewholer.llLeft.setVisibility(View.GONE);
			mViewholer.llRight.setVisibility(View.VISIBLE);
			setTimeShow(mViewholer.chatRightTime, time);
			setHeadImage(mViewholer.chatRightHead, selfuid);
			setResendData(mViewholer.ibtnResend, chatEntity, position);
			//			mViewholer.chatRightHead.setOnClickListener(new RightHeadClickListener());
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
			setDataToImage(mViewholer.rightImage, chatEntity, rightHaremListener);

		}
		if (isGroupChat) {
			String key = "Group_" + String.valueOf(chatEntity.getToUid()) + "#" + String.valueOf(chatEntity.getFromID());
			int bgIndex = SharedPreferencesTools.getInstance(activity).getIntValueByKey(key, 0);

			mViewholer.chatLeftTime.setBackgroundResource(R.drawable.chat_item_time_group_bg);
			mViewholer.chatRightTime.setBackgroundResource(R.drawable.chat_item_time_group_bg);

			setGroupVoiceLeftItemBackground(mViewholer.leftBack, bgIndex,chatEntity.getToUid(), chatEntity.getFromID());
			setGroupVoiceRightItemBackground(mViewholer.rightBack, bgIndex,chatEntity.getToUid(), chatEntity.getFromID());

			mViewholer.rightNick.setVisibility(View.VISIBLE);
			mViewholer.leftNick.setVisibility(View.VISIBLE);
			mViewholer.rightNick.setText(new String(BAApplication.mLocalUserInfo.nick));
			mViewholer.leftNick.setText(chatEntity.getRevStr2());
			
			setNickListener(mViewholer.leftNick, mViewholer.leftNick.getText().toString());

		}
		return convertView;
	}

	//设置图片内容
	private void setDataToImage(ImageView iv, ChatDatabaseEntity chatEntity, HaremFaceClickListener haremListener) {
		String path = chatEntity.getMessage();
		int resId = HaremFaceConversionUtil.getInstace().dealExpression(activity, path);
		imageLoader.displayImage("drawable://" + resId, iv, options_haremFaceimage);
		haremListener.setPath(path);
		//		iv.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View v) {
		//				if (listener != null) {
		//					listener.OnClickHaremVoice(path);
		//				}
		//			}
		//		});
	}

	private class HaremFaceClickListener implements OnClickListener {//红宫表情点击
		public String path;

		public void setPath(String path) {
			this.path = path;
		}

		@Override
		public void onClick(View v) {
			if (listener != null) {
				listener.OnClickHaremVoice(path);
			}
		}

	}

	public interface OnClickListenrHaremVoice {
		public void OnClickHaremVoice(String path);
	}

	private final class ViewHolder {
		//左边布局元素
		LinearLayout llLeft;
		ImageView chatLeftHead;//头像
		ImageView leftImage;//左边图片
		TextView chatLeftTime;//聊天时间
		LinearLayout leftBack;
		TextView leftNick;

		//右边布局元素
		LinearLayout llRight;
		ImageView chatRightHead;//头像
		ImageView rightImage;//右边图片
		TextView chatRightTime;//聊天时间
		ProgressBar progressBar;//发送消息进度条
		ImageButton ibtnResend;
		LinearLayout rightBack;
		TextView rightNick;

		RelativeLayout rootLayout;

	}
}
