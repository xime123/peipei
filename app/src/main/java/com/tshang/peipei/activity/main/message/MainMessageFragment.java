package com.tshang.peipei.activity.main.message;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.dialog.HintToastDialog;
import com.tshang.peipei.activity.dialog.SelectSilverGiftDialog;
import com.tshang.peipei.activity.main.BaseFragment;
import com.tshang.peipei.activity.main.message.adapter.MainMessageFragmentAdapter;
import com.tshang.peipei.activity.mine.MineBlackListActivity;
import com.tshang.peipei.activity.mine.MineFaqActivity;
import com.tshang.peipei.activity.mine.MineWriteActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.chat.ChatRecordBiz;
import com.tshang.peipei.model.biz.chat.ChatSessionManageBiz;
import com.tshang.peipei.model.biz.space.SkillUtilsBiz;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestGetGiftList.IGetGiftListCallBack;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfoList;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.operate.PeipeiSessionOperate;

import de.greenrobot.event.EventBus;

public class MainMessageFragment extends BaseFragment implements OnPageChangeListener, IGetGiftListCallBack {

	private ViewPager mViewPager;
	private TextView tv_message;
	private TextView tv_friends;
	private ImageView iv_message;
	private ImageView iv_friends_new, iv_message_new;
	private MainMessageFragmentAdapter adapter;
	private PopupWindow popWindow;
	private ProgressBar progressBar;

	private int mMessageCurrPage = 0;// 当前页卡编号

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		setUserVisibleHint(true);
		super.onActivityCreated(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_message, null);
		initUi(view);
		setTitleResource();
		showLoadOffLineMessageProgress();
		return view;
	}

	private void showLoadOffLineMessageProgress() {
		if (ChatRecordBiz.isOnLineMessageStart) {
			progressBar.setVisibility(View.VISIBLE);
		} else {
			progressBar.setVisibility(View.GONE);
		}
	}

	public void onEvent(NoticeEvent event) {
		if (event.getFlag() == NoticeEvent.NOTICE70) {
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.CHAT_OFFLINE_MESSAGE_START_VALUE);
		} else if (event.getFlag() == NoticeEvent.NOTICE71) {
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.CHAT_OFFLINE_MESSAGE_END_VALUE);
		}
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.GIFT_LIST_SUCCESS_VALUE:
			GiftInfoList giftInfoList = (GiftInfoList) msg.obj;
			if (giftInfoList != null) {
				SelectSilverGiftDialog dialog = new SelectSilverGiftDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, giftInfoList,
						mHandler);
				dialog.showDialog(0, 0);
			}
			break;
		case HandlerValue.GIFT_CHAT_LIMIT_SUCCESS_VALUE:
			BaseUtils.showTost(getActivity(), R.string.str_set_success);
			break;
		case HandlerValue.GIFT_CHAT_LIMIT_FAILED_VALUE:
			new HintToastDialog(getActivity(), R.string.str_loyalty_failed, R.string.i_know2).showDialog();
			break;
		case HandlerValue.CHAT_OFFLINE_MESSAGE_START_VALUE:
			progressBar.setVisibility(View.VISIBLE);
			break;
		case HandlerValue.CHAT_OFFLINE_MESSAGE_END_VALUE:
			progressBar.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_messge:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.tv_friends:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.tv_private_chat:
			BaseUtils.hidePopupWindow(popWindow);
			BaseUtils.showDialog(getActivity(), R.string.loading);
			new SkillUtilsBiz(getActivity()).getGiftList(this);
			break;
		case R.id.tv_to_read://置为已读
			BaseUtils.hidePopupWindow(popWindow);
			PeipeiSessionOperate ppOperate = PeipeiSessionOperate.getInstance(getActivity());
			int count = ppOperate.selectUnreadCount();
			if (count > 0) {
				ppOperate.cleanUnreadCount();
				NoticeEvent noticeEvent = new NoticeEvent();
				noticeEvent.setFlag(NoticeEvent.NOTICE68);
				EventBus.getDefault().postSticky(noticeEvent);
				setTitleNum();
			} else {
				BaseUtils.showTost(getActivity(), "没有未读消息");
			}

			break;
		case R.id.tv_my_blacklist:
			BaseUtils.hidePopupWindow(popWindow);
			BaseUtils.openActivity(getActivity(), MineBlackListActivity.class);
			break;
		case R.id.iv_message://右上角按钮
			if (mViewPager.getCurrentItem() == 0) {
				if (popWindow == null || !popWindow.isShowing()) {
					initPopuptWindow();
					popWindow.showAtLocation(mViewPager, Gravity.TOP, 0, 0);
				}
			} else {
				MineFaqActivity.openMineFaqActivity(getActivity(), MineFaqActivity.SEARCH_VALUE);
			}
			break;
		}
	}

	/**
	 * 弹出筛选框
	 * @author jeff
	 *
	 */
	private void initPopuptWindow() {//广播悬浮框选择
		// 获取自定义布局文件xml的视图
		View popview = getActivity().getLayoutInflater().inflate(R.layout.popupwindow_message, null, false);
		popWindow = new PopupWindow(popview, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popWindow.setTouchable(true);
		popWindow.setOutsideTouchable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
		popWindow.getContentView().setFocusableInTouchMode(true);
		popWindow.getContentView().setFocusable(true);
		// 设置动画效果
		popWindow.setAnimationStyle(R.style.anim_popwindow_alpha);
		popview.findViewById(R.id.tv_private_chat).setOnClickListener(this);
		popview.findViewById(R.id.tv_my_blacklist).setOnClickListener(this);
		popview.findViewById(R.id.tv_to_read).setOnClickListener(this);

		popview.findViewById(R.id.rl_main_message_root).setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				BaseUtils.hidePopupWindow(popWindow);
				return false;
			}
		});

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (this.isVisible()) {
			if (isVisibleToUser) {
				setTitleNum();
				showLoadOffLineMessageProgress();
			}
		}

		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		setTitleNum();
	}

	@Override
	public void onResume() {
		setTitleNum();
		super.onResume();
	}

	private void setTitleNum() {
		int unread = SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey(
				BAConstants.PEIPEI_DYNAMIC_UNREAD_NUM, 0);
		int fansNum = SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey(
				BAConstants.PEIPEI_FANS_UNREAD_NUM, 0);
		int messagenum = ChatSessionManageBiz.isExistUnreadMessage(getActivity()); //刷新界面
		if(mMessageCurrPage == 0){
			if (fansNum > 0) {
				iv_friends_new.setVisibility(View.VISIBLE);
			} else {
				iv_friends_new.setVisibility(View.GONE);
			}
		}else{
			iv_friends_new.setVisibility(View.GONE);
		}
		
		if(mMessageCurrPage == 1){
			if (messagenum > 0) {
				iv_message_new.setVisibility(View.VISIBLE);
			} else {
				iv_message_new.setVisibility(View.GONE);
			}
		}else{
			iv_message_new.setVisibility(View.GONE);
		}
	}

	private void initUi(View view) {
		mViewPager = (ViewPager) view.findViewById(R.id.message_viewpager);

		tv_message = (TextView) view.findViewById(R.id.tv_messge);
		tv_message.setOnClickListener(this);
		tv_friends = (TextView) view.findViewById(R.id.tv_friends);
		tv_friends.setOnClickListener(this);
		iv_friends_new = (ImageView) view.findViewById(R.id.tv_friends_new);
		iv_message_new = (ImageView) view.findViewById(R.id.tv_message_new);

		progressBar = (ProgressBar) view.findViewById(R.id.pg_receive_offline_message);

		setViewPageAndUnderline(view);

		iv_message = (ImageView) view.findViewById(R.id.iv_message);
		iv_message.setOnClickListener(this);
	}

	private void setViewPageAndUnderline(View view) {
		adapter = new MainMessageFragmentAdapter(getFragmentManager());
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setOnPageChangeListener(this);
	}

	private void setTitleResource() {
		if (mMessageCurrPage == 0) {
			iv_message.setImageResource(R.drawable.message_icon_more_un);
			tv_message.setTextColor(getResources().getColor(R.color.white));
			tv_message.setBackgroundResource(R.drawable.main_bar_tab_left_pr);
			tv_friends.setTextColor(getResources().getColor(R.color.main_bottom_bg_pre));
			tv_friends.setBackgroundResource(R.drawable.main_bar_tab_right_un);
		} else {
			iv_message.setImageResource(R.drawable.homepage_myfried_nav_search);
			tv_friends.setTextColor(getResources().getColor(R.color.white));
			tv_friends.setBackgroundResource(R.drawable.main_bar_tab_right_pr);
			tv_message.setTextColor(getResources().getColor(R.color.main_bottom_bg_pre));
			tv_message.setBackgroundResource(R.drawable.main_bar_tab_left_un);
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
		mMessageCurrPage = arg0;
		setTitleResource();
		setTitleNum();
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {//被销毁了数据恢复
		super.onViewStateRestored(savedInstanceState);
		if (savedInstanceState != null) {
			mMessageCurrPage = savedInstanceState.getInt("mMessageCurrPage");
			mViewPager.setCurrentItem(mMessageCurrPage);

		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {//销毁了数据保存
		super.onSaveInstanceState(outState);
		if (outState != null) {
			outState.putInt("mMessageCurrPage", mMessageCurrPage);
		}
	}

	@Override
	public void getGiftListCallBack(int retCode, GiftInfoList giftInfoList) {
		if (retCode == 0) {
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.GIFT_LIST_SUCCESS_VALUE, giftInfoList);
		} else {
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.GIFT_LIST_FAILED_VALUE);
		}
	}

}
