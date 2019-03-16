package com.tshang.peipei.view.fire;

import android.content.Context;

public class DotFactory {

	public DotFactory() {

	}

	public Dot makeDot(Context context, int kind, int x, int y) {

		int red = (int) (Math.random() * 255);
		int green = (int) (Math.random() * 255);
		int blue = (int) (Math.random() * 255);

		int col = 0xff000000 | red << 16 | green << 8 | blue;

		return new DotAnimFW(context, col, kind, x, y);
	}
}
