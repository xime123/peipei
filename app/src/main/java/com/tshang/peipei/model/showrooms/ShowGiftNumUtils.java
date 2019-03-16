package com.tshang.peipei.model.showrooms;

import android.view.View;
import android.widget.ImageView;

import com.tshang.peipei.R;

/**
 * @Title: ShowGiftNumUtils.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  秀刷礼物动画
 *
 * @date 2015年2月5日 下午2:18:08 
 *
 * @version V1.0   
 */
public class ShowGiftNumUtils {
	public static void showImageCount(ImageView imageView, ImageView imageView2, ImageView imageView3, int giftNum) {
		if (imageView == null || imageView2 == null || imageView3 == null) {
			return;
		}
		if (giftNum <= 0) {
			return;
		}

		int shiwei = 0, baiwei = 0, gewei = 0;

		baiwei = (giftNum % 1000) / 100;
		shiwei = (giftNum / 10) % 10;
		gewei = (giftNum % 100) % 10;
		if (baiwei > 0) {
			imageView3.setVisibility(View.VISIBLE);
			ShowSingleImageCount(imageView3, baiwei);
		} else {
			imageView3.setVisibility(View.GONE);
		}
		if (shiwei > 0) {
			imageView2.setVisibility(View.VISIBLE);
			ShowSingleImageCount(imageView2, shiwei);
		} else {
			imageView2.setVisibility(View.GONE);
		}

		ShowSingleImageCount(imageView, gewei);

	}

	public static void showImageCount1(ImageView imageView, ImageView imageView2, ImageView imageView3, int giftNum) {
		if (imageView == null || imageView2 == null || imageView3 == null) {
			return;
		}
		if (giftNum <= 0) {
			return;
		}

		int shiwei = 0, baiwei = 0, gewei = 0;

		baiwei = (giftNum % 1000) / 100;
		shiwei = (giftNum / 10) % 10;
		gewei = (giftNum % 100) % 10;
		if (baiwei > 0) {
			imageView3.setVisibility(View.VISIBLE);
			ShowSingleImageCount1(imageView3, baiwei);
		} else {
			imageView3.setVisibility(View.GONE);
		}
		if (shiwei > 0) {
			imageView2.setVisibility(View.VISIBLE);
			ShowSingleImageCount1(imageView2, shiwei);
		} else {
			imageView2.setVisibility(View.GONE);
		}

		ShowSingleImageCount1(imageView, gewei);

	}

	public static void showImageCount(ImageView imageView1, ImageView imageView2, ImageView imageView3, ImageView imageView4, int giftNum) {
		if (imageView1 == null || imageView2 == null || imageView3 == null || imageView4 == null) {
			return;
		}
		if (giftNum <= 0) {
			return;
		}

		int shiwei = 0, baiwei = 0, gewei = 0, qianwei = 0;

		qianwei = (giftNum % 10000) / 1000;
		baiwei = (giftNum % 1000) / 100;
		shiwei = (giftNum / 10) % 10;
		gewei = (giftNum % 100) % 10;
		if (qianwei > 0) {
			imageView4.setVisibility(View.VISIBLE);
			ShowSingleImageCount1(imageView4, baiwei);
		} else {
			imageView4.setVisibility(View.GONE);
		}
		if (baiwei > 0) {
			imageView3.setVisibility(View.VISIBLE);
			ShowSingleImageCount1(imageView3, baiwei);
		} else {
			imageView3.setVisibility(View.GONE);
		}
		if (shiwei > 0) {
			imageView2.setVisibility(View.VISIBLE);
			ShowSingleImageCount1(imageView2, shiwei);
		} else {
			imageView2.setVisibility(View.GONE);
		}

		ShowSingleImageCount1(imageView1, gewei);

	}

	private static void ShowSingleImageCount(ImageView imageView, int count) {
		if (count == 0) {
			imageView.setImageResource(R.drawable.casino_gif_gift_even_0);
		} else if (count == 1) {
			imageView.setImageResource(R.drawable.casino_gif_gift_even_1);
		} else if (count == 2) {
			imageView.setImageResource(R.drawable.casino_gif_gift_even_2);
		} else if (count == 3) {
			imageView.setImageResource(R.drawable.casino_gif_gift_even_3);
		} else if (count == 4) {
			imageView.setImageResource(R.drawable.casino_gif_gift_even_4);
		} else if (count == 5) {
			imageView.setImageResource(R.drawable.casino_gif_gift_even_5);
		} else if (count == 6) {
			imageView.setImageResource(R.drawable.casino_gif_gift_even_6);
		} else if (count == 7) {
			imageView.setImageResource(R.drawable.casino_gif_gift_even_7);
		} else if (count == 8) {
			imageView.setImageResource(R.drawable.casino_gif_gift_even_8);
		} else if (count == 9) {
			imageView.setImageResource(R.drawable.casino_gif_gift_even_9);
		}

	}

	private static void ShowSingleImageCount1(ImageView imageView, int count) {
		if (count == 0) {
			imageView.setImageResource(R.drawable.show_sharetoboardcast_num0);
		} else if (count == 1) {
			imageView.setImageResource(R.drawable.show_sharetoboardcast_num1);
		} else if (count == 2) {
			imageView.setImageResource(R.drawable.show_sharetoboardcast_num2);
		} else if (count == 3) {
			imageView.setImageResource(R.drawable.show_sharetoboardcast_num3);
		} else if (count == 4) {
			imageView.setImageResource(R.drawable.show_sharetoboardcast_num4);
		} else if (count == 5) {
			imageView.setImageResource(R.drawable.show_sharetoboardcast_num5);
		} else if (count == 6) {
			imageView.setImageResource(R.drawable.show_sharetoboardcast_num6);
		} else if (count == 7) {
			imageView.setImageResource(R.drawable.show_sharetoboardcast_num7);
		} else if (count == 8) {
			imageView.setImageResource(R.drawable.show_sharetoboardcast_num8);
		} else if (count == 9) {
			imageView.setImageResource(R.drawable.show_sharetoboardcast_num9);
		}

	}
}
