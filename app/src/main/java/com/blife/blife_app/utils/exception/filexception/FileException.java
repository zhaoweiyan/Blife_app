package com.blife.blife_app.utils.exception.filexception;

/**
 * Created by Somebody on 2016/8/8.
 */
public class FileException extends IOFileException {
    public FileException() {
    }

    public FileException(String detailMessage) {
        super(detailMessage);
    }
}
