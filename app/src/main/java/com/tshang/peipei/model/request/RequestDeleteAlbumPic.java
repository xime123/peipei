package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqDelAlbumPic;
import com.tshang.peipei.protocol.asn.gogirl.RspDelAlbumPic;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestDeletePhoto.java 
 *
 * @Description: 删除相片
 *
 * @author vactor
 *
 * @date 2014-5-10 上午10:08:13 
 *
 * @version V1.0   
 */
public class RequestDeleteAlbumPic extends AsnBase implements ISocketMsgCallBack {

	IDeleteAlbumPic iDeleteAlbumPic;

	public void deleteAlbumPic(byte[] auth, int ver, int uid, int albumId, int photoId, IDeleteAlbumPic callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqDelAlbumPic req = new ReqDelAlbumPic();
		req.albumid = BigInteger.valueOf(albumId);
		req.photoid = BigInteger.valueOf(photoId);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQDELALBUMPIC_CID;
		goGirlPkt.reqdelalbumpic = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iDeleteAlbumPic = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != iDeleteAlbumPic) {
			RspDelAlbumPic rsp = pkt.rspdelalbumpic;
			int resultCode = rsp.retcode.intValue();
			int picId = rsp.photoid.intValue();
			if (checkRetCode(resultCode))
				iDeleteAlbumPic.deletePhotoCallBack(resultCode, picId);

		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iDeleteAlbumPic) {
			iDeleteAlbumPic.deletePhotoCallBack(resultCode, -1);

		}
	}

	public interface IDeleteAlbumPic {
		public void deletePhotoCallBack(int resultCode, int picId);
	}

}
