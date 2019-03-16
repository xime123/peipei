package com.tshang.peipei.activity.chat.adapter;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
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
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.biz.chat.groupchat.PlayDareBiz;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: JoinHaremChatItemAdapter.java 
 *
 * @Description: 大冒险骰子
 *
 * @author allen  
 *
 * @date 2015年06月25日 上午11:39:45 
 *
 * @version V2.1.1   
 */
public class ViewDareChatItemAdapter extends ViewBaseChatItemAdapter {
	private BAHandler handler;

	public ViewDareChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, BAHandler handler, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
		this.handler = handler;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent, ChatDatabaseEntity chatEntity) {
		ViewHolder mViewholer;
		DetailClickListener detailClickListener = null;
		HeadClickListener leftHeadListener = null;
		HeadClickListener rightHeadListener = null;
		int des = chatEntity.getDes();
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_dice, parent, false);
			mViewholer.ivDare = (ImageView) convertView.findViewById(R.id.iv_chat_dice_image);
			mViewholer.llLeft = (LinearLayout) convertView.findViewById(R.id.ll_chat_left_text_content);
			mViewholer.llLeftHead = (LinearLayout) convertView.findViewById(R.id.ll_chat_left_head);
			mViewholer.chatLeftHead = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left);
			mViewholer.chatLeftTextView = (TextView) convertView.findViewById(R.id.chat_item_content_text_left);
			mViewholer.chatLeftTime = (TextView) convertView.findViewById(R.id.chat_item_time_left);
			mViewholer.leftNick = (TextView) convertView.findViewById(R.id.chat_item_nick_dare_left);

			mViewholer.llRight = (LinearLayout) convertView.findViewById(R.id.ll_chat_right_text);
			mViewholer.llRightHead = (LinearLayout) convertView.findViewById(R.id.ll_chat_right_head);
			mViewholer.chatRightHead = (ImageView) convertView.findViewById(R.id.chat_item_head_right);
			mViewholer.chatRightTextView = (TextView) convertView.findViewById(R.id.chat_item_content_text_right);
			mViewholer.chatRightTime = (TextView) convertView.findViewById(R.id.chat_item_time_right);
			mViewholer.ibtnResend = (ImageButton) convertView.findViewById(R.id.chat_item_right_sendflag_ibtn);
			mViewholer.progressBar = (ProgressBar) convertView.findViewById(R.id.chat_item_right_send_pb);
			mViewholer.rightNick = (TextView) convertView.findViewById(R.id.chat_item_nick_dare_right);

			mViewholer.rootLayout = (LinearLayout) convertView.findViewById(R.id.chat_dare_root_layout);

			leftHeadListener = new HeadClickListener(true, activity);
			mViewholer.chatLeftHead.setOnClickListener(leftHeadListener);
			rightHeadListener = new HeadClickListener(false, activity);
			mViewholer.chatRightHead.setOnClickListener(rightHeadListener);

			convertView.setTag(mViewholer);
			convertView.setTag(mViewholer.chatLeftHead.getId(), leftHeadListener);
			convertView.setTag(mViewholer.chatRightHead.getId(), rightHeadListener);

			detailClickListener = new DetailClickListener();
			mViewholer.ivDare.setOnClickListener(detailClickListener);
			convertView.setTag(mViewholer.ivDare.getId(), detailClickListener);

			convertView.setTag(mViewholer);

		} else {
			mViewholer = (ViewHolder) convertView.getTag();
			detailClickListener = (DetailClickListener) convertView.getTag(mViewholer.ivDare.getId());
			leftHeadListener = (HeadClickListener) convertView.getTag(mViewholer.chatLeftHead.getId());
			rightHeadListener = (HeadClickListener) convertView.getTag(mViewholer.chatRightHead.getId());

		}
		if (chatEntity != null) {
			int status = chatEntity.getProgress();
			String gameid = chatEntity.getMesSvrID();
			detailClickListener.setStrActionUrl(gameid);
			//			Log.d("Aaron", "status========" + status);
			//0 可点击 1已点击
			if (status == 1) {
				mViewholer.ivDare.setClickable(false);
				mViewholer.ivDare.setImageResource(R.drawable.message_img_dice_dis);
			} else if (status == 0) {
				if (des == BAConstants.ChatDes.TO_ME.getValue()) {
					mViewholer.ivDare.setClickable(true);
					mViewholer.ivDare.setImageResource(R.drawable.message_img_dice_selector);
				} else {
					mViewholer.ivDare.setClickable(false);
					mViewholer.ivDare.setImageResource(R.drawable.message_img_dice_dis);
				}
			} else {
				mViewholer.ivDare.setClickable(false);
				mViewholer.ivDare.setImageResource(R.drawable.message_img_dice_over);
			}
		}

		long time = chatEntity.getCreateTime();
		//		System.out.println("文本时间========" + time);
		if (des == BAConstants.ChatDes.TO_ME.getValue()) {
			mViewholer.llLeft.setVisibility(View.VISIBLE);
			mViewholer.llRight.setVisibility(View.GONE);
			mViewholer.llLeftHead.setVisibility(View.VISIBLE);
			mViewholer.llRightHead.setVisibility(View.INVISIBLE);

			setHeadImage(mViewholer.chatLeftHead, chatEntity.getFromID());
			leftHeadListener.setEntity(chatEntity);
			setTimeShow(mViewholer.chatLeftTime, time);
		} else {
			mViewholer.llLeft.setVisibility(View.GONE);
			mViewholer.llRight.setVisibility(View.VISIBLE);
			mViewholer.llLeftHead.setVisibility(View.INVISIBLE);
			mViewholer.llRightHead.setVisibility(View.VISIBLE);
			setHeadImage(mViewholer.chatRightHead, selfuid);
			setResendData(mViewholer.ibtnResend, chatEntity, position);
			setTimeShow(mViewholer.chatRightTime, time);
			mViewholer.progressBar.setVisibility(View.GONE);
			mViewholer.ibtnResend.setVisibility(View.GONE);

		}
		if (isGroupChat) {
			String key = "Group_" + String.valueOf(chatEntity.getToUid()) + "#" + String.valueOf(chatEntity.getFromID());
			int bgIndex = SharedPreferencesTools.getInstance(activity).getIntValueByKey(key, 0);

			mViewholer.chatLeftTime.setBackgroundResource(R.drawable.chat_item_time_group_bg);
			mViewholer.chatRightTime.setBackgroundResource(R.drawable.chat_item_time_group_bg);

			mViewholer.chatLeftTextView.setTextColor(activity.getResources().getColor(R.color.white));
			mViewholer.chatRightTextView.setTextColor(activity.getResources().getColor(R.color.white));

			setGroupDareLeftItemBackground(mViewholer.chatLeftTextView, bgIndex,chatEntity.getToUid(), chatEntity.getFromID());
			setGroupDareRightItemBackground(mViewholer.chatRightTextView, bgIndex,chatEntity.getToUid(), chatEntity.getFromID());

			mViewholer.leftNick.setVisibility(View.VISIBLE);
			mViewholer.rightNick.setVisibility(View.VISIBLE);
			mViewholer.rightNick.setText(new String(BAApplication.mLocalUserInfo.nick));
			mViewholer.leftNick.setText(chatEntity.getRevStr2());
			
			setNickListener(mViewholer.leftNick, mViewholer.leftNick.getText().toString());
		}
		return convertView;
	}

	private final class ViewHolder {
		//左边布局元素
		LinearLayout llLeft;
		ImageView chatLeftHead;//头像
		TextView chatLeftTextView;//文本
		TextView chatLeftTime;//聊天时间
		LinearLayout llLeftHead;//左边的头像
		TextView leftNick;

		//右边布局元素
		LinearLayout llRight;
		ImageView chatRightHead;//头像
		TextView chatRightTextView;//文本
		TextView chatRightTime;//聊天时间
		ProgressBar progressBar;//发送消息进度条
		ImageButton ibtnResend;
		LinearLayout llRightHead;//右边的头像
		TextView rightNick;

		ImageView ivDare;

		LinearLayout rootLayout;
	}

	private class DetailClickListener implements OnClickListener {

		private String strActionUrl = "";

		public DetailClickListener() {

		}

		public void setStrActionUrl(String strActionUrl) {
			this.strActionUrl = strActionUrl;
		}

		@Override
		public void onClick(View v) {
			PlayDareBiz playB = new PlayDareBiz();
			playB.PlayDare(friendUid, strActionUrl, handler);
		}
	}

}
