package com.tshang.peipei.activity.chat.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.activity.mine.MineFaqActivity;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: VideoItemView.java 
 *
 * @Description: 系统帮助或者女王攻略消息
 *
 * @author Jeff  
 *
 * @date 2014年9月1日 下午1:39:45 
 *
 * @version V1.0   
 */
public class ViewHelpItemAdapter extends ViewBaseChatItemAdapter {

	public ViewHelpItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
	}

	public View getView(final int position, View convertView, ViewGroup parent, final ChatDatabaseEntity chatEntity) {
		ViewHolder mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_left_text, parent, false);
			mViewholer.chatLeftHead = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left);
			mViewholer.chatLeftTextView = (TextView) convertView.findViewById(R.id.chat_item_content_text);
			mViewholer.chatLeftTime = (TextView) convertView.findViewById(R.id.chat_item_time);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHolder) convertView.getTag();

		}
		if (chatEntity != null) {
			String message = chatEntity.getMessage();
			long time = chatEntity.getCreateTime();
			setHeadImage(mViewholer.chatLeftHead, chatEntity.getFromID());
			setTimeShow(mViewholer.chatLeftTime, time);
			mViewholer.chatLeftTextView.setText(Html.fromHtml(message));
			mViewholer.chatLeftTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MineFaqActivity.openMineFaqActivity(activity, MineFaqActivity.KING_VALLUE);
				}
			});
		}
		return convertView;
	}

	private final class ViewHolder {
		ImageView chatLeftHead;//头像
		TextView chatLeftTextView;//文本
		TextView chatLeftTime;//聊天时间

	}

}
