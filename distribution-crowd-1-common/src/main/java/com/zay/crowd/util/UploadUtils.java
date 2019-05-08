package com.zay.crowd.util;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;

public class UploadUtils {
	/**
	 * 生成文件名
	 * @param originalFilename
	 * @return
	 */
	public static String generateFileName(String originalFilename) {
		
		String mainFileName = UUID.randomUUID().toString().replaceAll("-", "");
		
		String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
		
		return mainFileName + extName;
	}
	/**
	 * 根据当前天日期生成目录名
	 * @return
	 */
	public static String generateDayFolderName() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}
	
	/**
	 * 上传单个文件到OSS
	 * @param endpoint
	 * @param accessKeyId
	 * @param accessKeySecret
	 * @param fileName
	 * @param folderName
	 * @param bucketName
	 * @param inputStream
	 */
	public static void uploadSingleFile(
				String endpoint, 
				String accessKeyId, 
				String accessKeySecret, 
				String fileName,
				String folderName,
				String bucketName,
				InputStream inputStream) {
		try {
			// 创建OSSClient实例。
			OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			// 存入对象的名称=目录名称+"/"+文件名
			String objectName = folderName + "/" + fileName;
			ossClient.putObject(bucketName, objectName, inputStream);
			// 关闭OSSClient。
			ossClient.shutdown();
		} catch (OSSException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} catch (ClientException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
}
