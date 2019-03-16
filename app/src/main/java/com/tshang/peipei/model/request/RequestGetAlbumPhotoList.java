package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.PhotoInfoList;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetPhotoKeyList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetPhotoKeyList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: ReqGetAlbumPhotoList.java 
 *
 * @Description: 读取相册中的相片列表
 *
 * @author vactor
 *
 * @date 2014-4-4 上午11:36:17 
 *
 * @version V1.0   
 */
public class RequestGetAlbumPhotoList extends AsnBase implements ISocketMsgCallBack {

	private IGetAlbumPhotoList mGetAlbumPhotoList;

	public void getAlbumPhotoList(byte[] auth, int ver, int uid, int selfUid, int albumId, int start, int num, IGetAlbumPhotoList getAlbumPhotoList) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqGetPhotoKeyList req = new ReqGetPhotoKeyList();
		req.albumid = BigInteger.valueOf(albumId);
		req.num = BigInteger.valueOf(num);
		req.selfuid = BigInteger.valueOf(selfUid);
		req.start = BigInteger.valueOf(start);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETPHOTOKEYLIST_CID;
		goGirlPkt.reqgetphotokeylist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mGetAlbumPhotoList = getAlbumPhotoList;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != mGetAlbumPhotoList) {
			RspGetPhotoKeyList rsp = pkt.rspgetphotokeylist;
			int total = rsp.total.intValue();
			if (checkRetCode(rsp.retcode.intValue()))
				mGetAlbumPhotoList.getAlbumPhotoListCallBack(rsp.retcode.intValue(), total, rsp.photolist);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != mGetAlbumPhotoList) {
			mGetAlbumPhotoList.getAlbumPhotoListCallBack(resultCode, 0, null);
		}
	}

	public interface IGetAlbumPhotoList {

		public void getAlbumPhotoListCallBack(int retCode, int total, PhotoInfoList list);
	}

}
