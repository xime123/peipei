package com.tshang.peipei.activity.store;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAConstants.NumericalType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.biz.store.RecordBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackDaybook;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDaybookInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDaybookInfoList;

/**
 * @Title: IntegralRecordActivity.java 
 *
 * @Description: 积分记录
 *
 * @author allen  
 *
 * @date 2014-4-24 下午3:01:29 
 *
 * @version V1.0   
 */
public class IntegralRecordActivity extends BaseActivity implements BizCallBackDaybook {

	private static final int NUM = 50;

	private int mStart = 0;

	private int mTotal = 0;

	private BAHandler mHandler;
	private LinearLayout mPb;

	private ListView mRecordListView;
	private ConsumptionRecordAdapter mAdapter;
	private List<GoGirlDaybookInfo> mList = new ArrayList<GoGirlDaybookInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (BAApplication.mLocalUserInfo != null) {
//			MobclickAgent.onEvent(this, "jinrujifenshangchengrenshu");
		}

		initHandler();
		initUI();
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
					getRecord();
					break;
				case HandlerType.CREATE_GETDATA_BACK:
					mPb.setVisibility(View.GONE);

					if (msg.arg1 == 0) {
						GoGirlDaybookInfoList infoList = (GoGirlDaybookInfoList) msg.obj;
						if (infoList != null && infoList.size() > 0) {
							mList.addAll(infoList);
						}
						mAdapter.notifyDataSetChanged();

					}
					break;

				default:
					break;
				}
			}

		};
	}

	private void initUI() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.record_integral);

		mRecordListView = (ListView) findViewById(R.id.record_integral_list);
		mAdapter = new ConsumptionRecordAdapter(this, mList);
		mRecordListView.setAdapter(mAdapter);

		mPb = (LinearLayout) findViewById(R.id.record_integral_pb);
		mPb.setVisibility(View.VISIBLE);
	}

	private void getRecord() {

		int type = NumericalType.SCORE.getValue();
		RecordBiz recordBiz = new RecordBiz();
		if (mTotal <= mList.size() && mTotal != 0) {
			return;
		}

		mStart = mList.size();
		type = NumericalType.GOLD_COIN.getValue();
		recordBiz.getDayBookRecord(this, type, mStart, NUM, IntegralRecordActivity.this);

	}

	@Override
	public void getDaybook(int retCode, GoGirlDaybookInfoList list, int total) {
		mTotal = total;
		Message msg = Message.obtain();
		msg.what = HandlerType.CREATE_GETDATA_BACK;
		msg.arg1 = retCode;
		msg.obj = list;

		mHandler.sendMessage(msg);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initRecourse() {
		// TODO Auto-generated method stub

	}

	@Override
	protected int initView() {
		// TODO Auto-generated method stub
		return R.layout.activity_integralrecord;
	}

}
