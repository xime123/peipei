package com.tshang.peipei.model.request;

import java.util.List;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.BaseProtobuf;
import com.tshang.peipei.protocol.Gogirl;
import com.tshang.peipei.protocol.Gogirl.GoGirlBaseMsg;
import com.tshang.peipei.protocol.Gogirl.GoGirlChatDataP;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.AsnProtocolTools;
import com.tshang.peipei.protocol.protobuf.InvalidProtocolBufferException;

/**
 * @Title: RequestGetShowHistoryData.java 
 *
 * @Description: 获取秀场聊天记录 
 *
 * @author allen  
 *
 * @date 2015-1-26 下午3:26:46 
 *
 * @version V1.0   
 */
public class RequestGetShowHistoryData extends AsnBase implements ISocketMsgCallBack {

	private int type;
	private IGetShowHistoryData callback;

	public void getShowHistoryData(byte[] auth, int ver, int roomid, int hostuid, int uid, int start, int num, int type, IGetShowHistoryData callback) {
		this.callback = callback;
		this.type = type;
		String host = "ppapp.tshang.com";
		if (BAConstants.IS_TEST) {
			host = "pptest.yidongmengxiang.com";
		}

		String url = "/appbiz/api?cid=" + GoGirlBaseMsg.PacketId.ReqGetShowHistoryData_cid_VALUE;

		byte[] body = showHistoryData(auth, ver, roomid, hostuid, uid, start, num, type);
		if (body != null) {
			byte[] requests = http_encode_post(url, host, body);
			PeiPeiRequest request = new PeiPeiRequest(requests, this, false, host, 80);
			AppQueueManager.getInstance().addRequest(request);
		}
	}

	private byte[] showHistoryData(byte[] auth, int ver, int roomid, int hostuid, int uid, int start, int num, int type) {
		Gogirl.ReqGetShowHistoryData.Builder builder = Gogirl.ReqGetShowHistoryData.newBuilder();
		builder.setRoomid(roomid);
		builder.setSelfuid(uid);
		builder.setHostuid(hostuid);
		builder.setNum(num);
		builder.setStart(start);
		builder.setType(type);
		builder.setBasemsg(BaseProtobuf.setBaseMsg(auth, ver, GoGirlBaseMsg.PacketId.valueOf(GoGirlBaseMsg.PacketId.ReqGetShowHistoryData_cid_VALUE)));
		Gogirl.ReqGetShowHistoryData msg = builder.build();
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
					Gogirl.RspGetShowHistoryData rsp = Gogirl.RspGetShowHistoryData.parseFrom(tempByte);
					int retCode = rsp.getRetcode();
					if (checkRetCode(retCode)) {
						if (callback != null)
							callback.resultShowHistoryData(retCode, rsp.getIsend(), type, rsp.getChatdatalistList());
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
			callback.resultShowHistoryData(resultCode, 0, type, null);
	}

	public interface IGetShowHistoryData {
		public void resultShowHistoryData(int retCode, int end, int type, List<GoGirlChatDataP> list);
	}
}
