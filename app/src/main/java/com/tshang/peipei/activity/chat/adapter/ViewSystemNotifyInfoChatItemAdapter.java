package com.tshang.peipei.activity.chat.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.activity.mine.MineFaqActivity;
import com.tshang.peipei.activity.store.StoreIntegralActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

import de.greenrobot.event.EventBus;

/**
 * @Title: JoinHaremChatItemAdapter.java 
 *
 * @Description: 系统通知来消息了
 *
 * @author Jeff  
 *
 * @date 2015年03月09日 上午11:39:45 
 *
 * @version V2.1.1   
 */
public class ViewSystemNotifyInfoChatItemAdapter extends ViewBaseChatItemAdapter {
	public ViewSystemNotifyInfoChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, BAHandler handler, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent, ChatDatabaseEntity chatEntity) {
		ViewHolder mViewholer;
		DetailClickListener detailClickListener = null;
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_systemnotifyinfo_type, parent, false);
			mViewholer.btnDetail = (Button) convertView.findViewById(R.id.ok_cancel);
			mViewholer.btnShare = (Button) convertView.findViewById(R.id.ok_sure);
			mViewholer.tv_content = (TextView) convertView.findViewById(R.id.tv_systemnotify_content);
			mViewholer.title = (TextView) convertView.findViewById(R.id.tv_systemnotify_title);

			detailClickListener = new DetailClickListener();
			mViewholer.btnDetail.setOnClickListener(detailClickListener);
			mViewholer.btnDetail.setTag(mViewholer.btnDetail.getId(), detailClickListener);

			convertView.setTag(mViewholer);

		} else {
			mViewholer = (ViewHolder) convertView.getTag();
			detailClickListener = (DetailClickListener) convertView.getTag(mViewholer.btnDetail.getId());

		}
		if (chatEntity != null) {
			String message = chatEntity.getMessage();
			if (!TextUtils.isEmpty(message)) {
				ChatMessageEntity messageEntity = ChatMessageBiz.decodeMessage(message);
				if (messageEntity != null) {
					mViewholer.btnShare.setOnClickListener(new ShareClickListener(messageEntity));

					mViewholer.title.setText(messageEntity.getTitle());
					mViewholer.tv_content.setText(messageEntity.getDetail());
					String type = messageEntity.getType();
					if (!TextUtils.isEmpty(type)) {
						try {
							int typeValue = Integer.parseInt(type);
							if (typeValue == 3 || typeValue == 4 || typeValue == 5) {
								mViewholer.btnDetail.setVisibility(View.GONE);
							} else {
								mViewholer.btnDetail.setVisibility(View.VISIBLE);
								if (detailClickListener != null) {
									detailClickListener.setTypeId(typeValue);
									if (typeValue == 6) {//活动
										detailClickListener.setStrActionUrl(messageEntity.getCoverpickey());
									}
								}
							}

						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}

				}
			}

		}
		return convertView;
	}

	private final class ViewHolder {
		//左边布局元素

		Button btnDetail;//忽略
		Button btnShare;//接受
		TextView title;//下单标题
		TextView tv_content;//下单提示

	}

	private class DetailClickListener implements OnClickListener {

		private int typeId;
		private String strActionUrl = "";

		public DetailClickListener() {

		}

		public void setTypeId(int typeId) {
			this.typeId = typeId;
		}

		public void setStrActionUrl(String strActionUrl) {
			this.strActionUrl = strActionUrl;
		}

		@Override
		public void onClick(View v) {
			if (typeId == 0) {//到积分商城
				BaseUtils.openActivity(activity, StoreIntegralActivity.class);
			} else if (typeId == 1) {//到猜拳记录
				MineFaqActivity.openMineFaqActivity(activity, MineFaqActivity.FINGER_VALUE);
			} else if (typeId == 2) {//时时彩
				MineFaqActivity.openMineFaqActivity(activity, MineFaqActivity.GAMES_VALUE);
			} else if (typeId == 6) {
				Bundle bundle = new Bundle();
				bundle.putInt(MineFaqActivity.WHERE_FROM, MineFaqActivity.MAIN_HALL_VALUE);
				String verifystr = SharedPreferencesTools.getInstance(activity, BAApplication.mLocalUserInfo.uid.intValue() + "")
						.getStringValueByKey(SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL);
				if (!TextUtils.isEmpty(strActionUrl)) {
					bundle.putString("url", strActionUrl + "?u=" + verifystr + "&p=android");
					bundle.putString("title", "活动");
					BaseUtils.openActivity(activity, MineFaqActivity.class, bundle);
				}
			}
		}

	}

	private class ShareClickListener implements OnClickListener {

		private ChatMessageEntity messageEntity;

		public ShareClickListener(ChatMessageEntity messageEntity) {
			this.messageEntity = messageEntity;
		}

		@Override
		public void onClick(View v) {
			NoticeEvent noticeEvent = new NoticeEvent();
			noticeEvent.setFlag(NoticeEvent.NOTICE80);
			noticeEvent.setObj(messageEntity);
			EventBus.getDefault().post(noticeEvent);
		}

	}
}
