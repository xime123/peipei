package com.tshang.peipei.base;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.text.TextUtils;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

public class BaseString {

	/**   
	 * 字符串转换成十六进制字符串  
	 * @param String str 待转换的ASCII字符串  
	 * @return String 每个Byte之间空格分隔，如: [61 6C 6B]  
	 */
	public static String str2HexStr(String str) {

		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;

		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
			sb.append(' ');
		}
		return sb.toString().trim();
	}

	/**   
	 * 十六进制转换字符串  
	 * @param String str Byte字符串(Byte之间无分隔符 如:[616C6B])  
	 * @return String 对应的字符串  
	 */
	public static String hexStr2Str(String hexStr) {
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;

		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}

	/*
	 * 16进制数字字符集
	 */
	private static String hexString = "0123456789abcdef";

	/*
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 */
	public static String encode(byte[] bytes) {
		// 根据默认编码获取字节数组
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	/*
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	 */
	public static String decode(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "utf-8");//UTF-16le:Not 
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	// 字符串数组合成一个字符串
	public static String getColomns(String[] temp, String split) {
		String str = null;
		for (int i = 0; i < temp.length; i++) {
			if (i == 0) {
				str = temp[i];
			} else {
				str = str + split + temp[i];
			}
		}
		return str;
	}

	// 匹配数字
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	// 匹配数字或字母
	public static boolean isNumOrChar(String str) {
		Pattern pattern = Pattern.compile("[0-9A-Za-z]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 将 int 类型数据转成二进制的字符串，不足 int 类型位数时在前面添“0”以凑足位数
	 * 
	 * @param num
	 * @return
	 */
	public static String toFullBinaryString(int num) {
		char[] chs = new char[Integer.SIZE];
		for (int i = 0; i < Integer.SIZE; i++) {
			chs[Integer.SIZE - 1 - i] = (char) (((num >> i) & 1) + '0');
		}
		return new String(chs);
	}

	/**
	 * 将 long 类型数据转成二进制的字符串，不足 long 类型位数时在前面添“0”以凑足位数
	 * 
	 * @param num
	 * @return
	 */
	public static String toFullBinaryString(long num) {
		char[] chs = new char[Long.SIZE];
		for (int i = 0; i < Long.SIZE; i++) {
			chs[Long.SIZE - 1 - i] = (char) (((num >> i) & 1) + '0');
		}
		return new String(chs);
	}

	/**
	 * 判断是否为邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	/** 
	 * 验证手机格式 
	 */
	public static boolean isMobileNO(String mobiles) {
		/* 
		移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 
		联通：130、131、132、152、155、156、185、186 
		电信：133、153、180、189、（1349卫通） 
		总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9 
		*/
		String telRegex = "[1]\\d{10}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。  
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	public static byte[] getMD5Str(byte[] bs) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(bs);
		} catch (NoSuchAlgorithmException e) {
		}

		return messageDigest.digest();
	}

	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}

		// 16位加密，从第9位到25位
		return md5StrBuff.toString();

	}

	public static byte[] getMD5Str_8(byte[] bs) {
		MessageDigest messageDigest = null;
		byte[] b = new byte[8];

		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(bs);
			byte[] bb = messageDigest.digest();
			System.arraycopy(bb, 8, b, 0, 8);
		} catch (NoSuchAlgorithmException e) {
		}

		return b;
	}

	public static byte[] getMergeArray(byte[] al, byte[] bl) {
		byte[] a = al;
		byte[] b = bl;
		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}

	public static String getFilePath(Context context, int fuid, int type) {
		String path_pic = "";
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(context);
		if (userEntity != null) {
			if (type == MessageType.BURN_IMAGE.getValue() || type == MessageType.IMAGE.getValue()) {
				path_pic = userEntity.uid.intValue() + File.separator + BAConstants.PEIPEI_Image_FILE + File.separator + fuid;
			} else if (type == MessageType.BURN_VOICE.getValue() || type == MessageType.VOICE.getValue()) {
				path_pic = userEntity.uid.intValue() + File.separator + BAConstants.PEIPEI_AUDIO_FILE + File.separator + fuid;
			}
		}
		File directory_pic = BaseFile.getStoregeDirectory(path_pic, context);
		if (!directory_pic.exists()) {
			directory_pic.mkdirs();
		}
		return directory_pic.getAbsolutePath();
	}

	public static String Encrypt(String strSrc, String encName) {
		MessageDigest md = null;
		String strDes = null;

		byte[] bt = strSrc.getBytes();
		try {
			if (encName == null || encName.equals("")) {
				encName = "SHA-256";
			}
			md = MessageDigest.getInstance(encName);
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return strDes;
	}

	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

	public static String hmac_sha1(String value, String key) {
		try {
			// Get an hmac_sha1 key from the raw key bytes   
			byte[] keyBytes = key.getBytes();
			SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");

			// Get an hmac_sha1 Mac instance and initialize with the signing key   
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(signingKey);

			// Compute the hmac on input data bytes   
			byte[] rawHmac = mac.doFinal(value.getBytes());

			// Convert raw bytes to Hex   
			String hexBytes = byte2hex(rawHmac);
			return hexBytes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String byte2hex(final byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0xFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs;
	}

	public static String toURLEncoded(String paramString) {
		if (paramString == null || paramString.equals("")) {
			return "";
		}

		try {
			String str = new String(paramString.getBytes(), "UTF-8");
			str = URLEncoder.encode(str, "UTF-8");
			return str;
		} catch (Exception localException) {
			localException.printStackTrace();
		}

		return "";
	}
	
	public static String md5(String string) {
	    byte[] hash;
	    try {
	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Huh, MD5 should be supported?", e);
	    } catch (UnsupportedEncodingException e) {
	        throw new RuntimeException("Huh, UTF-8 should be supported?", e);
	    }

	    StringBuilder hex = new StringBuilder(hash.length * 2);
	    for (byte b : hash) {
	        if ((b & 0xFF) < 0x10) hex.append("0");
	        hex.append(Integer.toHexString(b & 0xFF));
	    }
	    return hex.toString();
	}
}
