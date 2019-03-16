package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.ILog;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.user.DynamicRequseControl;
import com.tshang.peipei.model.bizcallback.BizCallBackDeleteTopic;
import com.tshang.peipei.model.request.RequestDeleteTopic.IDeleteTopic;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsInfo;
import com.tshang.peipei.protocol.asn.gogirl.TopicInfo;
import com.tshang.peipei.storage.database.operate.PublishOperate;

/**
 * 删除个人发表的动态dialog
 * @author Jeff
 *
 */
public class DeleteDynamicInfoDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener, IDeleteTopic,
		BizCallBackDeleteTopic {

	private Activity context;
	private DynamicsInfo info;
	private int position;
	private int systemtopicid = -1;
	private BAHandler mHandler;

	public DeleteDynamicInfoDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public DeleteDynamicInfoDialog(Activity context, int theme, DynamicsInfo info, int position, int systemtopicid, BAHandler mHandler) {
		super(context, theme);
		this.context = context;
		this.info = info;
		this.position = position - 2;
		this.mHandler = mHandler;
		this.systemtopicid = systemtopicid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_view);
		TextView tvMsg = (TextView) findViewById(R.id.msg);
		tvMsg.setText(R.string.dialog_msg1);

		Button btn = (Button) findViewById(R.id.ok_sure);
		btn.setText(R.string.ok);
		btn.setOnClickListener(this);
		findViewById(R.id.ok_cancel).setOnClickListener(this);

		//		showDialog();
	}

	public void showDialog() {
		//		try {
		windowDeploy();
		//			
		//			if (info != null
		//					&& new BAParseRspData().parseTopicInfo(context, info.topiccontentlist, Gender.FEMALE.getValue()).getType() != BAConstants.MessageType.GIFT
		//							.getValue()) {//礼物不删除
		//				show();
		//			}
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}
		// 设置触摸对话框意外的地方取消对话框
		setCanceledOnTouchOutside(true);
		show();
	}

	// 设置窗口显示
	public void windowDeploy() {
		Window window = getWindow(); // 得到对话框
		final WindowManager.LayoutParams wlps = window.getAttributes();
		wlps.width = context.getResources().getDisplayMetrics().widthPixels * 3 / 4;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.3f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		wlps.gravity = Gravity.CENTER;
		window.setAttributes(wlps);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok_cancel:
			this.dismiss();
			break;
		case R.id.ok_sure://去登录
			this.dismiss();
			if (info.id.intValue() < 0) {//删除的是本地发送失败的数据
				if (BAApplication.mLocalUserInfo != null) {
					PublishOperate publisOperate = PublishOperate.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "");
					publisOperate.deleteCurrentTime(String.valueOf(info.createtime));
				}
			} else {//删除的是网络数据
				Dialog dialog = DialogFactory.createLoadingDialog(context, R.string.delete_loading);
				DialogFactory.showDialog(dialog);
				DynamicRequseControl control = new DynamicRequseControl();
				ILog.d("Aaron", "delete  uid==" + info.uid.intValue() + ", topicid==" + info.topicid.intValue() + ", dynamicsType="
						+ info.dynamicstype.intValue());
				control.deleteDynamic(info.uid.intValue(), info.topicid.intValue(), systemtopicid, info.dynamicstype.intValue(), dialog, mHandler);
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void deleteTopicCallBack(int retCode) {
		mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.SPACE_DELETE_TOPICINFO_VALUE, retCode, position));
	}

}
