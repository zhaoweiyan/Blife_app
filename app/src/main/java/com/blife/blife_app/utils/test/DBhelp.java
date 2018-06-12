package com.blife.blife_app.utils.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blife.blife_app.utils.logcat.L;


/**
 * Created by w on 2016/7/27.
 */
public class DBhelp extends SQLiteOpenHelper {

    private String[] sql;



    public DBhelp(Context context) {
        super(context, "test.db", null, 1);
    }

    public DBhelp(Context context, String[] sql) {
        super(context, "test.db", null, 1);
        this.sql = sql;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        L.e("TAG", "DB---onCreate创建表格");
        if (sql == null) {
            return;
        }
        if (sql.length <= 0) {
            return;
        }

        for (int i = 0; i < sql.length; i++) {
            db.execSQL(sql[i]);
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
