package com.tshang.peipei.activity.reward.adapter;

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
import com.tshang.peipei.protocol.asn.gogirl.AwardGiftInfo;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: RewardGiftGridViewAdapter.java 
 *
 * @Description: 发布悬赏礼物适配器 
 *
 * @author Aaron  
 *
 * @date 2015-9-28 上午11:38:49 
 *
 * @version V1.0   
 */
public class RewardGiftGridViewAdapter extends ArrayListAdapter<AwardGiftInfo> {

	private int clickPosition = -1;

	private DisplayImageOptions option;

	public RewardGiftGridViewAdapter(Activity context) {
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
		AwardGiftInfo entity = mList.get(position);
		
		viewHodler.glodTv.setBackgroundDrawable(BaseUtils.createGradientDrawable(1, mContext.getResources().getColor(R.color.dialog_content), 180,
				android.R.color.transparent));
		//判断金币或者银币礼物
		if (entity.pricegold.intValue() > 0) {
			viewHodler.glodTv.setText(String.valueOf(entity.pricegold.intValue()) + "金币");
		} else {
			viewHodler.glodTv.setText(String.valueOf(entity.pricesilver.intValue()) + "银币");
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
