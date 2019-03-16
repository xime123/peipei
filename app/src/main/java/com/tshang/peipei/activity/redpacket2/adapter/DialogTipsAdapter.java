package com.tshang.peipei.activity.redpacket2.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;

/**
 * @Title: TipsAdapter.java 
 *
 * @Description: 展示描述的adapter 
 *
 * @author DYH  
 *
 * @date 2015-12-15 下午8:43:39 
 *
 * @version V1.0   
 */
public class DialogTipsAdapter extends ArrayListAdapter<String> {

	public DialogTipsAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.dialog_adapter_tip_item, null);
			viewHolder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
			viewHolder.tv_des = (TextView) convertView.findViewById(R.id.tv_des);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		String des = mList.get(position);
		if(!TextUtils.isEmpty(des)){
			des = des.trim();
			viewHolder.tv_number.setVisibility(View.VISIBLE);
			viewHolder.tv_number.setText(position + "");
			viewHolder.tv_des.setText(des);
			convertView.setVisibility(View.VISIBLE);
		}else{
			viewHolder.tv_number.setVisibility(View.GONE);
			viewHolder.tv_des.setText("");
			convertView.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	private class ViewHolder{
		public TextView tv_number;
		public TextView tv_des;
	}
}
