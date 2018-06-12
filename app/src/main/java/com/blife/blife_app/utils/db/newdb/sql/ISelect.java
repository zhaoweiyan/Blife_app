package com.blife.blife_app.utils.db.newdb.sql;


import com.blife.blife_app.utils.db.newdb.sql.function.IFunction;

/**
 * 查询语句的中选择语句的接口定义
 * 
 * @author ByZhao </br> Date: 14-3-21
 */
public interface ISelect {
	/**
	 * select("age","name")表示<br/>
	 * "select age,name from table";<br/>
	 * <br/>
	 * select("count(*) as num")表示<br/>
	 * "select count(*) as num from table"; <br/>
	 * 
	 * @param columnNames
	 * @return
	 */
	public ISelect select(String... columnNames);

	public ISelect select(IFunction... functions);
}
