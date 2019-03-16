package com.tshang.peipei.activity.redpacket2.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.mine.MineSettingUserInfoActivity;
import com.tshang.peipei.activity.space.SpaceActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.FormatUtil;
import com.tshang.peipei.base.NumericUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: HallSolitaireDataAdapter.java 
 *
 * @Description: 大厅接龙红包Adapter 
 *
 * @author DYH  
 *
 * @date 2016-1-14 下午4:32:35 
 *
 * @version V1.0   
 */
public class HallSolitaireDataAdapter extends ArrayListAdapter<ChatDatabaseEntity> {
	private DisplayImageOptions options_uid_head;//通过UID加载
	private BAHandler handler;
	
	public HallSolitaireDataAdapter(Activity context) {
		super(context);
		options_uid_head = ImageOptionsUtils.GetHeadUidSmallRounded(context);
	}
	
	public HallSolitaireDataAdapter(Activity context, BAHandler handler) {
		super(context);
		options_uid_head = ImageOptionsUtils.GetHeadUidSmallRounded(context);
		this.handler = handler;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		HeadClickListener leftHeadClickListener = null;
		HeadClickListener rightHeadClickListener = null;
		RedPacketleftListener leftListener = null;
		RedPacketRightListener rightListener = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_hall_listview_solitaire_redpacket, null);
			viewHolder.leftView = convertView.findViewById(R.id.chat_item_solitaire_left);
			viewHolder.rightView = convertView.findViewById(R.id.chat_item_solitaire_right);
			viewHolder.ll_all = convertView.findViewById(R.id.ll_all);
			viewHolder.chat_item_notice = (TextView) convertView.findViewById(R.id.chat_item_notice);
			
			viewHolder.leftHeadIv = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left);
			viewHolder.rightHeadIv = (ImageView) convertView.findViewById(R.id.chat_item_head_right);
			
			viewHolder.leftRedPacketMoney = (TextView) convertView.findViewById(R.id.tv_left_redpacket_money);
			viewHolder.leftJionPerson = (TextView) convertView.findViewById(R.id.tv_left_jion_person);
			
			viewHolder.rightRedPacketMoney = (TextView) convertView.findViewById(R.id.tv_right_redpacket_money);
			viewHolder.rightJionPerson = (TextView) convertView.findViewById(R.id.tv_right_jion_person);
			
			viewHolder.leftNick = (TextView) convertView.findViewById(R.id.tv_nick_left);
			viewHolder.rightNick = (TextView) convertView.findViewById(R.id.tv_nick_right);
			
			leftListener = new RedPacketleftListener(mContext, handler);
			rightListener = new RedPacketRightListener(mContext, handler);
			viewHolder.leftView.setOnClickListener(leftListener);
			viewHolder.rightView.setOnClickListener(rightListener);
			leftHeadClickListener = new HeadClickListener(true, mContext);
			viewHolder.leftHeadIv.setOnClickListener(leftHeadClickListener);
			rightHeadClickListener = new HeadClickListener(false, mContext);
			viewHolder.rightHeadIv.setOnClickListener(rightHeadClickListener);
			
			convertView.setTag(viewHolder.leftHeadIv.getId(), leftHeadClickListener);
			convertView.setTag(viewHolder.rightHeadIv.getId(), rightHeadClickListener);
			convertView.setTag(viewHolder.leftView.getId(), leftListener);
			convertView.setTag(viewHolder.rightView.getId(), rightListener);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			leftHeadClickListener = (HeadClickListener) convertView.getTag(viewHolder.leftHeadIv.getId());
			rightHeadClickListener = (HeadClickListener) convertView.getTag(viewHolder.rightHeadIv.getId());
			leftListener = (RedPacketleftListener) convertView.getTag(viewHolder.leftView.getId());
			rightListener = (RedPacketRightListener) convertView.getTag(viewHolder.rightView.getId());
		}
		
		ChatDatabaseEntity chatEntity = mList.get(position);
		int des = chatEntity.getDes();
		String message = chatEntity.getMessage();
		ChatMessageEntity entity = ChatMessageBiz.decodeMessage(message);

		if (des == BAConstants.ChatDes.TO_ME.getValue()) {
			leftHeadClickListener.setEntity(chatEntity);
			viewHolder.leftView.setVisibility(View.VISIBLE);
			viewHolder.leftHeadIv.setVisibility(View.VISIBLE);
			viewHolder.rightView.setVisibility(View.GONE);
			viewHolder.rightHeadIv.setVisibility(View.GONE);
			
			setHeadImage(viewHolder.leftHeadIv, chatEntity.getFromID());
			viewHolder.leftNick.setText(entity.getCreatenick());
			if(Integer.parseInt(entity.getOrggoldcoin()) > 0){
				viewHolder.leftRedPacketMoney.setText(mContext.getString(R.string.str_current_money, mContext.getString(R.string.gold_money), FormatUtil.formatNumber(NumericUtils.parseLong(entity.getOrgtotalgoldcoin(), 0)) + mContext.getString(R.string.gold_money)));
			}else{
				viewHolder.leftRedPacketMoney.setText(mContext.getString(R.string.str_current_money, mContext.getString(R.string.silver_money), FormatUtil.formatNumber(NumericUtils.parseLong(entity.getOrgtotalgoldcoin(), 0)) + mContext.getString(R.string.silver_money)));
			}
			
			leftListener.setDate(entity);
			if(entity.getJionPersons() == null){
				viewHolder.leftJionPerson.setText(mContext.getString(R.string.str_has_join_person, 0));
			}else{
				viewHolder.leftJionPerson.setText(mContext.getString(R.string.str_has_join_person, entity.getJionPersons().size()));
			}
		} else {
			viewHolder.leftView.setVisibility(View.GONE);
			viewHolder.leftHeadIv.setVisibility(View.GONE);
			viewHolder.rightView.setVisibility(View.VISIBLE);
			viewHolder.rightHeadIv.setVisibility(View.VISIBLE);
			
			setHeadImage(viewHolder.rightHeadIv, chatEntity.getFromID());
			viewHolder.rightNick.setText(entity.getCreatenick());
			if(Integer.parseInt(entity.getOrggoldcoin()) > 0){
				viewHolder.rightRedPacketMoney.setText(mContext.getString(R.string.str_current_money, mContext.getString(R.string.gold_money), FormatUtil.formatNumber(NumericUtils.parseLong(entity.getOrgtotalgoldcoin(), 0)) + mContext.getString(R.string.gold_money)));
			}else{
				viewHolder.rightRedPacketMoney.setText(mContext.getString(R.string.str_current_money, mContext.getString(R.string.silver_money), FormatUtil.formatNumber(NumericUtils.parseLong(entity.getOrgtotalgoldcoin(), 0)) + mContext.getString(R.string.silver_money)));
			}
			rightListener.setDate(entity);
			if(entity.getJionPersons() == null){
				viewHolder.rightJionPerson.setText(mContext.getString(R.string.str_has_join_person, 0));
			}else{
				viewHolder.rightJionPerson.setText(mContext.getString(R.string.str_has_join_person, entity.getJionPersons().size()));
			}
			
		}
		
		if(Integer.parseInt(entity.getRedpacketstatus()) == 1 || Integer.parseInt(entity.getRedpacketstatus()) == 2){
			viewHolder.ll_all.setVisibility(View.VISIBLE);
			viewHolder.chat_item_notice.setVisibility(View.VISIBLE);
			viewHolder.chat_item_notice.setText(mContext.getString(R.string.str_the_redpacket_open_success, entity.getWinnick(), entity.getCreatenick(), entity.getTotalgoldcoin()));
		}else if(Integer.parseInt(entity.getRedpacketstatus()) == 4){
			viewHolder.ll_all.setVisibility(View.GONE);
			long leftTime = (NumericUtils.parseLong(entity.getEndtime(), 0) - NumericUtils.parseLong(entity.getCreatetime(), 0));
			long minute = 0;
			if(leftTime > 60){
				minute = leftTime / 60;
				viewHolder.chat_item_notice.setText(mContext.getString(R.string.str_the_redpacket_time_out, minute, entity.getCreatenick(), entity.getTotalgoldcoin()));
			}else{
				viewHolder.chat_item_notice.setText(mContext.getString(R.string.str_the_redpacket_time_out2, leftTime, entity.getCreatenick(), entity.getTotalgoldcoin()));
			}
			viewHolder.chat_item_notice.setVisibility(View.VISIBLE);
			
		}else{
			viewHolder.ll_all.setVisibility(View.VISIBLE);
			viewHolder.chat_item_notice.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	private class ViewHolder {
		private View leftView;
		private TextView leftRedPacketMoney;
		private TextView leftJionPerson;
		private TextView leftNick;
		private ImageView leftHeadIv, rightHeadIv;
		private View rightView;
		private TextView rightRedPacketMoney;
		private TextView rightJionPerson;
		private TextView rightNick;
		private View ll_all;
		private TextView chat_item_notice;
	}
	
	private void setHeadImage(ImageView iv, int uid) {
		imageLoader.displayImage("http://" + uid + BAConstants.LOAD_HEAD_UID_APPENDSTR, iv, options_uid_head);
	}
	
	private class RedPacketleftListener implements OnClickListener {//左边抢红包监听
		private Activity mContext;
		private BAHandler handler;
		private ChatMessageEntity entity;

		public void setDate(ChatMessageEntity entity) {
			this.entity = entity;
		}

		public RedPacketleftListener(Activity mContext, BAHandler handler) {
			this.mContext = mContext;
			this.handler = handler;

		}

		@Override
		public void onClick(View v) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.HALL_CHECK_SOLITAIRE_REDPACKET_STATUS, entity);
		}

	}
	
	private class RedPacketRightListener implements OnClickListener {//右边抢红包监听
		private Activity mContext;
		private BAHandler handler;
		private ChatMessageEntity entity;
		
		public void setDate(ChatMessageEntity entity) {
			this.entity = entity;
		}
		
		public RedPacketRightListener(Activity mContext, BAHandler handler) {
			this.mContext = mContext;
			this.handler = handler;
			
		}
		
		@Override
		public void onClick(View v) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.HALL_CHECK_SOLITAIRE_REDPACKET_STATUS, entity);
		}
		
	}
	
	private class HeadClickListener implements OnClickListener {//头像点击事件
		public boolean isLeft = false;
		private Activity activiyt;
		private ChatDatabaseEntity entity;

		public void setEntity(ChatDatabaseEntity entity) {
			this.entity = entity;
		}

		public HeadClickListener(boolean isLeft, Activity activiyt) {
			this.isLeft = isLeft;
			this.activiyt = activiyt;
		}

		@Override
		public void onClick(View v) {
			if (isLeft) {
				if (entity == null) {
					return;
				}
				int uid = entity.getFromID();
				if (uid == BAConstants.PEIPEI_XIAOPEI || uid == BAConstants.PEIPEI_CHAT_TONGZHI) {
					return;
				}
				Bundle bundle = new Bundle();
				bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, uid);
				String strSex = entity.getRevStr1();
				if (TextUtils.isEmpty(strSex)) {
//					bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, friendSex);
//					BaseUtils.openActivityByNew(mContext, SpaceActivity.class, bundle);

				} else {
					try {
						int sex = Integer.parseInt(strSex);
						bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, sex);
						BaseUtils.openActivityByNew(mContext, SpaceActivity.class, bundle);

					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			} else {
				if (BAApplication.mLocalUserInfo != null) {
					Bundle bundle = new Bundle();
					bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, BAApplication.mLocalUserInfo.uid.intValue());
					bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, BAApplication.mLocalUserInfo.sex.intValue());
					BaseUtils.openActivity(activiyt, MineSettingUserInfoActivity.class, bundle);
				}
			}
		}

	}

}
