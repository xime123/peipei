package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.GroupMemberInfoList;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetGroupMemberList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetGroupMemberList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGroupMemberInfoList.java 
 *
 * @Description: 后宫成员
 *
 * @author Jeff 
 *
 * @date 2014-9-18 上午13:40:54 
 *
 * @version V1.3.0   
 */
public class RequestGroupMemberInfoList extends AsnBase implements ISocketMsgCallBack {

	private IGetGroupMemberInfoList callback;

	public void getGroupMemberInfoList(byte[] auth, int ver, int groupid, int selfuid, int start, int num, IGetGroupMemberInfoList callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetGroupMemberList req = new ReqGetGroupMemberList();
		req.groupid = BigInteger.valueOf(groupid);
		req.selfuid = BigInteger.valueOf(selfuid);
		req.start = BigInteger.valueOf(start);
		req.num = BigInteger.valueOf(num);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETGROUPMEMBERLIST_CID;
		goGirlPkt.reqgetgroupmemberlist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callback = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != callback) {
			RspGetGroupMemberList rsp = pkt.rspgetgroupmemberlist;
			int retCode = rsp.retcode.intValue();
			GroupMemberInfoList infoList = rsp.memberlist;
			if (checkRetCode(retCode))
				callback.getgroupMemberInfoList(retCode, rsp.maxmembernum.intValue(), infoList);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.getgroupMemberInfoList(resultCode, 0, null);
		}

	}

	public interface IGetGroupMemberInfoList {
		public void getgroupMemberInfoList(int retCode, int maxnum, GroupMemberInfoList infoList);
	}

}
