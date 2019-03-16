package com.tshang.peipei.activity.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tshang.peipei.R;

public class MainReportAdapter extends BaseAdapter {

	private Context context;
	private String[] arrays;
	RadioBtnClickLitener radioClickListener;

	public MainReportAdapter(Context context, String[] arrays, RadioBtnClickLitener listener) {
		this.context = context;
		this.arrays = arrays;
		this.radioClickListener = listener;
	}

	@Override
	public int getCount() {
		return arrays.length;
	}

	@Override
	public Object getItem(int position) {
		return arrays[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.dialog_list__report_item, null);
		TextView txtContent = (TextView) convertView.findViewById(R.id.dialog_report_tv);
		RadioButton rb = (RadioButton) convertView.findViewById(R.id.dialog_report_rb);
		txtContent.setText(arrays[position]);
		rb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				radioClickListener.radioBtnClick();
			}
		});

		return convertView;
	}

	public interface RadioBtnClickLitener {
		public void radioBtnClick();
	}

}
