package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGoGirlConnSvrV2;
import com.tshang.peipei.protocol.asn.gogirl.ReqGoGirlHeartBeat;
import com.tshang.peipei.protocol.asn.gogirl.ReqGoGirlTransChatData;
import com.tshang.peipei.protocol.asn.gogirl.ReqGoGirlTransOtherData;
import com.tshang.peipei.protocol.asn.gogirl.RspGoGirlConnSvrV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestPersist.java 
 *
 * @Description: 长连接 
 *
 * @author allen  
 *
 * @date 2014-4-15 下午4:58:28 
 *
 * @version V1.0   
 */
public class RequestPersist extends AsnBase implements ISocketMsgCallBack {

	private IPersistSer persistSer;

	private byte[] auth;
	private int ver;
	private int uid;

	private static RequestPersist reqRessist;

	public static RequestPersist getReqRessist() {
		synchronized (RequestPersist.class) {
			if (reqRessist == null) {
				reqRessist = new RequestPersist();
			}
		}
		return reqRessist;
	}

	public void closePersist() {
		reqRessist = null;
	}

	public void openPersist(byte[] auth, int ver, int uid, IPersistSer persistSer) {
		if (reqRessist == null) {
			reqRessist = new RequestPersist();
		}
		this.auth = auth;
		this.ver = ver;
		this.uid = uid;

		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqGoGirlConnSvrV2 req = new ReqGoGirlConnSvrV2();
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGOGIRLCONNSVRV2_CID;
		goGirlPkt.reqgogirlconnsvrv2 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, reqRessist, true);
		request.setIs_create_conn(true);//设置不重复
		this.persistSer = persistSer;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != persistSer) {
			if (pkt.choiceId == GoGirlPkt.RSPGOGIRLCONNSVRV2_CID) {
				RspGoGirlConnSvrV2 rsp = pkt.rspgogirlconnsvrv2;
				persistSer.getPersistSer(rsp.retcode.intValue(), rsp, 0);
			} else if (pkt.choiceId == GoGirlPkt.REQGOGIRLTRANSCHATDATA_CID) {
				ReqGoGirlTransChatData rsp = pkt.reqgogirltranschatdata;
				persistSer.getPersistSer(0, rsp, ydmxMsg.seq.intValue());
			} else if (pkt.choiceId == GoGirlPkt.REQGOGIRLHEARTBEAT_CID) {
				BaseLog.w("long_conn", "-------------收到心跳包----------");
				ReqGoGirlHeartBeat rsp = pkt.reqgogirlheartbeat;
				persistSer.getPersistSer(0, rsp, ydmxMsg.seq.intValue());
			} else if (pkt.choiceId == GoGirlPkt.REQGOGIRLTRANSOTHERDATA_CID) {
				ReqGoGirlTransOtherData rsp = pkt.reqgogirltransotherdata;
				persistSer.getPersistSer(0, rsp, ydmxMsg.seq.intValue());
			}
		}
	}

	@Override
	public void error(int resultCode) {
		if (resultCode == ISocketMsgCallBack.RECONNECT) {
			openPersist(auth, ver, uid, persistSer);
		}
		if (null != persistSer) {
			persistSer.getPersistSer(resultCode, null, 0);

		}

	}

	public interface IPersistSer {

		public void getPersistSer(int retCode, Object obj, int seq);
	}

}
