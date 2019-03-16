package com.tshang.peipei.activity.mine;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.model.biz.store.StoreUserBiz;
import com.tshang.peipei.model.biz.user.GetRewardBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetDailyTaskList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetFreshTaskList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetUserProperty;
import com.tshang.peipei.protocol.asn.gogirl.GGTaskInfoList;
import com.tshang.peipei.protocol.asn.gogirl.UserPropertyInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.view.ReplyChildListView;

/**
 * @Title: MineMissionsActivity.java 
 *
 * @Description: 任务大厅 
 *
 * @author allen  
 *
 * @date 2014-7-18 上午11:27:49 
 *
 * @version V1.0   
 */
public class MineMissionsActivity extends BaseActivity implements BizCallBackGetUserProperty, BizCallBackGetFreshTaskList,
		BizCallBackGetDailyTaskList {

	private final int GETFRESHTASK = 1;
	private final int GETDAILYTASK = 2;

	private TextView mMissionGold, mMissionSilver;
	private LinearLayout mNoviciateLayout, mDailyLayout;
	private ReplyChildListView mNoviciateListView, mDailyListView;
	private TextView mMissionNever;
	private ImageView ivMissionNew;

	private MineMissionsFreshAdapter freshAdapter, dailyAdapter;

	@Override
	protected void initData() {
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			String from = bundle.getString("from");
			if (!TextUtils.isEmpty(from) && from.equals("0")) {
				mBackText.setText(R.string.hall);
			}
		}

		if (null != BAApplication.mLocalUserInfo) {
			StoreUserBiz.getInstance().getUserProperty(this, BAApplication.mLocalUserInfo.uid.intValue(), this);

			GetRewardBiz getRewardBiz = new GetRewardBiz();
			getRewardBiz.getFreshTaskList(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
					BAApplication.mLocalUserInfo.uid.intValue(), 0, 50, this);
			getRewardBiz.getDailyTaskList(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
					BAApplication.mLocalUserInfo.uid.intValue(), 0, 50, this);
		}
	}

	@Override
	protected void initRecourse() {
		ivMissionNew = (ImageView) findViewById(R.id.img_ponits_new);

//		if (!SharedPreferencesTools.getInstance(this).getBooleanKeyValue(BAConstants.PEIPEI_POINTS_WALL)) {
//			ivMissionNew.setVisibility(View.VISIBLE);
//		} else {
			ivMissionNew.setVisibility(View.GONE);
//		}

		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.mine);
		mBackText.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.missions_hall);

		mMissionGold = (TextView) findViewById(R.id.mission_gold);
		mMissionSilver = (TextView) findViewById(R.id.mission_silver);

		mMissionNever = (TextView) findViewById(R.id.mission_never_judges);
		mMissionNever.setText(String.format(getResources().getString(R.string.missions_received_s), 0));
		mMissionNever.setTextColor(getResources().getColor(R.color.gray));
		mMissionNever.setBackgroundResource(R.drawable.person_earn_btn_dis);
		mMissionNever.setPadding(10, 0, 10, 0);

		mNoviciateLayout = (LinearLayout) findViewById(R.id.mission_noviciate_layout);
		mDailyLayout = (LinearLayout) findViewById(R.id.mission_daily_layout);

		mNoviciateListView = (ReplyChildListView) findViewById(R.id.mission_noviciate_listview);
		mDailyListView = (ReplyChildListView) findViewById(R.id.mission_daily_listview);

		freshAdapter = new MineMissionsFreshAdapter(this, false);
		mNoviciateListView.setAdapter(freshAdapter);
		dailyAdapter = new MineMissionsFreshAdapter(this, true);
		mDailyListView.setAdapter(dailyAdapter);

//		findViewById(R.id.ll_mission_domd_points).setOnClickListener(this);
	}

	@Override
	protected int initView() {
		return R.layout.activity_mission;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerType.USER_PROPERTY_BACK:
			if (msg.arg1 == 0) {
				UserPropertyInfo user = (UserPropertyInfo) msg.obj;
				mMissionGold.setText(String.valueOf(user.goldcoin.intValue()));
				mMissionSilver.setText(String.valueOf(user.silvercoin.intValue()));
			}
			break;
		case GETFRESHTASK:
			if (msg.arg1 == 0) {
				GGTaskInfoList list = (GGTaskInfoList) msg.obj;
				if (list.size() > 0) {
					mNoviciateLayout.setVisibility(View.VISIBLE);
					freshAdapter.setList(list);
				} else {
					mNoviciateLayout.setVisibility(View.GONE);
				}
			}
			break;
		case GETDAILYTASK:
			if (msg.arg1 == 0) {
				mMissionNever.setText(String.format(getResources().getString(R.string.missions_received_s), msg.arg2));
				mMissionNever.setTextColor(getResources().getColor(R.color.gray1));
				mMissionNever.setBackgroundResource(R.drawable.my_task_btn_receive_dis);
				mMissionNever.setPadding(10, 0, 10, 0);

				GGTaskInfoList list = (GGTaskInfoList) msg.obj;
				if (list.size() > 0) {
					mDailyLayout.setVisibility(View.VISIBLE);
					dailyAdapter.setList(list);
				} else {
					mDailyLayout.setVisibility(View.GONE);
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
//		case R.id.ll_mission_domd_points:
//			SharedPreferencesTools.getInstance(this).saveBooleanKeyValue(true, BAConstants.PEIPEI_POINTS_WALL);
//			DMOfferWall.getInstance(this).showOfferWall(this);
//			break;
		default:
			break;
		}
	}

	@Override
	public void getUserProperty(int retCode, UserPropertyInfo userPropertyInfo) {
		sendHandlerMessage(mHandler, HandlerType.USER_PROPERTY_BACK, retCode, userPropertyInfo);
	}

	@Override
	public void getFreshTaskListCallBack(int retCode, GGTaskInfoList list) {
		sendHandlerMessage(mHandler, GETFRESHTASK, retCode, list);
	}

	@Override
	public void getDailyTaskListCallBack(int retCode, GGTaskInfoList list, int loginreward) {
		sendHandlerMessage(mHandler, GETDAILYTASK, retCode, loginreward, list);
	}

	//	@Override
	//	public void getRewardBack(int retCode, int type) {
	//		sendHandlerMessage(mHandler, HandlerType.REWARD_TOBACK, retCode, type);
	//	}

}
