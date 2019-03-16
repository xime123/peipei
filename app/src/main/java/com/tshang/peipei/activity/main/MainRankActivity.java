package com.tshang.peipei.activity.main;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.main.rank.RankBaseFragment;
import com.tshang.peipei.activity.main.rank.RankFemaleFragment;
import com.tshang.peipei.activity.main.rank.RankFragmenttAdapter;
import com.tshang.peipei.activity.main.rank.RankGameFragment;
import com.tshang.peipei.activity.main.rank.RankMaleFragment;
import com.tshang.peipei.activity.main.rank.RankNewFragment;
import com.tshang.peipei.activity.suspension.SuspensionActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.model.entity.SuspensionActEntity;
import com.tshang.peipei.model.entity.SuspensionEntity;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: MainRankActivity.java 
 *
 * @Description: 排行榜界面
 *
 * @author DYH  
 *
 * @date 2015-10-12 下午8:14:06 
 *
 * @version V1.0   
 */
public class MainRankActivity extends BaseActivity implements OnPageChangeListener {

	private TextView mTitleRight;
	private ViewPager mViewPager;
	private TextView mTextQueen;
	private TextView mTextKing;
	private TextView mTextNew;
	private TextView mTextGame;
	private View mNewViewLine;
	private View mQueenViewLine;
	private View mKingViewLine;
	private View mGameViewLine;
	private RankFragmenttAdapter adapter;
	private PopupWindow popupGlamourWindow;
	private PopupWindow popupNewWindow;
	private PopupWindow popupRichWindow;
	private PopupWindow popupGameWindow;

	private RankNewFragment rankNewFragment;
	private RankGameFragment rankGameFragment;
	private RankFemaleFragment rankGlamourFragment;
	private RankMaleFragment rankMaleFragment;

	private int mCurrPage = 0;// 当前页卡编号

	private ImageView iv_susp_icon;
	private ImageView iv_close;
	private FrameLayout rl_susp;
	private float density;
	private SuspensionActEntity actEntify;
	private boolean enterOpen;
	private int cur_view = SuspensionEntity.ACTIVITY_RANK_NEW; //当前MainActivity显示的View，用来处理显示悬浮窗的逻辑

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		density = metric.density;
	}

	@Override
	protected void initData() {
		setSuspensionView();
	}

	@Override
	protected void initRecourse() {
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(getString(R.string.hot_rank));
		mTitleRight = (TextView) findViewById(R.id.title_tv_right);
		mViewPager = (ViewPager) findViewById(R.id.rank_viewpager);
		mTextNew = (TextView) findViewById(R.id.rank_text_new);
		mTextQueen = (TextView) findViewById(R.id.rank_text_queen);
		mTextKing = (TextView) findViewById(R.id.rank_text_king);
		mTextGame = (TextView) findViewById(R.id.rank_text_game);

		mNewViewLine = findViewById(R.id.rank_text_new_line);
		mQueenViewLine = findViewById(R.id.rank_text_queen_line);
		mKingViewLine = findViewById(R.id.rank_text_king_line);
		mGameViewLine = findViewById(R.id.rank_text_game_line);

		rl_susp = (FrameLayout) findViewById(R.id.rl_susp);
		iv_susp_icon = (ImageView) findViewById(R.id.iv_susp_icon);
		iv_close = (ImageView) findViewById(R.id.iv_close);
		iv_susp_icon.setOnClickListener(this);
		iv_close.setOnClickListener(this);

		setViewPageAndUnderline();
		initPopupWindow();
		setListener();
	}

	private void initPopupWindow() {
		initNewPopupWindow();
		initGlamourPopupWindow();
		initRichPopupWindow();
		initGamePopupWindow();
	}

	private void initNewPopupWindow() {
		View view = View.inflate(this, R.layout.activity_rank_new_popupwindow, null);
		TextView tv_rank1 = (TextView) view.findViewById(R.id.tv_rank1);
		TextView tv_rank2 = (TextView) view.findViewById(R.id.tv_rank2);
		tv_rank1.setOnClickListener(newOnClickListener);
		tv_rank2.setOnClickListener(newOnClickListener);
		popupNewWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupNewWindow.setTouchable(true);
		popupNewWindow.setOutsideTouchable(true);
		popupNewWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
	}

	private void initGlamourPopupWindow() {
		View view = View.inflate(this, R.layout.activity_rank_popupwindow, null);
		TextView tv_rank_day = (TextView) view.findViewById(R.id.tv_rank_day);
		TextView tv_rank_week = (TextView) view.findViewById(R.id.tv_rank_week);
		TextView tv_rank_all = (TextView) view.findViewById(R.id.tv_rank_all);
		tv_rank_day.setOnClickListener(glamourOnClickListener);
		tv_rank_week.setOnClickListener(glamourOnClickListener);
		tv_rank_all.setOnClickListener(glamourOnClickListener);
		popupGlamourWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupGlamourWindow.setTouchable(true);
		popupGlamourWindow.setOutsideTouchable(true);
		popupGlamourWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
	}

	private void initRichPopupWindow() {
		View view = View.inflate(this, R.layout.activity_rank_popupwindow, null);
		TextView tv_rank_day = (TextView) view.findViewById(R.id.tv_rank_day);
		TextView tv_rank_week = (TextView) view.findViewById(R.id.tv_rank_week);
		TextView tv_rank_all = (TextView) view.findViewById(R.id.tv_rank_all);
		tv_rank_day.setOnClickListener(richOnClickListener);
		tv_rank_week.setOnClickListener(richOnClickListener);
		tv_rank_all.setOnClickListener(richOnClickListener);
		popupRichWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupRichWindow.setTouchable(true);
		popupRichWindow.setOutsideTouchable(true);
		popupRichWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
	}

	private void initGamePopupWindow() {
		View view = View.inflate(this, R.layout.activity_rank_new_popupwindow, null);
		TextView tv_rank1 = (TextView) view.findViewById(R.id.tv_rank1);
		TextView tv_rank2 = (TextView) view.findViewById(R.id.tv_rank2);
		tv_rank1.setText(getString(R.string.str_game_week_rank));
		tv_rank2.setText(getString(R.string.str_harem_week_rank));
		//TODO 去掉后宫排行榜
		tv_rank2.setVisibility(View.GONE);
		tv_rank1.setOnClickListener(gameOnClickListener);
		tv_rank2.setOnClickListener(gameOnClickListener);
		popupGameWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupGameWindow.setTouchable(true);
		popupGameWindow.setOutsideTouchable(true);
		popupGameWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
	}

	private OnClickListener newOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_rank1:
				onRankNewCheckChange(RankBaseFragment.RANK_NEW_FEMALE_TYPE);
				mTitleRight.setText(getString(R.string.beauty));
				break;
			case R.id.tv_rank2:
				onRankNewCheckChange(RankBaseFragment.RANK_NEW_MALE_TYPE);
				mTitleRight.setText(getString(R.string.handsome));
				break;
			}
			dismissNewPopWindow();
		}
	};

	private void onRankNewCheckChange(int index) {
		if (rankNewFragment == null) {
			rankNewFragment = (RankNewFragment) adapter.getFt();
		}
		rankNewFragment.onCheckedChanged(index);
	}

	private OnClickListener glamourOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_rank_day:
				onRankGlamourCheckChange(RankBaseFragment.RANK_DAY_TYPE);
				mTitleRight.setText(getString(R.string.day_rank));
				break;
			case R.id.tv_rank_week:
				onRankGlamourCheckChange(RankBaseFragment.RANK_WEEK_TYPE);
				mTitleRight.setText(getString(R.string.week_rank));
				break;
			case R.id.tv_rank_all:
				onRankGlamourCheckChange(RankBaseFragment.RANK_TOTAL_TYPE);
				mTitleRight.setText(getString(R.string.final_rank));
				break;
			}
			dismissGlamourPopWindow();
		}
	};

	private void onRankGlamourCheckChange(int index) {
		if (rankGlamourFragment == null) {
			rankGlamourFragment = (RankFemaleFragment) adapter.getFt();
		}
		rankGlamourFragment.onCheckedChanged(index);
	}

	private OnClickListener richOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_rank_day:
				onRankRichCheckChange(RankBaseFragment.RANK_DAY_TYPE);
				mTitleRight.setText(getString(R.string.day_rank));
				break;
			case R.id.tv_rank_week:
				onRankRichCheckChange(RankBaseFragment.RANK_WEEK_TYPE);
				mTitleRight.setText(getString(R.string.week_rank));
				break;
			case R.id.tv_rank_all:
				onRankRichCheckChange(RankBaseFragment.RANK_TOTAL_TYPE);
				mTitleRight.setText(getString(R.string.final_rank));
				break;
			}
			dismissRichPopWindow();
		}
	};

	private void onRankRichCheckChange(int index) {
		if (rankMaleFragment == null) {
			rankMaleFragment = (RankMaleFragment) adapter.getFt();
		}
		rankMaleFragment.onCheckedChanged(index);
	}

	private OnClickListener gameOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_rank1:
				onRankGameCheckChange(RankBaseFragment.RANK_NEW_FEMALE_TYPE);
				mTitleRight.setText(getString(R.string.str_game_week_rank));
				break;
			case R.id.tv_rank2:
				onRankGameCheckChange(RankBaseFragment.RANK_NEW_MALE_TYPE);
				mTitleRight.setText(getString(R.string.str_harem_week_rank));
				break;
			}
			dismissGamePopWindow();
		}
	};

	private void onRankGameCheckChange(int index) {
		if (rankGameFragment == null) {
			rankGameFragment = (RankGameFragment) adapter.getFt();
		}

		rankGameFragment.onCheckedChanged(index);
	}

	private void showGlamourPopWindow() {
		if (popupGlamourWindow != null)
			popupGlamourWindow.showAsDropDown(mTitleRight, -40, 12);
	}

	private void dismissGlamourPopWindow() {
		if (popupGlamourWindow != null)
			popupGlamourWindow.dismiss();
	}

	private void showNewPopWindow() {
		if (popupNewWindow != null)
			popupNewWindow.showAsDropDown(mTitleRight, -40, 12);
	}

	private void dismissNewPopWindow() {
		if (popupNewWindow != null)
			popupNewWindow.dismiss();
	}

	private void showRichPopWindow() {
		if (popupRichWindow != null)
			popupRichWindow.showAsDropDown(mTitleRight, -40, 12);
	}

	private void dismissRichPopWindow() {
		if (popupRichWindow != null)
			popupRichWindow.dismiss();
	}

	private void showGamePopWindow() {
		if (popupGameWindow != null)
			popupGameWindow.showAsDropDown(mTitleRight, -40, 12);
	}

	private void dismissGamePopWindow() {
		if (popupGameWindow != null)
			popupGameWindow.dismiss();
	}

	private void setListener() {
		findViewById(R.id.title_tv_left).setOnClickListener(this);
		mTitleRight.setOnClickListener(this);
		mTextNew.setOnClickListener(this);
		mTextQueen.setOnClickListener(this);
		mTextKing.setOnClickListener(this);
		mTextGame.setOnClickListener(this);
	}

	private void setViewPageAndUnderline() {
		adapter = new RankFragmenttAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOffscreenPageLimit(4);
		mViewPager.setOnPageChangeListener(this);
	}

	@Override
	protected int initView() {
		return R.layout.activity_rank;
	}

	public static void openMineFaqActivity(Activity activity) {
		BaseUtils.openActivity(activity, MainRankActivity.class);
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

	private void setMainView() {
		switch (mCurrPage) {
		case 0:
			cur_view = SuspensionEntity.ACTIVITY_RANK_NEW;
			break;
		case 1:
			cur_view = SuspensionEntity.ACTIVITY_RANK_FEMALE;
			break;
		case 2:
			cur_view = SuspensionEntity.ACTIVITY_RANK_MALE;
			break;
		case 3:
			cur_view = SuspensionEntity.ACTIVITY_RANK_GAME;
			break;
		}
		setSuspensionView();
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
		case R.id.title_tv_right:
			showPopWindow();
			break;
		case R.id.iv_susp_icon:
			enterInEnter();
			break;
		case R.id.iv_close:
			hideEnter();
			break;
		}
	}

	private void enterInEnter() {
		if (enterOpen) {
			if (actEntify != null) {
				SuspensionActivity.openMineFaqActivity(this, actEntify.getUrl());
			}
		} else {
			showEnter();
		}
	}

	private void showEnter() {
		hideSuspensionEnter(rl_susp, 0);
		enterOpen = true;
	}

	private void hideEnter() {
		hideSuspensionEnter(rl_susp, -32);
		enterOpen = false;
	}

	private void hideSuspensionEnter(View v, int margin) {
		ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
		lp.rightMargin = (int) (density * margin);
		v.setLayoutParams(lp);
	}

	private void setSuspensionView() {
		SuspensionEntity entity = BAApplication.getInstance().getSuspEntity();
		if (entity != null) {
			actEntify = entity.getActEntifyForNumber(cur_view);
			if (actEntify != null) {
				if (actEntify.getStatus() == SuspensionActEntity.SHOW_SUSP_ICON) {
					rl_susp.setVisibility(View.VISIBLE);
					if (!TextUtils.isEmpty(actEntify.getImage())) {
						DisplayImageOptions options = ImageOptionsUtils.GetGroupHeadKeySmallRounded(this);
						imageLoader.displayImage("third://" + actEntify.getImage(), iv_susp_icon, options);
					}
				} else {
					rl_susp.setVisibility(View.GONE);
				}
			} else {
				rl_susp.setVisibility(View.GONE);
			}
		}
	}

	private void showPopWindow() {
		switch (mCurrPage) {
		case 0:
			showNewPopWindow();
			break;
		case 1:
			showGlamourPopWindow();
			break;
		case 2:
			showRichPopWindow();
			break;
		case 3:
			//			showGamePopWindow();
			break;
		}
	}

	private void setTitleResource() {
		if (mCurrPage == 0) {
			mTextNew.setTextColor(getResources().getColor(R.color.peach));
			mTextQueen.setTextColor(getResources().getColor(R.color.gray));
			mTextKing.setTextColor(getResources().getColor(R.color.gray));
			mTextGame.setTextColor(getResources().getColor(R.color.gray));
			mNewViewLine.setVisibility(View.VISIBLE);
			mQueenViewLine.setVisibility(View.GONE);
			mKingViewLine.setVisibility(View.GONE);
			mGameViewLine.setVisibility(View.GONE);
			mTextNew.setBackgroundResource(R.drawable.main_bar_tab1_bg_pr);
			mTextQueen.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mTextKing.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mTextGame.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			setNewRankCheckedItem();
			Drawable drawable = getResources().getDrawable(R.drawable.rank_title_arrow);
			/// 这一步必须要做,否则不会显示.  
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			mTitleRight.setCompoundDrawables(null, null, drawable, null);
		} else if (mCurrPage == 1) {
			mTextNew.setTextColor(getResources().getColor(R.color.gray));
			mTextQueen.setTextColor(getResources().getColor(R.color.peach));
			mTextKing.setTextColor(getResources().getColor(R.color.gray));
			mTextGame.setTextColor(getResources().getColor(R.color.gray));
			mNewViewLine.setVisibility(View.GONE);
			mQueenViewLine.setVisibility(View.VISIBLE);
			mKingViewLine.setVisibility(View.GONE);
			mGameViewLine.setVisibility(View.GONE);
			mTextNew.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mTextQueen.setBackgroundResource(R.drawable.main_bar_tab1_bg_pr);
			mTextKing.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mTextGame.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			setGlamourRankCheckedItem();
			Drawable drawable = getResources().getDrawable(R.drawable.rank_title_arrow);
			/// 这一步必须要做,否则不会显示.  
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			mTitleRight.setCompoundDrawables(null, null, drawable, null);
		} else if (mCurrPage == 2) {
			mTextNew.setTextColor(getResources().getColor(R.color.gray));
			mTextQueen.setTextColor(getResources().getColor(R.color.gray));
			mTextKing.setTextColor(getResources().getColor(R.color.peach));
			mTextGame.setTextColor(getResources().getColor(R.color.gray));
			mNewViewLine.setVisibility(View.GONE);
			mQueenViewLine.setVisibility(View.GONE);
			mKingViewLine.setVisibility(View.VISIBLE);
			mGameViewLine.setVisibility(View.GONE);
			mTextNew.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mTextQueen.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mTextKing.setBackgroundResource(R.drawable.main_bar_tab1_bg_pr);
			mTextGame.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			Drawable drawable = getResources().getDrawable(R.drawable.rank_title_arrow);
			/// 这一步必须要做,否则不会显示.  
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			mTitleRight.setCompoundDrawables(null, null, drawable, null);
		} else {
			mTextNew.setTextColor(getResources().getColor(R.color.gray));
			mTextQueen.setTextColor(getResources().getColor(R.color.gray));
			mTextKing.setTextColor(getResources().getColor(R.color.gray));
			mTextGame.setTextColor(getResources().getColor(R.color.peach));
			mNewViewLine.setVisibility(View.GONE);
			mQueenViewLine.setVisibility(View.GONE);
			mKingViewLine.setVisibility(View.GONE);
			mGameViewLine.setVisibility(View.VISIBLE);
			mTextNew.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mTextQueen.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mTextKing.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mTextGame.setBackgroundResource(R.drawable.main_bar_tab1_bg_pr);
			setGameRankCheckedItem();
			mTitleRight.setCompoundDrawables(null, null, null, null);
		}
	}

	private void setNewRankCheckedItem() {
		if (RankNewFragment.curType == RankBaseFragment.RANK_NEW_FEMALE_TYPE) {
			mTitleRight.setText(getString(R.string.beauty));
		} else {
			mTitleRight.setText(getString(R.string.handsome));
		}
	}

	private void setGlamourRankCheckedItem() {
		switch (RankFemaleFragment.curType) {
		case RankBaseFragment.RANK_DAY_TYPE:
			mTitleRight.setText(getString(R.string.day_rank));
			break;
		case RankBaseFragment.RANK_WEEK_TYPE:
			mTitleRight.setText(getString(R.string.week_rank));
			break;
		case RankBaseFragment.RANK_TOTAL_TYPE:
			mTitleRight.setText(getString(R.string.final_rank));
			break;
		}
	}

	private void setRichRankCheckedItem() {
		switch (RankMaleFragment.curType) {
		case RankBaseFragment.RANK_DAY_TYPE:
			mTitleRight.setText(getString(R.string.day_rank));
			break;
		case RankBaseFragment.RANK_WEEK_TYPE:
			mTitleRight.setText(getString(R.string.week_rank));
			break;
		case RankBaseFragment.RANK_TOTAL_TYPE:
			mTitleRight.setText(getString(R.string.final_rank));
			break;
		}
	}

	private void setGameRankCheckedItem() {
		if (RankGameFragment.curType == RankBaseFragment.RANK_NEW_FEMALE_TYPE) {
			mTitleRight.setText(getString(R.string.str_game_week_rank));
		} else {
			mTitleRight.setText(getString(R.string.str_harem_week_rank));
		}
	}
}
