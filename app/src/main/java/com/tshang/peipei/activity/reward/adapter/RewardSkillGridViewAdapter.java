package com.tshang.peipei.activity.reward.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.protocol.asn.gogirl.AwardTextInfo;

/**
 * @Title: RewardSkillGridViewAdapter.java 
 *
 * @Description: 发布悬赏技能列表适配
 *
 * @author Aaron  
 *
 * @date 2015-9-24 下午4:51:06 
 *
 * @version V1.0   
 */
public class RewardSkillGridViewAdapter extends ArrayListAdapter<AwardTextInfo> {

	private Activity context;
	private ViewHolder viewHolder = null;

	private int clickPosition = -1;

	private SkillCloseCallBack callBack;

	public RewardSkillGridViewAdapter(Activity context, SkillCloseCallBack callBack) {
		super(context);
		this.context = context;
		this.callBack = callBack;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.reward_skill_item_layout, null);
			viewHolder.mContentTV = (TextView) convertView.findViewById(R.id.reward_skill_item_context_tv);
			viewHolder.closeIv = (ImageView) convertView.findViewById(R.id.reward_skill_item_close_icon);
			viewHolder.addIcon = (ImageView) convertView.findViewById(R.id.reward_add_costom_icon_iv);
			viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.reward_skill_layout);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final AwardTextInfo entity = mList.get(position);
		viewHolder.mContentTV.setText(new String(entity.content));
		viewHolder.mContentTV.setTextColor(Color.parseColor("#" + new String(entity.textcolor)));

		if (entity.id.intValue() == -1) {
			viewHolder.addIcon.setVisibility(View.VISIBLE);
			viewHolder.layout.setBackgroundDrawable(BaseUtils.createGradientDrawable(2, Color.parseColor("#" + new String(entity.framecolor)), 180,
					Color.parseColor("#B1936E")));
		} else {
			viewHolder.addIcon.setVisibility(View.GONE);
			viewHolder.layout.setBackgroundDrawable(BaseUtils.createGradientDrawable(2, Color.parseColor("#" + new String(entity.framecolor)), 180,
					Color.parseColor("#FFFFFFFF")));
		}

		//设置点击效果
		if (clickPosition != -1 && clickPosition == position) {
			viewHolder.layout
					.setBackgroundDrawable(BaseUtils.createGradientDrawable(2, Color.parseColor("#ff5e49"), 180, Color.parseColor("#ff5e49")));
			viewHolder.mContentTV.setTextColor(Color.WHITE);
		}

		if (entity.id.intValue() == -2) {
			viewHolder.closeIv.setVisibility(View.VISIBLE);
		} else {
			viewHolder.closeIv.setVisibility(View.GONE);
		}

		viewHolder.closeIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (callBack != null) {
					callBack.close();
				}
			}
		});

		return convertView;
	}

	private class ViewHolder {
		private TextView mContentTV;
		private ImageView closeIv;
		private ImageView addIcon;
		private LinearLayout layout;
	}

	public void setClickPosition(int position) {
		this.clickPosition = position;
		notifyDataSetChanged();
	}

	public interface SkillCloseCallBack {
		public void close();
	}
}
