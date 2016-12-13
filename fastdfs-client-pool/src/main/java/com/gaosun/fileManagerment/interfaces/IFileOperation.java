package com.gaosun.fileManagerment.interfaces;

import java.io.InputStream;

/**
 * 
 * @author sungao
 *
 */
public interface IFileOperation {
	
	/**
	 * 上传文件地址
	 * @param local_file
	 * @return
	 */
	public abstract String upload_file(String local_file);
	/**
	 * 
	 * @param file_buff 文件字节流
	 * @param offset	偏移量
	 * @param length	字节长度
	 * @param file_ext_name 文件后缀名
	 * @return
	 */
	public abstract String upload_file(byte[] file_buff, String file_ext_name);
	
	
	public abstract String upload_fileByStream(InputStream inStream, String file_ext_name, long fileLength);
	/**
	 * 删除
	 * @param identifier 文件标识
	 * @param expand_str 扩展字段
	 * @return
	 */
	public abstract Boolean delete_file(String identifier,String expand_str);
	
	
	
	/**
	 * 下载
	 * @param identifier 文件标识
	 * @param target_url 下载模板路径
	 * @param expand_str 扩展字段
	 * @param file_offset
	 * @param download_bytes
	 * file content/buff, return null if fail
	 */
	public abstract byte[] download_file(String identifier, long file_offset, long download_bytes,String expand_str);
	
	
	/**
	 * 下载
	 * @param identifier 文件标识
	 * @param target_url 下载模板路径
	 * @param expand_str 扩展str
	 * file content/buff, return null if fail
	 */
	public abstract Boolean download_file(String identifier,String target_url,String expand_str);
	
	
}
