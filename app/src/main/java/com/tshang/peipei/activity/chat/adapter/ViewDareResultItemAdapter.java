package com.tshang.peipei.activity.chat.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: VideoItemView.java 
 *
 * @Description: 大冒险结果
 *
 * @author allen  
 *
 * @date 20154年7月9日 下午2:39:45 
 *
 * @version V1.0   
 */
public class ViewDareResultItemAdapter extends ViewBaseChatItemAdapter {

	public ViewDareResultItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
	}

	public View getView(final int position, View convertView, ViewGroup parent, ChatDatabaseEntity chatEntity) {
		ViewHolder mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_dare_result, parent, false);
			mViewholer.head1 = (ImageView) convertView.findViewById(R.id.dare_item_user_head_1);
			mViewholer.nick1 = (TextView) convertView.findViewById(R.id.dare_item_user_nick_1);
			mViewholer.layout2 = (LinearLayout) convertView.findViewById(R.id.dare_item_user_2);
			mViewholer.head2 = (ImageView) convertView.findViewById(R.id.dare_item_user_head_2);
			mViewholer.nick2 = (TextView) convertView.findViewById(R.id.dare_item_user_nick_2);
			mViewholer.content = (TextView) convertView.findViewById(R.id.dare_item_user_content);

			mViewholer.rootLayout = (LinearLayout) convertView.findViewById(R.id.chat_dare_result_root_layout);

			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHolder) convertView.getTag();
		}
		if (chatEntity != null) {
			String showMessage = chatEntity.getMessage();
			if (!TextUtils.isEmpty(showMessage)) {
				ChatMessageEntity entity = ChatMessageBiz.decodeMessage(showMessage);
				int uid1 = Integer.parseInt(entity.getDareuid1());
				if (TextUtils.isEmpty(entity.getDareuid2())) {
					mViewholer.layout2.setVisibility(View.GONE);
				} else {
					mViewholer.layout2.setVisibility(View.VISIBLE);
					mViewholer.nick2.setText((new String(Base64.decode(entity.getDarenick2().getBytes(), Base64.DEFAULT))));
					int uid2 = Integer.parseInt(entity.getDareuid2());
					setHeadImage(mViewholer.head2, uid2);
				}

				mViewholer.nick1.setText((new String(Base64.decode(entity.getDarenick1().getBytes(), Base64.DEFAULT))));
				setHeadImage(mViewholer.head1, uid1);
				mViewholer.content.setText(entity.getMemo());
			}
		}
		if (isGroupChat) {
//			setGroupViewPading1(mViewholer.rootLayout);
		}
		return convertView;
	}

	private final class ViewHolder {
		ImageView head1;
		TextView nick1;
		LinearLayout layout2;
		ImageView head2;
		TextView nick2;

		TextView content;

		LinearLayout rootLayout;
	}

}
