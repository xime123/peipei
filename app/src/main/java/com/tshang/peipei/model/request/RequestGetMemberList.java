package com.tshang.peipei.model.request;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.BaseProtobuf;
import com.tshang.peipei.protocol.Gogirl;
import com.tshang.peipei.protocol.Gogirl.GoGirlBaseMsg;
import com.tshang.peipei.protocol.Gogirl.RspGetRoomMemberList;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.AsnProtocolTools;
import com.tshang.peipei.protocol.protobuf.InvalidProtocolBufferException;

/**
 * @Title: RequestGetMemberList.java 
 *
 * @Description: 获取成员列表
 *
 * @author allen  
 *
 * @date 2015-1-22 下午3:44:11 
 *
 * @version V1.0   
 */
public class RequestGetMemberList extends AsnBase implements ISocketMsgCallBack {

	private IGetMemberList callback;

	public void getMemberList(byte[] auth, int ver, int roomid, int Selfuid, int uid, int start, int num, IGetMemberList callback) {
		this.callback = callback;
		String host = "ppapp.tshang.com";
		if (BAConstants.IS_TEST) {
			host = "pptest.yidongmengxiang.com";
		}

		String url = "/appbiz/api?cid=" + GoGirlBaseMsg.PacketId.ReqGetRoomMemberList_cid_VALUE;

		byte[] body = getRoomMemberlist(auth, ver, roomid, Selfuid, uid, start, num);
		if (body != null) {
			byte[] requests = http_encode_post(url, host, body);
			PeiPeiRequest request = new PeiPeiRequest(requests, this, false, host, 80);
			AppQueueManager.getInstance().addRequest(request);
		}
	}

	private byte[] getRoomMemberlist(byte[] auth, int ver, int roomid, int Selfuid, int uid, int start, int num) {
		Gogirl.ReqGetRoomMemberList.Builder builder = Gogirl.ReqGetRoomMemberList.newBuilder();
		builder.setRoomid(roomid);
		builder.setSelfuid(Selfuid);
		builder.setNum(num);
		builder.setHostuid(uid);
		builder.setStart(start);
		builder.setBasemsg(BaseProtobuf.setBaseMsg(auth, ver, GoGirlBaseMsg.PacketId.valueOf(GoGirlBaseMsg.PacketId.ReqGetRoomMemberList_cid_VALUE)));
		Gogirl.ReqGetRoomMemberList msg = builder.build();
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
					Gogirl.RspGetRoomMemberList rsp = Gogirl.RspGetRoomMemberList.parseFrom(tempByte);
					int retCode = rsp.getRetcode();
					if (checkRetCode(retCode)) {
						if (callback != null)
							callback.getMemberResult(retCode, rsp);
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
			callback.getMemberResult(resultCode, null);
	}

	public interface IGetMemberList {
		public void getMemberResult(int retCode, RspGetRoomMemberList list);
	}
}
