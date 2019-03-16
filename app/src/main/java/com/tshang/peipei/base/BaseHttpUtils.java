package com.tshang.peipei.base;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;

/**
 * @Title: BaseHttpUtils.java 
 *
 * @Description: http协议
 *
 * @author allen  
 *
 * @date 2014-12-6 下午2:30:00 
 *
 * @version V1.0   
 */
public class BaseHttpUtils {

	/* 上传文件至Server，uploadUrl：接收文件的处理页面 */
	public static void uploadFile(String uploadUrl, String srcPath, String act, int uid, BAHandler mHandler) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("act", act));
			params.add(new BasicNameValuePair("uid", uid + ""));

			//对参数编码  
			String param = URLEncodedUtils.format(params, "UTF-8");

			URL url = new URL(uploadUrl + "?" + param);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			// 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃  
			// 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。  
			//			httpURLConnection.setChunkedStreamingMode(400 * 1024);// 128K  
			// 允许输入输出流  
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			// 使用POST方法  
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\"" + srcPath.substring(srcPath.lastIndexOf("/") + 1)
					+ "\"" + end);
			dos.writeBytes(end);

			FileInputStream fis = new FileInputStream(srcPath);
			byte[] buffer = new WeakReference<byte[]>(new byte[8196]).get(); // 8k  
			int count = 0;
			// 读取文件  
			while ((count = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, count);
			}
			fis.close();

			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			InputStream is = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String result = br.readLine();

			Message msg = mHandler.obtainMessage();
			msg.what = HandlerValue.RESULT_UPDATE_BY_WEBVIEW;
			msg.obj = result;
			mHandler.sendMessage(msg);

			dos.close();
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getContent(String strUrl) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setConnectTimeout(10000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("accept", "*/*");
			conn.connect();
			InputStream stream = conn.getInputStream();
			byte[] data = new WeakReference<byte[]>(new byte[2048]).get();
			int length = stream.read(data);
			String str = new String(data, 0, length);
			conn.disconnect();
			stream.close();
			return str;
		} catch (Exception ee) {
			ee.printStackTrace();
			return "";
		}
	}

	public static String downLoadFile(Context context, String urlStr, String filename) {
		try {
			HttpGet httpRequest = new HttpGet(urlStr);

			// 取得HttpClient 对象
			HttpClient httpclient = new DefaultHttpClient();
			// 请求httpClient ，取得HttpRestponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得相关信息 取得HttpEntiy
				HttpEntity httpEntity = httpResponse.getEntity();
				// 获得一个输入流
				InputStream is = httpEntity.getContent();

				File f = writeToSDfromInput(SdCardUtils.getInstance().getDirectory(0), filename, is);
				is.close();
				return f.getAbsolutePath();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public static File writeToSDfromInput(String path, String fileName, InputStream inputStream) {
		File file = new File(path, fileName);
		OutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file);
			byte[] buffer = new byte[1024];

			int length;
			while ((length = (inputStream.read(buffer))) > 0) {
				outStream.write(buffer, 0, length);
			}
			outStream.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public String download(String urlStr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public int downFile(String urlStr, String path, String fileName) {
		InputStream inputStream = null;
		try {

			inputStream = getInputStreamFromURL(urlStr);
			File resultFile = writeToSDfromInput(path, fileName, inputStream);
			if (resultFile == null) {
				return -1;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public InputStream getInputStreamFromURL(String urlStr) {
		HttpURLConnection urlConn = null;
		InputStream inputStream = null;
		try {
			URL url = new URL(urlStr);
			urlConn = (HttpURLConnection) url.openConnection();
			inputStream = urlConn.getInputStream();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return inputStream;
	}
}
