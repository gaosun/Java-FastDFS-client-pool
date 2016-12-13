package com.gaosun.filesystempool;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FastDFSConnectionPool {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FastDFSConnectionPool.class);

	/** 空闲的连接池 */
	private static LinkedBlockingQueue<TrackerServer> idleConnectionPool = new LinkedBlockingQueue<TrackerServer>();

	/** 连接池默认最小连接数 */
	private long minPoolSize = 10;

	/** 默认等待时间（单位：秒） */
	private long waitTimes = 10;

	public FastDFSConnectionPool() {
		poolInit();
		HeartBeat beat = new HeartBeat(this);
		beat.beat();
	}

	private void poolInit() {
		try {
			initClientGlobal();
			for (int i = 0; i < minPoolSize; i++) {
				createTrackerServer();
			}
			LOGGER.info("[FASTDFS初始化]] " + idleConnectionPool.size());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("[FASTDFS初始化--异常]{}]", e);
		}
	}

	private void initClientGlobal() throws Exception {
		URL url = FastDFSConnectionPool.class.getResource("/fdfs_client.conf");
		try {
			String configPath = "./fdfs_client.conf";
			if (url != null && !url.getPath().isEmpty())
				configPath = url.getPath();
			ClientGlobal.init(configPath);
		} catch (IOException | MyException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 创建TrackerServer,并放入空闲连接池
	 * 
	 */
	private void createTrackerServer() {
		try {
			TrackerClient trackerClient = new TrackerClient();
			TrackerServer trackerServer = trackerClient.getConnection();
			org.csource.fastdfs.ProtoCommon.activeTest(trackerServer
					.getSocket());
			idleConnectionPool.add(trackerServer);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("[创建TrackerServer(createTrackerServer)][异常：{}]", e);
		}
	}

	/**
	 * 
	 * 获取空闲连接
	 * 
	 * @throws InterruptedException
	 * 
	 */
	public TrackerServer achieve() throws InterruptedException {

		TrackerServer trackerServer = idleConnectionPool.poll(waitTimes,
				TimeUnit.SECONDS);
		if (trackerServer == null) {
			LOGGER.info("[trackerServer is NULL] ");
			createTrackerServer();
			trackerServer = idleConnectionPool.poll(waitTimes, TimeUnit.SECONDS);
		}
		LOGGER.info("[获取空闲连接成功]");
		return trackerServer;

	}

	/**
	 * 释放繁忙连接 
	 * 
	 * @param trackerServer
	 */

	public void release(TrackerServer trackerServer) {
		
		LOGGER.info("[释放当前连接[prams:" + trackerServer + "] ");
		if (trackerServer != null) {
			idleConnectionPool.add(trackerServer);
		}
	}

	/**
	 * 
	 * 删除不可用的连接
	 * 
	 * @param trackerServer
	 * 
	 */
	public synchronized void drop(TrackerServer trackerServer) {
		LOGGER.info("[删除不可用连接方法(drop)][parms:" + trackerServer + "] ");
		if (trackerServer != null) {
			try {
				trackerServer.close();
			} catch (IOException e) {
				LOGGER.info("[删除不可用连接方法(drop)--关闭trackerServer异常][异常：{}]", e);
			}
		}
	}

	public LinkedBlockingQueue<TrackerServer> getIdleConnectionPool() {
		return idleConnectionPool;
	}

	public void setMinPoolSize(long minPoolSize) {
		if (minPoolSize != 0) {
			this.minPoolSize = minPoolSize;
		}
	}

	public void setWaitTimes(int waitTimes) {
		if (waitTimes != 0) {
			this.waitTimes = waitTimes;
		}
	}
}
