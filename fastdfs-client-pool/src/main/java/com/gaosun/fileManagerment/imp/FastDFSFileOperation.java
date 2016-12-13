package com.gaosun.fileManagerment.imp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerServer;
import org.csource.fastdfs.UploadStream;

import com.gaosun.fileManagerment.interfaces.IFileOperation;
import com.gaosun.filesystempool.FastDFSConnectionPool;

/**
 * 
 * @author sungao
 */
public class FastDFSFileOperation implements IFileOperation {

	private FastDFSConnectionPool fastDFSConnectionPool;

	public FastDFSFileOperation(FastDFSConnectionPool fastDFSConnectionPool) {
		this.fastDFSConnectionPool = fastDFSConnectionPool;
	}

	public String upload_file(String local_file) {
		TrackerServer trackerServer = this.getTrackerServer();
		StorageClient storageClient = new StorageClient(trackerServer, null);
		String fso = null;
		try {
			String[] str = storageClient.upload_file(local_file, null,
					new NameValuePair[] {});
			fso = str[0] + File.separator + str[1];
			this.release(trackerServer);
		} catch (Exception e) {
			this.drop(trackerServer);
			e.printStackTrace();
		}
		return fso;
	}

	public String upload_file(byte[] file_buff, String file_ext_name) {
		TrackerServer trackerServer = this.getTrackerServer();
		StorageClient storageClient = new StorageClient(trackerServer, null);
		String fso = null;
		try {
			String[] str = storageClient.upload_file(file_buff, 0,
					file_buff.length, file_ext_name, new NameValuePair[] {});
			fso = str[0] + File.separator + str[1];
			this.release(trackerServer);
		} catch (Exception e) {
			this.drop(trackerServer);
			e.printStackTrace();
		}
		return fso;
	}

	public String upload_fileByStream(InputStream inStream,
			String file_ext_name, long fileLength) {
		String fso = null;
		String[] results = null;
		TrackerServer trackerServer = this.getTrackerServer();
		StorageClient storageClient = new StorageClient(trackerServer, null);
		try {
			results = storageClient.upload_file(null, fileLength,
					new UploadStream(inStream, fileLength), file_ext_name,
					new NameValuePair[] {});
			fso = results[0] + File.separator + results[1];
			this.release(trackerServer);
		} catch (Exception e) {
			fso = null;
			this.drop(trackerServer);
			e.printStackTrace();
		}
		return fso;
	};

	public Boolean delete_file(String identifier, String expand_str) {
		TrackerServer trackerServer = this.getTrackerServer();
		StorageClient storageClient = new StorageClient(trackerServer, null);
		String group_name = identifier.substring(0, identifier.indexOf("/"));
		String remote_filename = identifier
				.substring(identifier.indexOf("/") + 1);
		int i = 1;
		try {
			i = storageClient.delete_file(group_name, remote_filename);
			this.release(trackerServer);
		} catch (IOException | MyException e) {
			this.drop(trackerServer);
			e.printStackTrace();
		}
		return i == 0 ? true : false;
	}

	public byte[] download_file(String identifier, long file_offset,
			long download_bytes, String expand_str) {
		TrackerServer trackerServer = this.getTrackerServer();
		StorageClient storageClient = new StorageClient(trackerServer, null);
		String group_name = identifier.substring(0, identifier.indexOf("/"));
		String remote_filename = identifier
				.substring(identifier.indexOf("/") + 1);
		byte[] b = null;
		try {
			b = storageClient.download_file(group_name, remote_filename,
					file_offset, download_bytes);
			this.release(trackerServer);
		} catch (IOException | MyException e) {
			this.drop(trackerServer);
			e.printStackTrace();
		}
		return b;
	}

	public Boolean download_file(String identifier, String target_url,
			String expand_str) {
		TrackerServer trackerServer = this.getTrackerServer();
		StorageClient storageClient = new StorageClient(trackerServer, null);
		String group_name = identifier.substring(0, identifier.indexOf("/"));
		String remote_filename = identifier
				.substring(identifier.indexOf("/") + 1);
		int i = 1;
		try {
			i = storageClient.download_file(group_name, remote_filename,
					target_url);
			this.release(trackerServer);
		} catch (IOException | MyException e) {
			this.drop(trackerServer);
			e.printStackTrace();
		}
		return i == 0 ? true : false;
	}

	private TrackerServer getTrackerServer() {
		try {
			return fastDFSConnectionPool.achieve();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void release(TrackerServer trackerServer) {
		try {
			fastDFSConnectionPool.release(trackerServer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void drop(TrackerServer trackerServer) {
		try {
			fastDFSConnectionPool.drop(trackerServer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
