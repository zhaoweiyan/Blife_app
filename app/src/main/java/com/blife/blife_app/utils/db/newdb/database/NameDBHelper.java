package com.blife.blife_app.utils.db.newdb.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blife.blife_app.utils.util.LogUtils;


/**
 * 实现DBHelper 继承SQLiteOpenHelper<br/>
 * 子类可以通过继承该类进行监听数据库版本的变化，和数据库创建,用法参照(
 * {@link SQLiteOpenHelper})
 * 
 * @author Administrator
 * 
 */
public class NameDBHelper extends SQLiteOpenHelper implements DBHelper {
	private String path;

	public NameDBHelper(Context context, String name, int version) {
		super(context, name, null, version);
		path = context.getDatabasePath(name).getPath();
		LogUtils.e("NameDBHelper path=="+path);
	}
	@Override
	public SQLiteDatabase getDatabase() {
		SQLiteDatabase database = null;
		try {
			database = getWritableDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return database;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	@Override
	public String getDatabasePath() {
		return path;
	}
}
