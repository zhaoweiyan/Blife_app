package com.blife.blife_app.utils.exception.cacheException;


import com.blife.blife_app.utils.exception.filexception.FileException;

/**
 * Created by Somebody on 2016/8/8.
 */
public class FileCacheException extends FileException {
    public FileCacheException() {
    }

    public FileCacheException(String detailMessage) {
        super(detailMessage);
    }
}
