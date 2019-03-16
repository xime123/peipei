package com.tshang.peipei.activity.chat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfo;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: ChatGiftAdapter.java 
 *
 * @Description: 礼物适配
 *
 * @author allen  
 *
 * @date 2014-6-26 下午3:03:45 
 *
 * @version V1.0   
 */
public class ChatGiftAdapter extends ArrayListAdapter<GiftInfo> {

	private LinearLayout.LayoutParams linParams;
	private DisplayImageOptions options;

	public ChatGiftAdapter(Activity context) {
		super(context);
		int width = (BasePhone.getScreenWidth(context) - BaseUtils.dip2px(context, 72)) / 3;
		linParams = new LinearLayout.LayoutParams(width, width);
		options = ImageOptionsUtils.getImageKeyOptions(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_gift, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.giftImage = (ImageView) convertView.findViewById(R.id.chat_gift_item_image);
			viewHolder.giftImage.setLayoutParams(linParams);
			viewHolder.giftText = (TextView) convertView.findViewById(R.id.chat_gift_item_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		GiftInfo info = mList.get(position);
		if (info != null) {

			viewHolder.giftText.setText(new String(info.name));
			String key = new String(info.pickey) + BAConstants.LOAD_180_APPENDSTR;
			imageLoader.displayImage("http://" + key, viewHolder.giftImage, options);
		}
		return convertView;
	}

	final class ViewHolder {
		ImageView giftImage;
		TextView giftText;
	}
}
