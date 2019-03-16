package com.tshang.peipei.activity.skillnew.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.protocol.asn.gogirl.SkillGiftInfo;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: SkillInviteGiftAdapter.java 
 *
 * @Description: 女神技能邀请礼物适配器
 *
 * @author DYH  
 *
 * @date 2015-11-10 下午7:02:55 
 *
 * @version V1.0   
 */
public class SkillInviteGiftAdapter extends ArrayListAdapter<SkillGiftInfo> {

	private int clickPosition = -1;

	private DisplayImageOptions option;

	public SkillInviteGiftAdapter(Activity context) {
		super(context);
		option = ImageOptionsUtils.getRewardGiftOptions(context);
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler viewHodler = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.reward_gift_item_layout, null);
			viewHodler = new ViewHodler();

			viewHodler.gift = (ImageView) convertView.findViewById(R.id.reward_gift_item_gift_iv);
			viewHodler.marker = (ImageView) convertView.findViewById(R.id.reward_gift_item_seletor_iv);
			viewHodler.glodTv = (TextView) convertView.findViewById(R.id.reward_gift_item_glod_tv);

			convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		SkillGiftInfo entity = mList.get(position);
		
		viewHodler.glodTv.setBackgroundDrawable(BaseUtils.createGradientDrawable(1, mContext.getResources().getColor(R.color.dialog_content), 180,
				android.R.color.transparent));
		//判断金币或者银币礼物
		if (entity.pricegold.intValue() > 0) {
			viewHodler.glodTv.setText(String.valueOf(entity.pricegold.intValue()) + mContext.getString(R.string.gold_money));
		} else {
			viewHodler.glodTv.setText(String.valueOf(entity.pricesilver.intValue()) + mContext.getString(R.string.silver_money));
		}
		//礼物图片
		String imgKey = new String(entity.pickey) + BAConstants.LOAD_180_APPENDSTR;
		imageLoader.displayImage("http://" + imgKey, viewHodler.gift, option);

		if (clickPosition != -1 && clickPosition == position) {
			viewHodler.marker.setVisibility(View.VISIBLE);
		} else {
			viewHodler.marker.setVisibility(View.GONE);
		}

		return convertView;
	}

	private class ViewHodler {
		private ImageView gift, marker;
		private TextView glodTv;
	}

	public void setClickPosition(int position) {
		this.clickPosition = position;
		notifyDataSetChanged();
	}

}
