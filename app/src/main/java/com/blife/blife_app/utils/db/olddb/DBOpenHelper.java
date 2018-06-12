package com.blife.blife_app.utils.db.olddb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBOpenHelper extends SQLiteOpenHelper {

    private static DBOpenHelper helper;
    private String table;
    private String[] colum;
    private String[] tableArray;

    private DBOpenHelper(Context context, String dbname, int version, String[] tableArray, String[] column) {
        super(context, dbname, null, version);
        this.tableArray = tableArray;
        this.colum = column;
    }

    public static synchronized DBOpenHelper getInstance(Context context, String dbname, int version, String[] tableArray, String[] column) {
        if (helper == null) {
            helper = new DBOpenHelper(context, dbname, version, tableArray, column);
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (tableArray == null || tableArray.length <= 0 || colum == null || colum.length <= 0) {
            return;
        }
        for (int i = 0; i < tableArray.length; i++) {
            String sql = "CREATE TABLE IF NOT EXISTS "
                    + tableArray[i] + " ("
                    + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
            StringBuffer stringBuffer = new StringBuffer();
            //将表里的各列的名称放入表结构
            if (colum.length > 0) {
                for (int j = 0; j < colum.length; j++) {
                    if (j == colum.length - 1) {
                        stringBuffer.append(colum[j] + " TEXT");
                    } else {
                        stringBuffer.append(colum[j] + " TEXT, ");
                    }
                }
                sql = sql + stringBuffer.toString() + ")";
            } else {
                sql = sql + ")";
            }
            Log.i("TAG", "sql==" + sql);
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}