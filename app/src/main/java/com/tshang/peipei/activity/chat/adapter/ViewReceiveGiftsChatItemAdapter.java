package com.tshang.peipei.activity.chat.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.ChatGiftAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfo;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.view.PeipeiGridView;

/**
 * @Title: ReceiveGiftsChatItemAdapterView.java 
 *
 * @Description: 收到了礼物的布局
 *
 * @author Jeff 
 *
 * @date 2014年9月1日 下午1:39:45 
 *
 * @version V1.0   
 */
public class ViewReceiveGiftsChatItemAdapter extends ViewBaseChatItemAdapter {

	public ViewReceiveGiftsChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
	}

	public View getView(int position, View convertView, ViewGroup parent, ChatDatabaseEntity chatEntity) {
		ViewHolder mViewholer;
		HeadClickListener leftHeadListener = null;
		HeadClickListener rightHeadListener = null;
		if (convertView == null) {
			mViewholer = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_gift, parent, false);
			mViewholer.chatLeftHead = (ImageView) convertView.findViewById(R.id.chat_item_head);
			mViewholer.chatLeftTime = (TextView) convertView.findViewById(R.id.chat_item_time);
			mViewholer.chatLeftContent = (TextView) convertView.findViewById(R.id.chat_item_content_text);
			mViewholer.giftToast = (TextView) convertView.findViewById(R.id.chat_item_gift_tv);
			mViewholer.gridView = (PeipeiGridView) convertView.findViewById(R.id.chat_item_gift_gv);
			mViewholer.leftLayout = (LinearLayout) convertView.findViewById(R.id.chat_item_gift_left_layout);
			mViewholer.rightLayout = (LinearLayout) convertView.findViewById(R.id.chat_item_gift_right_layout);

			mViewholer.chatRightHead = (ImageView) convertView.findViewById(R.id.chat_item_right_head);
			mViewholer.chatRightTime = (TextView) convertView.findViewById(R.id.chat_item_right_time);
			mViewholer.chatRightContent = (TextView) convertView.findViewById(R.id.chat_item_right_content_text);
			mViewholer.chatRightGiftToast = (TextView) convertView.findViewById(R.id.chat_item_right_gift_tv);
			mViewholer.chatRightGridView = (PeipeiGridView) convertView.findViewById(R.id.chat_right_item_gift_gv);

			leftHeadListener = new HeadClickListener(true, activity);
			rightHeadListener = new HeadClickListener(true, activity);
			mViewholer.chatLeftHead.setOnClickListener(leftHeadListener);
			mViewholer.chatRightHead.setOnClickListener(rightHeadListener);

			convertView.setTag(mViewholer);
			convertView.setTag(mViewholer.chatLeftHead.getId(), leftHeadListener);
			convertView.setTag(mViewholer.chatRightHead.getId(), rightHeadListener);

		} else {
			mViewholer = (ViewHolder) convertView.getTag();
			leftHeadListener = (HeadClickListener) convertView.getTag(mViewholer.chatLeftHead.getId());
			rightHeadListener = (HeadClickListener) convertView.getTag(mViewholer.chatRightHead.getId());
		}
		if (chatEntity != null) {
			int des = chatEntity.getDes();
			String message = chatEntity.getMessage();
			if (des == BAConstants.ChatDes.TO_ME.getValue()) {
				String revStr3 = chatEntity.getRevStr3();
				if (!TextUtils.isEmpty(revStr3) && Integer.parseInt(revStr3) == 1) {
					if (!TextUtils.isEmpty(chatEntity.getRevStr1()) && Integer.parseInt(chatEntity.getRevStr1()) == Gender.MALE.getValue()) {
						mViewholer.chatLeftHead.setImageResource(R.drawable.dynamic_defalut_man);
					} else {
						mViewholer.chatLeftHead.setImageResource(R.drawable.dynamic_defalut_woman);
					}
				} else {
					setHeadImage(mViewholer.chatLeftHead, chatEntity.getFromID());
					leftHeadListener.setEntity(chatEntity);
				}
				setTimeShow(mViewholer.chatLeftTime, chatEntity.getCreateTime());
				if (!TextUtils.isEmpty(message)) {
					ChatMessageEntity messageEntity = ChatMessageBiz.decodeMessage(message);
					if (messageEntity != null) {
						GiftInfo info = new GiftInfo();
						info.name = messageEntity.getGiftName().getBytes();
						info.pickey = messageEntity.getGiftKey().getBytes();
						int Giftid = 1;
						if (!TextUtils.isEmpty(messageEntity.getGiftId())) {
							try {
								Giftid = Integer.parseInt(messageEntity.getGiftId());
							} catch (NumberFormatException e) {
								e.printStackTrace();
								Giftid = 1;
							}
						}
						String giftStr = null;
						if (Giftid != 100) {
							if (!TextUtils.isEmpty(revStr3) && Integer.parseInt(revStr3) == 1) {
								giftStr = String.format(activity.getString(R.string.chat_gift_context_message), new String(chatEntity.getRevStr2()));
							} else
								giftStr = String.format(activity.getString(R.string.chat_gift_context_message), new String(
										BAApplication.mLocalUserInfo.nick));
							mViewholer.giftToast.setText(R.string.chat_get_gift_context);
						} else {
							giftStr = activity.getString(R.string.chat_gift_context_message2);
							mViewholer.giftToast.setText(R.string.chat_get_gift_context2);
						}
						mViewholer.chatLeftContent.setText(giftStr);
						String giftSize = messageEntity.getGiftSize();
						if (!TextUtils.isEmpty(giftSize)) {
							try {
								int iGiftSize = Integer.parseInt(messageEntity.getGiftSize());
								List<GiftInfo> giftList = new ArrayList<GiftInfo>();
								for (int i = 0; i < iGiftSize; i++) {
									giftList.add(info);
								}
								ChatGiftAdapter giftAdapter = new ChatGiftAdapter(activity);
								mViewholer.gridView.setAdapter(giftAdapter);
								giftAdapter.setList(giftList);
								mViewholer.chatRightGridView.setSelection(giftAdapter.getCount() - 1);
								giftAdapter.notifyDataSetChanged();
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
						}
					}
				}
				mViewholer.leftLayout.setVisibility(View.VISIBLE);
				mViewholer.rightLayout.setVisibility(View.GONE);
			} else if (des == BAConstants.ChatDes.TO_FRIEDN.getValue()) {
				Log.i("Aaron", "revStr3==" + chatEntity.getRevStr3());
				if (!TextUtils.isEmpty(chatEntity.getRevStr3()) && "1".equals(chatEntity.getRevStr3())) {
					setHeadImage(mViewholer.chatRightHead, BAApplication.getInstance().mLocalUserInfo.uid.intValue());
				} else
					setHeadImage(mViewholer.chatRightHead, chatEntity.getFromID());
				rightHeadListener.setEntity(chatEntity);
				setTimeShow(mViewholer.chatRightTime, chatEntity.getCreateTime());
				if (!TextUtils.isEmpty(message)) {
					ChatMessageEntity messageEntity = ChatMessageBiz.decodeMessage(message);
					if (messageEntity != null) {
						GiftInfo info = new GiftInfo();
						info.name = messageEntity.getGiftName().getBytes();
						info.pickey = messageEntity.getGiftKey().getBytes();
						int Giftid = 1;
						if (!TextUtils.isEmpty(messageEntity.getGiftId())) {
							try {
								Giftid = Integer.parseInt(messageEntity.getGiftId());
							} catch (NumberFormatException e) {
								e.printStackTrace();
								Giftid = 1;
							}
						}

						String giftStr = null;
						if (Giftid != 100) {
							giftStr = String.format(activity.getString(R.string.chat_gift_context_message), new String(chatEntity.getRevStr2()));
							mViewholer.chatRightGiftToast.setText("送给TA的礼物");
						} else {
							giftStr = activity.getString(R.string.chat_gift_context_message2);
							mViewholer.giftToast.setText(R.string.chat_get_gift_context2);
						}

						mViewholer.chatRightContent.setText(giftStr);
						String giftSize = messageEntity.getGiftSize();
						if (!TextUtils.isEmpty(giftSize)) {
							try {
								int iGiftSize = Integer.parseInt(messageEntity.getGiftSize());
								List<GiftInfo> giftList = new ArrayList<GiftInfo>();
								for (int i = 0; i < iGiftSize; i++) {
									giftList.add(info);
								}
								ChatGiftAdapter giftAdapter = new ChatGiftAdapter(activity);
								mViewholer.chatRightGridView.setAdapter(giftAdapter);
								giftAdapter.setList(giftList);
								mViewholer.chatRightGridView.setSelection(giftAdapter.getCount() - 1);
								giftAdapter.notifyDataSetChanged();
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
						}
					}
				}
				mViewholer.leftLayout.setVisibility(View.GONE);
				mViewholer.rightLayout.setVisibility(View.VISIBLE);
			}
		}
		return convertView;
	}

	private final class ViewHolder {
		//左边布局元素
		//		LinearLayout llLeft;
		ImageView chatLeftHead;//头像
		TextView chatLeftContent;//谁送的礼物
		TextView chatLeftTime;//聊天时间
		PeipeiGridView gridView;//礼物列表
		TextView giftToast;//礼物消息类型提示，有回赠和收到的类别

		private LinearLayout leftLayout, rightLayout;

		private ImageView chatRightHead;
		private TextView chatRightContent;
		private TextView chatRightTime;
		private PeipeiGridView chatRightGridView;
		private TextView chatRightGiftToast;
	}
}
