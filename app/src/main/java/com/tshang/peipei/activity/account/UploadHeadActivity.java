package com.tshang.peipei.activity.account;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.PhotoSetDialog;
import com.tshang.peipei.activity.mine.ClipViewActivity;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.BaseBitmap;
import com.tshang.peipei.base.BaseCameraGalleryPhoto;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.model.biz.user.UserAccountBiz;
import com.tshang.peipei.model.biz.user.UserSettingBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetUserInfo;
import com.tshang.peipei.model.bizcallback.BizCallBackUploadHeadPic;

/**
 * @Title: UploadHeadActivity.java 
 *
 * @Description: 注册上传头像
 *
 * @author allen  
 *
 * @date 2014-7-29 下午8:14:18 
 *
 * @version V1.0   
 */
public class UploadHeadActivity extends BaseActivity implements BizCallBackUploadHeadPic, BizCallBackGetUserInfo {

	public final String RESULT_BITMAP_DATA = "result_bitmap_data";

	private final int UPLOAD_HEAD = 1;
	private final int SET_HEAD = 2;
	private final int RESULT_CLIP_IMAGE = 0x10;//图片剪切返回

	public final int GET_PHOTO_BY_GALLERY = 1020;
	public final int GET_PHOTO_BY_CAMERA = 1010;

	private LinearLayout mCamera;
	private LinearLayout mPhoto;
	private Button mSumbit;
	private ImageView mImage;
	private byte[] resultBytes;
	private int mSex;

	@Override
	protected void initData() {}

	@Override
	protected void initRecourse() {
		mSex = getIntent().getExtras().getInt("sex");

		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setVisibility(View.GONE);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.setting_head);

		//		findViewById(R.id.upload_btn_pass).setOnClickListener(this);
		mImage = (ImageView) findViewById(R.id.upload_image);

		if (mSex == Gender.FEMALE.getValue()) {
			findViewById(R.id.upload_sex_text).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.upload_sex_text).setVisibility(View.GONE);
		}

		mCamera = (LinearLayout) findViewById(R.id.upload_head_camera);
		mPhoto = (LinearLayout) findViewById(R.id.upload_head_photo);
		mCamera.setOnClickListener(this);
		mPhoto.setOnClickListener(this);

		mSumbit = (Button) findViewById(R.id.upload_btn_submit);
		mSumbit.setOnClickListener(this);
	}

	@Override
	protected int initView() {
		return R.layout.activity_uploadhead;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case UPLOAD_HEAD:
			if (msg.arg1 == 0) {
				BaseUtils.showTost(this, R.string.str_upload_head_image_success);
				new UserAccountBiz(this).getUserInfo(this);
				setResult(InvitationActivity.INVITATE);
				SuccessFinish();
			} else {
				BaseUtils.showTost(this, R.string.str_update_error);
			}
			break;
		case SET_HEAD:
			mImage.setBackgroundDrawable(new BitmapDrawable(BaseBitmap.bytes2Bimap(resultBytes)));
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.upload_btn_pass:
			setResult(InvitationActivity.INVITATE);
			SuccessFinish();
			break;
		case R.id.upload_head_camera:
			BaseCameraGalleryPhoto.intentSelectImage(this, false, GET_PHOTO_BY_CAMERA);
			break;
		case R.id.upload_head_photo:
			BaseCameraGalleryPhoto.intentSelectImage(this, true, GET_PHOTO_BY_GALLERY);
			break;
		case R.id.upload_btn_submit:
			if (resultBytes != null) {
				uploadHeadPic(resultBytes);
			} else {
				BaseUtils.showTost(this, "你还没有上传头像");
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == RESULT_CLIP_IMAGE) {
				resultBytes = data.getByteArrayExtra(RESULT_BITMAP_DATA);
				if (resultBytes != null) {
					mHandler.sendEmptyMessage(SET_HEAD);
				}
			}
			if (requestCode == PhotoSetDialog.GET_PHOTO_BY_CAMERA) {//file:///storage/sdcard0/DCIM/100MEDIA/50340temp.jpg
				resultBytes = null;
				startPhotoZoom(BaseFile.getTempFile().getAbsolutePath());
				return;
			}

			if (requestCode == PhotoSetDialog.GET_PHOTO_BY_GALLERY) {
				resultBytes = null;
				Uri uri = data.getData(); // 读取相册图片
				startPhotoZoom(BaseFile.getFilePathFromContentUri(uri, getContentResolver()));
				return;
			}

		}
	}

	private void uploadHeadPic(byte[] pic) {
		new UserSettingBiz().uploadHeadPic(this, pic, BAConstants.UploadHeadType.HEAD, this);
	}

	public void startPhotoZoom(String path) {//进入到图片剪切页面
		Bundle bundle = new Bundle();
		bundle.putString(ClipViewActivity.GET_IMAGE_PATH, path);
		BaseUtils.openResultActivity(this, ClipViewActivity.class, bundle, RESULT_CLIP_IMAGE);
	}

	@Override
	public void uploadHeadPicCallBack(int retCode) {
		sendHandlerMessage(mHandler, UPLOAD_HEAD, retCode);
	}

	@Override
	public void getUserInfoCallBack(int retCode, GoGirlUserInfo userinfo) {}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
		}
		return false;
	}

}
