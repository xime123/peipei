package com.tshang.peipei.activity.mine;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.model.biz.space.SpaceCustomBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackTipoffUser;

/**
 * @Title: ReportActivity.java 
 *
 * @Description: 举报界面
 *
 * @author DYH  
 *
 * @date 2015-10-12 上午10:13:45 
 *
 * @version V1.0   
 */
public class ReportActivity extends BaseActivity implements BizCallBackTipoffUser {

	public static final String FUID = "fuid";
	
	private TextView title_tv_left;
	private TextView title_tv_right;
	private TextView report_abuse;
	private TextView report_sex;
	private TextView report_ad;
	private TextView report_political;
	private TextView report_other;
	private int reasonid = -1;
	private int otherUid = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void initData() {
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			otherUid = bundle.getInt(FUID);
		}
	}

	@Override
	protected void initRecourse() {
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(getString(R.string.report));
		title_tv_left = (TextView) findViewById(R.id.title_tv_left);
		title_tv_left.setTextColor(getResources().getColor(R.color.space_black));
		title_tv_right = (TextView) findViewById(R.id.title_tv_right);
		title_tv_right.setText(getString(R.string.ok));
		title_tv_right.setVisibility(View.VISIBLE);
		report_abuse = (TextView) findViewById(R.id.report_abuse);
		report_sex = (TextView) findViewById(R.id.report_sex);
		report_ad = (TextView) findViewById(R.id.report_ad);
		report_political = (TextView) findViewById(R.id.report_political);
		report_other = (TextView) findViewById(R.id.report_other);
		
		setListener();
	}
	
	private void setListener(){
		title_tv_right.setOnClickListener(this);
		title_tv_left.setOnClickListener(this);
		report_abuse.setOnClickListener(itemClickListener);
		report_sex.setOnClickListener(itemClickListener);
		report_ad.setOnClickListener(itemClickListener);
		report_political.setOnClickListener(itemClickListener);
		report_other.setOnClickListener(itemClickListener);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_tv_right:
			report();
			break;
		}
	}
	
	private OnClickListener itemClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			setUnSelect();
			switch (v.getId()) {
			case R.id.report_abuse:
				reasonid = 0;
				report_abuse.setBackgroundResource(R.drawable.activity_report_selected_item_bg);
				report_abuse.setTextColor(getResources().getColor(R.color.space_bottom_txt_color));
				break;
			case R.id.report_sex:
				reasonid = 1;
				report_sex.setBackgroundResource(R.drawable.activity_report_selected_item_bg);
				report_sex.setTextColor(getResources().getColor(R.color.space_bottom_txt_color));
				break;
			case R.id.report_ad:
				reasonid = 2;
				report_ad.setBackgroundResource(R.drawable.activity_report_selected_item_bg);
				report_ad.setTextColor(getResources().getColor(R.color.space_bottom_txt_color));
				break;
			case R.id.report_political:
				reasonid = 3;
				report_political.setBackgroundResource(R.drawable.activity_report_selected_item_bg);
				report_political.setTextColor(getResources().getColor(R.color.space_bottom_txt_color));
				break;
			case R.id.report_other:
				reasonid = 4;
				report_other.setBackgroundResource(R.drawable.activity_report_selected_item_bg);
				report_other.setTextColor(getResources().getColor(R.color.space_bottom_txt_color));
				break;
			}
		}
	};
	
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		if (msg.what == 0) {
			BaseUtils.showTost(this, "举报成功");
		} else {
			BaseUtils.showTost(this, "举报失败");
		}
		finish();
	}
	
	private void report(){
		new SpaceCustomBiz().tipoffUser(this, otherUid, reasonid, this);
	}
	
	public static void openMineFaqActivity(Activity activity, int fuid) {
		Bundle bundle = new Bundle();
		bundle.putInt(FUID, fuid);
		BaseUtils.openActivity(activity, ReportActivity.class, bundle);
	}

	private void setUnSelect(){
		report_abuse.setBackgroundResource(R.drawable.activity_report_unselected_item_bg);
		report_abuse.setTextColor(getResources().getColor(R.color.space_black));
		report_sex.setBackgroundResource(R.drawable.activity_report_unselected_item_bg);
		report_sex.setTextColor(getResources().getColor(R.color.space_black));
		report_ad.setBackgroundResource(R.drawable.activity_report_unselected_item_bg);
		report_ad.setTextColor(getResources().getColor(R.color.space_black));
		report_political.setBackgroundResource(R.drawable.activity_report_unselected_item_bg);
		report_political.setTextColor(getResources().getColor(R.color.space_black));
		report_other.setBackgroundResource(R.drawable.activity_report_unselected_item_bg);
		report_other.setTextColor(getResources().getColor(R.color.space_black));
	}
	
	@Override
	protected int initView() {
		return R.layout.activity_report;
	}

	@Override
	public void tipoffUserCallBack(int retCode) {
		HandlerUtils.sendHandlerMessage(mHandler, retCode);
	}

}
