package com.tshang.peipei.activity.chat;

import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.space.SpaceActivity;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetRelevantPeopleInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetRelevantPeopleInfoList;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.ChatSessionManageBiz;
import com.tshang.peipei.model.request.RequestGetVisitorList.IGetVisitorList;
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.common.util.ListUtils;

/**
 * @Title: MessageWhoSawMeActivity.java 
 *
 * @Description: 谁看过我
 *
 * @author allen  
 *
 * @date 2014-8-8 下午2:06:59 
 *
 * @version V1.0   
 */
public class MessageVisitorActivity extends BaseActivity implements IGetVisitorList, OnItemClickListener {

	private final int GET_VISITOR_LIST = 11;

	private long time;

	private int start = -1;
	private int num = 100;

	private ListView listView;
	private MessageVisitorAdapter adapter;

	@Override
	protected void initData() {
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(this);
		if (userInfo != null) {
			SharedPreferencesTools.getInstance(this, userInfo.uid.intValue() + "").saveLongKeyValue(System.currentTimeMillis(),
					BAConstants.PEIPEI_INTERESTED);

			SharedPreferencesTools.getInstance(this, userInfo.uid.intValue() + "").saveBooleanKeyValue(false, BAConstants.PEIPEI_INTERESTED_NEW);

			BaseUtils.showDialog(this, R.string.loading);
			ChatSessionManageBiz mBiz = new ChatSessionManageBiz();
			mBiz.getVisitorList(userInfo.auth, BAApplication.app_version_code, userInfo.uid.intValue(), userInfo.uid.intValue(), start, num, this);
		}

	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.who_saw_me);

		listView = (ListView) findViewById(R.id.main_message_lvw);
		GoGirlUserInfo mEntity = UserUtils.getUserEntity(this);
		if (mEntity != null) {
			time = SharedPreferencesTools.getInstance(this, mEntity.uid.intValue() + "").getLongKeyValue(BAConstants.PEIPEI_INTERESTED);
		}
		adapter = new MessageVisitorAdapter(this, time);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(this);
	}

	@Override
	protected int initView() {
		return R.layout.activity_visitor;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case GET_VISITOR_LIST:
			BaseUtils.cancelDialog();
			if (msg.arg1 == 0) {
				List<RetRelevantPeopleInfo> list = (List<RetRelevantPeopleInfo>) msg.obj;
				if (!ListUtils.isEmpty(list)) {
					Collections.reverse(list);
					adapter.appendToList(list);
				}
			}

			break;
		default:
			break;
		}
	}

	@Override
	public void getVisitorListCallBack(int retCode, int isend, RetRelevantPeopleInfoList list) {
		sendHandlerMessage(mHandler, GET_VISITOR_LIST, retCode, list);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		RetRelevantPeopleInfo info = (RetRelevantPeopleInfo) parent.getAdapter().getItem(position);
		SpaceUtils.toSpaceCustom(this, info.userinfo.uid.intValue(), info.userinfo.sex.intValue());

	}

}
