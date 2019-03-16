package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetGiftShowList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetGiftShowList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestGetGiftShowList.java 
 *
 * @Description: 秀场礼物  
 *
 * @author allen
 *
 * @date 2015-1-28 下午4:58:10 
 *
 * @version V1.0   
 */
public class RequestGetGiftShowList extends AsnBase implements ISocketMsgCallBack {

	private IGetGiftShowList callback;

	public void getGiftShow(byte[] auth, int ver, int uid, int start, int num, IGetGiftShowList callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetGiftShowList req = new ReqGetGiftShowList();
		req.num = BigInteger.valueOf(num);
		req.start = BigInteger.valueOf(start);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETGIFTSHOWLIST_CID;
		goGirlPkt.reqgetgiftshowlist = req;
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
			RspGetGiftShowList rsp = pkt.rspgetgiftshowlist;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				callback.resultGetGiftShowList(retCode, rsp.giftlist);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.resultGetGiftShowList(resultCode, null);
		}

	}

	public interface IGetGiftShowList {
		public void resultGetGiftShowList(int retCode, GiftInfoList list);
	}

}
