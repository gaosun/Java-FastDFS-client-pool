package com.gaosun.filesystempool;



/**
 * 
 * @author sungao
 */
public class ConnectionPoolManager {

	private static FastDFSConnectionPool fastDFSConnectionPool;
	
	
	//private static TFPConnetctionPool pool;
	
	//private static LocalConnetctionPool pool;

	public static FastDFSConnectionPool initFastDFSPool() {
		fastDFSConnectionPool = new FastDFSConnectionPool();
		return fastDFSConnectionPool;
	}
}
