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
import android.widget.ScrollView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.redpacket2.adapter.HallRandomRedpacketAdapter;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastRedPacketInfo;
import com.tshang.peipei.vender.common.util.TimeUtils;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;
import com.tshang.peipei.view.PeiPeiListView;

/**
 * @Title: GrapHallRandomRedpacketSuccessDialog.java 
 *
 * @Description: 用于展示拼手气红包的详情
 *
 * @author DYH  
 *
 * @date 2016-1-19 上午11:16:44 
 *
 * @version V1.0   
 */
public class GrapHallRandomRedpacketLuckDialog extends Dialog implements OnDismissListener, android.view.View.OnClickListener {

	private Activity activity;
	private ImageView iv_close;
	private ImageView iv_avatar;
	private TextView tv_sender;
	private TextView tv_desc;
	private TextView tv_redpacket_desc;
	private PeiPeiListView listview;
	private BAHandler mHandler;
	private ImageLoader imageLoader;
	private DisplayImageOptions options_head;
	private BroadcastRedPacketInfo grabHallRedPacketInfo;
	private HallRandomRedpacketAdapter adapter;
	private ScrollView sv_scroll;

	public GrapHallRandomRedpacketLuckDialog(Activity activity) {
		super(activity);
		this.activity = activity;
	}

	public GrapHallRandomRedpacketLuckDialog(Activity activity, int theme, BroadcastRedPacketInfo grabHallRedPacketInfo, BAHandler mHandler) {
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
		setContentView(R.layout.dialog_grap_hall_random_redpacket_luck);
		iv_close = (ImageView) findViewById(R.id.iv_close);
		iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
		tv_sender = (TextView) findViewById(R.id.tv_sender);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		tv_redpacket_desc = (TextView) findViewById(R.id.tv_redpacket_desc);
		listview = (PeiPeiListView) findViewById(R.id.listview);
		sv_scroll = (ScrollView) findViewById(R.id.sv_scroll);
		adapter = new HallRandomRedpacketAdapter(activity, grabHallRedPacketInfo);
		listview.setAdapter(adapter);
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
			String totalmoney = "";
			if(grabHallRedPacketInfo.totalgoldcoin.longValue() > 0){
				totalmoney = grabHallRedPacketInfo.totalgoldcoin.longValue() + activity.getString(R.string.gold_money);
			}else{
				totalmoney = grabHallRedPacketInfo.totalsilvercoin.longValue() + activity.getString(R.string.silver_money);
			}
			if(grabHallRedPacketInfo.desc != null){
				tv_desc.setText(new String(grabHallRedPacketInfo.desc));
			}
			String strDes = activity.getString(R.string.str_the_redpacket_desc, grabHallRedPacketInfo.totalportionnum.longValue(), totalmoney);
//			if(grabHallRedPacketInfo.redpacketstatus.intValue() == 2 || grabHallRedPacketInfo.redpacketstatus.intValue() == 3){
//				strDes += activity.getString(R.string.str_the_redpacket_time_no_money, TimeUtils.getLeftTime(activity, grabHallRedPacketInfo.endtime.longValue()));
//			}
			if(grabHallRedPacketInfo.leftportionnum.intValue() <= 0){
				strDes += activity.getString(R.string.str_the_redpacket_time_no_money, TimeUtils.getLeftTime(activity, grabHallRedPacketInfo.needtime.longValue()));
			}
			tv_redpacket_desc.setText(strDes);
			
			if(grabHallRedPacketInfo.records != null && grabHallRedPacketInfo.records.size() > 0){
				adapter.appendToList(adapter.getHallRedpacketListData(grabHallRedPacketInfo, grabHallRedPacketInfo.records));
			}
			sv_scroll.smoothScrollTo(0, 0);
		}
		
		iv_close.setOnClickListener(this);
		GrapHallRandomRedpacketLuckDialog.this.setOnDismissListener(this);
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
