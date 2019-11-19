package com.github.foundation.common.exception;

/**
 * @Description: IllegalParameterException
 * @Author: kevin
 * @Date: 2019/7/4 14:43
 */
public class IllegalParameterException extends BaseRuntimeException {

    private static final long serialVersionUID = -285699186370621764L;

    public IllegalParameterException() {
        super("illegal parameter exception.");
    }

    public IllegalParameterException(String errMsg) {
        super(errMsg);
    }

    public IllegalParameterException(Throwable cause) {
        super(cause);
    }

    public IllegalParameterException(String errMsg, Throwable cause) {
        super(errMsg, cause);
    }
}
