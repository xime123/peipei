package com.tshang.peipei.activity.harem;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.model.harem.GroupTrickBiz;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: PatronizeActivity.java 
 *
 * @Description: 宠幸
 *
 * @author allen  
 *
 * @date 2014-9-24 下午2:53:11 
 *
 * @version V1.0   
 */
public class PatronizeActivity extends BaseActivity {

	private ImageView mHeadView;
	private TextView mNickText;
	private int fuid = -1;
	private String fnick = "";
	private int groupid = -1;
	private RelativeLayout mLayoutAnim;
	private ImageView mHeartSelfImage, mHeartFriendImage;
	private ImageView mHeadSelf, mHeadFriend;

	private DisplayImageOptions options;
	private DisplayImageOptions headOptions;

	@Override
	protected void initData() {
		options = ImageOptionsUtils.GetGroupHeadKeySmall(this);
		headOptions = ImageOptionsUtils.GetHeadKeyBigRounded(this);
		Bundle b = getIntent().getExtras();
		if (b != null) {
			fuid = b.getInt("fuid");
			fnick = b.getString("fnick");
			groupid = b.getInt("groupid");
		}

		if (fuid != -1) {
			imageLoader.displayImage("http://" + fuid + BAConstants.LOAD_HEAD_UID_GROUP, mHeadView, options);
			imageLoader.displayImage("http://" + fuid + BAConstants.LOAD_HEAD_UID_APPENDSTR, mHeadFriend, headOptions);
		}

		if (BAApplication.mLocalUserInfo != null) {
			imageLoader.displayImage("http://" + BAApplication.mLocalUserInfo.uid.intValue() + BAConstants.LOAD_HEAD_UID_APPENDSTR, mHeadSelf,
					headOptions);
		}

		if (!TextUtils.isEmpty(fnick)) {
			String alias = SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(fuid);
			String sendUserName = TextUtils.isEmpty(alias) ? fnick : alias;

			mNickText.setText(sendUserName);
		}

	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_patronize);

		mHeadView = (ImageView) findViewById(R.id.patronize_head_iv);
		mNickText = (TextView) findViewById(R.id.patronize_nick_tv);
		findViewById(R.id.patronize_sure_btn).setOnClickListener(this);

		mLayoutAnim = (RelativeLayout) findViewById(R.id.patronize_anim_ll);
		mHeartSelfImage = (ImageView) findViewById(R.id.patronize_heart_self_iv);
		mHeartFriendImage = (ImageView) findViewById(R.id.patronize_heart_friend_iv);

		mHeadSelf = (ImageView) findViewById(R.id.patronize_head_self_iv);
		mHeadFriend = (ImageView) findViewById(R.id.patronize_head_friend_iv);
	}

	@Override
	protected int initView() {
		return R.layout.activity_patronize;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_tv_left:
			if (mLayoutAnim.getVisibility() == View.VISIBLE) {
				return;
			} else {
				finish();
			}
			break;
		case R.id.patronize_sure_btn:
			if (mLayoutAnim.getVisibility() == View.VISIBLE) {
				return;
			}

			if (BAApplication.mLocalUserInfo != null) {
				GroupTrickBiz gBiz = new GroupTrickBiz();
				gBiz.groupTrick(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(), fuid,
						groupid, mHandler);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (mLayoutAnim.getVisibility() == View.VISIBLE) {
				mLayoutAnim.setVisibility(View.GONE);
				setResult(SelectUserActivity.PATRONIZE_CODE);
				finish();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerType.GROUP_TRICK:
			mLayoutAnim.setVisibility(View.VISIBLE);
			SharedPreferencesTools.getInstance(this).saveLongKeyValue(System.currentTimeMillis(), BAConstants.PEIPEI_SELECT_USER);

			Animation animationself = AnimationUtils.loadAnimation(this, R.anim.patronize_heart_self);
			Animation animationfriend = AnimationUtils.loadAnimation(this, R.anim.patronize_heart_friend);
			animationfriend.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {}

				@Override
				public void onAnimationRepeat(Animation animation) {}

				@Override
				public void onAnimationEnd(Animation animation) {
					mLayoutAnim.setVisibility(View.GONE);
					setResult(SelectUserActivity.PATRONIZE_CODE);
					finish();
				}
			});

			mHeartSelfImage.startAnimation(animationself);
			mHeartFriendImage.startAnimation(animationfriend);
			break;
		case HandlerType.GROUP_TRICK_NUM:
			BaseUtils.showTost(this, "您的精力不足哦~明天再来吧");
			setResult(SelectUserActivity.PATRONIZE_CODE);
			finish();
			break;

		default:
			break;
		}
	}

}
