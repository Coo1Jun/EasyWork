package com.ew.server.email.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lzf
 * @create 2023/03/03
 * @description 邮箱工具类
 */
public class EmailUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    );

    /**
     * 验证邮箱格式是否正确
     * @param email 邮箱
     * @return
     */
    public static boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    /**
     * 生成6位随机验证码
     * @return
     */
    public static String generateCode() {
        Random random = new Random();
        int num = random.nextInt(900000) + 100000; // 生成100000到999999之间的随机数
        return String.valueOf(num);
    }
}
