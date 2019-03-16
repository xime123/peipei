package com.tshang.peipei.activity.chat.adapter;

import android.app.Activity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.activity.chat.bean.EmojiFaceConversionUtil;
import com.tshang.peipei.activity.chat.bean.HaremEmotionUtil;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.emoji.EmojiParser;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: ViewGoddessSkillChatItemAdapter.java 
 *
 * @Description: 女神技适配器
 *
 * @author DYH  
 *
 * @date 2015-11-5 上午10:16:41 
 *
 * @version V1.0   
 */
public class ViewGoddessSkillChatItemAdapter extends ViewBaseChatItemAdapter {

	private DisplayImageOptions option;
	private SkillInviteResCallBack callBack;

	public ViewGoddessSkillChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, SkillInviteResCallBack callBack, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
		option = ImageOptionsUtils.getRewardGiftOptions(activity);
		this.callBack = callBack;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent, ChatDatabaseEntity chatEntity) {
		ViewHolder viewHolder = null;
		HeadClickListener leftHeadClickListener = null;
		HeadClickListener rightHeadClickListener = null;
		int des = chatEntity.getDes();
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(activity, R.layout.item_chat_listview_goddess_skill, null);

			viewHolder.leftHeadIv = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left);
			viewHolder.rightHeadIv = (ImageView) convertView.findViewById(R.id.chat_item_head_right);
			viewHolder.leftGiftHeadIv = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left2);
			viewHolder.rightGiftHeadIv = (ImageView) convertView.findViewById(R.id.chat_item_head_right2);

			viewHolder.leftView = convertView.findViewById(R.id.chat_item_goddess_skill_left);
			viewHolder.leftSecondGiftView = (RelativeLayout) convertView.findViewById(R.id.rl_chat_item_goddess_skill_gift_left);
			viewHolder.rightView = convertView.findViewById(R.id.chat_item_goddess_skill_right);
			viewHolder.rightSecondGiftView = (RelativeLayout) convertView.findViewById(R.id.rl_chat_item_goddess_skill_gift_right);

			viewHolder.leftSkill = (TextView) convertView.findViewById(R.id.item_chat_goddess_skill_left_tv);
			viewHolder.leftGift = (ImageView) convertView.findViewById(R.id.item_chat_skill_gift_left_img);
			viewHolder.leftGiftName = (TextView) convertView.findViewById(R.id.item_chat_skill_gift_name_left_tv);
			viewHolder.leftGlod = (TextView) convertView.findViewById(R.id.item_chat_skill_glod_left_tv);
			viewHolder.leftCharm = (TextView) convertView.findViewById(R.id.item_chat_skill_charm_left_tv);
			viewHolder.leftTime = (TextView) convertView.findViewById(R.id.item_chat_reward_time_left_tv);
			viewHolder.leftPostscript = (TextView) convertView.findViewById(R.id.item_chat_postscript_left_tv);
			viewHolder.leftRefuse = (TextView) convertView.findViewById(R.id.chat_item_left_refulse_tv);
			viewHolder.leftAccept = (TextView) convertView.findViewById(R.id.chat_item_left_accept_tv);
			viewHolder.leftBottomBt = convertView.findViewById(R.id.chat_item_left_buttom_botton_ll);
			viewHolder.leftInviteStatus = (TextView) convertView.findViewById(R.id.chat_item_left_skill_has_accept_tv);
			viewHolder.leftSecondGift = (ImageView) convertView.findViewById(R.id.item_chat_skill_gift_left_img2);
			viewHolder.leftSecondGiftName = (TextView) convertView.findViewById(R.id.item_chat_skill_gift_name_left_tv2);
			viewHolder.leftSecondGlod = (TextView) convertView.findViewById(R.id.item_chat_skill_glod_left_tv2);
			viewHolder.leftSecondCharm = (TextView) convertView.findViewById(R.id.item_chat_skill_charm_left_tv2);
			viewHolder.leftPostscriptLL = (LinearLayout) convertView.findViewById(R.id.item_chat_postsript_left_ll);

			viewHolder.itemNotice = (TextView) convertView.findViewById(R.id.chat_item_notice);

			viewHolder.rightSkill = (TextView) convertView.findViewById(R.id.item_chat_goddess_skill_right_tv);
			viewHolder.rightGift = (ImageView) convertView.findViewById(R.id.item_chat_skill_gift_right_img);
			viewHolder.rightGiftName = (TextView) convertView.findViewById(R.id.item_chat_skill_gift_name_right_tv);
			viewHolder.rightGlod = (TextView) convertView.findViewById(R.id.item_chat_skill_glod_right_tv);
			viewHolder.rightCharm = (TextView) convertView.findViewById(R.id.item_chat_skill_charm_right_tv);
			viewHolder.rightTime = (TextView) convertView.findViewById(R.id.item_chat_reward_time_right_tv);
			viewHolder.rightPostscript = (TextView) convertView.findViewById(R.id.item_chat_postscript_right_tv);
			viewHolder.rightNoResponse = (TextView) convertView.findViewById(R.id.item_chat_right_skill_status_tv);
			viewHolder.rightSecondGift = (ImageView) convertView.findViewById(R.id.item_chat_skill_gift_right_img2);
			viewHolder.rightSecondGiftName = (TextView) convertView.findViewById(R.id.item_chat_skill_gift_name_right_tv2);
			viewHolder.rightSecondGlod = (TextView) convertView.findViewById(R.id.item_chat_skill_glod_right_tv2);
			viewHolder.rightSecondCharm = (TextView) convertView.findViewById(R.id.item_chat_skill_charm_right_tv2);
			viewHolder.rightPostscriptLL = (LinearLayout) convertView.findViewById(R.id.item_chat_postsript_right_ll);

			leftHeadClickListener = new HeadClickListener(true, activity);
			viewHolder.leftHeadIv.setOnClickListener(leftHeadClickListener);
			viewHolder.leftGiftHeadIv.setOnClickListener(leftHeadClickListener);
			rightHeadClickListener = new HeadClickListener(false, activity);
			viewHolder.rightHeadIv.setOnClickListener(rightHeadClickListener);
			viewHolder.rightGiftHeadIv.setOnClickListener(rightHeadClickListener);

			viewHolder.leftAccept.setOnClickListener(acceptClickListener);
			viewHolder.leftRefuse.setOnClickListener(refuseClickListener);

			convertView.setTag(viewHolder);
			convertView.setTag(viewHolder.leftHeadIv.getId(), leftHeadClickListener);
			convertView.setTag(viewHolder.rightHeadIv.getId(), rightHeadClickListener);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			leftHeadClickListener = (HeadClickListener) convertView.getTag(viewHolder.leftHeadIv.getId());
			rightHeadClickListener = (HeadClickListener) convertView.getTag(viewHolder.rightHeadIv.getId());
		}

		String message = chatEntity.getMessage();
		ChatMessageEntity entity = ChatMessageBiz.decodeMessage(message);
		viewHolder.leftAccept.setTag(chatEntity);
		viewHolder.leftRefuse.setTag(chatEntity);
		String picKey = entity.getGiftKey() + BAConstants.LOAD_180_APPENDSTR;
		String sex = activity.getString(R.string.str_he);
		if (friendSex == Gender.FEMALE.getValue()) {
			sex = activity.getString(R.string.str_she);
		}
		String time = getTime(chatEntity);
		updateStatus(chatEntity);
		int status = chatEntity.getProgress();
		if (des == BAConstants.ChatDes.TO_ME.getValue()) {
			leftHeadClickListener.setEntity(chatEntity);
			viewHolder.leftView.setVisibility(View.VISIBLE);
			viewHolder.leftHeadIv.setVisibility(View.VISIBLE);
			viewHolder.rightView.setVisibility(View.GONE);
			viewHolder.rightHeadIv.setVisibility(View.GONE);
			viewHolder.leftGiftHeadIv.setVisibility(View.GONE);
			viewHolder.leftSecondGiftView.setVisibility(View.GONE);
			viewHolder.rightSecondGiftView.setVisibility(View.GONE);
			viewHolder.rightGiftHeadIv.setVisibility(View.GONE);
			setHeadImage(viewHolder.leftHeadIv, chatEntity.getFromID());

			viewHolder.leftSkill.setText(entity.getContent());
			imageLoader.displayImage("http://" + picKey, viewHolder.leftGift, option);
			viewHolder.leftGiftName.setText(activity.getResources().getString(R.string.reward_affirm_gift) + entity.getGiftName());
			viewHolder.leftGlod.setText(activity.getResources().getString(R.string.reward_affirm_cost) + entity.getGold());
			viewHolder.leftCharm.setText(activity.getResources().getString(R.string.reward_affirm_charm) + "  " + entity.getCharmeffect());
			viewHolder.leftTime.setText(time);

			if (TextUtils.isEmpty(chatEntity.getRevStr2())) {
				viewHolder.leftPostscript.setVisibility(View.GONE);
				viewHolder.leftPostscriptLL.setVisibility(View.GONE);
			} else {
				viewHolder.leftPostscript.setVisibility(View.VISIBLE);
				viewHolder.leftPostscriptLL.setVisibility(View.VISIBLE);
				setChatTextShow(chatEntity.getRevStr2(), viewHolder.leftPostscript);
			}

			switch (status) {
			case 0:
				viewHolder.leftSecondGiftView.setVisibility(View.VISIBLE);
				viewHolder.leftGiftHeadIv.setVisibility(View.VISIBLE);
				viewHolder.leftBottomBt.setVisibility(View.GONE);
				viewHolder.leftInviteStatus.setVisibility(View.VISIBLE);
				viewHolder.itemNotice.setVisibility(View.VISIBLE);
				viewHolder.leftInviteStatus.setText(activity.getString(R.string.str_you_has_accept_invite, sex));
				viewHolder.itemNotice.setText(activity.getString(R.string.str_accept_skill_success));
				setHeadImage(viewHolder.leftGiftHeadIv, chatEntity.getFromID());
				imageLoader.displayImage("http://" + picKey, viewHolder.leftSecondGift, option);
				viewHolder.leftSecondGiftName.setText(activity.getResources().getString(R.string.str_send) + entity.getGiftName());
				viewHolder.leftSecondGlod.setText(activity.getResources().getString(R.string.reward_affirm_cost) + entity.getGold());
				viewHolder.leftSecondCharm.setText(activity.getResources().getString(R.string.reward_affirm_charm) + "  " + entity.getCharmeffect());
				viewHolder.leftTime.setText(activity.getString(R.string.str_has_accept));
				break;
			case 1:
				viewHolder.leftSecondGiftView.setVisibility(View.GONE);
				viewHolder.leftGiftHeadIv.setVisibility(View.GONE);
				viewHolder.leftBottomBt.setVisibility(View.GONE);
				viewHolder.leftInviteStatus.setVisibility(View.VISIBLE);
				viewHolder.itemNotice.setVisibility(View.VISIBLE);
				viewHolder.leftInviteStatus.setText(activity.getString(R.string.str_refuse_invite, sex));
				viewHolder.itemNotice.setText(activity.getString(R.string.str_refuse_invite_for_skill, sex));
				viewHolder.leftTime.setText(activity.getString(R.string.str_has_refuse));
				break;
			case 2:
				viewHolder.leftSecondGiftView.setVisibility(View.GONE);
				viewHolder.leftGiftHeadIv.setVisibility(View.GONE);
				viewHolder.leftBottomBt.setVisibility(View.VISIBLE);
				viewHolder.leftInviteStatus.setVisibility(View.GONE);
				viewHolder.itemNotice.setVisibility(View.VISIBLE);
				viewHolder.itemNotice.setText(activity.getString(R.string.str_skill_safety_notice));
				break;
			case 3:
				viewHolder.leftSecondGiftView.setVisibility(View.GONE);
				viewHolder.leftGiftHeadIv.setVisibility(View.GONE);
				viewHolder.leftBottomBt.setVisibility(View.GONE);
				viewHolder.itemNotice.setVisibility(View.VISIBLE);
				viewHolder.itemNotice.setText(activity.getString(R.string.str_you_no_response_invite_fail,
						Integer.parseInt(entity.getLefttime()) / 60));
				viewHolder.leftInviteStatus.setVisibility(View.VISIBLE);
				viewHolder.leftInviteStatus.setText(activity.getString(R.string.str_wait_time_out));
				viewHolder.leftTime.setText(activity.getString(R.string.str_turned_out));
				break;

			default:
				break;
			}

		} else {
			viewHolder.leftView.setVisibility(View.GONE);
			viewHolder.leftHeadIv.setVisibility(View.GONE);
			viewHolder.leftGiftHeadIv.setVisibility(View.GONE);
			viewHolder.leftSecondGiftView.setVisibility(View.GONE);
			viewHolder.rightSecondGiftView.setVisibility(View.GONE);
			viewHolder.rightGiftHeadIv.setVisibility(View.GONE);
			viewHolder.rightView.setVisibility(View.VISIBLE);
			viewHolder.rightHeadIv.setVisibility(View.VISIBLE);
			setHeadImage(viewHolder.rightHeadIv, BAApplication.mLocalUserInfo.uid.intValue());

			viewHolder.rightSkill.setText(entity.getContent());
			imageLoader.displayImage("http://" + picKey, viewHolder.rightGift, option);
			viewHolder.rightGiftName.setText(activity.getResources().getString(R.string.reward_affirm_gift) + entity.getGiftName());
			viewHolder.rightGlod.setText(activity.getResources().getString(R.string.reward_affirm_cost) + entity.getGold());
			viewHolder.rightCharm.setText(activity.getResources().getString(R.string.reward_affirm_charm) + "  " + entity.getCharmeffect());
			viewHolder.rightTime.setText(time);

			if (TextUtils.isEmpty(chatEntity.getRevStr2())) {
				viewHolder.rightPostscript.setVisibility(View.GONE);
				viewHolder.rightPostscriptLL.setVisibility(View.GONE);
			} else {
				viewHolder.rightPostscript.setVisibility(View.VISIBLE);
				viewHolder.rightPostscriptLL.setVisibility(View.VISIBLE);
				setChatTextShow(chatEntity.getRevStr2(), viewHolder.rightPostscript);
			}

			switch (status) {
			case 0:
				viewHolder.rightGiftHeadIv.setVisibility(View.VISIBLE);
				viewHolder.rightSecondGiftView.setVisibility(View.VISIBLE);
				viewHolder.rightNoResponse.setVisibility(View.VISIBLE);
				viewHolder.itemNotice.setVisibility(View.VISIBLE);
				viewHolder.rightNoResponse.setText(activity.getString(R.string.str_has_accept_invite_other_side));
				viewHolder.itemNotice.setText(activity.getString(R.string.str_accept_skill_success_other_side, sex));

				setHeadImage(viewHolder.rightGiftHeadIv, BAApplication.mLocalUserInfo.uid.intValue());
				imageLoader.displayImage("http://" + picKey, viewHolder.rightSecondGift, option);
				viewHolder.rightSecondGiftName.setText(activity.getResources().getString(R.string.str_send) + entity.getGiftName());
				viewHolder.rightSecondGlod.setText(activity.getResources().getString(R.string.reward_affirm_cost) + entity.getGold());
				viewHolder.rightSecondCharm.setText(activity.getResources().getString(R.string.reward_affirm_charm) + "  " + entity.getCharmeffect());
				viewHolder.rightTime.setText(activity.getString(R.string.str_has_accept));
				break;
			case 1:
				viewHolder.rightGiftHeadIv.setVisibility(View.GONE);
				viewHolder.rightSecondGiftView.setVisibility(View.GONE);
				viewHolder.rightNoResponse.setVisibility(View.VISIBLE);
				viewHolder.itemNotice.setVisibility(View.VISIBLE);
				viewHolder.rightNoResponse.setText(activity.getString(R.string.str_has_refuse_invite_other_side));
				viewHolder.itemNotice.setText(activity.getString(R.string.str_refuse_invite_for_skill_other_side, sex));
				viewHolder.rightTime.setText(activity.getString(R.string.str_has_refuse));
				break;
			case 2:
				viewHolder.rightGiftHeadIv.setVisibility(View.GONE);
				viewHolder.rightSecondGiftView.setVisibility(View.GONE);
				viewHolder.rightNoResponse.setVisibility(View.VISIBLE);
				viewHolder.itemNotice.setVisibility(View.GONE);
				viewHolder.rightNoResponse.setText(activity.getString(R.string.str_no_response));
				break;
			case 3:
				viewHolder.rightGiftHeadIv.setVisibility(View.GONE);
				viewHolder.rightSecondGiftView.setVisibility(View.GONE);
				viewHolder.itemNotice.setVisibility(View.VISIBLE);
				viewHolder.rightNoResponse.setVisibility(View.VISIBLE);
				viewHolder.rightNoResponse.setText(activity.getString(R.string.str_no_response_invite_fail));
				viewHolder.itemNotice.setText(activity.getString(R.string.str_no_response_invite_fail_other_side));
				viewHolder.rightTime.setText(activity.getString(R.string.str_turned_out));
				break;

			default:
				break;
			}
		}
		return convertView;
	}

	private void setChatTextShow(String message, TextView tv) {//设置纯文本数据显示
		if (!TextUtils.isEmpty(message)) {
			String unicode = EmojiParser.getInstance(activity).parseEmoji(message);
			SpannableString builder = EmojiFaceConversionUtil.getInstace().getExpressionString(activity, unicode, HaremEmotionUtil.EMOJI_MIDDLE_SIZE);
			tv.setText(builder);
		}

	}

	private class ViewHolder {
		private View leftView;
		private TextView leftSkill;
		private ImageView leftGift;
		private TextView leftGlod;
		private TextView leftGiftName;
		private TextView leftCharm;
		private TextView leftTime;
		private TextView leftPostscript;
		private TextView leftRefuse;
		private TextView leftAccept;
		private View leftBottomBt;
		private TextView leftInviteStatus;
		private RelativeLayout leftSecondGiftView;
		private LinearLayout leftPostscriptLL;

		private ImageView leftSecondGift;
		private TextView leftSecondGlod;
		private TextView leftSecondGiftName;
		private TextView leftSecondCharm;

		private ImageView leftHeadIv, leftGiftHeadIv, rightHeadIv, rightGiftHeadIv;
		private TextView itemNotice;

		private View rightView;
		private TextView rightSkill;
		private ImageView rightGift;
		private TextView rightGlod;
		private TextView rightGiftName;
		private TextView rightCharm;
		private TextView rightTime;
		private TextView rightPostscript;
		private TextView rightNoResponse;
		private RelativeLayout rightSecondGiftView;
		private LinearLayout rightPostscriptLL;

		private ImageView rightSecondGift;
		private TextView rightSecondGlod;
		private TextView rightSecondGiftName;
		private TextView rightSecondCharm;
	}

	private void updateStatus(ChatDatabaseEntity entity) {
		if (entity == null) {
			return;
		}

		if (entity.getProgress() != 2) {
			return;
		}

		if (isTimeOut(entity)) {
			entity.setProgress(3);
			callBack.skillInviteStatusCallBack(entity, 3, isGroupChat);
		}
	}

	private String getTime(ChatDatabaseEntity chatEntity) {
		String time = "";
		if (chatEntity != null) {
			long curtime = System.currentTimeMillis() / 1000;
			long endtime = Long.parseLong(chatEntity.getRevStr3());
			long lefttime = endtime - curtime;
			if (lefttime <= 0) {
				time = activity.getString(R.string.str_turned_out);
			} else {
				if (lefttime < 60) {
					time = lefttime + activity.getString(R.string.str_millis);
				} else {
					time = (lefttime / 60) + activity.getString(R.string.str_minute);
				}
			}
		}
		return time;
	}

	private boolean isTimeOut(ChatDatabaseEntity chatEntity) {
		long curtime = System.currentTimeMillis() / 1000;
		long endtime = Long.parseLong(chatEntity.getRevStr3());
		long lefttime = endtime - curtime;
		if (lefttime <= 0) {
			return true;
		} else {
			return false;
		}
	}

	private OnClickListener acceptClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ChatDatabaseEntity dbEntity = (ChatDatabaseEntity) v.getTag();
			callBack.skillInviteAccept(dbEntity);
		}
	};

	private OnClickListener refuseClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ChatDatabaseEntity dbEntity = (ChatDatabaseEntity) v.getTag();
			callBack.skillInviteRefuse(dbEntity);
		}
	};

	public interface SkillInviteResCallBack {
		public void skillInviteAccept(ChatDatabaseEntity dbEntity);

		public void skillInviteRefuse(ChatDatabaseEntity dbEntity);

		public void skillInviteStatusCallBack(ChatDatabaseEntity dbEntity, int progress, boolean isGroupChat);
	}

}
