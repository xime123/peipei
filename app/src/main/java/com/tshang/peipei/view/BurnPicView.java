package com.tshang.peipei.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;

@SuppressLint("ViewConstructor")
public class BurnPicView extends LinearLayout {

	private ImageView mImageView;
	private ProgressBar pb;
	private TextView tv;
	private RepeatButton preButton;

	private Bitmap bmp;

	public RepeatButton getPreButton() {
		return preButton;
	}

	public void setPreButton(RepeatButton preButton) {
		this.preButton = preButton;
	}

	public BurnPicView(Context context) {
		super(context);
		initUI(context);
	}

	public BurnPicView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUI(context);
	}

	private void initUI(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.view_showpicbyburn, null);

		mImageView = (ImageView) view.findViewById(R.id.showpic_ll);
		pb = (ProgressBar) view.findViewById(R.id.showpic_pb);
		tv = (TextView) view.findViewById(R.id.showpic_tv);

		addView(view);
	}

	public void setBurnViewUI(Context context, int pro, String content) {
		if (pro == -1) {
			pb.setVisibility(View.INVISIBLE);
			tv.setVisibility(View.INVISIBLE);
		} else {
			pb.setVisibility(View.VISIBLE);
			pb.setProgress(pro);
			tv.setVisibility(View.VISIBLE);

			tv.setText(pro / 1000 + "\"");
		}

		this.requestFocus();

		if (content.contains(BAConstants.PEIPEI_FILE)) {
			// bmp = BitmapFactory.decodeFile(picKey);
			byte[] b = BaseFile.getBytesByFilePath(content);
			if (b != null) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.RGB_565; // 默认是Bitmap.Config.ARGB_8888
				/* 下面两个字段需要组合使用 */
				options.inPurgeable = true;
				options.inInputShareable = true;
				bmp = BitmapFactory.decodeByteArray(b, 0, b.length, options);

				if (bmp != null) {
					// ivPic.setImageBitmap(bmp);

					@SuppressWarnings("deprecation")
					int width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
					@SuppressWarnings("deprecation")
					int height = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();

					DisplayMetrics metric = new DisplayMetrics();
					((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
					float den = (float) metric.density;

					double wf = bmp.getWidth() * den * 100 / width;
					double hf = bmp.getHeight() * den * 100 / height;
					if (wf > 100 || hf > 100) {
						if (wf > hf) {
							int dstHeight = (int) (bmp.getHeight() * den * 100 / wf);
							bmp = Bitmap.createScaledBitmap(bmp, width, dstHeight, false);
						} else {
							int dstWidth = (int) (bmp.getWidth() * den * 100 / hf);
							bmp = Bitmap.createScaledBitmap(bmp, dstWidth, height, false);
						}
					}

					mImageView.setImageDrawable(new BitmapDrawable(getResources(), bmp));
				}
			} else {
				BaseUtils.showTost(context, R.string.no_pic);
			}

			b = null;
		} else {
			setImage(context, content);
		}
	}

	public void setImage(Context context, String content) {
		//		BAImageFetcherUtil.getImageFetcher(context).loadImage(content, mImageView);
	}

}
