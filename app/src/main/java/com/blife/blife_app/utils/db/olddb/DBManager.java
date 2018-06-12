package com.blife.blife_app.utils.db.olddb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.blife.blife_app.utils.exception.dbexception.DBException;
import com.blife.blife_app.utils.exception.dbexception.DBRuntimeException;
import com.blife.blife_app.utils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Somebody on 2016/7/18.
 */
public class DBManager {
    private static DBManager dbManger;
    private SQLiteDatabase db;
    private DBOpenHelper dbHelper;

//  ***********  重要：：：：想要增加表结构，一定需要升级dbVersion

    /**
     * 创建数据库带单个表结构
     *
     * @param context
     * @param dbName    数据库名称
     * @param dbVersion 数据库版本号 想要创建表，需要先将dbVersion升级
     * @param table     表名称
     * @param column    列名内容，自带主键id
     */
    private DBManager(Context context, String dbName, int dbVersion, String table, String[] column) {
        String[] tablearray = {table};
        if (dbVersion <= 0) {
            throw new IllegalArgumentException("dbVersion <= 0");
        }
        dbHelper = DBOpenHelper.getInstance(context, dbName, dbVersion, tablearray, column);
        try {
            db = dbHelper.getReadableDatabase();
        } catch (Exception e) {
            //如果通过自动增加版本处理异常，则 不是版本造成的异常就会陷入死循环
            throw new DBRuntimeException("新建表需要升级数据库，增加dbVersion");
        }
    }

    /**
     * 创建数据库带多个表结构
     *
     * @param context
     * @param dbName    数据库名称
     * @param dbVersion 数据库版本号,想要创建表，需要先将dbVersion升级
     * @param table     表名称
     * @param column    列名内容，自带主键id,是个不同表不同的列
     */
    private DBManager(Context context, String dbName, int dbVersion, String[] table, String[] column) throws DBException {
        if (dbVersion <= 0) {
            throw new IllegalArgumentException("dbVersion <= 0");
        }
        if (column == null || column.length <= 0 || table == null || table.length <= 0) {
            throw new NullPointerException("colume is null or table=null");
        }
        dbHelper = DBOpenHelper.getInstance(context, dbName, dbVersion, table, column);
        try {
            db = dbHelper.getReadableDatabase();
        } catch (Exception e) {
            throw new DBRuntimeException("新建表需要升级数据库，增加dbVersion");
        }

    }

    public static synchronized DBManager getInstance(Context context,String dbName, int dbVersion, String table, String[] column) {
        if (dbManger == null) {
            dbManger = new DBManager(context, dbName, dbVersion, table, column);
        }
        return dbManger;
    }

    /**
     * 判断某张表是否存在
     *
     * @param tableName 表名
     * @return
     */
    public boolean tabbleIsExist(String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from sqlite_master where type ='table' and name =" + "'" + tableName + "'";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * 删除表结构
     *
     * @param table
     */
    public void deleteTable(String table) throws DBException {
        if (table == null) {
            return;
        }
        db.execSQL("DROP TABLE IF EXISTS " + table);
    }

    /**
     * 有多少个表
     *
     * @return
     */
    public int getTableCount() throws DBException {
        int count = 0;
        Cursor cursor = db.rawQuery("select count(*) from sqlite_master  where type='table'", null);
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        return count;
    }

    //遍历所有的表
    public List<String> getTable() throws DBException {
        List<String> list = new ArrayList<>();
        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", null);
            while (cursor.moveToNext()) {
                //遍历出表名
                String name = cursor.getString(0);
                list.add(name);
            }
            db.setTransactionSuccessful();
            return list;
        } catch (Exception e) {
            throw new DBException("getTable 失败");
        } finally {
            //结束事务
            db.endTransaction();
        }
    }

    /**
     * 整行插入，带table
     *
     * @param data   json数据
     * @param column 列名
     */
    public synchronized Boolean insertData(String table, String[] column, String[] data) throws DBException {                  //这里需要抓取异常，表不存在的异常

        if (column.length <= 0) {
            return false;
        }
        if (table == null || column == null || data == null) {
            return false;
        }
        if (!tabbleIsExist(table)) {
            throw new DBException("此表不存在");
        }
        if (column.length != data.length) {
            throw new DBException("列长度与插入的数据长度不一致");
        }
        ContentValues values = new ContentValues();
        if (data.length > 0 && column.length == data.length) {
            for (int i = 0; i < data.length; i++) {
                values.put(column[i], data[i]);
            }
            long replace = db.replace(table, null, values);
            if (replace > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 插入某个表table 某列key下值
     *
     * @param table 某表
     * @param key   某列key
     * @param value 要插入的值
     */
    public void insert(String table, String key, String value) throws DBException {
        if (table == null || table.trim().length() <= 0) {
            return;
        }
        if (!tabbleIsExist(table)) {
            throw new DBException("此表不存在");
        }
        if (key == null || key.trim().length() <= 0 || checkColumnExist(table, key)) {
            throw new DBException("列名不存在");
        }
        if (value == null) {
            value = "";
        }
        db.execSQL("insert into " + table + "(" + key + ")" + "values(?)", new Object[]{value});
    }

    /**
     * 更新某个表table的一列
     *
     * @param table   某表
     * @param idKey   根据此列更新
     * @param idValue 根据此列更新对应的value
     * @param key     要更新的列
     * @param value   要更新的列的值
     */
    public Boolean update(String table, String idKey, String idValue, String key, String value) throws DBException {
        if (table == null || table.trim().length() <= 0) {
            throw new DBException("table is null");
        }
        if (idKey == null || idKey.trim().length() <= 0 || !checkColumnExist(table, idKey)) {
            throw new DBException("idKey is wrong");
        }

        if (key == null || key.trim().length() <= 0 || !checkColumnExist(table, key)) {
            throw new DBException("key is wrong");
        }
        if (idValue == null) {
            throw new DBException("idValue is wrong");
        }
        if (value == null) {
            throw new DBException("value is wrong");
        }

        if (!tabbleIsExist(table)) {
            throw new DBException("table is not exist");
        }
        ContentValues values = new ContentValues();
        values.put(key, value);//key为字段名，value为值
        //update返回值是更新成功的个数
        int update = 0;
        try {
            update = db.update(table, values, idKey + "=?", new String[]{idValue});
        } catch (Exception e) {
            throw new DBException("update失败:" + e.getMessage());
        }
        if (update > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * @param table
     * @param column  要更新的全部列名
     * @param data    要更新的全部数据
     * @param idKey   根据这列这行的值更新
     * @param idValue 这行的值
     */
    public Boolean update(String table, String idKey, String idValue, String[] column, String[] data) throws DBException {
        if (table == null || table.trim().length() <= 0) {
            throw new DBException("table is null");
        }
        if (column == null || data == null || idKey == null || idValue == null) {
            throw new DBException("参数错误");
        }
        if (!checkColumnExist(table, idKey)) {
            throw new DBException("列名不存在");
        }
        if (!tabbleIsExist(table)) {
            throw new DBException("table is null");
        }
        if (column.length <= 0) {
            return false;
        }
        for (int i = 0; i < column.length; i++) {
            if (!checkColumnExist(table, column[i])) {
                throw new DBException("列名不存在");
            }
        }

        if (column.length != data.length) {
            throw new DBException("表结构与更新的数据不匹配");
        }
        ContentValues updatedValues = new ContentValues();
        if (data.length > 0 && column.length == data.length) {
            for (int i = 0; i < data.length; i++) {
                updatedValues.put(column[i], data[i]);
            }
            db.beginTransaction();
            int update = 0;
            try {
                update = db.update(table, updatedValues, idKey + "=?", new String[]{idValue});
                db.setTransactionSuccessful();
            } catch (Exception e) {
                //抛异常
                throw new DBException("update异常，请检查表结构");
            } finally {
                //结束事务
                db.endTransaction();
            }
            if (update > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 查找单列所有值
     *
     * @param table
     * @param column
     * @return
     */
    public List<String> getSelectData(String table, String column) throws DBException {
        List<String> list = new ArrayList<>();
        if (!tabbleIsExist(table)) {
            throw new DBException("table is not exist");
        }
        if (!checkColumnExist(table, column)) {
            throw new DBException("列名不正确");
        }
        try {
            Cursor cursor = db.rawQuery("select " + column + " from " + table, null);
            LogUtils.e("  cursor.getCount=" + cursor.getCount());
            while (cursor.moveToNext()) {
                list.add(cursor.getString(cursor.getColumnIndex(column)));
            }
        } catch (Exception e) {
            throw new DBException("获取失败，请检查表结构");
        }
        return list;
    }

    /**
     * 查找多列的值
     *
     * @param key   字段
     * @param value 字段的值
     * @return 数据
     */
    public synchronized List<String> getRankData(String table, String key, String value, String[] column) throws DBException {
        List<String> list = new ArrayList<>();
        if (!tabbleIsExist(table)) {
            throw new DBException("table is not exist");
        }
        if (!checkColumnExist(table, key)) {
            throw new DBException("列名错误");
        }
        if (column.length <= 0) {
            return list;
        }
        for (int i = 0; i < column.length; i++) {
            if (!checkColumnExist(table, column[i])) {
                throw new DBException("列名错误");
            }
        }
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + table + " WHERE " + key + "= ?", new String[]{value});
            if (cursor.moveToNext() && column.length > 0) {
                for (int i = 0; i < column.length; i++) {
                    Log.e("TAG", "  cursor.getColumnIndex=" + cursor.getColumnIndex(column[i]));
                    list.add(cursor.getString(cursor.getColumnIndex(column[i])));
                }
            }
        } catch (Exception e) {
            throw new DBException("getRankData 异常：" + e.getMessage());
        }
        return list;
    }

    /**
     * 查找整行的值
     *
     * @param key   字段
     * @param value 字段的值
     * @return 数据
     */
    public synchronized List<String> getRankData(String table, String key, String value) throws DBException {
        List<String> list = new ArrayList<>();
        if (!tabbleIsExist(table)) {
            throw new DBException("table is not exist");
        }
        if (!checkColumnExist(table, key)) {
            throw new DBException("列名错误");
        }
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + table + " WHERE " + key + "= ?", new String[]{value});
            if (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    list.add(cursor.getString(i));
                }
            }
        } catch (Exception e) {
            throw new DBException("getRankData 异常：" + e.getMessage());
        }
        return list;
    }

    /**
     * 根据某列数据获取某列的数据（根据url标签值获取data数据）
     *
     * @param key      字段
     * @param value    字段的值
     * @param columVal 列名
     * @return 数据
     */
    public synchronized String getData(String table, String key, String value, String columVal) throws DBException {
        String result = "";
        if (!tabbleIsExist(table)) {
            throw new DBException("table is not exist");
        }
        if (!checkColumnExist(table, key) || !checkColumnExist(table, columVal)) {
            throw new DBException("列名错误");
        }
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + table + " WHERE " + key + "= ?", new String[]{value});
            if (cursor.moveToNext()) {
                result = cursor.getString(cursor.getColumnIndex(columVal));
            }
        } catch (Exception e) {
            throw new DBException("getData 异常：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param table 要删除数据的表结构
     * @param key   要删除的列名
     * @param value 要删除的行值
     */
    public void delete(String table, String key, String value) throws DBException {

        if (!tabbleIsExist("aaa")) {
            throw new DBException("table is not exist");
        }
        if (!checkColumnExist(table, key)) {
            throw new DBException("列名错误");
        }
        try {
            db.delete(table, key + "=?", new String[]{value});
        } catch (Exception e) {
            throw new DBException("delete失败，请检查表结构");
        }
    }

    /**
     * 表里有几行数据
     *
     * @param table
     * @return
     */
    public long getDataCount(String table) throws DBException {
        long result = 0;
        if (!tabbleIsExist(table)) {
            throw new DBException("table is not exist");
        }
        try {
            Cursor cursor = db.rawQuery("select count(*) from " + table, null);
            cursor.moveToFirst();
            result = cursor.getLong(0);
        } catch (Exception e) {
            throw new DBException("getDataCount 失败+" + e.getMessage());
        }
        return result;
    }


    /**
     * 方法1：检查某表列是否存在
     *
     * @param tableName  表名
     * @param columnName 列名
     * @return
     */
    public boolean checkColumnExist(String tableName, String columnName) {
        boolean result = false;
        Cursor cursor = null;
        try {
            //查询一行
            cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0"
                    , null);
            result = cursor != null && cursor.getColumnIndex(columnName) != -1;
        } catch (Exception e) {
            return false;
        }
        return result;
    }
}
