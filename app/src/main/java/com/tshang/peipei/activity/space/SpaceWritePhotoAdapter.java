package com.tshang.peipei.activity.space;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.model.entity.PhotoEntity;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * @Title: SpaceWritePhotoAdapter.java 
 *
 * @Description: 写贴中的图片adapter
 *
 * @author vactor
 *
 * @date 2014-4-10 下午3:14:00 
 *
 * @version V1.0   
 */
public class SpaceWritePhotoAdapter extends BaseAdapter {

	private final Activity mContext;
	private int mNumColumns = 0;
	private GridView.LayoutParams mImageViewLayoutParams;
	private List<PhotoEntity> mPhotoList;
	private int mItemWidth;

	protected DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public SpaceWritePhotoAdapter(Activity context, List<PhotoEntity> photoList) {
		mContext = context;
		int width = BasePhone.getScreenWidth(context);
		mItemWidth = (width - BaseUtils.dip2px(context, 72)) / 3;
		mImageViewLayoutParams = new GridView.LayoutParams(mItemWidth, mItemWidth);

		this.mPhotoList = photoList;
		options = new DisplayImageOptions.Builder().cacheOnDisk(false).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		return mPhotoList.size();

	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position < mNumColumns ? 0 : position - mNumColumns;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return (position < mNumColumns) ? 1 : 0;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		if (position < mNumColumns) {
			if (convertView == null) {
				convertView = new View(mContext);
			}
			return convertView;
		}
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(mContext);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setLayoutParams(mImageViewLayoutParams);
		} else {
			imageView = (ImageView) convertView;
		}

		PhotoEntity photo = mPhotoList.get(position);
		if (!TextUtils.isEmpty(photo.getPath()))
			imageLoader.displayImage("file://" + photo.getPath(), imageView);
		return imageView;
	}

	public void setNumColumns(int numColumns) {
		mNumColumns = numColumns;
	}

	public int getNumColumns() {
		return mNumColumns;
	}

}
