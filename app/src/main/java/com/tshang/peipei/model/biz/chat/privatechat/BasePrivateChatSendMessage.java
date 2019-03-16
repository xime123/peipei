package com.tshang.peipei.model.biz.chat.privatechat;

import android.app.Activity;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.reward.RewardListActivity;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.ISentMessageCallBack;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.BaseChatSendMessage;
import com.tshang.peipei.model.biz.chat.ChatManageBiz;
import com.tshang.peipei.model.biz.chat.ChatRecordBiz;
import com.tshang.peipei.model.entity.ChatMessageReceiptEntity;
import com.tshang.peipei.model.request.RequestSentMessage;
import com.tshang.peipei.model.request.RequestSentMessageV2;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: BasePrivateChatSendMessage.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Jeff   私聊
 *
 * @date 2014年10月24日 下午3:30:52 
 *
 * @version V1.4.0   
 */
public class BasePrivateChatSendMessage extends BaseChatSendMessage implements ISentMessageCallBack {

	public void sendPrivateChatMsg(Activity activity, byte[] data, int type, int length, int friendUid, String burnID, String fnick, int fsex,
			long msgLocalId, int chatType, ChatDatabaseEntity chatDatabaseEntity) {
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(activity);
		if (userInfo == null) {
			return;
		}
		if (chatType == RewardListActivity.CHAT_FROM_REWARD) {//匿名悬赏
			RequestSentMessageV2 requestSentMessage = new RequestSentMessageV2();
			requestSentMessage.sentMessage(userInfo.auth, BAApplication.app_version_code, userInfo.uid.intValue(), data, type, length, friendUid,
					burnID, new String(userInfo.nick), fnick, userInfo.sex.intValue(), fsex, msgLocalId, chatDatabaseEntity, this);
		} else {
			RequestSentMessage requestSentMessage = new RequestSentMessage();
			requestSentMessage.sentMessage(userInfo.auth, BAApplication.app_version_code, userInfo.uid.intValue(), data, type, length, friendUid,
					burnID, new String(userInfo.nick), fnick, userInfo.sex.intValue(), fsex, msgLocalId, chatDatabaseEntity, this);
		}
	}

	@Override
	public void sentMessageCallBack(byte[] auth, int retCode, ChatMessageReceiptEntity receipt, ChatDatabaseEntity chatDatabaseEntity) {
		if (receipt == null || activiy == null || receipt == null || chatDatabaseEntity == null) {
			return;
		}
		if (receipt.getType() != MessageType.RECEIPT.getValue()) {
			if (retCode == 0) {
				chatDatabaseEntity.setStatus(ChatStatus.SUCCESS.getValue());
				ChatManageBiz.getInManage(activiy).changeMessageStatusByLocalID(activiy, receipt.getfUid(), ChatStatus.SUCCESS.getValue(),
						receipt.getLocalId(), receipt.getTime(), false);
			} else {
				chatDatabaseEntity.setStatus(ChatStatus.FAILED.getValue());
				ChatManageBiz.getInManage(activiy).changeMessageStatusByLocalID(activiy, receipt.getfUid(), ChatStatus.FAILED.getValue(),
						receipt.getLocalId(), receipt.getTime(), false);
				if (retCode == BAConstants.rspContMsgType.E_GG_BLACKLIST) {//黑名单
					ChatDatabaseEntity entity = ChatManageBiz.insertMessage(activiy, receipt.getfUid(),
							activiy.getString(R.string.add_black_content), receipt.getTime(), false);
					HandlerUtils.sendHandlerMessage(handler, HandlerValue.CHAT_APPEND_DATA_VALUE, entity);
				} else if (retCode == BAConstants.rspContMsgType.E_GG_FORBIT) {//被禁言了
					HandlerUtils.sendHandlerMessage(handler, HandlerValue.CHAT_FORBIT_MESSAGE_VALUE);
				} else if (retCode == -28303) {//匿名私聊对方匿名ID过期
					HandlerUtils.sendHandlerMessage(handler, HandlerValue.CHAT_ANONYM_NICK_PAST);
				}
			}
		} else {
			if (retCode != 0) {//说明回执发送失败，需要存起来重新发
				ChatRecordBiz.saveReceipt(activiy, receipt.getfUid(), receipt.getBurnId(), receipt.getfNick(), receipt.getfSex());
			}
		}
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.CHAT_REFLESH_UI_VALUE, retCode, 0);//通知刷新界面
	}
}
