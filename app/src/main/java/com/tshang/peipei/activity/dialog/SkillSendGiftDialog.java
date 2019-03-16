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
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.UserPropertyInfo;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.request.RequestGetRelasionship.IGetRelationship;
import com.tshang.peipei.model.request.RequestParticipate.IParticipate;
import com.tshang.peipei.model.skill.SkillJoin;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * 送礼dialog
 * @author Jeff
 *
 */
public class SkillSendGiftDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener, IParticipate {

	private Activity context;
	private UserPropertyInfo userPropertyInfo;
	private String giftKey = "";
	private String userName = "";
	private String giftName = "";
	private int loyaltyValue = 0;
	private int charmValue = 0;
	private int giftCount = 0;
	private int pircegold = 0;
	private int pricesilver = 0;
	private int skillId = -1;
	private int otherUid = -1;
	private IGetRelationship callback;
//	private BizCallBackSendGiftSuccess sendGiftCallBack;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private BAHandler handler;

	public SkillSendGiftDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public SkillSendGiftDialog(Activity context, int theme, UserPropertyInfo userPropertyInfo, String giftKey, String userName, String giftName,
			int loyaltyValue, int charmValue, int giftCount, int pircegold, int pricesilver, int skillId, int otherUid,BAHandler handler, IGetRelationship callback,
			BizCallBackSendGiftSuccess giftCallBack) {
		super(context, theme);
		this.context = context;
		this.userPropertyInfo = userPropertyInfo;
		this.giftKey = giftKey;
		this.userName = userName;
		this.giftName = giftName;
		this.loyaltyValue = loyaltyValue;
		this.charmValue = charmValue;
		this.giftCount = giftCount;
		this.pircegold = pircegold;
		this.pricesilver = pricesilver;
		this.skillId = skillId;
		this.otherUid = otherUid;
		this.callback = callback;
		this.handler = handler;
//		this.sendGiftCallBack = giftCallBack;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_buygift_from_skill);
		findViewById(R.id.dialog_gift_buy_button).setOnClickListener(this);
		TextView tv_gold_money = (TextView) findViewById(R.id.tv_gold_money);
		TextView tv_silver_money = (TextView) findViewById(R.id.tv_silver_money);
		tv_silver_money.setText(String.valueOf(userPropertyInfo.silvercoin.intValue()));
		tv_gold_money.setText(String.valueOf(userPropertyInfo.goldcoin.intValue()));
		ImageView imageview = (ImageView) findViewById(R.id.dialog_gift_image);
		imageLoader = ImageLoader.getInstance();
		options = ImageOptionsUtils.getImageKeyOptions(context);
		imageLoader.displayImage("http://" + giftKey + BAConstants.LOAD_180_APPENDSTR, imageview, options);

		TextView textView = (TextView) findViewById(R.id.dialog_gift_text);
		textView.setText("赠送给" + userName + "的礼物" + giftName + giftCount + "个,将增加你对她的 魅力贡献值" + (giftCount * loyaltyValue) + "点，并提高她的魅力值"
				+ (giftCount * charmValue) + "点！");
		ImageView ivMoney = (ImageView) findViewById(R.id.iv_dialog_money);
		TextView tvMoney = (TextView) findViewById(R.id.tv_dialog_money);
		if (pricesilver > 0) {
			ivMoney.setImageResource(R.drawable.homepage_skill_presented_sfb);
			tvMoney.setText((pricesilver * giftCount) + "银币");
		}
		if (pircegold > 0) {
			textView.setText(R.string.str_skill_send_skill_toast);
			ivMoney.setImageResource(R.drawable.homepage_skill_presented_sfb2);
			tvMoney.setText((pircegold * giftCount) + "金币");
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
		wlps.width = context.getResources().getDisplayMetrics().widthPixels * 3 / 4;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.6f;
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
		case R.id.dialog_gift_buy_button://赠送
			this.dismiss();

			if (pricesilver > 0) {//说明是银币礼物
				if (userPropertyInfo.silvercoin.intValue() < pricesilver * giftCount) {//银币不足
					new participatePromptDialog(context, android.R.style.Theme_Translucent_NoTitleBar, false, userPropertyInfo.goldcoin.intValue(),
							userPropertyInfo.silvercoin.intValue()).showDialog();
					return;
				}
			}
			if (pircegold > 0) {//说明是金币礼物
				if (userPropertyInfo.goldcoin.intValue() < pircegold * giftCount) {//金币不足
					new participatePromptDialog(context, android.R.style.Theme_Translucent_NoTitleBar, true, userPropertyInfo.goldcoin.intValue(),
							userPropertyInfo.silvercoin.intValue()).showDialog();
					return;
				}
			}
			//			new SkillUtilsBiz(context).participate(otherUid, skillId, this);
			SkillJoin.getInstance().reqaddSkillDeal(context, skillId, otherUid, BAApplication.mLocalUserInfo.uid.intValue(), handler);
			break;

		default:
			break;
		}

	}

	@Override
	public void participateCallBack(int retCode, String retMsg) {
//		handler.sendMessage(handler.obtainMessage(PARTICIPATE_BACK, retCode, 0, retMsg));
	}

//	private static final int PARTICIPATE_BACK = 0x10;
//	@SuppressLint("HandlerLeak")
//	private Handler handler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			BaseUtils.cancelDialog();
//			int retCode = -1;
//			switch (msg.what) {
//			case PARTICIPATE_BACK:
//				retCode = msg.arg1;
//				if (retCode == 0) {//说明送礼成功
//					HashMap<String, String> m = new HashMap<String, String>();
//					m.put("__ct__", String.valueOf(pircegold * giftCount));
//					MobclickAgent.onEvent(context, "JiNengJiaoYiZongJinBiShu", m);
//					MobclickAgent.onEvent(context, "JiNengChengJiaoCiShu");
//					//					if (BAApplication.mUserEntity != null) {
//					//						MobclickAgent.onEvent(context, "JiNengChengJiaoRenShu", BAApplication.mUserEntity.getUid() + "");
//					//					}
//
//					new SkillGiftSuccessDialog(context, android.R.style.Theme_Translucent_NoTitleBar, retCode, callback).showDialog();
//					//继续回调到主页通知用户送礼成功
//					if (sendGiftCallBack != null) {
//						sendGiftCallBack.getSendGiftSuccess();
//					}
//				} else {
//					BaseUtils.showDialog(context, "送礼失败");
//				}
//				break;
//
//			default:
//				break;
//			}
//		}
//
//	};

	public interface BizCallBackSendGiftSuccess {
		public void getSendGiftSuccess();
	};
}
