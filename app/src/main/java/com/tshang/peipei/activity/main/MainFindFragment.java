package com.tshang.peipei.activity.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.mine.MineFaqActivity;
import com.tshang.peipei.activity.reward.RewardListActivity;
import com.tshang.peipei.activity.space.DynamicActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.RewardType;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.storage.SharedPreferencesTools;

import de.greenrobot.event.EventBus;

/**
 * @Title: MainFindFragment.java 
 *
 * @Description: 发现 
 *
 * @author Aaron  
 *
 * @date 2015-10-9 下午4:48:40 
 *
 * @version V1.0   
 */
public class MainFindFragment extends BaseFragment {

	private LinearLayout rewardLayout, rankLayout, activityLayout, gameLayout, dynamicLayout, blankHouseLayout, haremLayout;
	private ImageView ivUrlNew, iv_dynamic_new;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		setUserVisibleHint(true);
		super.onActivityCreated(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_find, null);
		findViewById(view);
		return view;

	}

	private void findViewById(View view) {
		rewardLayout = (LinearLayout) view.findViewById(R.id.find_reward_layout);
		rankLayout = (LinearLayout) view.findViewById(R.id.find_rank_layout);
		activityLayout = (LinearLayout) view.findViewById(R.id.find_activity_layout);
		gameLayout = (LinearLayout) view.findViewById(R.id.find_game_layout);
		dynamicLayout = (LinearLayout) view.findViewById(R.id.find_dynamic_layout);
		blankHouseLayout = (LinearLayout) view.findViewById(R.id.find_black_house_layout);
		ivUrlNew = (ImageView) view.findViewById(R.id.img_activities_new);
		iv_dynamic_new = (ImageView) view.findViewById(R.id.img_dynamic_new);
		haremLayout = (LinearLayout) view.findViewById(R.id.find_harem_layout);

		rewardLayout.setOnClickListener(this);
		rankLayout.setOnClickListener(this);
		activityLayout.setOnClickListener(this);
		gameLayout.setOnClickListener(this);
		dynamicLayout.setOnClickListener(this);
		blankHouseLayout.setOnClickListener(this);
		haremLayout.setOnClickListener(this);

		setNum();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {//友盟统计
		super.onHiddenChanged(hidden);
		if (!hidden) {
			setNum();
		}
		setDynamicNum();
	}

	private void setDynamicNum() {
		iv_dynamic_new.setVisibility(View.GONE);
	}

	public void onEvent(NoticeEvent event) {
		if (event.getFlag() == NoticeEvent.NOTICE88) {//取消
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					iv_dynamic_new.setVisibility(View.GONE);
				}
			});

		} else if (event.getFlag() == NoticeEvent.NOTICE87 || event.getFlag() == NoticeEvent.NOTICE89) {//显示
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					iv_dynamic_new.setVisibility(View.VISIBLE);
				}
			});
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (this.isVisible()) {
			if (isVisibleToUser) {
				setDynamicNum();
			}
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void onResume() {
		super.onResume();
		setNum();
		setDynamicNum();
	}

	private void setNum() {
		if (SharedPreferencesTools.getInstance(getActivity()).getBooleanKeyValue(BAConstants.PEIPEI_NEW_URL)) {
			ivUrlNew.setVisibility(View.VISIBLE);
		} else {
			ivUrlNew.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.find_reward_layout:
			Bundle bundle = new Bundle();
			bundle.putInt(RewardListActivity.LABLE_FLAG, RewardType.ALL.getValue());
			BaseUtils.openActivity(getActivity(), RewardListActivity.class, bundle);
			break;
		case R.id.find_rank_layout:
			MainRankActivity.openMineFaqActivity(getActivity());
			break;
		case R.id.find_activity_layout:
			MineFaqActivity.openMineFaqActivity(getActivity(), MineFaqActivity.ACTIVITY_VALUE);
			break;
		case R.id.find_game_layout:
			MineFaqActivity.openMineFaqActivity(getActivity(), MineFaqActivity.GAMES_VALUE);
			break;
		case R.id.find_dynamic_layout:
			DynamicActivity.openMineFaqActivity(getActivity());
			break;
		case R.id.find_black_house_layout:
			MineFaqActivity.openMineFaqActivity(getActivity(), MineFaqActivity.FORBIT_VALUE);
			break;
		case R.id.find_harem_layout:
			BaseUtils.openActivity(getActivity(), HaremListActivity.class);
			break;

		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
