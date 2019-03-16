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

import com.tshang.peipei.R;

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
import android.view.MotionEvent;
import android.view.View;


/**
 * This class is the custom view where all of the Droidflakes are drawn. This
 * class has all of the logic for adding, subtracting, and rendering
 * Droidflakes.
 */
@SuppressLint("NewApi")
public class PetalRainView extends View {
	PetalAnimationEndListener petallistener;
	private Context context;
	Bitmap droid1; // The bitmap that all flakes use
	int numFlakes = 0; // Current number of flakes
	ArrayList<RoseRainFlake> flakes = new ArrayList<RoseRainFlake>(); // List of current flakes

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
	public PetalRainView(Context context, PetalAnimationEndListener listener) {
		super(context);
		this.context = context;
		petallistener = listener;
		droid1 = BitmapFactory.decodeResource(getResources(), R.drawable.broadcast_magic_petal1);
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
					RoseRainFlake flake = flakes.get(i);
					int index = i % 16;

					float temp = flake.speed * secs * 2;
					switch (index) {
					case 0:
						flake.y += temp;
						flake.x += temp + (float) Math.random();
						break;
					case 1:
						flake.y -= temp;
						flake.x -= temp + (float) Math.random();
						break;
					case 2:
						flake.y -= temp + (float) Math.random();
						flake.x += temp;
						break;
					case 3:
						flake.y += temp;
						flake.x -= temp + (float) Math.random();
						break;
					case 4:
						flake.y -= temp + (float) Math.random();
						flake.x -= (float) Math.random();
						break;
					case 5:
						flake.y += temp + (float) Math.random();
						flake.x += (float) Math.random();
						break;
					case 6:
						flake.y -= (float) Math.random();
						flake.x += temp + (float) Math.random();
						break;
					case 7:
						flake.y += (float) Math.random();
						flake.x -= temp + (float) Math.random();
						break;
					case 8:
						flake.y -= temp + (float) Math.random();
						flake.x += (float) Math.random();
						break;
					case 9:
						flake.y += temp + (float) Math.random();
						flake.x -= (float) Math.random();
						break;
					case 10:
						flake.y += (float) Math.random();
						flake.x += temp + (float) Math.random();
						break;
					case 11:
						flake.y -= (float) Math.random();
						flake.x -= temp + (float) Math.random();
						break;
					case 12:
						flake.y += temp + (float) Math.random();
						flake.x += temp;
						break;
					case 13:
						flake.y -= temp + (float) Math.random();
						flake.x -= temp;
						break;
					case 14:
						flake.y -= temp;
						flake.x += temp + (float) Math.random();
						break;
					case 15:
						flake.y += temp + (float) Math.random();
						flake.x -= temp;
						break;
					default:
						break;
					}

					if (flake.y < 0 || flake.x > getWidth() || flake.y > getHeight() || flake.x < 0) {
						// If a flake falls off the bottom, send it back to the
						// top
						flake.y = getHeight() / 2;
						flake.x = getWidth() / 2;
						flake.speed = 20 + (float) Math.random() * 50;

						timeTmp++;
						if (petallistener != null && timeTmp >= numFlakes) {
							petallistener.onEndAnimation();
						}
					}
					flake.rotation = flake.rotation + (flake.rotationSpeed * secs);
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
				if (petallistener != null) {
					petallistener.onEndAnimation();
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
			flakes.add(RoseRainFlake.createFlake(context, getWidth(), getHeight(), droid1));
			
		}
		setNumFlakes(numFlakes + quantity);
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
		addFlakes(38);
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
			RoseRainFlake flake = flakes.get(i);
			m.setTranslate(-flake.width / 2, -flake.height / 2);
			m.postRotate(flake.rotation);
			m.postTranslate(flake.width / 2 + flake.x, flake.height / 2 + flake.y);
			canvas.drawBitmap(flake.bitmap, m, null);
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
			if (petallistener != null) {
				petallistener.onEndAnimation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	public interface PetalAnimationEndListener {
		public void onEndAnimation();
	}
}
