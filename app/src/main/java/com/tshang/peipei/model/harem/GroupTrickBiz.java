package com.tshang.peipei.model.harem;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.request.RequestGroupTrick;
import com.tshang.peipei.model.request.RequestGroupTrick.iGroupTrick;

/**
 * @Title: GroupTrickBiz.java 
 *
 * @Description: 翻牌子
 *
 * @author allen  
 *
 * @date 2014-9-26 下午4:15:15 
 *
 * @version V1.0   
 */
public class GroupTrickBiz implements iGroupTrick {

	private BAHandler handler;

	public void groupTrick(byte[] auth, int ver, int uid, int trickuid, int groupid, BAHandler handler) {
		RequestGroupTrick req = new RequestGroupTrick();
		this.handler = handler;
		req.groupTrick(auth, ver, uid, trickuid, groupid, this);
	}

	@Override
	public void groupTrickBack(int retcode) {
		if (retcode == BAConstants.rspContMsgType.E_GG_GROUP_TRICK_NUM && handler != null) {
			handler.sendEmptyMessage(HandlerType.GROUP_TRICK_NUM);
		} else if (retcode == 0) {
			handler.sendEmptyMessage(HandlerType.GROUP_TRICK);
		}
	}
}
