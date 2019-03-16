package com.tshang.peipei.activity.redpacket2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.HallRedPacketQuestionDialog;
import com.tshang.peipei.activity.dialog.SolitaireRedPacketQuestionDialog;
import com.tshang.peipei.activity.dialog.participatePromptDialog;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.FormatUtil;
import com.tshang.peipei.base.NumericUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.ReceiverChatData;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.redpacket2.SolitaireRedPacketBiz;
import com.tshang.peipei.model.request.RequestHallRedpacketTip.GetHallRedpacketTipCallBack;
import com.tshang.peipei.model.request.RequestIsSendHallRedpacket.GetIsSendHallRedpacketCallBack;
import com.tshang.peipei.model.request.RequestSendHallRedpacket.SendHallRedpacketCallBack;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.view.RedpacketCheckButton1;

import de.greenrobot.event.EventBus;

/**
 * @Title: SendHallRedpacketActivity.java 
 *
 * @Description: 大厅发送红包界面 
 *
 * @author DYH  
 *
 * @date 2016-1-16 下午7:30:59 
 *
 * @version V1.0   
 */
public class SendHallRedpacketActivity extends BaseActivity implements SendHallRedpacketCallBack, GetHallRedpacketTipCallBack, GetIsSendHallRedpacketCallBack {
	public static int REQUEST_CODE = 100;
	private ImageView title_iv_right;
	private RedpacketCheckButton1 redpacket_money_gold;
	private RedpacketCheckButton1 redpacket_money_silver;
	private TextView tv_total_amount;
	private TextView tv_money_type;
	private EditText et_redpacket_num;
	private EditText et_redpacket_amount;
	private EditText et_leave;
	private int sndType = RANDOM_REDPACKET;
	private static final int NORMAL_REDPACKET = 0;
	private static final int RANDOM_REDPACKET = 1;
	private boolean isGold = true; //是否金币
	private TextView tv_change_type;
	private TextView tv_everyone_amount_notice;
	private TextView tv_money;
	private TextView tv_notice;
	private TextView tv_total_money_type;
	private SolitaireRedPacketBiz redPacketBiz;
	
	private long lastClickTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initData() {
		redPacketBiz = new SolitaireRedPacketBiz();
		requestTip();
		requestHelp();
	}

	public static void openMineFaqActivity(Activity activity) {
		BaseUtils.openResultActivity(activity, SendHallRedpacketActivity.class, null, REQUEST_CODE);
	}

	@Override
	protected void initRecourse() {
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_hall_send_redpacket);
		title_iv_right = (ImageView) findViewById(R.id.title_iv_right);
		title_iv_right.setImageResource(R.drawable.redpacket_help_icon);
		title_iv_right.setVisibility(View.VISIBLE);
		redpacket_money_gold = (RedpacketCheckButton1) findViewById(R.id.redpacket_money_gold);
		redpacket_money_silver = (RedpacketCheckButton1) findViewById(R.id.redpacket_money_silver);
		redpacket_money_gold.setBackgroundRes(0, R.drawable.redpacket_gold_selected, true);
		redpacket_money_silver.setBackgroundRes(0, R.drawable.redpacket_silver_selected, false);
		redpacket_money_gold.setCheck(true);
		tv_total_amount = (TextView) findViewById(R.id.tv_total_amount);
		tv_money_type = (TextView) findViewById(R.id.tv_money_type);

		et_redpacket_num = (EditText) findViewById(R.id.et_redpacket_num);
		et_redpacket_num.setText("5");
		et_redpacket_amount = (EditText) findViewById(R.id.et_redpacket_amount);
		et_leave = (EditText) findViewById(R.id.et_leave);
		tv_change_type = (TextView) findViewById(R.id.tv_change_type);
		tv_everyone_amount_notice = (TextView) findViewById(R.id.tv_everyone_amount_notice);
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_total_money_type = (TextView) findViewById(R.id.tv_total_money_type);
		tv_notice = (TextView) findViewById(R.id.tv_notice);
		setListener();
	}

	private void setListener() {
		findViewById(R.id.title_tv_left).setOnClickListener(this);
		findViewById(R.id.tv_send).setOnClickListener(this);
		title_iv_right.setOnClickListener(this);
		redpacket_money_gold.setOnClickListener(this);
		redpacket_money_silver.setOnClickListener(this);
		tv_change_type.setOnClickListener(this);
		
		et_redpacket_num.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				String totalAmount = et_redpacket_amount.getText().toString().trim();
				long amount = NumericUtils.parseLong(totalAmount, 0);
				if(amount > 0){
					if(sndType == NORMAL_REDPACKET){
						String strNum = et_redpacket_num.getText().toString();
						int num = NumericUtils.parseInt(strNum, 0);
						if(num > 0){
							tv_money.setText(FormatUtil.formatNumber((amount * num)));
						}else{
							tv_money.setText(FormatUtil.formatNumber(NumericUtils.parseLong(totalAmount, 0)));
						}
					}else{
						tv_money.setText(FormatUtil.formatNumber(NumericUtils.parseLong(totalAmount, 0)));
					}
				}else{
					tv_money.setText(0 + "");
				}
			}
		});
		
		et_redpacket_amount.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				String totalAmount = et_redpacket_amount.getText().toString().trim();
				long amount = NumericUtils.parseLong(totalAmount, 0);
				if(amount > 0){
					if(sndType == NORMAL_REDPACKET){
						String strNum = et_redpacket_num.getText().toString();
						int num = NumericUtils.parseInt(strNum, 0);
						if(num > 0){
							tv_money.setText(FormatUtil.formatNumber((amount * num)));
						}else{
							tv_money.setText(FormatUtil.formatNumber(NumericUtils.parseLong(totalAmount, 0)));
						}
					}else{
						tv_money.setText(FormatUtil.formatNumber(NumericUtils.parseLong(totalAmount, 0)));
					}
				}else{
					tv_money.setText(0 + "");
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_iv_right:
			new HallRedPacketQuestionDialog(this, android.R.style.Theme_Translucent_NoTitleBar).showDialog();
			break;
		case R.id.redpacket_money_gold:
			redpacket_money_gold.setCheck(true);
			redpacket_money_silver.setCheck(false);
			tv_money_type.setText(R.string.gold_money);
			tv_total_money_type.setText(R.string.gold_money);
			et_redpacket_amount.setText("");
			et_redpacket_num.setText("5");
			tv_money.setText("0");
			isGold = true;
			break;
		case R.id.redpacket_money_silver:
			redpacket_money_gold.setCheck(false);
			redpacket_money_silver.setCheck(true);
			tv_money_type.setText(R.string.silver_money);
			tv_total_money_type.setText(R.string.silver_money);
			et_redpacket_amount.setText("");
			et_redpacket_num.setText("5");
			tv_money.setText("0");
			isGold = false;
			break;
		case R.id.tv_change_type:
			if (sndType == NORMAL_REDPACKET) {
				tv_total_amount.setText(R.string.str_total_amount_fight);
				if(isGold){
					tv_money_type.setText(R.string.gold_money);
				}else{
					tv_money_type.setText(R.string.silver_money);
				}
				tv_everyone_amount_notice.setText(R.string.str_get_amount_random_everyone);
				tv_change_type.setText(R.string.str_change_normal_redpacket);
				et_redpacket_amount.setText("");
				et_redpacket_num.setText("5");
				tv_money.setText("0");
				sndType = RANDOM_REDPACKET;
			} else {
				tv_total_amount.setText(R.string.str_single_amount);
				if(isGold){
					tv_money_type.setText(R.string.gold_money);
				}else{
					tv_money_type.setText(R.string.silver_money);
				}
				tv_everyone_amount_notice.setText(R.string.str_get_amount_fixed_everyone);
				tv_change_type.setText(R.string.str_change_luck_redpacket);
				et_redpacket_amount.setText("");
				et_redpacket_num.setText("5");
				tv_money.setText("0");
				sndType = NORMAL_REDPACKET;
			}
			break;
		case R.id.tv_send:
			long hallTime = System.currentTimeMillis() - lastClickTime;
			if (hallTime > 1000) {
				checkEnableSendRedpacket();
			}
			lastClickTime = System.currentTimeMillis();
			
			break;
		}
	}

	private void requestTip() {
		redPacketBiz.requestHallRedpacketTip(0, this);
	}
	
	private void requestHelp(){
		redPacketBiz.requestHallRedpacketTip(1, this);
	}

	private void sendRedpacket() {
		long totalgoldcoin = 0;
		long totalsilvercoin = 0;
		int portionnum;
		String desc = "";
		String money = et_redpacket_amount.getText().toString().trim();
		String num = et_redpacket_num.getText().toString().trim();
		num = num.replaceAll("^(0+)", "");
		String etDesc = et_leave.getText().toString();
		String hintDesc = et_leave.getHint().toString();
		
		if (isGold) {
			totalgoldcoin = NumericUtils.parseLong(money, 0);
		} else {
			totalsilvercoin = NumericUtils.parseLong(money, 0);
		}
		if(!TextUtils.isEmpty(etDesc)){
			desc = etDesc;
		}else if(!TextUtils.isEmpty(hintDesc)){
			desc = hintDesc;
		}
		if(TextUtils.isEmpty(desc)){
			desc = getString(R.string.str_good_luck);
		}
		
		portionnum = NumericUtils.parseInt(num, 0);
		
		if(TextUtils.isEmpty(money) || (totalgoldcoin <=0 && totalsilvercoin <= 0)){
			BaseUtils.showTost(this, R.string.str_please_input_redpacket_money);
			return;
		}
		
		if(TextUtils.isEmpty(num) || portionnum <= 0){
			BaseUtils.showTost(this, R.string.str_please_input_redpacket_num);
			return;
		}
		
		redPacketBiz.requestSendHallRedpacket(0, sndType, totalgoldcoin, totalsilvercoin, portionnum, desc, this);
	}
	
	private void checkEnableSendRedpacket(){
		redPacketBiz.requestIsSendHallRedpacket(1, this);
	}

	@Override
	protected int initView() {
		return R.layout.activity_send_hall_redpacket;
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.HALL_GET_REDPACKET_TIP_SUCCESS:
			int retCode = msg.arg1;
			if (retCode == 0) {
				String errMsg = (String) msg.obj;
				tv_notice.setText(errMsg);
			}else{
				Bundle bundle = msg.getData();
				String retMsg = bundle.getString("data");
				BaseUtils.showTost(this, retMsg);
			}
			break;
		case HandlerValue.HALL_GET_REDPACKET_TIP_ERROR:
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		case HandlerValue.HALL_SEND_REDPACKET_SUCCESS:
			if(msg.arg1 == 0){
				BroadcastInfo broadcastInfo = (BroadcastInfo) msg.obj;
				appentSolitaireBroadcast(broadcastInfo);
			} else if (msg.arg1 == rspContMsgType.E_GG_NOT_ENGOUH_WELTH) {//财富不够
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, true, 0, 0).showDialog();
			} else if (msg.arg1 == rspContMsgType.E_GG_LACK_OF_SILVER) {//财富不够银币
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, false, 0, 0).showDialog();
			} else{
				Bundle bundle = msg.getData();
				String retMsg = bundle.getString("data");
				BaseUtils.showTost(this, retMsg);
			}
			break;
		case HandlerValue.HALL_SEND_REDPACKET_ERROR:
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		case HandlerValue.HALL_GET_REDPACKET_HELP_SUCCESS:
			if (msg.arg1 == 0) {
				String helpMsg = (String) msg.obj;
				BAApplication.hallRedHelp = helpMsg;
			}
			break;
		case HandlerValue.HALL_GET_ENABLE_SEND_REDPACKET_SUCCESS:
			if(msg.arg1 == 0){
				if(msg.arg2 == 1){
					sendRedpacket();
				}else{
					String retMsg = (String) msg.obj;
					BaseUtils.showTost(this, retMsg);
				}
			}else{
				String retMsg = (String) msg.obj;
				BaseUtils.showTost(this, retMsg);
			}
			break;
		case HandlerValue.HALL_GET_ENABLE_SEND_REDPACKET_ERROR:
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		default:
			break;
		}
	}
	
	private void appentSolitaireBroadcast(BroadcastInfo info) {
		NoticeEvent noticeEvent = new NoticeEvent();
		noticeEvent.setFlag(NoticeEvent.NOTICE101);
		noticeEvent.setObj(info);
		EventBus.getDefault().post(noticeEvent);
		finishPreActivity();
	}
	
	private void finishPreActivity(){
		setResult(REQUEST_CODE);
		finish();
	}

	@Override
	public void sendHallRedpacketOnSuccess(int code, String retMsg, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.HALL_SEND_REDPACKET_SUCCESS, code, obj, retMsg);
	}

	@Override
	public void sendHallRedpacketOnError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.HALL_SEND_REDPACKET_ERROR, code);
	}

	@Override
	public void getHallRedpacketTipOnSuccess(int code, String retMsg, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.HALL_GET_REDPACKET_TIP_SUCCESS, code, obj, retMsg);
	}

	@Override
	public void getHallRedpacketTipOnError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.HALL_GET_REDPACKET_TIP_ERROR, code);
	}

	@Override
	public void getHallRedpacketHelpOnSuccess(int code, String retMsg, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.HALL_GET_REDPACKET_HELP_SUCCESS, 0, obj);
	}

	@Override
	public void getIsSendHallRedpacketOnSuccess(int code, int obj, String retMsg) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_GET_ENABLE_SEND_REDPACKET_SUCCESS, code, obj, retMsg);
	}

	@Override
	public void getIsSendHallSolitaireRedpacketOnSuccess(int code, int obj, String retMsg) {
		
	}

	@Override
	public void getIsSendHallRedpacketOnError(int code) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_GET_ENABLE_SEND_REDPACKET_ERROR, code);
	}

}
