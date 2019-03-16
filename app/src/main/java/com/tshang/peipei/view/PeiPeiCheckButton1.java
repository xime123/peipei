package com.tshang.peipei.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tshang.peipei.R;

/**
 * @Title: PeiPeiCheckButton1.java 
 *
 * @Description: 点击
 *
 * @author allen  
 *
 * @date 2014-12-17 下午19:59:14 
 *
 * @version V1.0   
 */
public class PeiPeiCheckButton1 extends LinearLayout {

	private int backgroundid1 = -1, backgroundid2 = -1;
	private Button btnBackground;
	private ImageView imageCheck;
	private boolean isCheck;

	public boolean isCheck() {
		return isCheck;
	}

	public PeiPeiCheckButton1(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUi(context);
	}

	public PeiPeiCheckButton1(Context context) {
		super(context);
		initUi(context);
	}

	private void initUi(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.view_checkbutton1, null);
		btnBackground = (Button) view.findViewById(R.id.view_checkbutton_button);
		imageCheck = (ImageView) view.findViewById(R.id.view_checkbutton_check);

		addView(view);
	}

	public void setBackgroundRes(int resid1, int resid2, boolean b) {
		backgroundid1 = resid1;
		backgroundid2 = resid2;

		setCheck(b);
	}

	public void setTextAndColor(int str, int color) {
		btnBackground.setText(str);
		btnBackground.setTextColor(color);
	}

	public void setCheck(boolean b) {
		isCheck = b;
		if (b) {
			btnBackground.setBackgroundResource(backgroundid2);
			btnBackground.getBackground().setAlpha(255);
			imageCheck.setVisibility(View.VISIBLE);
		} else {
			if (backgroundid1 != 0) {
				btnBackground.setBackgroundResource(backgroundid1);
			} else {
				btnBackground.setBackgroundResource(backgroundid2);
				btnBackground.getBackground().setAlpha(88);
			}
			imageCheck.setVisibility(View.GONE);
		}
	}

	public void setBackgroudPading(int padding) {
		RelativeLayout.LayoutParams linParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		linParams.setMargins(padding, padding, padding, padding);
		linParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		imageCheck.setLayoutParams(linParams);
	}
}
