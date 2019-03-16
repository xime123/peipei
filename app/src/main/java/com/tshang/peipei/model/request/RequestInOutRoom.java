package com.tshang.peipei.model.request;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.BaseProtobuf;
import com.tshang.peipei.protocol.Gogirl;
import com.tshang.peipei.protocol.Gogirl.GoGirlBaseMsg;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.AsnProtocolTools;
import com.tshang.peipei.protocol.protobuf.InvalidProtocolBufferException;

/**
 * @Title: RequestInOutRoom.java 
 *
 * @Description: 进出房间 
 *
 * @author allen  
 *
 * @date 2015-1-15 下午5:54:11 
 *
 * @version V1.0   
 */
public class RequestInOutRoom extends AsnBase implements ISocketMsgCallBack {
	private IInOutRoom callback;
	private int act;

	public void InOutRoom(byte[] auth, int ver, int act, int roomid, int Selfuid, int uid, IInOutRoom callback) {
		this.callback = callback;
		this.act = act;
		String host = "ppapp.tshang.com";
		if (BAConstants.IS_TEST) {
			host = "pptest.yidongmengxiang.com";
		}

		String url = "/appbiz/api?cid=" + GoGirlBaseMsg.PacketId.ReqInOutRoom_cid_VALUE;

		byte[] body = getRoom(auth, ver, act, roomid, Selfuid, uid);
		if (body != null) {
			byte[] requests = http_encode_post(url, host, body);
			PeiPeiRequest request = new PeiPeiRequest(requests, this, false, host, 80);
			AppQueueManager.getInstance().addRequest(request);
		}
	}

	private byte[] getRoom(byte[] auth, int ver, int act, int roomid, int Selfuid, int uid) {
		Gogirl.ReqInOutRoom.Builder builder = Gogirl.ReqInOutRoom.newBuilder();
		builder.setAct(act);
		builder.setRoomid(roomid);
		builder.setSelfuid(Selfuid);
		builder.setAffecteduid(uid);
		builder.setBasemsg(BaseProtobuf.setBaseMsg(auth, ver, GoGirlBaseMsg.PacketId.valueOf(GoGirlBaseMsg.PacketId.ReqInOutRoom_cid_VALUE)));

		Gogirl.ReqInOutRoom msg = builder.build();
		return msg.toByteArray();
	}

	@Override
	public void succuess(byte[] msg) {
		if (msg != null && msg.length > 0) {
			int len = AsnProtocolTools.http_net_body_length(msg);
			int totalLen = msg.length;
			if (len > 0) {
				byte[] tempByte = new byte[len];
				System.arraycopy(msg, totalLen - len, tempByte, 0, len);

				try {
					Gogirl.RspInOutRoom rsp = Gogirl.RspInOutRoom.parseFrom(tempByte);
					int retCode = rsp.getRetcode();
					if (checkRetCode(retCode)) {
						if (callback != null) {
							callback.resultInOut(rsp.getRetcode(), act, rsp.getRole());
						}
					}
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}

			}

		}

	}

	@Override
	public void error(int resultCode) {
		if (callback != null) {
			callback.resultInOut(resultCode, act, 0);
		}
	}

	public interface IInOutRoom {
		public void resultInOut(int retCode, int act, int role);
	}

}
