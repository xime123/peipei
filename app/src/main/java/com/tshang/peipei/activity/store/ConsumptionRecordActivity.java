package com.tshang.peipei.activity.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDaybookInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDaybookInfoList;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAConstants.NumericalType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.biz.store.RecordBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackDaybook;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

/**
 * @Title: ConsumptionRecordActivity.java 
 *
 * @Description: 消费记录界面
 *
 * @author allen  
 *
 * @date 2014-4-24 上午10:17:21 
 *
 * @version V1.0   
 */
public class ConsumptionRecordActivity extends BaseActivity {

	private static final int GOLD_FLAG = 0;
	private static final int SILVER_FLAG = 1;

	private static final int NUM = 50;

	private int flag = GOLD_FLAG;

	private int mStartGold = -1, mStartSilver = -1;

	private int mTotalGold = 0, mTotalSilver = 0;

	private PullToRefreshListView mRecordGoldListView, mRecordSilverListView;
	private Button mGoldBtn, mSilverBtn;
	private LinearLayout llRecordGold, llRecordSilver;

	private BAHandler mHandler;

	private List<GoGirlDaybookInfo> mListGold = new ArrayList<GoGirlDaybookInfo>();
	private List<GoGirlDaybookInfo> mListSilver = new ArrayList<GoGirlDaybookInfo>();
	private ConsumptionRecordAdapter mGoldAdapter, mSilverAdapter;

	private SilverCallBack mSilverCallBack;
	private GoldCallBack mGoldCallBack = new GoldCallBack();
	private TextView tvEmpty;
	private TextView tvEmptyTwo;
	private static final int DO_NOT_GOLD_LOAD_MORE_DATA = 0x10;
	private static final int DO_NOT_SILVER_LOAD_MORE_DATA = 0x11;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initHandler();
		initUi();

		initListener();

		mHandler.sendEmptyMessage(HandlerType.CREATE_TO_GETDATA);
	}

	private void initHandler() {
		mHandler = new BAHandler(this) {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				try {
					super.handleMessage(msg);
				} catch (Exception e) {
					return;
				}

				switch (msg.what) {
				case HandlerType.CREATE_TO_GETDATA:
					getRecord(true);
					break;
				case HandlerType.GOLD_RECORD_BACK:
					mRecordGoldListView.onRefreshComplete();
					BaseUtils.cancelDialog();
					if (msg.arg1 == 0) {
						GoGirlDaybookInfoList infoList = (GoGirlDaybookInfoList) msg.obj;
						if (infoList != null && infoList.size() > 0) {
							Collections.reverse(infoList);

							for (Object object : infoList) {
								GoGirlDaybookInfo info = (GoGirlDaybookInfo) object;
								if (info.tonum.intValue() != info.fromnum.intValue()) {
									mListGold.add(info);
								}
							}
							if (infoList.size() < NUM) {//加到底了
								mRecordGoldListView.setMode(Mode.PULL_FROM_START);
							} else {//还有数据
								mRecordGoldListView.setMode(Mode.BOTH);
							}
						}
						mGoldAdapter.notifyDataSetChanged();
					}

					break;
				case HandlerType.SILVER_RECORD_BACK:
					mRecordSilverListView.onRefreshComplete();
					BaseUtils.cancelDialog();
					if (msg.arg1 == 0) {
						GoGirlDaybookInfoList infoList = (GoGirlDaybookInfoList) msg.obj;
						if (infoList != null && infoList.size() > 0) {
							Collections.reverse(infoList);
							mListSilver.addAll(infoList);

							if (infoList.size() < NUM) {//加到底了
								mRecordSilverListView.setMode(Mode.PULL_FROM_START);
							} else {//还有数据
								mRecordSilverListView.setMode(Mode.BOTH);
							}
						}
						mSilverAdapter.notifyDataSetChanged();
					}

					break;
				case DO_NOT_GOLD_LOAD_MORE_DATA:
					mRecordGoldListView.onRefreshComplete();
					break;
				case DO_NOT_SILVER_LOAD_MORE_DATA:
					mRecordSilverListView.onRefreshComplete();
					break;
				default:
					break;
				}
			}

		};
	}

	private void initUi() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.consumption_record);

		tvEmpty = (TextView) findViewById(R.id.tv_empty_data);
		tvEmptyTwo = (TextView) findViewById(R.id.tv_empty_data_two);
		llRecordGold = (LinearLayout) findViewById(R.id.ll_gold);
		llRecordSilver = (LinearLayout) findViewById(R.id.ll_silver);

		mGoldBtn = (Button) findViewById(R.id.record_gold_btn);
		mSilverBtn = (Button) findViewById(R.id.record_silver_btn);

		mRecordGoldListView = (PullToRefreshListView) findViewById(R.id.record_consumption_gold_list);
		mRecordSilverListView = (PullToRefreshListView) findViewById(R.id.record_consumption_silver_list);
		mGoldAdapter = new ConsumptionRecordAdapter(this, mListGold);
		mSilverAdapter = new ConsumptionRecordAdapter(this, mListSilver);
		mRecordGoldListView.setAdapter(mGoldAdapter);
		mRecordSilverListView.setAdapter(mSilverAdapter);
		mRecordGoldListView.setOnRefreshListener(new PullToRefreshListener());
		mRecordSilverListView.setOnRefreshListener(new PullToRefreshListener());
		mRecordGoldListView.setMode(Mode.BOTH);
		mRecordSilverListView.setMode(Mode.BOTH);
		mRecordGoldListView.setEmptyView(tvEmpty);
		mRecordSilverListView.setEmptyView(tvEmptyTwo);

	}

	private void initListener() {
		mGoldBtn.setOnClickListener(this);
		mSilverBtn.setOnClickListener(this);
	}

	private void getRecord(boolean isLoading) {
		if (isLoading) {
			BaseUtils.showDialog(this, R.string.loading);
		}

		int type = NumericalType.GOLD_COIN.getValue();
		int start = mStartGold;
		RecordBiz recordBiz = new RecordBiz();
		if (flag == GOLD_FLAG) {
			if (mTotalGold <= mListGold.size() && mTotalGold != 0) {
				mHandler.sendEmptyMessage(DO_NOT_GOLD_LOAD_MORE_DATA);
				return;
			}
			type = NumericalType.GOLD_COIN.getValue();
			mStartGold = mListGold.size();
			start = mStartGold;
			recordBiz.getDayBookRecord(this, type, -1 - start, NUM, mGoldCallBack);
		} else {
			if (mTotalSilver <= mListSilver.size() && mTotalSilver != 0) {
				mHandler.sendEmptyMessage(DO_NOT_SILVER_LOAD_MORE_DATA);
				return;
			}
			type = NumericalType.SILVER_COIN.getValue();
			mStartSilver = mListSilver.size();
			start = mStartSilver;
			recordBiz.getDayBookRecord(this, type, -1 - start, NUM, mSilverCallBack);
		}

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.record_gold_btn:
			flag = GOLD_FLAG;
			if (mListGold.size() == 0) {
				getRecord(true);
			}
			mGoldBtn.setBackgroundResource(R.drawable.rank_btn_left_pr);
			mSilverBtn.setBackgroundResource(R.drawable.rank_btn_right_un);
			mGoldBtn.setTextColor(getResources().getColor(R.color.white));
			mSilverBtn.setTextColor(getResources().getColor(R.color.red));

			llRecordGold.setVisibility(View.VISIBLE);
			llRecordSilver.setVisibility(View.GONE);
			break;
		case R.id.record_silver_btn:
			mGoldBtn.setBackgroundResource(R.drawable.rank_btn_left_un);
			mSilverBtn.setBackgroundResource(R.drawable.rank_btn_right_pr);
			mGoldBtn.setTextColor(getResources().getColor(R.color.red));
			mSilverBtn.setTextColor(getResources().getColor(R.color.white));
			if (mSilverCallBack == null) {
				mSilverCallBack = new SilverCallBack();
			}
			flag = SILVER_FLAG;

			llRecordGold.setVisibility(View.GONE);
			llRecordSilver.setVisibility(View.VISIBLE);

			if (mListSilver.size() == 0) {
				getRecord(true);
			}
			break;
		default:
			break;
		}
	}

	private class GoldCallBack implements BizCallBackDaybook {

		@Override
		public void getDaybook(int retCode, GoGirlDaybookInfoList list, int total) {
			mTotalGold = total;
			Message msg = Message.obtain();
			msg.what = HandlerType.GOLD_RECORD_BACK;
			msg.arg1 = retCode;
			msg.arg2 = flag;
			msg.obj = list;

			mHandler.sendMessage(msg);

		}

	}

	private class SilverCallBack implements BizCallBackDaybook {

		@Override
		public void getDaybook(int retCode, GoGirlDaybookInfoList list, int total) {
			mTotalSilver = total;
			Message msg = Message.obtain();
			msg.what = HandlerType.SILVER_RECORD_BACK;
			msg.arg1 = retCode;
			msg.arg2 = flag;
			msg.obj = list;

			mHandler.sendMessage(msg);
		}

	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initRecourse() {

	}

	@Override
	protected int initView() {
		return R.layout.activity_consumptionrecord;
	}

	//上拉刷新 ,下拉加载更多刷新 
	class PullToRefreshListener implements OnRefreshListener2<ListView> {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (flag == GOLD_FLAG) {
				mListGold.clear();
			} else {
				mListSilver.clear();
			}
			getRecord(false);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			getRecord(false);
		}
	}
}
