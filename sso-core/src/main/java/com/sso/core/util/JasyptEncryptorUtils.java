package com.sso.core.util;

import org.jasypt.properties.PropertyValueEncryptionUtils;
import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lzf
 * @create 2023/02/13
 * @description 数据加密工具类
 */
public class JasyptEncryptorUtils {

    private static Logger logger = LoggerFactory.getLogger(JasyptEncryptorUtils.class);

    private static final String salt = "this_is_salt";

    private static BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();

    static {
        basicTextEncryptor.setPassword(salt);
    }

    private JasyptEncryptorUtils() {
    }

    /**
     * 明文加密
     *
     * @param plaintext
     * @return
     */
    public static String encode(String plaintext) {
        return basicTextEncryptor.encrypt(plaintext);
    }

    /**
     * 解密
     *
     * @param ciphertext
     * @return
     */
    public static String decode(String ciphertext) {
        ciphertext = "ENC(" + ciphertext + ")";
        if (PropertyValueEncryptionUtils.isEncryptedValue(ciphertext)) {
            try {
                return PropertyValueEncryptionUtils.decrypt(ciphertext, basicTextEncryptor);
            } catch (Exception e) {
                logger.warn("解密异常");
            }
        }
//        System.out.println("解密失败");
        return "";
    }
}
