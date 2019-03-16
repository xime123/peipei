package com.tshang.peipei.activity.chat.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.NumericUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: ViewRewardChatItemAdapter.java 
 *
 * @Description: 悬赏适配器 
 *
 * @author Aaron  
 *
 * @date 2015-10-8 下午2:39:54 
 *
 * @version V1.0   
 */
public class ViewRewardChatItemAdapter extends ViewBaseChatItemAdapter {

	private DisplayImageOptions option;

	public ViewRewardChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
		option = ImageOptionsUtils.getRewardGiftOptions(activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent, ChatDatabaseEntity chatEntity) {
		ViewHolder viewHolder = null;
		HeadClickListener leftHeadClickListener = null;
		HeadClickListener rightHeadClickListener = null;
		int des = chatEntity.getDes();
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_reward, parent, false);

			viewHolder.leftSkill = (TextView) convertView.findViewById(R.id.item_chat_reward_skill_left_tv);
			viewHolder.leftGift = (ImageView) convertView.findViewById(R.id.item_chat_reward_gift_left_img);
			viewHolder.leftGlod = (TextView) convertView.findViewById(R.id.item_chat_reward_glod_left_tv);
			viewHolder.leftGiftName = (TextView) convertView.findViewById(R.id.item_chat_reward_gift_name_left_tv);
			viewHolder.leftCharm = (TextView) convertView.findViewById(R.id.item_chat_reward_charm_left_tv);
			viewHolder.leftTime = (TextView) convertView.findViewById(R.id.item_chat_reward_time_left_tv);
			viewHolder.leftLayout = (LinearLayout) convertView.findViewById(R.id.item_chat_reward_left_layout);
			viewHolder.leftIntegral = (TextView) convertView.findViewById(R.id.item_chat_reward_integral_name_left_tv);
			viewHolder.leftCreateTime = (TextView) convertView.findViewById(R.id.chat_item_reward_left_time_tv);
			viewHolder.leftSkillDesc = (TextView) convertView.findViewById(R.id.item_chat_reward_skill_desc_left_tv);

			viewHolder.rightSkill = (TextView) convertView.findViewById(R.id.item_chat_reward_skill_right_tv);
			viewHolder.rightGift = (ImageView) convertView.findViewById(R.id.item_chat_reward_gift_right_img);
			viewHolder.rightGlod = (TextView) convertView.findViewById(R.id.item_chat_reward_glod_right_tv);
			viewHolder.rightGiftName = (TextView) convertView.findViewById(R.id.item_chat_reward_gift_name_right_tv);
			viewHolder.rightCharm = (TextView) convertView.findViewById(R.id.item_chat_reward_charm_right_tv);
			viewHolder.rightTime = (TextView) convertView.findViewById(R.id.item_chat_reward_time_right_tv);
			viewHolder.rightLayout = (LinearLayout) convertView.findViewById(R.id.item_chat_reward_right_layout);
			viewHolder.rightIntegral = (TextView) convertView.findViewById(R.id.item_chat_reward_integral_name_right_tv);
			viewHolder.rightCreateTime = (TextView) convertView.findViewById(R.id.chat_item_reward_right_time_tv);
			viewHolder.rightSkillDesc = (TextView) convertView.findViewById(R.id.item_chat_reward_skill_desc_right_tv);

			viewHolder.leftHeadIv = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left);
			viewHolder.rightHeadIv = (ImageView) convertView.findViewById(R.id.chat_item_head_right);

			leftHeadClickListener = new HeadClickListener(true, activity);

			rightHeadClickListener = new HeadClickListener(false, activity);

			convertView.setTag(viewHolder);
			convertView.setTag(viewHolder.leftHeadIv.getId(), leftHeadClickListener);
			convertView.setTag(viewHolder.rightHeadIv.getId(), rightHeadClickListener);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			leftHeadClickListener = (HeadClickListener) convertView.getTag(viewHolder.leftHeadIv.getId());
			rightHeadClickListener = (HeadClickListener) convertView.getTag(viewHolder.rightHeadIv.getId());
		}
		String message = chatEntity.getMessage();
		ChatMessageEntity entity = ChatMessageBiz.decodeMessage(message);
		String time = "";
		if (entity != null) {
			long endTime = Long.parseLong(entity.getLastupdatetime());
			long currentTime = System.currentTimeMillis() / 1000;
			if (currentTime > endTime) {
				time = activity.getResources().getString(R.string.reward_end_str);
			} else {
				time = BaseTimes.getHH_mmTime(endTime - currentTime);
			}
		}
		String str = chatEntity.getRevStr3();
		if (!TextUtils.isEmpty(str) && str.equals("1")) {
			time = activity.getResources().getString(R.string.reward_end_str);
		}

		if (des == BAConstants.ChatDes.TO_ME.getValue()) {
			if (entity != null) {
				String picKey = entity.getGiftKey() + BAConstants.LOAD_180_APPENDSTR;
				viewHolder.leftSkill.setText(entity.getAccessloyalty());
				viewHolder.leftGiftName.setText(activity.getResources().getString(R.string.reward_affirm_gift) + entity.getGiftName());
				viewHolder.leftGlod.setText(activity.getResources().getString(R.string.reward_affirm_cost) + entity.getGlobid());
				viewHolder.leftIntegral.setText(activity.getResources().getString(R.string.reward_affirm_integral) + entity.getCancelflag());
				viewHolder.leftCharm.setText(activity.getResources().getString(R.string.reward_affirm_charm) + entity.getAlbumname());
				viewHolder.leftSkillDesc.setText(entity.getCoverpic());
				imageLoader.displayImage("http://" + picKey, viewHolder.leftGift, option);
			}

			//判断是否匿名悬赏
			if (!"".equals(entity.getDesc()) && NumericUtils.parseInt(entity.getDesc(), 0) == 1) {
				if (!TextUtils.isEmpty(chatEntity.getRevStr1()) && NumericUtils.parseInt(chatEntity.getRevStr1(), 0) == Gender.MALE.getValue()) {
					viewHolder.leftHeadIv.setImageResource(R.drawable.dynamic_defalut_man);
				} else {
					viewHolder.leftHeadIv.setImageResource(R.drawable.dynamic_defalut_woman);
				}
			} else {
				viewHolder.leftHeadIv.setOnClickListener(leftHeadClickListener);
				setHeadImage(viewHolder.leftHeadIv, chatEntity.getFromID());
			}

			viewHolder.leftTime.setText(time);
			setTimeShow(viewHolder.leftCreateTime, chatEntity.getCreateTime());
			leftHeadClickListener.setEntity(chatEntity);
			viewHolder.leftHeadIv.setVisibility(View.VISIBLE);
			viewHolder.leftLayout.setVisibility(View.VISIBLE);
			viewHolder.rightHeadIv.setVisibility(View.GONE);
			viewHolder.rightLayout.setVisibility(View.GONE);
		} else {
			if (entity != null) {
				String picKey = entity.getGiftKey() + BAConstants.LOAD_180_APPENDSTR;
				viewHolder.rightSkill.setText(entity.getAccessloyalty());
				viewHolder.rightGiftName.setText(activity.getResources().getString(R.string.reward_affirm_gift) + entity.getGiftName());
				viewHolder.rightGlod.setText(activity.getResources().getString(R.string.reward_affirm_cost) + entity.getGlobid());
				viewHolder.rightIntegral.setText(activity.getResources().getString(R.string.reward_affirm_integral) + entity.getCancelflag());
				viewHolder.rightCharm.setText(activity.getResources().getString(R.string.reward_affirm_charm) + entity.getAlbumname());
				imageLoader.displayImage("http://" + picKey, viewHolder.rightGift, option);
				viewHolder.rightSkillDesc.setText(entity.getCoverpic());
			}
			viewHolder.rightTime.setText(time);
			setHeadImage(viewHolder.rightHeadIv, chatEntity.getToUid());
			viewHolder.rightHeadIv.setOnClickListener(rightHeadClickListener);
			setTimeShow(viewHolder.rightCreateTime, chatEntity.getCreateTime());
			rightHeadClickListener.setEntity(chatEntity);
			viewHolder.leftHeadIv.setVisibility(View.GONE);
			viewHolder.leftLayout.setVisibility(View.GONE);
			viewHolder.rightHeadIv.setVisibility(View.VISIBLE);
			viewHolder.rightLayout.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	private class ViewHolder {
		//left
		private TextView leftSkill;
		private ImageView leftGift;
		private TextView leftGlod;
		private TextView leftGiftName;
		private TextView leftCharm;
		private TextView leftTime;
		private LinearLayout leftLayout;
		private TextView leftIntegral;
		private TextView leftCreateTime;
		private TextView leftSkillDesc;

		private ImageView leftHeadIv, rightHeadIv;

		//right
		private TextView rightSkill;
		private ImageView rightGift;
		private TextView rightGlod;
		private TextView rightGiftName;
		private TextView rightCharm;
		private TextView rightTime;
		private LinearLayout rightLayout;
		private TextView rightIntegral;
		private TextView rightCreateTime;
		private TextView rightSkillDesc;

	}
}
