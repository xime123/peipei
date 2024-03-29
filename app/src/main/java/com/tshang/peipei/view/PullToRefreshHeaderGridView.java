/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.tshang.peipei.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

import com.tshang.peipei.R;
import com.tshang.peipei.vender.pulltoprefresh.library.OverscrollHelper;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshAdapterViewBase;
import com.tshang.peipei.vender.pulltoprefresh.library.internal.EmptyViewMethodAccessor;

public class PullToRefreshHeaderGridView extends PullToRefreshAdapterViewBase<HeaderGridView> {

	private MainHallHintListener hintListener;

	public PullToRefreshHeaderGridView(Context context) {
		super(context);
	}

	public PullToRefreshHeaderGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshHeaderGridView(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshHeaderGridView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	protected final HeaderGridView createRefreshableView(Context context, AttributeSet attrs) {
		final HeaderGridView gv;
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			gv = new InternalGridViewSDK9(context, attrs);
		} else {
			gv = new InternalGridView(context, attrs);
		}

		// Use Generated ID (from res/values/ids.xml)
		gv.setId(R.id.gridview);
		return gv;
	}

	class InternalGridView extends HeaderGridView implements EmptyViewMethodAccessor {

		public InternalGridView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void setEmptyView(View emptyView) {
			PullToRefreshHeaderGridView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}
	}

	@TargetApi(9)
	final class InternalGridViewSDK9 extends InternalGridView {

		public InternalGridViewSDK9(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX,
				int maxOverScrollY, boolean isTouchEvent) {

			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX,
					maxOverScrollY, isTouchEvent);

			// Does all of the hard work...
			OverscrollHelper.overScrollBy(PullToRefreshHeaderGridView.this, deltaX, scrollX, deltaY, scrollY, isTouchEvent);

			return returnValue;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (hintListener != null) {
			hintListener.hintShow(firstVisibleItem <= 3);
		}

		super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
	}

	public interface MainHallHintListener {
		public void hintShow(boolean b);
	}

	public void setMainHallHintListener(MainHallHintListener listener) {
		hintListener = listener;
	}
}
