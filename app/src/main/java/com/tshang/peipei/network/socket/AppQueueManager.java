package com.tshang.peipei.network.socket;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 *类        名 : AppQueueManager.java
 *功能描述 : 消息管理类
 *作　    者 : vactor
 *设计日期 :2014-3-19 下午6:16:19
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class AppQueueManager {

	// 单例对象
	private static AppQueueManager manager;

	// 线程池线程开关
	private boolean workSwitch = true;

	// 先进先出的消息队列
	Queue<PeiPeiRequest> sendQueue = new LinkedList<PeiPeiRequest>();

	// 创建一个线程数最大值为4的线程池对象
	ExecutorService pool = Executors.newFixedThreadPool(4);

	//	static int mTaskid = 0;

	/**
	 * 增加一个任务到消息队列
	 */
	public void addRequest(PeiPeiRequest task) {
		// int cur_task_id = mTaskid++;
		synchronized (sendQueue) {
			sendQueue.offer(task);
			sendQueue.notify();
		}
	}

	private AppQueueManager() {}

	public static AppQueueManager getInstance() {
		if (null == manager) {
			synchronized (AppQueueManager.class) {
				if (null == manager)
					manager = new AppQueueManager();
			}
		}
		return manager;
	}

	/*
	 * 初始化4个线程
	 */
	public void initWork() {
		QueueThread thread1 = new QueueThread(sendQueue, workSwitch);
		QueueThread thread2 = new QueueThread(sendQueue, workSwitch);
		QueueThread thread3 = new QueueThread(sendQueue, workSwitch);
		QueueThread thread4 = new QueueThread(sendQueue, workSwitch);
		pool.execute(thread1);
		pool.execute(thread2);
		pool.execute(thread3);
		pool.execute(thread4);

	}

	/**
	 * 关闭线程池,程序退出!
	 */
	public void shutdownWork() {
		int count = 0;
		while (count < 5 && sendQueue.size() > 0) {
			try {
				Thread.sleep(1000);
				count++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		workSwitch = false;

		synchronized (sendQueue) {
			sendQueue.notifyAll();
		}
		if (null != pool) {
			pool.shutdown();
		}
		sendQueue.clear();
	}

	/**
	 * 关闭当前ExecutorService
	 * 
	 * @author liangxz
	 * @param timeout
	 *            void
	 * @date 2013-8-23
	 */
	public void shutdown(long timeout) {
		if (pool != null && !pool.isShutdown()) {
			try {
				pool.awaitTermination(timeout, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			workSwitch = false;
			pool.shutdown();
		}
	}

}
