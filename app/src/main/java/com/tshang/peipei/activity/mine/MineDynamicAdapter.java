package com.tshang.peipei.activity.mine;

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
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.DynamicEntity;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: MineDynamicAdapter.java 
 *
 * @Description: 我的动态适配器
 *
 * @author allen  
 *
 * @date 2014-12-4 下午1:56:35 
 *
 * @version V1.0   
 */
public class MineDynamicAdapter extends ArrayListAdapter<DynamicEntity> {

	private DisplayImageOptions options_head;
	private long time;

	public MineDynamicAdapter(Activity context, long time) {
		super(context);
		options_head = ImageOptionsUtils.GetHeadUidSmallRounded(context);
		this.time = time;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler viewholer;
		if (convertView == null) {
			viewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_list, parent, false);
			initViewHoler(convertView, viewholer);
			convertView.setTag(viewholer);
		} else {
			viewholer = (ViewHoler) convertView.getTag();
		}
		DynamicEntity info = (DynamicEntity) mList.get(position);

		String key = info.getUserID() + BAConstants.LOAD_HEAD_UID_APPENDSTR;
		viewholer.headerImage.setTag(key);
		viewholer.headerImage.setImageResource(R.drawable.main_img_defaulthead_selector);
		imageLoader.displayImage("http://" + key, viewholer.headerImage, options_head);
		if (info.getSex() == Gender.FEMALE.getValue()) {
			viewholer.sexImage.setImageResource(R.drawable.broadcast_img_girl);
		} else {
			viewholer.sexImage.setImageResource(R.drawable.broadcast_img_boy);
		}

		if (info.getLatestUpdateTime() >= time) {
			viewholer.headerNew.setVisibility(View.VISIBLE);
		} else {
			viewholer.headerNew.setVisibility(View.INVISIBLE);
		}

		viewholer.timeText.setText(BaseTimes.getDiffTime(info.getLatestUpdateTime()));

		viewholer.contentText.setText(info.getSessionData());

		String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
				info.getUserID());
		viewholer.nameText.setText(TextUtils.isEmpty(alias) ? new String(info.getNick()) : alias);

		return convertView;
	}

	private void initViewHoler(View convertView, ViewHoler viewholer) {
		viewholer.headerImage = (ImageView) convertView.findViewById(R.id.dynamic_list_header_image);
		viewholer.headerNew = (ImageView) convertView.findViewById(R.id.dynamic_list_header_text);
		viewholer.nameText = (TextView) convertView.findViewById(R.id.dynamic_list_chat_name);
		viewholer.sexImage = (ImageView) convertView.findViewById(R.id.dynamic_list_chat_sex);
		viewholer.timeText = (TextView) convertView.findViewById(R.id.dynamic_list_chat_time);
		viewholer.contentText = (TextView) convertView.findViewById(R.id.dynamic_list_content);
	}

	final class ViewHoler {
		ImageView headerImage;
		ImageView headerNew;
		TextView nameText;
		ImageView sexImage;
		TextView timeText;
		TextView contentText;
	}
}
