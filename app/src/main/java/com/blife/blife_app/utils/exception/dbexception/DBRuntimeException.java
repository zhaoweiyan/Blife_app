package com.blife.blife_app.utils.exception.dbexception;

/**
 * Created by Somebody on 2016/8/12.
 */
public class DBRuntimeException extends RuntimeException {
    public DBRuntimeException() {
    }

    public DBRuntimeException(String detailMessage) {
        super(detailMessage);
    }

    public DBRuntimeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DBRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
