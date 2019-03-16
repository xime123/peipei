package com.tshang.peipei.activity.show.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;

/**
 * @Title: ShowBottomAdapter.java 
 *
 * @Description: 秀场底部按键适配
 *
 * @author allen  
 *
 * @date 2014-7-9 下午4:32:00 
 *
 * @version V1.0   
 */
public class ShowBottomAdapter extends ArrayListAdapter<String> {
	private String[] bottom;
	private Integer[] showPic = { R.drawable.message_icon_boardcast_selector, R.drawable.message_icon_baobox_selector };

	public ShowBottomAdapter(Activity context) {
		super(context);
		bottom = context.getResources().getStringArray(R.array.show_button_array);
		setList(bottom);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_show_button, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.bottomIv = (ImageView) convertView.findViewById(R.id.item_gv_chat_image);
			viewHolder.bottomTv = (TextView) convertView.findViewById(R.id.item_gv_chat_tv);
			viewHolder.bottomNew = (TextView) convertView.findViewById(R.id.item_gv_chat_count);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.bottomIv.setImageResource(showPic[position]);
		viewHolder.bottomTv.setText(bottom[position]);
		if (position == 1 && BAApplication.showBoxNum > 0) {
			viewHolder.bottomNew.setText(BAApplication.showBoxNum + "");
			viewHolder.bottomNew.setVisibility(View.VISIBLE);
		} else {
			viewHolder.bottomNew.setVisibility(View.GONE);
		}

		return convertView;
	}

	final class ViewHolder {
		ImageView bottomIv;
		TextView bottomTv;
		TextView bottomNew;
	}

}
