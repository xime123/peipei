package com.tshang.peipei.model.redpacket2;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.tshang.peipei.R;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.NumericUtils;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.model.ReceiverChatData;
import com.tshang.peipei.model.broadcast.BroadCastUtils;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastRedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;

/**
 * @Title: RedDecodeUtil.java 
 *
 * @Description: 大厅红包解码工具 
 *
 * @author DYH  
 *
 * @date 2016-1-16 下午12:05:32 
 *
 * @version V1.0   
 */
public class RedDecodeUtil {
	
	public static RedPacketBetCreateInfo decodeGetHallRedpacketRedpacket(BroadcastInfo grapbroadcastInfo){
		RedPacketBetCreateInfo solitaireinfo = null;
		if (grapbroadcastInfo != null) {
			byte[] datalist = grapbroadcastInfo.datalist;
			GoGirlDataInfo datainfo = null;
			if (datalist != null && datalist.length > 0) {
				datainfo = BroadCastUtils.getGoGirlDataInfo(datalist);
				if (datainfo != null) {
					if (datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_BET.getValue()
							|| datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_BET_TIMEOUT.getValue()
							|| datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_BET_UNPACK.getValue()) {
						solitaireinfo = ReceiverChatData.getSolitaireRedpacketInfo(datainfo);
					}
				}
			}
		}
		
		return solitaireinfo;
	}
	
	/**
	 * 解码大厅的抢红包
	 * @author DYH
	 *
	 * @param grapbroadcastInfo
	 * @return
	 */
	public static BroadcastRedPacketInfo decodeGetHallRedpacket(BroadcastInfo grapbroadcastInfo){
		BroadcastRedPacketInfo halRedPacketInfo = null;
		if (grapbroadcastInfo != null) {
			byte[] datalist = grapbroadcastInfo.datalist;
			GoGirlDataInfo datainfo = null;
			if (datalist != null && datalist.length > 0) {
				datainfo = BroadCastUtils.getGoGirlDataInfo(datalist);
				if (datainfo != null) {
					if (datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET.getValue()
							|| datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_TIMEOUT.getValue()
							|| datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_UNPACK.getValue()
							|| datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_DONE.getValue()
							|| datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_GET_BROADCAST_RED_PACKET_YET.getValue()
							|| datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_TIMEOUT.getValue()) {
						halRedPacketInfo = ReceiverChatData.getHallRedpacketInfo(datainfo);
					}
				}
			}
		}
		
		return halRedPacketInfo;
	}
	
	public static SolitaireRedpacket parseSolitaireRedpacketXml(String message){
		SolitaireRedpacket solitaireRedpacket = null;
		try {
			ByteArrayInputStream xml = new ByteArrayInputStream(message.getBytes());
			XmlPullParser parser = Xml.newPullParser();

			parser.setInput(xml, "UTF-8");
			int event = parser.getEventType();
			while(event != XmlPullParser.END_DOCUMENT){
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					solitaireRedpacket = new SolitaireRedpacket();
					break;
				case XmlPullParser.START_TAG:
					if("solitaireRedpacket".equals(parser.getName())){
						solitaireRedpacket.setRedpacketId(NumericUtils.parseInt(parser.getAttributeValue(0), 0));
						solitaireRedpacket.setGold(NumericUtils.parseLong(parser.getAttributeValue(1), 0));
						solitaireRedpacket.setSilver(NumericUtils.parseLong(parser.getAttributeValue(2), 0));
						solitaireRedpacket.setJionPerson(NumericUtils.parseInt(parser.getAttributeValue(3), 0));
					}
					break;
				}
				event = parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return solitaireRedpacket;
	}
	
	public static HallRedpacket parseRedpacketXml(String message){
		HallRedpacket redpacket = null;
		try {
			ByteArrayInputStream xml = new ByteArrayInputStream(message.getBytes());
			XmlPullParser parser = Xml.newPullParser();
			
			parser.setInput(xml, "UTF-8");
			int event = parser.getEventType();
			while(event != XmlPullParser.END_DOCUMENT){
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					redpacket = new HallRedpacket();
					break;
				case XmlPullParser.START_TAG:
					if("redpacket".equals(parser.getName())){
						redpacket.setRedpacketId(NumericUtils.parseInt(parser.getAttributeValue(0), 0));
						redpacket.setRedpacketType(NumericUtils.parseInt(parser.getAttributeValue(1), 0));
						redpacket.setUserType(NumericUtils.parseInt(parser.getAttributeValue(2), 0));
						redpacket.setDesc(parser.getAttributeValue(3));
					}
					break;
				}
				event = parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return redpacket;
	}
}
