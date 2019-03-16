package com.tshang.peipei.activity.harem;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.harem.adapter.SelectMemberAdapter;
import com.tshang.peipei.protocol.asn.gogirl.GroupMemberInfo;
import com.tshang.peipei.protocol.asn.gogirl.GroupMemberInfoList;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.harem.CreateHarem;
import com.tshang.peipei.view.PullToRefreshHeaderGridView;

/**
 * @Title: SelectUserActivity.java 
 *
 * @Description: 翻牌子
 *
 * @author allen  
 *
 * @date 2014-9-23 下午3:22:13 
 *
 * @version V1.3.0   
 */
public class SelectUserActivity extends BaseActivity implements OnItemClickListener {

	public final static int PATRONIZE_CODE = 1990;

	private PullToRefreshHeaderGridView pgvGroupMember;
	private SelectMemberAdapter adapter;
	private int groupid = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		default:
			break;
		}
	}

	@Override
	protected void initData() {
		adapter = new SelectMemberAdapter(this);
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
		mTitle.setText(R.string.str_turn_over_the_brand);

		pgvGroupMember = (PullToRefreshHeaderGridView) findViewById(R.id.pgv_group_member);
		findViewById(R.id.ll_select_group_member).setVisibility(View.GONE);
		pgvGroupMember.setOnItemClickListener(this);
	}

	@Override
	protected int initView() {
		return R.layout.activity_sannomiyasixhomes;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position < adapter.getList().size()) {
			GroupMemberInfo info = (GroupMemberInfo) parent.getAdapter().getItem(position);
			if (info != null) {
				Bundle bundle = new Bundle();
				bundle.putInt("fuid", info.uid.intValue());
				bundle.putString("fnick", new String(info.nick));
				bundle.putInt("groupid", groupid);
				Intent intent = new Intent(this, PatronizeActivity.class);
				intent.putExtras(bundle);
				startActivityForResult(intent, PATRONIZE_CODE);
			}
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == PATRONIZE_CODE) {
			finish();
		}

	}
}
