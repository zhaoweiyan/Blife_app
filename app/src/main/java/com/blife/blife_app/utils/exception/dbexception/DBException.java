package com.blife.blife_app.utils.exception.dbexception;


import com.blife.blife_app.utils.exception.BaseException;

/**
 * Created by Somebody on 2016/8/11.
 */
public class DBException extends BaseException {

    public DBException() {
    }
    public DBException(String detailMessage) {
        super(detailMessage);
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBException(Throwable cause) {
        super((cause == null ? null : cause.toString()), cause);
    }
}
