package com.tshang.peipei.activity.space;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ImageDetailActivity;
import com.tshang.peipei.activity.mine.MineShowAllGiftListActivity;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAParseRspData.ContentData;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/*
 *类        名 : SpaceCustomGridViewAdapter.java
 *功能描述 : 嵌套在  listview 中的  一个gridview 中的 adapter
 *作　    者 : vactor
 *设计日期 : 2014-3-27 下午1:38:52
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class SpaceCustomGridViewAdapter extends BaseAdapter {

	private Activity mContext;
	private int mFriendUid;
	private ContentData mData;
	private int mSex;

	private int width = 245;
	private int giftWidth = 180;
	private int oneWidth;
	private FrameLayout.LayoutParams params3;
	private FrameLayout.LayoutParams params2;
	protected DisplayImageOptions options;
	private DisplayImageOptions options_wifi;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public SpaceCustomGridViewAdapter(Activity context, ContentData data, int mFriendUid, int sex) {
		this.mContext = context;
		this.mData = data;
		this.mFriendUid = mFriendUid;
		this.mSex = sex;
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.main_img_defaultpic_small)
				.showImageForEmptyUri(R.drawable.main_img_defaultpic_small).showImageOnFail(R.drawable.main_img_defaultpic_small).cacheInMemory(true)
				.cacheOnDisk(false).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		int screenWidth = BasePhone.getScreenWidth(mContext);
		oneWidth = (screenWidth - BaseUtils.dip2px(mContext, 90));
		params3 = new FrameLayout.LayoutParams(oneWidth / 3, oneWidth / 3);
		params2 = new FrameLayout.LayoutParams(oneWidth / 2, oneWidth / 2);
		options_wifi = ImageOptionsUtils.getImageKeyOptions(context);
	}

	@Override
	public int getCount() {
		int size = 0;
		if (mData.getType() == BAConstants.MessageType.GIFT.getValue()) {
			//设置最多显示4张图片
			size = mData.getImageList().size() >= 4 ? 4 : mData.getImageList().size();
		} else {
			//设置最多显示4张图片
			size = mData.getImageList().size() >= 9 ? 9 : mData.getImageList().size();
		}

		return size;
	}

	@Override
	public Object getItem(int position) {
		return mData.getImageList().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_custom_space_gridview, null);
			viewHolder.rcImageView = (ImageView) convertView.findViewById(R.id.item_space_grid_secreat);
			viewHolder.tvCount = (TextView) convertView.findViewById(R.id.item_space_grid_tv_count);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		//最多只显示 9个ITEM,所以最后一个不下载
		if (position <= 8) {
			final ArrayList<String> lists = new ArrayList<String>();
			ArrayList<String> list = mData.getImageList();
			if (mData.getType() == BAConstants.MessageType.GIFT.getValue()) {
				if (list != null && !list.isEmpty())

					for (int len = list.size() - 1; len >= 0; len--) {
						lists.add(list.get(len));
					}
			} else {
				lists.addAll(list);
			}
			String imageKey = lists.get(position);
			if (mData.getType() == BAConstants.MessageType.GIFT.getValue()) {
				viewHolder.rcImageView.setTag(imageKey + "@false@" + giftWidth + "@" + giftWidth);
				imageLoader.displayImage("http://" + imageKey + "@false@" + giftWidth + "@" + giftWidth, viewHolder.rcImageView, options_wifi);
			} else {
				viewHolder.rcImageView.setTag(imageKey + "@false@" + width + "@" + width);
				imageLoader.displayImage("http://" + imageKey + "@false@" + width + "@" + width, viewHolder.rcImageView, options_wifi);
			}

			if (imageKey.contains("/sdcard") || imageKey.contains("/mnt")) {
				imageLoader.displayImage("file://" + imageKey, viewHolder.rcImageView, options, null);
			}
			viewHolder.rcImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putInt(ImageDetailActivity.POSITION, position);
					bundle.putStringArrayList(ImageDetailActivity.EXTRA_IMAGE, lists);
					if (mData.getType() == BAConstants.MessageType.GIFT.getValue()) {
						bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, mFriendUid);
						bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, mSex);
						BaseUtils.openActivity(mContext, MineShowAllGiftListActivity.class, bundle);
					} else {
						BaseUtils.openActivity(mContext, ImageDetailActivity.class, bundle);
					}

				}
			});
			viewHolder.tvCount.setVisibility(View.GONE);
		} else {
			viewHolder.rcImageView.setImageBitmap(null);
			viewHolder.tvCount.setVisibility(View.VISIBLE);
			viewHolder.tvCount.setText("共" + mData.getImageList().size() + "张");
		}

		//有两张图片或四张图片时
		int size = mData.getImageList().size();
		if (size == 2 || size == 4) {
			viewHolder.rcImageView.setLayoutParams(params2);
		} else {
			viewHolder.rcImageView.setLayoutParams(params3);
		}

		return convertView;
	}

	class ViewHolder {
		ImageView rcImageView;
		TextView tvCount;
	}

	public void freshData(ContentData data) {
		this.mData = data;
		notifyDataSetChanged();
	}

}
