package com.tshang.peipei.activity.chat.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.activity.chat.listener.RedPacketDetailClick;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: ViewCreatRedPacketChatItemAdapter.java 
 *
 * @Description: 领取红包成功收到群消息推送界面
 *
 * @author Jeff  
 *
 * @date 2014年10月17日 下午1:39:45 
 *
 * @version V1.4.0   
 */
public class ViewGetRedPacketChatItemAdapter extends ViewBaseChatItemAdapter {

	public ViewGetRedPacketChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent, ChatDatabaseEntity chatEntity) {
		ViewHolder mViewholer;
		int des = chatEntity.getDes();
		RedPacketDetailClick redListener;
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_redpacket_desc_type, parent, false);
			mViewholer.llLeft = (LinearLayout) convertView.findViewById(R.id.ll_redpacket_desc);
			mViewholer.chatLeftTextView = (TextView) convertView.findViewById(R.id.tv_content);

			redListener = new RedPacketDetailClick(activity);
			mViewholer.llLeft.setOnClickListener(redListener);

			convertView.setTag(mViewholer);
			convertView.setTag(mViewholer.llLeft.getId(), redListener);
		} else {
			mViewholer = (ViewHolder) convertView.getTag();
			redListener = (RedPacketDetailClick) convertView.getTag(mViewholer.llLeft.getId());

		}
		String message = chatEntity.getMessage();
		String strMessage = activity.getString(R.string.str_get_content);
		if (!TextUtils.isEmpty(message)) {
			ChatMessageEntity chatMessageEntity = ChatMessageBiz.decodeMessage(message);
			if (chatMessageEntity != null) {
				int tatolNum = 0;
				int leftNum = 0;
				try {

					tatolNum = Integer.parseInt(chatMessageEntity.getTotalportionnum());
					leftNum = Integer.parseInt(chatMessageEntity.getLeftportionnum());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				strMessage = String.format(strMessage, chatMessageEntity.getHaveGetRedPacketCoinUserName(), chatMessageEntity.getIntdata(),
						chatMessageEntity.getTotalportionnum(), chatMessageEntity.getTotalgoldcoin(), (tatolNum - leftNum) + "");
				mViewholer.chatLeftTextView.setText(strMessage);
			}
			try {
				redListener.setData(Integer.parseInt(chatMessageEntity.getId()), Integer.parseInt(chatMessageEntity.getCreateuid()));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return convertView;
	}

	private final class ViewHolder {
		//左边布局元素
		LinearLayout llLeft;
		TextView chatLeftTextView;//文本

	}

}
