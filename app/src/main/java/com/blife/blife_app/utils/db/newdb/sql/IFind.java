package com.blife.blife_app.utils.db.newdb.sql;

/**
 * 查询语句
 * 
 * @author ByZhao </br> Date: 13-11-18
 */
public interface IFind {

	public IFind groupBy(String columnName);

	public IFind orderBy(String columnName, boolean desc);

	public IFind limit(int limit);

	public IFind offset(int offset);
}
