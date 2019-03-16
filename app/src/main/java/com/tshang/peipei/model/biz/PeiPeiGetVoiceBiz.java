package com.tshang.peipei.model.biz;

import android.text.TextUtils;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.model.biz.chat.ChatRecordBiz;
import com.tshang.peipei.model.request.RequestGetVoice;
import com.tshang.peipei.model.request.RequestGetVoice.IGetVocie;
import com.tshang.peipei.storage.database.operate.ChatOperate;

/**
 * @Title: PeiPeiGetVoiceBize.java 
 *
 * @Description: 通过key获取音频
 *
 * @author allen  
 *
 * @date 2014-4-17 下午9:11:40 
 *
 * @version V1.0   
 */
public class PeiPeiGetVoiceBiz implements IGetVocie {

	public void getVoiceByKey(byte[] key, long id, int friendUid) {
		RequestGetVoice requestGetVoice = new RequestGetVoice();
		requestGetVoice.setData(id, friendUid);
		requestGetVoice.getVoice("".getBytes(), BAApplication.app_version_code, key, this);
	}

	@Override
	public void getVocie(int retCode, byte[] data, long id, int friendUid, String fileName) {
		if (TextUtils.isEmpty(ChatRecordBiz.saveFile(BAApplication.getInstance().getApplicationContext(), friendUid, id, data, false))) {
			ChatOperate chatDatabase = ChatOperate.getInstance(BAApplication.getInstance().getApplicationContext(), friendUid, false);
			if (chatDatabase != null)
				chatDatabase.delete(id);
		}
	}

}
