package cn.edu.hzu.common.api.utils;

import cn.edu.hzu.common.api.ResultCode;
import cn.edu.hzu.common.exception.CommonException;
import org.apache.commons.io.IOUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author lzf
 * @createTime 2022/09/01
 * @description BASE64工具类
 */
public class Base64Utils extends org.springframework.util.Base64Utils {

    private static String IMG_PREFIX_PNG = "data:image/png;base64,";

    private static BASE64Decoder decoder = new BASE64Decoder();

    private static BASE64Encoder encoder = new BASE64Encoder();

    public static BASE64Encoder getBASE64Encoder() {
        return encoder;
    }

    public static BASE64Decoder getBASE64Decoder() {
        return decoder;
    }

    /**
     * 将图片用base64编码输出
     * 前端引用：<img src="data:image/png;base64,${str}"/>   ${str}为图片的base64字符串
     * @param imgUrl 可访问的图片连接
     * @return 图片的base64字符串
     */
    public static String encodeImgFromUrl(String imgUrl) {
        byte[] bytes = encodeImgFromUrlToByte(imgUrl);
        return encoder.encode(bytes);
    }

    /**
     * 将图片用base64编码输出
     * 前端引用：<img src="data:image/png;base64,${str}"/>   ${str}为图片的base64字符串
     * @param imgUrl 可访问的图片连接
     * @return 图片的base64字节数据
     */
    public static byte[] encodeImgFromUrlToByte(String imgUrl) {
        URL url = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;
        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            is = httpUrl.getInputStream();

            outStream = new ByteArrayOutputStream();
            IOUtils.copy(is, outStream);
            // 对字节数组Base64编码
            return outStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpUrl != null) {
                httpUrl.disconnect();
            }
        }
        throw new CommonException(ResultCode.FAILURE);
    }

    /**
     * 将输入流转化成base64编码字符串
     * @param source 输入流
     * @return 图片的base64字符串
     */
    public static String encodeToString(InputStream source) {
        ByteArrayOutputStream outStream = null;
        try {
            outStream = new ByteArrayOutputStream();
            IOUtils.copy(source, outStream);
            return encoder.encode(outStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new CommonException(ResultCode.FAILURE);
    }

    /**
     * 将输入流的内容转化成Base64，并由输出流带出
     *
     * @param source 输入流数据源
     * @param target 转成Base64的输出流
     */
    public static void encodeToOutput(InputStream source, OutputStream target) {
        try {
            encoder.encode(source, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将读取了Base64的输入流解码后由输出流带出
     * @param source 读取了Base64的输入流
     * @param target 解码后的输出流
     */
    public static void decodeToOutput(InputStream source, OutputStream target) {
        try {
            decoder.decodeBuffer(source, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Base64字符串解码后由输出流带出
     * @param source Base64字符串
     * @param target 解码后的输出流
     */
    public static void decodeToOutput(String source, OutputStream target) {
        if (source.startsWith(IMG_PREFIX_PNG)) {
            source = source.replaceFirst(IMG_PREFIX_PNG, "");
        }
        try {
            byte[] bytes = decoder.decodeBuffer(source);
            IOUtils.write(bytes, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
