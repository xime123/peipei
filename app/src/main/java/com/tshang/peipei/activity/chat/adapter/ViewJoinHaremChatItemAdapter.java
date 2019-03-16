package com.tshang.peipei.activity.chat.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.protocol.asn.gogirl.ApplyJoinGroupInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.base.json.ApplyJoinGroupInfoJson;
import com.tshang.peipei.model.harem.JoinHarem;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: JoinHaremChatItemAdapter.java 
 *
 * @Description: 申请加入后宫适配
 *
 * @author Jeff  
 *
 * @date 2014年9月20日 下午1:39:45 
 *
 * @version V1.3.0   
 */
public class ViewJoinHaremChatItemAdapter extends ViewBaseChatItemAdapter {
	public ViewJoinHaremChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, BAHandler handler, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
		this.handler = handler;
	}

	private BAHandler handler;

	@Override
	public View getView(int position, View convertView, ViewGroup parent, ChatDatabaseEntity chatEntity) {
		ViewHolder mViewholer;
		HeadClickListener leftHeadListener = null;
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_group_join_harem_type, parent, false);
			mViewholer.chatLeftHead = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left);
			mViewholer.chatLeftTime = (TextView) convertView.findViewById(R.id.chat_item_join_harem_receive_time);
			mViewholer.btnIgnore = (Button) convertView.findViewById(R.id.ok_cancel);
			mViewholer.btnReceive = (Button) convertView.findViewById(R.id.ok_sure);

			leftHeadListener = new HeadClickListener(true, activity);
			mViewholer.chatLeftHead.setOnClickListener(leftHeadListener);

			convertView.setTag(mViewholer);
			convertView.setTag(mViewholer.chatLeftHead.getId(), leftHeadListener);

		} else {
			mViewholer = (ViewHolder) convertView.getTag();
			leftHeadListener = (HeadClickListener) convertView.getTag(mViewholer.chatLeftHead.getId());

		}
		if (chatEntity != null) {
			String message = chatEntity.getMessage();
			if (!TextUtils.isEmpty(message)) {
				ApplyJoinGroupInfo info = ApplyJoinGroupInfoJson.getApplyJoinGroupInfo(message);
				if (info != null) {
					GoGirlUserInfo userInfo = info.joinuser;
					if (userInfo != null) {
						int status = chatEntity.getStatus();
						if (status == ChatStatus.SENDING.getValue()) {//如果同意过，变灰
							setBtnEnabled(mViewholer.btnIgnore, mViewholer.btnReceive);
							mViewholer.btnReceive.setText(activity.getString(R.string.str_have_recivie));
						} else if (status == ChatStatus.FAILED.getValue()) {//之前忽略过此消息
							setBtnEnabled(mViewholer.btnIgnore, mViewholer.btnReceive);
							mViewholer.btnIgnore.setText(activity.getString(R.string.str_have_ignore));
						} else {
							mViewholer.btnIgnore.setOnClickListener(new ClickListener(mViewholer.btnIgnore, mViewholer.btnReceive, chatEntity,
									userInfo.uid.intValue(), info.groupid.intValue()));
							mViewholer.btnReceive.setOnClickListener(new ClickListener(mViewholer.btnIgnore, mViewholer.btnReceive, chatEntity,
									userInfo.uid.intValue(), info.groupid.intValue()));
						}
					}
				}
			}
			long time = chatEntity.getCreateTime();
			setHeadImage(mViewholer.chatLeftHead, chatEntity.getFromID());
			leftHeadListener.setEntity(chatEntity);
			setTimeShow(mViewholer.chatLeftTime, time);
		}
		return convertView;
	}

	private final class ViewHolder {
		//左边布局元素
		ImageView chatLeftHead;//头像
		Button btnIgnore;//忽略
		Button btnReceive;//接受
		TextView chatLeftTime;//聊天时间
	}

	private class ClickListener implements OnClickListener {
		private Button btnIgnore;
		private Button btnReceive;
		private ChatDatabaseEntity chatEntity;
		private int uid;
		private int groupid;

		public ClickListener(Button btnIgnore, Button btnReceive, ChatDatabaseEntity chatEntity, int uid, int groupid) {
			this.btnIgnore = btnIgnore;
			this.btnReceive = btnReceive;
			this.chatEntity = chatEntity;
			this.uid = uid;
			this.groupid = groupid;
		}

		@Override
		public void onClick(View v) {
			if (v == btnIgnore) {
				setBtnEnabled(btnIgnore, btnReceive);
				handler.sendMessage(handler.obtainMessage(HandlerValue.HAREM_AGREE_GROUP_IGNORE_VALUE, 1000, 0, chatEntity));
			} else if (v == btnReceive) {
				setBtnEnabled(btnIgnore, btnReceive);
				JoinHarem.getInstance().agreeJoinGroup(activity, groupid, uid, handler, chatEntity);
			}

		}

	}

	private void setBtnEnabled(Button b1, Button b2) {
		b1.setEnabled(false);
		b2.setEnabled(false);
		b1.setTextColor(activity.getResources().getColor(R.color.gray));
		b2.setTextColor(activity.getResources().getColor(R.color.gray));
	}
}
