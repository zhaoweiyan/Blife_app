package com.blife.blife_app.utils.db.newdb.table;


import com.blife.blife_app.utils.db.newdb.annotations.Check;
import com.blife.blife_app.utils.db.newdb.annotations.Id;
import com.blife.blife_app.utils.db.newdb.annotations.NotNull;
import com.blife.blife_app.utils.db.newdb.annotations.Transient;
import com.blife.blife_app.utils.db.newdb.annotations.Unique;
import com.blife.blife_app.utils.db.newdb.annotations.Column;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


/**
 * @author ByZhao </br> Date: 13-11-18
 */
public class ColumnUtils {
    /**
     * 是否是不需要的字段
     *
     * @param field
     * @return 是否是不需要的字段
     */
    public static boolean isTransient(Field field) {
        int mark = field.getModifiers();
        return Modifier.isStatic(mark) || Modifier.isTransient(mark)
                || field.getAnnotation(Transient.class) != null;
    }

    /**
     * 得到字段名
     *
     * @param field
     * @return 在数据库表中字段的名字
     */
    public static String getColumnName(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (column != null) {
            return column.column();
        }
        return field.getName();
    }

    /**
     * 得到该字段的默认值
     *
     * @param column
     * @return 默认值
     */
    public static String getDefaultValue(com.blife.blife_app.utils.db.newdb.table.Column column) {
        return getDefalutValue(column.getColumnField());
    }

    /**
     * 得到该字段的默认值
     *
     * @param field
     * @return 默认值
     */
    public static String getDefalutValue(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (column != null)
            return column.column();
        return null;
    }

    /**
     * 该字段是否是唯一
     *
     * @param field
     * @return 是否是唯一
     */
    public static boolean isUnique(Field field) {
        return field.getAnnotation(Unique.class) != null;
    }

    /**
     * 该字段是否是不为空
     *
     * @param field
     * @return 是否是不为空
     */
    public static boolean isNotNull(Field field) {
        return field.getAnnotation(NotNull.class) != null;
    }

    public static String getCheck(Field field) {
        Check check = field.getAnnotation(Check.class);
        if (check != null)
            return check.value();
        return null;
    }

    public static boolean isAutoIncrement(Table table) {
        return table.getId().getColumnField().getAnnotation(Id.class)
                .autoIncrement();
    }

    public static boolean isId(Field field) {
        return field.getAnnotation(Id.class) != null;
    }

}
