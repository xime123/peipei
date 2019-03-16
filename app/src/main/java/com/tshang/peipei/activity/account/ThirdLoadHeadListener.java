package com.tshang.peipei.activity.account;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.BaseBitmap;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.biz.user.UserAccountBiz;
import com.tshang.peipei.model.biz.user.UserSettingBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetUserInfo;
import com.tshang.peipei.model.bizcallback.BizCallBackUploadHeadPic;
import com.tshang.peipei.vender.imageloader.core.assist.FailReason;
import com.tshang.peipei.vender.imageloader.core.listener.ImageLoadingListener;

/**
 * @Title: ThirdLoadHead.java 
 *
 * @Description: 下载头像
 *
 * @author allen  
 *
 * @date 2014-7-30 下午2:18:08 
 *
 * @version V1.0   
 */
public class ThirdLoadHeadListener implements ImageLoadingListener, BizCallBackUploadHeadPic, BizCallBackGetUserInfo {

	private Activity context;
	private int sex;

	public ThirdLoadHeadListener(Activity context, int sex) {
		this.context = context;
		this.sex = sex;
	}

	@Override
	public void onLoadingStarted(String imageUri, View view) {}

	@Override
	public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
		Bundle b = new Bundle();
		b.putInt("sex", sex);

		Intent intent = new Intent(context, UploadHeadActivity.class);
		if (b != null) {
			intent.putExtras(b);
		}

		context.setResult(InvitationActivity.INVITATE_FAILED);
		context.finish();
		//		context.startActivityForResult(intent, InvitationActivity.INVITATE_FAILED);
		//		context.overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		new UserSettingBiz().uploadHeadPic(context, BaseBitmap.bitmap2Bytes(loadedImage), BAConstants.UploadHeadType.HEAD, this);
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {}

	@Override
	public void uploadHeadPicCallBack(int retCode) {
		new UserAccountBiz(context).getUserInfo(this);
		context.setResult(InvitationActivity.INVITATE);
		context.finish();
	}

	@Override
	public void getUserInfoCallBack(int retCode, GoGirlUserInfo userinfo) {}

}
