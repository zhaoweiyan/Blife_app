package com.blife.blife_app.utils.db.newdb.converters;

/**
 * sql类型与java类型 互相转化的转化器
 * 
 * @author ByZhao </br> Date: 13-11-18
 */
public interface IColumnConverter {

	public Object toSqlValue(Object value);

	public Object toJavaValue(Object value);

	public DBType getDBType();
}
