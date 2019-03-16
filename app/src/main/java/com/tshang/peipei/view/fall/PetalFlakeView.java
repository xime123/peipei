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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.tshang.peipei.R;

/**
 * This class is the custom view where all of the Droidflakes are drawn. This
 * class has all of the logic for adding, subtracting, and rendering
 * Droidflakes.
 */
public class PetalFlakeView extends View {

	private Context context;
	Bitmap droid; // The bitmap that all flakes use
	Bitmap droid1;
	//	Bitmap droid2;
	//	Bitmap droid3;
	int numFlakes = 0; // Current number of flakes
	ArrayList<SnowFlake> flakes = new ArrayList<SnowFlake>(); // List of current flakes

	// Animator used to drive all separate flake animations. Rather than have
	// potentially
	// hundreds of separate animators, we just use one and then update all
	// flakes for each
	// frame of that single animation.
	ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
	long startTime, prevTime; // Used to track elapsed time for animations and
	Matrix m = new Matrix(); // Matrix used to translate/rotate each flake
								// during rendering

	/**
	 * Constructor. Create objects used throughout the life of the View: the
	 * Paint and the animator
	 */
	public PetalFlakeView(Context context) {
		super(context);
		this.context = context;
		droid = BitmapFactory.decodeResource(getResources(), R.drawable.valentine_day_petal1);
		droid1 = BitmapFactory.decodeResource(getResources(), R.drawable.valentine_day_petal1);
		//		droid2 = BitmapFactory.decodeResource(getResources(), R.drawable.space_snow3);
		//		droid3 = BitmapFactory.decodeResource(getResources(), R.drawable.space_snow4);

		// This listener is where the action is for the flak animations. Every
		// frame of the
		// animation, we calculate the elapsed time and update every flake's
		// position and rotation
		// according to its speed.
		animator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator arg0) {
				long nowTime = System.currentTimeMillis();
				float secs = (float) (nowTime - prevTime) / 1000f;
				prevTime = nowTime;
				for (int i = 0; i < numFlakes; ++i) {
					SnowFlake flake = flakes.get(i);
					flake.y += (flake.speed * secs);
					if (flake.y > getHeight()) {
						// If a flake falls off the bottom, send it back to the
						// top
						flake.y = 0 - flake.height;
						flake.x = (float) Math.random() * (getWidth() - flake.width);
						flake.speed = 20 + (float) Math.random() * 50;
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
	}

	int getNumFlakes() {
		return numFlakes;
	}

	private void setNumFlakes(int quantity) {
		numFlakes = quantity;
	}

	/**
	 * Add the specified number of droidflakes.
	 */
	void addFlakes(int quantity) {
		for (int i = 0; i < quantity; ++i) {
			int a = new Random().nextInt(1);
			if (a == 0) {
				flakes.add(SnowFlake.createFlake(context, getWidth(), droid));
			} else if (a == 1) {
				flakes.add(SnowFlake.createFlake(context, getWidth(), droid1));
			}
			//			else if (a == 2) {
			//				flakes.add(SnowFlake.createFlake(context, getWidth(), droid2));
			//			} else {
			//				flakes.add(SnowFlake.createFlake(context, getWidth(), droid3));
			//			} 

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
		addFlakes(2);
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
			SnowFlake flake = flakes.get(i);
			m.setTranslate(-flake.width / 2, -flake.height / 2);
			m.postRotate(flake.rotation);
			m.postTranslate(flake.width / 2 + flake.x, flake.height / 2 + flake.y);
			canvas.drawBitmap(flake.bitmap, m, null);
		}
	}

	public void pause() {
		// Make sure the animator's not spinning in the background when the
		// activity is paused.
		animator.cancel();
	}

	public void resume() {
		animator.start();
	}
}
