package com.tshang.peipei.activity.skillnew.adapter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.reward.PublishRewardActivity;
import com.tshang.peipei.activity.skillnew.bean.GoddessSkillInfo;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.protocol.asn.gogirl.SkillTextInfo;

/**
 * @Title: GoddessSkillListAdapter.java 
 *
 * @Description: 选着技能列表适配器
 *
 * @author DYH  
 *
 * @date 2015-11-5 下午1:53:31 
 *
 * @version V1.0   
 */
public class GoddessSkillListAdapter extends ArrayListAdapter<SkillTextInfo> {
	private SelectSkillCallBack callBack;
	private List<SkillTextInfo> hasList;
	private GoddessSkillInfo info;
	private Dialog dialog;
	
	public GoddessSkillListAdapter(Activity context, SelectSkillCallBack callBack) {
		super(context);
		this.callBack = callBack;
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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.adapter_goddesslist_item, null);
			viewHolder.tv_skill_name = (TextView) convertView.findViewById(R.id.tv_skill_name);
			viewHolder.tv_select = (TextView) convertView.findViewById(R.id.tv_select);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_skill_name.setText("");
		SkillTextInfo item = mList.get(position);
		viewHolder.item = item;
		viewHolder.tv_skill_name.setText(new String(item.content));
		if (item.isaddskill.intValue() == 1) {
			viewHolder.tv_select.setBackgroundResource(R.drawable.godddesslist_add_bg);
			viewHolder.tv_select.setText(R.string.str_add_new);
		} else {
			viewHolder.tv_select.setText(mContext.getString(R.string.dialog_cancel));
			viewHolder.tv_select.setBackgroundResource(R.drawable.godddesslist_del_bg);
		}
		viewHolder.tv_select.setTag(viewHolder);
		viewHolder.tv_select.setOnClickListener(itemClickListener);
		return convertView;
	}
	
	private OnClickListener itemClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ViewHolder viewHolder = (ViewHolder) v.getTag();
			if (viewHolder != null && viewHolder.item != null) {
				if (viewHolder.item.isaddskill.intValue() == 1) {
					if(hasList != null && info != null && hasList.size() >= info.getTotalskillnum()){
						BaseUtils.showTost(mContext, mContext.getString(R.string.str_skill_size_exception, info.getTotalskillnum()));
						return;
					}
					showDetailDialog(viewHolder);
				} else {
					viewHolder.tv_select.setText(mContext.getString(R.string.str_add_new));
					viewHolder.tv_select.setBackgroundResource(R.drawable.godddesslist_add_bg);
					viewHolder.item.isaddskill = BigInteger.valueOf(1);
					callBack.deleteSkill(viewHolder.item);
				}
			}
			notifyDataSetChanged();
		}
	};
	
	private void showDetailDialog(final ViewHolder viewHolder){
		View dialogView = LayoutInflater.from(mContext)
				.inflate(R.layout.reward_skill_selete_comfir_dialog_layout, null);
		TextView tvName = (TextView) dialogView.findViewById(R.id.tv_skill_name);
		tvName.setVisibility(View.GONE);
		TextView tv = (TextView) dialogView.findViewById(R.id.reward_skill_comfir_tv);
		TextView descTv = (TextView) dialogView.findViewById(R.id.reward_skill_desc_comfir_tv);
		descTv.setText(new String(viewHolder.item.desc));
		tv.setText(new String(viewHolder.item.content));
		dialog = DialogFactory.showMsgDialog(mContext, "", dialogView, mContext.getString(R.string.str_add_new), mContext.getString(R.string.reward_slete_age), null, new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewHolder.tv_select.setBackgroundResource(R.drawable.godddesslist_del_bg);
				viewHolder.tv_select.setText(R.string.dialog_cancel);
				viewHolder.item.isaddskill = BigInteger.valueOf(0);
				callBack.selectedSkill(viewHolder.item);
				DialogFactory.dimissDialog(dialog);
			}
		});
	}
	
	public void setHasList(List<SkillTextInfo> hasList) {
		this.hasList = hasList;
	}
	
	public void setInfo(GoddessSkillInfo info) {
		this.info = info;
	}

	private class ViewHolder {
		private TextView tv_skill_name;
		private TextView tv_select;
		private SkillTextInfo item;
	}
	
	public interface SelectSkillCallBack{
		public void selectedSkill(SkillTextInfo item);
		public void deleteSkill(SkillTextInfo item);
	}
}
