package com.blife.blife_app.utils.exception.bitmapexception;


import com.blife.blife_app.utils.exception.BaseException;

/**
 * Created by w on 2016/8/11.
 */
public class BitmapException extends BaseException {

    public BitmapException() {
    }

    public BitmapException(String detailMessage) {
        super(detailMessage);
    }

    public BitmapException(String detailMessage, Throwable cause) {
        super(detailMessage, cause);
    }

    public BitmapException(Throwable cause) {
        super(cause);
    }
}
