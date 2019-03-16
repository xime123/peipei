package com.tshang.peipei.activity.harem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.harem.UpdateHarem;

/**
 * 修改后宫告示
 * @author Jeff
 *
 */
public class UpdateGroupInfoNoticeActivity extends BaseActivity {
	private EditText edtNotice;
	private int groupid = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.HAREM_UPDATE_GROUP_INFO_SUCCESS_VALUE:
			String name = edtNotice.getText().toString();
			Intent intent = new Intent();
			intent.putExtra("haremnotice", name);
			setResult(RESULT_OK, intent);
			this.finish();
			break;
		case HandlerValue.HAREM_UPDATE_GROUP_INFO_FAILED_VALUE:
			BaseUtils.showTost(this, "修改失败");
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_tv_right:
			String notice = edtNotice.getText().toString();
			if (TextUtils.isEmpty(notice)) {
				notice = "";
			}
			UpdateHarem.getInstance().reqUpdateGroupInfo(this, "".getBytes(), groupid, "", notice, mHandler);

			break;

		default:
			break;
		}
	}

	@Override
	protected void initData() {
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			groupid = bundle.getInt("groupid", -1);
			String name = bundle.getString("haremnotice");
			edtNotice.setText(name);
		}
	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText("后宫告示");
		mTextRight = (TextView) findViewById(R.id.title_tv_right);
		mTextRight.setText(R.string.submit);
		mTextRight.setVisibility(View.VISIBLE);
		mTextRight.setOnClickListener(this);
		edtNotice = (EditText) findViewById(R.id.edt_update_groupinfo_notice);

	}

	@Override
	protected int initView() {
		return R.layout.activity_update_groupinfo_notice;
	}

}
