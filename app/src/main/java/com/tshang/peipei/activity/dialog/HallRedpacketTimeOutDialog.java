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
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastRedPacketInfo;
import com.tshang.peipei.vender.common.util.TimeUtils;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * @Title: HallRedpacketInfoDialog.java 
 *
 * @Description: 大厅红包超时 
 *
 * @author DYH  
 *
 * @date 2016-1-18 下午8:39:01 
 *
 * @version V1.0   
 */
public class HallRedpacketTimeOutDialog extends Dialog implements OnDismissListener, android.view.View.OnClickListener {
	private Activity context;
	private ImageLoader imageLoader;
	private DisplayImageOptions options_head;
	private BroadcastRedPacketInfo redpacketInfo;

	private ImageView iv_close;
	private ImageView iv_avatar;
	private TextView tv_sender;
	private TextView tv_timeout;

	public HallRedpacketTimeOutDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public HallRedpacketTimeOutDialog(Activity context, int theme, BroadcastRedPacketInfo redpacketInfo) {
		super(context, theme);
		this.context = context;
		this.redpacketInfo = redpacketInfo;
		imageLoader = ImageLoader.getInstance();
		options_head = ImageOptionsUtils.GetHeadKeyBigRounded(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_hall_redpacket_timeout);

		iv_close = (ImageView) findViewById(R.id.iv_close);
		iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
		tv_sender = (TextView) findViewById(R.id.tv_sender);
		tv_timeout = (TextView) findViewById(R.id.tv_timeout);

		if (redpacketInfo != null) {
			if (redpacketInfo.type.intValue() == 0) {
				if(redpacketInfo.userInfo != null){
					setHeadImage(iv_avatar, redpacketInfo.userInfo.uid.intValue());
					tv_sender.setText(new String(redpacketInfo.userInfo.nick));
				}
				
			} else {
				iv_avatar.setImageResource(R.drawable.logo);
				tv_sender.setText(R.string.str_official);
			}
			tv_timeout.setText(context.getString(R.string.str_the_hall_redpacket_time_out, TimeUtils.getLeftTime(context, redpacketInfo.timeouttime.longValue())));
		}
		
		setListener();
		HallRedpacketTimeOutDialog.this.setOnDismissListener(this);
	}
	
	private void setListener(){
		iv_close.setOnClickListener(this);
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
		wlps.height = context.getResources().getDisplayMetrics().heightPixels * 4 / 6;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		wlps.gravity = Gravity.CENTER;
		window.setAttributes(wlps);
	}

	protected void setHeadImage(ImageView iv, int uid) {
		imageLoader.displayImage("http://" + uid + BAConstants.LOAD_HEAD_UID_APPENDSTR, iv, options_head);
	}

	@Override
	public void onDismiss(DialogInterface arg0) {

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
