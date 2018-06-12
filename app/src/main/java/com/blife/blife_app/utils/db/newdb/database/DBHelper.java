package com.blife.blife_app.utils.db.newdb.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * 获取数据库，数据库的路径的接口<br/>
 * 使用者通过实现该接口，实现返回自己的数据库，返回自己的数据库路径
 * 
 * @author ByZhao </br> Date: 13-11-18
 */
public interface DBHelper {

	public SQLiteDatabase getDatabase();

	public String getDatabasePath();

}
