package com.tshang.peipei.activity.mine;

import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.protocol.asn.gogirl.PhotoInfo;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: 相片列表界面对应adapter
 *
 * @Description: 匹配相应的相片数据
 *
 * @author allen
 *
 * @version V1.0   
 */
public class MineNetPhotosListAdapter extends ArrayListAdapter<PhotoInfo> {

	private int mItemWidth;

	private boolean isManage;
	private HashMap<String, PhotoInfo> photoMap = new HashMap<String, PhotoInfo>();
	private DisplayImageOptions options;

	public MineNetPhotosListAdapter(Activity context) {
		super(context);
		mItemWidth = (BasePhone.getScreenWidth(context) - BaseUtils.dip2px(mContext, 6)) / 3;
		options = ImageOptionsUtils.getImageKeyOptions(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHoler mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_photo_list_gvw, parent, false);

			mViewholer.ivw_photo = (ImageView) convertView.findViewById(R.id.photo_list_ivw);
			mViewholer.ivw_photo.setLayoutParams(new LayoutParams(mItemWidth, mItemWidth));
			mViewholer.ivw_check = (ImageView) convertView.findViewById(R.id.photo_list_ivw_check);

			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHoler) convertView.getTag();
		}

		PhotoInfo info = mList.get(position);
		if (info != null) {
			String key = new String(info.key) + BAConstants.LOAD_210_APPENDSTR;
			mViewholer.ivw_photo.setTag(key);
			imageLoader.displayImage("http://" + key, mViewholer.ivw_photo, options);
			if (isManage) {
				mViewholer.ivw_check.setVisibility(View.VISIBLE);
				if (null != photoMap.get(info.id.intValue() + "")) {
					mViewholer.ivw_check.setImageResource(R.drawable.album_img_choose_pr);
				} else {
					mViewholer.ivw_check.setImageResource(R.drawable.album_img_choose_un);
				}

			} else {
				mViewholer.ivw_check.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	public void removeItem(int followId) {
		for (int i = 0; i < mList.size(); i++) {
			PhotoInfo info = mList.get(i);
			if (followId == info.id.intValue()) {
				mList.remove(info);
				notifyDataSetChanged();
			}
		}

	}

	final class ViewHoler {
		private ImageView ivw_photo;
		private ImageView ivw_check;
	}

	public boolean isManage() {
		return isManage;
	}

	public void setManage(boolean isManage) {
		this.isManage = isManage;
	}

	public void freshAdapter() {
		isManage = true;
		notifyDataSetChanged();
	}

	public void freshAdpaterExitManage() {
		isManage = false;
		photoMap.clear();
		notifyDataSetChanged();
	}

	public void freshAdapterByChecked(HashMap<String, PhotoInfo> map) {
		this.photoMap = map;
		notifyDataSetChanged();
	}

}