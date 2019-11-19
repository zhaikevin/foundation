package com.github.foundation.common.exception;

/**
 * @Description: 内部异常
 * @Author: kevin
 * @Date: 2019/7/4 14:43
 */
public class InternalException extends BaseRuntimeException {

    private static final long serialVersionUID = -285699186370621764L;

    public InternalException() {
        super("internal exception.");
    }

    public InternalException(String errMsg) {
        super(errMsg);
    }

    public InternalException(Throwable cause) {
        super(cause);
    }

    public InternalException(String errMsg, Throwable cause) {
        super(errMsg, cause);
    }
}
