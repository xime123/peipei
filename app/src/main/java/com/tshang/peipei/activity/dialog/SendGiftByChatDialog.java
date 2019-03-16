package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * @Title: SendGiftByChatDialog.java 
 *
 * @Description: 购买礼物
 *
 * @author allen  
 *
 * @date 2014-12-22 下午3:21:47 
 *
 * @version V1.0   
 */
public class SendGiftByChatDialog extends Dialog implements OnClickListener {

	private Activity context;
	private int gold, silver;
	private String nick, name;
	private String loyaltyeffect, charmeffect;
	private String pickey;
	private String giftsilver;
	private BAHandler mHandler;

	public SendGiftByChatDialog(Activity context, int gold, int silver, String nick, String name, String loyaltyeffect, String charmeffect,
			String pickey, String giftsilver, BAHandler mHandler) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		this.gold = gold;
		this.silver = silver;
		this.nick = nick;
		this.name = name;
		this.loyaltyeffect = loyaltyeffect;
		this.charmeffect = charmeffect;
		this.pickey = pickey;
		this.giftsilver = giftsilver;
		this.mHandler = mHandler;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_buygift2);

		TextView goldTV = (TextView) findViewById(R.id.dialog_gift_gold);
		TextView silverTV = (TextView) findViewById(R.id.dialog_gift_silver);
		goldTV.setText(gold + "");
		silverTV.setText(silver + "");
		TextView tvName = (TextView) findViewById(R.id.dialog_gift_text);
		tvName.setText(String.format(context.getResources().getString(R.string.str_gift_content), nick, name, loyaltyeffect, charmeffect) + "\n需要银币："
				+ giftsilver);
		ImageView iv = (ImageView) findViewById(R.id.dialog_gift_image);
		DisplayImageOptions options = ImageOptionsUtils.getImageKeyOptions(context);
		ImageLoader.getInstance().displayImage("http://" + pickey + BAConstants.LOAD_180_APPENDSTR, iv, options);

		setCanceledOnTouchOutside(true);

		findViewById(R.id.dialog_gift_buy_button).setOnClickListener(this);

		Window w = getWindow();
		WindowManager.LayoutParams wlps = w.getAttributes();
		wlps.width = context.getResources().getDisplayMetrics().widthPixels * 5 / 6;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.gravity = Gravity.CENTER;
		w.setAttributes(wlps);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_gift_buy_button:
			mHandler.sendEmptyMessage(HandlerType.GIFT_BUY_RETURN);
			dismiss();
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
