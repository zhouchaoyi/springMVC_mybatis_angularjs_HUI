package com.dawn.bgSys.exception;


import com.dawn.bgSys.common.consts.Consts;

/**
 * 所有异常的基类
 * Created with IntelliJ IDEA.
 * User: malone
 * Date: 13-12-10
 * Time: 下午4:40
 * To change this template use File | Settings | File Templates.
 */
public class GenericException extends RuntimeException {

    /**
     *  异常对应的错误码(约定)
     */
    private String errorCode;

    GenericException(String msg) {
        super(msg);
        this.errorCode = Consts.COMMON_ERROR_CODE;
    }

    GenericException(String msg, String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
