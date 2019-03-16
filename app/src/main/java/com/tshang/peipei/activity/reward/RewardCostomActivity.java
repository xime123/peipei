package com.tshang.peipei.activity.reward;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.base.BaseUtils;

/**
 * @Title: RewardCostomActivity.java 
 *
 * @Description: 悬赏自定义 
 *
 * @author Aaron  
 *
 * @date 2015-11-3 下午4:29:29 
 *
 * @version V1.0   
 */
public class RewardCostomActivity extends BaseActivity {

	private TextView skillTv, skillDescTv;
	private EditText skillEt, skillDescEt;

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initRecourse() {
		mTitle = (TextView) this.findViewById(R.id.title_tv_mid);
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setOnClickListener(this);
		mLinRight = (LinearLayout) this.findViewById(R.id.title_lin_right);
		mLinRight.setVisibility(View.VISIBLE);
		mLinRight.setOnClickListener(this);
		mTextRight = (TextView) this.findViewById(R.id.title_tv_right);
		mTextRight.setVisibility(View.VISIBLE);
		mTextRight.setText("确定");
		mTitle.setText("自定义悬赏");

		skillTv = (TextView) findViewById(R.id.reward_costom_skill_num_tv);
		skillEt = (EditText) findViewById(R.id.reward_costom_skill_num_et);
		skillDescTv = (TextView) findViewById(R.id.reward_costom_skill_desc_num_tv);
		skillDescEt = (EditText) findViewById(R.id.reward_costom_skill_desc_num_et);

		skillDescEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				skillDescTv.setText(s.length() + "/" + 20);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		skillEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				skillTv.setText(s.length() + "/" + 10);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	protected int initView() {
		return R.layout.activity_reward_costom;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_lin_right://确定
			if (TextUtils.isEmpty(skillEt.getText().toString())) {
				BaseUtils.showTost(this, "技能不能为空");
				return;
			}
			if (TextUtils.isEmpty(skillDescEt.getText().toString())) {
				BaseUtils.showTost(this, "技术描述不能为空");
				return;
			}
			Intent intent = new Intent();
			intent.putExtra("skill", skillEt.getText().toString().trim());
			intent.putExtra("skillDesc", skillDescEt.getText().toString().trim());
			this.setResult(PublishRewardActivity.COSTOM_CODE, intent);
			this.finish();
			break;

		default:
			break;
		}
	}

}
