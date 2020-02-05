package com.github.foundation.common.utils;

import com.github.foundation.common.exception.IllegalParameterException;
import org.apache.commons.lang.StringUtils;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/19 14:45
 */
public final class ValidateUtils {

    public static void notEmptyString(String data) {
        if (StringUtils.isEmpty(data)) {
            throw new IllegalParameterException();
        }
    }

    public static void notEmptyString(String data, String message) {
        if (StringUtils.isEmpty(data)) {
            throw new IllegalParameterException(message);
        }
    }

    public static void notNull(Object object) {
        if (object == null) {
            throw new IllegalParameterException();
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalParameterException(message);
        }
    }

}
