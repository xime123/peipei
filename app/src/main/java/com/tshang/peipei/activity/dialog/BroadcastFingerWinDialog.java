package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * @Title: BroadcastFingerWinDialog.java 
 *
 * @Description: 广播猜拳结果
 *
 * @author allen  
 *
 * @date 2014-9-20 下午6:47:28 
 *
 * @version V1.0   
 */
public class BroadcastFingerWinDialog extends Dialog {

	private Activity context;
	private String msg;
	private int uid1, uid2;
	private int finger1, finger2;
	private int winuid;
	private BAHandler mBaHandler;

	public BroadcastFingerWinDialog(Activity context, String msg, int uid1, int uid2, int finger1, int finger2, int winuid, BAHandler mBaHandler) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		this.msg = msg;
		this.uid1 = uid1;
		this.uid2 = uid2;
		this.finger1 = finger1;
		this.finger2 = finger2;
		this.winuid = winuid;
		this.mBaHandler = mBaHandler;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.item_chat_listview_finger_win);

		ImageView leftFinger = (ImageView) findViewById(R.id.chat_item_finger_winner_left_image);
		ImageView rightFinger = (ImageView) findViewById(R.id.chat_item_finger_winner_right_image);
		ImageView leftHead = (ImageView) findViewById(R.id.chat_item_finger_winner_left_head);
		ImageView rightHead = (ImageView) findViewById(R.id.chat_item_finger_winner_right_head);
		ImageView midFinger = (ImageView) findViewById(R.id.chat_item_finger_winner_image);
		TextView tvMsg = (TextView) findViewById(R.id.chat_item_finger_winner_text);

		tvMsg.setText(msg);
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options_uid_head = ImageOptionsUtils.GetHeadUidSmallRounded(context);
		imageLoader.displayImage("http://" + uid1 + BAConstants.LOAD_HEAD_UID_APPENDSTR, leftHead, options_uid_head);
		imageLoader.displayImage("http://" + uid2 + BAConstants.LOAD_HEAD_UID_APPENDSTR, rightHead, options_uid_head);

		//左边玩家
		if (finger1 == 0) {
			leftFinger.setBackgroundResource(R.drawable.message_img_morra_stone2);
		} else if (finger1 == 1) {
			leftFinger.setBackgroundResource(R.drawable.message_img_morra_cloth2);
		} else if (finger1 == 2) {
			leftFinger.setBackgroundResource(R.drawable.message_img_morra_scissors2);
		}
		//右边玩家
		if (finger2 == 0) {
			rightFinger.setBackgroundResource(R.drawable.message_img_morra_stone1);
		} else if (finger2 == 1) {
			rightFinger.setBackgroundResource(R.drawable.message_img_morra_cloth1);
		} else if (finger2 == 2) {
			rightFinger.setBackgroundResource(R.drawable.message_img_morra_scissors1);
		}

		if (winuid != 0) {//获胜者昵称不为空且胜负不为0，则表示为不为平局
			if (winuid == uid2) {
				midFinger.setImageResource(R.drawable.message_results_win);
			} else {
				midFinger.setImageResource(R.drawable.message_results_lose);
			}
		} else {
			midFinger.setImageResource(R.drawable.message_results_flat);
		}

	}

	public void showDialog() {
		try {
			windowDeploy();
			// 设置触摸对话框意外的地方取消对话框
			setCanceledOnTouchOutside(true);
			show();

			mBaHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					dismiss();
				}
			}, 5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 设置窗口显示
	public void windowDeploy() {
		Window window = getWindow(); // 得到对话框
		final WindowManager.LayoutParams wlps = window.getAttributes();
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		wlps.gravity = Gravity.CENTER;
		window.setAttributes(wlps);
	}

}
