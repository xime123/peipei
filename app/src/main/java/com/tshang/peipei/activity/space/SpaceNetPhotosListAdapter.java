package com.tshang.peipei.activity.space;

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
public class SpaceNetPhotosListAdapter extends ArrayListAdapter<PhotoInfo> {

	private int mItemWidth;

	private boolean mIsManage;
	private DisplayImageOptions options;

	public SpaceNetPhotosListAdapter(Activity context) {
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
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHoler) convertView.getTag();
		}

		PhotoInfo info = mList.get(position);
		if (info != null) {
			String key = new String(info.key) + BAConstants.LOAD_210_APPENDSTR;
			imageLoader.displayImage("http://" + key, mViewholer.ivw_photo, options);
		}

		return convertView;
	}

	final class ViewHoler {
		private ImageView ivw_photo;
	}

	public void setManage(boolean b) {
		mIsManage = b;
	}

	public boolean isManage() {
		return mIsManage;
	}

}