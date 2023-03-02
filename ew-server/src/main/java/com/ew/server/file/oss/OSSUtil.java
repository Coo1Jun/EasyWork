package com.ew.server.file.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.ew.server.constants.OSSConstant;

import java.io.InputStream;
import java.util.List;

/**
 * @author lzf
 * @create 2023/03/01
 * @description OSS工具类
 */
public class OSSUtil {

    /**
     * 获取OSSClient
     * OSSClient是线程安全的，允许多线程访问同一实例。也可以创建多个OSSClient实例，分别使用。
     * OSSClient实例内部维持一个连接池。当OSSClient实例不再使用时，需要调用shutdown方法将其关闭，避免创建过多的OSSClient实例导致资源耗尽。
     */
    public static OSS getOSSClient() {
        return new OSSClientBuilder().build(
                OSSConstant.END_POINT,
                OSSConstant.ACCESS_KEY_ID,
                OSSConstant.ACCESS_KEY_SECRET);
    }

    /**
     * 上传文件
     * @param fileName 文件的路径和名称，如：test/show.txt
     * @param inputStream 文件的输入流
     * @return 文件的元信息
     */
    public static PutObjectResult upload(String fileName, InputStream inputStream) {
        OSS ossClient = getOSSClient();
        PutObjectRequest putObjectRequest = new PutObjectRequest(OSSConstant.BUCKET_NAME, fileName, inputStream);
        // 设置该属性可以返回response。如果不设置，则PutObjectResult返回的response为空。
        putObjectRequest.setProcess("true");
        PutObjectResult result = ossClient.putObject(putObjectRequest);
        ossClient.shutdown();
        return result;
    }

    /**
     上传文件
     * @param fileName 文件的路径和名称，如：test/show.txt
     * @param inputStream 文件的输入流
     * @param objectMetadata 可以设置ContentType、ContentLength等信息
     * @return 文件的元信息
     */
    public static PutObjectResult upload(String fileName, InputStream inputStream, ObjectMetadata objectMetadata) {
        OSS ossClient = getOSSClient();
        PutObjectRequest putObjectRequest = new PutObjectRequest(OSSConstant.BUCKET_NAME, fileName, inputStream, objectMetadata);
        // 设置该属性可以返回response。如果不设置，则PutObjectResult返回的response为空。
        putObjectRequest.setProcess("true");
        PutObjectResult result = ossClient.putObject(putObjectRequest);
        ossClient.shutdown();
        return result;
    }

    /**
     * 批量删除文件，删除后无法恢复，谨慎调用！
     * @param fileNames 文件的路径和名称，如：test/show.txt。如果要删除目录，目录必须为空。
     */
    public static void delete(List<String> fileNames) {
        OSS ossClient = getOSSClient();
        ossClient.deleteObjects(new DeleteObjectsRequest(OSSConstant.BUCKET_NAME).withKeys(fileNames));
        ossClient.shutdown();
    }

}
