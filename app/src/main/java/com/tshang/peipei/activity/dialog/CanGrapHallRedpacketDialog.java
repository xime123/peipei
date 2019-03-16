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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.redpacket2.adapter.HallRedpacketDataAdapter;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.redpacket2.HallRedpacket;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastRedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastRedPacketInfoList;

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
public class CanGrapHallRedpacketDialog extends Dialog implements OnDismissListener, android.view.View.OnClickListener, OnItemClickListener {

	private Activity activity;
	private ListView listview;
	private ImageView iv_close;
	private HallRedpacketDataAdapter adapter;
	private BAHandler mHandler;
	private BroadcastRedPacketInfoList listData;

	public CanGrapHallRedpacketDialog(Activity activity) {
		super(activity);
		this.activity = activity;
	}

	public CanGrapHallRedpacketDialog(Activity activity, int theme, BroadcastRedPacketInfoList listData, BAHandler mHandler) {
		super(activity, theme);
		this.activity = activity;
		this.mHandler = mHandler;
		this.listData = listData;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_can_grap_hall_redpacket);
		listview = (ListView) findViewById(R.id.listview);
		iv_close = (ImageView) findViewById(R.id.iv_close);
		adapter = new HallRedpacketDataAdapter(activity);
		listview.setAdapter(adapter);
		adapter.appendToList(adapter.getHallRedpacketListData(listData));
		iv_close.setOnClickListener(this);
		listview.setOnItemClickListener(this);
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (adapter.getList() != null && adapter.getList().size() > arg2) {
			BroadcastRedPacketInfo info = adapter.getList().get(arg2);
			if (info != null) {
				HallRedpacket redpacket = new HallRedpacket();
				redpacket.setRedpacketId(info.id.intValue());
				HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_CHECK_REDPACKET_STATUS, redpacket);
				this.dismiss();
			}
		}
	}

}
