package com.tshang.peipei.activity.space.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.mine.MineNetPhotosListActivity;
import com.tshang.peipei.activity.space.SpaceNetPhotosListActivity;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.PhotoInfo;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/*
 *个人主页展示个人相册小图片
 */
public class SpaceCustomHzListViewAdapter extends ArrayListAdapter<PhotoInfo> {

	private RelativeLayout.LayoutParams mParams = null;
	private RelativeLayout.LayoutParams mImgParams = null;
	private int whosUid = -1;
	private boolean isHost = false;
	private DisplayImageOptions options;

	public SpaceCustomHzListViewAdapter(Activity context, int whosUid) {
		super(context);
		this.whosUid = whosUid;
		int mItemWidth = (BasePhone.getScreenWidth(context) - BaseUtils.dip2px(context, 15)) / 4;
		mParams = new RelativeLayout.LayoutParams(BasePhone.getScreenWidth(context) / 4, mItemWidth);
		mImgParams = new RelativeLayout.LayoutParams(BasePhone.getScreenWidth(context) / 4, mItemWidth);
		mParams.leftMargin = BaseUtils.dip2px(mContext, 3);
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(context);
		if (userEntity != null && userEntity.uid.intValue() == whosUid) {//说明是主人的身份
			isHost = true;
		}
		options = ImageOptionsUtils.getImageKeyOptions(context);
	}

	@Override
	public int getCount() {
		if (!isHost) {//非主人态就不需要有一个加号
			if (mList != null)
				return mList.size();
			return 0;
		}
		if (mList == null) {
			return 1;
		}
		if (mList.size() >= 4) {
			return 4;
		}
		return mList.size() + 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_custom_hzlistview, null);
			holder.rcImageView = (ImageView) convertView.findViewById(R.id.custom_iv);
			holder.rcImageView.setLayoutParams(mImgParams);
			holder.button = (Button) convertView.findViewById(R.id.btn_uploadphoto);
			holder.button.setLayoutParams(mParams);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if ((mList == null || mList.size() == 0) && isHost) {
			holder.button.setVisibility(View.VISIBLE);
			holder.rcImageView.setVisibility(View.GONE);
		}
		if (mList != null && position < mList.size()) {
			holder.button.setVisibility(View.GONE);
			holder.rcImageView.setVisibility(View.VISIBLE);

			final PhotoInfo photo = mList.get(position);
			if (photo != null) {
				String photoKey = new String(photo.key);
				if (TextUtils.isEmpty(photoKey)) {
					holder.rcImageView.setImageResource(R.drawable.homepage_img_default);//设置没有图片时的默认图片
				} else {
					String key = photoKey + BAConstants.LOAD_210_APPENDSTR;
					holder.rcImageView.setTag(key);
					imageLoader.displayImage("http://" + key, holder.rcImageView, options);
				}
				holder.rcImageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (photo.id.intValue() < 0) {
							return;
						}
						Intent intent = new Intent(mContext, SpaceNetPhotosListActivity.class);
						intent.putExtra(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, whosUid);
						intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMID, 0);
						intent.putExtra(BAConstants.IntentType.PHOTOS_BACK, mContext.getResources().getString(R.string.private_page));
						mContext.startActivity(intent);
						mContext.overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
					}
				});

			}
		} else {
			if (isHost) {
				holder.button.setVisibility(View.VISIBLE);
				holder.rcImageView.setVisibility(View.GONE);
			}
		}
		if (isHost && position == 3) {
			holder.button.setVisibility(View.VISIBLE);
			holder.rcImageView.setVisibility(View.GONE);
		}

		holder.button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//				if (sex == Gender.FEMALE.getValue()) {
				//					Intent mineIntent = new Intent(mContext, MineAlbumActivity.class);
				//					mContext.startActivity(mineIntent);
				//					mContext.overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
				//				} else {
				Intent intent = new Intent(mContext, MineNetPhotosListActivity.class);
				//将相册ID传到上传界面
				intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMID, 0);
				String albumName = "相册";
				intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMNAME, albumName);
				intent.putExtra("viewpeopleuid", whosUid);
				intent.putExtra("viewpeoplesex", Gender.MALE.getValue());
				intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMCOUNT, 0);
				mContext.startActivity(intent);
				mContext.overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
				//				}
			}
		});
		return convertView;
	}

	final class ViewHolder {
		ImageView rcImageView;
		Button button;
	}

}
