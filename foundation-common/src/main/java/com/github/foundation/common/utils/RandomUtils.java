package com.github.foundation.common.utils;

import java.util.Random;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/25 17:24
 */
public final class RandomUtils {

    private RandomUtils() {

    }

    /**
     * 随机数
     * @param place 定义随机数的位数
     */
    public static String randomGen(int place) {
        String base = "qwertyuioplkjhgfdsazxcvbnmQAZWSXEDCRFVTGBYHNUJMIKLOP0123456789";
        StringBuffer sb = new StringBuffer();
        Random rd = new Random();
        for (int i = 0; i < place; i++) {
            sb.append(base.charAt(rd.nextInt(base.length())));
        }
        return sb.toString();
    }
}
