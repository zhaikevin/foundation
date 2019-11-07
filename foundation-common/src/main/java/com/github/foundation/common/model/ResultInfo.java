package com.github.foundation.common.model;

import com.github.foundation.common.consts.Consts;

/**
 * @Description: 客户端的HTTP调用的应答结果类，返回信息包括调用状态、可读的msg、结果数据.
 * @Author: kevin
 * @Date: 2019/9/12 10:08
 */
public class ResultInfo {

    private int status = Consts.RESULT_CODE_SUCCESS;

    private String statusInfo = "SUCCESS"; // 操作结果描述信息

    private Object data;// 操作返回数据绑定

    public final static ResultInfo SUCCESSRESULT = new ResultInfo();

    public static ResultInfo error() {
        ResultInfo res = new ResultInfo(Consts.RESULT_CODE_COMMONERR, "ERROR");
        return res;
    }

    public static ResultInfo errorMessage(String errorMessage) {
        ResultInfo res = new ResultInfo(Consts.RESULT_CODE_COMMONERR, errorMessage);
        return res;
    }

    public static ResultInfo errorMessage(String errorMessage, Object data) {
        ResultInfo res = new ResultInfo(Consts.RESULT_CODE_COMMONERR, errorMessage);
        res.setData(data);
        return res;
    }

    public static ResultInfo result(int status, String info) {
        ResultInfo res = new ResultInfo();
        res.status = status;
        res.statusInfo = info;
        return res;
    }

    public static ResultInfo result(int status, String info, Object data) {
        ResultInfo res = new ResultInfo();
        res.status = status;
        res.statusInfo = info;
        res.data = data;
        return res;
    }

    public static ResultInfo success() {
        ResultInfo res = new ResultInfo();
        return res;
    }

    public static ResultInfo success(Object data) {
        ResultInfo res = new ResultInfo();
        res.setData(data);
        return res;
    }

    public static ResultInfo successMessage(String message) {
        ResultInfo res = new ResultInfo(Consts.RESULT_CODE_SUCCESS, message);
        return res;
    }

    public ResultInfo() {
    }

    public ResultInfo(int status, String statusInfo) {
        this.status = status;
        this.statusInfo = statusInfo;
    }

    public ResultInfo(int status, String statusInfo, Object data) {
        super();
        this.status = status;
        this.statusInfo = statusInfo;
        this.data = data;
    }

    public boolean isSuccess() {
        return this.status == 0;
    }

    public Object getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + status;
        result = prime * result + ((statusInfo == null) ? 0 : statusInfo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ResultInfo other = (ResultInfo) obj;
        if (data == null) {
            if (other.data != null) {
                return false;
            }
        } else if (!data.equals(other.data)) {
            return false;
        }
        if (status != other.status) {
            return false;
        }
        if (statusInfo == null) {
            if (other.statusInfo != null) {
                return false;
            }
        } else if (!statusInfo.equals(other.statusInfo)) {
            return false;
        }
        return true;
    }
}
