package com.tshang.peipei.base;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import android.os.Environment;

/**
 * @Title: BaseFileLog.java 
 *
 * @Description: 本地日志文件 
 *
 * @author allen  
 *
 * @date 2014-8-5 上午9:57:44 
 *
 * @version V1.0   
 */
public class BaseFileLog {

	public static void saveLogByFile(String message, String fileName) {
		Scanner sc = null;
		PrintWriter pw = null;
		try {
			File files = new File(Environment.getExternalStorageDirectory() + "/peipei");
			if (!files.exists()) {
				files.mkdirs();
			}
			File f = new File(files, fileName);

			if (!f.exists()) {//如果文件不存在,则新建.  
				f.createNewFile();
			}

			sc = new Scanner(f);
			StringBuilder sb = new StringBuilder();
			while (sc.hasNextLine())//先读出旧文件内容,并暂存sb中;  
			{
				sb.append(sc.nextLine());
				sb.append("\r\n");
			}
			sc.close();

			pw = new PrintWriter(new FileWriter(f), true);

			pw.println(sb.toString());//,写入旧文件内容.  
			pw.println(message + "  [" + getCurrentDate() + "]");//写入新日志.  
			pw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getCurrentDate() {
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sm.format(new Date());
	}

	public static String getTraceInfo() {
		StringBuffer sb = new StringBuffer();

		StackTraceElement[] stacks = new Throwable().getStackTrace();
		int stacksLen = stacks.length;
		sb.append("class: ").append(stacks[1].getClassName()).append("; method: ").append(stacks[1].getMethodName()).append("; number: ")
				.append(stacks[1].getLineNumber());

		return sb.toString();
	}
}
