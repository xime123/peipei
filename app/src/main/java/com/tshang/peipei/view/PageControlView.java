package com.tshang.peipei.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tshang.peipei.R;

public class PageControlView extends LinearLayout {
	private Context context;
	public int count;
	public int image[] = { R.drawable.broadcast_img_dot_un, R.drawable.broadcast_img_dot_pr };
	private int index_page_gap;

	public PageControlView(Context context) {
		super(context);
		this.context = context;
		index_page_gap = (int) context.getResources().getDimension(R.dimen.default_padding_gridview_edge);
	}

	public PageControlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		index_page_gap = (int) context.getResources().getDimension(R.dimen.default_padding_gridview_edge);
	}

	public void generatePageControl(int currentPage) {
		this.removeAllViews();
		for (int i = 0; i < count; i++) {
			ImageView imageView = new ImageView(context);
			if (i == currentPage) {
				imageView.setImageResource(image[1]);
			} else {
				imageView.setImageResource(image[0]);
			}
			LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 0, index_page_gap, 0);
			imageView.setLayoutParams(lp);
			this.addView(imageView);
		}
	}
}
