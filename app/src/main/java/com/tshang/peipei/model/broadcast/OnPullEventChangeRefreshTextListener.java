package com.tshang.peipei.model.broadcast;

import android.app.Activity;
import android.widget.ListView;

import com.tshang.peipei.R;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnPullEventListener;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.State;

/**
 * @Title: OnPullEventChangeRefreshTextListener.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2014年8月11日 下午5:02:10 
 *
 * @version V1.0   
 */
public class OnPullEventChangeRefreshTextListener implements OnPullEventListener<ListView> {
	private Activity activity;

	public OnPullEventChangeRefreshTextListener(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onPullEvent(PullToRefreshBase<ListView> refreshView, State state, Mode direction) {
		if (state.equals(State.PULL_TO_REFRESH)) {
			refreshView.getLoadingLayoutProxy().setPullLabel("下拉加载更多");
			refreshView.getLoadingLayoutProxy().setReleaseLabel(activity.getString(R.string.pull_to_load_release_label));
			refreshView.getLoadingLayoutProxy().setRefreshingLabel(activity.getString(R.string.pull_to_load_loading_label));
		} else {
			refreshView.getLoadingLayoutProxy().setPullLabel("上拉刷新");
			refreshView.getLoadingLayoutProxy().setReleaseLabel(activity.getString(R.string.pull_to_refresh_release_label));
			refreshView.getLoadingLayoutProxy().setRefreshingLabel(activity.getString(R.string.pull_to_refresh_refreshing_label));
		}
	}

}
