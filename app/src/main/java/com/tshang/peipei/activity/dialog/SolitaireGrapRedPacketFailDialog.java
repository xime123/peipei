package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.base.FormatUtil;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;

/**
 * @Title: SolitaireGrapRedPacketFailDialog.java 
 *
 * @Description: 抢红包失败信息弹框
 *
 * @author DYH  
 *
 * @date 2015-12-9 下午2:36:07 
 *
 * @version V1.0   
 */
public class SolitaireGrapRedPacketFailDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener {
	private Activity context;
	private TextView tv_money;
	private TextView tv_money_type;
	private TextView tv_ok;
	private RedPacketBetCreateInfo checkRedpacketInfo;

	public SolitaireGrapRedPacketFailDialog(Activity context) {
		super(context);
		this.context = context;
	}
	
	public SolitaireGrapRedPacketFailDialog(Activity context, int theme, RedPacketBetCreateInfo checkRedpacketInfo) {
		super(context, theme);
		this.context = context;
		this.checkRedpacketInfo = checkRedpacketInfo;
	}

	/* (non-Javadoc)
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_grap_solitaire_redpacket_fail);
		
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_money_type = (TextView) findViewById(R.id.tv_money_type);
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_ok = (TextView) findViewById(R.id.tv_ok);
		
		if(checkRedpacketInfo != null){
			if(checkRedpacketInfo.userInfo != null){
				if(checkRedpacketInfo.orggoldcoin.intValue() > 0){
					tv_money_type.setText(context.getString(R.string.str_redpacket_gold_add, context.getString(R.string.gold_money)));
				}else{
					tv_money_type.setText(context.getString(R.string.str_redpacket_gold_add, context.getString(R.string.silver_money)));
				}
				tv_money.setText(FormatUtil.formatNumber(checkRedpacketInfo.totalgoldcoin.longValue()));
			}
		}
		
		tv_ok.setOnClickListener(this);
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

	@Override
	public void onDismiss(DialogInterface arg0) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_ok:
			this.dismiss();
			break;
		}
	}
}
