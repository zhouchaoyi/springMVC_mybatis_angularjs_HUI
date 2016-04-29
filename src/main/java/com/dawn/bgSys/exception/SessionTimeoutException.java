package com.dawn.bgSys.exception;

/**
 * session超时异常
 * Created with IntelliJ IDEA.
 * User: malone
 * Date: 14-3-24
 * Time: 下午2:16
 * To change this template use File | Settings | File Templates.
 */
public class SessionTimeoutException extends GenericException {

    public SessionTimeoutException(String msg) {
        super(msg);
    }

    public SessionTimeoutException(String msg, String errorCode) {
        super(msg, errorCode);
    }
}
