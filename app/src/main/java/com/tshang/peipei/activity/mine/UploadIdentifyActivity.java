package com.tshang.peipei.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.PhotoSetDialog;
import com.tshang.peipei.base.BaseBitmap;
import com.tshang.peipei.base.BaseCameraGalleryPhoto;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.user.UserSettingBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackUploadHeadPic;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.common.util.ImageUtils;
import com.tshang.peipei.vender.imageloader.core.assist.FailReason;
import com.tshang.peipei.vender.imageloader.core.listener.ImageLoadingListener;

/**
 * @Title: UploadIdentifyActivity.java 
 *
 * @Description: 上传认证照片界面
 *
 * @author allen  
 *
 * @date 2014-9-12 下午3:25:29 
 *
 * @version V1.0   
 */
public class UploadIdentifyActivity extends BaseActivity implements BizCallBackUploadHeadPic {
	public final String RESULT_BITMAP_DATA = "result_bitmap_data";
	public final int GET_PHOTO_BY_CAMERA = 1010;
	private final int RESULT_CLIP_IMAGE = 0x10;//图片剪切返回

	private final int UPLOAD_HEAD = 1;
	private final int SET_HEAD = 2;

	private byte[] resultBytes;

	private ImageView mImage;

	@Override
	protected void initData() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.identify_head);

		findViewById(R.id.identify_btn_photo).setOnClickListener(this);
		findViewById(R.id.identify_btn_summit).setOnClickListener(this);
		mImage = (ImageView) findViewById(R.id.identify_imageview);

	}

	@Override
	protected void initRecourse() {}

	@Override
	protected int initView() {
		return R.layout.activity_head_identifed_upload;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case UPLOAD_HEAD:
			if (msg.arg1 == 0) {
				BaseUtils.showTost(this, R.string.indentify_upload_success);
				SuccessFinish();
				sendNoticeEvent(NoticeEvent.NOTICE62);
			} else {
				BaseUtils.showTost(this, R.string.indentify_upload_failed);
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
		case R.id.identify_btn_photo:
			BaseCameraGalleryPhoto.intentSelectImage(this, false, GET_PHOTO_BY_CAMERA);
			break;
		case R.id.identify_btn_summit:
			if (resultBytes != null) {
//				MobclickAgent.onEvent(UploadIdentifyActivity.this, "shangctouxiang");
				uploadHeadPic(resultBytes);
				GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
				if (userEntity != null) {
					SharedPreferencesTools.getInstance(this, userEntity.uid.intValue() + "").saveBooleanKeyValue(false, BAConstants.PEIPEI_IDENTY);
				}
			} else {
				BaseUtils.showTost(this, "你还没有拍照");
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PhotoSetDialog.GET_PHOTO_BY_CAMERA) {//file:///storage/sdcard0/DCIM/100MEDIA/50340temp.jpg
				resultBytes = null;

				sendChatImage(BaseFile.getTempFile().getAbsolutePath());
				return;
			}
		}
	}

	public void startPhotoZoom(String path) {//进入到图片剪切页面
		Bundle bundle = new Bundle();
		bundle.putString(ClipViewActivity.GET_IMAGE_PATH, path);
		BaseUtils.openResultActivity(this, ClipViewActivity.class, bundle, RESULT_CLIP_IMAGE);
	}

	private void uploadHeadPic(byte[] pic) {
		new UserSettingBiz().uploadHeadPic(this, pic, BAConstants.UploadHeadType.IDENTIFY, this);
	}

	@Override
	public void uploadHeadPicCallBack(int retCode) {
		sendHandlerMessage(mHandler, UPLOAD_HEAD, retCode);
	}

	private void sendChatImage(String path) {//私聊发送图片
		if (!TextUtils.isEmpty(path)) {
			try {
				imageLoader.loadImage("file://" + path, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						if (loadedImage != null) {
							float scale = ((float) 640 / loadedImage.getWidth());
							loadedImage = ImageUtils.scaleImage(loadedImage, scale, scale);
							resultBytes = ImageUtils.bitmapToByte(loadedImage, 80);
							if (resultBytes != null) {
								mHandler.sendEmptyMessage(SET_HEAD);
							}
						}
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			BaseUtils.showTost(this, R.string.msg_rechoice_gallery);
		}
	}

}
