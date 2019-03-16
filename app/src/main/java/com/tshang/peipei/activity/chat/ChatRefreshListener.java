package com.tshang.peipei.activity.chat;

import java.util.List;

import android.app.Activity;
import android.widget.AbsListView;
import android.widget.ListView;

import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.chat.ChatManageBiz;
import com.tshang.peipei.network.socket.ThreadPoolService;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

/**
 * @Title: ChatRefreshListener.java 
 *
 * @Description: 私聊对话内容刷新
 *
 * @author allen  
 *
 * @date 2014-7-30 下午4:54:45 
 *
 * @version V1.0   
 */
public class ChatRefreshListener implements OnRefreshListener<ListView> {

	private Activity context;
	private int mFriendUid;
	private ChatAdapter mChatListAdpater;
	private ChatManageBiz mChatManageBiz;
	private static final int mShowNum = 10;
	private boolean isGroup;
	private PullToRefreshListView listView;
	private BAHandler handler;

	public ChatRefreshListener(Activity context, ChatAdapter mChatListAdpater, ChatManageBiz mChatManageBiz, int mFriendUid, boolean isGroup,
			PullToRefreshListView listView, BAHandler handler) {
		this.context = context;
		this.mChatListAdpater = mChatListAdpater;
		this.mChatManageBiz = mChatManageBiz;
		this.mFriendUid = mFriendUid;
		this.isGroup = isGroup;
		this.listView = listView;
		this.handler = handler;
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		try {
			listView.getRefreshableView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
		} catch (Exception e) {//防止魅族手机崩溃
			e.printStackTrace();
		}
		ThreadPoolService.getInstance().execute(new Runnable() {

			@Override
			public void run() {
				int size = mChatListAdpater.getCount();
				if (size > 0) {
					List<ChatDatabaseEntity> temp = mChatManageBiz.getChatList(context, mFriendUid, size, mShowNum, isGroup);
					HandlerUtils.sendHandlerMessage(handler, HandlerValue.CHAT_LOAD_HISTORY_DATA_VALUE, temp);
					if (temp.isEmpty() || temp.size() < mShowNum) {
						HandlerUtils.sendHandlerMessage(handler, HandlerValue.CHAT_LOAD_HISTORY_NO_DATA_VALUE);
					}
				} else {
					HandlerUtils.sendHandlerMessage(handler, ChatBaseActivity.REFRESH_FLAG);
				}
			}
		});
	}
}