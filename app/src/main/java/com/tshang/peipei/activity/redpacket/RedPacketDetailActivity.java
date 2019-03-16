package com.tshang.peipei.activity.redpacket;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.redpacket.adapter.RedPacketGetAdapter;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.UserSimpleInfoList;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.redpacket.RedPacketCreate;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

/**
 * @Title: 红包详情界面
 *
 * @author Jeff
 *
 * @version V1.4.0   
 */
public class RedPacketDetailActivity extends BaseActivity {
	public static final String STR_REDPACKETID = "str_redpacketid";
	public static final String STR_REDPACKETUID = "str_redpacketuid";
	private int redpacketid = -1;
	private int redpacketuid = -1;
	private PullToRefreshListView pListView;
	private RedPacketGetAdapter adapter;

	private TextView tvName;
	private TextView tvTime;
	private TextView tvStatus;
	private TextView tvDesc;

	private TextView tvGetCount;
	private TextView tvGetCoin;
	private TextView tvTotalCount;
	private TextView tvTotalCoin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.RED_PACKET_GET_REDPACKET_DETAIL_SUCCESS_VALUE:
			RedPacketInfo info = (RedPacketInfo) msg.obj;
			if (info != null) {

				tvName.setText(new String(info.createnick) + "的红包");
				tvTime.setText(BaseTimes.getChatDiffTimeMMDD(info.createtime.longValue() * 1000));
				tvDesc.setText(new String(info.desc));

				int totalportionnum = info.totalportionnum.intValue();
				int totalgoldcoin = info.totalgoldcoin.intValue();
				int leftportionnum = info.leftportionnum.intValue();
				int leftgoldcoin = info.leftgoldcoin.intValue();
				tvGetCount.setText(String.format(getString(R.string.str_get_redpacket_count), "" + (totalportionnum - leftportionnum)));
				tvGetCoin.setText(String.valueOf(totalgoldcoin - leftgoldcoin));
				tvTotalCount.setText(String.format(getString(R.string.str_total_redpacket_count) + ",", "" + totalportionnum));
				tvTotalCoin.setText(String.valueOf(totalgoldcoin));

				UserSimpleInfoList list = info.records;
				if (list != null && !list.isEmpty()) {
					adapter.setList(list);
				}
				int endtime = info.endtime.intValue();
				if (leftportionnum == 0) {
					tvStatus.setText(R.string.str_redpacketget_complete);
				} else {
					if (endtime > 0) {//大于0过期
						tvStatus.setText(R.string.str_redpacket_time_out);
						tvStatus.setTextColor(this.getResources().getColor(R.color.gray));
					} else {
						tvStatus.setText(R.string.str_redpacket_getting);
						tvStatus.setTextColor(this.getResources().getColor(R.color.redpacket_status_complete_color));
					}
				}

			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void initData() {
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			redpacketid = bundle.getInt(STR_REDPACKETID, -1);
			redpacketuid = bundle.getInt(STR_REDPACKETUID, -1);
			RedPacketCreate.getInstance().reqGetRedPacketDetail(this, redpacketid, redpacketuid, mHandler);
		}
	}

	@Override
	protected void initRecourse() {
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_red_packet);
		findViewById(R.id.title_tv_left).setOnClickListener(this);

		View headView = this.getLayoutInflater().inflate(R.layout.head_redpacket_detail, null);
		tvName = (TextView) headView.findViewById(R.id.tv_red_packet_username);
		tvTime = (TextView) headView.findViewById(R.id.tv_red_packet_time);
		tvDesc = (TextView) headView.findViewById(R.id.tv_red_packet_desc);
		tvStatus = (TextView) headView.findViewById(R.id.tv_red_packet_status);

		tvGetCount = (TextView) headView.findViewById(R.id.tv_get_redpacket_count);
		tvGetCoin = (TextView) headView.findViewById(R.id.tv_get_redpacket_coin);
		tvTotalCount = (TextView) headView.findViewById(R.id.tv_total_redpacket_count);
		tvTotalCoin = (TextView) headView.findViewById(R.id.tv_total_redpacket_coin);

		adapter = new RedPacketGetAdapter(this);
		pListView = (PullToRefreshListView) findViewById(R.id.plv_redpacket_detail);
		pListView.getRefreshableView().addHeaderView(headView);
		pListView.setAdapter(adapter);
		pListView.setMode(Mode.DISABLED);
		pListView.getRefreshableView().setDivider(null);

	}

	@Override
	protected int initView() {
		return R.layout.activity_redpacket_detail;
	}

}
