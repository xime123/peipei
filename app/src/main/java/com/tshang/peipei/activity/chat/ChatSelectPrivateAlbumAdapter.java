package com.tshang.peipei.activity.chat;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.protocol.asn.gogirl.AlbumInfo;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * @Title: 相册列表界面对应的adapter
 *
 * @Description: 匹配相册列表相应的数据
 *
 * @author allen
 *
 * @version V1.0   
 */
public class ChatSelectPrivateAlbumAdapter extends BaseAdapter {

	private Activity mContext;
	private List<AlbumInfo> mPhotoList;

	private ImageLoader imageLoader;

	private LinearLayout.LayoutParams linParams;
	private int mCurrSelect = -1;
	private DisplayImageOptions options;

	public ChatSelectPrivateAlbumAdapter(Activity context, List<AlbumInfo> list) {
		mContext = context;
		mPhotoList = list;
		imageLoader = ImageLoader.getInstance();
		int width = (BasePhone.getScreenWidth(context) - BaseUtils.dip2px(context, 36)) / 3;
		linParams = new LinearLayout.LayoutParams(width, width);
		options = ImageOptionsUtils.getImageKeyOptions(context);
	}

	@Override
	public int getCount() {
		return mPhotoList.size();
	}

	@Override
	public Object getItem(int position) {
		return mPhotoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_photo_album_gvw, parent, false);
			mViewholer.ivw_photo = (ImageView) convertView.findViewById(R.id.photo_ivw);
			mViewholer.ivw_check = (ImageView) convertView.findViewById(R.id.item_sdc_photo_grid_ivuncheck);
			mViewholer.tvAlbumName = (TextView) convertView.findViewById(R.id.item_photo_abum_tv_albumname);
			mViewholer.tvLoyaltiy = (TextView) convertView.findViewById(R.id.item_photo_album_tv_loyaltiy);
			mViewholer.tvUpdateTime = (TextView) convertView.findViewById(R.id.item_photo_album_tv_updatetime);
			mViewholer.rlPhoto = (RelativeLayout) convertView.findViewById(R.id.item_photo_rl);
			mViewholer.ivw_secret = (ImageView) convertView.findViewById(R.id.photo_iv_secret);
			mViewholer.rlPhoto.setLayoutParams(linParams);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHoler) convertView.getTag();
		}

		AlbumInfo album = mPhotoList.get(position);
		mViewholer.tvAlbumName.setText(new String(album.albumname));

		mViewholer.tvUpdateTime.setText(BaseTimes.getFormatTime((album.createtime.longValue()) * 1000));
		//公开相册
		if (album.accessloyalty.intValue() > 0) {
			mViewholer.tvLoyaltiy.setText(album.accessloyalty.intValue() + "");
			//私密相册
		} else {
			mViewholer.ivw_secret.setVisibility(View.GONE);
			mViewholer.tvLoyaltiy.setText("所有人可看");
		}

		if (null != album.coverpickey) {
			String key = new String(album.coverpickey) + BAConstants.LOAD_210_APPENDSTR;
			mViewholer.ivw_photo.setTag(key);
			imageLoader.displayImage("http://" + key, mViewholer.ivw_photo, options);
		}
		mViewholer.ivw_check.setVisibility(View.VISIBLE);
		if (position == mCurrSelect) {
			mViewholer.ivw_check.setBackgroundResource(R.drawable.album_img_choose_pr);
		} else {
			mViewholer.ivw_check.setBackgroundResource(R.drawable.album_img_choose_un);
		}
		return convertView;
	}

	//打勾图片置为选中状态
	public void freshAdpaterByChecked(int postion) {
		mCurrSelect = postion;
		notifyDataSetChanged();
	}

	final class ViewHoler {
		ImageView ivw_photo;
		ImageView ivw_check;
		ImageView ivw_secret;
		TextView tvAlbumName;
		TextView tvLoyaltiy;
		TextView tvUpdateTime;
		RelativeLayout rlPhoto;

	}

}
