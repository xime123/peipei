package com.tshang.peipei.activity.show.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfo;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: ShowGiftAdapter.java 
 *
 * @Description: 秀场礼物
 *
 * @author allen  
 *
 * @date 2015-1-28 下午7:59:27 
 *
 * @version V1.0   
 */
public class ShowGiftAdapter extends ArrayListAdapter<GiftInfo> {

	private int mItemWidth;
	private LinearLayout.LayoutParams linParams;
	private DisplayImageOptions options;
	private int curpostion;

	public void setCurpostion(int curpostion) {
		this.curpostion = curpostion;
		notifyDataSetChanged();
	}

	public ShowGiftAdapter(Activity context) {
		super(context);

		mItemWidth = BasePhone.getScreenWidth(context) / 5;
		int width = (BasePhone.getScreenWidth(context) - BaseUtils.dip2px(context, 120)) / 5;
		linParams = new LinearLayout.LayoutParams(width, width);
		options = ImageOptionsUtils.getImageKeyOptions(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gift_show, parent, false);
			mViewholer.ivGift = (ImageView) convertView.findViewById(R.id.item_show_gift_iv);
			mViewholer.tvName = (TextView) convertView.findViewById(R.id.item_show_gift_name);
			mViewholer.tvPrice = (TextView) convertView.findViewById(R.id.item_show_gift_price);
			mViewholer.tvRanknum = (TextView) convertView.findViewById(R.id.item_show_gift_tv);
			mViewholer.ivGift.setLayoutParams(linParams);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHolder) convertView.getTag();
		}

		GiftInfo giftInfo = mList.get(position);

		String key = new String(giftInfo.pickey);
		if (!TextUtils.isEmpty(key)) {
			mViewholer.ivGift.setTag(key + BAConstants.LOAD_180_APPENDSTR);
		}
		imageLoader.displayImage("http://" + key + BAConstants.LOAD_180_APPENDSTR, mViewholer.ivGift, options);

		mViewholer.tvName.setText(new String(giftInfo.name));
		if (giftInfo.pricesilver.intValue() > 0) {
			mViewholer.tvPrice.setText(giftInfo.pricesilver.intValue() + mContext.getResources().getString(R.string.silver_money));
			mViewholer.tvPrice.setTextColor(mContext.getResources().getColor(R.color.gray1));
		} else if (giftInfo.pricegold.intValue() > 0) {
			mViewholer.tvPrice.setText(giftInfo.pricegold.intValue() + mContext.getResources().getString(R.string.gold_money));
			mViewholer.tvPrice.setTextColor(mContext.getResources().getColor(R.color.orange));
		}

		mViewholer.tvRanknum.setText(mContext.getResources().getString(R.string.glamour) + "+" + giftInfo.charmeffect.intValue());

		if (curpostion == position) {
			convertView.setBackgroundResource(R.drawable.gift_img_kuang);
		} else {
			convertView.setBackgroundResource(R.drawable.gift_img_kuang_h);
		}
		return convertView;
	}

	private final class ViewHolder {
		ImageView ivGift;
		TextView tvName;
		TextView tvPrice;
		TextView tvRanknum;
	}
}
