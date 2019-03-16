package com.tshang.peipei.activity.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.main.rank.RankFragmenttAdapter;
import com.tshang.peipei.model.entity.SuspensionEntity;
import com.tshang.peipei.model.event.NoticeEvent;

import de.greenrobot.event.EventBus;

/**
 * deprecated
 * 
 * @Title: MainRankFragment
 *
 * @Description: 主界面排行界面
 *
 * @author allen
 *
 * @version V1.0   
 */
public class MainRankFragment extends BaseFragment implements OnPageChangeListener {

	private ViewPager mViewPager;
	private TextView mTextQueen;
	private TextView mTextKing;
	private TextView mTextNew;
	private TextView mTextGame;
	private RankFragmenttAdapter adapter;

	private int mCurrPage = 0;// 当前页卡编号
	public static int curView = SuspensionEntity.ACTIVITY_RANK_NEW;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_rank, null);
		initUi(view);
		setTitleResource();

		return view;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rank_text_new:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.rank_text_queen:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.rank_text_king:
			mViewPager.setCurrentItem(2);
			break;
		case R.id.rank_text_game:
			mViewPager.setCurrentItem(3);
			break;
		}
	}

	private void initUi(View view) {
		mViewPager = (ViewPager) view.findViewById(R.id.rank_viewpager);

		mTextNew = (TextView) view.findViewById(R.id.rank_text_new);
		mTextNew.setOnClickListener(this);
		mTextQueen = (TextView) view.findViewById(R.id.rank_text_queen);
		mTextQueen.setOnClickListener(this);
		mTextKing = (TextView) view.findViewById(R.id.rank_text_king);
		mTextKing.setOnClickListener(this);
		mTextGame = (TextView) view.findViewById(R.id.rank_text_game);
		mTextGame.setOnClickListener(this);

		setViewPageAndUnderline(view);
	}

	private void setViewPageAndUnderline(View view) {
		adapter = new RankFragmenttAdapter(getFragmentManager());
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOffscreenPageLimit(4);
		mViewPager.setOnPageChangeListener(this);
	}

	private void setTitleResource() {
		if (mCurrPage == 0) {
			mTextNew.setTextColor(getResources().getColor(R.color.white));
			mTextQueen.setTextColor(getResources().getColor(R.color.peach));
			mTextKing.setTextColor(getResources().getColor(R.color.peach));
			mTextGame.setTextColor(getResources().getColor(R.color.peach));
			mTextNew.setBackgroundResource(R.drawable.main_bar_tab_pr);
			mTextQueen.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mTextKing.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mTextGame.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		} else if (mCurrPage == 1) {
			mTextNew.setTextColor(getResources().getColor(R.color.peach));
			mTextQueen.setTextColor(getResources().getColor(R.color.white));
			mTextKing.setTextColor(getResources().getColor(R.color.peach));
			mTextGame.setTextColor(getResources().getColor(R.color.peach));
			mTextNew.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mTextQueen.setBackgroundResource(R.drawable.main_bar_tab_pr);
			mTextKing.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mTextGame.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		} else if (mCurrPage == 2) {
			mTextNew.setTextColor(getResources().getColor(R.color.peach));
			mTextQueen.setTextColor(getResources().getColor(R.color.peach));
			mTextKing.setTextColor(getResources().getColor(R.color.white));
			mTextGame.setTextColor(getResources().getColor(R.color.peach));
			mTextNew.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mTextQueen.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mTextKing.setBackgroundResource(R.drawable.main_bar_tab_pr);
			mTextGame.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		} else {
			mTextNew.setTextColor(getResources().getColor(R.color.peach));
			mTextQueen.setTextColor(getResources().getColor(R.color.peach));
			mTextKing.setTextColor(getResources().getColor(R.color.peach));
			mTextGame.setTextColor(getResources().getColor(R.color.white));
			mTextNew.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mTextQueen.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mTextKing.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mTextGame.setBackgroundResource(R.drawable.main_bar_tab_pr);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		mCurrPage = arg0;
		setTitleResource();
		setMainView();
	}
	
	private void setMainView(){
		switch (mCurrPage) {
		case 0:
			curView = SuspensionEntity.ACTIVITY_RANK_NEW;
			break;
		case 1:
			curView = SuspensionEntity.ACTIVITY_RANK_FEMALE;
			break;
		case 2:
			curView = SuspensionEntity.ACTIVITY_RANK_MALE;
			break;
		case 3:
			curView = SuspensionEntity.ACTIVITY_RANK_GAME;
			break;
		}
		notifySuspsionFlush();
	}
	
	private void notifySuspsionFlush(){
		NoticeEvent notice = new NoticeEvent();
		notice.setFlag(NoticeEvent.NOTICE93);
		notice.setNum(curView);
		EventBus.getDefault().post(notice);
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {//被销毁了数据恢复
		super.onViewStateRestored(savedInstanceState);
		if (savedInstanceState != null) {
			mCurrPage = savedInstanceState.getInt("mCurrPage");
			mViewPager.setCurrentItem(mCurrPage);

		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (hidden) {
			mHandler.removeCallbacksAndMessages(null);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {//销毁了数据保存
		super.onSaveInstanceState(outState);
		if (outState != null) {
			outState.putInt("mCurrPage", mCurrPage);
		}
	}

}
