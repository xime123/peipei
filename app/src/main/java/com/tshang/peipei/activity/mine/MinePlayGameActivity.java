package com.tshang.peipei.activity.mine;

import java.util.Random;

import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.HintToastDialog;
import com.tshang.peipei.activity.dialog.participatePromptDialog;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.broadcast.WriteBroadCastBiz;
import com.tshang.peipei.view.PeiPeiCheckButton;
import com.tshang.peipei.view.PeiPeiCheckButton1;

/**
 * @Title: MinePlayGameActivity.java 
 *
 * @Description: 游戏场界面
 *
 * @author allen
 *
 * @date 2014-12-26 下午1:48:30 
 *
 * @version V1.0   
 */
public class MinePlayGameActivity extends BaseActivity {

	private PeiPeiCheckButton fingerNum100, fingerNum1000, fingerNum5000;
	private PeiPeiCheckButton fingerStone, fingerScissors, fingerCloth;
	private PeiPeiCheckButton1 fingerGold, fingerSilver;

	private int fingerCurr;//猜拳数值
	private int fingerNum = 1000;//猜拳赌注数值
	private int antetype = 0;

	private WriteBroadCastBiz writeBiz;
	private boolean isSending;

	@Override
	protected void initData() {
		fingerNum100.setOnClickListener(this);
		fingerNum1000.setOnClickListener(this);
		fingerNum5000.setOnClickListener(this);
		fingerStone.setOnClickListener(this);
		fingerScissors.setOnClickListener(this);
		fingerCloth.setOnClickListener(this);
		fingerGold.setOnClickListener(this);
		fingerSilver.setOnClickListener(this);

		findViewById(R.id.ll_play_games).setOnClickListener(this);
		findViewById(R.id.ll_play_games).setVisibility(View.GONE);
		findViewById(R.id.play_game_sure).setOnClickListener(this);
		findViewById(R.id.play_finger_record).setOnClickListener(this);
	}

	@Override
	protected void initRecourse() {
		writeBiz = new WriteBroadCastBiz(this, mHandler);

		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.str_broadcast);
		mBackText.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_send_game);

		fingerNum100 = (PeiPeiCheckButton) findViewById(R.id.broadcast_finger_100);
		fingerNum1000 = (PeiPeiCheckButton) findViewById(R.id.broadcast_finger_1000);
		fingerNum5000 = (PeiPeiCheckButton) findViewById(R.id.broadcast_finger_5000);
		fingerStone = (PeiPeiCheckButton) findViewById(R.id.broadcast_finger_stone);
		fingerScissors = (PeiPeiCheckButton) findViewById(R.id.broadcast_finger_scissors);
		fingerCloth = (PeiPeiCheckButton) findViewById(R.id.broadcast_finger_cloth);

		fingerGold = (PeiPeiCheckButton1) findViewById(R.id.broadcast_finger_gold);
		fingerSilver = (PeiPeiCheckButton1) findViewById(R.id.broadcast_finger_silver);

		fingerGold.setBackgroundRes(0, R.drawable.message_select_goldaward, true);
		fingerSilver.setBackgroundRes(0, R.drawable.message_select_silveraward, false);

		fingerGold.setTextAndColor(R.string.gold_money, getResources().getColor(R.color.finger_gold_color));
		fingerSilver.setTextAndColor(R.string.silver_money, getResources().getColor(R.color.finger_silver_color));

		Random r = new Random();
		fingerCurr = r.nextInt(3);
		fingerNum100.setBackgroundRes(0, R.drawable.message_select_gold1, false);
		fingerNum1000.setBackgroundRes(0, R.drawable.message_select_gold2, true);
		fingerNum5000.setBackgroundRes(0, R.drawable.message_select_gold3, false);

		fingerStone.setBackgroundRes(0, R.drawable.message_img_morra_stone1, false);
		fingerScissors.setBackgroundRes(0, R.drawable.message_img_morra_scissors1, false);
		fingerCloth.setBackgroundRes(0, R.drawable.message_img_morra_cloth1, false);
		if (fingerCurr == 0) {
			fingerStone.setCheck(true);
		} else if (fingerCurr == 1) {
			fingerCloth.setCheck(true);
		} else {
			fingerScissors.setCheck(true);
		}

		int width = (BasePhone.getScreenWidth(this) - BaseUtils.dip2px(this, 70)) / 3;
		LinearLayout.LayoutParams linParams = new LinearLayout.LayoutParams(width, width);

		fingerNum100.setBackgroudPading(BaseUtils.dip2px(this, 10));
		fingerNum1000.setBackgroudPading(BaseUtils.dip2px(this, 10));
		fingerNum5000.setBackgroudPading(BaseUtils.dip2px(this, 10));

		fingerStone.setLayoutParams(linParams);
		fingerScissors.setLayoutParams(linParams);
		fingerCloth.setLayoutParams(linParams);
		fingerNum100.setLayoutParams(linParams);
		fingerNum1000.setLayoutParams(linParams);
		fingerNum5000.setLayoutParams(linParams);
	}

	@Override
	protected int initView() {
		return R.layout.activity_play_game;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_play_games:
			MineFaqActivity.openMineFaqActivity(this, MineFaqActivity.GAMES_VALUE);
			break;
		case R.id.broadcast_finger_100:
			fingerNum = 100;
			fingerNum100.setCheck(true);
			fingerNum1000.setCheck(false);
			fingerNum5000.setCheck(false);
			break;
		case R.id.broadcast_finger_1000:
			fingerNum = 1000;
			fingerNum100.setCheck(false);
			fingerNum1000.setCheck(true);
			fingerNum5000.setCheck(false);
			break;
		case R.id.broadcast_finger_5000:
			fingerNum = 5000;
			fingerNum100.setCheck(false);
			fingerNum1000.setCheck(false);
			fingerNum5000.setCheck(true);
			break;
		case R.id.broadcast_finger_stone:
			fingerCurr = 0;
			fingerStone.setCheck(true);
			fingerScissors.setCheck(false);
			fingerCloth.setCheck(false);
			break;
		case R.id.broadcast_finger_scissors:
			fingerCurr = 2;
			fingerStone.setCheck(false);
			fingerScissors.setCheck(true);
			fingerCloth.setCheck(false);
			break;
		case R.id.broadcast_finger_cloth:
			fingerCurr = 1;
			fingerStone.setCheck(false);
			fingerScissors.setCheck(false);
			fingerCloth.setCheck(true);
			break;
		case R.id.broadcast_finger_gold:
			antetype = 0;
			fingerGold.setBackgroundRes(0, R.drawable.message_select_goldaward, true);
			fingerSilver.setBackgroundRes(0, R.drawable.message_select_silveraward, false);
			fingerNum100.setBackgroundRes(0, R.drawable.message_select_gold1, fingerNum == 100);
			fingerNum1000.setBackgroundRes(0, R.drawable.message_select_gold2, fingerNum == 1000);
			fingerNum5000.setBackgroundRes(0, R.drawable.message_select_gold3, fingerNum == 5000);
			break;
		case R.id.broadcast_finger_silver:
			antetype = 1;
			fingerGold.setBackgroundRes(0, R.drawable.message_select_goldaward, false);
			fingerSilver.setBackgroundRes(0, R.drawable.message_select_silveraward, true);
			fingerNum100.setBackgroundRes(0, R.drawable.message_select_silver1, fingerNum == 100);
			fingerNum1000.setBackgroundRes(0, R.drawable.message_select_silver2, fingerNum == 1000);
			fingerNum5000.setBackgroundRes(0, R.drawable.message_select_silver3, fingerNum == 5000);
			break;
		case R.id.play_game_sure:
			if (isSending) {
				return;
			}
			isSending = writeBiz.sendFingerBroadcast(this, BAConstants.PEIPEI_BROADCASET, fingerCurr, fingerNum, "", antetype);
			break;
		case R.id.play_finger_record:
			MineFaqActivity.openMineFaqActivity(this, MineFaqActivity.FINGER_VALUE);
			break;
		default:
			break;
		}
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.BROADCAST_SEND_SUCCESS_VALUE:
			if (msg.arg1 != -10) {
				isSending = false;
			}

			if (msg.arg1 == 0) {
				this.finish();
			} else if (msg.arg1 == rspContMsgType.E_GG_FORBIT) {
				new HintToastDialog(this, R.string.limit_talk, R.string.ok).showDialog();
			} else if (msg.arg1 == rspContMsgType.E_GG_NOT_ENGOUH_WELTH) {//财富不够
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, true, 0, 0).showDialog();
			} else if (msg.arg1 == rspContMsgType.E_GG_LACK_OF_SILVER) {//财富不够
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, false, 0, 0).showDialog();
			}
			break;
		default:
			break;
		}

	}

}
