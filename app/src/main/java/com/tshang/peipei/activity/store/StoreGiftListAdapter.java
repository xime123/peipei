package com.tshang.peipei.activity.store;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfo;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: StoreGiftListAdapter.java 
 *
 * @Description: 礼物商场列表适配文件 
 *
 * @author allen  
 *
 * @date 2014-4-12 上午10:37:54 
 *
 * @version V1.0   
 */
public class StoreGiftListAdapter extends ArrayListAdapter<GiftInfo> {

	private int mItemWidth;
	private LinearLayout.LayoutParams linParams;
	private DisplayImageOptions options;

	public StoreGiftListAdapter(Activity context) {
		super(context);
		mItemWidth = BasePhone.getScreenWidth(context) / 3;
		int width = (BasePhone.getScreenWidth(context) - BaseUtils.dip2px(context, 36)) / 3;
		linParams = new LinearLayout.LayoutParams(width, width);
		options = ImageOptionsUtils.getImageKeyOptions(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler viewholer;
		if (convertView == null) {
			viewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_get_giftlist, parent, false);
			viewholer.giftname = (TextView) convertView.findViewById(R.id.gift_item_name);
			viewholer.giftView = (ImageView) convertView.findViewById(R.id.gift_item_image);
			viewholer.giftmoney = (TextView) convertView.findViewById(R.id.gift_item_money);
			viewholer.giftloyalty = (TextView) convertView.findViewById(R.id.gift_item_loyalty);
			viewholer.rlGift = (RelativeLayout) convertView.findViewById(R.id.gift_item_rl);
			viewholer.rlGift.setLayoutParams(linParams);

			convertView.setTag(viewholer);
		} else {
			viewholer = (ViewHoler) convertView.getTag();
		}

		GiftInfo giftInfo = mList.get(position);
		viewholer.giftname.setText(new String(giftInfo.name));

		if (giftInfo.pricegold.intValue() > 0) {
			viewholer.giftmoney.setBackgroundResource(R.drawable.gift_bg_yellow);
			viewholer.giftmoney.setText(String.format(mContext.getString(R.string.gold_money_add), giftInfo.pricegold.intValue()));
		} else {
			viewholer.giftmoney.setBackgroundResource(R.drawable.gift_bg_silver);
			viewholer.giftmoney.setText(String.format(mContext.getString(R.string.silver_money_add), giftInfo.pricesilver.intValue()));
		}
		viewholer.giftloyalty.setText(String.format(mContext.getString(R.string.add_loyalty), giftInfo.loyaltyeffect.intValue()));
		String key = new String(giftInfo.pickey);
		if (!TextUtils.isEmpty(key)) {
			viewholer.giftView.setTag(key + BAConstants.LOAD_180_APPENDSTR);
		}
		imageLoader.displayImage("http://" + key + BAConstants.LOAD_180_APPENDSTR, viewholer.giftView, options);
		LayoutParams lp = new LayoutParams(mItemWidth, mItemWidth);
		viewholer.giftView.setLayoutParams(lp);

		return convertView;
	}

	final class ViewHoler {
		TextView giftname;
		ImageView giftView;
		TextView giftmoney;
		TextView giftloyalty;
		RelativeLayout rlGift;
	}
}
