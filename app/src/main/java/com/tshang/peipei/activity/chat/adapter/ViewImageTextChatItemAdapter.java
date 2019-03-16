package com.tshang.peipei.activity.chat.adapter;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: VideoItemView.java 
 *
 * @Description: 大冒险骰子点数
 *
 * @author allen  
 *
 * @date 2015年6月258日 下午17:39:45 
 *
 * @version V1.0   
 */
public class ViewImageTextChatItemAdapter extends ViewBaseChatItemAdapter {

	private BAHandler mHandler;

	public ViewImageTextChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, BAHandler mHandler, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
		this.mHandler = mHandler;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent, ChatDatabaseEntity chatEntity) {
		ViewHolder mViewholer;
		HeadClickListener leftHeadListener = null;
		HeadClickListener rightHeadListener = null;
		int des = chatEntity.getDes();
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_text_image_type, parent, false);
			mViewholer.llLeft = (LinearLayout) convertView.findViewById(R.id.ll_chat_left_text_image_content);
			mViewholer.llLeftHead = (LinearLayout) convertView.findViewById(R.id.ll_chat_left_head);
			mViewholer.chatLeftHead = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left);
			mViewholer.chatLeftDare = (ImageView) convertView.findViewById(R.id.chat_item_dare_num_left);
			mViewholer.chatLeftTime = (TextView) convertView.findViewById(R.id.chat_item_time_image_left);
			mViewholer.leftNick = (TextView) convertView.findViewById(R.id.chat_item_nick_dare_result_left);
			mViewholer.leftBg = (LinearLayout) convertView.findViewById(R.id.chat_item_content_text_image_left);

			mViewholer.llRight = (LinearLayout) convertView.findViewById(R.id.ll_chat_right_text_image);
			mViewholer.llRightHead = (LinearLayout) convertView.findViewById(R.id.ll_chat_right_head);
			mViewholer.chatRightHead = (ImageView) convertView.findViewById(R.id.chat_item_head_right);
			mViewholer.chatRightDare = (ImageView) convertView.findViewById(R.id.chat_item_dare_num_right);
			mViewholer.chatRightTime = (TextView) convertView.findViewById(R.id.chat_item_time_right);
			mViewholer.ibtnResend = (ImageButton) convertView.findViewById(R.id.chat_item_right_sendflag_ibtn);
			mViewholer.progressBar = (ProgressBar) convertView.findViewById(R.id.chat_item_right_send_pb);
			mViewholer.rightNick = (TextView) convertView.findViewById(R.id.chat_item_nick_dare_result_right);
			mViewholer.rightBg = (LinearLayout) convertView.findViewById(R.id.chat_item_content_text_right);

			mViewholer.rootLayout = (LinearLayout) convertView.findViewById(R.id.chat_iamge_text_root_layout);

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
		String message = chatEntity.getMessage();

		int sendStatus = chatEntity.getStatus();
		long time = chatEntity.getCreateTime();
		//		System.out.println("文本时间========" + time);
		if (des == BAConstants.ChatDes.TO_ME.getValue()) {
			mViewholer.llLeft.setVisibility(View.VISIBLE);
			mViewholer.llRight.setVisibility(View.GONE);
			mViewholer.llLeftHead.setVisibility(View.VISIBLE);
			mViewholer.llRightHead.setVisibility(View.INVISIBLE);

			//			if (System.currentTimeMillis() - time < 2000) {
			//				setDireImage(mViewholer.chatLeftDare, Integer.parseInt(message));
			//			} else {
			setDireImage1(mViewholer.chatLeftDare, Integer.parseInt(message));
			//			}
			setHeadImage(mViewholer.chatLeftHead, chatEntity.getFromID());
			leftHeadListener.setEntity(chatEntity);
			setTimeShow(mViewholer.chatLeftTime, time);
		} else {
			mViewholer.llLeft.setVisibility(View.GONE);
			mViewholer.llRight.setVisibility(View.VISIBLE);
			mViewholer.llLeftHead.setVisibility(View.INVISIBLE);
			mViewholer.llRightHead.setVisibility(View.VISIBLE);

			long tt = System.currentTimeMillis();
			//			if (tt - time < 2000) {
			//				setDireImage(mViewholer.chatRightDare, Integer.parseInt(message));
			//			} else {
			setDireImage1(mViewholer.chatRightDare, Integer.parseInt(message));
			//			}
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

		}
		if (isGroupChat) {
			String key = "Group_" + String.valueOf(chatEntity.getToUid()) + "#" + String.valueOf(chatEntity.getFromID());
			int bgIndex = SharedPreferencesTools.getInstance(activity).getIntValueByKey(key, 0);
			
			mViewholer.chatLeftTime.setBackgroundResource(R.drawable.chat_item_time_group_bg);
			mViewholer.chatRightTime.setBackgroundResource(R.drawable.chat_item_time_group_bg);

			setGroupLeftItemBackGround(mViewholer.leftBg, 15, 10, 10, 10, bgIndex, chatEntity.getToUid(), chatEntity.getFromID());
			setGroupRightItemBackGround(mViewholer.rightBg, 10, 10, 15, 10, bgIndex, chatEntity.getToUid(), chatEntity.getFromID());

			mViewholer.rightNick.setVisibility(View.VISIBLE);
			mViewholer.leftNick.setVisibility(View.VISIBLE);
			mViewholer.rightNick.setText(new String(BAApplication.mLocalUserInfo.nick));
			mViewholer.leftNick.setText(chatEntity.getRevStr2());

			setNickListener(mViewholer.leftNick, mViewholer.leftNick.getText().toString());

		}
		return convertView;
	}

	private void setDireImage1(ImageView iv, int dire) {
		switch (dire) {
		case 1:
			iv.setImageResource(R.drawable.message_img_dice1);
			break;
		case 2:
			iv.setImageResource(R.drawable.message_img_dice2);
			break;
		case 3:
			iv.setImageResource(R.drawable.message_img_dice3);
			break;
		case 4:
			iv.setImageResource(R.drawable.message_img_dice4);
			break;
		case 5:
			iv.setImageResource(R.drawable.message_img_dice5);
			break;
		case 6:
			iv.setImageResource(R.drawable.message_img_dice6);
			break;
		default:
			break;
		}
	}

	private void setDireImage(ImageView iv, int dire) {
		iv.setBackgroundResource(R.anim.message_img_dice_anim);
		AnimationDrawable rocketAnimation = (AnimationDrawable) iv.getBackground();
		rocketAnimation.start();

		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.CHAT_DARE_INFO_ANIM;
		msg.arg1 = dire;
		msg.obj = iv;
		mHandler.sendMessageDelayed(msg, 2000);
	}

	private final class ViewHolder {
		//左边布局元素
		LinearLayout llLeft;
		ImageView chatLeftHead;//头像
		ImageView chatLeftDare;//文本
		TextView chatLeftTime;//聊天时间
		LinearLayout llLeftHead;//左边的头像
		LinearLayout leftBg;
		TextView leftNick;

		//右边布局元素
		LinearLayout llRight;
		ImageView chatRightHead;//头像
		ImageView chatRightDare;//文本
		TextView chatRightTime;//聊天时间
		ProgressBar progressBar;//发送消息进度条
		ImageButton ibtnResend;
		LinearLayout llRightHead;//右边的头像
		LinearLayout rightBg;
		TextView rightNick;

		LinearLayout rootLayout;

	}

}
