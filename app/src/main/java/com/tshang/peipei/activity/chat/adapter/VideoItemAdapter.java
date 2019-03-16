package com.tshang.peipei.activity.chat.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.model.biz.chat.Video;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.assist.ImageScaleType;

/**
 * @Title: 视频每项匹配
 *
 * @author Jeff
 *
 * @version V1.0   
 */
public class VideoItemAdapter extends ArrayListAdapter<Video> {

	private RelativeLayout.LayoutParams linParams;

	private DisplayImageOptions options;

	public VideoItemAdapter(Activity context) {
		super(context);
		int width = (BasePhone.getScreenWidth(context) - BaseUtils.dip2px(mContext, 36)) / 3;
		linParams = new RelativeLayout.LayoutParams(width, width);
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.message_icon_video_file_default).cacheOnDisk(true)
				.cacheInMemory(true).considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoler mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false);
			mViewholer.ivw_photo = (ImageView) convertView.findViewById(R.id.iv_vedio_thumb);
			mViewholer.tvLen = (TextView) convertView.findViewById(R.id.item_video_time_len);
			mViewholer.ivw_photo.setLayoutParams(linParams);

			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHoler) convertView.getTag();
		}

		Video video = mList.get(position);
		if (video != null) {
			mViewholer.tvLen.setText(video.getDuration() + "\"");
			String thumbpath = video.getThumbImg();
			if (!TextUtils.isEmpty(thumbpath)) {
				imageLoader.displayImage(thumbpath, mViewholer.ivw_photo, options);
			}
		}
		return convertView;
	}

	final class ViewHoler {
		ImageView ivw_photo;
		TextView tvLen;

	}

}
