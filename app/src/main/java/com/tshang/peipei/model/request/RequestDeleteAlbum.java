package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqDelAlbum;
import com.tshang.peipei.protocol.asn.gogirl.RspDelAlbum;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestDeleteAlbum.java 
 *
 * @Description: 删除相册
 *
 * @author vactor
 *
 * @date 2014-5-9 下午5:34:12 
 *
 * @version V1.0   
 */
public class RequestDeleteAlbum extends AsnBase implements ISocketMsgCallBack {

	IDeleteAlbum iDeleteAlbum;

	public void deleteAlbum(byte[] auth, int ver, int uid, int albumId, IDeleteAlbum callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqDelAlbum req = new ReqDelAlbum();
		req.albumid = BigInteger.valueOf(albumId);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQDELALBUM_CID;
		goGirlPkt.reqdelalbum = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iDeleteAlbum = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != iDeleteAlbum) {
			RspDelAlbum rsp = pkt.rspdelalbum;
			int resultCode = rsp.retcode.intValue();
			if (checkRetCode(resultCode))
				iDeleteAlbum.deleteAlbumCallBack(resultCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iDeleteAlbum) {
			iDeleteAlbum.deleteAlbumCallBack(resultCode);
		}
	}

	public interface IDeleteAlbum {
		public void deleteAlbumCallBack(int resultCode);
	}

}
