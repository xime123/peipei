package com.tshang.peipei.protocol.asn;


/**
 * @author jaceky
 * 
 *         asn编码解码的封装类
 * 
 */
public final class AsnProtocolTools {

	private final static double[] DOUBLE_POW = new double[4];

	static {
		for (int i = 0; i < 4; i++) {
			DOUBLE_POW[i] = Math.pow(2, 8 * (3 - i));
		}
	}

	/**
	 * 判断asn协议包是否完整
	 * 
	 * return: =0 - 收到部分正确的包，但不完整，要继续收 <0 - 出错，需要关连 >0 - 收到完整包
	 */
	public static int is_pkg_complete(byte[] buffer) {
		final int MAX_LEN = 1024 * 1024 * 128;

		int buffer_lenght = buffer.length;
		if (buffer_lenght <= 0)
			return 0;

		if (buffer[0] != 48) {
			if (new String(buffer).indexOf("HTTP") == 0) {
				return http_net_complete_func(buffer);
			} else
				return -1;
		}

		if (buffer_lenght < 4)
			return 0;

		int head_len = 0, cont_len = 0;

		int unsignedByte = buffer[1] >= 0 ? buffer[1] : buffer[1] & 0xff;

		if (unsignedByte > 128) {
			int len_len = buffer[1] & 127;
			int addr_src = 2;
			head_len = 2 + len_len;

			byte[] addr_dest = new byte[4];
			int addr_offset = 4 - len_len;
			for (int i = addr_offset; i < 4; i++) {
				addr_dest[i] = buffer[addr_src];
				addr_src++;
			}

			for (int i = 0; i < 4; ++i) {
				int unSigle_int = addr_dest[i] >= 0 ? addr_dest[i] : addr_dest[i] & 0xff;
				cont_len += unSigle_int * DOUBLE_POW[i];
			}
		} else {
			head_len = 2;
			cont_len = (int) buffer[1];
		}

		int pkt_len = head_len + cont_len;

		if (MAX_LEN < pkt_len)
			return -1;

		if (buffer_lenght > pkt_len) {
			if (buffer[pkt_len] != 48) {
				return -1;
			}
			return pkt_len;
		} else if (buffer_lenght == pkt_len) {
			return pkt_len;
		} else {
			return 0;
		}

	}

	public static int http_net_complete_func(byte[] data) {

		int data_len = data.length;
		//				System.out.println("data_len=======" + data_len + "--------\n" + new String(data));
		if (data_len < 14)
			return 0;

		String strData = new String(data);
		int pos = strData.indexOf("\r\n\r\n");
		//		System.out.println("pos=====" + pos);
		String contentLength = "Content-Length:";

		if (pos > 0) {
			int contentLenPos = strData.indexOf(contentLength);
			//			System.out.println("contentLenPos==========" + contentLenPos);
			if (contentLenPos < 0) {
				return pos + 4;
			} else {
				int end_content_length = strData.indexOf("\r\n", contentLenPos);
				//				System.out.println("end_content_length==========" + end_content_length);
				String subData = strData.substring(contentLenPos + contentLength.length(), end_content_length).trim();
				//				System.out.println("subData======" + subData);
				int bodyLength;
				try {
					bodyLength = Integer.parseInt(subData);
					//					System.out.println("bodyLength=222222222=======" + bodyLength);
					int pkt_len = pos + 4 + bodyLength;
					if (data_len >= pkt_len) {
						return pkt_len;
					} else {
						return 0;
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					return 0;
				}

			}
		} else if (data_len > 10240000)
			return -1;
		else
			return 0;

	}

	public static int http_net_body_length(byte[] data) {

		int data_len = data.length;
		if (data_len < 14)
			return 0;

		String strData = new String(data);
		int pos = strData.indexOf("\r\n\r\n");
		String contentLength = "Content-Length:";

		if (pos > 0) {
			int contentLenPos = strData.indexOf(contentLength);
			if (contentLenPos < 0) {
				return pos + 4;
			} else {
				int end_content_length = strData.indexOf("\r\n", contentLenPos);
				String subData = strData.substring(contentLenPos + contentLength.length(), end_content_length).trim();
				int bodyLength;
				try {
					bodyLength = Integer.parseInt(subData);
					return bodyLength;
				} catch (NumberFormatException e) {
					e.printStackTrace();

				}

			}
		}
		return 0;

	}

}
