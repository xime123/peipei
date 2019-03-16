package com.tshang.peipei.activity.space.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.mine.MineShowAllGiftListActivity;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfo;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/*
 *个人主页展示个人礼物列表
 */
public class SpaceGiftAdapter extends ArrayListAdapter<GiftDealInfo> {

	private DisplayImageOptions options;
	private FrameLayout.LayoutParams mParams = null;
	private int sex;
	private int uid;

	public SpaceGiftAdapter(Activity context, int sex, int uid) {
		super(context);
		int mItemWidth = (BasePhone.getScreenWidth(context) - BaseUtils.dip2px(context, 15)) / 4;
		mParams = new FrameLayout.LayoutParams(mItemWidth, mItemWidth);
		options = ImageOptionsUtils.getImageKeyOptions(context);
		this.sex = sex;
		this.uid = uid;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		OnGiftClickListener listener = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_custom_space_gridview, null);
			holder.rcImageView = (ImageView) convertView.findViewById(R.id.item_space_grid_secreat);
			holder.rcImageView.setLayoutParams(mParams);
			listener = new OnGiftClickListener();
			holder.rcImageView.setOnClickListener(listener);
			convertView.setTag(holder);
			convertView.setTag(holder.rcImageView.getId(), listener);
		} else {
			holder = (ViewHolder) convertView.getTag();
			listener = (OnGiftClickListener) convertView.getTag(holder.rcImageView.getId());
		}
		GiftDealInfo info = mList.get(position);
		if (info != null) {
			imageLoader.displayImage("http://" + new String(info.gift.pickey) + BAConstants.LOAD_180_APPENDSTR, holder.rcImageView, options);
		}

		return convertView;
	}

	final class ViewHolder {
		ImageView rcImageView;
	}

	private class OnGiftClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Bundle bundle = new Bundle();
			bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, uid);
			bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, sex);
			BaseUtils.openActivity(mContext, MineShowAllGiftListActivity.class, bundle);
		}

	}

}
