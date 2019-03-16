/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.tshang.peipei.activity.main.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.main.adapter.bean.AdvBean;
import com.tshang.peipei.activity.mine.MineFaqActivity;
import com.tshang.peipei.base.BaseTools;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.common.util.ListUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;
import com.tshang.peipei.vender.imageloader.core.assist.ImageScaleType;
import com.tshang.peipei.vender.imageloader.core.display.RoundedBitmapDisplayer;

public class TopAdvAdapter extends RecyclingPagerAdapter {

	private Activity mContext;
	private List<AdvBean> mList = new ArrayList<AdvBean>();
	private boolean isInfiniteLoop;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;

	public void setList(List<AdvBean> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	public void setList(AdvBean[] list) {
		if (list != null && list.length != 0) {
			List<AdvBean> arrayList = new ArrayList<AdvBean>(list.length);
			for (AdvBean t : list) {
				arrayList.add(t);
			}
			setList(arrayList);
		}
	}

	private int mChildCount = 0;

	@Override
	public void notifyDataSetChanged() {//解决刷新问题 
		mChildCount = mList.size();
		super.notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object) {
		if (mChildCount > 0) {
			mChildCount--;
			return POSITION_NONE;
		}
		return super.getItemPosition(object);
	}

	public List<AdvBean> getmList() {
		return mList;
	}

	public void setmList(List<AdvBean> mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}

	public TopAdvAdapter(Activity context) {
		this.mContext = context;
		isInfiniteLoop = true;
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.makefriend_topadertise_bg).cacheOnDisk(true).cacheInMemory(true)
				.considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY).displayer(new RoundedBitmapDisplayer(BaseUtils.dip2px(context, 5)))
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		int size = ListUtils.getSize(mList);
		if (size == 0) {
			return 0;
		} else if (size == 1) {
			return 1;
		}
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : size;
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		int size = ListUtils.getSize(mList);
		if (size == 0) {
			return 0;
		}
		return isInfiniteLoop ? position % size : position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;
		if (row == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			row = inflater.inflate(R.layout.head_webview_hall_adv, null);
			holder = new ViewHolder();
			holder.webView = (ImageView) row.findViewById(R.id.hall_title_adv);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		final AdvBean advBean = mList.get(getPosition(position));
		if (advBean != null) {
			holder.webView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putInt(MineFaqActivity.WHERE_FROM, MineFaqActivity.MAIN_HALL_VALUE);
					String verifystr = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "")
							.getStringValueByKey(SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL);
					bundle.putString("url", advBean.getUrl() + "&u=" + verifystr + "&p=android&v=" + BaseTools.getAppVersionName(mContext));
					bundle.putString("title", advBean.getName());
					BaseUtils.openActivity(mContext, MineFaqActivity.class, bundle);

				}
			});
			imageLoader.displayImage("php_img://" + advBean.getImgUrl(), holder.webView, options);
		}
		return row;
	}

	final class ViewHolder {
		ImageView webView;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop the isInfiniteLoop to set
	 */
	public TopAdvAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}

}
