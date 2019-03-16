package com.tshang.peipei.activity.space;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: SpaceAvatarActivity.java 
 *
 * @Description: 头像放大界面 
 *
 * @author Administrator  
 *
 * @date 2015-10-15 下午4:14:41 
 *
 * @version V1.0   
 */
public class SpaceAvatarActivity extends BaseActivity{

	private String mPicHead = "";//客人头像KEY
	private ImageView mIvHead;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void initData() {
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			mPicHead = bundle.getString(BAConstants.IntentType.MAINHALLFRAGMENT_HEADPIC);
		}
		
		if (!TextUtils.isEmpty(mPicHead)) {
			String key = mPicHead + "@true@425@425";
			DisplayImageOptions options_head = ImageOptionsUtils.getImageKeyOptionsPersonBg(this);
//			DisplayImageOptions options_head = ImageOptionsUtils.GetHeadKeyBigRounded(this);
			imageLoader.displayImage("http://" + key, mIvHead, options_head);
		}
	}

	@Override
	protected void initRecourse() {
		mIvHead = (ImageView) findViewById(R.id.iv_avatar);
		findViewById(R.id.rl_parent).setOnClickListener(this);
	}
	
	public static void openMineFaqActivity(Activity activity, String mPicHead) {
		Bundle bundle = new Bundle();
		bundle.putString(BAConstants.IntentType.MAINHALLFRAGMENT_HEADPIC, mPicHead);
		BaseUtils.openNormalActivity(activity, SpaceAvatarActivity.class, bundle);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_parent:
			scaleFinish();
			break;
		}
	}
	
	@Override
	protected int initView() {
		return R.layout.activity_space_avatar;
	}

}
