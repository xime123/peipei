package com.tshang.peipei.model.biz.chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseHttpUtils;
import com.tshang.peipei.base.BaseString;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.base.babase.BAConstants;

/**
 * @Title: BaiduCloudUtils.java 
 *
 * @Description: 百度云的一些方法
 *
 * @author allen  
 *
 * @date 2015-6-27 下午3:38:35 
 *
 * @version V1.0   
 */
public class BaiduCloudUtils {

	public static void putFileToCloud(File file) {
		//		path = "/peipei/a.mp4"

		String bucker = "peipei";
		if (BAConstants.IS_TEST) {
			bucker = "peipeitest2";
		}

		if (file != null && file.exists()) {
			try {
				String name = VideoUtils.getMd5ByFile(file);
				String timestamp = BaseTimes.getChatDiffTimeYYMDHMS(System.currentTimeMillis());//"2015-06-23T06:29:22Z";
				String url = "http://bj.bcebos.com/" + bucker + "/" + name;
				String canonicalQueryString = "";
				String canonicalHeaders = "content-type:" + BaseString.toURLEncoded("application/octet-stream") + "\nhost:bj.bcebos.com\nx-bce-date:"
						+ BaseString.toURLEncoded(timestamp);
				String CanonicalRequest = joinN("\n", new String[] { "PUT", "/" + bucker + "/" + name, canonicalQueryString, canonicalHeaders });

				String signKeyInfo = "bce-auth-v1" + "/" + BAConstants.BAIDU_APP_KEY + "/" + timestamp + "/3600";

				String signingKey = BaseString.hmac_sha1(signKeyInfo, BAConstants.secretKey);

				String signature = BaseString.hmac_sha1(CanonicalRequest, signingKey);

				String authorization = joinN("/", new String[] { "bce-auth-v1", BAConstants.BAIDU_APP_KEY, timestamp, String.valueOf(3600),
						"content-type;host;x-bce-date", signature });

				HttpClient client = new DefaultHttpClient();
				HttpPut put = new HttpPut(url);
				//						put.setHeader("Content-md5", "I54VFvoncRoO+dN2kd2yBw==");
				put.setHeader("Content-type", "application/octet-stream");
				put.setHeader("Host", "bj.bcebos.com");
				put.setHeader("x-bce-date", timestamp);
				put.setHeader("Authorization", authorization);

				byte[] b = BaseFile.getBytesByFilePath(file);

				ByteArrayEntity params = new ByteArrayEntity(b);
				put.setEntity(params);

				HttpResponse response = client.execute(put);
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void downloadFileFromCloud(String path, String name) {
		//		String path = "/peipeitest2/ttt.txt";
		String timestamp = BaseTimes.getChatDiffTimeYYMDHMS(System.currentTimeMillis());
		int expiresInSeconds = 3600;
		String CanonicalURI = path;

		String canonicalQueryString = "";
		String canonicalHeaders = "host:bj.bcebos.com\nx-bce-date:" + BaseString.toURLEncoded(timestamp);
		String CanonicalRequest = joinN("\n", new String[] { "GET", CanonicalURI, canonicalQueryString, canonicalHeaders });

		String signKeyInfo = "bce-auth-v1" + "/" + BAConstants.BAIDU_APP_KEY + "/" + timestamp + "/" + expiresInSeconds;

		String signingKey = BaseString.hmac_sha1(signKeyInfo, BAConstants.secretKey);

		String signature = BaseString.hmac_sha1(CanonicalRequest, signingKey);

		String authorization = joinN("/", new String[] { "bce-auth-v1", BAConstants.BAIDU_APP_KEY, timestamp, String.valueOf(expiresInSeconds),
				"host;x-bce-date", signature });

		String httpurl = "http://bj.bcebos.com" + path;
		HttpGet httpRequest = new HttpGet(httpurl);
		httpRequest.setHeader("Host", "bj.bcebos.com");
		httpRequest.setHeader("x-bce-date", timestamp);
		httpRequest.setHeader("Authorization", authorization);

		HttpClient httpclient = new DefaultHttpClient();
		try {
			// 请求httpClient ，取得HttpRestponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得相关信息 取得HttpEntiy
				HttpEntity httpEntity = httpResponse.getEntity();

				// 获得一个输入流
				InputStream is = httpEntity.getContent();

				BaseHttpUtils.writeToSDfromInput(SdCardUtils.getInstance().getVedioDirectory(), name, is);
				is.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean getObjectMetaFromCloud(String path) {
		//		String path = "/peipeitest2/ttt.txt";
		String timestamp = BaseTimes.getChatDiffTimeYYMDHMS(System.currentTimeMillis());
		int expiresInSeconds = 3600;
		String CanonicalURI = path;

		String canonicalQueryString = "";
		String canonicalHeaders = "host:bj.bcebos.com\nx-bce-date:" + BaseString.toURLEncoded(timestamp);
		String CanonicalRequest = joinN("\n", new String[] { "HEAD", CanonicalURI, canonicalQueryString, canonicalHeaders });

		String signKeyInfo = "bce-auth-v1" + "/" + BAConstants.BAIDU_APP_KEY + "/" + timestamp + "/" + expiresInSeconds;

		String signingKey = BaseString.hmac_sha1(signKeyInfo, BAConstants.secretKey);

		String signature = BaseString.hmac_sha1(CanonicalRequest, signingKey);

		String authorization = joinN("/", new String[] { "bce-auth-v1", BAConstants.BAIDU_APP_KEY, timestamp, String.valueOf(expiresInSeconds),
				"host;x-bce-date", signature });

		String httpurl = "http://bj.bcebos.com" + path;
		HttpHead httpRequest = new HttpHead(httpurl);
		httpRequest.setHeader("Host", "bj.bcebos.com");
		httpRequest.setHeader("x-bce-date", timestamp);
		httpRequest.setHeader("Authorization", authorization);

		HttpClient httpclient = new DefaultHttpClient();
		try {
			// 请求httpClient ，取得HttpRestponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	private static String joinN(String split, String[] args) {
		String ret = args[0];
		for (int i = 1; i < args.length; i++)
			ret = ret + split + args[i];
		return ret;
	}
}
