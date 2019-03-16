package com.tshang.peipei.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.space.SpaceActivity;
import com.tshang.peipei.activity.store.StoreIntegralActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: MineShowGiftDialogActivity.java 
 *
 * @Description: 展示具体礼物对话框 
 *
 * @author allen  
 *
 * @date 2014-6-6 上午11:13:48 
 *
 * @version V1.0   
 */
public class MineShowGiftDialogActivity extends BaseActivity {

	private TextView mTvNick, mTvGiftName, mTvInvate, mTvGlamour, mTvLoyalty;
	private ImageView mIvHead, mIvGiftImg;
	private int mFriendSex, mFriendUid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		initListener();
	}

	private void initUI() {
		mTvNick = (TextView) findViewById(R.id.show_gift_nick);
		mTvGiftName = (TextView) findViewById(R.id.show_gift_name);
		mTvInvate = (TextView) findViewById(R.id.show_gift_integral);
		mTvGlamour = (TextView) findViewById(R.id.show_gift_glamour);
		mTvLoyalty = (TextView) findViewById(R.id.show_gift_loyalty);

		mIvHead = (ImageView) findViewById(R.id.show_gift_head_iv);
		mIvGiftImg = (ImageView) findViewById(R.id.show_gift_pic_iv);

		Intent intent = getIntent();

		mTvNick.setText(intent.getStringExtra("nick"));
		mTvGiftName.setText(String.format(getString(R.string.give_gift_content), intent.getStringExtra("name")));
		mTvInvate.setText(intent.getStringExtra("invate"));
		mTvGlamour.setText(intent.getStringExtra("glamour"));
		mTvLoyalty.setText(intent.getStringExtra("loyalty"));
		mFriendSex = intent.getIntExtra("friendsex", -1);
		mFriendUid = intent.getIntExtra("frienduid", -1);
		DisplayImageOptions options_head = ImageOptionsUtils.GetHeadUidSmallRounded(this);
		imageLoader.displayImage("http://" + mFriendUid + BAConstants.LOAD_HEAD_UID_APPENDSTR, mIvHead, options_head);
		DisplayImageOptions options = ImageOptionsUtils.getImageKeyOptions(this);
		imageLoader.displayImage("http://" + intent.getStringExtra("imagepic") + BAConstants.LOAD_180_APPENDSTR, mIvGiftImg, options);

	}

	private void initListener() {
		findViewById(R.id.show_gift_change).setOnClickListener(this);
		findViewById(R.id.show_gift_delete).setOnClickListener(this);
		mIvHead.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		case R.id.show_gift_delete:
			defaultFinish();
			break;
		case R.id.show_gift_change:
			Intent intent = new Intent(this, StoreIntegralActivity.class);
			startActivity(intent);

			overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
			break;
		case R.id.show_gift_head_iv:
			SpaceUtils.toSpaceCustom(this, mFriendUid, mFriendSex);
			finish();
			break;
		default:
			break;
		}
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
		return R.layout.activity_showgift_dialog;
	}

}
