package com.blife.blife_app.utils.db.newdb.converters.ColumnConverter;


import com.blife.blife_app.utils.db.newdb.converters.DBType;

/**
 * Created by Somebody on 2016/8/16.
 */
public class FlaotConverter extends BaseConverter {

    // float ->> double
    @Override
    public Object toSqlValue(Object value) {
        return ((Number) value).doubleValue();
    }

    // double ->> float
    @Override
    public Float toJavaValue(Object value) {
        return ((Number) value).floatValue();
    }

    @Override
    public DBType getDBType() {
        return DBType.REAL;
    }
}