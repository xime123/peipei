package com.tshang.peipei.activity.mine.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;
import com.tshang.peipei.vender.imageloader.core.assist.FailReason;
import com.tshang.peipei.vender.imageloader.core.listener.ImageLoadingListener;

import de.greenrobot.event.EventBus;

/**
 * @Title: MineImageTextAdapter.java 
 *
 * @Description: 发表动态文字配图适配器
 *
 * @author Aaron  
 *
 * @date 2015-7-30 上午11:05:52 
 *
 * @version V1.0   
 */
public class MineImageTextAdapter extends PagerAdapter {

	@SuppressWarnings("unused")
	private final String TAG = this.getClass().getSimpleName();

	private Context mContext;

	private LayoutInflater inflater;

	private DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	private String[] images;

	private String content;
	private String topic_title;

	private boolean isTypefaceblank = false;

	public MineImageTextAdapter(Context context, Activity activity, String content, String topicTitle, String[] images) {
		this.mContext = context;
		inflater = LayoutInflater.from(context);
		options = ImageOptionsUtils.GetImgTextRounded(activity);
		this.content = content;
		this.images = images;
		this.topic_title = topicTitle;
	}

	@Override
	public float getPageWidth(int position) {
		return super.getPageWidth(position);
	}

	@Override
	public int getCount() {
		return images.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;//官方提示这样写 
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@SuppressLint("InflateParams")
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View view = inflater.inflate(R.layout.img_text_adapter_viewpager_layout, null);
		TextView textView = (TextView) view.findViewById(R.id.img_text_adapter_viewpager_item_textview);
		ImageView imageView = (ImageView) view.findViewById(R.id.img_text_adapter_viewpager_item_iv);
		TextView topicTV = (TextView) view.findViewById(R.id.img_text_adapter_viewpager_item_topic_title_tv);
		if (!TextUtils.isEmpty(topic_title)) {
			topicTV.setText(topic_title);
			topicTV.setVisibility(View.VISIBLE);
		}

		//网络:third:// 
		//http:// 自己服务器
		//file:// 本地
		imageLoader.displayImage("file://" + images[position], imageView, options, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {}
		});
		textView.setText(content);
		if (isTypefaceblank) {
			textView.setTextColor(mContext.getResources().getColor(R.color.black));
			textView.setShadowLayer(15, 5, 5, mContext.getResources().getColor(R.color.white));

			topicTV.setTextColor(mContext.getResources().getColor(R.color.black));
			topicTV.setShadowLayer(15, 5, 5, mContext.getResources().getColor(R.color.white));
		} else {
			textView.setTextColor(mContext.getResources().getColor(R.color.white));
			textView.setShadowLayer(15, 5, 5, mContext.getResources().getColor(R.color.black));

			topicTV.setTextColor(mContext.getResources().getColor(R.color.white));
			topicTV.setShadowLayer(15, 5, 5, mContext.getResources().getColor(R.color.black));
		}
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				NoticeEvent event = new NoticeEvent();
				event.setObj(images[position]);
				event.setFlag(NoticeEvent.NOTICE85);
				EventBus.getDefault().post(event);
			}
		});
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	public void setTextColor(boolean isBlank) {
		this.isTypefaceblank = isBlank;
		notifyDataSetChanged();
	}

}
