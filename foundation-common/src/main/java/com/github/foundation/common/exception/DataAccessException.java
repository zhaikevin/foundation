package com.github.foundation.common.exception;

/**
 * @Description: DataAccessException
 * @Author: kevin
 * @Date: 2019/7/4 14:43
 */
public class DataAccessException extends BaseRuntimeException {

    private static final long serialVersionUID = -285699186370621764L;

    public DataAccessException() {
        super("data access exception.");
    }

    public DataAccessException(String errMsg) {
        super(errMsg);
    }

    public DataAccessException(Throwable cause) {
        super(cause);
    }

    public DataAccessException(String errMsg, Throwable cause) {
        super(errMsg, cause);
    }
}
