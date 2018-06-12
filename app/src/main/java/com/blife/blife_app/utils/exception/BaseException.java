package com.blife.blife_app.utils.exception;


import com.blife.blife_app.utils.logcat.L;

/**
 * Created by w on 2016/7/22.
 */
public class BaseException extends Throwable {

    public BaseException() {
    }

    public BaseException(String detailMessage) {
        super("44##" + detailMessage);
        L.e(detailMessage);
    }

    public BaseException(String detailMessage, Throwable cause) {
        super(detailMessage, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}
