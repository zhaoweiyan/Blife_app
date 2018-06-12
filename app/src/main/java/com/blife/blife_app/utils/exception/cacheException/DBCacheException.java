package com.blife.blife_app.utils.exception.cacheException;


import com.blife.blife_app.utils.exception.dbexception.DBException;

/**
 * Created by Somebody on 2016/8/11.
 */
public class DBCacheException extends DBException {
    public DBCacheException() {
    }

    public DBCacheException(String detailMessage) {
        super(detailMessage);
    }
}
