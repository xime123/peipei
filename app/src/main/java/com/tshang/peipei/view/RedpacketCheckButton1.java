package com.tshang.peipei.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tshang.peipei.R;

/**
 * @Title: PeiPeiCheckButton.java 
 *
 * @Description: 点击
 *
 * @author DYH  
 *
 * @date 2016-1-18 下午14:42:14 
 *
 * @version V1.0   
 */
public class RedpacketCheckButton1 extends LinearLayout {

	private int backgroundid1 = -1, backgroundid2 = -1;
	private ImageView imageBackground, imageCheck;
	private boolean isCheck;

	public boolean isCheck() {
		return isCheck;
	}

	public RedpacketCheckButton1(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUi(context);
	}

	public RedpacketCheckButton1(Context context) {
		super(context);
		initUi(context);
	}

	private void initUi(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.red_checkbutton1, null);
		imageBackground = (ImageView) view.findViewById(R.id.view_checkbutton_image);
		imageCheck = (ImageView) view.findViewById(R.id.view_checkbutton_check);

		addView(view);
	}

	public void setBackgroundRes(int resid1, int resid2, boolean b) {
		backgroundid1 = resid1;
		backgroundid2 = resid2;

		setCheck(b);
	}

	public void setCheck(boolean b) {
		isCheck = b;
		if (b) {
			imageBackground.setBackgroundResource(backgroundid2);
			imageBackground.getBackground().setAlpha(255);
			imageCheck.setVisibility(View.VISIBLE);
		} else {
			if (backgroundid1 != 0) {
				imageBackground.setBackgroundResource(backgroundid1);
			} else {
				imageBackground.setBackgroundResource(backgroundid2);
				imageBackground.getBackground().setAlpha(88);
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
