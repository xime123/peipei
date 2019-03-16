package com.tshang.peipei.activity.main;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

public class BaseFragment extends Fragment implements OnClickListener {
	protected int screenWidth = 0;//屏幕宽度
	protected byte[] auth = "".getBytes();
	protected static final int LOADCOUNT = 24;//记录每次拉取的数量
	protected BAHandler mHandler;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	protected static final int USE_CACHE_DATA = 0;//使用缓存数据
	protected static final int REFRESH_NETWORK_DATA = 1;//重新刷新数据

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenWidth = metric.widthPixels; // 屏幕宽度（像素）
		mHandler = new BAHandler(getActivity()) {
			@Override
			public void handleMessage(Message msg) {
				BaseUtils.cancelDialog();
				BaseFragment.this.dispatchMessage(msg);
			}
		};

	}

	/** 处理handler回传的信息 */
	public void dispatchMessage(Message msg) {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BAApplication.mLocalUserInfo != null) {
			auth = BAApplication.mLocalUserInfo.auth;
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
	}

	@Override
	public void onClick(View v) {

	}

	protected boolean isMan(GoGirlUserInfo userEntity) {
		if (userEntity != null && userEntity.sex.intValue() == BAConstants.Gender.FEMALE.getValue()) {
			return false;
		}
		return true;
	}

	protected void delayLoadData(final int value) {//延迟加载数据，防止界面显示加载过长时间
		if (mHandler == null) {
			return;
		}
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				HandlerUtils.sendHandlerMessage(mHandler, value);

			}
		}, 1);
	}

	/**
	 * 
	 * @author Jeff
	 * 设置刷新文字
	 */
	protected void setRefreshTextLable(PullToRefreshListView mPullRefreshListView, int refreshing, int start, int end) {
		mPullRefreshListView.getLoadingLayoutProxy().setRefreshingLabel(getString(refreshing));
		mPullRefreshListView.getLoadingLayoutProxy().setPullLabel(getString(end));
		mPullRefreshListView.getLoadingLayoutProxy().setReleaseLabel(getString(start));
	}
}
