package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.RelationshipInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetRelationship;
import com.tshang.peipei.protocol.asn.gogirl.RspGetRelationship;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

public class RequestGetRelasionship extends AsnBase implements ISocketMsgCallBack {

	IGetRelationship mIGetRelationshi;
	private int isCheckAttention = 1;//1代表点击的是私聊，2代表检测是否关注 区分这样就不会跳转到私聊页面

	public void getRelashionShip(byte[] auth, int ver, int uid, int uid2, int isCheckAttention, IGetRelationship callBack) {
		this.isCheckAttention = isCheckAttention;
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqGetRelationship req = new ReqGetRelationship();
		req.selfuid = BigInteger.valueOf(uid);
		req.otheruid = BigInteger.valueOf(uid2);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETRELATIONSHIP_CID;
		goGirlPkt.reqgetrelationship = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mIGetRelationshi = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != mIGetRelationshi) {
			RspGetRelationship rsp = pkt.rspgetrelationship;
			int retCode = rsp.retcode.intValue();
			RelationshipInfo relation = rsp.relationship;
			if (checkRetCode(retCode))
				mIGetRelationshi.getRelationshipCallBack(retCode, isCheckAttention, relation);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != mIGetRelationshi) {
			mIGetRelationshi.getRelationshipCallBack(resultCode, isCheckAttention, null);
		}
	}

	public interface IGetRelationship {
		public void getRelationshipCallBack(int retCode, int isAttention, RelationshipInfo relation);
	}

}
