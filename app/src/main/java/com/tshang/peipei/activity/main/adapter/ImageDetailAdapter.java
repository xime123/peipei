package com.tshang.peipei.activity.main.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tshang.peipei.R;
import com.tshang.peipei.vender.common.util.ListUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;
import com.tshang.peipei.vender.imageloader.core.assist.FailReason;
import com.tshang.peipei.vender.imageloader.core.display.FadeInBitmapDisplayer;
import com.tshang.peipei.vender.imageloader.core.listener.ImageLoadingListener;
import com.tshang.peipei.vender.imageloader.utils.StorageUtils;
import com.tshang.peipei.view.ZoomImageView;
import com.tshang.peipei.view.ZoomImageView.OnPhotoTapListener;

/**
 * ImagePagerAdapter
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class ImageDetailAdapter extends RecyclingPagerAdapter {
	private Activity mContext;
	private List<String> mList = new ArrayList<String>();
	private DisplayImageOptions options;
	private DisplayImageOptions options_wifi;

	/** 觸摸ImageView是否finish Activity */
	//	private boolean isClose;
	/** 觸屏的第一點坐標 */
	//	private PointF startPoint = new PointF();

	public void setList(List<String> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	public ImageDetailAdapter(Activity context) {
		this.mContext = context;
		options = new DisplayImageOptions.Builder().cacheInMemory(true).showImageOnFail(R.drawable.main_img_defaultpic_small).cacheOnDisk(false)
				.considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(600)).build();
		options_wifi = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.main_img_defaultpic_small).cacheOnDisk(true).cacheInMemory(true)
				.considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(600)).build();
	}

	@Override
	public int getCount() {
		return ListUtils.getSize(mList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;
		PhotoTapListener listener;
		if (row == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			row = inflater.inflate(R.layout.image_detail_fragment, null);
			holder = new ViewHolder();
			holder.headerImage = (ZoomImageView) row.findViewById(R.id.imageView);
			holder.progressBar = (ProgressBar) row.findViewById(R.id.progress_loading_img_pb);
			listener = new PhotoTapListener();
			holder.headerImage.setOnPhotoTapListener(listener);
			row.setTag(holder);
			row.setTag(holder.headerImage.getId(), listener);//对监听对象保存  
		} else {
			holder = (ViewHolder) row.getTag();
			listener = (PhotoTapListener) row.getTag(holder.headerImage.getId());
		}
		String path = mList.get(position);
		String key = path + "@false@640@-1";
		File f = StorageUtils.getOwnCacheDirectory(mContext, "");
		String filePath = "";
		if (f != null) {
			filePath = f.getAbsolutePath();
		}

		if (!TextUtils.isEmpty(filePath) && path.contains(filePath)) {
			ImageLoader.getInstance().displayImage("file://" + path, holder.headerImage, options, new LoadingListener(holder.progressBar));
		} else {
			ImageLoader.getInstance().displayImage("http://" + key, holder.headerImage, options_wifi, new LoadingListener(holder.progressBar));
		}
		//		holder.headerImage.setOnPhotoTapListener(new PhotoTapListener());
		return row;
	}

	private class LoadingListener implements ImageLoadingListener {
		private ProgressBar progressBar;

		public LoadingListener(ProgressBar progressBar) {
			this.progressBar = progressBar;
		}

		@Override
		public void onLoadingStarted(String imageUri, View view) {}

		@Override
		public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
			progressBar.setVisibility(View.GONE);

		}

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			progressBar.setVisibility(View.GONE);

		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			progressBar.setVisibility(View.GONE);

		}

	}

	private class PhotoTapListener implements OnPhotoTapListener {
		@Override
		public void onPhotoTap(View view, float x, float y) {
			mContext.finish();
		}
	}

	final class ViewHolder {
		ZoomImageView headerImage;
		ProgressBar progressBar;

	}
}
