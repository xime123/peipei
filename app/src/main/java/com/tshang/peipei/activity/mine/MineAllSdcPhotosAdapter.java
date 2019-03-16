package com.tshang.peipei.activity.mine;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.model.entity.PhotoEntity;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: 相片列表界面对应adapter
 *
 * @Description: 匹配相应的相片数据
 *
 * @author vactor
 *
 * @version V1.0   
 */
@SuppressLint("UseSparseArrays")
public class MineAllSdcPhotosAdapter extends ArrayListAdapter<PhotoEntity> {

	private HashMap<Integer, PhotoEntity> checkedMap = new HashMap<Integer, PhotoEntity>();

	private int mItemWidth;
	private RelativeLayout.LayoutParams params;
	private DisplayImageOptions options;

	public MineAllSdcPhotosAdapter(Activity context) {
		super(context);
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.main_img_defaultpic_small)
				.showImageForEmptyUri(R.drawable.main_img_defaultpic_small).showImageOnFail(R.drawable.main_img_defaultpic_small).cacheInMemory(true)
				.cacheOnDisk(false).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		mItemWidth = (BasePhone.getScreenWidth(context) - BaseUtils.dip2px(context, 3) * 2) / 3;
		params = new RelativeLayout.LayoutParams(mItemWidth, mItemWidth);

	}

	public HashMap<Integer, PhotoEntity> getCheckedMap() {
		return checkedMap;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHoler mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sdc_photos_grid, parent, false);
			mViewholer.iv_photo = (ImageView) convertView.findViewById(R.id.item_sdc_photo_grid_iv);
			mViewholer.iv_check = (ImageView) convertView.findViewById(R.id.item_sdc_photo_grid_ivcheck);
			mViewholer.iv_photo.setLayoutParams(params);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHoler) convertView.getTag();
		}
		PhotoEntity photo = mList.get(position);
		if (photo != null) {
			imageLoader.displayImage("file://" + photo.getPath(), mViewholer.iv_photo, options, null);
		}
		//选中与否
		PhotoEntity photoEntity = checkedMap.get(position);
		if (null != photoEntity) {
			mViewholer.iv_check.setVisibility(View.VISIBLE);
		} else {
			mViewholer.iv_check.setVisibility(View.GONE);
		}
		return convertView;
	}

	final class ViewHoler {
		private ImageView iv_photo;
		private ImageView iv_check;
	}

}