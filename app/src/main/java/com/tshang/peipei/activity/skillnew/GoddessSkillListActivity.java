package com.tshang.peipei.activity.skillnew;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.skillnew.adapter.GoddessSkillListAdapter;
import com.tshang.peipei.activity.skillnew.adapter.GoddessSkillListAdapter.SelectSkillCallBack;
import com.tshang.peipei.activity.skillnew.bean.GoddessSkillInfo;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestGetGoddessSkillList.GetGoddessSkillListCallBack;
import com.tshang.peipei.model.request.RequestPersonSkillInfo.GetPersonSkillInfo;
import com.tshang.peipei.model.request.RequestSaveGoddessSkill.SaveGoddessSkillCallBack;
import com.tshang.peipei.model.skillnew.GoddessSkillEngine;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillTextInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillTextInfoList;
import com.tshang.peipei.vender.common.util.ListUtils;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

import de.greenrobot.event.EventBus;

/**
 * @Title: MineGoddessSkillActivity.java 
 *
 * @Description: 选择女神技能列表界面 
 *
 * @author DYH  
 *
 * @date 2015-10-29 下午2:11:24 
 *
 * @version V1.0   
 */
public class GoddessSkillListActivity extends BaseActivity implements OnRefreshListener2<ListView>, GetGoddessSkillListCallBack, SaveGoddessSkillCallBack, GetPersonSkillInfo, SelectSkillCallBack {

	private TextView mRightTv;
	private PullToRefreshListView pulltorefreshlistview;
	private TextView tvEmpty;
	private GoddessSkillListAdapter adapter;
	private GoddessSkillEngine engine;
	private static final int LOADCOUNT = 10;
	private boolean isRefresh = true;
	private GoddessSkillInfo info;
	private TextView tv_select_size;
	private List<SkillTextInfo> hasList = new ArrayList<SkillTextInfo>(); // 已经选中的技能
	private Dialog loadDialog;

	@Override
	protected void initData() {
		engine = new GoddessSkillEngine();
		refreshData(true);
		getPersonSkill();
	}

	@Override
	protected void initRecourse() {
		mRightTv = (TextView) findViewById(R.id.title_tv_right);
		mRightTv.setVisibility(View.VISIBLE);
		mRightTv.setText(R.string.save);
		findViewById(R.id.title_tv_left).setOnClickListener(this);
		mRightTv.setOnClickListener(this);
		pulltorefreshlistview = (PullToRefreshListView) findViewById(R.id.pulltorefreshlistview);
		adapter = new GoddessSkillListAdapter(this, this);
		adapter.setHasList(hasList);
		adapter.setInfo(info);
		pulltorefreshlistview.setAdapter(adapter);
		pulltorefreshlistview.setOnRefreshListener(this);
		tvEmpty = (TextView) findViewById(R.id.tv_empty_data);
		pulltorefreshlistview.setEmptyView(tvEmpty);
		tv_select_size = (TextView) findViewById(R.id.tv_select_size);

	}

	public static void openMineFaqActivity(Activity activity) {
		BaseUtils.openActivity(activity, GoddessSkillListActivity.class);
	}

	@Override
	protected int initView() {
		return R.layout.activity_goddess_skill;
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.PULL_REFRESH_GODDESS_SKILL_COMPLEMENT:
			pulltorefreshlistview.onRefreshComplete();
			break;
		case HandlerValue.GET_GODDESS_SKILL_LIST_SUCCESS:
			pulltorefreshlistview.onRefreshComplete();
			if (msg.arg1 == 0) {
				info = (GoddessSkillInfo) msg.obj;
				if(info != null){
					adapter.setInfo(info);
					tv_select_size.setText(getString(R.string.str_select_skill, info.getTotalskillnum()));
					setListViewShowData(isRefresh, msg.arg2, (SkillTextInfoList) info.getObject());
				}
			}
			break;
		case HandlerValue.GET_GODDESS_SKILL_LIST_ERROR:
			pulltorefreshlistview.onRefreshComplete();
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		case HandlerValue.GET_SAVE_SKILL_LIST_SUCCESS:
			dismissDialog();
			if(msg.arg1 == 0){
				BaseUtils.showTost(this, R.string.str_save_success);
				sendNoticeEvent(NoticeEvent.NOTICE96);
				finish();
			}else if(msg.arg1 == -28010){
				NoticeEvent event = new NoticeEvent();
				event.setFlag(NoticeEvent.NOTICE27);
				EventBus.getDefault().post(event);
			}else{
				String strMsg = (String) msg.obj;
				BaseUtils.showTost(this, strMsg);
			}
			break;
		case HandlerValue.GET_SAVE_SKILL_LIST_ERROR:
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		case HandlerValue.GET_PERSON_GODDESS_SKILL_INFO_SUCCESS:
			if(msg.arg1 == 0){
				SkillTextInfoList list = (SkillTextInfoList) msg.obj;
				if(!ListUtils.isEmpty(list)){
					hasList.addAll(adapter.getGoddessSkillListData(list));
				}
			}
			break;
		case HandlerValue.ADD_GODDESS_SKILL:
			SkillTextInfo addItem = (SkillTextInfo) msg.obj;
			if(addItem != null){
				if(!isSkillThere(addItem)){
//					if(hasList.size() >= info.getTotalskillnum()){
//						BaseUtils.showTost(this, getString(R.string.str_skill_size_exception, info.getTotalskillnum()));
//					}else{
//						
//					}
					hasList.add(addItem);
				}
			}
			break;
		case HandlerValue.DELETE_GODDESS_SKILL:
			SkillTextInfo delItem = (SkillTextInfo) msg.obj;
			if(isSkillThere(delItem)){
				removeToHasList(delItem);
			}
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_tv_right:
			saveSkill();
			break;
		}
	}
	
	private void saveSkill(){
		if(info == null){
			return;
		}
		
//		screeningSkill();
		
		engine.requestSaveGoddessSkill(beanTransformation(), this);
	}
	
	private List<SkillInfo> beanTransformation(){
		List<SkillInfo> list = new ArrayList<SkillInfo>();
		for(SkillTextInfo item : hasList){
			SkillInfo skillInfo = new SkillInfo();
			skillInfo.type = BigInteger.valueOf(0);
			skillInfo.skillid = item.id;
			skillInfo.skillkind = item.type;
			skillInfo.skillname =item.content;
			skillInfo.skilldesc = item.desc;
			skillInfo.revint0 = item.revint0;
			skillInfo.revint1 = item.revint1;
			skillInfo.revint2 = item.revint2;
			skillInfo.revint3 = item.revint3;
			skillInfo.revint4 = item.revint4;
			skillInfo.revstr0 = item.revstr0;
			skillInfo.revstr1 = item.revstr1;
			skillInfo.revstr2 = item.revstr2;
			skillInfo.revstr3 = item.revstr3;
			skillInfo.revstr4 = item.revstr4;
			list.add(skillInfo);
		}
		return list;
	}
	
	/**
	 * 筛选技能
	 * @author DYH
	 *
	 */
	private void screeningSkill(){
		for(int i=0; i<adapter.getList().size(); i++){
			SkillTextInfo info1 = adapter.getList().get(i);
			if(info1.isaddskill.intValue() == 0){
				if(!isSkillThere(info1)){
					hasList.add(info1);
				}
			}else{
				if(isSkillThere(info1)){
					removeToHasList(info1);
				}
			}
		}
	}
	
	/**
	 * 技能是否已经在我选着的技能列表中
	 * @author Administrator
	 *
	 * @param info
	 * @return
	 */
	private boolean isSkillThere(SkillTextInfo info){
		for(SkillTextInfo item : hasList){
			if(item.id.intValue() == info.id.intValue()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 从我已经选择的技能列表中删除技能
	 * @author Administrator
	 *
	 * @param info
	 */
	private void removeToHasList(SkillTextInfo info){
		for(int i=0; i<hasList.size(); i++){
			if(hasList.get(i).id.intValue() == info.id.intValue()){
				hasList.remove(i);
				break;
			}
		}
	}

	private void refreshData(boolean isRefresh) {
		if (isRefresh) {
			engine.requestGoddessSkillList(0, -1, LOADCOUNT, this);
		} else {
			int start = 0 - (adapter.getCount() + 1);
			engine.requestGoddessSkillList(0, start, LOADCOUNT, this);
		}
	}
	
	private void getPersonSkill() {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
		if(userEntity != null){
			loadDialog = DialogFactory.createLoadingDialog(this);
			engine.requestPersonSkillInfo(userEntity.uid.intValue(), this);
		}
	}
	
	private void dismissDialog(){
		if(loadDialog != null){
			loadDialog.dismiss();
		}
	}

	@SuppressWarnings("unchecked")
	private void setListViewShowData(boolean isRefresh, int isEnd, SkillTextInfoList list) {
		if(isRefresh){
			adapter.clearList();
		}
		
		if (!ListUtils.isEmpty(list)) {
			adapter.appendToList(adapter.getGoddessSkillListData(list));
		}

		if (isEnd == 0) {//还有数据
			pulltorefreshlistview.setMode(Mode.BOTH);
		} else {//加到底了
			pulltorefreshlistview.setMode(Mode.PULL_FROM_START);
		}
	}

	@Override
	public void onGoddessSkillListSuccess(int code, int isSend, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.GET_GODDESS_SKILL_LIST_SUCCESS, code, isSend, obj);
	}

	@Override
	public void onGoddessSkillListError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.GET_GODDESS_SKILL_LIST_ERROR, code);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//		isRefresh = true;
//		refreshData(isRefresh);
		sendHandlerMessage(mHandler, HandlerValue.PULL_REFRESH_GODDESS_SKILL_COMPLEMENT, 0);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = false;
		refreshData(isRefresh);
	}

	@Override
	public void saveOnSuccess(int code, String msg) {
		sendHandlerMessage(mHandler, HandlerValue.GET_SAVE_SKILL_LIST_SUCCESS, code, msg);
	}

	@Override
	public void saveOnError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.GET_SAVE_SKILL_LIST_ERROR, code);
	}

	@Override
	public void getPersonSkillInfoOnSuccess(int resCode, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.GET_PERSON_GODDESS_SKILL_INFO_SUCCESS, resCode, obj);
	}

	@Override
	public void getPersonSkillInfoOnError(int resCode) {
		sendHandlerMessage(mHandler, HandlerValue.GET_PERSON_GODDESS_SKILL_INFO_ERROR, resCode);
	}

	@Override
	public void selectedSkill(SkillTextInfo item) {
		sendHandlerMessage(mHandler, HandlerValue.ADD_GODDESS_SKILL, 0, item);
	}

	@Override
	public void deleteSkill(SkillTextInfo item) {
		sendHandlerMessage(mHandler, HandlerValue.DELETE_GODDESS_SKILL, 0, item);
	}

}
