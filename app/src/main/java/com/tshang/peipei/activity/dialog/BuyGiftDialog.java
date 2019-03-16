package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.reward.RewardListActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfo;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * @Title: BuyGiftDialog.java 
 *
 * @Description: 购买礼物
 *
 * @author allen  
 *
 * @date 2014-7-24 下午3:21:47 
 *
 * @version V1.0   
 */
public class BuyGiftDialog extends Dialog implements OnClickListener {

	private Activity context;
	private int gold, silver;
	private String nick, name;
	private int loyaltyeffect, charmeffect;
	private String pickey;
	private BAHandler mHandler;
	private int from;

	private GiftInfo giftInfo;
	private EditText et;
	private CheckBox cb;
	private TextView totalTv;
	private TextView pendantTv;
	private LinearLayout pendantLayout;
	private TextView anonmyText;

	public BuyGiftDialog(Activity context, int gold, int silver, String nick, String name, int loyaltyeffect, int charmeffect, String pickey,
			GiftInfo giftInfo, BAHandler mHandler, int from) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		this.gold = gold;
		this.silver = silver;
		this.nick = nick;
		this.name = name;
		this.loyaltyeffect = loyaltyeffect;
		this.charmeffect = charmeffect;
		this.pickey = pickey;
		this.giftInfo = giftInfo;
		this.mHandler = mHandler;
		this.from = from;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_buygift);

		et = (EditText) findViewById(R.id.dialog_gift_buy_num);
		cb = (CheckBox) findViewById(R.id.dialog_gift_buy_checkbox);
		TextView goldTV = (TextView) findViewById(R.id.dialog_gift_gold);
		TextView silverTV = (TextView) findViewById(R.id.dialog_gift_silver);
		totalTv = (TextView) findViewById(R.id.dialog_gift_cost_tatal);
		pendantLayout = (LinearLayout) findViewById(R.id.dialog_gift_pendnt_layout);
		pendantTv = (TextView) findViewById(R.id.dialog_gift_pendnt_tv);
		anonmyText = (TextView) findViewById(R.id.dialog_gift_buy_anonym_text);
		goldTV.setText(gold + "");
		silverTV.setText(silver + "");
		TextView tvName = (TextView) findViewById(R.id.dialog_gift_text);
		tvName.setText(String.format(context.getResources().getString(R.string.str_gift_content), nick, name, loyaltyeffect, charmeffect));
		ImageView iv = (ImageView) findViewById(R.id.dialog_gift_image);
		DisplayImageOptions options = ImageOptionsUtils.getImageKeyOptions(context);
		ImageLoader.getInstance().displayImage("http://" + pickey + BAConstants.LOAD_180_APPENDSTR, iv, options);
		et.setSelection(1);

		//判断挂件实物
		if (giftInfo.revint0.intValue() == 1) {
			pendantLayout.setVisibility(View.VISIBLE);
			pendantTv.setText(new String(giftInfo.revstr0));
			findViewById(R.id.dialog_gift_buy_layout).setVisibility(View.GONE);
		} else {
			pendantLayout.setVisibility(View.GONE);
			findViewById(R.id.dialog_gift_buy_layout).setVisibility(View.VISIBLE);
		}

		if (giftInfo.pricegold.intValue() != 0) {
			totalTv.setText(giftInfo.pricegold.intValue() + "金币");
		} else {
			totalTv.setText(giftInfo.pricesilver.intValue() + "银币");
		}

		et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s)) {
					totalTv.setText("");
				} else {
					if (giftInfo.pricegold.intValue() != 0) {
						totalTv.setText(giftInfo.pricegold.intValue() * Integer.parseInt(s.toString().trim()) + "金币");
					} else {
						totalTv.setText(giftInfo.pricesilver.intValue() * Integer.parseInt(s.toString().trim()) + "银币");
					}
				}
			}
		});
		setCanceledOnTouchOutside(true);

		findViewById(R.id.dialog_gift_buy_button).setOnClickListener(this);
		findViewById(R.id.dialog_gift_buy_minus).setOnClickListener(this);
		findViewById(R.id.dialog_gift_buy_plus).setOnClickListener(this);

		Window w = getWindow();
		WindowManager.LayoutParams wlps = w.getAttributes();
		wlps.width = context.getResources().getDisplayMetrics().widthPixels * 5 / 6;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.gravity = Gravity.CENTER;
		w.setAttributes(wlps);

		if (from == RewardListActivity.CHAT_FROM_REWARD) {
			cb.setVisibility(View.GONE);
			anonmyText.setVisibility(View.GONE);
		} else {
			cb.setVisibility(View.VISIBLE);
			anonmyText.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_gift_buy_button:
			Message msg = Message.obtain();
			msg.what = HandlerType.GIFT_BUY_RETURN;
			String num = et.getText().toString();
			if (TextUtils.isEmpty(num)) {
				BaseUtils.showTost(context, "礼物数量不能为空!");
				return;
			}
			msg.arg1 = Integer.parseInt(num);
			if (cb.isChecked()) {
				msg.arg2 = 1;
			} else {
				msg.arg2 = 0;
			}
			mHandler.sendMessage(msg);
			dismiss();
			break;
		case R.id.dialog_gift_buy_minus:
			String num0 = et.getText().toString();
			if (TextUtils.isEmpty(num0)) {
				num0 = 1 + "";
				et.setText(num0);
			} else {
				int count = Integer.parseInt(num0);
				count--;
				if (count < 1) {
					count = 1;
				}
				et.setText(count + "");
				if (giftInfo.pricegold.intValue() != 0) {
					totalTv.setText(giftInfo.pricegold.intValue() * count + "金币");
				} else {
					totalTv.setText(giftInfo.pricesilver.intValue() * count + "银币");
				}
			}
			et.setSelection(et.getText().toString().length());
			break;
		case R.id.dialog_gift_buy_plus:
			String num1 = et.getText().toString();
			if (TextUtils.isEmpty(num1)) {
				num1 = 1 + "";
				et.setText(num1);
			} else {
				int count = Integer.parseInt(num1);
				count++;
				et.setText(count + "");
				if (giftInfo.pricegold.intValue() != 0) {
					totalTv.setText(giftInfo.pricegold.intValue() * count + "金币");
				} else {
					totalTv.setText(giftInfo.pricesilver.intValue() * count + "银币");
				}
			}
			et.setSelection(et.getText().toString().length());
			break;
		default:
			break;
		}
	}

	public void showDialog() {
		try {
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
