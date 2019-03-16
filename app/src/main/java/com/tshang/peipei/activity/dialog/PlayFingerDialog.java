package com.tshang.peipei.activity.dialog;

import java.util.Random;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.mine.MineFaqActivity;
import com.tshang.peipei.protocol.asn.gogirl.FingerGuessingInfo;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants.Finger;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.model.biz.chat.groupchat.FingerGruessMessage;
import com.tshang.peipei.view.PeiPeiCheckButton;
import com.tshang.peipei.view.PeiPeiCheckButton1;

/**
 * @Title: PlayFingerDialog.java 
 *
 * @Description: 猜拳游戏对话框
 *
 * @author allen  
 *
 * @date 2014-9-10 上午10:20:10 
 *
 * @version V1.1.0   
 */
public class PlayFingerDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener {

	private Activity context;
	private PeiPeiCheckButton checkStone, checkScissors, checkCloth;
	private PeiPeiCheckButton _100_coin, _1000_coin, _5000_coin;
	private PeiPeiCheckButton1 playGold, playSilver;
	private BAHandler handler;
	private SpannableStringBuilder content;
	private FingerGuessingInfo fingerInfo;
	private int bet = 0;
	private boolean isGroupChat;
	private int uid;
	private int sex;
	private String friendName = "";
	private int antetype = 0;

	public PlayFingerDialog(Activity context, int sex, boolean isGroupChat, int uid, String friendName, BAHandler handler) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		this.sex = sex;
		this.handler = handler;
		this.isGroupChat = isGroupChat;
		this.uid = uid;
		this.friendName = friendName;
	}

	public PlayFingerDialog(Activity context, SpannableStringBuilder content, BAHandler handler, FingerGuessingInfo fingerInfo) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		this.handler = handler;
		this.content = content;
		this.fingerInfo = fingerInfo;
		this.antetype = fingerInfo.antetype.intValue();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_finger);
		TextView tvMsg = (TextView) findViewById(R.id.tv_guess_title);
		if (!TextUtils.isEmpty(content)) {
			tvMsg.setText(content);
			findViewById(R.id.tv_coin_select).setVisibility(View.GONE);
			findViewById(R.id.ll_view_coin_select).setVisibility(View.GONE);
		}

		int width = (BasePhone.getScreenWidth(context) * 3 / 4 - BaseUtils.dip2px(context, 36)) / 3;
		LinearLayout.LayoutParams linParams = new LinearLayout.LayoutParams(width, width);

		_100_coin = (PeiPeiCheckButton) findViewById(R.id.dialog_finger_100);
		_1000_coin = (PeiPeiCheckButton) findViewById(R.id.dialog_finger_1000);
		_5000_coin = (PeiPeiCheckButton) findViewById(R.id.dialog_finger_5000);
		_100_coin.setLayoutParams(linParams);
		_1000_coin.setLayoutParams(linParams);
		_5000_coin.setLayoutParams(linParams);

		playGold = (PeiPeiCheckButton1) findViewById(R.id.play_finger_gold);
		playSilver = (PeiPeiCheckButton1) findViewById(R.id.play_finger_silver);
		playGold.setOnClickListener(this);
		playSilver.setOnClickListener(this);

		checkStone = (PeiPeiCheckButton) findViewById(R.id.dialog_finger_stone);
		checkScissors = (PeiPeiCheckButton) findViewById(R.id.dialog_finger_scissors);
		checkCloth = (PeiPeiCheckButton) findViewById(R.id.dialog_finger_cloth);
		checkStone.setLayoutParams(linParams);
		checkScissors.setLayoutParams(linParams);
		checkCloth.setLayoutParams(linParams);

		checkStone.setBackgroundRes(0, R.drawable.message_img_morra_stone1, false);
		checkScissors.setBackgroundRes(0, R.drawable.message_img_morra_scissors1, false);
		checkCloth.setBackgroundRes(0, R.drawable.message_img_morra_cloth1, false);

		playGold.setBackgroundRes(0, R.drawable.message_select_goldaward, true);
		playSilver.setBackgroundRes(0, R.drawable.message_select_silveraward, false);
		playGold.setTextAndColor(R.string.gold_money, context.getResources().getColor(R.color.finger_gold_color));
		playSilver.setTextAndColor(R.string.silver_money, context.getResources().getColor(R.color.finger_silver_color));

		if (fingerInfo != null) {
			findViewById(R.id.play_monkey_ll).setVisibility(View.GONE);
			findViewById(R.id.play_monkey_tv).setVisibility(View.GONE);
		}

		findViewById(R.id.play_finger_record).setOnClickListener(this);

		_100_coin.setBackgroundRes(0, R.drawable.message_select_gold1, false);
		_1000_coin.setBackgroundRes(0, R.drawable.message_select_gold2, false);
		_5000_coin.setBackgroundRes(0, R.drawable.message_select_gold3, false);
		int whatGuess = new Random().nextInt(3);
		if (whatGuess == 0) {
			checkStone.setCheck(true);
		} else if (whatGuess == 1) {
			checkCloth.setCheck(true);
		} else if (whatGuess == 2) {
			checkScissors.setCheck(true);
		}

		_100_coin.setOnClickListener(this);
		_1000_coin.setOnClickListener(this);
		_5000_coin.setOnClickListener(this);

		checkStone.setOnClickListener(this);
		checkScissors.setOnClickListener(this);
		checkCloth.setOnClickListener(this);

		findViewById(R.id.ok_sure).setOnClickListener(this);
		findViewById(R.id.ok_cancel).setOnClickListener(this);
		this.setOnDismissListener(this);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		checkStone.setCheck(true);
		checkScissors.setCheck(true);
		checkCloth.setCheck(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok_sure:
			int checkid = -1;
			if (checkStone.isCheck()) {
				checkid = Finger.STONE.getValue();
			} else if (checkScissors.isCheck()) {
				checkid = Finger.SCISSORS.getValue();
			} else if (checkCloth.isCheck()) {
				checkid = Finger.CLOTH.getValue();
			}

			if (checkid > 2) {
				BaseUtils.showTost(context, "请选择您要出的猜拳");
				return;
			}
			if (fingerInfo != null) {
				HandlerUtils.sendHandlerMessage(handler, HandlerType.CHAT_FINGER_START, checkid, checkid, fingerInfo);
			} else {
				FingerGruessMessage.getInstance().playFinger(context, uid, null, isGroupChat, null, friendName, sex, handler, false, checkid, bet,
						antetype);
			}

			dismiss();
			break;
		case R.id.ok_cancel:
			dismiss();
			break;
		case R.id.dialog_finger_stone:
			checkStone.setCheck(true);
			checkScissors.setCheck(false);
			checkCloth.setCheck(false);
			break;
		case R.id.dialog_finger_scissors:
			checkScissors.setCheck(true);
			checkStone.setCheck(false);
			checkCloth.setCheck(false);
			break;
		case R.id.dialog_finger_cloth:
			checkCloth.setCheck(true);
			checkScissors.setCheck(false);
			checkStone.setCheck(false);
			break;
		case R.id.dialog_finger_100:
			if (_100_coin.isCheck()) {
				_100_coin.setCheck(false);
				bet = 0;
			} else {
				bet = 100;
				_100_coin.setCheck(true);
			}
			_1000_coin.setCheck(false);
			_5000_coin.setCheck(false);
			break;
		case R.id.dialog_finger_1000:
			_100_coin.setCheck(false);
			if (_1000_coin.isCheck()) {
				_1000_coin.setCheck(false);
				bet = 0;
			} else {
				bet = 1000;
				_1000_coin.setCheck(true);
			}

			_5000_coin.setCheck(false);
			break;
		case R.id.dialog_finger_5000:
			_100_coin.setCheck(false);
			_1000_coin.setCheck(false);

			if (_5000_coin.isCheck()) {
				_5000_coin.setCheck(false);
				bet = 0;
			} else {
				_5000_coin.setCheck(true);
				bet = 5000;
			}
			break;
		case R.id.play_finger_gold:
			antetype = 0;
			playGold.setBackgroundRes(0, R.drawable.message_select_goldaward, true);
			playSilver.setBackgroundRes(0, R.drawable.message_select_silveraward, false);
			_100_coin.setBackgroundRes(0, R.drawable.message_select_gold1, bet == 100);
			_1000_coin.setBackgroundRes(0, R.drawable.message_select_gold2, bet == 1000);
			_5000_coin.setBackgroundRes(0, R.drawable.message_select_gold3, bet == 5000);
			break;
		case R.id.play_finger_silver:
			antetype = 1;
			playGold.setBackgroundRes(0, R.drawable.message_select_goldaward, false);
			playSilver.setBackgroundRes(0, R.drawable.message_select_silveraward, true);
			_100_coin.setBackgroundRes(0, R.drawable.message_select_silver1, bet == 100);
			_1000_coin.setBackgroundRes(0, R.drawable.message_select_silver2, bet == 1000);
			_5000_coin.setBackgroundRes(0, R.drawable.message_select_silver3, bet == 5000);
			break;
		case R.id.play_finger_record:
			MineFaqActivity.openMineFaqActivity(context, MineFaqActivity.FINGER_VALUE);
			break;
		default:
			break;
		}

	}

	public void showDialog() {
		try {
			windowDeploy();
			// 设置触摸对话框意外的地方取消对话框
			setCanceledOnTouchOutside(true);
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 设置窗口显示
	public void windowDeploy() {
		Window window = getWindow(); // 得到对话框
		final WindowManager.LayoutParams wlps = window.getAttributes();
		wlps.width = context.getResources().getDisplayMetrics().widthPixels * 3 / 4;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		wlps.gravity = Gravity.CENTER;
		window.setAttributes(wlps);
	}

}
