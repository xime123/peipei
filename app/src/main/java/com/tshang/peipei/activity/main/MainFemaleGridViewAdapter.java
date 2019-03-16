package com.tshang.peipei.activity.main;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAParseRspData.ContentData;
import com.tshang.peipei.storage.database.entity.PublishDatabaseEntity;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/*
 *类        名 : MainMineFemaleGridViewAdapter.java
 *功能描述 : 嵌套在  listview 中的  一个gridview 中的 adapter
 *作　    者 : vactor
 *设计日期 : 2014-3-27 下午1:38:52
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class MainFemaleGridViewAdapter extends BaseAdapter {

	private Activity mContext;
	private Object mData;

	private int width = 245;
	private int giftWidth = 180;
	private int oneWidth;
	private FrameLayout.LayoutParams params;
	private FrameLayout.LayoutParams params2;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;

	BitmapFactory.Options newOpts = new BitmapFactory.Options();
	private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

	public MainFemaleGridViewAdapter(Activity context, Object data) {
		this.mData = data;
		this.mContext = context;

		int screenWidth = BasePhone.getScreenWidth(mContext);
		oneWidth = screenWidth - BaseUtils.dip2px(mContext,90);
		params = new FrameLayout.LayoutParams(oneWidth / 3, oneWidth / 3);
		params2 = new FrameLayout.LayoutParams(oneWidth / 2, oneWidth / 2);
		newOpts.inSampleSize = 3;
		options = ImageOptionsUtils.getImageKeyOptions(context);
	}

	@Override
	public int getCount() {
		int size = 0;
		//设置最多显示9张图片
		if (mData instanceof ContentData) {
			ContentData data = (ContentData) mData;
			size = data.getImageList().size() >= 9 ? 9 : data.getImageList().size();
		} else {
			PublishDatabaseEntity publishEntity = (PublishDatabaseEntity) mData;
			if (!TextUtils.isEmpty(publishEntity.getImageKeys())) {
				String[] images = publishEntity.getImageKeys().split(";");
				size = images.length >= 9 ? 9 : images.length;
			}
		}
		return size;
	}

	@Override
	public Object getItem(int position) {
		return mData;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
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

		//本地数据
		if (mData instanceof PublishDatabaseEntity) {
			PublishDatabaseEntity publishEntity = (PublishDatabaseEntity) mData;
			if (!TextUtils.isEmpty(publishEntity.getImageKeys())) {
				String[] images = publishEntity.getImageKeys().split(";");
				//最多只显示 9个ITEM,所以最后一个不下载
				if (position <= 8) {
					Bitmap bitmap;
					//加载SDC图片
					if (imageCache.containsKey(images[position])) {
						SoftReference<Bitmap> softReference = imageCache.get(images[position]);
						bitmap = softReference.get();
						if (null == bitmap) {
							bitmap = BitmapFactory.decodeFile(images[position], newOpts);
							imageCache.put(images[position], new SoftReference<Bitmap>(bitmap));
						}
					} else {
						bitmap = BitmapFactory.decodeFile(images[position], newOpts);
						imageCache.put(images[position], new SoftReference<Bitmap>(bitmap));
					}
					if (null != bitmap) {
						viewHolder.rcImageView.setImageBitmap(bitmap);
					}
					viewHolder.rcImageView.setLayoutParams(params);
					viewHolder.tvCount.setVisibility(View.GONE);
				} else {
					viewHolder.rcImageView.setImageBitmap(null);
					viewHolder.tvCount.setVisibility(View.VISIBLE);
					viewHolder.tvCount.setText("共" + images.length + "张");
				}
				//有两张图片或四张图片时
				int size = images.length;
				if (size == 2 || size == 4) {
					viewHolder.rcImageView.setLayoutParams(params2);
				} else {
					viewHolder.rcImageView.setLayoutParams(params);
				}
			}

		}
		//服务器数据
		if (mData instanceof ContentData) {
			//最多只显示 9个ITEM,所以最后一个不下载
			ContentData data = (ContentData) mData;
			if (position <= 8) {
				String imageKey = data.getImageList().get(position);
				if (data.getType() == BAConstants.MessageType.GIFT.getValue()) {

					String key = imageKey + "@false@" + giftWidth + "@" + giftWidth;
					viewHolder.rcImageView.setTag(key);
					imageLoader.displayImage("http://" + key, viewHolder.rcImageView, options);
				} else {

					String key = imageKey + "@false@" + width + "@" + width;
					viewHolder.rcImageView.setTag(key);
					imageLoader.displayImage("http://" + key, viewHolder.rcImageView, options);

				}

				viewHolder.rcImageView.setLayoutParams(params);
				viewHolder.tvCount.setVisibility(View.GONE);
			} else {
				viewHolder.rcImageView.setImageBitmap(null);
				viewHolder.tvCount.setVisibility(View.VISIBLE);
				viewHolder.tvCount.setText("共" + data.getImageList().size() + "张");
			}
			//有两张图片或四张图片时
			int size = data.getImageList().size();
			if (size == 2 || size == 4) {
				viewHolder.rcImageView.setLayoutParams(params2);
			} else {
				viewHolder.rcImageView.setLayoutParams(params);
			}
		}

		return convertView;
	}

	class ViewHolder {
		ImageView rcImageView;
		TextView tvCount;
	}

	public void freshData(Object data) {
		this.mData = data;
		notifyDataSetChanged();
	}

}
