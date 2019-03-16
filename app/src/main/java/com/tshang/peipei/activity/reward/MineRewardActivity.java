package com.tshang.peipei.activity.reward;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.reward.adapter.MineFragmentViewPagerAdapter;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.request.RequestRewardListInfo.GetRewardListInfoCallBack;

/**
 * @Title: MineRewardActivity.java 
 *
 * @Description: 我的悬赏 
 *
 * @author Aaron  
 *
 * @date 2015-9-29 下午3:20:39 
 *
 * @version V1.0   
 */
public class MineRewardActivity extends BaseActivity implements OnPageChangeListener, GetRewardListInfoCallBack {

	private TextView joinTv, pulishTv, winTv;
	private TextView joinLineTv, pulishLineTv, winLineTv;

	private ViewPager viewPager;

	protected static final int LOADCOUNT = 10;
	protected int startLoadPosition = -1;

	@Override
	protected void initData() {

	}

	@Override
	protected void initRecourse() {
		mTitle = (TextView) this.findViewById(R.id.title_tv_mid);
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setOnClickListener(this);
		mTitle.setText("我的悬赏");

		joinTv = (TextView) findViewById(R.id.join_reward_title_tv);
		pulishTv = (TextView) findViewById(R.id.publish_reward_title_tv);
		winTv = (TextView) findViewById(R.id.win_reward_title_tv);
		joinLineTv = (TextView) findViewById(R.id.join_reward_line_tv);
		pulishLineTv = (TextView) findViewById(R.id.publish_reward_line_tv);
		winLineTv = (TextView) findViewById(R.id.win_reward_line_tv);

		viewPager = (ViewPager) findViewById(R.id.mine_reward_viewPager);
		viewPager.setPageMargin(30);
		viewPager.setAdapter(new MineFragmentViewPagerAdapter(getSupportFragmentManager(), getIntent().getExtras().getInt("anonymNickId")));
		viewPager.setOnPageChangeListener(this);
		viewPager.setOffscreenPageLimit(3);

		joinTv.setOnClickListener(this);
		pulishTv.setOnClickListener(this);
		winTv.setOnClickListener(this);

		seleteJoinReward();
	}

	@Override
	protected int initView() {
		return R.layout.activity_mine_reward_layout;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.join_reward_title_tv:
			seleteJoinReward();
			viewPager.setCurrentItem(0);
			break;
		case R.id.publish_reward_title_tv:
			seletePulishReward();
			viewPager.setCurrentItem(1);
			break;
		case R.id.win_reward_title_tv:
			seleteWinReward();
			viewPager.setCurrentItem(2);
			break;

		default:
			break;
		}
	}

	private void seleteJoinReward() {
		joinLineTv.setVisibility(View.VISIBLE);
		joinTv.setTextColor(getResources().getColor(R.color.red));

		pulishLineTv.setVisibility(View.GONE);
		pulishTv.setTextColor(getResources().getColor(R.color.black));

		winLineTv.setVisibility(View.GONE);
		winTv.setTextColor(getResources().getColor(R.color.black));
	}

	private void seletePulishReward() {
		joinLineTv.setVisibility(View.GONE);
		joinTv.setTextColor(getResources().getColor(R.color.black));

		pulishLineTv.setVisibility(View.VISIBLE);
		pulishTv.setTextColor(getResources().getColor(R.color.red));

		winLineTv.setVisibility(View.GONE);
		winTv.setTextColor(getResources().getColor(R.color.black));
	}

	private void seleteWinReward() {
		joinLineTv.setVisibility(View.GONE);
		joinTv.setTextColor(getResources().getColor(R.color.black));

		pulishLineTv.setVisibility(View.GONE);
		pulishTv.setTextColor(getResources().getColor(R.color.black));

		winLineTv.setVisibility(View.VISIBLE);
		winTv.setTextColor(getResources().getColor(R.color.red));
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		switch (arg0) {
		case 0:
			seleteJoinReward();
			break;
		case 1:
			seletePulishReward();
			break;
		case 2:
			seleteWinReward();
			break;

		default:
			break;
		}
	}

	@Override
	public void onGetRewardListInfoSuccess(int code, int isend, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.GET_MINE_REWARD_LIST_SUCCESS, code, isend, obj);
	}

	@Override
	public void onGetRewardListInfoError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.GET_MINE_REWARD_LIST_ERROR, code);
	}
}
