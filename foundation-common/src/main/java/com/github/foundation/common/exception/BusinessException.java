package com.github.foundation.common.exception;

/**
 * @Description: BusinessException
 * @Author: kevin
 * @Date: 2019/7/4 14:43
 */
public class BusinessException extends BaseRuntimeException {

    private static final long serialVersionUID = -285699186370621764L;

    public BusinessException() {
        super("business exception.");
    }

    public BusinessException(String errMsg) {
        super(errMsg);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String errMsg, Throwable cause) {
        super(errMsg, cause);
    }
}
