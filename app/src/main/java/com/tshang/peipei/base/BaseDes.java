package com.tshang.peipei.base;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import android.annotation.SuppressLint;

/**
 * DES Coder<br/>
 * secret key length: 56 bit, default: 56 bit<br/>
 * mode: ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128<br/>
 * padding: Nopadding/PKCS5Padding/ISO10126Padding/
 * 
 * @author
 * 
 */
@SuppressLint("DefaultLocale")
public class BaseDes {

	private static final String KEY_ALGORITHM = "DES";

	private static final String DEFAULT_CIPHER_ALGORITHM = "DES/ECB/Nopadding";

	private static final String DEFAULT_CIPHER_ALGORITHM_2 = "DES/ECB/PKCS5Padding";

	public static byte[] initSecretKey() throws Exception {
		KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		kg.init(56);
		SecretKey secretKey = kg.generateKey();
		return secretKey.getEncoded();
	}

	private static Key toKey(byte[] key) throws Exception {
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		SecretKey secretKey = skf.generateSecret(dks);
		return secretKey;
	}

	public static byte[] encrypt(byte[] data, Key key) throws Exception {
		return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
	}

	public static byte[] encrypt(byte[] data, byte[] key) {
		byte[] b = null;
		try {
			b = encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public static byte[] encrypt_1(byte[] data, byte[] key) {
		byte[] b = null;
		try {
			b = encrypt(data, key, DEFAULT_CIPHER_ALGORITHM_2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public static byte[] encrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
		Key k = toKey(key);
		return encrypt(data, k, cipherAlgorithm);
	}

	public static byte[] encrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(data);
	}

	public static byte[] decrypt(byte[] data, byte[] key) {
		byte[] b = null;
		try {
			b = decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;

	}

	public static byte[] decrypt(byte[] data, Key key) throws Exception {
		return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
	}

	public static byte[] decrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
		Key k = toKey(key);
		return decrypt(data, k, cipherAlgorithm);
	}

	public static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(data);
	}

	@SuppressWarnings("unused")
	private static String showByteArray(byte[] data) {
		if (null == data) {
			return null;
		}
		StringBuilder sb = new StringBuilder("{");
		for (byte b : data) {
			sb.append(b).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		return sb.toString();
	}

	public static String bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}

}