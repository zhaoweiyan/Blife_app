package com.blife.blife_app.utils.db.olddb;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Somebody on 2016/7/29.
 */
public class DBUtils {
    /**
     * 有多少个数据库
     *
     * @param context
     * @return
     */
    public static int getDbCount(Context context) {
        if (context.databaseList() == null || context.databaseList().length <= 0) {
            return 0;
        }
        return context.databaseList().length;
    }

    /**
     * 获取数据库的名称集合
     *
     * @param context
     * @return
     */
    public static List<String> getDb(Context context) {
        List<String> list = new ArrayList<>();
        if (context.databaseList() == null || context.databaseList().length <= 0) {
            return list;
        }
        for (int i = 0; i < context.databaseList().length; i++) {
            list.add(context.databaseList()[i]);
        }
        return list;
    }
    /**
     * 删除数据库
     *
     * @param context
     * @param dbName
     */
    public static void deleteDB(Context context, String dbName) {
        if (dbName == null || context.databaseList() == null || context.databaseList().length <= 0) {
            return;
        }
        context.deleteDatabase(dbName);
    }
}
