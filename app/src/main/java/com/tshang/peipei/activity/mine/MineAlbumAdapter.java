package com.tshang.peipei.activity.mine;

import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.protocol.asn.gogirl.AlbumInfo;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: 相册列表界面对应的adapter
 *
 * @Description: 匹配相册列表相应的数据
 *
 * @author allen
 *
 * @version V1.0   
 */
public class MineAlbumAdapter extends ArrayListAdapter<AlbumInfo> {

	private boolean mIsManage;

	private LinearLayout.LayoutParams linParams;
	private HashMap<String, AlbumInfo> albumMap = new HashMap<String, AlbumInfo>();

	private DisplayImageOptions options;

	public MineAlbumAdapter(Activity context) {
		super(context);
		int width = (BasePhone.getScreenWidth(context) - BaseUtils.dip2px(mContext, 36)) / 3;
		linParams = new LinearLayout.LayoutParams(width, width);
		options = ImageOptionsUtils.getImageKeyOptions(context);
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

		AlbumInfo album = mList.get(position);
		if (album != null) {
			String name = new String(album.albumname);
			mViewholer.tvUpdateTime.setText(album.photototal.intValue() + "张/" + BaseTimes.getDiffTime(((album.createtime.longValue()) * 1000)));
			//公开相册
			if (album.accessloyalty.intValue() > 0) {
				mViewholer.tvLoyaltiy.setText("魅力贡献值达" + album.accessloyalty.intValue() + "可看");
				//私密相册
			} else {
				name = mContext.getString(R.string.photo_public);
				mViewholer.ivw_secret.setVisibility(View.GONE);
				mViewholer.tvLoyaltiy.setText("所有人可看");
			}
			mViewholer.tvAlbumName.setText(name);
			if (null != album.coverpickey) {
				String key = new String(album.coverpickey) + BAConstants.LOAD_210_APPENDSTR;
				imageLoader.displayImage("http://" + key, mViewholer.ivw_photo, options);
			} else {
				mViewholer.ivw_photo.setImageResource(R.drawable.main_img_defaultpic_small);
			}
			if (mIsManage) {
				if (album.accessloyalty.intValue() > 0) {
					mViewholer.ivw_check.setVisibility(View.VISIBLE);
				} else {
					mViewholer.ivw_check.setVisibility(View.GONE);
				}

				if (albumMap.get(album.id.intValue() + "") != null) {
					mViewholer.ivw_check.setBackgroundResource(R.drawable.album_img_choose_pr);
				} else {
					mViewholer.ivw_check.setBackgroundResource(R.drawable.album_img_choose_un);
				}

			} else {
				mViewholer.ivw_check.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	//显示打勾图片
	public void freshAdapter() {
		mIsManage = true;
		notifyDataSetChanged();
	}

	//隐藏打勾图片
	public void freshAdpaterExitManage() {
		mIsManage = false;
		notifyDataSetChanged();
	}

	//打勾图片置为选中状态
	public void freshAdpaterByChecked(HashMap<String, AlbumInfo> map) {
		this.albumMap = map;
		notifyDataSetChanged();
	}

	public boolean ismIsManage() {
		return mIsManage;
	}

	public void setManage(boolean b) {
		mIsManage = b;
	}

	public boolean isManage() {
		return mIsManage;
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
