package com.blife.blife_app.utils.db.newdb.converters.ColumnConverter;


import com.blife.blife_app.utils.db.newdb.converters.DBType;

/**
 * Created by Somebody on 2016/8/16.
 */
public class LongConverter extends BaseConverter {
    // long ->> long
    @Override
    public DBType getDBType() {
        return DBType.BIGINT;
    }

}