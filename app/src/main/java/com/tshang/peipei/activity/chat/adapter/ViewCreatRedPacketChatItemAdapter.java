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
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.listener.RedPacketDetailClick;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.emoji.ParseMsgUtil;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.model.redpacket.RedPacketCreate;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: ViewCreatRedPacketChatItemAdapter.java 
 *
 * @Description: 发送红包界面
 *
 * @author Jeff  
 *
 * @date 2014年10月17日 下午1:39:45 
 *
 * @version V1.4.0   
 */
public class ViewCreatRedPacketChatItemAdapter extends ViewBaseChatItemAdapter {

	public ViewCreatRedPacketChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, BAHandler handler, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
		this.handler = handler;
	}

	private BAHandler handler;

	@Override
	public View getView(int position, View convertView, ViewGroup parent, ChatDatabaseEntity chatEntity) {
		ViewHolder mViewholer;
		HeadClickListener leftHeadListener = null;
		HeadClickListener rightHeadListener = null;
		RedPacketleftListener leftListener = null;
		RedPacketDetailClick redPacketDetailListener = null;
		int des = chatEntity.getDes();
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_redpacket_type, parent, false);
			mViewholer.llLeft = (LinearLayout) convertView.findViewById(R.id.ll_chat_left_text_content);
			mViewholer.llLeftHead = (LinearLayout) convertView.findViewById(R.id.ll_chat_left_head);
			mViewholer.chatLeftHead = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left);
			mViewholer.chatLeftTextView = (TextView) convertView.findViewById(R.id.tv_redpacket_content_left);
			mViewholer.chatLeftTime = (TextView) convertView.findViewById(R.id.chat_item_time_left);
			mViewholer.leftBack = (LinearLayout) convertView.findViewById(R.id.chat_item_redpack_left);
			mViewholer.leftNick = (TextView) convertView.findViewById(R.id.chat_item_nick_redpack_left);
			mViewholer.leftTitle = (TextView) convertView.findViewById(R.id.chat_item_redpack_left_title);

			mViewholer.llRight = (LinearLayout) convertView.findViewById(R.id.ll_chat_right_text);
			mViewholer.llRightHead = (LinearLayout) convertView.findViewById(R.id.ll_chat_right_head);
			mViewholer.chatRightHead = (ImageView) convertView.findViewById(R.id.chat_item_head_right);
			mViewholer.chatRightTextView = (TextView) convertView.findViewById(R.id.tv_redpacket_content_right);
			mViewholer.chatRightTime = (TextView) convertView.findViewById(R.id.chat_item_time_right);
			mViewholer.ibtnResend = (ImageButton) convertView.findViewById(R.id.chat_item_right_sendflag_ibtn);
			mViewholer.progressBar = (ProgressBar) convertView.findViewById(R.id.chat_item_right_send_pb);
			mViewholer.rightBack = (LinearLayout) convertView.findViewById(R.id.chat_item_redpack_right);
			mViewholer.rightNick = (TextView) convertView.findViewById(R.id.chat_item_nick_redpack_right);
			mViewholer.rightTitle = (TextView) convertView.findViewById(R.id.chat_item_redpack_right_title);
			mViewholer.rightDetail = (TextView) convertView.findViewById(R.id.tv_redpacket_content_right_detail);

			mViewholer.rootLayout = (LinearLayout) convertView.findViewById(R.id.chat_item_redpack_layout);

			leftHeadListener = new HeadClickListener(true, activity);
			mViewholer.chatLeftHead.setOnClickListener(leftHeadListener);
			rightHeadListener = new HeadClickListener(false, activity);
			mViewholer.chatRightHead.setOnClickListener(rightHeadListener);
			leftListener = new RedPacketleftListener(activity, handler);
			mViewholer.llLeft.setOnClickListener(leftListener);
			redPacketDetailListener = new RedPacketDetailClick(activity);
			mViewholer.llRight.setOnClickListener(redPacketDetailListener);

			convertView.setTag(mViewholer);
			convertView.setTag(mViewholer.chatLeftHead.getId(), leftHeadListener);
			convertView.setTag(mViewholer.chatRightHead.getId(), rightHeadListener);
			convertView.setTag(mViewholer.llLeft.getId(), leftListener);
			convertView.setTag(mViewholer.llRight.getId(), redPacketDetailListener);

		} else {
			mViewholer = (ViewHolder) convertView.getTag();
			leftHeadListener = (HeadClickListener) convertView.getTag(mViewholer.chatLeftHead.getId());
			rightHeadListener = (HeadClickListener) convertView.getTag(mViewholer.chatRightHead.getId());
			leftListener = (RedPacketleftListener) convertView.getTag(mViewholer.llLeft.getId());
			redPacketDetailListener = (RedPacketDetailClick) convertView.getTag(mViewholer.llRight.getId());

		}
		String message = chatEntity.getMessage();
		String strMessage = activity.getString(R.string.str_harm_redpacket_content);
		if (!TextUtils.isEmpty(message)) {

			long time = chatEntity.getCreateTime();
			ChatMessageEntity chatMessageEntity = ChatMessageBiz.decodeMessage(message);
			if (chatMessageEntity != null) {
				if (des == BAConstants.ChatDes.TO_ME.getValue()) {
					mViewholer.llLeft.setVisibility(View.VISIBLE);
					mViewholer.llRight.setVisibility(View.GONE);
					mViewholer.llLeftHead.setVisibility(View.VISIBLE);
					mViewholer.llRightHead.setVisibility(View.INVISIBLE);
					setChatTextShow(strMessage.replace("$", chatMessageEntity.getDesc()), mViewholer.chatLeftTextView);
					setHeadImage(mViewholer.chatLeftHead, chatEntity.getFromID());
					leftHeadListener.setEntity(chatEntity);
					setTimeShow(mViewholer.chatLeftTime, time);

					try {
						leftListener.setDate(Integer.parseInt(chatMessageEntity.getId()), Integer.parseInt(chatMessageEntity.getCreateuid()));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				} else {
					mViewholer.llLeft.setVisibility(View.GONE);
					mViewholer.llRight.setVisibility(View.VISIBLE);
					try {
						redPacketDetailListener.setData(Integer.parseInt(chatMessageEntity.getId()),
								Integer.parseInt(chatMessageEntity.getCreateuid()));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
					mViewholer.llLeftHead.setVisibility(View.INVISIBLE);
					mViewholer.llRightHead.setVisibility(View.VISIBLE);

					setChatTextShow(strMessage.replace("$", chatMessageEntity.getDesc()), mViewholer.chatRightTextView);
					setHeadImage(mViewholer.chatRightHead, selfuid);
					//					mViewholer.chatRightHead.setOnClickListener(new RightHeadClickListener());
					setTimeShow(mViewholer.chatRightTime, time);
					mViewholer.progressBar.setVisibility(View.GONE);
					mViewholer.ibtnResend.setVisibility(View.GONE);

				}
			}
		}
		if (isGroupChat) {
			String key = "Group_" + String.valueOf(chatEntity.getToUid()) + "#" + String.valueOf(chatEntity.getFromID());
			int bgIndex = SharedPreferencesTools.getInstance(activity).getIntValueByKey(key, 0);

			mViewholer.chatLeftTime.setBackgroundResource(R.drawable.chat_item_time_group_bg);
			mViewholer.chatRightTime.setBackgroundResource(R.drawable.chat_item_time_group_bg);

			mViewholer.leftTitle.setTextColor(activity.getResources().getColor(R.color.white));
			mViewholer.chatLeftTextView.setTextColor(activity.getResources().getColor(R.color.white));

			mViewholer.rightTitle.setTextColor(activity.getResources().getColor(R.color.white));
			mViewholer.chatRightTextView.setTextColor(activity.getResources().getColor(R.color.white));
			mViewholer.rightDetail.setTextColor(activity.getResources().getColor(R.color.white));

			setGroupRedpackLeftItemBackground(mViewholer.leftBack, bgIndex, chatEntity.getToUid(), chatEntity.getFromID());
			setGroupRedpackRightItemBackground(mViewholer.rightBack, bgIndex, chatEntity.getToUid(), chatEntity.getFromID());

			mViewholer.rightNick.setVisibility(View.VISIBLE);
			mViewholer.leftNick.setVisibility(View.VISIBLE);
			mViewholer.rightNick.setText(new String(BAApplication.mLocalUserInfo.nick));
			mViewholer.leftNick.setText(chatEntity.getRevStr2());

			setNickListener(mViewholer.leftNick, mViewholer.leftNick.getText().toString());
		}
		return convertView;
	}

	private void setChatTextShow(String message, TextView tv) {//设置纯文本数据显示
		if (!TextUtils.isEmpty(message)) {
			tv.setText(ParseMsgUtil.convetToHtml(message, activity, BaseUtils.dip2px(activity, 24)));
		}

	}

	private final class ViewHolder {
		//左边布局元素
		LinearLayout llLeft;
		ImageView chatLeftHead;//头像
		TextView chatLeftTextView;//文本
		TextView chatLeftTime;//聊天时间
		LinearLayout llLeftHead;//左边的头像
		LinearLayout leftBack;
		TextView leftNick;
		TextView leftTitle;

		//右边布局元素
		LinearLayout llRight;
		ImageView chatRightHead;//头像
		TextView chatRightTextView;//文本
		TextView chatRightTime;//聊天时间
		ProgressBar progressBar;//发送消息进度条
		ImageButton ibtnResend;
		LinearLayout llRightHead;//右边的头像
		LinearLayout rightBack;
		TextView rightNick;
		TextView rightTitle;
		TextView rightDetail;

		LinearLayout rootLayout;

	}

	private class RedPacketleftListener implements OnClickListener {//左边拆红包监听
		private Activity activity;
		private BAHandler handler;
		private int redpacketid;
		private int createuid;

		public void setDate(int redpacketid, int createuid) {
			this.redpacketid = redpacketid;
			this.createuid = createuid;
		}

		public RedPacketleftListener(Activity activity, BAHandler handler) {
			this.activity = activity;
			this.handler = handler;

		}

		@Override
		public void onClick(View v) {
			RedPacketCreate.getInstance().reqUnpackRedPacket(activity, redpacketid, createuid, handler);
		}

	}

}
