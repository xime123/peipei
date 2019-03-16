package com.tshang.peipei.activity.mine;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.model.entity.AlbumEntity;
import com.tshang.peipei.model.entity.PhotoEntity;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: 相片列表界面对应adapter
 *
 * @Description: 匹配相应的相片数据,以文件夹为画分
 *
 * @author allen
 *
 * @version V1.0   
 */
public class MineDirectorySdcPhotosListAdapter extends ArrayListAdapter<AlbumEntity> {
	private DisplayImageOptions options;

	public MineDirectorySdcPhotosListAdapter(Activity context) {
		super(context);
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.main_img_defaultpic_small)
				.showImageForEmptyUri(R.drawable.main_img_defaultpic_small).showImageOnFail(R.drawable.main_img_defaultpic_small).cacheInMemory(true)
				.cacheOnDisk(false).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sdc_photos_list, parent, false);

			mViewholer.ivPhoto = (ImageView) convertView.findViewById(R.id.rank_main_ivw_frist);
			mViewholer.txtName = (TextView) convertView.findViewById(R.id.item_sdc_photos_tv_name);
			mViewholer.txtCount = (TextView) convertView.findViewById(R.id.item_sdc_photos_tv_count);

			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHoler) convertView.getTag();
		}
		AlbumEntity album = mList.get(position);
		PhotoEntity photo = album.getList().get(0);
		//		Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(mContext.getContentResolver(), Long.valueOf(photo.getId()),
		//				Images.Thumbnails.MICRO_KIND, null);
		//		mViewholer.ivPhoto.setImageBitmap(bitmap);
		if (photo != null) {
			imageLoader.displayImage("file://" + photo.getPath(), mViewholer.ivPhoto, options, null);
		}

		mViewholer.txtName.setText(album.getAlbumname());
		mViewholer.txtCount.setText(album.getList().size() + "");
		return convertView;
	}

	final class ViewHoler {
		private ImageView ivPhoto;
		private TextView txtName;
		private TextView txtCount;
	}

}