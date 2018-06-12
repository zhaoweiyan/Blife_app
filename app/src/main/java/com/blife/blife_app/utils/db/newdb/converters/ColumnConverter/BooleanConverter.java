package com.blife.blife_app.utils.db.newdb.converters.ColumnConverter;


import com.blife.blife_app.utils.db.newdb.converters.DBType;
import com.blife.blife_app.utils.db.newdb.converters.IColumnConverter;

/**
 * Created by Somebody on 2016/8/16.
 */
public class BooleanConverter implements IColumnConverter {
    // boolean ->> int
    @Override
    public Integer toSqlValue(Object value) {
        return (Boolean) value ? 1 : 0;
    }

    // int ->> boolean
    @Override
    public Boolean toJavaValue(Object value) {
        return (Integer) value == 1 ? true : false;
    }

    @Override
    public DBType getDBType() {
        return DBType.INTEGER;
    }
}
