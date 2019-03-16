package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.mine.MineMissionsActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.protocol.asn.gogirl.LoginRewardInfo;
import com.tshang.peipei.protocol.asn.gogirl.LoginRewardInfoList;

/**
 * @Title: UpdateApkDialog.java 
 *
 * @Description: 2个按键+1个提示的对话框基类
 *
 * @author allen  
 *
 * @date 2014-7-17 上午10:25:44 
 *
 * @version V1.0   
 */
public class DayTaskDialog extends Dialog implements OnClickListener {

	public Activity context;
	private int position = 1;
	private LoginRewardInfoList lists;

	private DayTaskDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public DayTaskDialog(Activity context, int theme, int position, LoginRewardInfoList lists) {
		super(context, theme);
		this.context = context;
		this.position = position;
		this.lists = lists;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.dialog_task);
			findViewById(R.id.btn_view_other_task).setOnClickListener(this);
			TextView tvPoint = (TextView) findViewById(R.id.tv_task_getpoint);
			TextView tvStartPoint = (TextView) findViewById(R.id.tv_start_point);
			TextView tvMiddlePoint = (TextView) findViewById(R.id.tv_middle_point);
			TextView tvEndPoint = (TextView) findViewById(R.id.tv_end_point);
			TextView tvStartDay = (TextView) findViewById(R.id.tv_start_day);
			TextView tvMiddleDay = (TextView) findViewById(R.id.tv_middle_day);
			TextView tvEndDay = (TextView) findViewById(R.id.tv_end_day);
			LinearLayout ll_more = (LinearLayout) findViewById(R.id.ll_more);

			if (position == 1) {
				tvStartPoint.setBackgroundResource(R.drawable.hall_prize_circle_press);
				tvStartPoint.setTextColor(context.getResources().getColor(R.color.task_point_c_color));
				tvMiddlePoint.setBackgroundResource(R.drawable.hall_prize_circle_un);
				tvMiddlePoint.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvEndPoint.setBackgroundResource(R.drawable.hall_prize_circle_un);
				tvEndPoint.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvStartDay.setText("第1天");
				tvStartDay.setTextColor(context.getResources().getColor(R.color.task_point_c_color));
				tvMiddleDay.setText("第2天");
				tvMiddleDay.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvEndDay.setText("第7天");
				tvEndDay.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvStartPoint.setText(((LoginRewardInfo) lists.get(0)).rewardsilvercoin.intValue() + "");
				tvMiddlePoint.setText(((LoginRewardInfo) lists.get(1)).rewardsilvercoin.intValue() + "");
				tvEndPoint.setText(((LoginRewardInfo) lists.get(6)).rewardsilvercoin.intValue() + "");
				tvPoint.setText("+" + ((LoginRewardInfo) lists.get(0)).rewardsilvercoin.intValue());
			} else if (position == 2) {
				tvStartPoint.setBackgroundResource(R.drawable.hall_prize_circle_un);
				tvStartPoint.setText(((LoginRewardInfo) lists.get(1)).rewardsilvercoin.intValue() + "");
				tvStartPoint.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvMiddlePoint.setBackgroundResource(R.drawable.hall_prize_circle_press);
				tvMiddlePoint.setTextColor(context.getResources().getColor(R.color.task_point_c_color));
				tvEndPoint.setBackgroundResource(R.drawable.hall_prize_circle_un);
				tvEndPoint.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvStartDay.setText("第1天");
				tvStartDay.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvMiddleDay.setText("第2天");
				tvMiddleDay.setTextColor(context.getResources().getColor(R.color.task_point_c_color));
				tvEndDay.setText("第7天");
				tvEndDay.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvStartPoint.setText(((LoginRewardInfo) lists.get(0)).rewardsilvercoin.intValue() + "");
				tvMiddlePoint.setText(((LoginRewardInfo) lists.get(1)).rewardsilvercoin.intValue() + "");
				tvEndPoint.setText(((LoginRewardInfo) lists.get(6)).rewardsilvercoin.intValue() + "");
				tvPoint.setText("+" + ((LoginRewardInfo) lists.get(1)).rewardsilvercoin.intValue());
			} else if (position == 3) {
				tvStartPoint.setBackgroundResource(R.drawable.hall_prize_circle_un);
				tvStartPoint.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvMiddlePoint.setBackgroundResource(R.drawable.hall_prize_circle_press);
				tvMiddlePoint.setTextColor(context.getResources().getColor(R.color.task_point_c_color));
				tvEndPoint.setBackgroundResource(R.drawable.hall_prize_circle_un);
				tvEndPoint.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvStartDay.setText("第2天");
				tvStartDay.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvMiddleDay.setText("第3天");
				tvMiddleDay.setTextColor(context.getResources().getColor(R.color.task_point_c_color));
				tvEndDay.setText("第7天");
				tvEndDay.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvStartPoint.setText(((LoginRewardInfo) lists.get(1)).rewardsilvercoin.intValue() + "");
				tvMiddlePoint.setText(((LoginRewardInfo) lists.get(2)).rewardsilvercoin.intValue() + "");
				tvEndPoint.setText(((LoginRewardInfo) lists.get(6)).rewardsilvercoin.intValue() + "");
				tvPoint.setText("+" + ((LoginRewardInfo) lists.get(2)).rewardsilvercoin.intValue());
			} else if (position == 4) {
				tvStartPoint.setBackgroundResource(R.drawable.hall_prize_circle_un);
				tvStartPoint.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvMiddlePoint.setBackgroundResource(R.drawable.hall_prize_circle_press);
				tvMiddlePoint.setTextColor(context.getResources().getColor(R.color.task_point_c_color));
				tvEndPoint.setBackgroundResource(R.drawable.hall_prize_circle_un);
				tvEndPoint.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvStartDay.setText("第3天");
				tvStartDay.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvMiddleDay.setText("第4天");
				tvMiddleDay.setTextColor(context.getResources().getColor(R.color.task_point_c_color));
				tvEndDay.setText("第7天");
				tvEndDay.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvStartPoint.setText(((LoginRewardInfo) lists.get(2)).rewardsilvercoin.intValue() + "");
				tvMiddlePoint.setText(((LoginRewardInfo) lists.get(3)).rewardsilvercoin.intValue() + "");
				tvEndPoint.setText(((LoginRewardInfo) lists.get(6)).rewardsilvercoin.intValue() + "");
				tvPoint.setText("+" + ((LoginRewardInfo) lists.get(3)).rewardsilvercoin.intValue());
			} else if (position == 5) {
				tvStartPoint.setBackgroundResource(R.drawable.hall_prize_circle_press);
				tvStartPoint.setTextColor(context.getResources().getColor(R.color.task_point_c_color));
				tvMiddlePoint.setBackgroundResource(R.drawable.hall_prize_circle_un);
				tvMiddlePoint.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvEndPoint.setBackgroundResource(R.drawable.hall_prize_circle_un);
				tvEndPoint.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvStartDay.setText("第5天");
				tvStartDay.setTextColor(context.getResources().getColor(R.color.task_point_c_color));
				tvMiddleDay.setText("第6天");
				tvMiddleDay.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvEndDay.setText("第7天");
				tvEndDay.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvStartPoint.setText(((LoginRewardInfo) lists.get(4)).rewardsilvercoin.intValue() + "");
				tvMiddlePoint.setText(((LoginRewardInfo) lists.get(5)).rewardsilvercoin.intValue() + "");
				tvEndPoint.setText(((LoginRewardInfo) lists.get(6)).rewardsilvercoin.intValue() + "");
				tvPoint.setText("+" + ((LoginRewardInfo) lists.get(4)).rewardsilvercoin.intValue());
				ll_more.setVisibility(View.GONE);
			} else if (position == 6) {
				tvStartPoint.setBackgroundResource(R.drawable.hall_prize_circle_un);
				tvStartPoint.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvMiddlePoint.setBackgroundResource(R.drawable.hall_prize_circle_press);
				tvMiddlePoint.setTextColor(context.getResources().getColor(R.color.task_point_c_color));
				tvEndPoint.setBackgroundResource(R.drawable.hall_prize_circle_un);
				tvEndPoint.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvStartDay.setText("第5天");
				tvStartDay.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvMiddleDay.setText("第6天");
				tvMiddleDay.setTextColor(context.getResources().getColor(R.color.task_point_c_color));
				tvEndDay.setText("第7天");
				tvEndDay.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvStartPoint.setText(((LoginRewardInfo) lists.get(4)).rewardsilvercoin.intValue() + "");
				tvMiddlePoint.setText(((LoginRewardInfo) lists.get(5)).rewardsilvercoin.intValue() + "");
				tvEndPoint.setText(((LoginRewardInfo) lists.get(6)).rewardsilvercoin.intValue() + "");
				tvPoint.setText("+" + ((LoginRewardInfo) lists.get(5)).rewardsilvercoin.intValue());
				ll_more.setVisibility(View.GONE);
			} else if (position >= 7) {
				tvStartPoint.setBackgroundResource(R.drawable.hall_prize_circle_un);
				tvStartPoint.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvMiddlePoint.setBackgroundResource(R.drawable.hall_prize_circle_un);
				tvMiddlePoint.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvEndPoint.setBackgroundResource(R.drawable.hall_prize_circle_press);
				tvEndPoint.setTextColor(context.getResources().getColor(R.color.task_point_c_color));
				tvStartDay.setText("第5天");
				tvStartDay.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvMiddleDay.setText("第6天");
				tvMiddleDay.setTextColor(context.getResources().getColor(R.color.task_point_d_color));
				tvEndDay.setText("第7天");
				tvEndDay.setTextColor(context.getResources().getColor(R.color.task_point_c_color));
				ll_more.setVisibility(View.GONE);
				tvStartPoint.setText(((LoginRewardInfo) lists.get(4)).rewardsilvercoin.intValue() + "");
				tvMiddlePoint.setText(((LoginRewardInfo) lists.get(5)).rewardsilvercoin.intValue() + "");
				tvEndPoint.setText(((LoginRewardInfo) lists.get(6)).rewardsilvercoin.intValue() + "");
				tvPoint.setText("+" + ((LoginRewardInfo) lists.get(6)).rewardsilvercoin.intValue());
			}

			final Window w = getWindow();
			final WindowManager.LayoutParams wlps = w.getAttributes();
			wlps.width = WindowManager.LayoutParams.WRAP_CONTENT;
			wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
			wlps.dimAmount = 0.6f;
			wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
			wlps.gravity = Gravity.CENTER;
			w.setAttributes(wlps);
			setCanceledOnTouchOutside(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_view_other_task:
			dismiss();
			Bundle bundle = new Bundle();
			bundle.putString("from", "0");
			BaseUtils.openActivity(context, MineMissionsActivity.class, bundle);
			break;
		default:
			break;
		}

	}

	public void showDialog() {
		try {
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
