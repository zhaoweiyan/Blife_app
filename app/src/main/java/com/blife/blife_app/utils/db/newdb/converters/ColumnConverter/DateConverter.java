package com.blife.blife_app.utils.db.newdb.converters.ColumnConverter;


import com.blife.blife_app.utils.db.newdb.converters.DBType;
import com.blife.blife_app.utils.db.newdb.converters.IColumnConverter;

import java.sql.Date;

/**
 * Created by Somebody on 2016/8/16.
 */

public class DateConverter implements IColumnConverter {

    // date ->> long
    @Override
    public Long toSqlValue(Object value) {
        Date date = (Date) value;
        return date.getTime();
    }

    // long ->> date
    @Override
    public Date toJavaValue(Object value) {
        Long time = (Long) value;
        return new Date(time);
    }

    @Override
    public DBType getDBType() {
        return DBType.BIGINT;
    }

}