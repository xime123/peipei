package com.tshang.peipei.activity.chat.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.json.MemberInOutInfoJson;
import com.tshang.peipei.protocol.asn.gogirl.MemberInOutInfo;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: VideoItemView.java 
 *
 * @Description: 版本低提示
 *
 * @author Jeff  
 *
 * @date 2014年9月1日 下午1:39:45 
 *
 * @version V1.0   
 */
public class ViewVersionLowerItemAdapter extends ViewBaseChatItemAdapter {

	public ViewVersionLowerItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
	}

	public View getView(final int position, View convertView, ViewGroup parent, ChatDatabaseEntity chatEntity) {
		ViewHolder mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_versionlow, parent, false);
			mViewholer.versionLowTextView = (TextView) convertView.findViewById(R.id.chat_version_text);
			mViewholer.imageview = (ImageView) convertView.findViewById(R.id.iv_chat_horn);
			mViewholer.rootLayout = (LinearLayout) convertView.findViewById(R.id.chat_item_versionlow_root_layout);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHolder) convertView.getTag();

		}
		if (chatEntity != null) {
			String showMessage = chatEntity.getMessage();
			if (chatEntity.getType() == 14) {//系统消息
				String haremJoinMessage = activity.getString(R.string.str_have_join_your_harem);
				if (!TextUtils.isEmpty(showMessage)) {
					if (showMessage.equals(haremJoinMessage)) {
						if (friendSex == Gender.FEMALE.getValue()) {
							mViewholer.versionLowTextView.setText("\"" + friendNick + "\"已加入您的后宫,快点去后宫宠幸她吧");
						} else {
							mViewholer.versionLowTextView.setText("\"" + friendNick + "\"已加入您的后宫,快点去后宫使唤他吧");
						}
					} else {
						mViewholer.versionLowTextView.setText(showMessage);
					}
				}

			} else if (chatEntity.getType() == 27) {
				if (!TextUtils.isEmpty(showMessage)) {

					MemberInOutInfo memberInOutInfo = MemberInOutInfoJson.getMemberInOutInfo(showMessage);

					if (memberInOutInfo != null) {//
						String showToastData = "";
						int act = memberInOutInfo.act.intValue();
						if (act == 0) {//0-进群，1-离群，2-被踢
							showToastData = new String(memberInOutInfo.nick) + "\"加入后宫";
						} else if (act == 1) {
							showToastData = new String(memberInOutInfo.nick) + "\"退出后宫";
						} else if (act == 2) {
							showToastData = new String(memberInOutInfo.nick) + "\"被宫主踢出后宫";
						}
						setTextViewLeftDrawable(R.drawable.message_prompt_haremicon, mViewholer.versionLowTextView);
						mViewholer.versionLowTextView.setText(showToastData);
					}
				}
			} else if (chatEntity.getType() == 59) {
				mViewholer.versionLowTextView.setText(chatEntity.getRevStr3());
			} else if (chatEntity.getType() == 60) {
				mViewholer.versionLowTextView.setText(showMessage);
			} else {
				mViewholer.versionLowTextView.setText(activity.getString(R.string.version_low));
			}
		}
		
		if (isGroupChat) {
//			setGroupViewPading1(mViewholer.rootLayout);
		}

		return convertView;
	}

	private void setTextViewLeftDrawable(int source, TextView tv) {
		if (tv != null) {
			Drawable drawable = activity.getResources().getDrawable(source);
			if (drawable != null) {
				drawable.setBounds(-2, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
				tv.setCompoundDrawables(drawable, null, null, null);
			}
		}
	}

	private final class ViewHolder {
		TextView versionLowTextView;//版本低
		ImageView imageview;
		LinearLayout rootLayout;
	}

}
