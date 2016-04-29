package com.dawn.bgSys.exception;

/**
 * 操作错误exception
 * Created with IntelliJ IDEA.
 * User: malone
 * Date: 13-12-10
 * Time: 下午4:43
 * To change this template use File | Settings | File Templates.
 */
public class OperateFailureException extends GenericException {

    public OperateFailureException(String msg) {
        super(msg);
    }

    public OperateFailureException(String msg, String errorCode) {
        super(msg, errorCode);
    }

}
