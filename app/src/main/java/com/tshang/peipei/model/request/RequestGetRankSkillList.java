package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetRankSkillList;
import com.tshang.peipei.protocol.asn.gogirl.RetGGSkillInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetRankSkillList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetRankSkillList.java 
 *
 * @Description: 获取技能列表 
 *
 * @author allen  
 *
 * @date 2014-7-14 下午8:24:27 
 *
 * @version V1.0   
 */
public class RequestGetRankSkillList extends AsnBase implements ISocketMsgCallBack {

	private IMainSkill callback;

	public void getHallSkillList(byte[] auth, int ver, int uid, int start, int num, IMainSkill mainHotCallBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体	
		ReqGetRankSkillList req = new ReqGetRankSkillList();
		req.uid = BigInteger.valueOf(uid);
		req.start = BigInteger.valueOf(start);
		req.num = BigInteger.valueOf(num);
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETRANKSKILLLIST_CID;
		goGirlPkt.reqgetrankskilllist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		// 编码
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callback = mainHotCallBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callback) {
			RspGetRankSkillList rspGetMainList = pkt.rspgetrankskilllist;
			int isEnd = rspGetMainList.isend.intValue();
			callback.getMainSkillCallBack(rspGetMainList.retcode.intValue(), isEnd, rspGetMainList.skilllist);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.getMainSkillCallBack(resultCode, -1, null);
		}
	}

	public interface IMainSkill {
		public void getMainSkillCallBack(int retCode, int isEnd, RetGGSkillInfoList userInfoList);
	}

}
