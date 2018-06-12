package com.blife.blife_app.utils.db.newdb.converters.ColumnConverter;


import com.blife.blife_app.utils.db.newdb.converters.DBType;

/**
 * Created by Somebody on 2016/8/16.
 */

public class ByteConverter extends BaseConverter {
    // byte ->> int
    @Override
    public Integer toSqlValue(Object value) {
        return ((Number) value).intValue();
    }

    // int ->> byte
    @Override
    public Byte toJavaValue(Object value) {
        return ((Number) value).byteValue();
    }

    // byte
    @Override
    public DBType getDBType() {
        return DBType.INTEGER;
    }

}