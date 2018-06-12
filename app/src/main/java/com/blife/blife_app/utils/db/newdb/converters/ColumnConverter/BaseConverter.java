package com.blife.blife_app.utils.db.newdb.converters.ColumnConverter;


import com.blife.blife_app.utils.db.newdb.converters.IColumnConverter;

/**
 * Created by Somebody on 2016/8/16.
 */
public abstract class BaseConverter implements IColumnConverter {

    // 不进行转化
    @Override
    public Object toSqlValue(Object value) {
        return value;
    }

    // 不进行转化
    @Override
    public Object toJavaValue(Object value) {
        return value;
    }
}