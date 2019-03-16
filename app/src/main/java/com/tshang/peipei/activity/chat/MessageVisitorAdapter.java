package com.tshang.peipei.activity.chat;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.RetRelevantPeopleInfo;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: MessageWhoSawMeAdapter.java 
 *
 * @Description: 谁看过我界面适配器
 *
 * @author allen  
 *
 * @date 2014-8-11 下午7:36:17 
 *
 * @version V1.0   
 */
public class MessageVisitorAdapter extends ArrayListAdapter<RetRelevantPeopleInfo> {

	private DisplayImageOptions options_head;
	private long time;

	public MessageVisitorAdapter(Activity context, long time) {
		super(context);
		options_head = ImageOptionsUtils.GetHeadUidSmallRounded(context);
		this.time = time;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler viewholer;
		if (convertView == null) {
			viewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_visitor_list, parent, false);
			initViewHoler(convertView, viewholer);
			convertView.setTag(viewholer);
		} else {
			viewholer = (ViewHoler) convertView.getTag();
		}
		RetRelevantPeopleInfo info = (RetRelevantPeopleInfo) mList.get(position);

		String key = info.userinfo.uid.intValue() + BAConstants.LOAD_HEAD_UID_APPENDSTR;
		viewholer.headerImage.setTag(key);
		viewholer.headerImage.setImageResource(R.drawable.main_img_defaulthead_selector);
		imageLoader.displayImage("http://" + key, viewholer.headerImage, options_head);
		if (info.userinfo.sex.intValue() == Gender.FEMALE.getValue()) {
			viewholer.sexImage.setImageResource(R.drawable.broadcast_img_girl);
		} else {
			viewholer.sexImage.setImageResource(R.drawable.broadcast_img_boy);
		}

		if (info.relevantinfo.createtime.longValue() * 1000 >= time) {
			viewholer.headerNew.setVisibility(View.VISIBLE);
		} else {
			viewholer.headerNew.setVisibility(View.INVISIBLE);
		}

		viewholer.timeText.setText(BaseTimes.getDiffTime(info.relevantinfo.createtime.longValue() * 1000));

		String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
				info.userinfo.uid.intValue());
		viewholer.nameText.setText(TextUtils.isEmpty(alias) ? new String(info.userinfo.nick) : alias);

		return convertView;
	}

	private void initViewHoler(View convertView, ViewHoler viewholer) {
		viewholer.headerImage = (ImageView) convertView.findViewById(R.id.message_list_header_image);
		viewholer.headerNew = (ImageView) convertView.findViewById(R.id.message_list_header_text);
		viewholer.nameText = (TextView) convertView.findViewById(R.id.message_list_chat_name);
		viewholer.sexImage = (ImageView) convertView.findViewById(R.id.message_list_chat_sex);
		viewholer.timeText = (TextView) convertView.findViewById(R.id.message_list_chat_time);
	}

	final class ViewHoler {
		ImageView headerImage;
		ImageView headerNew;
		TextView nameText;
		ImageView sexImage;
		TextView timeText;
	}

}
