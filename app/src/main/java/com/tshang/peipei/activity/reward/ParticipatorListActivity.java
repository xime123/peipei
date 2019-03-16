package com.tshang.peipei.activity.reward;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.reward.adapter.ParticipatorAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.reward.RewardRequestControl;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestParticipatorInfo.GetRewardParticipatorCallBack;
import com.tshang.peipei.protocol.asn.gogirl.ParticipateInfo;
import com.tshang.peipei.protocol.asn.gogirl.ParticipateInfoList;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

import de.greenrobot.event.EventBus;

/**
 * @Title: participatorListActivity.java 
 *
 * @Description: 参加列表
 *
 * @author Aaron  
 *
 * @date 2015-9-29 下午4:41:17 
 *
 * @version V1.0   
 */
public class ParticipatorListActivity extends BaseActivity implements GetRewardParticipatorCallBack, OnRefreshListener2<ListView> {

	private PullToRefreshListView pullToRefreshListView;
	private TextView emptyTv;

	private ParticipatorAdapter adapter;

	private RewardRequestControl control;

	private int type, awarduid, awardid, anonym;

	private int issndaward;//是否已经悬赏过

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		control = new RewardRequestControl();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				pullToRefreshListView.setRefreshing();
			}
		}, 600);
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initRecourse() {
		type = getIntent().getExtras().getInt("type");
		awarduid = getIntent().getExtras().getInt("awarduid");
		awardid = getIntent().getExtras().getInt("awardid");
		anonym = getIntent().getExtras().getInt("anonym");

		mTitle = (TextView) this.findViewById(R.id.title_tv_mid);
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setOnClickListener(this);
		mTitle.setText(R.string.join_reward);

		emptyTv = (TextView) findViewById(R.id.participator_reward_empty_tv);
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.participator_reward_listview);
		pullToRefreshListView.setOnRefreshListener(this);

		adapter = new ParticipatorAdapter(this, mHandler, type, awarduid, awardid, anonym);
		pullToRefreshListView.setAdapter(adapter);
		pullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				adapter.setClickPosition(position - 1);
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	protected int initView() {
		return R.layout.activity_participator_layout;
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.GET_PARTICIPATOR_SUCCSS:
			pullToRefreshListView.onRefreshComplete();
			if (msg.arg1 == 0) {
				ParticipateInfoList infoList = (ParticipateInfoList) msg.obj;
				if (infoList != null && infoList.size() > 0) {
					List<ParticipateInfo> list = new ArrayList<ParticipateInfo>();
					for (int i = 0; i < infoList.size(); i++) {
						ParticipateInfo info = (ParticipateInfo) infoList.get(i);
						if (info != null) {
							list.add((ParticipateInfo) infoList.get(i));
						}
					}
					adapter.appendToList(list);
				} else {
					pullToRefreshListView.setEmptyView(emptyTv);
				}
				pullToRefreshListView.setMode(Mode.DISABLED);
				issndaward = msg.arg2;
				adapter.setIssndaward(issndaward);

			} else if (msg.arg1 == -28010) {
				NoticeEvent event = new NoticeEvent();
				event.setFlag(NoticeEvent.NOTICE27);
				EventBus.getDefault().post(event);
			} else {
				BaseUtils.showTost(this, R.string.get_data_failure);
			}

			break;
		case HandlerValue.GET_PARTICIPATOR_ERROR:

			break;
		case HandlerValue.PARTICIPATOR_SUCCESS://悬赏成功
			if (msg.arg1 == 0) {
				adapter.setIssndaward(1);
				NoticeEvent n = new NoticeEvent();
				n.setFlag(NoticeEvent.NOTICE94);
				EventBus.getDefault().post(n);
			} else if (msg.arg1 == -28096) {
				BaseUtils.showTost(this, R.string.reward_paritici_end_toast);
			} else if (msg.arg1 == -28095) {
				BaseUtils.showTost(this, "请勿重复悬赏");
			} else {
				BaseUtils.showTost(this, R.string.reward_failure);
			}
			break;
		case HandlerValue.PARTICIPATOR_ERROR://悬赏失败
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		default:
			break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		control.getParticipatorInfoList(type, awarduid, awardid, anonym, this);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		control.getParticipatorInfoList(type, awarduid, awardid, anonym, this);
	}

	@Override
	public void onParticipatorSuccess(int code, int issndaward, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.GET_PARTICIPATOR_SUCCSS, code, issndaward, obj);
	}

	@Override
	public void onParticipatorError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.GET_PARTICIPATOR_ERROR, code);
	}

}
