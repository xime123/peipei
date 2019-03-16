package com.tshang.peipei.activity.dialog;

import java.math.BigInteger;
import java.util.List;

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
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.main.message.adapter.SelectSilverGiftDialogAdapter;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.babase.UserSharePreference;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.user.UserAccountBiz;
import com.tshang.peipei.model.biz.user.UserSettingBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetUserInfo;
import com.tshang.peipei.model.bizcallback.BizCallBackSetChatReshold;

public class SelectSilverGiftDialog extends Dialog implements OnItemClickListener, BizCallBackSetChatReshold, BizCallBackGetUserInfo {

	private Activity context;
	private ListView listview;
	private GiftInfoList lists;
	private SelectSilverGiftDialogAdapter adapter;
	private BAHandler handler;
	private int chatValue = 0;

	public SelectSilverGiftDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public SelectSilverGiftDialog(Activity context, int theme, GiftInfoList lists, BAHandler handler) {
		super(context, theme);
		this.context = context;
		this.lists = lists;
		this.handler = handler;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_set_private_chat);
		adapter = new SelectSilverGiftDialogAdapter(context);
		listview = (ListView) findViewById(R.id.lv_select_silver_gift);
		listview.setOnItemClickListener(this);
		listview.setAdapter(adapter);

		if (lists != null && !lists.isEmpty()) {
			GiftInfo giftInfo = (GiftInfo) lists.get(0);
			giftInfo.pricesilver = BigInteger.valueOf(0);
			giftInfo.id = BigInteger.valueOf(-1);
			adapter.appendToList(giftInfo);
		}

		for (Object object : lists) {//排除银币礼物，不要银币礼物
			GiftInfo info = (GiftInfo) object;
			int price = info.pricesilver.intValue();
			if (price > 0 && info.id.intValue() < 50 && price < 2000) {
				adapter.appendToList(info);
			}
		}
		if (BAApplication.mLocalUserInfo != null) {
			List<GiftInfo> lists = adapter.getList();
			if (lists != null && !lists.isEmpty()) {
				for (int i = 0, len = lists.size(); i < len; i++) {
					if (BAApplication.mLocalUserInfo.chatthreshold.intValue() == lists.get(i).loyaltyeffect.intValue()) {
						adapter.setSelectPos(i);
					}
				}
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		GiftInfo giftInfo = (GiftInfo) parent.getAdapter().getItem(position);
		adapter.setSelectPos(position);
		if (giftInfo != null) {
			int value = giftInfo.charmeffect.intValue();
			if (giftInfo.id.intValue() == -1) {
				value = 0;
			}
			chatValue = value;
			GoGirlUserInfo userEntity = UserUtils.getUserEntity(context);
			if (userEntity != null) {
				UserSettingBiz settingBiz = new UserSettingBiz();
				settingBiz.setChatReshold(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), value, this);
			}
		}
		this.dismiss();

	}

	@Override
	public void setChatResholdCallBack(int retCode) {
		if (retCode == 0) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.GIFT_CHAT_LIMIT_SUCCESS_VALUE);
			new UserAccountBiz(context).getUserInfo(this);
		} else if (retCode == rspContMsgType.E_GG_CHAT_THRESHOLD) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.GIFT_CHAT_LIMIT_FAILED_VALUE);
		}
	}

	@Override
	public void getUserInfoCallBack(int retCode, GoGirlUserInfo userinfo) {
		UserSharePreference.getInstance(context).saveUserByKey(userinfo);
		BAApplication.mLocalUserInfo = userinfo;
	}

}
