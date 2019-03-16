package com.tshang.peipei.activity.chat.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.base.FormatUtil;
import com.tshang.peipei.base.NumericUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: ViewSolitaireRedPacketChatItemAdapter.java 
 *
 * @Description: 后宫红包抢接龙适配器
 *
 * @author DYH  
 *
 * @date 2015-12-9 上午15:42:41 
 *
 * @version V1.0   
 */
public class ViewGrapSolitaireRedPacketChatItemAdapter extends ViewBaseChatItemAdapter {

	public ViewGrapSolitaireRedPacketChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, BAHandler handler, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
		this.handler = handler;
	}

	private BAHandler handler;

	@Override
	public View getView(int position, View convertView, ViewGroup parent, ChatDatabaseEntity chatEntity) {
		ViewHolder viewHolder = null;
		HeadClickListener leftHeadClickListener = null;
		HeadClickListener rightHeadClickListener = null;
		RedPacketleftListener leftListener = null;
		RedPacketRightListener rightListener = null;
		int des = chatEntity.getDes();
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(activity, R.layout.item_chat_listview_solitaire_redpacket, null);
			viewHolder.leftView = convertView.findViewById(R.id.chat_item_solitaire_left);
			viewHolder.rightView = convertView.findViewById(R.id.chat_item_solitaire_right);
			viewHolder.ll_all = convertView.findViewById(R.id.ll_all);
			viewHolder.chat_item_notice = (TextView) convertView.findViewById(R.id.chat_item_notice);

			viewHolder.leftHeadIv = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left);
			viewHolder.rightHeadIv = (ImageView) convertView.findViewById(R.id.chat_item_head_right);
			viewHolder.leftNick = (TextView) convertView.findViewById(R.id.chat_item_nick_solitair_left);
			viewHolder.leftBg = (LinearLayout) convertView.findViewById(R.id.chat_item_solitair_left);

			viewHolder.leftRedPacketMoney = (TextView) convertView.findViewById(R.id.tv_left_redpacket_money);
			viewHolder.leftJionPerson = (TextView) convertView.findViewById(R.id.tv_left_jion_person);

			viewHolder.rightRedPacketMoney = (TextView) convertView.findViewById(R.id.tv_right_redpacket_money);
			viewHolder.rightJionPerson = (TextView) convertView.findViewById(R.id.tv_right_jion_person);
			viewHolder.rightNick = (TextView) convertView.findViewById(R.id.chat_item_nick_solitair_right);
			viewHolder.rightBg = (LinearLayout) convertView.findViewById(R.id.chat_item_solitair_right);

			viewHolder.rootLayout = (LinearLayout) convertView.findViewById(R.id.solitaire_root_layout);

			leftListener = new RedPacketleftListener(activity, handler);
			rightListener = new RedPacketRightListener(activity, handler);
			viewHolder.leftView.setOnClickListener(leftListener);
			viewHolder.rightView.setOnClickListener(rightListener);
			leftHeadClickListener = new HeadClickListener(true, activity);
			viewHolder.leftHeadIv.setOnClickListener(leftHeadClickListener);
			rightHeadClickListener = new HeadClickListener(false, activity);
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

		String message = chatEntity.getMessage();
		ChatMessageEntity entity = ChatMessageBiz.decodeMessage(message);

		if (des == BAConstants.ChatDes.TO_ME.getValue()) {
			leftHeadClickListener.setEntity(chatEntity);
			viewHolder.leftView.setVisibility(View.VISIBLE);
			viewHolder.leftHeadIv.setVisibility(View.VISIBLE);
			viewHolder.rightView.setVisibility(View.GONE);
			viewHolder.rightHeadIv.setVisibility(View.INVISIBLE);

			viewHolder.leftBg.setVisibility(View.VISIBLE);
			viewHolder.rightBg.setVisibility(View.GONE);

			setHeadImage(viewHolder.leftHeadIv, chatEntity.getFromID());
			if (Integer.parseInt(entity.getOrggoldcoin()) > 0) {
				viewHolder.leftRedPacketMoney.setText(activity.getString(R.string.str_current_money, activity.getString(R.string.gold_money),
						FormatUtil.formatNumber(NumericUtils.parseLong(entity.getOrgtotalgoldcoin(), 0)) + activity.getString(R.string.gold_money)));
			} else {
				viewHolder.leftRedPacketMoney
						.setText(activity.getString(
								R.string.str_current_money,
								activity.getString(R.string.silver_money),
								FormatUtil.formatNumber(NumericUtils.parseLong(entity.getOrgtotalgoldcoin(), 0))
										+ activity.getString(R.string.silver_money)));
			}
			leftListener.setDate(entity);
			if (entity.getJionPersons() == null) {
				viewHolder.leftJionPerson.setText(activity.getString(R.string.str_has_join_person, 0));
			} else {
				viewHolder.leftJionPerson.setText(activity.getString(R.string.str_has_join_person, entity.getJionPersons().size()));
			}
		} else {
			viewHolder.leftView.setVisibility(View.GONE);
			viewHolder.leftHeadIv.setVisibility(View.INVISIBLE);
			viewHolder.rightView.setVisibility(View.VISIBLE);
			viewHolder.rightHeadIv.setVisibility(View.VISIBLE);

			viewHolder.leftBg.setVisibility(View.GONE);
			viewHolder.rightBg.setVisibility(View.VISIBLE);

			setHeadImage(viewHolder.rightHeadIv, chatEntity.getFromID());
			if (Integer.parseInt(entity.getOrggoldcoin()) > 0) {
				viewHolder.rightRedPacketMoney.setText(activity.getString(R.string.str_current_money, activity.getString(R.string.gold_money),
						FormatUtil.formatNumber(NumericUtils.parseLong(entity.getOrgtotalgoldcoin(), 0)) + activity.getString(R.string.gold_money)));
			} else {
				viewHolder.rightRedPacketMoney
						.setText(activity.getString(
								R.string.str_current_money,
								activity.getString(R.string.silver_money),
								FormatUtil.formatNumber(NumericUtils.parseLong(entity.getOrgtotalgoldcoin(), 0))
										+ activity.getString(R.string.silver_money)));
			}
			rightListener.setDate(entity);
			if (entity.getJionPersons() == null) {
				viewHolder.rightJionPerson.setText(activity.getString(R.string.str_has_join_person, 0));
			} else {
				viewHolder.rightJionPerson.setText(activity.getString(R.string.str_has_join_person, entity.getJionPersons().size()));
			}
		}

		if (Integer.parseInt(entity.getRedpacketstatus()) == 1 || Integer.parseInt(entity.getRedpacketstatus()) == 2) {
			viewHolder.ll_all.setVisibility(View.VISIBLE);
			viewHolder.chat_item_notice.setVisibility(View.VISIBLE);
			viewHolder.chat_item_notice.setText(activity.getString(R.string.str_the_redpacket_open_success, entity.getWinnick(),
					entity.getCreatenick(), entity.getTotalgoldcoin()));
		} else if (Integer.parseInt(entity.getRedpacketstatus()) == 4) {
			viewHolder.ll_all.setVisibility(View.GONE);
			long leftTime = (NumericUtils.parseLong(entity.getEndtime(), 0) - NumericUtils.parseLong(entity.getCreatetime(), 0));
			long minute = 0;
			if (leftTime > 60) {
				minute = leftTime / 60;
				viewHolder.chat_item_notice.setText(activity.getString(R.string.str_the_redpacket_time_out, minute, entity.getCreatenick(),
						entity.getTotalgoldcoin()));
			} else {
				viewHolder.chat_item_notice.setText(activity.getString(R.string.str_the_redpacket_time_out2, leftTime, entity.getCreatenick(),
						entity.getTotalgoldcoin()));
			}
			viewHolder.chat_item_notice.setVisibility(View.VISIBLE);
		} else {
			viewHolder.ll_all.setVisibility(View.VISIBLE);
			viewHolder.chat_item_notice.setVisibility(View.GONE);
		}

		if (isGroupChat) {

			viewHolder.leftNick.setText(chatEntity.getRevStr2());
			viewHolder.rightNick.setText(new String(BAApplication.mLocalUserInfo.nick));
			viewHolder.leftNick.setVisibility(View.VISIBLE);
			viewHolder.rightNick.setVisibility(View.VISIBLE);
			
			setNickListener(viewHolder.leftNick, viewHolder.leftNick.getText().toString());
		}

		return convertView;
	}

	private class ViewHolder {
		private View leftView;
		private TextView leftRedPacketMoney;
		private TextView leftJionPerson;
		private ImageView leftHeadIv, rightHeadIv;
		private View rightView;
		private TextView rightRedPacketMoney;
		private TextView rightJionPerson;
		private View ll_all;
		private TextView chat_item_notice;
		private LinearLayout rootLayout;

		private TextView leftNick, rightNick;
		private LinearLayout leftBg, rightBg;
	}

	private class RedPacketleftListener implements OnClickListener {//左边抢红包监听
		private Activity activity;
		private BAHandler handler;
		private ChatMessageEntity entity;

		public void setDate(ChatMessageEntity entity) {
			this.entity = entity;
		}

		public RedPacketleftListener(Activity activity, BAHandler handler) {
			this.activity = activity;
			this.handler = handler;

		}

		@Override
		public void onClick(View v) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.HAREM_CHECK_SOLITAIRE_REDPACKET_STATUS, entity);
		}

	}

	private class RedPacketRightListener implements OnClickListener {//右边抢红包监听
		private Activity activity;
		private BAHandler handler;
		private ChatMessageEntity entity;

		public void setDate(ChatMessageEntity entity) {
			this.entity = entity;
		}

		public RedPacketRightListener(Activity activity, BAHandler handler) {
			this.activity = activity;
			this.handler = handler;

		}

		@Override
		public void onClick(View v) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.HAREM_CHECK_SOLITAIRE_REDPACKET_STATUS, entity);
		}

	}

}
