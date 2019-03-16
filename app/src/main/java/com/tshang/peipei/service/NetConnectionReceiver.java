package com.tshang.peipei.service;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.model.biz.chat.ChatManageBiz;
import com.tshang.peipei.model.biz.chat.ChatRecordBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackSentChatMessage;
import com.tshang.peipei.model.entity.ChatMessageReceiptEntity;
import com.tshang.peipei.storage.database.entity.ReceiptEntity;
import com.tshang.peipei.vender.common.util.ListUtils;

/**
 * @Title: ConnectionChangeReceiver.java 
 *
 * @Description: 网络变化
 *
 * @author allen  
 *
 * @date 2014-6-16 下午3:55:21 
 *
 * @version V1.0   
 */
public class NetConnectionReceiver extends BroadcastReceiver implements BizCallBackSentChatMessage {
	//	private final Logger log = LoggerFactory.getLogger(NetConnectionReceiver.class);

	private static int lastType = -1;
	private Context mContext;

	@Override
	public void onReceive(Context context, Intent arg1) {
		//		String action = arg1.getAction();
		// 获得网络连接服务
		mContext = context;
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = connManager.getActiveNetworkInfo();

		if (info == null || !connManager.getBackgroundDataSetting()) {
			lastType = -1;
		} else {
			int netType = info.getType();
			if (netType != lastType) {
				if (info.isConnected()) {
					List<ReceiptEntity> list = ChatRecordBiz.getReceiptList(context);
					if (!ListUtils.isEmpty(list)) {
						if (BAApplication.mLocalUserInfo != null) {
							for (ReceiptEntity entity : list) {
								ChatManageBiz.getInManage(context).sentMsg(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
										BAApplication.mLocalUserInfo.uid.intValue(), "".getBytes(), MessageType.RECEIPT.getValue(), 0,
										entity.getFromID(), entity.getMesSvrID(), entity.getNick(), entity.getFNick(), entity.getSex(),
										entity.getFSex(), this, 0, 0);
							}
						}
					}
				}
				lastType = netType;
			}

		}
	}

	@Override
	public void getSentChatMessageCallBack(int retcode, ChatMessageReceiptEntity recepit) {
		if (retcode == 0)
			ChatRecordBiz.deletReceipte(mContext, recepit.getfUid(), recepit.getBurnId());

	}

}