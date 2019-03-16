package com.tshang.peipei.model.showrooms;

import java.util.List;

import android.app.Activity;

import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.protocol.Gogirl.GoGirlChatDataP;
import com.tshang.peipei.protocol.Gogirl.GoGirlDataInfoP;
import com.tshang.peipei.storage.database.operate.ShowsOperate;

/**
 * @Title: ShowsRoomDbBiz.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author allen  
 *
 * @date 2015-1-28 下午2:28:08 
 *
 * @version V1.0   
 */
public class ShowsRoomDbBiz {

	private Activity activity;
	private BAHandler handler;

	public ShowsRoomDbBiz(Activity activity) {
		this.activity = activity;
	}

	public void initDataToDB(List<GoGirlChatDataP> list) {
		ShowsOperate operate = ShowsOperate.getInstance(activity);
		GoGirlDataInfoP info = null;
		String[] str;
		for (GoGirlChatDataP goGirlChatDataP : list) {
			info = goGirlChatDataP.getChatdatalist(0);
			str = new String(info.getData().toByteArray()).split("/");
			if (!operate.isHaveSession(str[str.length - 1], info.getType())) {
				operate.insert(str[str.length - 1], info.getType(), 0);
			}
		}
	}

	public void initDataToDB(GoGirlChatDataP dataP) {
		ShowsOperate operate = ShowsOperate.getInstance(activity);
		GoGirlDataInfoP info = dataP.getChatdatalist(0);
		String[] str = new String(info.getData().toByteArray()).split("/");
		if (!operate.isHaveSession(str[str.length - 1], 45)) {
			operate.insert(str[str.length - 1], 45, 0);
		}
	}

	public void initDataToDBByVoice(String voicepath, int type) {
		ShowsOperate operate = ShowsOperate.getInstance(activity);
		String[] str = voicepath.split("/");
		if (!operate.isHaveSession(str[str.length - 1], type)) {
			operate.insert(str[str.length - 1], type, 0);
		}
	}

	public void updataStatus(String data, int status) {
		ShowsOperate operate = ShowsOperate.getInstance(activity);
		operate.updateStutas(data, status);
	}

	public int getStatusByData(String data, int type) {
		ShowsOperate operate = ShowsOperate.getInstance(activity);
		return operate.selectStatus(data, type);
	}
}
