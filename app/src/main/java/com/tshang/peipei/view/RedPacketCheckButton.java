package com.tshang.peipei.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;

/**
 * @Title: PeiPeiCheckButton.java 
 *
 * @Description: 点击
 *
 * @author DYH  
 *
 * @date 2014-7-10 下午1:59:14 
 *
 * @version V1.0   
 */
public class RedPacketCheckButton extends LinearLayout {
	private Context context;
	private int backgroundid1 = -1, backgroundid2 = -1;
	private TextView tv_value;
	private boolean isCheck;

	public boolean isCheck() {
		return isCheck;
	}

	public RedPacketCheckButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUi(context);
	}

	public RedPacketCheckButton(Context context) {
		super(context);
		initUi(context);
	}

	private void initUi(Context context) {
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.red_checkbutton, null);
		tv_value = (TextView) view.findViewById(R.id.tv_value);

		addView(view);
	}

	public void setBackgroundRes(int resid1, int resid2, boolean b) {
		backgroundid1 = resid1;
		backgroundid2 = resid2;

		setCheck(b);
	}
	
	public void setText(CharSequence text){
		tv_value.setText(text);
	}

	public void setCheck(boolean b) {
		isCheck = b;
		if (b) {
			tv_value.setBackgroundResource(backgroundid2);
			tv_value.setTextColor(context.getResources().getColor(R.color.default_checked_gold_text_color));
		} else {
			if (backgroundid1 != 0) {
				tv_value.setBackgroundResource(backgroundid1);
				tv_value.setTextColor(context.getResources().getColor(R.color.white));
			} else {
				tv_value.setBackgroundResource(backgroundid2);
				tv_value.setTextColor(context.getResources().getColor(R.color.default_checked_gold_text_color));
			}
		}
	}

}
