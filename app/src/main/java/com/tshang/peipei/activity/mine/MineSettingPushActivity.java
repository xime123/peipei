package com.tshang.peipei.activity.mine;

import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.SwitchStatus;
import com.tshang.peipei.base.babase.UserSharePreference;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.user.UserAccountBiz;
import com.tshang.peipei.model.biz.user.UserSettingBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetUserInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;

/**
 * @Title: 设置界面
 *
 * @Description: 消息推送设置界面
 *
 * @author allen
 *
 * @version V1.0   
 */
public class MineSettingPushActivity extends BaseActivity implements BizCallBackGetUserInfo {

	private final int SETTING_BACK = 1;

	private ToggleButton mChatPush;
	private ToggleButton mSysPush;
	private ToggleButton mGiftPush;
	private ToggleButton mIgnoreAll;
	private ToggleButton mSound;
	private ToggleButton mShake;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void switchAll() {
		// 上报消息开关
		int action = mChatPush.isChecked() ? 1 : 0;
		setPushStatus(action, BAConstants.SwitchType.const_req0);
		action = mSysPush.isChecked() ? 1 : 0;
		setPushStatus(action, BAConstants.SwitchType.const_req1);
		action = mGiftPush.isChecked() ? 1 : 0;
		setPushStatus(action, BAConstants.SwitchType.const_req2);
		action = mIgnoreAll.isChecked() ? 1 : 0;
		setPushStatus(action, BAConstants.SwitchType.const_req3);

		SharedPreferencesTools.getInstance(this).saveBooleanKeyValue(!mSound.isChecked(), BAConstants.SOUND);
		SharedPreferencesTools.getInstance(this).saveBooleanKeyValue(!mShake.isChecked(), BAConstants.SHAKE);
	}

	private void setPushStatus(int action, int type) {
		UserSettingBiz settingBiz = new UserSettingBiz();
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
		if (userEntity != null) {
			settingBiz.setUserBit(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, userEntity.uid.intValue(), action, type, null);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switchAll();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_rel_exit:
			UserSharePreference.getInstance(this).removeUserByKey();
			break;
		case R.id.title_tv_left:
			switchAll();
			this.finish();
			break;
		default:
			break;
		}

	}

	@Override
	protected void initData() {
		new UserAccountBiz(this).getUserInfo(this);
	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mBackText.setText(R.string.setting);
		mBackText.setOnClickListener(this);
		mTitle.setText(R.string.str_message_notify);
		mChatPush = (ToggleButton) findViewById(R.id.setting_push_chat_toggle);
		mSysPush = (ToggleButton) findViewById(R.id.setting_push_sys_toggle);
		mGiftPush = (ToggleButton) findViewById(R.id.setting_push_gift_toggle);
		mIgnoreAll = (ToggleButton) findViewById(R.id.setting_push_ignore_toggle);

		boolean sound = SharedPreferencesTools.getInstance(this).getBooleanKeyValue(BAConstants.SOUND);
		boolean shake = SharedPreferencesTools.getInstance(this).getBooleanKeyValue(BAConstants.SHAKE);

		mSound = (ToggleButton) findViewById(R.id.setting_push_sound_toggle);
		mShake = (ToggleButton) findViewById(R.id.setting_push_shake_toggle);
		mSound.setChecked(!sound);
		mShake.setChecked(!shake);
	}

	@Override
	protected int initView() {
		return R.layout.activity_setting_push;
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);

		switch (msg.what) {
		case SETTING_BACK:
			if (msg.arg1 == 0) {
				GoGirlUserInfo userInfo = (GoGirlUserInfo) msg.obj;
				int status = userInfo.userstatus.intValue();
				if ((status & SwitchStatus.GG_US_CHAT_PUSH_FLAG) == 0) {
					mChatPush.setChecked(false);
				} else {
					mChatPush.setChecked(true);
				}

				if ((status & SwitchStatus.GG_US_SYS_PUSH_FLAG) == 0) {
					mSysPush.setChecked(false);
				} else {
					mSysPush.setChecked(true);
				}

				if ((status & SwitchStatus.GG_US_GIFT_PUSH_FLAG) == 0) {
					mGiftPush.setChecked(false);
				} else {
					mGiftPush.setChecked(true);
				}

				if ((status & SwitchStatus.GG_US_ANTIANNOY_FLAG) == 0) {
					mIgnoreAll.setChecked(false);
				} else {
					mIgnoreAll.setChecked(true);
				}

			}
			break;

		default:
			break;
		}
	}

	@Override
	public void getUserInfoCallBack(int retCode, GoGirlUserInfo userinfo) {
		sendHandlerMessage(mHandler, SETTING_BACK, retCode, userinfo);
	}

}
