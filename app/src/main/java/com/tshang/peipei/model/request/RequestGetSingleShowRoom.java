package com.tshang.peipei.model.request;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.BaseProtobuf;
import com.tshang.peipei.protocol.Gogirl;
import com.tshang.peipei.protocol.Gogirl.GoGirlBaseMsg;
import com.tshang.peipei.protocol.Gogirl.ShowRoomInfo;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.AsnProtocolTools;
import com.tshang.peipei.protocol.protobuf.InvalidProtocolBufferException;

/**
 * @Title: RequestGetDevoteRank.java 
 *
 * @Description: 获取单个秀场消息
 *
 * @author allen  
 *
 * @date 2015-1-22 下午4:02:05 
 *
 * @version V1.0   
 */
public class RequestGetSingleShowRoom extends AsnBase implements ISocketMsgCallBack {

	private IGetSingleShowRoom callback;

	public void getRoomInfo(byte[] auth, int ver, int roomid, int Selfuid, int uid, IGetSingleShowRoom callback) {
		this.callback = callback;
		String host = "ppapp.tshang.com";
		if (BAConstants.IS_TEST) {
			host = "pptest.yidongmengxiang.com";
		}

		String url = "/appbiz/api?cid=" + GoGirlBaseMsg.PacketId.ReqGetSingleShowRoom_cid_VALUE;

		byte[] body = devoteRank(auth, ver, roomid, Selfuid, uid);
		if (body != null) {
			byte[] requests = http_encode_post(url, host, body);
			PeiPeiRequest request = new PeiPeiRequest(requests, this, false, host, 80);
			AppQueueManager.getInstance().addRequest(request);
		}
	}

	private byte[] devoteRank(byte[] auth, int ver, int roomid, int Selfuid, int uid) {
		Gogirl.ReqGetSingleShowRoom.Builder builder = Gogirl.ReqGetSingleShowRoom.newBuilder();
		builder.setRoomid(roomid);
		builder.setSelfuid(Selfuid);
		builder.setHostuid(uid);
		builder.setBasemsg(BaseProtobuf.setBaseMsg(auth, ver, GoGirlBaseMsg.PacketId.valueOf(GoGirlBaseMsg.PacketId.ReqGetSingleShowRoom_cid_VALUE)));
		Gogirl.ReqGetSingleShowRoom msg = builder.build();
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
					Gogirl.RspGetSingleShowRoom rsp = Gogirl.RspGetSingleShowRoom.parseFrom(tempByte);
					int retCode = rsp.getRetcode();
					if (checkRetCode(retCode)) {
						if (callback != null)
							callback.getIGetSingleShowRoomResult(retCode, rsp.getRoominfo());
					}
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void error(int resultCode) {
		if (callback != null)
			callback.getIGetSingleShowRoomResult(resultCode, null);
	}

	public interface IGetSingleShowRoom {
		public void getIGetSingleShowRoomResult(int retCode, ShowRoomInfo roomInfo);
	}
}
