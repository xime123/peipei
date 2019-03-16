package com.tshang.peipei.activity.harem;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.chat.ChatActivity;
import com.tshang.peipei.activity.harem.adapter.HaremGroupAdapter;
import com.tshang.peipei.activity.mine.MineFaqActivity;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfo;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfoList;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.harem.CreateHarem;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.view.ReplyChildListView;

import de.greenrobot.event.EventBus;

/**
 * 我的后宫界面
 * @author Jeff
 *
 */
public class MyHaremActivity extends BaseActivity {
	private int mFriendUid = 0;
	private int mSex = 0;
	private ReplyChildListView myHaremListView;
	private ReplyChildListView myJoinHaremListView;
	private HaremGroupAdapter myHaremAdapter;
	private HaremGroupAdapter myJoinHaremAdapter;
	private Button btnCreateHarem;
	private TextView tvEmptyJoinHarem;
	private TextView tvEmptyCreateHarem;
	private TextView tvCreateHaremTag;
	private TextView tvJoinHaremTag;
	private LinearLayout llCreateHarem;
	private static final int CREATE_HAREM_REQUEST_CODE = 1;
	private boolean isHost = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void onEvent(NoticeEvent event) {
		super.onEvent(event);
		if (event.getFlag() == NoticeEvent.NOTICE65) {
			CreateHarem.getInstance().getRelevantGroupList(this, mFriendUid, mHandler);//获取后宫列表
		}
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.HAREM_GET_GROUP_LIST_SUCCESS_VALUE:
			GroupInfoList infoList = (GroupInfoList) msg.obj;
			if (infoList != null) {
				ArrayList<GroupInfo> myCreateHaremList = new ArrayList<GroupInfo>();
				ArrayList<GroupInfo> myJoinHaremList = new ArrayList<GroupInfo>();
				for (Object object : infoList) {
					GroupInfo info = (GroupInfo) object;
					if (info != null) {
						if (info.owner.intValue() == mFriendUid) {
							myCreateHaremList.add(info);//我创建的后宫
						} else {
							myJoinHaremList.add(info);//我加入的后宫
						}
					}

				}
				if (myCreateHaremList.size() == 0) {
					if (isHost) {
						if (mSex == Gender.FEMALE.getValue()) {
							if (SharedPreferencesTools.getInstance(this).getIntValueByKey(BAConstants.PEIPEI_APP_CONFIG_IS_CREATE_HAREM_PESSIMION) == 0) {
								btnCreateHarem.setVisibility(View.GONE);
							} else {
								btnCreateHarem.setVisibility(View.VISIBLE);
								llCreateHarem.setVisibility(View.VISIBLE);
							}
						} else {
							btnCreateHarem.setVisibility(View.VISIBLE);
							llCreateHarem.setVisibility(View.VISIBLE);
						}
					} else {
						tvEmptyCreateHarem.setVisibility(View.VISIBLE);
						if (mSex == Gender.FEMALE.getValue()) {//性别不同，显示的文字也不一样
							tvEmptyCreateHarem.setText(R.string.str_her_not_create_harem);
							tvEmptyCreateHarem.setVisibility(View.GONE);
							tvCreateHaremTag.setVisibility(View.GONE);//女号暂时没有
						} else {
							tvEmptyCreateHarem.setText(R.string.str_he_not_create_harem);
						}
					}
				} else {
					btnCreateHarem.setVisibility(View.GONE);
					myHaremListView.setVisibility(View.VISIBLE);
					myHaremAdapter.setList(myCreateHaremList);
				}
				if (myJoinHaremList.size() == 0) {
					tvEmptyJoinHarem.setVisibility(View.VISIBLE);
					if (isHost) {
						myJoinHaremListView.setVisibility(View.GONE);
					} else {
						tvJoinHaremTag.setVisibility(View.VISIBLE);
						if (mSex == Gender.FEMALE.getValue()) {//性别不同，显示的文字也不一样
							tvEmptyJoinHarem.setText(R.string.str_her_not_join_harem);
						} else {
							tvEmptyJoinHarem.setText(R.string.str_he_not_join_harem);

						}
					}
				} else {
					tvEmptyJoinHarem.setVisibility(View.GONE);
					myJoinHaremListView.setVisibility(View.VISIBLE);
					myJoinHaremAdapter.setList(myJoinHaremList);
				}
			}
			break;
		case HandlerValue.HAREM_GET_GROUP_LIST_FAILED_VALUE:
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
			MineFaqActivity.openMineFaqActivity(this, MineFaqActivity.GROUP_VALUE);
			break;
		case R.id.btn_create_harem:
			BaseUtils.openResultActivity(this, CreateHaremActivity.class, null, CREATE_HAREM_REQUEST_CODE);
			break;

		default:
			break;
		}
	}

	@Override
	protected void initData() {
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			mFriendUid = bundle.getInt("frienduid", 0);
			mSex = bundle.getInt("msex", 0);
			int accessStatus = UserUtils.getAccessStatus(this, mFriendUid, mSex);
			switch (accessStatus) {
			case UserUtils.HOST_MALE_STATUS://男主人态
				mTitle.setText(R.string.str_my_harem);
				isHost = true;
			case UserUtils.HOST_FEMALE_STATUS://女主人态
				tvCreateHaremTag.setVisibility(View.GONE);//女号暂时没有
				break;
			case UserUtils.GUEST_FEMALE_STATUS://访问女客人态
				isHost = false;
				mTitle.setText(R.string.str_female_other_harem);
				tvCreateHaremTag.setVisibility(View.GONE);//女号暂时没有
				tvCreateHaremTag.setText(R.string.str_female_other_harem);
				tvJoinHaremTag.setText(R.string.str_her_join_harem);
				break;
			case UserUtils.GUEST_MALE_STATUS://访问男客人态
				isHost = false;
				mTitle.setText(R.string.str_male_other_harem);
				tvCreateHaremTag.setText(R.string.str_male_other_harem);
				tvJoinHaremTag.setText(R.string.str_he_join_harem);
				break;

			default:
				break;
			}
		}

		myHaremAdapter = new HaremGroupAdapter(this, 0);
		myHaremListView.setAdapter(myHaremAdapter);
		myJoinHaremAdapter = new HaremGroupAdapter(this, 0);
		myJoinHaremListView.setAdapter(myJoinHaremAdapter);

		CreateHarem.getInstance().getRelevantGroupList(this, mFriendUid, mHandler);//获取后宫列表
	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.private_page);
		mBackText.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTextRight = (TextView) findViewById(R.id.title_tv_right);
		mTextRight.setText(R.string.str_harem_rule);
		mTextRight.setVisibility(View.VISIBLE);
		mTextRight.setOnClickListener(this);

		llCreateHarem = (LinearLayout) findViewById(R.id.ll_create_view);
		myHaremListView = (ReplyChildListView) findViewById(R.id.lv_my_create_harem);
		myHaremListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				GroupInfo info = myHaremAdapter.getList().get(position);
				intentManagerHaremActivity(info, true);
			}
		});
		myJoinHaremListView = (ReplyChildListView) findViewById(R.id.lv_my_join_harem);
		myJoinHaremListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				GroupInfo info = myJoinHaremAdapter.getList().get(position);
				intentManagerHaremActivity(info, false);
			}
		});
		btnCreateHarem = (Button) findViewById(R.id.btn_create_harem);
		btnCreateHarem.setOnClickListener(this);
		tvEmptyJoinHarem = (TextView) findViewById(R.id.tv_my_join_harem_empty);
		tvEmptyCreateHarem = (TextView) findViewById(R.id.tv_my_create_harem_empty);
		tvCreateHaremTag = (TextView) findViewById(R.id.tv_my_create_harem_tag);
		tvJoinHaremTag = (TextView) findViewById(R.id.tv_my_join_harem_tag);

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == RESULT_OK) {
			if (arg0 == CREATE_HAREM_REQUEST_CODE) {
				CreateHarem.getInstance().getRelevantGroupList(this, mFriendUid, mHandler);//获取后宫列表
			}
		}
	}

	private void intentManagerHaremActivity(GroupInfo info, boolean isCreate) {
		if (info != null) {
			int groupid = info.groupid.intValue();
			String groupName = new String(info.groupname);
			if (TextUtils.isEmpty(groupName)) {
				groupName = "";
			}
			String groupbadgekey = new String(info.groupbadgekey);
			if (TextUtils.isEmpty(groupbadgekey)) {
				groupbadgekey = "";
			}
			String groupNotice = new String(info.groupnotice);
			if (TextUtils.isEmpty(groupNotice)) {
				groupNotice = "";
			}
			if (isHost) {//主人进入到群聊
				ChatActivity.intentChatActivity(MyHaremActivity.this, groupid, groupName, 1, true, false, 0);
			} else {//客人看群信息，可以申请加入
				Bundle bundle = new Bundle();
				bundle.putInt("groupid", groupid);
				bundle.putInt("from", 0);
				BaseUtils.openActivity(MyHaremActivity.this, ManagerHaremActivity.class, bundle);
			}

		}
	}

	@Override
	protected int initView() {
		return R.layout.activity_harem_my;
	}
}
