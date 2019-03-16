package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.base.FormatUtil;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastRedPacketInfo;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * @Title: CanGrapSolitaireRedpacketDialog.java 
 *
 * @Description: 用于展示可以抢的大厅红包 
 *
 * @author DYH  
 *
 * @date 2016-1-19 上午11:16:44 
 *
 * @version V1.0   
 */
public class GrapHallNormalRedpacketSuccessDialog extends Dialog implements OnDismissListener, android.view.View.OnClickListener {

	private Activity activity;
	private ImageView iv_close;
	private ImageView iv_avatar;
	private TextView tv_sender;
	private TextView tv_desc;
	private TextView tv_money;
	private TextView tv_money_type;
	private BAHandler mHandler;
	private ImageLoader imageLoader;
	private DisplayImageOptions options_head;
	private BroadcastRedPacketInfo grabHallRedPacketInfo;

	public GrapHallNormalRedpacketSuccessDialog(Activity activity) {
		super(activity);
		this.activity = activity;
	}

	public GrapHallNormalRedpacketSuccessDialog(Activity activity, int theme, BroadcastRedPacketInfo grabHallRedPacketInfo, BAHandler mHandler) {
		super(activity, theme);
		this.activity = activity;
		this.mHandler = mHandler;
		imageLoader = ImageLoader.getInstance();
		options_head = ImageOptionsUtils.GetHeadKeyBigRounded(activity);
		this.grabHallRedPacketInfo = grabHallRedPacketInfo;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_grap_hall_normal_redpacket_success);
		iv_close = (ImageView) findViewById(R.id.iv_close);
		iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
		tv_sender = (TextView) findViewById(R.id.tv_sender);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_money_type = (TextView) findViewById(R.id.tv_money_type);
		
		if(grabHallRedPacketInfo != null){
			if (grabHallRedPacketInfo.type.intValue() == 0) {
				if(grabHallRedPacketInfo.userInfo != null){
					setHeadImage(iv_avatar, grabHallRedPacketInfo.userInfo.uid.intValue());
					tv_sender.setText(new String(grabHallRedPacketInfo.userInfo.nick));
				}
			} else {
				iv_avatar.setImageResource(R.drawable.logo_rounded);
				tv_sender.setText(R.string.str_official);
			}
			tv_money.setText(FormatUtil.formatNumber(grabHallRedPacketInfo.getgoldcoin.longValue()) + "");
			if(grabHallRedPacketInfo.totalgoldcoin.longValue() > 0){
				tv_money_type.setText(activity.getString(R.string.gold_money));
			}else{
				tv_money_type.setText(activity.getString(R.string.silver_money));
			}
			if(grabHallRedPacketInfo.desc != null){
				tv_desc.setText(new String(grabHallRedPacketInfo.desc));
			}
		}
		
		iv_close.setOnClickListener(this);
		GrapHallNormalRedpacketSuccessDialog.this.setOnDismissListener(this);
	}
	
	protected void setHeadImage(ImageView iv, int uid) {
		imageLoader.displayImage("http://" + uid + BAConstants.LOAD_HEAD_UID_APPENDSTR, iv, options_head);
	}

	@Override
	public void onDismiss(DialogInterface arg0) {

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
		wlps.width = activity.getResources().getDisplayMetrics().widthPixels * 9 / 10;
		wlps.height = activity.getResources().getDisplayMetrics().heightPixels * 4 / 6;;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		wlps.gravity = Gravity.CENTER;
		window.setAttributes(wlps);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_close:
			this.dismiss();
			break;

		default:
			break;
		}

	}
}
