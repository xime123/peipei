package com.tshang.peipei.activity.space.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfo;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * 
 * @author Jeff
 *
 */
public class SelectGiftDialogAdapter extends ArrayListAdapter<GiftInfo> {
	private DisplayImageOptions options;

	public SelectGiftDialogAdapter(Activity context) {
		super(context);
		options = ImageOptionsUtils.getImageKeyOptions(context);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_select_gift, parent, false);
			mViewholer.imageview = (ImageView) convertView.findViewById(R.id.iv_item_select_gift);
			mViewholer.tv_name = (TextView) convertView.findViewById(R.id.tv_item_select_gift_name);
			mViewholer.tv_describe = (TextView) convertView.findViewById(R.id.tv_item_select_gift_describe);

			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHoler) convertView.getTag();
		}
		GiftInfo giftInfo = mList.get(position);
		if (giftInfo != null) {
			String key = new String(giftInfo.pickey) + BAConstants.LOAD_180_APPENDSTR;
			mViewholer.imageview.setTag(key);
			imageLoader.displayImage("http://" + key, mViewholer.imageview, options);
			mViewholer.tv_name.setText(new String(giftInfo.name));
			mViewholer.tv_describe.setText("(" + mContext.getString(R.string.glamour) + "+" + giftInfo.loyaltyeffect.intValue() + "　"
					+ mContext.getString(R.string.integral) + "+" + giftInfo.scoreeffect + ")");
		}

		return convertView;
	}

	final class ViewHoler {
		ImageView imageview;
		TextView tv_name;
		TextView tv_describe;

	}

}
