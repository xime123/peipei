package com.tshang.peipei.base.json;

/**
 * 后台返回的json数据
 * 
 * @author Jeff
 * 
 * @param <T>
 */
public class JsonData<T> {
	private boolean success;// 返回的状态，true or false
	private String resultInfo;// ok（调用成功，获取数据成功） 其他类容 为操作提示或错误提示
	private T[] t;// 数据，循环数据

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(String resultInfo) {
		this.resultInfo = resultInfo;
	}

	public T[] getT() {
		return t;
	}

	public void setT(T[] t) {
		this.t = t;
	}

}
