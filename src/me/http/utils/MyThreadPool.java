package me.http.utils;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import me.app.global.Constant;
import android.util.Log;

public class MyThreadPool {

	private static MyThreadPool mThreadPool = null;
	private static final boolean D = Constant.DEBUG;
	private static final String TAG = "MyThreadPool";
	private static final int THREAD_NUM = 2;
	private ExecutorService executorService = null;

	protected MyThreadPool() {
		executorService = Executors.newFixedThreadPool(THREAD_NUM);
	}

	public static MyThreadPool getInstance() {
		if (mThreadPool == null) {
			mThreadPool = new MyThreadPool();
		}
		return mThreadPool;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void mHandleGetTask(String url, Map<String, String> mapParams) {
		executorService.submit(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

			}

		});
	}

	/**
	 * 描述：立即关闭.
	 */
	public void shutdownNow() {
		if (!executorService.isTerminated()) {
			executorService.shutdownNow();
			listenShutdown();
		}

	}

	/**
	 * 描述：平滑关闭.
	 */
	public void shutdown() {
		if (!executorService.isTerminated()) {
			executorService.shutdown();
			listenShutdown();
		}
	}

	/**
	 * 描述：关闭监听.
	 */
	public void listenShutdown() {
		try {
			while (!executorService.awaitTermination(1, TimeUnit.MILLISECONDS)) {
				if (D)
					Log.d(TAG, "线程池未关闭");
			}
			if (D)
				Log.d(TAG, "线程池已关闭");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
