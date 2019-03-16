package com.tshang.peipei.activity.main.message.adapter;

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
public class SelectSilverGiftDialogAdapter extends ArrayListAdapter<GiftInfo> {
	private DisplayImageOptions options;

	public SelectSilverGiftDialogAdapter(Activity context) {
		super(context);
		options = ImageOptionsUtils.getImageKeyOptions(context);

	}

	private int selectPos = -1;

	public int getSelectPos() {
		return selectPos;
	}

	public void setSelectPos(int selectPos) {
		this.selectPos = selectPos;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_select_silvergift, parent, false);
			mViewholer.imageview = (ImageView) convertView.findViewById(R.id.iv_item_select_gift);
			mViewholer.tv_name = (TextView) convertView.findViewById(R.id.tv_item_select_gift_name);
			mViewholer.tv_describe = (TextView) convertView.findViewById(R.id.tv_item_select_gift_describe);
			mViewholer.ivSelect = (ImageView) convertView.findViewById(R.id.iv_select_gift);

			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHoler) convertView.getTag();
		}
		GiftInfo giftInfo = mList.get(position);
		if (giftInfo != null) {
			if (giftInfo.id.intValue() == -1) {//说明不需要礼物
				mViewholer.imageview.setImageResource(R.drawable.message_chatdoor_default);
				mViewholer.tv_name.setText("不需要礼物");
				mViewholer.tv_describe.setVisibility(View.GONE);
			} else {
				String key = new String(giftInfo.pickey) + BAConstants.LOAD_180_APPENDSTR;
				mViewholer.imageview.setTag(key);
				imageLoader.displayImage("http://" + key, mViewholer.imageview, options);
				mViewholer.tv_describe.setVisibility(View.VISIBLE);
				mViewholer.tv_name.setText(new String(giftInfo.name));
				mViewholer.tv_describe.setText(mContext.getString(R.string.glamour) + "+" + giftInfo.loyaltyeffect.intValue() + "　"
						+ mContext.getString(R.string.integral) + "+" + giftInfo.scoreeffect);
			}
		}
		if (selectPos == position) {
			mViewholer.ivSelect.setVisibility(View.VISIBLE);
		} else {
			mViewholer.ivSelect.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	final class ViewHoler {
		ImageView imageview;
		TextView tv_name;
		TextView tv_describe;
		ImageView ivSelect;

	}

}
