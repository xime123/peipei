package com.tshang.peipei.activity.chat;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.chat.adapter.VideoItemAdapter;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.chat.Video;
import com.tshang.peipei.model.biz.chat.VideoUtils;
import com.tshang.peipei.view.PullToRefreshHeaderGridView;

public class ShowVedioActivity extends BaseActivity implements OnItemClickListener {
	private PullToRefreshHeaderGridView pgw;
	public static final int MYVIDEO = 1;//查看已经压缩好的视频
	public static final int ALLVIDEO = 2;//查看所有的视频
	public int curerntShowVideo = MYVIDEO;
	public static final String CURRENTVIDEO = "currentvideo";
	public static final String RETURNVIDEOURI = "returnvideouri";
	public static final String RETURNVIDEOPATH = "returnvideopath";
	public static final String RETURNVIDEOSIZE = "returnvideosize";
	private VideoItemAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initData() {
		Bundle bundle = this.getIntent().getExtras();
		curerntShowVideo = bundle.getInt(CURRENTVIDEO);
		if (curerntShowVideo == ALLVIDEO) {
			mTitle.setText(R.string.str_local_vedio);
		}
		if (bundle != null) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					Map<String, List<Video>> maps = VideoUtils.getVideoMap(ShowVedioActivity.this);
					if (curerntShowVideo == ALLVIDEO) {
						mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.CHAT_VIDEO_SHOW_LIST_VALUE, maps.get("allVideo")));
					} else {
						mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.CHAT_VIDEO_SHOW_LIST_VALUE, maps.get("myvideo")));
					}
				}
			}).start();
		}

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		default:
			break;
		}
	}

	@Override
	protected void initRecourse() {

		mBackText = (TextView) findViewById(R.id.title_tv_left);//左返回按钮
		mBackText.setOnClickListener(this);
		mBackText.setText(R.string.back);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);//标题
		mTitle.setText(R.string.str_peipei_vedio);

		pgw = (PullToRefreshHeaderGridView) findViewById(R.id.pgv_video);
		adapter = new VideoItemAdapter(this);
		pgw.setAdapter(adapter);
		pgw.getRefreshableView().setOnItemClickListener(this);
	}

	@Override
	protected int initView() {
		return R.layout.activity_showvideo;
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.CHAT_VIDEO_SHOW_LIST_VALUE://搜索本地所有的视频文件
			List<Video> videos = (List<Video>) msg.obj;
			adapter.setList(videos);
			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		List<Video> list = adapter.getList();
		if (list != null && !list.isEmpty()) {
			Video video = list.get(position);
			if (video != null) {
				String uriPath = video.getThumbImg();
				String filePath = video.getPath();
				if (TextUtils.isEmpty(uriPath)) {
					uriPath = "";
				}
				if (TextUtils.isEmpty(filePath)) {
					filePath = "";
				}
				long size = video.getSize();
				Intent intent = new Intent();
				intent.putExtra(RETURNVIDEOPATH, filePath);
				intent.putExtra(RETURNVIDEOURI, uriPath);
				intent.putExtra(RETURNVIDEOSIZE, size);
				setResult(RESULT_OK, intent);
				this.finish();
			}
		}
	}
}
