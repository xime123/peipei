package com.tshang.peipei.activity.chat;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.chat.Video;
import com.tshang.peipei.model.biz.chat.VideoUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

public class SelectVedioActivity extends BaseActivity {
	public static final int VIDEO_CAPTURE = 0;//录像回来请求码
	public static final int VIDEO_MY_SELECT = 1;//选择我的视频请求码
	public static final int VIDEO_ALL_SELECT = 2;//其他视频请求码
	public static final String VIDEO_SELECT_VALUE = "video_select_value";//选择了那个视频
	public static final String VIDEO_SELECT_URI = "video_select_uri";//视频的uri
	public static final String VIDEO_SELECT_FILE_SIZE = "video_select_file_size";//视频压缩的大小
	private TextView tv_my_video_count;
	private TextView tv_all_video_count;
	private DisplayImageOptions options;
	private ImageView iv_all_video;
	private ImageView iv_my_video;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//		MobclickAgent.onEvent(this, "JinRuGeRenZhuYeCiShu");
		getVideosThread();
	}

	private void getVideosThread() {
		new Thread(new Runnable() {//搜索本地所有视频

					@Override
					public void run() {
						Map<String, List<Video>> maps = VideoUtils.getVideoMap(SelectVedioActivity.this);
						mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.CHAT_VIDEO_MY_VALUE, maps.get("myvideo")));
						mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.CHAT_VEDIO_SDCARD_ALL_VALUE, maps.get("allVideo")));
					}
				}).start();
	}


	@Override
	protected void initData() {
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.message_icon_video_file_default).cacheOnDisk(true)
				.cacheInMemory(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_vedio:
			Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			//限制时长
			intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
			startActivityForResult(intent, VIDEO_CAPTURE);
			break;
		case R.id.ll_my_video:
			if (tv_my_video_count.getText().toString().equals("0")) {
				BaseUtils.showTost(this, "没有视频文件");
				return;
			}
			toShowVedioActivity(ShowVedioActivity.MYVIDEO, VIDEO_MY_SELECT);
			break;
		case R.id.ll_all_video:
			if (tv_all_video_count.getText().toString().equals("0")) {
				BaseUtils.showTost(this, "没有视频文件");
				return;
			}
			toShowVedioActivity(ShowVedioActivity.ALLVIDEO, VIDEO_ALL_SELECT);
			break;
		default:
			break;
		}
	}

	private void toShowVedioActivity(int value, int requestCode) {//跳转到视频列表
		Bundle bundle = new Bundle();
		bundle.putInt(ShowVedioActivity.CURRENTVIDEO, value);
		BaseUtils.openResultActivity(this, ShowVedioActivity.class, bundle, requestCode);
	}

	@Override
	protected void initRecourse() {

		mBackText = (TextView) findViewById(R.id.title_tv_left);//左返回按钮
		mBackText.setOnClickListener(this);
		mBackText.setText(R.string.back);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);//标题
		mTitle.setText(R.string.str_vedio);

		findViewById(R.id.iv_vedio).setOnClickListener(this);
		tv_my_video_count = (TextView) findViewById(R.id.tv_peipei_vedio_count);
		tv_all_video_count = (TextView) findViewById(R.id.tv_local_vedio_count);
		findViewById(R.id.ll_my_video).setOnClickListener(this);
		findViewById(R.id.ll_all_video).setOnClickListener(this);
		iv_my_video = (ImageView) findViewById(R.id.iv_peipei_vedio);
		iv_all_video = (ImageView) findViewById(R.id.iv_local_vedio);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (data == null) {
				return;
			}
			backPreActivityReturnData(data, requestCode);
		}
	}

	/**
	 * 
	 * @author Administrator
	 *
	 * @param data
	 * @param selectValue  VIDEO_CAPTURE //录像返回 VIDEO_MY_SELECT //选择我的视频返回，是已经压缩好了的，不需要重新压缩
	 * VIDEO_ALL_SELECT //选择其他视频返回
	 */
	private void backPreActivityReturnData(Intent data, int selectValue) {
		String path = data.getStringExtra(ShowVedioActivity.RETURNVIDEOURI);
		long size = data.getLongExtra(ShowVedioActivity.RETURNVIDEOSIZE, 0);
		String filePaht = data.getStringExtra(ShowVedioActivity.RETURNVIDEOPATH);
		Intent intent = new Intent();
		intent.putExtra(VIDEO_SELECT_VALUE, selectValue);
		intent.putExtra(VIDEO_SELECT_FILE_SIZE, size);
		if (selectValue == VIDEO_CAPTURE) {
			intent.putExtra(VIDEO_SELECT_URI, data.getData());
		} else {
			if (!TextUtils.isEmpty(path)) {
				intent.putExtra(VIDEO_SELECT_URI, Uri.parse(path));
				intent.putExtra("compressGoodVideoPath", filePaht);
			}
		}
		setResult(RESULT_OK, intent);
		this.finish();
	}

	@Override
	protected int initView() {
		return R.layout.activity_select_vedio;
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.CHAT_VEDIO_SDCARD_ALL_VALUE://搜索本地所有的视频文件
			List<Video> videos = (List<Video>) msg.obj;
			ShowItem(videos, iv_all_video, tv_all_video_count);
			break;
		case HandlerValue.CHAT_VIDEO_MY_VALUE:
			List<Video> myvideos = (List<Video>) msg.obj;
			ShowItem(myvideos, iv_my_video, tv_my_video_count);
			break;
		default:
			break;
		}

	}

	/**
	 * 
	 * @author Jeff
	 *
	 * @param videos 展示视频的数量和第一张缩略图
	 * @param imageview
	 * @param textView
	 */
	private void ShowItem(List<Video> videos, ImageView imageview, TextView textView) {
		if (videos == null || videos.size() == 0) {
			return;
		}
		textView.setText(String.valueOf(videos.size()));
		String thumbImg = videos.get(0).getThumbImg();
		if (!TextUtils.isEmpty(thumbImg)) {
			imageLoader.displayImage(thumbImg, imageview, options);
		}

	}
}
