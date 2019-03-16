/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tshang.peipei.view.fall;

import java.util.ArrayList;
import java.util.Random;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.tshang.peipei.R;

/**
 * This class is the custom view where all of the Droidflakes are drawn. This
 * class has all of the logic for adding, subtracting, and rendering
 * Droidflakes.
 */
@SuppressLint("NewApi")
public class StarFallFlakeView1 extends View {
	StarAnimationEndListener roselistener;
	private Context context;
	Bitmap droid; // The bitmap that all flakes use
	Bitmap droid1;
	Bitmap star;
	int numFlakes = 0; // Current number of flakes
	ArrayList<StarFallFlake1> flakes = new ArrayList<StarFallFlake1>(); // List of current flakes

	// Animator used to drive all separate flake animations. Rather than have
	// potentially
	// hundreds of separate animators, we just use one and then update all
	// flakes for each
	// frame of that single animation.
	ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
	long startTime, prevTime; // Used to track elapsed time for animations and
								// fps
	Matrix m = new Matrix(); // Matrix used to translate/rotate each flake
								// during rendering
	String fpsString = "";
	String numFlakesString = "";

	Paint mPaint;
	int alpha_value = 2;
	int timeTmp = 0;

	/**
	 * Constructor. Create objects used throughout the life of the View: the
	 * Paint and the animator
	 */
	public StarFallFlakeView1(Context context, StarAnimationEndListener listener) {
		super(context);
		this.context = context;
		roselistener = listener;
		droid = BitmapFactory.decodeResource(getResources(), R.drawable.broadcast_magic_flowrain_red1);
		droid1 = BitmapFactory.decodeResource(getResources(), R.drawable.broadcast_magic_flowrain_yellow1);
		star = BitmapFactory.decodeResource(getResources(), R.drawable.star);
		// This listener is where the action is for the flak animations. Every
		// frame of the
		// animation, we calculate the elapsed time and update every flake's
		// position and rotation
		// according to its speed.
		mPaint = new Paint();
		mPaint.setColor(Color.TRANSPARENT);

		animator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator arg0) {
				long nowTime = System.currentTimeMillis();
				float secs = (float) (nowTime - prevTime) / 1000f;
				prevTime = nowTime;

				for (int i = 0; i < numFlakes; ++i) {
					StarFallFlake1 flake = flakes.get(i);
					if (!flake.isAlpha) {
						flake.speed += 4;
						flake.y += ((flake.speed) * secs) + 2;
						flake.x -= (flake.speed * secs);
						if (flake.y > getHeight()) {
							// If a flake falls off the bottom, send it back to the
							// top
							flake.y = 0 - flake.height;
							if (flake.speed >= 250) {
								flake.speed = 50 + (float) Math.random() * 150;
							}
							timeTmp++;
							if (roselistener != null && timeTmp > numFlakes) {
								roselistener.onEndAnimation();
							}
						}
						if (flake.x < 0) {
							// If a flake falls off the bottom, send it back to the
							// top
							flake.x = (float) Math.random() * (getWidth() - flake.width);
							if (flake.speed >= 250) {
								flake.speed = 50 + (float) Math.random() * 150;
							}
						}
						flake.rotation = flake.rotation + (flake.rotationSpeed * secs);
					} else {
						flake.alpha += alpha_value;
						if (flake.alpha >= 255) {
							alpha_value = -2;
						}
						if (flake.alpha <= 255) {
							alpha_value = 2;
						}
					}
				}
				// Force a redraw to see the flakes in their new positions and
				// orientations
				invalidate();
			}
		});
		animator.setRepeatCount(ValueAnimator.INFINITE);
		animator.setDuration(3000);
		animator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				animator.cancel();
				if (roselistener != null) {
					roselistener.onEndAnimation();
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
	}

	int getNumFlakes() {
		return numFlakes;
	}

	private void setNumFlakes(int quantity) {
		numFlakes = quantity;
		numFlakesString = "numFlakes: " + numFlakes;
	}

	/**
	 * Add the specified number of droidflakes.
	 */
	void addFlakes(int quantity) {
		for (int i = 0; i < quantity; ++i) {
			int a = new Random().nextInt(2);
			if (a == 0) {
				flakes.add(StarFallFlake1.createFlake(context, getWidth(), getHeight(), droid, false));
			} else if (a == 1) {
				flakes.add(StarFallFlake1.createFlake(context, getWidth(), getHeight(), droid1, false));
			}
		}
		int a = new Random().nextInt(1) + 5;
		for (int i = 0; i < a; ++i) {
			flakes.add(StarFallFlake1.createFlake(context, getWidth(), getHeight(), star, true));
		}
		setNumFlakes(numFlakes + quantity + a);
	}

	/**
	 * Subtract the specified number of droidflakes. We just take them off the
	 * end of the list, leaving the others unchanged.
	 */
	void subtractFlakes(int quantity) {
		for (int i = 0; i < quantity; ++i) {
			int index = numFlakes - i - 1;
			flakes.remove(index);
		}
		setNumFlakes(numFlakes - quantity);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// Reset list of droidflakes, then restart it with 8 flakes
		flakes.clear();
		numFlakes = 0;
		addFlakes(15);
		// Cancel animator in case it was already running
		animator.cancel();
		// Set up fps tracking and start the animation
		startTime = System.currentTimeMillis();
		prevTime = startTime;
		animator.start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// For each flake: back-translate by half its size (this allows it to
		// rotate around its center),
		// rotate by its current rotation, translate by its location, then draw
		// its bitmap

		for (int i = 0; i < numFlakes; ++i) {
			StarFallFlake1 flake = flakes.get(i);
			if (!flake.isAlpha) {
				m.setTranslate(-flake.width / 2, -flake.height / 2);
				m.postRotate(flake.rotation);
				m.postTranslate(flake.width / 2 + flake.x, flake.height / 2 + flake.y);
				canvas.drawBitmap(flake.bitmap, m, null);
			} else {
				mPaint.setAlpha(flake.alpha);
				Rect dst = new Rect();// 屏幕位置及尺寸
				// 下面的 dst 是表示 绘画这个图片的位置
				dst.left = (int) flake.x; // miDTX,//这个是可以改变的，也就是绘图的起点X位置
				dst.top = (int) flake.y; // mBitQQ.getHeight();//这个是QQ图片的高度。
											// 也就相当于 桌面图片绘画起点的Y坐标
				dst.right = (int) (flake.x + flake.width); // miDTX +
															// mBitDestTop.getWidth();//
															// 表示需绘画的图片的右上角
				dst.bottom = (int) (flake.y + flake.height); // mBitQQ.getHeight()
																// +
																// mBitDestTop.getHeight();//表示需绘画的图片的右下角

				canvas.drawBitmap(flake.bitmap, null, dst, mPaint);
			}
		}
		// fps counter: count how many frames we draw and once a second
		// calculate the
		// frames per second
	}

	public void pause() {
		// Make sure the animator's not spinning in the background when the
		// activity is paused.
		animator.cancel();
	}

	public void resume() {
		animator.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		try {
			if (roselistener != null) {
				roselistener.onEndAnimation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	public interface StarAnimationEndListener {
		public void onEndAnimation();
	}
}
