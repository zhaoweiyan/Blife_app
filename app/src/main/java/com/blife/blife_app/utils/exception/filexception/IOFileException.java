package com.blife.blife_app.utils.exception.filexception;


import com.blife.blife_app.utils.exception.BaseException;

/**
 * Created by Somebody on 2016/8/8.
 */
public class IOFileException extends BaseException {

    public IOFileException() {
    }

    public IOFileException(String detailMessage) {
        super(detailMessage);
    }

    public IOFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public IOFileException(Throwable cause) {
        super(cause == null ? null : cause.toString(), cause);
    }
}
