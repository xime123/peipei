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

import java.util.HashMap;
import java.util.Random;

import com.tshang.peipei.base.BaseUtils;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * This class represents a single Droidflake, with properties representing its
 * size, rotation, location, and speed.
 */
public class Rose1Flake {

	// These are the unique properties of any flake: its size, rotation, speed,
	// location, and its underlying Bitmap object
	float x, y;
	float rotation;
	float speed;
	float rotationSpeed;
	int width, height;
	Bitmap bitmap;

	// This map stores pre-scaled bitmaps according to the width. No reason to
	// create
	// new bitmaps for sizes we've already seen.
	static HashMap<Integer, Bitmap> bitmapMap = new HashMap<Integer, Bitmap>();

	/**
	 * Creates a new droidflake in the given xRange and with the given bitmap.
	 * Parameters of location, size, rotation, and speed are randomly
	 * determined.
	 */
	static Rose1Flake createFlake(Context context, float xRange, Bitmap originalBitmap) {
		Rose1Flake flake = new Rose1Flake();
		// Size each flake with a width between 5 and 55 and a proportional
		// height
		int size = BaseUtils.dip2px(context, 15);
		flake.width = (int) (size + (float) Math.random() * size);
		float hwRatio = originalBitmap.getHeight() / originalBitmap.getWidth();
		flake.height = (int) (flake.width * hwRatio);

		// Position the flake horizontally between the left and right of the
		// range
		flake.x = (float) Math.random() * (xRange - flake.width);
		// Position the flake vertically slightly off the top of the display
		flake.y = 0 - (flake.height + (float) new Random().nextInt(12) * flake.height);

		// Each flake travels at 50-200 pixels per second
		flake.speed = 20 + (float) Math.random() * 50;

		// Flakes start at -90 to 90 degrees rotation, and rotate between -45
		// and 45
		// degrees per second
		flake.rotation = (float) Math.random() * 180 - 90;
		flake.rotationSpeed = (float) Math.random() * 90 - 45;

		// Get the cached bitmap for this size if it exists, otherwise create
		// and cache one
		flake.bitmap = bitmapMap.get(flake.width);
		if (flake.bitmap == null) {
			flake.bitmap = Bitmap.createScaledBitmap(originalBitmap, (int) flake.width, (int) flake.height, true);
			bitmapMap.put(flake.width, flake.bitmap);
		}
		return flake;
	}
}
