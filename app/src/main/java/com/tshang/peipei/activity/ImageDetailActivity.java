/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tshang.peipei.activity;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.main.adapter.ImageDetailAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.request.RequestReportPic;
import com.tshang.peipei.model.request.RequestReportPic.IReportPicListener;
import com.tshang.peipei.vender.common.util.ListUtils;

public class ImageDetailActivity extends BaseActivity implements OnClickListener, IReportPicListener {
	public static final String EXTRA_IMAGE = "extra_image";
	public static final String POSITION = "position";
	public static final String ISREPORT = "isreport";
	public static final String PIC_UID = "pic_uid";
	public int pic_uid = 0;
	private ArrayList<String> lists;
	private String pic_key;
	private TextView textView;

	private ViewPager mPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			mPager = (ViewPager) findViewById(R.id.pager);
			lists = bundle.getStringArrayList(EXTRA_IMAGE);
			boolean isReport = bundle.getBoolean(ISREPORT, false);
			pic_uid = bundle.getInt(PIC_UID, 0);
			if (ListUtils.isEmpty(lists)) {
				return;
			}
			final int totalCount = lists.size();
			pic_key = lists.get(0);
			ImageDetailAdapter mAdapter = new ImageDetailAdapter(this);
			mPager.setAdapter(mAdapter);
			mAdapter.setList(bundle.getStringArrayList(EXTRA_IMAGE));
			mPager.setOffscreenPageLimit(2);
			final int extraCurrentItem = bundle.getInt(POSITION, 0);
			if (extraCurrentItem != -1) {
				mPager.setCurrentItem(extraCurrentItem);
			}
			textView = (TextView) findViewById(R.id.tv_page_indicator);
			if (isReport) {
				TextView tv_report = (TextView) findViewById(R.id.tv_report);
				tv_report.setVisibility(View.VISIBLE);
				tv_report.setOnClickListener(this);
			}
			int current = extraCurrentItem + 1;
			textView.setText(current + "/" + totalCount);
			mPager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
					pic_key = lists.get(arg0);
					textView.setText((arg0 + 1) + "/" + totalCount);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {

				}
			});
		}

	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.REPOPRT_SUCCESS_VALUE:
			BaseUtils.showTost(this, "举报成功");
			break;
		case HandlerValue.REPOPRT_FAILED_VALUE:
			BaseUtils.showTost(this, "举报失败");
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Set on the ImageView in the ViewPager children fragments, to enable/disable low profile mode
	 * when the ImageView is touched.
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_report:

			if (!TextUtils.isEmpty(pic_key)) {
				RequestReportPic req = new RequestReportPic();
				req.getReportPic(this, pic_key, pic_uid, this);
			}
			break;

		default:
			final int vis = mPager.getSystemUiVisibility();
			if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
				mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
			} else {
				mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
			}
			break;
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
		return R.layout.image_detail_pager;
	}

	@Override
	public void reportPicCallBack(int retCode) {
		if (retCode == 0) {
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.REPOPRT_SUCCESS_VALUE);
		} else {
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.REPOPRT_FAILED_VALUE);
		}
	}

}
