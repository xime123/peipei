package com.tshang.peipei.network.socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 * 
 * @author Jeff
 * @date2014-10-15 上午10:20:02
 */
public class ThreadPoolService {
	/**
	 * 默认线程池大小
	 */
	public static final int DEFAULT_POOL_SIZE = 2;

	/**
	 * 默认一个任务的超时时间，单位为毫秒
	 */
	public static final long DEFAULT_TASK_TIMEOUT = 10000;

	/**
	 * 池大小
	 */
	private int poolSize = DEFAULT_POOL_SIZE;

	/************ Singleton ***********/

	private static ThreadPoolService instance = new ThreadPoolService(DEFAULT_POOL_SIZE);

	private ThreadPoolService(int poolSize) {
		setPoolSize(poolSize);
	}

	public static ThreadPoolService getInstance() {
		return instance;
	}

	/************ Singleton ***********/

	private ExecutorService executorService;

	/**
	 * 使用线程池中的线程来执行任务
	 * 
	 * @author liangxz
	 * @param task
	 *            void
	 * @date 2013-8-23
	 */
	public void execute(Runnable task) {
		executorService.execute(task);
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
		if (executorService != null && !executorService.isShutdown()) {
			try {
				executorService.awaitTermination(timeout, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			executorService.shutdown();
		}
	}

	/**
	 * 关闭当前ExecutorService，随后根据poolSize创建新的ExecutorService
	 * 
	 */
	public void createExecutorService() {
		shutdown(1000);
		executorService = Executors.newFixedThreadPool(poolSize);
	}

	/**
	 * 调整线程池大小
	 * 
	 * @author liangxz
	 * @param poolSize
	 *            void
	 * @date 2013-8-23
	 */
	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
		createExecutorService();
	}
}
