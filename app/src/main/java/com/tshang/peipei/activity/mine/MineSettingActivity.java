package com.tshang.peipei.activity.mine;

import java.io.File;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.chat.ChatActivity;
import com.tshang.peipei.activity.dialog.GoLogoutDialog;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.network.socket.ThreadPoolService;

/**
 * @Title: 设置界面
 *
 * @Description: 设置
 *
 * @author allen
 *
 * @version V1.0   
 */
public class MineSettingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.CLEAR_MEMORY_VALUE:
			BaseUtils.showTost(this, "清理成功");
			break;

		default:
			break;
		}
	}

	@SuppressLint("InlinedApi")
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.setting_tv_exit:
			new GoLogoutDialog(this, android.R.style.Theme_Translucent_NoTitleBar, R.string.cofirm_exit, R.string.ok, R.string.cancel).showDialog();
			break;
		case R.id.setting_message_tv:
			BaseUtils.openActivity(this, MineSettingPushActivity.class);
			break;
		case R.id.setting_userinfo_tv:
			if (BAApplication.mLocalUserInfo != null) {
				Bundle bundle = new Bundle();
				bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, BAApplication.mLocalUserInfo.uid.intValue());
				bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, BAApplication.mLocalUserInfo.sex.intValue());
				BaseUtils.openActivity(this, MineSettingUserInfoActivity.class, bundle);
			}
			break;
		case R.id.setting_about_tv:
			BaseUtils.openActivity(this, MineSettingAboutActivity.class);
			break;
		case R.id.setting_feedback_tv:
			ChatActivity.intentChatActivity(this, BAConstants.PEIPEI_CHAT_XIAOPEI, getString(R.string.xiaopei), Gender.FEMALE.getValue(), false,
					false, 0);
			break;
		case R.id.setting_local_img_tv://清理文件缓存
			BaseUtils.showDialog(this, "正在清理，请稍等...");
			ThreadPoolService.getInstance().execute(new Runnable() {

				@Override
				public void run() {
					try {
						if (SdCardUtils.isExistSdCard()) {//清理掉之前的语音文件存错了
							File dirFile = new File(Environment.getExternalStorageDirectory() + "/PeiPei");
							if (dirFile != null) {
								File[] files = dirFile.listFiles();
								if (files != null) {
									for (File file : files) {
										if (file.isFile()) {
											file.delete();
										}
									}
								}
							}
						}
						imageLoader.clearDiskCache();
						HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.CLEAR_MEMORY_VALUE);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});

			break;
		case R.id.setting_memory_tv://清理内存缓存
			BaseUtils.showDialog(this, "正在清理，请稍等...");
			ThreadPoolService.getInstance().execute(new Runnable() {

				@Override
				public void run() {
					try {
						imageLoader.clearMemoryCache();
						HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.CLEAR_MEMORY_VALUE);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});

			break;
		case R.id.setting_help_tv:
			MineFaqActivity.openMineFaqActivity(this, MineFaqActivity.KING_VALLUE);
			break;
		default:
			break;
		}
	}

	@Override
	protected void initData() {}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mBackText.setText(R.string.mine);
		mBackText.setOnClickListener(this);
		mTitle.setText(R.string.setting);
		findViewById(R.id.setting_userinfo_tv).setOnClickListener(this);
		findViewById(R.id.setting_message_tv).setOnClickListener(this);

		findViewById(R.id.setting_feedback_tv).setOnClickListener(this);
		findViewById(R.id.setting_about_tv).setOnClickListener(this);
		findViewById(R.id.setting_tv_exit).setOnClickListener(this);
		findViewById(R.id.setting_local_img_tv).setOnClickListener(this);
		findViewById(R.id.setting_memory_tv).setOnClickListener(this);
		findViewById(R.id.setting_help_tv).setOnClickListener(this);
	}

	@Override
	protected int initView() {
		return R.layout.activity_setting;
	}

}
