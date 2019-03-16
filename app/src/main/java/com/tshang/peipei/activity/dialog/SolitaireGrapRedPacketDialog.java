package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.base.FormatUtil;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * @Title: SolitaireRedPacketInfoDialog.java 
 *
 * @Description: 抢红包接龙确认弹框
 *
 * @author DYH  
 *
 * @date 2015-12-9 下午2:36:07 
 *
 * @version V1.0   
 */
public class SolitaireGrapRedPacketDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener {
	private Activity context;
	private ImageView iv_question;
	private ImageView iv_close;
	private ImageView iv_head;
	private TextView tv_money;
	private TextView tv_open_redpacket;
	private TextView tv_cancel;
	private BAHandler mHandler;
	private ImageLoader imageLoader;
	private DisplayImageOptions options_head;
	private RedPacketBetCreateInfo checkRedpacketInfo;
	private boolean isHall;

	public SolitaireGrapRedPacketDialog(Activity context) {
		super(context);
		this.context = context;
	}
	
	public SolitaireGrapRedPacketDialog(Activity context, int theme, boolean isHall, RedPacketBetCreateInfo checkRedpacketInfo, BAHandler mHandler) {
		super(context, theme);
		this.isHall = isHall;
		this.context = context;
		this.checkRedpacketInfo = checkRedpacketInfo;
		this.mHandler = mHandler;
		imageLoader = ImageLoader.getInstance();
		options_head = ImageOptionsUtils.GetHeadKeyBigRounded(context);
	}

	/* (non-Javadoc)
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_grap_solitaire_redpacket);
		
		iv_close = (ImageView) findViewById(R.id.iv_close);
		iv_question = (ImageView) findViewById(R.id.iv_question);
		iv_head = (ImageView) findViewById(R.id.iv_head);
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_open_redpacket = (TextView) findViewById(R.id.tv_open_redpacket);
		tv_cancel = (TextView) findViewById(R.id.tv_cancel);
		
		if(checkRedpacketInfo != null){
			if(checkRedpacketInfo.userInfo != null){
				String moneyType = context.getString(R.string.silver_money);
				String key1 = new String(checkRedpacketInfo.userInfo.headpickey) + "@true@210@210";
				imageLoader.displayImage("http://" + key1, iv_head, options_head);
				if(checkRedpacketInfo.orggoldcoin.intValue() > 0){
					moneyType = context.getString(R.string.gold_money);
				}
				tv_money.setText(FormatUtil.formatNumber(checkRedpacketInfo.comsume.intValue()) + moneyType);
			}
		}
		
		iv_close.setOnClickListener(this);
		iv_question.setOnClickListener(this);
		tv_open_redpacket.setOnClickListener(this);
		tv_cancel.setOnClickListener(this);
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
		wlps.width = context.getResources().getDisplayMetrics().widthPixels * 9 / 10;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		wlps.gravity = Gravity.CENTER;
		window.setAttributes(wlps);
	}

	@Override
	public void onDismiss(DialogInterface arg0) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_open_redpacket:
			this.dismiss();
			openRedpacket();
			break;
		case R.id.tv_cancel:
		case R.id.iv_close:
			this.dismiss();
			break;
		case R.id.iv_question:
			new SolitaireRedPacketQuestionDialog(context, android.R.style.Theme_Translucent_NoTitleBar, isHall, checkRedpacketInfo).showDialog();
			this.dismiss();
			break;
		}
	}
	
	private void openRedpacket(){
		if(isHall){
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_GRAB_SOLITAIRE_REDPACKET_ENSURE, checkRedpacketInfo);
		}else{
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HAREM_GRAB_SOLITAIRE_REDPACKET_ENSURE, checkRedpacketInfo);
		}
	}
}
