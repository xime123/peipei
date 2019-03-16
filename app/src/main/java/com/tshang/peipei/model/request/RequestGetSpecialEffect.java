package com.tshang.peipei.model.request;

import android.app.Activity;

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
 * @Title: RequestGetShowApi.java 
 *
 * @Description: 特效配置
 *
 * @author jeff 
 *
 * @date 2015-1-15 下午2:52:57 
 *
 * @version V1.0   
 */
public class RequestGetSpecialEffect extends AsnBase implements ISocketMsgCallBack {

	private IGetSpecialEffect callBack;

	public void getSpecialEffect(Activity activity, byte[] auth, int ver, int uid, IGetSpecialEffect callBack) {
		String host = PEIPEI_PRODUCT_HOST;
		if (BAConstants.IS_TEST) {
			host = PEIPEI_TEST_HOST;
		}

		String url = "/appbiz/api?cid=" + GoGirlBaseMsg.PacketId.ReqGetSpecialEffect_cid_VALUE;
		this.callBack = callBack;

		byte[] body = getSpecialEffect(auth, ver, uid);
		if (body != null) {
			byte[] requests = http_encode_post(url, host, body);
			PeiPeiRequest request = new PeiPeiRequest(requests, this, false, host, 80);
			AppQueueManager.getInstance().addRequest(request);
		}
	}

	private byte[] getSpecialEffect(byte[] auth, int ver, int uid) {
		Gogirl.ReqGetSpecialEffect.Builder builder = Gogirl.ReqGetSpecialEffect.newBuilder();
		builder.setUid(uid);
		builder.setBasemsg(BaseProtobuf.setBaseMsg(auth, ver, GoGirlBaseMsg.PacketId.valueOf(GoGirlBaseMsg.PacketId.ReqGetSpecialEffect_cid_VALUE)));

		Gogirl.ReqGetSpecialEffect msg = builder.build();
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
					Gogirl.RspGetSpecialEffect rsp = Gogirl.RspGetSpecialEffect.parseFrom(tempByte);
					int retCode = rsp.getRetcode();
					if (checkRetCode(retCode)) {
						if (callBack != null) {
							callBack.getSpecialEffect(retCode, rsp.getRidingid(), rsp.getSceneid());

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
		if (callBack != null) {
			callBack.getSpecialEffect(resultCode, -1, -1);
		}
	}

	public interface IGetSpecialEffect {
		public void getSpecialEffect(int retCode, int riding, int sceneid);
	}
}
