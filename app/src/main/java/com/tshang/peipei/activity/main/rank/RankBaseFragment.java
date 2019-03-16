package com.tshang.peipei.activity.main.rank;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.main.BaseFragment;
import com.tshang.peipei.activity.main.rank.adapter.RankAdapter;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfo;
import com.tshang.peipei.model.harem.HaremUtils;
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

/**
 * @Title: RankBaseFt.java 
 *
 * @Description: 排行基类
 *
 * @author Jeff 
 *
 * @date 2014年7月29日 下午5:42:21 
 *
 * @version V1.0   
 */
public class RankBaseFragment extends BaseFragment implements OnItemClickListener, OnRefreshListener2<ListView> {

	protected PullToRefreshListView plistview;

	protected RankAdapter mRankAdapter;
	protected DisplayImageOptions options;
	protected int RANK_CURRENT_TYPE = -1;

	public static final int RANK_NEW_FEMALE_TYPE = 0;
	public static final int RANK_NEW_MALE_TYPE = 1;

	public static final int RANK_TOTAL_TYPE = 2;
	public static final int RANK_WEEK_TYPE = 3;
	public static final int RANK_DAY_TYPE = 4;

	public static final int RANK_GAME_TYPE = 5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		options = ImageOptionsUtils.getImageKeyOptions(getActivity());//初始化加载图片参数
		mRankAdapter = new RankAdapter(getActivity());
	}

	public synchronized void refresh(int type) {}//处理刷新

	public synchronized void loadMore(int type) {};//处理加载更多

	protected synchronized void initLoadData() {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//每一项的点击事件
		Object object = parent.getAdapter().getItem(position);
		if (object instanceof GroupInfo) {
			GroupInfo info = (GroupInfo) object;
			if (info != null) {
				boolean isHost = false;
				if (BAApplication.mLocalUserInfo != null && BAApplication.mLocalUserInfo.uid.intValue() == info.owner.intValue()) {//说明进入的是自己个人主页，底部不可见
					isHost = true;
				}
				HaremUtils.intentManagerHaremActivity(getActivity(), info, false, isHost);
			}
		} else if (object instanceof GoGirlUserInfo) {
			GoGirlUserInfo userInfo = (GoGirlUserInfo) object;
			if (userInfo != null) {
				SpaceUtils.toSpaceCustom(getActivity(), userInfo, 2);//跳转到个人空间
			}
		}

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {//下拉刷新 
		refresh(RANK_CURRENT_TYPE);

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {//上拉加载更多刷新 
		loadMore(RANK_CURRENT_TYPE);
	}

	protected void ViewStateRestored() {//界面恢复

	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {//被销毁了数据恢复
		super.onViewStateRestored(savedInstanceState);
		if (savedInstanceState != null) {
			RANK_CURRENT_TYPE = savedInstanceState.getInt("rank_current_type");
			ViewStateRestored();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {//销毁了数据保存
		super.onSaveInstanceState(outState);
		if (outState != null) {
			outState.putInt("rank_current_type", RANK_CURRENT_TYPE);
		}
	}

}
