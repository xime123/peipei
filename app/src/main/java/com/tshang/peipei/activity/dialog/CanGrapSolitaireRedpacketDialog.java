package com.tshang.peipei.activity.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
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
import com.tshang.peipei.activity.chat.adapter.SolitaireAdapter;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.operate.RedpacketOperate;

/**
 * @Title: CanGrapSolitaireRedpacketDialog.java 
 *
 * @Description: 用于展示可以抢的红包 
 *
 * @author DYH  
 *
 * @date 2015-12-14 下午6:00:44 
 *
 * @version V1.0   
 */
public class CanGrapSolitaireRedpacketDialog extends Dialog implements OnDismissListener, android.view.View.OnClickListener, OnItemClickListener {

	private Activity activity;
	private int fuid;
	private ListView listview;
	private ImageView iv_close;
	private SolitaireAdapter adapter;
	private BAHandler mHandler;

	public CanGrapSolitaireRedpacketDialog(Activity activity) {
		super(activity);
		this.activity = activity;
	}

	public CanGrapSolitaireRedpacketDialog(Activity activity, int theme, int fuid, BAHandler mHandler) {
		super(activity, theme);
		this.fuid = fuid;
		this.activity = activity;
		this.mHandler = mHandler;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_can_grap_solitaire_redpacket);
		listview = (ListView) findViewById(R.id.listview);
		iv_close = (ImageView) findViewById(R.id.iv_close);
		adapter = new SolitaireAdapter(activity);
		listview.setAdapter(adapter);
		initData();
		iv_close.setOnClickListener(this);
		listview.setOnItemClickListener(this);
	}

	private void initData() {
		new AsyncLoadRedpacket().execute("");
	}

	@Override
	public void onDismiss(DialogInterface arg0) {

	}

	private class AsyncLoadRedpacket extends AsyncTask<String, Void, List<ChatDatabaseEntity>> {

		@SuppressWarnings("unchecked")
		@Override
		protected List<ChatDatabaseEntity> doInBackground(String... arg0) {
			RedpacketOperate operate = RedpacketOperate.getInstance(activity, fuid);
			List<ChatDatabaseEntity> list = new ArrayList<ChatDatabaseEntity>();
			if (operate != null) {
				list.addAll(operate.getMessageList());
				ComparatorList comparator = new ComparatorList();
				Collections.sort(list, comparator);
			}
			return list;
		}

		@Override
		protected void onPostExecute(List<ChatDatabaseEntity> result) {
			super.onPostExecute(result);
			adapter.clearList();
			adapter.appendToList(result);
			adapter.notifyDataSetChanged();
		}
	}
	
	public class ComparatorList implements Comparator {

		public int compare(Object arg0, Object arg1) {
			ChatDatabaseEntity dbEntity0 = (ChatDatabaseEntity) arg0;
			ChatDatabaseEntity dbEntity1 = (ChatDatabaseEntity) arg1;
			ChatMessageEntity msgEntity0 = ChatMessageBiz.decodeMessage(dbEntity0.getMessage());
			ChatMessageEntity msgEntity1 = ChatMessageBiz.decodeMessage(dbEntity1.getMessage());
			int num1 = msgEntity0.getJionPersons().size();
			int num2 = msgEntity1.getJionPersons().size();
			//首先比较年龄，如果年龄相同，则比较名字
			int flag = num2 - num1;
			if (flag == 0) {
				return msgEntity0.getCreatetime().compareTo(msgEntity1.getCreatetime());
			} else {
				return flag;
			}
		}

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
			ChatDatabaseEntity entity = adapter.getList().get(arg2);
			if (entity != null) {
				ChatMessageEntity messageEntity = ChatMessageBiz.decodeMessage(entity.getMessage());
				HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HAREM_CHECK_SOLITAIRE_REDPACKET_STATUS, messageEntity);
				this.dismiss();
			}
		}
	}

}
