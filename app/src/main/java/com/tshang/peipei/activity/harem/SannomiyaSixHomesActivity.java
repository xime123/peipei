package com.tshang.peipei.activity.harem;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.harem.adapter.GroupMemberAdapter;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.harem.CreateHarem;
import com.tshang.peipei.protocol.asn.gogirl.GroupMemberInfo;
import com.tshang.peipei.protocol.asn.gogirl.GroupMemberInfoList;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.view.PullToRefreshHeaderGridView;

/**
 * 三宫六院
 * @author Jeff
 *
 */
public class SannomiyaSixHomesActivity extends BaseActivity {
	private PullToRefreshHeaderGridView pgvGroupMember;
	private GroupMemberAdapter adapter;
	private int groupid = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void onEvent(NoticeEvent event) {
		super.onEvent(event);
		if (event.getFlag() == NoticeEvent.NOTICE65) {
			CreateHarem.getInstance().getGroupMemberInfoList(this, groupid, mHandler);//获取后宫成员列表
		}
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.HAREM_GET_GROUP_MEMBER_LIST_SUCCESS_VALUE:
			GroupMemberInfoList infoList = (GroupMemberInfoList) msg.obj;
			List<GroupMemberInfo> lists = new ArrayList<GroupMemberInfo>();
			if (infoList != null && !infoList.isEmpty()) {
				for (Object object : infoList) {
					GroupMemberInfo groupMemberInfo = (GroupMemberInfo) object;
					if (groupMemberInfo.level.intValue() != 9000) {
						lists.add(groupMemberInfo);
					}
				}
			}
			adapter.setList(lists);
			adapter.setCount(msg.arg1 - 1);

			break;
		case HandlerValue.HAREM_GET_GROUP_MEMBER_LIST_FAILED_VALUE:

			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_tv_right:
			Bundle bundle1 = new Bundle();
			bundle1.putInt("groupid", groupid);
			bundle1.putInt("from", 0);
			BaseUtils.openActivity(this, ManagerHaremActivity.class, bundle1);
			break;
		case R.id.tv_select_group_member:
			long time = SharedPreferencesTools.getInstance(this).getLongKeyValue(BAConstants.PEIPEI_SELECT_USER);
			if (BaseTimes.isNewDay(time)) {
				Bundle bundle = new Bundle();
				bundle.putInt("groupid", groupid);
				BaseUtils.openActivity(this, SelectUserActivity.class, bundle);
			} else {
				BaseUtils.showTost(this, "您的精力不足哦~明天再来吧");
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void initData() {
		adapter = new GroupMemberAdapter(this);
		pgvGroupMember.setAdapter(adapter);
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			groupid = bundle.getInt("groupid", -1);
			CreateHarem.getInstance().getGroupMemberInfoList(this, groupid, mHandler);//获取后宫成员列表
		}

	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_sannomiya_six_homes);
		mTextRight = (TextView) findViewById(R.id.title_tv_right);
		mTextRight.setText(R.string.manage);
		mTextRight.setOnClickListener(this);
		mTextRight.setVisibility(View.VISIBLE);

		pgvGroupMember = (PullToRefreshHeaderGridView) findViewById(R.id.pgv_group_member);
		findViewById(R.id.tv_select_group_member).setOnClickListener(this);
	}

	@Override
	protected int initView() {
		return R.layout.activity_sannomiyasixhomes;
	}

}
