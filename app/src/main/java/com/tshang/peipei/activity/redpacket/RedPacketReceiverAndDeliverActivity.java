package com.tshang.peipei.activity.redpacket;

import java.util.Collections;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.redpacket.adapter.RedPacketDeliverAndReceiverAdapter;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfoList;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.redpacket.RedPacketCreate;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

/**
 * @Title: 我发的红包界面
 *
 * @author Jeff
 *
 * @version V1.4.0   
 */
public class RedPacketReceiverAndDeliverActivity extends BaseActivity implements OnItemClickListener, OnRefreshListener2<ListView> {
	private PullToRefreshListView pListView;
	private RedPacketDeliverAndReceiverAdapter adapter;
	private TextView tv_empty;
	public static final String DELIVER_OR_RECEIVER = "deliver_or_receiver";
	public boolean deliver_or_receiver = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		pListView.onRefreshComplete();
		switch (msg.what) {
		case HandlerValue.RED_PACKET_GET_DELIVER_REDPACKET_LIST_SUCCESS_VALUE:
			RedPacketInfoList lists = (RedPacketInfoList) msg.obj;
			Collections.reverse(lists);
			if (lists != null && !lists.isEmpty()) {
				adapter.appendToList(lists);
			}
			if (msg.arg1 == 1) {//说明加载完 了所有
				pListView.setMode(Mode.DISABLED);
			}
			break;
		case HandlerValue.RED_PACKET_GET_DELIVER_REDPACKET_LIST_FAILED_VALUE:
			break;

		default:
			break;
		}
	}

	@Override
	protected void initData() {
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			deliver_or_receiver = bundle.getBoolean(DELIVER_OR_RECEIVER);
		}
		BaseUtils.showDialog(this, R.string.loading);
		RedPacketCreate.getInstance().setLoadDeliverPosition(-1);//充值加载位置
		if (deliver_or_receiver) {
			mTitle.setText(R.string.str_my_send_red_packet);
			RedPacketCreate.getInstance().reqGetDeliverRedPacketList(this, mHandler);
		} else {
			mTitle.setText(R.string.str_my_receiver_red_packet);
			tv_empty.setText(R.string.str_redpacet_no_receiver_data);
			RedPacketCreate.getInstance().reqGetReceiverRedPacketList(this, mHandler);
		}
	}

	@Override
	protected void initRecourse() {

		findViewById(R.id.title_tv_left).setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		tv_empty = (TextView) findViewById(R.id.tv_redpacket_empyt);

		adapter = new RedPacketDeliverAndReceiverAdapter(this);
		pListView = (PullToRefreshListView) findViewById(R.id.plv_redpacket_detail);
		pListView.setAdapter(adapter);
		pListView.setMode(Mode.PULL_FROM_END);
		pListView.getRefreshableView().setSelector(R.drawable.click_selector_peach);
		pListView.setEmptyView(tv_empty);
		pListView.getRefreshableView().setOnItemClickListener(this);
		pListView.setOnRefreshListener(this);
	}

	@Override
	protected int initView() {
		return R.layout.activity_redpacket_detail;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		RedPacketInfo info = (RedPacketInfo) parent.getAdapter().getItem(position);
		if (info != null) {
			Bundle bundle = new Bundle();
			bundle.putInt(RedPacketDetailActivity.STR_REDPACKETID, info.id.intValue());
			bundle.putInt(RedPacketDetailActivity.STR_REDPACKETUID, info.createuid.intValue());
			BaseUtils.openActivity(this, RedPacketDetailActivity.class, bundle);
		}

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (deliver_or_receiver) {
			RedPacketCreate.getInstance().reqGetDeliverRedPacketList(this, mHandler);
		} else {
			RedPacketCreate.getInstance().reqGetReceiverRedPacketList(this, mHandler);
		}

	}

}
