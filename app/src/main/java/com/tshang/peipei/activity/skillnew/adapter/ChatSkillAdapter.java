package com.tshang.peipei.activity.skillnew.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.protocol.asn.gogirl.SkillTextInfo;

/**
 * @Title: SpaceSkillAdapter.java 
 *
 * @Description: 个人主页女神技展示adapter
 *
 * @author DYH  
 *
 * @date 2015-11-6 下午2:12:00 
 *
 * @version V1.0   
 */
public class ChatSkillAdapter extends ArrayListAdapter<SkillTextInfo>{

	public ChatSkillAdapter(Activity context) {
		super(context);
	}
	
	public ArrayList<SkillTextInfo> getGoddessSkillListData(List<SkillTextInfo> list) {//去除重复的数据
		ArrayList<SkillTextInfo> newLists = new ArrayList<SkillTextInfo>();
		if (list != null && !list.isEmpty()) {
			for (SkillTextInfo skillTextInfo : list) {
				newLists.add(skillTextInfo);
			}
		}
		return newLists;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.adapter_chat_skill_item, null);
			viewHolder.tv_skill_item = (TextView) convertView.findViewById(R.id.tv_skill_item);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		SkillTextInfo item = mList.get(position);
		viewHolder.tv_skill_item.setText(new String(item.content));
		viewHolder.tv_skill_item.setBackgroundDrawable(BaseUtils.createGradientDrawable(2, mContext.getResources().getColor((R.color.transparent)), 180,
					Color.parseColor("#" + new String(item.fillcolor))));
		viewHolder.tv_skill_item.setTextColor(Color.parseColor("#" + new String(item.textcolor)));
		
		return convertView;
	}
	
	private class ViewHolder{
		private TextView tv_skill_item;
	}

}
