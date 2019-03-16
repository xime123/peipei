package com.tshang.peipei.activity.chat.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.chat.bean.EmotionBean;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;

/**
 * 
 ******************************************
 * @author Jeff
 * @文件名称	:  HaremEmotionAdapter.java
 * @创建时间	: 2014-12-04下午02:34:01
 * @文件描述	: 宫表情填充器
 ******************************************
 */
@SuppressLint("NewApi")
public class HaremEmotionAdapter extends ArrayListAdapter<EmotionBean> {
	RelativeLayout.LayoutParams params;

	public HaremEmotionAdapter(Activity context) {
		super(context);
		int width = BasePhone.getScreenWidth(context);
		int itemWidth = (width - BaseUtils.dip2px(context, 30)) / 4;
		params = new RelativeLayout.LayoutParams(itemWidth, itemWidth);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_harem_face, null);
			viewHolder.iv_face = (ImageView) convertView.findViewById(R.id.item_iv_face);
			viewHolder.iv_face.setLayoutParams(params);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final EmotionBean bean = mList.get(position);
		if (bean != null) {
			viewHolder.iv_face.setImageResource(bean.getId());

//			viewHolder.iv_face.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//				}
//			});
		}
		return convertView;
	}

	class ViewHolder {

		public ImageView iv_face;
	}
}