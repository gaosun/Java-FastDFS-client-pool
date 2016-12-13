package com.gaosun.fileManagerment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gaosun.fileManagerment.imp.FastDFSFileOperation;
import com.gaosun.fileManagerment.interfaces.IFileOperation;
import com.gaosun.filesystempool.ConnectionPoolManager;
import com.gaosun.filesystempool.FastDFSConnectionPool;

/**
 * 
 * @author sungao
 *
 */
public class FileManage {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FileManage.class);

	private static String defaultDevice = "FDFS";

	// 如果后续有其他类型文件系统，可把使用接口
	private static FastDFSConnectionPool fastDFSConnectionPool;

	private static IFileOperation fastDFSFileOperation;

	public static void initFastDFSPool() {
		fastDFSConnectionPool = ConnectionPoolManager.initFastDFSPool();
	}

	/**
	 * 获取文件上传类
	 * 
	 * @return FileOperation | null
	 */
	public static IFileOperation createtDefaultDevice() {
		if (null == fastDFSConnectionPool) {
			fastDFSConnectionPool = ConnectionPoolManager.initFastDFSPool();
			LOGGER.info("加载fastDFSConnectionPool：" + fastDFSConnectionPool);
		}
		switch (defaultDevice) {
		case "FDFS":
			if (null == fastDFSFileOperation) {
				fastDFSFileOperation = new FastDFSFileOperation(
						fastDFSConnectionPool);
				LOGGER.info("加载fastDFSFileOperation：" + fastDFSFileOperation);
			}
			break;
		case "FTP":

			// 未完待续
			break;
		default:
			break;
		}
		return fastDFSFileOperation;
	}

	public static String getDefaultDevice() {
		return defaultDevice;
	}

	public static void setDefaultDevice(String defaultDevice) {
		FileManage.defaultDevice = defaultDevice;
	}

}
