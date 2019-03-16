package com.tshang.peipei.activity.reward;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.chat.ChatActivity;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.reward.adapter.RewardAllAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants.RewardType;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.reward.RewardRequestControl;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestRewardChat.GetRewardChatCallBack;
import com.tshang.peipei.model.request.RequestRewardListInfo.GetRewardListInfoCallBack;
import com.tshang.peipei.protocol.asn.gogirl.AwardDetail;
import com.tshang.peipei.protocol.asn.gogirl.AwardDetailList;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

import de.greenrobot.event.EventBus;

/**
 * @Title: RewardListActivity.java 
 *
 * @Description: 所有悬赏列表 
 *
 * @author Aaron  
 *
 * @date 2015-9-28 下午4:17:09 
 *
 * @version V1.0   
 */
public class RewardListActivity extends BaseActivity implements GetRewardListInfoCallBack, OnRefreshListener2<ListView>, OnItemClickListener,
		GetRewardChatCallBack {

	private final String TAG = this.getClass().getSimpleName();

	public static String LABLE_FLAG = "lable";

	public static int CHAT_FROM_REWARD = 901;

	private TextView backTV, titleTV;
	private LinearLayout rightLayout;
	private PullToRefreshListView mPullToRefreshListView;
	private LinearLayout addRewardLayout;
	private TextView emptyTv;
	private TextView allReward, processReward, endReward;

	private RewardAllAdapter adapter;

	private PopupWindow popWindow;

	private RewardRequestControl control;

	protected static final int LOADCOUNT = 10;
	protected int startLoadPosition = -1;

	private boolean isRefresh = true;

	private int type = 0;

	private Dialog dialog;

	private AwardDetail entity;

	private AnonymNickDialog anonymNickDialog;

	private int anonymNickId;
	private String anonymNick;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		control = new RewardRequestControl();
		anonymNickDialog = new AnonymNickDialog(this);
		anonymNickDialog.bindAnonymNick();
	}

	@Override
	protected void initData() {
		type = getIntent().getExtras().getInt(LABLE_FLAG, 0);
		if (type == RewardType.ALL.getValue()) {
			titleTV.setText(R.string.reward_all);
		} else if (type == RewardType.PROCCED.getValue()) {
			titleTV.setText(R.string.reward_progress);
		} else if (type == RewardType.END.getValue()) {
			titleTV.setText(R.string.reward_end);
		}
	}

	@Override
	protected void initRecourse() {
		backTV = (TextView) findViewById(R.id.reward_title_tv_left);
		titleTV = (TextView) findViewById(R.id.reward_title_tv_mid);
		//		rightTV = (TextView) findViewById(R.id.reward_title_tv_right);
		rightLayout = (LinearLayout) findViewById(R.id.reward_title_lin_right);

		backTV.setOnClickListener(this);
		titleTV.setOnClickListener(this);
		rightLayout.setOnClickListener(this);

		emptyTv = (TextView) findViewById(R.id.reward_all_not_data_tv);
		addRewardLayout = (LinearLayout) findViewById(R.id.reward_add_layout);
		addRewardLayout.setOnClickListener(this);

		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.reward_all_listview);
		mPullToRefreshListView.setMode(Mode.BOTH);
		mPullToRefreshListView.setOnRefreshListener(this);
		mPullToRefreshListView.getRefreshableView().setOnItemClickListener(this);

		adapter = new RewardAllAdapter(this, mHandler, type);
		mPullToRefreshListView.setAdapter(adapter);

	}

	@Override
	protected int initView() {
		return R.layout.activity_reward_all;
	}

	private void getRewardListInfo(int anonymNickId, int type, int start, int num) {
		control.requestListInfo(anonymNickId, type, start, num, this);
	}

	@SuppressLint("InflateParams")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.GET_REWARD_LIST_SUCCESS://获取悬赏列表成功
			mPullToRefreshListView.onRefreshComplete();
			if (msg.arg1 == 0) {
				AwardDetailList detailLists = (AwardDetailList) msg.obj;
				if (isRefresh) {
					adapter.clearList();
					mPullToRefreshListView.setMode(Mode.BOTH);
				}

				List<AwardDetail> list = new ArrayList<AwardDetail>();
				for (int i = detailLists.size() - 1; i >= 0; i--) {
					list.add((AwardDetail) detailLists.get(i));
				}
				adapter.appendToList(list);

				if (detailLists.size() <= 0) {
					if (!isRefresh) {
						BaseUtils.showTost(this, R.string.namypic_data_null);
						mPullToRefreshListView.setMode(Mode.PULL_FROM_START);
					} else {
						mPullToRefreshListView.setEmptyView(emptyTv);
					}
				}
			} else {
				BaseUtils.showTost(this, R.string.get_reward_list_failure);
			}
			adapter.setType(type);
			adapter.setAnonymNickId(anonymNickDialog.getAnonymNickId());
			break;
		case HandlerValue.GET_REWARD_LIST_ERROR://获取悬赏列表失败
			mPullToRefreshListView.onRefreshComplete();
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		case HandlerValue.JOIN_REWARD_SUCCESS://参加悬赏成功
			if (msg.arg1 == 0) {
				entity = (AwardDetail) msg.obj;
				View view = LayoutInflater.from(this).inflate(R.layout.reward_chat_confirm_dialog_layout, null);
				dialog = DialogFactory.showMsgDialog(this, null, view, getResources().getString(R.string.chat), null, null, new OnClickListener() {

					@Override
					public void onClick(View v) {
						DialogFactory.dimissDialog(dialog);
						dialog = DialogFactory.createLoadingDialog(RewardListActivity.this, R.string.reward_req_chat);
						DialogFactory.showDialog(dialog);
						control.requestChat(type, entity.uid.intValue(), BAApplication.mLocalUserInfo.uid.intValue(), entity.id.intValue(),
								RewardListActivity.this);
					}
				});
			} else if (msg.arg1 == -28096) {
				BaseUtils.showTost(this, R.string.toast_reward_end);
			} else if (msg.arg1 == -28094) {
				BaseUtils.showTost(this, R.string.toast_reward_full);
			} else if (msg.arg1 == -28010) {//登录超时
				NoticeEvent event = new NoticeEvent();
				event.setFlag(NoticeEvent.NOTICE27);
				EventBus.getDefault().post(event);
			} else if (msg.arg1 == -28101) {
				BaseUtils.showTost(this, R.string.toast_reward_join_forbid);
			} else if (msg.arg1 == -28095) {
				BaseUtils.showTost(this, "你已经参加过该悬赏");
			} else {
				BaseUtils.showTost(this, R.string.join_reward_failure);
			}

			break;
		case HandlerValue.JOIN_REWARD_ERROR://参加悬赏失败
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		case HandlerValue.REWARD_CHAT_SUCCESS://私聊回调成功
			if (msg.arg1 == 0) {
				entity = (AwardDetail) msg.obj;
				if (dialog != null) {
					DialogFactory.dimissDialog(dialog);
				}
				if (entity == null) {
					entity = (AwardDetail) msg.obj;
				}
				if (entity == null) {
					return;
				}
				if (entity.isanonymous.intValue() == 1) {
					ChatActivity.intentChatActivity(this, entity.uid.intValue(), new String(entity.nick), entity.sex.intValue(), false, false,
							RewardListActivity.CHAT_FROM_REWARD);
				} else {
					ChatActivity.intentChatActivity(this, entity.uid.intValue(), new String(entity.nick), entity.sex.intValue(), false, false, 0);
				}
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						mPullToRefreshListView.setRefreshing();
					}
				}, 1000);

			} else if (msg.arg1 == -28010) {
				NoticeEvent event = new NoticeEvent();
				event.setFlag(NoticeEvent.NOTICE27);
				EventBus.getDefault().post(event);
			} else {
				BaseUtils.showTost(this, R.string.reard_chat_toast_error);
			}
			break;
		case HandlerValue.REWARD_CHAT_ERROR://私聊回调失败
			DialogFactory.dimissDialog(dialog);
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.reward_title_tv_left:
			this.finish();
			break;
		case R.id.reward_title_tv_mid:
			if (mPullToRefreshListView.isRefreshing()) {
				return;
			}
			initPopupwidown();
			break;
		case R.id.reward_title_lin_right:
			Bundle bundle1 = new Bundle();
			bundle1.putInt("anonymNickId", anonymNickId);
			BaseUtils.openActivity(this, MineRewardActivity.class, bundle1);
			break;
		case R.id.reward_add_layout:
			Bundle bundle = new Bundle();
			bundle.putInt("nickId", anonymNickId);
			bundle.putString("nick", anonymNick);
			BaseUtils.openActivity(this, PublishRewardActivity.class, bundle);
			break;
		case R.id.reward_popupwidown_layout:
			BaseUtils.hidePopupWindow(popWindow);
			break;
		case R.id.all_reward_tv://全部悬赏
			BaseUtils.hidePopupWindow(popWindow);
			type = 0;
			titleTV.setText(R.string.reward_all);
			mPullToRefreshListView.setRefreshing();
			break;
		case R.id.proceed_reward_tv://进行中的悬赏
			BaseUtils.hidePopupWindow(popWindow);
			type = 1;
			titleTV.setText(R.string.reward_progress);
			mPullToRefreshListView.setRefreshing();
			break;
		case R.id.end_reward_tv://已结束的悬赏
			BaseUtils.hidePopupWindow(popWindow);
			type = 2;
			titleTV.setText(R.string.reward_end);
			mPullToRefreshListView.setRefreshing();
			break;

		default:
			break;
		}
	}

	/**
	 * 初始化popupwidown
	 * @author Aaron
	 *
	 */
	@SuppressLint("InflateParams")
	private void initPopupwidown() {
		View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_reward_layout, null);
		popWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popWindow.setTouchable(true);
		popWindow.setOutsideTouchable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
		popWindow.getContentView().setFocusableInTouchMode(true);
		popWindow.getContentView().setFocusable(true);

		// 设置动画效果
		popWindow.setAnimationStyle(R.style.anim_popwindow_alpha);

		view.findViewById(R.id.reward_popupwidown_layout).setOnClickListener(this);
		allReward = (TextView) view.findViewById(R.id.all_reward_tv);
		processReward = (TextView) view.findViewById(R.id.proceed_reward_tv);
		endReward = (TextView) view.findViewById(R.id.end_reward_tv);

		if (type == RewardType.ALL.getValue()) {
			allReward.setTextColor(getResources().getColor(R.color.red));
			processReward.setTextColor(getResources().getColor(R.color.dialog_title));
			endReward.setTextColor(getResources().getColor(R.color.dialog_title));
		} else if (type == RewardType.PROCCED.getValue()) {
			allReward.setTextColor(getResources().getColor(R.color.dialog_title));
			processReward.setTextColor(getResources().getColor(R.color.red));
			endReward.setTextColor(getResources().getColor(R.color.dialog_title));
		} else if (type == RewardType.END.getValue()) {
			allReward.setTextColor(getResources().getColor(R.color.dialog_title));
			processReward.setTextColor(getResources().getColor(R.color.dialog_title));
			endReward.setTextColor(getResources().getColor(R.color.red));
		}

		allReward.setOnClickListener(this);
		processReward.setOnClickListener(this);
		endReward.setOnClickListener(this);

		popWindow.showAsDropDown(findViewById(R.id.reward_title_layout), 0, 0);
	}

	@Override
	public void onGetRewardListInfoSuccess(int code, int isend, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.GET_REWARD_LIST_SUCCESS, code, isend, obj);
	}

	@Override
	public void onGetRewardListInfoError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.GET_REWARD_LIST_ERROR, code);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = true;
		startLoadPosition = -1;
		getRewardListInfo(anonymNickId, type, startLoadPosition, LOADCOUNT);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = false;
		startLoadPosition = startLoadPosition - LOADCOUNT;
		getRewardListInfo(anonymNickId, type, startLoadPosition, LOADCOUNT);
	}

	@Override
	public void onEvent(final NoticeEvent event) {
		super.onEvent(event);
		switch (event.getFlag()) {
		case NoticeEvent.NOTICE94:
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					type = 0;
					mPullToRefreshListView.setRefreshing();
				}
			});
			break;
		case NoticeEvent.NOTICE98://匿名绑定成功
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					anonymNickId = event.getNum();
					anonymNick = event.getObj().toString();
					mPullToRefreshListView.setRefreshing();
				}
			}, 600);
			break;
		case NoticeEvent.NOTICE100://匿名悬赏开关
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					anonymNickId = event.getNum();
					mPullToRefreshListView.setRefreshing();
				}
			}, 600);

			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	public void onChatSuccess(int code) {
		sendHandlerMessage(mHandler, HandlerValue.REWARD_CHAT_SUCCESS, code);
	}

	@Override
	public void onChatError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.REWARD_CHAT_ERROR, code);
	}
}
