package com.tshang.peipei.activity.mine;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.base.BaseUtils;

/**
 * @Title: MineEditSigActivity.java 
 *
 * @Description: 编辑个性签名界面 
 *
 * @author DYH  
 *
 * @date 2015-10-27 下午2:25:30 
 *
 * @version V1.0   
 */
public class MineEditSigActivity extends BaseActivity {

	private TextView mRightTv;
	private EditText et_sig;
	private TextView tv_sig_count;
	private static final int SIG_COUNT = 257;
	
	@Override
	protected void initData() {

	}

	@Override
	protected void initRecourse() {
		mRightTv = (TextView) findViewById(R.id.title_tv_right);
		mRightTv.setVisibility(View.VISIBLE);
		mRightTv.setText(R.string.ok);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_my_sig);
		findViewById(R.id.title_tv_left).setOnClickListener(this);
		et_sig = (EditText) findViewById(R.id.et_sig);
		tv_sig_count = (TextView) findViewById(R.id.tv_sig_count);
		tv_sig_count.setText("0/" + SIG_COUNT);
		setListener();
	}
	
	private void setListener(){
		mRightTv.setOnClickListener(this);
		et_sig.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				tv_sig_count.setText(getBodyLength() + "/" + SIG_COUNT);
			}
		});
	}
	
	
	/**
	 * 获取输入内容的长度
	 * 
	 * @return
	 */
	private int getBodyLength() {
		String body = et_sig.getText().toString();
		if (body == null || body.trim().length() == 0) {
			return 0;
		}
		return body.length();
	}
	
	public static void openMineFaqActivity(Activity activity) {
		BaseUtils.openActivity(activity, MineEditSigActivity.class);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_tv_right:
			
			break;

		default:
			break;
		}
	}

	@Override
	protected int initView() {
		return R.layout.activity_edit_sig;
	}

}
