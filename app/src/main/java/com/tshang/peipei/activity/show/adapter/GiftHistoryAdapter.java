package com.tshang.peipei.activity.show.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.protocol.Gogirl.GiftDealInfoP;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * @Title: GiftHistoryAdapter.java 
 *
 * @Description: 礼物动态
 *
 * @author allen  
 *
 * @date 2015-3-10 下午1:53:05 
 *
 * @version V1.0   
 */
public class GiftHistoryAdapter extends ArrayListAdapter<GiftDealInfoP> {

	protected DisplayImageOptions options_gift;
	protected ImageLoader imageLoader;

	private Activity activity;

	public GiftHistoryAdapter(Activity context) {
		super(context);
		activity = context;
		imageLoader = ImageLoader.getInstance();
		options_gift = ImageOptionsUtils.getImageKeyOptions(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_show_gift_history, parent, false);
			mViewholer.ivGift = (ImageView) convertView.findViewById(R.id.item_gift_history_iv);
			mViewholer.tvNick = (TextView) convertView.findViewById(R.id.item_gift_history_nick);
			mViewholer.tvTime = (TextView) convertView.findViewById(R.id.item_gift_history_time);
			mViewholer.tvNum = (TextView) convertView.findViewById(R.id.item_gift_history_num);
			mViewholer.giftBg = (LinearLayout) convertView.findViewById(R.id.item_gift_history_bg);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHolder) convertView.getTag();
		}

		GiftDealInfoP info = mList.get(position);
		mViewholer.tvNick.setText(new String(info.getFromnick().toByteArray()));

		mViewholer.tvTime.setText(BaseTimes.getChatDiffTimeHHMMSS((long) info.getCreatetime() * 1000));
		imageLoader.displayImage("http://" + new String(info.getGift().getPickey().toByteArray()) + BAConstants.LOAD_180_APPENDSTR,
				mViewholer.ivGift, options_gift);
		if (info.getGiftnum() > 1) {
			mViewholer.tvNum.setText("x" + info.getGiftnum());
			mViewholer.tvNum.setVisibility(View.VISIBLE);
		} else {
			mViewholer.tvNum.setVisibility(View.GONE);
		}
		if (position % 2 == 1) {
			mViewholer.giftBg.setBackgroundColor(activity.getResources().getColor(R.color.show_gift_bg_color2));
		} else {
			mViewholer.giftBg.setBackgroundColor(activity.getResources().getColor(R.color.show_gift_bg_color1));
		}

		return convertView;
	}

	private final class ViewHolder {
		TextView tvNick;
		TextView tvTime;
		ImageView ivGift;
		TextView tvNum;
		LinearLayout giftBg;
	}
}
