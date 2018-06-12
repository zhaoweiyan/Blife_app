package com.blife.blife_app.utils.db.newdb.converters.ColumnConverter;


import com.blife.blife_app.utils.db.newdb.converters.DBType;

/**
 * Created by Somebody on 2016/8/16.
 */
public class ShortConverter extends BaseConverter {

    // short ->> int
    @Override
    public Integer toSqlValue(Object value) {
        return ((Number) value).intValue();
    }

    // int ->> short
    @Override
    public Short toJavaValue(Object value) {
        return ((Number) value).shortValue();
    }

    @Override
    public DBType getDBType() {
        return DBType.INTEGER;
    }

}