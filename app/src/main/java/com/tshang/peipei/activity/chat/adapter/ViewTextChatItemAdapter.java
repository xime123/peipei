package com.tshang.peipei.activity.chat.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.bean.EmojiFaceConversionUtil;
import com.tshang.peipei.activity.chat.bean.HaremEmotionUtil;
import com.tshang.peipei.activity.skill.SkillDetailActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.emoji.EmojiParser;
import com.tshang.peipei.base.emoji.ParseMsgUtil;
import com.tshang.peipei.model.biz.baseviewoperate.SkillUtils;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: VideoItemView.java 
 *
 * @Description: 纯文本适配
 *
 * @author Jeff  
 *
 * @date 2014年9月1日 下午1:39:45 
 *
 * @version V1.0   
 */
public class ViewTextChatItemAdapter extends ViewBaseChatItemAdapter {

	public ViewTextChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
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

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_text_type, parent, false);
			mViewholer.llLeft = (LinearLayout) convertView.findViewById(R.id.ll_chat_left_text_content);
			mViewholer.llLeftHead = (LinearLayout) convertView.findViewById(R.id.ll_chat_left_head);
			mViewholer.chatLeftHead = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left);
			mViewholer.chatLeftTextView = (TextView) convertView.findViewById(R.id.chat_item_content_text_left);
			mViewholer.chatLeftTime = (TextView) convertView.findViewById(R.id.chat_item_time_left);
			mViewholer.leftNick = (TextView) convertView.findViewById(R.id.chat_item_nick_text_left);

			mViewholer.llRight = (LinearLayout) convertView.findViewById(R.id.ll_chat_right_text);
			mViewholer.llRightHead = (LinearLayout) convertView.findViewById(R.id.ll_chat_right_head);
			mViewholer.chatRightHead = (ImageView) convertView.findViewById(R.id.chat_item_head_right);
			mViewholer.chatRightTextView = (TextView) convertView.findViewById(R.id.chat_item_content_text_right);
			mViewholer.chatRightTime = (TextView) convertView.findViewById(R.id.chat_item_time_right);
			mViewholer.ibtnResend = (ImageButton) convertView.findViewById(R.id.chat_item_right_sendflag_ibtn);
			mViewholer.progressBar = (ProgressBar) convertView.findViewById(R.id.chat_item_right_send_pb);
			mViewholer.rightNick = (TextView) convertView.findViewById(R.id.chat_item_nick_text_right);

			mViewholer.rootLayout = (LinearLayout) convertView.findViewById(R.id.chat_text_root_layout);

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
		String message = chatEntity.getMessage().replace(" #@#", " ");
		if (chatEntity.getFromID() == BAConstants.PEIPEI_XIAOPEI || chatEntity.getFromID() == BAConstants.PEIPEI_CHAT_XIAOPEI
				|| chatEntity.getFromID() == BAConstants.PEIPEI_CHAT_TONGZHI) {
			mViewholer.chatLeftTextView.setAutoLinkMask(Linkify.ALL);
		}
		int sendStatus = chatEntity.getStatus();
		long time = chatEntity.getCreateTime();
		//		System.out.println("文本时间========" + time);
		if (des == BAConstants.ChatDes.TO_ME.getValue()) {
			mViewholer.llLeft.setVisibility(View.VISIBLE);
			mViewholer.llRight.setVisibility(View.GONE);
			mViewholer.llLeftHead.setVisibility(View.VISIBLE);
			mViewholer.llRightHead.setVisibility(View.INVISIBLE);
			if (chatEntity.getType() == 32) {
				final ChatMessageEntity messageEntity = ChatMessageBiz.decodeMessage(message);
				if (messageEntity != null) {
					String nick = messageEntity.getNick();
					if (TextUtils.isEmpty(nick)) {
						nick = "";
					}
					String title = messageEntity.getTitle();
					if (TextUtils.isEmpty(title)) {
						title = "";
					}
					message = String.format(activity.getString(R.string.str_skill_intergerin_success), nick, title);
					SpannableStringBuilder style = ParseMsgUtil.convetToHtml(message, activity, BaseUtils.dip2px(activity, 24));
					mViewholer.chatLeftTextView.setMovementMethod(LinkMovementMethod.getInstance());
					int start = nick.length() + 12;

					style.setSpan(new ClickableSpan() {

						@Override
						public void onClick(View widget) {
							try {
								Bundle bundle = new Bundle();
								bundle.putBoolean(SkillUtils.SKILL_FROM, true);
								bundle.putInt(SkillUtils.SKILL_ID, Integer.parseInt(messageEntity.getId()));
								BaseUtils.openActivity(activity, SkillDetailActivity.class, bundle);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						@Override
						public void updateDrawState(TextPaint ds) {//选中的文字变色
							super.updateDrawState(ds);//369a00
							ds.setColor(activity.getResources().getColor(R.color.bule));
							ds.setUnderlineText(true);

						}
					}, start, start + 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					//					mViewholer.chatLeftTextView.setTextIsSelectable(false);
					mViewholer.chatLeftTextView.setText(style);
				}
			} else {
				//				mViewholer.chatLeftTextView.setTextIsSelectable(true);
				setChatTextShow(message, mViewholer.chatLeftTextView, new String(BAApplication.mLocalUserInfo.nick));
			}
			if (!TextUtils.isEmpty(chatEntity.getRevStr3()) && Integer.parseInt(chatEntity.getRevStr3()) == 1) {
				if (Integer.parseInt(chatEntity.getRevStr1()) == Gender.FEMALE.getValue()) {
					mViewholer.chatLeftHead.setImageResource(R.drawable.dynamic_defalut_woman);
				} else {
					mViewholer.chatLeftHead.setImageResource(R.drawable.dynamic_defalut_man);
				}
				leftHeadListener.setEntity(null);
			} else {
				setHeadImage(mViewholer.chatLeftHead, chatEntity.getFromID());
				leftHeadListener.setEntity(chatEntity);
			}
			setTimeShow(mViewholer.chatLeftTime, time);
		} else {
			mViewholer.llLeft.setVisibility(View.GONE);
			mViewholer.llRight.setVisibility(View.VISIBLE);
			mViewholer.llLeftHead.setVisibility(View.INVISIBLE);
			mViewholer.llRightHead.setVisibility(View.VISIBLE);
			setChatTextShow(message, mViewholer.chatRightTextView, chatEntity.getRevStr2());
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

			setGroupTextRightItemBackground(mViewholer.chatRightTextView, bgIndex, chatEntity.getToUid(), chatEntity.getFromID());
			setGroupTextLeftItemBackground(mViewholer.chatLeftTextView, bgIndex, chatEntity.getToUid(), chatEntity.getFromID());

			mViewholer.leftNick.setText(chatEntity.getRevStr2());
			mViewholer.rightNick.setText(new String(BAApplication.mLocalUserInfo.nick));
			mViewholer.leftNick.setVisibility(View.VISIBLE);
			mViewholer.rightNick.setVisibility(View.VISIBLE);

			setNickListener(mViewholer.leftNick, mViewholer.leftNick.getText().toString());
		}
		return convertView;
	}

	private void setChatTextShow(String message, TextView tv, String nick) {//设置纯文本数据显示
		if (!TextUtils.isEmpty(message)) {
			String unicode = EmojiParser.getInstance(activity).parseEmoji(message);
			SpannableString builder = EmojiFaceConversionUtil.getInstace().getExpressionString(activity, unicode, HaremEmotionUtil.EMOJI_MIDDLE_SIZE);
			if (message.startsWith("@")) {
				try {
					final String len = message.substring(0, message.indexOf(" "));
					builder.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, len.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					//@点击处理
					tv.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View arg0, MotionEvent arg1) {
							if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
								//自己不能@自己
								if (!len.contains("@" + new String(BAApplication.mLocalUserInfo.nick))) {
									nickOnClickListener.onClickNick(len.substring(1, len.length()).trim());
								}
							}
							return false;
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			tv.setText(builder);
		}
	}

	public class AboutNickClick implements OnClickListener {
		private String nick;

		public AboutNickClick(String nick) {
			this.nick = nick.substring(1, nick.length()).trim();
		}

		@Override
		public void onClick(View arg0) {
			nickOnClickListener.onClickNick(nick);
		}
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

		LinearLayout rootLayout;
	}
}
