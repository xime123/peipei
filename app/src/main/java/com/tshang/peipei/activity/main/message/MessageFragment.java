package com.tshang.peipei.activity.main.message;

import java.util.ArrayList;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.WheelActivity;
import com.tshang.peipei.activity.chat.ChatActivity;
import com.tshang.peipei.activity.chat.MessageVisitorActivity;
import com.tshang.peipei.activity.dialog.DeleteMessageRecordDialog;
import com.tshang.peipei.activity.dialog.DeleteMessageRecordDialog.RefreshMessageListener;
import com.tshang.peipei.activity.main.BaseFragment;
import com.tshang.peipei.activity.main.message.adapter.MainMessageAdapter;
import com.tshang.peipei.activity.mine.MineBlackListActivity;
import com.tshang.peipei.activity.reward.RewardListActivity;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatFromType;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.ChatRecordBiz;
import com.tshang.peipei.model.biz.chat.ChatSessionManageBiz;
import com.tshang.peipei.model.event.MainEvent;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.network.socket.ThreadPoolService;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.SessionDatabaseEntity;

import de.greenrobot.event.EventBus;

/**
 * @Title: MainMessageFragment
 *
 * @Description: 主界面消息界面
 *
 * @author allen
 *
 * @version V1.0   
 */
public class MessageFragment extends BaseFragment implements OnItemClickListener, OnItemLongClickListener, RefreshMessageListener {

	public static final int VIEW_ME_USER_ID = -1;//谁看过我的，默认为-1的用户id
	public static final int REQUESTCODE = 10;

	private final static int ONEVENTMAIN = 113;//长连接私聊界面消息数量

	private ListView mMessage;
	private MainMessageAdapter mMessageAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		EventBus.getDefault().registerSticky(this);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message, null);

		initUi(view);
		return view;
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.CHAT_MESSAGE_ORDER_TIME_VALUE:
			ArrayList<SessionDatabaseEntity> list = (ArrayList<SessionDatabaseEntity>) msg.obj;
			mMessageAdapter.setList(list);
			break;
		case ONEVENTMAIN:
			MainEvent mainEvent = (MainEvent) msg.obj;
			if (!TextUtils.isEmpty(mainEvent.getMainStr())) {
				if (mainEvent.getMainStr().equals("message")) {
					if (mainEvent.getNum() != 0) {
						getMessageTimeOrder();
					}
				}
			}
			break;
		case HandlerValue.SESSION_CHAT_BLACK:
			BaseUtils.openActivity(getActivity(), MineBlackListActivity.class);
			break;
		case HandlerValue.SESSION_CHAT_LIMET:
			Intent intent = new Intent(getActivity(), WheelActivity.class);
			intent.putExtra(WheelActivity.FROM, "MineMessageFragment");
			if (BAApplication.mLocalUserInfo != null)
				intent.putExtra(WheelActivity.COUNT, BAApplication.mLocalUserInfo.chatthreshold.intValue());
			startActivityForResult(intent, REQUESTCODE);
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		getMessageTimeOrder();
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	private void initUi(View view) {
		mMessage = (ListView) view.findViewById(R.id.main_message_lvw);

		mMessageAdapter = new MainMessageAdapter(getActivity());
		mMessage.setAdapter(mMessageAdapter);
		mMessage.setOnItemLongClickListener(this);
		mMessage.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SessionDatabaseEntity messageEntity = (SessionDatabaseEntity) parent.getAdapter().getItem(position);

		if (messageEntity != null) {
			if (messageEntity.getUserID() == VIEW_ME_USER_ID) {
				BaseUtils.openActivity(getActivity(), MessageVisitorActivity.class);
			} else {
				int type = messageEntity.getType();
				if (type == ChatFromType.CHAT_PRIVATE.getValue()) {//私聊
					String alias = SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "_remark")
							.getAlias(messageEntity.getUserID());
					String sendUserName = TextUtils.isEmpty(alias) ? messageEntity.getNick() : alias;
					ChatActivity.intentChatActivity(getActivity(), messageEntity.getUserID(), sendUserName, messageEntity.getSex(), false, false, 0);
				} else if (type == ChatFromType.CHAT_GROUP.getValue()) {//群聊 后营
					if (BaseTimes.isChangeGroupChatBg(getActivity())) {
						int number = new Random().nextInt(11);
						String key = "Group_" + String.valueOf(messageEntity.getUserID()) + "#" + BAApplication.mLocalUserInfo.uid.intValue();
						SharedPreferencesTools.getInstance(getActivity()).saveIntKeyValue(number, key);
					}
					ChatActivity.intentChatActivity(getActivity(), messageEntity.getUserID(), messageEntity.getNick(), messageEntity.getSex(), true,
							false, 0);
				} else if (type == ChatFromType.CHAT_ANONYM.getValue()) {//匿名 私聊
					ChatActivity.intentChatActivity(getActivity(), messageEntity.getUserID(), messageEntity.getNick(), messageEntity.getSex(), false,
							false, RewardListActivity.CHAT_FROM_REWARD);
				}
			}
		}

		//		else if (messageEntity.getUserID() == BAConstants.PEIPEI_CHAT_XIAOPEI) {
		//			ChatActivity.intentChatActivity(getActivity(), BAConstants.PEIPEI_CHAT_XIAOPEI, getString(R.string.xiaopei),
		//					Gender.FEMALE.getValue(), false, false, 0);
		//		} 

	}

	public void onEventMainThread(MainEvent mainEvent) {
		Message msg = Message.obtain();
		msg.what = ONEVENTMAIN;
		msg.obj = mainEvent;
		if (mHandler != null)
			mHandler.sendMessage(msg);
	}

	public void onEvent(NoticeEvent event) {
		if (event.getFlag() == NoticeEvent.NOTICE68) {
			getMessageTimeOrder();
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {//左右切换会调用这个方法
		if (this.isVisible()) {
			if (isVisibleToUser) {
				getMessageTimeOrder();
			}
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		SessionDatabaseEntity entity = (SessionDatabaseEntity) mMessage.getAdapter().getItem(position);
		if (entity != null) {
			if (entity.getUserID() == VIEW_ME_USER_ID || entity.getUserID() == BAConstants.PEIPEI_CHAT_XIAOPEI) {
				return true;
			}
			new DeleteMessageRecordDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, entity, this).showDialog();
			return true;
		}
		return false;
	}

	@Override
	public void refreshMessage() {
		getMessageTimeOrder();
		NoticeEvent noticeEvent = new NoticeEvent();
		noticeEvent.setFlag(NoticeEvent.NOTICE67);
		EventBus.getDefault().post(noticeEvent);

	}

	private void getMessageTimeOrder() {//会话列表有更改，需要更新界面
		if (UserUtils.getUserEntity(getActivity()) != null) {
			ThreadPoolService.getInstance().execute(new Runnable() {

				@Override
				public void run() {
					ArrayList<SessionDatabaseEntity> allList = new ArrayList<SessionDatabaseEntity>();
					SessionDatabaseEntity entity = new SessionDatabaseEntity();
					entity.setSessionData(getActivity().getString(R.string.who_saw_me1));
					entity.setUserID(VIEW_ME_USER_ID);//说明是看过我的人
					allList.add(entity);
					//					PeipeiSessionOperate ppOperate = PeipeiSessionOperate.getInstance(getActivity());
					//					SessionDatabaseEntity sessionEntity = ppOperate.selectSessionDate(BAConstants.PEIPEI_CHAT_XIAOPEI, 0);
					//					sessionEntity.setUserID(BAConstants.PEIPEI_CHAT_XIAOPEI);
					//					allList.add(sessionEntity);
					ArrayList<SessionDatabaseEntity> list = ChatSessionManageBiz.chatSessionDataWithRange(getActivity());
					list = deleteAnonymChatMsg(list);
					if (list != null) {
						allList.addAll(list);
					}
					HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.CHAT_MESSAGE_ORDER_TIME_VALUE, allList);
				}
			});
		}
	}

	//12小时 
	private long chatAnonymValidTime = 60 * 60 * 12 * 1000;

	/**
	 * 删除匿名私聊会话框
	 * @author Aaron
	 *
	 * @param list
	 * @return
	 */
	private ArrayList<SessionDatabaseEntity> deleteAnonymChatMsg(ArrayList<SessionDatabaseEntity> list) {
		try {
			for (int i = 0; i < list.size(); i++) {
				long rewardEndTime = SharedPreferencesTools.getInstance(getActivity()).getLongKeyValueV2(
						String.valueOf(list.get(i).getUserID()) + "_endRewardTime");
				if (list.get(i).getType() == ChatFromType.CHAT_ANONYM.getValue() && rewardEndTime > 0
						&& System.currentTimeMillis() - rewardEndTime > chatAnonymValidTime) {
					boolean b = ChatRecordBiz.clearDbMessage(getActivity(), list.get(i).getUserID(), false);
					if (b) {
						ChatSessionManageBiz.removeChatSessionWithUserID(getActivity(), list.get(i).getUserID(), list.get(i).getType());
						SharedPreferencesTools.getInstance(getActivity()).remove(String.valueOf(list.get(i).getUserID()) + "_endRewardTime");//时间移除
						list.remove(i);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
