package com.tshang.peipei.activity.space;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.mine.MineWriteActivity;
import com.tshang.peipei.activity.space.adapter.DynamicFragmentAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.model.event.NoticeEvent;

import de.greenrobot.event.EventBus;

/**
 * @Title: DynamicActivity.java 
 *
 * @Description: 动态界面
 *
 * @author DYH  
 *
 * @date 2015-10-16 下午5:10:24 
 *
 * @version V1.0   
 */
public class DynamicActivity extends BaseActivity implements OnPageChangeListener {

	private ViewPager mViewPager;
	private TextView allDynamic, meDynamic;
	private TextView aboutMeTv;
	private LinearLayout aboutMeLayout;
	private ImageView aboutMeTic;
	private DynamicFragmentAdapter adapter;
	private ImageView mRightView;
	private int mCurrPage = 0;// 当前页卡编号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initRecourse() {
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(getString(R.string.str_dynamic));
		mViewPager = (ViewPager) findViewById(R.id.dynamic_viewpager);
		mRightView = (ImageView) findViewById(R.id.title_iv_right);
		mRightView.setImageResource(R.drawable.broadcast_icon_write);
		mRightView.setVisibility(View.VISIBLE);
		allDynamic = (TextView) findViewById(R.id.dynamic_ma_all_tv);
		meDynamic = (TextView) findViewById(R.id.dynamic_ma_me_tv);
		aboutMeLayout = (LinearLayout) findViewById(R.id.dynamic_ma_about_me_layout);
		aboutMeTic = (ImageView) findViewById(R.id.dynamic_ma_about_me_tip_number_tv);
		aboutMeTv = (TextView) findViewById(R.id.dynamic_ma_about_me_tv);

		allDynamic.setOnClickListener(this);
		meDynamic.setOnClickListener(this);
		aboutMeLayout.setOnClickListener(this);
		

		aboutMeTic.setVisibility(View.INVISIBLE);
		clickAllDy();
		setViewPageAndUnderline();
		setListener();
	}

	private void setListener() {
		findViewById(R.id.title_tv_left).setOnClickListener(this);
		mRightView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.dynamic_ma_all_tv:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.dynamic_ma_me_tv:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.dynamic_ma_about_me_layout:
			mViewPager.setCurrentItem(2);
			//取消提示
			NoticeEvent event = new NoticeEvent();
			event.setFlag(NoticeEvent.NOTICE88);
			EventBus.getDefault().post(event);
			break;
		case R.id.title_iv_right:
			Bundle bundle = new Bundle();
			bundle.putString(MineWriteActivity.FROM_FLAG, MineWriteActivity.PUBLIC_DYNAMIC);
			BaseUtils.openActivity(this, MineWriteActivity.class, bundle);
			break;
		case R.id.title_lin_right:
			
			break;
		}
	}

	private void setViewPageAndUnderline() {
		adapter = new DynamicFragmentAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setOnPageChangeListener(this);
	}

	public static void openMineFaqActivity(Activity activity) {
		BaseUtils.openActivity(activity, DynamicActivity.class);
	}

	@Override
	protected int initView() {
		return R.layout.activity_all_dynamic;
	}

	private void setTitleResource() {
		if (mCurrPage == 0) {
			clickAllDy();
		} else if (mCurrPage == 1) {
			clickMeDy();
		} else {
			clickAboutMe();
		}
	}

	private void clickAllDy() {
		allDynamic.setTextColor(getResources().getColor(R.color.peach));
		meDynamic.setTextColor(getResources().getColor(R.color.gray));
		aboutMeTv.setTextColor(getResources().getColor(R.color.gray));
		allDynamic.setBackgroundResource(R.drawable.main_bar_tab1_bg_pr);
		meDynamic.setBackgroundResource(R.drawable.main_bar_tab1_bg_on);
		aboutMeLayout.setBackgroundResource(R.drawable.main_bar_tab1_bg_on);
	}

	private void clickMeDy() {
		allDynamic.setTextColor(getResources().getColor(R.color.gray));
		meDynamic.setTextColor(getResources().getColor(R.color.peach));
		aboutMeTv.setTextColor(getResources().getColor(R.color.gray));
		allDynamic.setBackgroundResource(R.drawable.main_bar_tab1_bg_on);
		meDynamic.setBackgroundResource(R.drawable.main_bar_tab1_bg_pr);
		aboutMeLayout.setBackgroundResource(R.drawable.main_bar_tab1_bg_on);
	}

	private void clickAboutMe() {
		allDynamic.setTextColor(getResources().getColor(R.color.gray));
		meDynamic.setTextColor(getResources().getColor(R.color.gray));
		aboutMeTv.setTextColor(getResources().getColor(R.color.peach));
		allDynamic.setBackgroundResource(R.drawable.main_bar_tab1_bg_on);
		meDynamic.setBackgroundResource(R.drawable.main_bar_tab1_bg_on);
		aboutMeLayout.setBackgroundResource(R.drawable.main_bar_tab1_bg_pr);
	}

	public void onEvent(NoticeEvent event) {
		switch (event.getFlag()) {
		case NoticeEvent.NOTICE88://取消
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					aboutMeTic.setVisibility(View.INVISIBLE);
				}
			});
			break;
		case NoticeEvent.NOTICE89:
		case NoticeEvent.NOTICE87://提示
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					aboutMeTic.setVisibility(View.VISIBLE);
				}
			});
			break;
		default:
			break;
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
	}

}
