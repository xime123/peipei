package com.tshang.peipei.activity.main.message;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.main.BaseFragment;
import com.tshang.peipei.model.event.NoticeEvent;

import de.greenrobot.event.EventBus;

/**
 * deprecated
 * 
 * @Title: DynamicFragmentAll.java 
 *
 * @Description: 动态 管理类
 *
 * @author Aaron  
 *
 * @date 2015-8-12 上午9:55:10 
 *
 * @version V1.0   
 */
@SuppressLint("InflateParams")
public class DynamicFragmentManage extends BaseFragment {

	private final String TAG = "Aaron";

	private TextView allDynamic, meDynamic;
	private TextView aboutMeTv;
	private LinearLayout aboutMeLayout;
	private ImageView aboutMeTic;

	private DynamicFragmentAll mDynamicFragmentAll;
	private DynamicFragmentMe mDynamicFragmentMe;
	private DynamicFragmentAbout mDynamicFragmentAbout;

	//	private FragmentTransaction transaction;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDynamicFragmentAll = new DynamicFragmentAll();
		mDynamicFragmentMe = new DynamicFragmentMe();
		mDynamicFragmentAbout = new DynamicFragmentAbout();
		EventBus.getDefault().register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dynamic_manage_layout, null);
		findViewById(view);
		return view;
	}

	private void findViewById(View v) {
		allDynamic = (TextView) v.findViewById(R.id.dynamic_ma_all_tv);
		meDynamic = (TextView) v.findViewById(R.id.dynamic_ma_me_tv);
		aboutMeLayout = (LinearLayout) v.findViewById(R.id.dynamic_ma_about_me_layout);
		aboutMeTic = (ImageView) v.findViewById(R.id.dynamic_ma_about_me_tip_number_tv);
		aboutMeTv = (TextView) v.findViewById(R.id.dynamic_ma_about_me_tv);

		allDynamic.setOnClickListener(this);
		meDynamic.setOnClickListener(this);
		aboutMeLayout.setOnClickListener(this);

		aboutMeTic.setVisibility(View.INVISIBLE);

		clickAllDy();
		addFragment(mDynamicFragmentAll, "all");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dynamic_ma_all_tv:
			clickAllDy();
			addFragment(mDynamicFragmentAll, "all");
			break;
		case R.id.dynamic_ma_me_tv:
			clickMeDy();
			addFragment(mDynamicFragmentMe, "me");
			break;
		case R.id.dynamic_ma_about_me_layout:
			clickAboutMe();
			//			aboutMeTic.setVisibility(View.INVISIBLE);
			addFragment(mDynamicFragmentAbout, "about");
			//取消提示
			NoticeEvent event = new NoticeEvent();
			event.setFlag(NoticeEvent.NOTICE88);
			EventBus.getDefault().post(event);
			break;

		default:
			break;
		}
	}

	private void addFragment(Fragment fragment, String tag) {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.dynamic_manage_content, fragment);
		transaction.addToBackStack(null);
		transaction.commitAllowingStateLoss();
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
}
