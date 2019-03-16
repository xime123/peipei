package com.tshang.peipei.activity;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tshang.peipei.R;
import com.tshang.peipei.vender.wheelview.OnWheelChangedListener;
import com.tshang.peipei.vender.wheelview.OnWheelScrollListener;
import com.tshang.peipei.vender.wheelview.WheelView;
import com.tshang.peipei.vender.wheelview.adapters.AbstractWheelTextAdapter;

/**
 * @Title: WheelActivity.java 
 *
 * @Description: 滚轮选择界面
 *
 * @author vactor 
 *
 * @date 2014-5-8 下午4:46:13 
 *
 * @version V1.0   
 */
public class WheelActivity extends BaseActivity {
	public static final String RESULT = "result";
	public static final String FROM = "from";
	public static final String COUNT = "count";

	private final int RESULTCODE = 10;
	private String from;
	private String numbers[] = new String[] { "50", "100", "200", "500", "1000", "1500", "2000" };
	private String limit[] = new String[] { "0", "2", "5", "12", "26", "50" };

	private WheelView mWheelView;
	private RelativeLayout mRelRoot;

	// Time changed flag
	//	private boolean timeChanged = false;
	// Time scrolled flag
	private boolean timeScrolled = false;
	private int value = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWheelView = (WheelView) this.findViewById(R.id.wheelview);
		mRelRoot = (RelativeLayout) this.findViewById(R.id.wheel_rel_root);
		Intent intent = getIntent();
		from = intent.getStringExtra(FROM);
		value = intent.getIntExtra(COUNT, 0);
		if (null != from && from.equals("MineMessageFragment")) {
			CountryAdapter adapter2 = new CountryAdapter(this, limit);
			adapter2.setItemResource(R.layout.wheel_text_item);
			adapter2.setItemTextResource(R.id.text);
			mWheelView.setViewAdapter(adapter2);

			mWheelView.setCurrentItem(getCurrNumByString(limit, value + ""));
			OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
				public void onChanged(WheelView wheel, int oldValue, int newValue) {
					value = Integer.parseInt(limit[newValue]);
				}
			};
			mWheelView.addChangingListener(wheelListener);
		} else {
			CountryAdapter adapter2 = new CountryAdapter(this, numbers);
			adapter2.setItemResource(R.layout.wheel_text_item);
			adapter2.setItemTextResource(R.id.text);
			mWheelView.setViewAdapter(adapter2);
			OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
				public void onChanged(WheelView wheel, int oldValue, int newValue) {
					value = Integer.parseInt(numbers[newValue]);
				}
			};
			mWheelView.addChangingListener(wheelListener);
		}

		OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				timeScrolled = true;
			}

			public void onScrollingFinished(WheelView wheel) {
				timeScrolled = false;
			}
		};

		mWheelView.addScrollingListener(scrollListener);
		mRelRoot.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(RESULT, value);
				setResult(RESULTCODE, intent);
				finish();
			}
		});

	}

	/**
	* Adapter for countries
	*/
	private class CountryAdapter extends AbstractWheelTextAdapter {
		// Countries names

		String[] num;

		/**
		 * Constructor
		 */
		protected CountryAdapter(Context context, String[] num) {
			super(context, R.layout.wheel_number_layout, NO_RESOURCE);
			this.num = num;

			setItemTextResource(R.id.wheel_num_tv);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return num.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return num[index];
		}
	}

	@Override
	protected void initData() {}

	@Override
	protected void initRecourse() {}

	@Override
	protected int initView() {
		return R.layout.activity_wheel;
	}

	public int getCurrNumByString(String str[], String source) {
		List<String> tempList = Arrays.asList(str);

		// 利用list的包含方法,进行判断
		if (tempList.contains(source)) {
			return tempList.indexOf(source);
		} else {
			return 0;
		}
	}
}
