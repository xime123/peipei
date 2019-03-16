package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.space.adapter.SelectGiftDialogAdapter;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfoList;

public class SelectGiftDialog extends Dialog implements android.view.View.OnClickListener, OnItemClickListener {

	private Activity context;
	private ListView listview;
	private GiftInfoList lists;
	private IGiftInfoItemOnClickCallBack giftInfoItemOnClickCallBack;
	private SelectGiftDialogAdapter adapter;

	public SelectGiftDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public SelectGiftDialog(Activity context, int theme, GiftInfoList lists, IGiftInfoItemOnClickCallBack giftInfoItemOnClickCallBack) {
		super(context, theme);
		this.context = context;
		this.lists = lists;
		this.giftInfoItemOnClickCallBack = giftInfoItemOnClickCallBack;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_select_gift);
		adapter = new SelectGiftDialogAdapter(context);
		findViewById(R.id.tv_select_gift_cancel).setOnClickListener(this);
		listview = (ListView) findViewById(R.id.lv_select_gift);
		listview.setOnItemClickListener(this);
		listview.setAdapter(adapter);
		for (Object object : lists) {//排除银币礼物，不要银币礼物
			GiftInfo info = (GiftInfo) object;
			if (info.pricegold.intValue() > 0 && info.id.intValue() < 50) {
				adapter.appendToList(info);
			}
		}
	}

	public void showDialog(int x, int y) {
		try {
			windowDeploy(x, y);

			// 设置触摸对话框意外的地方取消对话框
			setCanceledOnTouchOutside(true);
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 设置窗口显示
	public void windowDeploy(int x, int y) {
		final Window w = getWindow();
		w.setWindowAnimations(R.style.anim_popwindow_bottombar); // 设置窗口弹出动画
		final WindowManager.LayoutParams wlps = w.getAttributes();
		wlps.width = WindowManager.LayoutParams.MATCH_PARENT;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.gravity = Gravity.BOTTOM;
		w.setAttributes(wlps);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_select_gift_cancel:
			this.dismiss();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		GiftInfo giftInfo = (GiftInfo) parent.getAdapter().getItem(position);
		if (giftInfoItemOnClickCallBack != null && giftInfo != null) {
			giftInfoItemOnClickCallBack.getGiftInfoOnClickCallBack(giftInfo);
		}
	}

	public interface IGiftInfoItemOnClickCallBack {
		public void getGiftInfoOnClickCallBack(GiftInfo giftInfo);
	}
}
