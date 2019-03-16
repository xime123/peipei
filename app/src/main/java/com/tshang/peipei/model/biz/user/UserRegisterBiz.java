package com.tshang.peipei.model.biz.user;

import android.content.Context;

import com.tshang.peipei.R;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatDes;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.model.biz.chat.ChatManageBiz;
import com.tshang.peipei.model.biz.chat.ChatSessionManageBiz;
import com.tshang.peipei.model.event.MainEvent;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.operate.ChatOperate;
import com.tshang.peipei.storage.database.operate.PeipeiSessionOperate;

import de.greenrobot.event.EventBus;

/**
 * @Title: UserRegisterBiz.java 
 *
 * @Description: 注册后添加陪陪系统消息
 *
 * @author allen  
 *
 * @date 2014-7-18 下午8:22:23 
 *
 * @version V1.0   
 */
public class UserRegisterBiz {
	public void insertMessageToPeipei(Context context) {
		boolean b = ChatSessionManageBiz.isHaveSession(context, BAConstants.PEIPEI_XIAOPEI, 0);
		if (!b) {
			ChatManageBiz.haveNotSession(context, BAConstants.PEIPEI_XIAOPEI, context.getString(R.string.str_system_user), Gender.FEMALE.getValue(),
					0, "暂无消息");
		}

		ChatDatabaseEntity chatDatabaseEntity = new ChatDatabaseEntity();
		chatDatabaseEntity.setStatus(ChatStatus.SUCCESS.getValue());
		chatDatabaseEntity.setDes(ChatDes.TO_ME.getValue());
		chatDatabaseEntity.setFromID(BAConstants.PEIPEI_XIAOPEI);
		chatDatabaseEntity.setMesSvrID("");
		chatDatabaseEntity.setProgress(0);
		chatDatabaseEntity.setCreateTime(System.currentTimeMillis());
		String sessionMsg;
		chatDatabaseEntity.setType(MessageType.PEIPEI_SYSTEM.getValue());
		sessionMsg = "欢迎您来到陪陪，陪陪是一个陪你聊天陪你玩儿的地方，所有女孩都是可以通过礼物来收买的哦。如果有不明白的地方，请查看<a href>系统帮助";
		chatDatabaseEntity.setMessage(sessionMsg);

		ChatOperate chatDatabase = ChatOperate.getInstance(context, BAConstants.PEIPEI_XIAOPEI, false);
		if (chatDatabase == null)
			return;
		chatDatabase.insert(chatDatabaseEntity);
		chatDatabase = null;

		ChatSessionManageBiz.upDataSession(context, sessionMsg, context.getString(R.string.str_system_user), BAConstants.PEIPEI_XIAOPEI,
				System.currentTimeMillis(), 0);

		PeipeiSessionOperate sessionOperate = PeipeiSessionOperate.getInstance(context);
		if (sessionOperate == null)
			return;
		int unread = sessionOperate.getUnreadCount(BAConstants.PEIPEI_XIAOPEI, 0);
		sessionOperate.updateUnreadCount(unread + 1, BAConstants.PEIPEI_XIAOPEI, 0);
		sessionOperate = null;

		//刷新界面
		int num = ChatSessionManageBiz.isExistUnreadMessage(context);
		if (num > 0) {
			MainEvent mainEvent = new MainEvent();
			mainEvent.setMainStr("message");
			mainEvent.setNum(num);
			EventBus.getDefault().postSticky(mainEvent);
		}
	}
}
