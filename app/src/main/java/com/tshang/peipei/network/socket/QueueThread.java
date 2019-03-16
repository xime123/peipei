package com.tshang.peipei.network.socket;

import java.util.Queue;

/*
 *类        名 : QueueThread.java
 *功能描述 : 工作线程
 *作　    者 : vactor
 *设计日期 :2014-3-19 下午6:17:58
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class QueueThread extends Thread {

	private Queue<PeiPeiRequest> queue;
	private boolean workSwitch;

	public QueueThread(Queue<PeiPeiRequest> queue, boolean workSwitch) {
		this.queue = queue;
		this.workSwitch = workSwitch;
	}

	@Override
	public void run() {
		while (workSwitch) {
			PeiPeiRequest task = null;
			synchronized (queue) {
				try {
					if (queue.size() > 0) {
						task = queue.poll();
					} else {
						queue.wait();
						if (queue.size() > 0)
							task = queue.poll();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}catch(Exception e2){
					e2.printStackTrace();
				}
			}
			if (null != task)
				task.excuteTask();
		}
	}
}
