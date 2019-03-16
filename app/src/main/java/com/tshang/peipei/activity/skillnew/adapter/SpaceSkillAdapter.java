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
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
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
public class SpaceSkillAdapter extends ArrayListAdapter<SkillTextInfo>{

	private boolean isHost = false;
	
	public SpaceSkillAdapter(Activity context, int whosUid) {
		super(context);
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(context);
		if (userEntity != null && userEntity.uid.intValue() == whosUid) {//说明是主人的身份
			isHost = true;
		}
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
			convertView = View.inflate(mContext, R.layout.adapter_space_skill_item, null);
			viewHolder.tv_skill_item = (TextView) convertView.findViewById(R.id.tv_skill_item);
			viewHolder.tv_add_skill = (TextView) convertView.findViewById(R.id.tv_add_skill);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if(isHost){
			if(mList != null && position < (mList.size()-1)){
				SkillTextInfo item = mList.get(position);
				viewHolder.tv_skill_item.setText(new String(item.content));
				viewHolder.tv_skill_item.setBackgroundDrawable(BaseUtils.createGradientDrawable(2, mContext.getResources().getColor((R.color.transparent)), 180,
							Color.parseColor("#" + new String(item.fillcolor))));
				viewHolder.tv_skill_item.setTextColor(Color.parseColor("#" + new String(item.textcolor)));
				viewHolder.tv_add_skill.setVisibility(View.GONE);
			}else{
				viewHolder.tv_add_skill.setVisibility(View.VISIBLE);
				viewHolder.tv_skill_item.setVisibility(View.GONE);
			}
		}else{
			SkillTextInfo item = mList.get(position);
			viewHolder.tv_skill_item.setText(new String(item.content));
			viewHolder.tv_skill_item.setBackgroundDrawable(BaseUtils.createGradientDrawable(2, mContext.getResources().getColor((R.color.transparent)), 180,
						Color.parseColor("#" + new String(item.fillcolor))));
			viewHolder.tv_skill_item.setTextColor(Color.parseColor("#" + new String(item.textcolor)));
			viewHolder.tv_add_skill.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	private class ViewHolder{
		private TextView tv_skill_item;
		private TextView tv_add_skill;
	}

}
