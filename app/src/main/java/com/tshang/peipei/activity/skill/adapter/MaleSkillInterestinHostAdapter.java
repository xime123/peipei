package com.tshang.peipei.activity.skill.adapter;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.ChatActivity;
import com.tshang.peipei.activity.dialog.SkillOrderDialog;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetParticipantInfo;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: 主人态男人的技能
 *
 * @Description:
 *
 * @author Jeff
 *
 * @version V1.4.0   
 */
public class MaleSkillInterestinHostAdapter extends ArrayListAdapter<RetParticipantInfo> {

	private DisplayImageOptions options;
	private int skillid;
	private int skilluid;
	private String skillGiftName;
	private int skillGiftNum;
	private BAHandler handler;

	public MaleSkillInterestinHostAdapter(Activity context, int skillid, int skilluid, String skillGiftName, int skillGiftNum, BAHandler handler) {
		super(context);
		options = ImageOptionsUtils.GetHeadKeyBigRounded(context);
		this.skillid = skillid;
		this.skilluid = skilluid;
		this.skillGiftName = skillGiftName;
		this.skillGiftNum = skillGiftNum;
		this.handler = handler;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_male_skill_interestin_host_item, parent, false);
			mViewholer.imageview = (ImageView) convertView.findViewById(R.id.adapter_interestin_head_item_iv);
			mViewholer.tvName = (TextView) convertView.findViewById(R.id.tv_username);
			mViewholer.tvMessage = (TextView) convertView.findViewById(R.id.tv_message);
			mViewholer.btnChat = (Button) convertView.findViewById(R.id.btn_skill_chat_item);
			mViewholer.btnOrder = (Button) convertView.findViewById(R.id.btn_skill_order_item);
			mViewholer.llBottom = (LinearLayout) convertView.findViewById(R.id.ll_bottom_item);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHoler) convertView.getTag();
		}
		if (position == mList.size() - 1) {
			mViewholer.llBottom.setBackgroundResource(R.drawable.homepage_skill_reward_particulars_btnbg_bot);
		} else {
			mViewholer.llBottom.setBackgroundResource(R.drawable.homepage_skill_reward_particulars_btnbg_mid);
		}

		RetParticipantInfo info = mList.get(position);
		if (info != null) {
			String key = new String(info.participantuserinfo.headpickey) + BAConstants.LOAD_HEAD_KEY_APPENDSTR;
			imageLoader.displayImage("http://" + key, mViewholer.imageview, options);

			String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
					info.participantuserinfo.uid.intValue());

			mViewholer.tvName.setText(TextUtils.isEmpty(alias) ? new String(info.participantuserinfo.nick) : alias);
			mViewholer.btnChat.setOnClickListener(new ChatListener(info.participantuserinfo));
			mViewholer.btnOrder.setOnClickListener(new OrderListener(info));

			SpannableStringBuilder style = new SpannableStringBuilder(mContext.getString(R.string.str_leave_message)
					+ new String(info.participantinfo.introduce));
			style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black)), 0, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			mViewholer.tvMessage.setText(style);

		}
		return convertView;
	}

	private class ChatListener implements OnClickListener {
		private GoGirlUserInfo info;

		public ChatListener(GoGirlUserInfo info) {
			this.info = info;
		}

		@Override
		public void onClick(View v) {
			if (info == null) {
				return;
			}
			String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
					info.uid.intValue());

			ChatActivity.intentChatActivity(mContext, info.uid.intValue(), TextUtils.isEmpty(alias) ? new String(info.nick) : alias,
					info.sex.intValue(), false, false, 0);
		}

	}

	private class OrderListener implements OnClickListener {
		RetParticipantInfo info;

		public OrderListener(RetParticipantInfo info) {
			this.info = info;
		}

		@Override
		public void onClick(View v) {
			if (info == null) {
				return;
			}
			new SkillOrderDialog(mContext, android.R.style.Theme_Translucent_NoTitleBar, info, skillGiftName, skillGiftNum, skillid, skilluid,
					handler).showDialog();
		}

	}

	final class ViewHoler {
		LinearLayout llBottom;
		ImageView imageview;
		TextView tvName;
		TextView tvMessage;
		Button btnChat;
		Button btnOrder;
	}

}
