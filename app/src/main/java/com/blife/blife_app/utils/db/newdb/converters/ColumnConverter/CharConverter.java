package com.blife.blife_app.utils.db.newdb.converters.ColumnConverter;


import com.blife.blife_app.utils.db.newdb.converters.DBType;
import com.blife.blife_app.utils.db.newdb.converters.IColumnConverter;

/**
 * Created by Somebody on 2016/8/16.
 */
public class CharConverter implements IColumnConverter {
    // char ->> int
    @Override
    public Integer toSqlValue(Object value) {
        return (int) ((Character) value).charValue();
    }

    // char ->> int
    @Override
    public Character toJavaValue(Object value) {
        return (char) ((Integer) value).intValue();

    }

    @Override
    public DBType getDBType() {
        return DBType.INTEGER;
    }

}