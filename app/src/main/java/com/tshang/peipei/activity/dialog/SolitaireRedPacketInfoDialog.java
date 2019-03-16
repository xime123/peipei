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
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * @Title: SolitaireRedPacketInfoDialog.java 
 *
 * @Description: 红包接龙信息弹框
 *
 * @author DYH  
 *
 * @date 2015-12-9 下午2:36:07 
 *
 * @version V1.0   
 */
public class SolitaireRedPacketInfoDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener {
	private Activity context;
	private ImageView iv_question;
	private ImageView iv_close;
	private ImageView iv_head;
	private TextView tv_money;
	private TextView tv_money_type;
	private TextView tv_sponsor;
	private TextView tv_des;
	private TextView tv_open_redpacket;
	private BAHandler mHandler;
	private Dialog dialog;
	private ImageLoader imageLoader;
	private DisplayImageOptions options_head;
	private int groupid = -1;
	private RedPacketBetCreateInfo checkRedpacketInfo;

	public SolitaireRedPacketInfoDialog(Activity context) {
		super(context);
		this.context = context;
	}
	
	public SolitaireRedPacketInfoDialog(Activity context, int theme, int groupid, RedPacketBetCreateInfo checkRedpacketInfo, BAHandler mHandler) {
		super(context, theme);
		this.context = context;
		this.groupid = groupid;
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
		setContentView(R.layout.dialog_solitaire_redpacket_info);
		
		iv_close = (ImageView) findViewById(R.id.iv_close);
		iv_question = (ImageView) findViewById(R.id.iv_question);
		iv_head = (ImageView) findViewById(R.id.iv_head);
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_sponsor = (TextView) findViewById(R.id.tv_sponsor);
		tv_money_type = (TextView) findViewById(R.id.tv_money_type);
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_des = (TextView) findViewById(R.id.tv_des);
		tv_open_redpacket = (TextView) findViewById(R.id.tv_open_redpacket);
		
		if(checkRedpacketInfo != null){
			if(checkRedpacketInfo.userInfo != null){
				String key1 = new String(checkRedpacketInfo.userInfo.headpickey) + "@true@210@210";
				imageLoader.displayImage("http://" + key1, iv_head, options_head);
				if(checkRedpacketInfo.records == null){
					tv_sponsor.setText(context.getString(R.string.str_current_redpacket_join_person, new String(checkRedpacketInfo.userInfo.nick), 0));
				}else{
					tv_sponsor.setText(context.getString(R.string.str_current_redpacket_join_person, new String(checkRedpacketInfo.userInfo.nick), checkRedpacketInfo.records.size()));
				}
				if(checkRedpacketInfo.orggoldcoin.intValue() > 0){
					tv_money_type.setText(context.getString(R.string.str_current_redpacket_money_type, context.getString(R.string.gold_money)));
				}else{
					tv_money_type.setText(context.getString(R.string.str_current_redpacket_money_type, context.getString(R.string.silver_money)));
				}
				tv_money.setText(FormatUtil.formatNumber(checkRedpacketInfo.totalgoldcoin.longValue()));
				tv_des.setText(new String(checkRedpacketInfo.comsumedesc));
			}
		}
		
		iv_close.setOnClickListener(this);
		iv_question.setOnClickListener(this);
		tv_open_redpacket.setOnClickListener(this);
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
			showEnsureDialog();
			break;
		case R.id.iv_close:
			this.dismiss();
			break;
		case R.id.iv_question:
			boolean ishall = groupid == 1 ? true : false;
			new SolitaireRedPacketQuestionDialog(context, android.R.style.Theme_Translucent_NoTitleBar, ishall, checkRedpacketInfo).showDialog();
			this.dismiss();
			break;
		}
	}
	
	private void showEnsureDialog(){
		this.dismiss();
		boolean ishall = groupid == 1 ? true : false;
		new SolitaireGrapRedPacketDialog(context, android.R.style.Theme_Translucent_NoTitleBar, ishall, checkRedpacketInfo, mHandler).showDialog();
		
	}
}
