package com.tshang.peipei.activity.mine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.dialog.PhotoSetDialog;
import com.tshang.peipei.activity.mine.adapter.MineImageTextAdapter;
import com.tshang.peipei.base.BaseCameraGalleryPhoto;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.IFileUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.user.DynamicRequseControl;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.view.ClipImageLayout;

import de.greenrobot.event.EventBus;

/**
 * @Title: MineImageTextAdaterActivity.java 
 *
 * @Description: 配图
 *
 * @author Aaron  
 *
 * @date 2015-7-29 下午4:36:23 
 *
 * @version V1.0   
 */
@SuppressLint({ "ClickableViewAccessibility", "FloatMath" })
public class MineImageTextAdaterActivity extends BaseActivity implements OnClickListener {

	private final String TAG = this.getClass().getSimpleName();
	public final static String CONTENT_MSG = "content";
	public final static String ANONYMITY_FLAG = "anonymity";
	public final static String TOPICID = "topicid";
	public final static String TOPIC_TITLE = "topictitle";
	public final static String SRCPIC = "srcpic";

	private String path = IFileUtils.getSDCardRootDirectory() + "/PeiPei/cach/image/";

	private String bitmapPath = null;

	private ImageView mImageRight;
	private ViewPager mViewPager;
	private LinearLayout mLayout;
	private ClipImageLayout clipImageLayout;

	public boolean isAnonymity = false;//是否匿名发布
	private boolean isVisibilityGallery = false;
	private boolean isTypefaceblank = false;//0:黑色字体，1：白色字体
	private String content;
	private String topic_title;

	private MineImageTextAdapter mAdapter;

	private int topicid = -1;
	private int srcpic = 0;//图片默认来源抱抱,0抱抱，1百度，2本地

	private String[] images;

	private Dialog dialog;

	@Override
	protected void initData() {
		File file = new File(path);
		File[] tempList = file.listFiles();
		images = new String[tempList.length];
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				images[i] = tempList[i].toString();
			}
		}
		topicid = getIntent().getExtras().getInt(TOPICID);
		content = getIntent().getExtras().getString(CONTENT_MSG);
		topic_title = getIntent().getExtras().getString(TOPIC_TITLE);
		isAnonymity = getIntent().getExtras().getBoolean(ANONYMITY_FLAG);
		srcpic = getIntent().getExtras().getInt(SRCPIC);
		clipImageLayout.setTitleTextView(topic_title);
		String path = images[images.length - 1];
		clipImageLayout.setImagePath(path);
		clipImageLayout.setContentText(content);

		initViewPager();
	}

	@Override
	protected void initRecourse() {
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText("配图");
		mTextRight = (TextView) findViewById(R.id.title_tv_right);
		mLinRight = (LinearLayout) this.findViewById(R.id.title_lin_right);
		mLinRight.setVisibility(View.VISIBLE);
		mLinRight.setOnClickListener(this);
		mImageRight = (ImageView) this.findViewById(R.id.title_iv_right);
		mImageRight.setVisibility(View.GONE);
		mTextRight = (TextView) this.findViewById(R.id.title_tv_right);
		mTextRight.setVisibility(View.VISIBLE);
		mTextRight.setText("发布");
		mTextRight.setVisibility(View.VISIBLE);

		mLayout = (LinearLayout) findViewById(R.id.img_text_adpter_viewpager_layout);
		mViewPager = (ViewPager) mLayout.findViewById(R.id.img_text_adpter_viewpager);
		clipImageLayout = (ClipImageLayout) findViewById(R.id.img_text_adapter_clipImageLayout);

		findViewById(R.id.title_tv_left).setOnClickListener(this);
		findViewById(R.id.img_text_adapter_next_mg_layout).setOnClickListener(this);
		findViewById(R.id.img_text_adapter_album_layout).setOnClickListener(this);
		findViewById(R.id.img_text_adapter_typeface_layout).setOnClickListener(this);
		mTextRight.setOnClickListener(this);

		visibilityGallery(isVisibilityGallery);
	}

	@Override
	protected int initView() {
		return R.layout.activity_image_text_adpter;
	}

	/**
	 * 初始化ViewPager
	 * @author Aaron
	 *
	 */
	private void initViewPager() {
		mAdapter = new MineImageTextAdapter(this, this, content, topic_title, images);
		mViewPager.setOffscreenPageLimit(8);
		mLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mViewPager.dispatchTouchEvent(event);
			}
		});
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if (mViewPager != null) {
					mViewPager.invalidate();
					mLayout.invalidate();
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_tv_left:
			this.finish();
			break;
		case R.id.title_tv_right://发布
			dialog = DialogFactory.createLoadingDialog(this, R.string.publish_loading);
			DialogFactory.showDialog(dialog);
			new Thread() {
				public void run() {
					Bitmap bitmap = clipImageLayout.clip();
					saveMyBitmap("dynamic", bitmap);
					mHandler.sendEmptyMessage(HandlerValue.IMG_CLIP_SUCCESS);
				};
			}.start();
			break;
		case R.id.img_text_adapter_next_mg_layout://切图
			if (isGroupChatValue) {
				mTextRight.setVisibility(View.VISIBLE);
				isVisibilityGallery = false;
			} else {
				mTextRight.setVisibility(View.GONE);
				isVisibilityGallery = true;
			}
			visibilityGallery(isVisibilityGallery);
			break;
		case R.id.img_text_adapter_album_layout://相册
			BaseCameraGalleryPhoto.intentSelectImage(this, true, PhotoSetDialog.GET_PHOTO_BY_GALLERY);
			break;
		case R.id.img_text_adapter_typeface_layout://字体
			if (isTypefaceblank) {
				clipImageLayout.setTextColor(getResources().getColor(R.color.white));
				isTypefaceblank = false;
			} else {
				clipImageLayout.setTextColor(getResources().getColor(R.color.black));
				isTypefaceblank = true;
			}
			mAdapter.setTextColor(isTypefaceblank);
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @author Aaron
	 *
	 * @param bitName
	 * @param mBitmap
	 */
	public void saveMyBitmap(String bitName, Bitmap mBitmap) {
		File f = new File(BaseFile.getExternalCacheDir(this) + bitName + ".png");
		bitmapPath = f.getAbsolutePath().toString();
		try {
			f.createNewFile();
		} catch (IOException e) {
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void visibilityGallery(boolean isVisibilityGallery) {
		if (isVisibilityGallery) {
			mLayout.setVisibility(View.VISIBLE);
		} else
			mLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	public void dispatchMessage(Message msg) {
		switch (msg.what) {
		case HandlerValue.IMG_TEXT_VIEWPAGER_ITEM_CLICK://ViewPage Item点击事件处理
			srcpic = getIntent().getExtras().getInt(SRCPIC);
			String path = (String) msg.obj;
			clipImageLayout.setImagePath(path);
			isVisibilityGallery = false;
			visibilityGallery(isVisibilityGallery);
			mTextRight.setVisibility(View.VISIBLE);
			break;
		case HandlerValue.IMG_CLIP_SUCCESS://图片裁剪成功
			if (!TextUtils.isEmpty(topic_title)) {
				content = topic_title + "\n" + content;
			}
			//请求上传
			DynamicRequseControl control = new DynamicRequseControl();
			control.writeDynamicRequest("广东", "深圳", isAnonymity == true ? 1 : 0, isTypefaceblank == true ? 0 : 1, srcpic, content, bitmapPath,
					topicid, mHandler);
			break;
		case HandlerValue.PUBLISH_DYNAMIC_SUCCESS://发贴成功
			DialogFactory.dimissDialog(dialog);
			if (msg.arg1 == 0) {
				NoticeEvent event = new NoticeEvent();
				event.setFlag(NoticeEvent.NOTICE86);
				EventBus.getDefault().post(event);
				this.finish();
			} else
				BaseUtils.showTost(this, R.string.publish_error);
			break;
		case HandlerValue.PUBLISH_DYNAMIC_ERROR://发贴失败
			DialogFactory.dimissDialog(dialog);
			BaseUtils.showTost(this, R.string.publish_error);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case PhotoSetDialog.GET_PHOTO_BY_GALLERY://相册回调
				Uri uri = data.getData();
				if (uri != null && getContentResolver() != null) {
					srcpic = (1 << 2);
					String path = BaseFile.getFilePathFromContentUri(uri, getContentResolver());
					clipImageLayout.setImagePath(path);
					isVisibilityGallery = false;
					visibilityGallery(isVisibilityGallery);
				}
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onEvent(NoticeEvent event) {
		switch (event.getFlag()) {
		case NoticeEvent.NOTICE85:
			sendHandlerMessage(mHandler, HandlerValue.IMG_TEXT_VIEWPAGER_ITEM_CLICK, 0, event.getObj());
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (mLayout.getVisibility() == View.VISIBLE) {
				if (isVisibilityGallery) {
					isVisibilityGallery = false;
					visibilityGallery(isVisibilityGallery);
					return true;
				} else {
					isVisibilityGallery = true;
					visibilityGallery(isVisibilityGallery);
				}
			}
		}
		BaseFile.delete(new File(IFileUtils.getSDCardRootDirectory() + "/PeiPei/cach/image"));
		return super.onKeyDown(keyCode, event);
	}
}
