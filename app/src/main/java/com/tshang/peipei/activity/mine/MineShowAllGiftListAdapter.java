package com.tshang.peipei.activity.mine;

import java.math.BigInteger;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfo;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: ShowAllGiftAdapter.java 
 *
 * @Description: 收到礼物界面适配文件
 *
 * @author allen
 *
 * @date 2014-4-12 下午2:21:16 
 *
 * @version V1.0   
 */
public class MineShowAllGiftListAdapter extends ArrayListAdapter<GiftDealInfo> {

	private DisplayImageOptions options;
	private final int GG_GIFTDEAL_STATUS_FEEDBACK = 1 << 0; // 回赠标志

	private LinearLayout.LayoutParams linParams;
	private boolean mIsMySelf = false;
	private IFeedKiss feedKiss;
	private int sex;

	public MineShowAllGiftListAdapter(Activity context, int sex) {
		super(context);
		this.sex = sex;
		int width = (BasePhone.getScreenWidth(context) - BaseUtils.dip2px(context, 36)) / 3;
		linParams = new LinearLayout.LayoutParams(width, width);
		options = ImageOptionsUtils.getImageKeyOptions(context);
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHoler viewholer;
		if (convertView == null) {
			viewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_show_gift, parent, false);
			viewholer.giftView = (ImageView) convertView.findViewById(R.id.show_gift_item_image);
			viewholer.giftfeed = (TextView) convertView.findViewById(R.id.show_gift_item_name);
			viewholer.giftFrom = (TextView) convertView.findViewById(R.id.show_gift_item_from);
			viewholer.rlGift = (RelativeLayout) convertView.findViewById(R.id.show_gift_item_rl);
			viewholer.rlGift.setLayoutParams(linParams);
			viewholer.btnFeed = (Button) convertView.findViewById(R.id.show_gift_item_feed);
			viewholer.giftLayout = (LinearLayout) convertView.findViewById(R.id.show_gift_item_layout);
			viewholer.giftTime = (TextView) convertView.findViewById(R.id.show_gift_item_time);

			convertView.setTag(viewholer);
		} else {
			viewholer = (ViewHoler) convertView.getTag();
		}

		final GiftDealInfo giftinfo = mList.get(position);
		if (giftinfo != null) {
			viewholer.giftfeed.setText(new String(giftinfo.gift.name));
			if (sex == Gender.FEMALE.getValue()) {
				viewholer.giftFrom.setText(String.format(mContext.getString(R.string.gift_from), new String(giftinfo.fromnick)));
			} else {
				viewholer.giftFrom.setText(String.format(mContext.getString(R.string.gift_to), new String(giftinfo.tonick)));
			}
		}
		String key = new String(giftinfo.gift.pickey) + BAConstants.LOAD_180_APPENDSTR;
		viewholer.giftView.setTag(key);
		imageLoader.displayImage("http://" + key, viewholer.giftView, options);
		viewholer.giftLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (feedKiss != null) {
					feedKiss.onItem(giftinfo);
				}
			}
		});

		int status = giftinfo.giftdealstatus.intValue() & GG_GIFTDEAL_STATUS_FEEDBACK;
		if (mIsMySelf == true && sex == Gender.FEMALE.getValue()) {
			viewholer.btnFeed.setVisibility(View.VISIBLE);
			if (status != 0) {
				viewholer.btnFeed.setBackgroundDrawable(new BitmapDrawable());
				viewholer.btnFeed.setText(R.string.gift_feeded);
				viewholer.btnFeed.setTextColor(mContext.getResources().getColor(R.color.peach));
				viewholer.btnFeed.setOnClickListener(null);
			} else {
				viewholer.btnFeed.setBackgroundResource(R.drawable.message_btn_send_selector);
				viewholer.btnFeed.setText(R.string.feed_kiss);
				viewholer.btnFeed.setTextColor(mContext.getResources().getColor(R.color.white));
				viewholer.btnFeed.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (feedKiss != null) {
							feedKiss.feedKiss(giftinfo.from.intValue(), giftinfo.id.intValue(), position, v);
						}
					}
				});
			}
		} else {
			viewholer.btnFeed.setVisibility(View.GONE);
		}
		viewholer.giftTime.setText(BaseTimes.getTime1(giftinfo.createtime.longValue() * 1000));

		return convertView;
	}

	//同一礼物一起刷新
	public void freshAdapterByFeed(int position) {
		GiftDealInfo giftinfo = mList.get(position);
		giftinfo.giftdealstatus = BigInteger.valueOf(giftinfo.giftdealstatus.intValue() | GG_GIFTDEAL_STATUS_FEEDBACK);

		mList.add(position, giftinfo);
		mList.remove(position + 1);
		notifyDataSetChanged();
	}

	public void setMIsMySelf(boolean b) {
		mIsMySelf = b;
	}

	final class ViewHoler {
		ImageView giftView;
		TextView giftfeed;
		TextView giftFrom;
		RelativeLayout rlGift;
		Button btnFeed;
		LinearLayout giftLayout;
		TextView giftTime;
	}

	public void setFeedListener(IFeedKiss feed) {
		feedKiss = feed;
	}

	public interface IFeedKiss {
		public void feedKiss(int fuid, int dealId, int postion, View v);

		public void onItem(GiftDealInfo giftInfo);
	}
}
