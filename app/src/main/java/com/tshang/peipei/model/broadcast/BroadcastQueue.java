package com.tshang.peipei.model.broadcast;

import java.util.Queue;

/**
 * @Title: BroadcaseQueue.java 
 *
 * @Description: 广播动画队列
 *
 * @author allen  
 *
 * @date 2014-9-18 下午4:27:00 
 *
 * @version V1.0   
 */
public class BroadcastQueue extends Thread {
	private Queue<BroadcastMessage> queue;
	private boolean workSwitch = true;
	private boolean synchWork = false;

	public boolean isSynchWork() {
		return synchWork;
	}

	public void setSynchWork(boolean synchWork) {
		this.synchWork = synchWork;
	}

	public boolean isWorkSwitch() {
		return workSwitch;
	}

	public void setWorkSwitch(boolean workSwitch) {
		this.workSwitch = workSwitch;
	}

	public BroadcastQueue(Queue<BroadcastMessage> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		while (workSwitch) {
			BroadcastMessage task = null;
			try {
				if (synchWork) {
					sleep(300);
					continue;
				}
				if (queue.size() > 0) {
					task = queue.poll();
				} else {
					sleep(300);
				}

				if (null != task) {
					synchWork = true;
					task.sentMessage();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
