package com.tshang.peipei.activity.harem;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.HintToastDialog;
import com.tshang.peipei.activity.dialog.PhotoSetDialog;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.harem.CreateHarem;

/**
 * 我的后宫界面
 * @author Jeff
 *
 */
public class CreateHaremActivity extends BaseActivity {
	private EditText edtSign;
	private EditText edtName;
	private ImageView imageview;
	private String imagePath = "";

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.HAREM_CREATE_SUCCESS_VALUE:
			BaseUtils.showTost(this, R.string.str_harem_create_success);
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			this.finish();
			break;
		case HandlerValue.HAREM_CREATE_LEVER_LOWER_FAILED_VALUE://等级不够
			new HintToastDialog(this, R.string.str_harem_dialog_level_lower, R.string.ok).showDialog();
			break;
		case HandlerValue.HAREM_CREATE_LEVER_GROUP_LIMIT_FAILED_VALUE://后宫达上限
			BaseUtils.showTost(this, "你不能够创建更多的后宫了");
			break;
		case HandlerValue.HAREM_CREATE_FAILED_VALUE:
			BaseUtils.showTost(this, "创建失败");
			break;

		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_create_harem_image:
			new PhotoSetDialog(this, android.R.style.Theme_Translucent_NoTitleBar).showDialog(0, 0);
			break;
		case R.id.tv_create_harem_submit:
			String name = edtName.getText().toString();
			String haremSign = edtSign.getText().toString();
			CreateHarem.getInstance().reqCreateHarem(this, imagePath, name, haremSign, mHandler);
			break;

		default:
			break;
		}
	}

	@Override
	protected void initData() {}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_create_harem);

		edtSign = (EditText) findViewById(R.id.edt_create_harem_sign);
		edtName = (EditText) findViewById(R.id.edt_create_harem_name);
		imageview = (ImageView) findViewById(R.id.iv_create_harem_image);
		imageview.setOnClickListener(this);
		findViewById(R.id.tv_create_harem_submit).setOnClickListener(this);
	}

	@Override
	protected int initView() {
		return R.layout.activity_haremcreate;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PhotoSetDialog.GET_PHOTO_BY_CAMERA) {//file:///storage/sdcard0/DCIM/100MEDIA/50340temp.jpg
				imagePath = BaseFile.getTempFile().getAbsolutePath();
			}

			if (requestCode == PhotoSetDialog.GET_PHOTO_BY_GALLERY) {
				Uri uri = data.getData(); // 读取相册图片
				if (uri != null) {
					imagePath = BaseFile.getFilePathFromContentUri(uri, getContentResolver());
				}
			}
			if (!TextUtils.isEmpty(imagePath)) {
				imageLoader.displayImage("file://" + imagePath, imageview);
			}
		}
	}

}
