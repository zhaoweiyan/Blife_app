package com.blife.blife_app.utils.cache;

import android.content.Context;
import android.os.Handler;
import android.os.Message;


import com.blife.blife_app.utils.db.olddb.DBManager;
import com.blife.blife_app.utils.db.olddb.DBUtils;
import com.blife.blife_app.utils.exception.cacheException.DBCacheException;
import com.blife.blife_app.utils.exception.dbexception.DBException;
import com.blife.blife_app.utils.exception.filexception.IOFileException;
import com.blife.blife_app.utils.file.FileManager;
import com.blife.blife_app.utils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;


//储存数组的数据库缓存

/**
 * Created by Somebody on 2016/7/28.
 */
public class NetSQLiteCacheManager {
    private static NetSQLiteCacheManager netSQLiteCacheManager;
    private Context context;
    private DBManager dbManager;
    private String TABLE = "classframeCache";
    private String LOSTTIME = "loseTime";
    private String CREATTIME = "creatTime";
    private String URL = "url";
    private String DATA = "data";
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DeleteIsSucess deleteIsSucess = (DeleteIsSucess) msg.obj;
            if (msg.what == 0) {
                deleteIsSucess.deleteSucess();
            } else if (msg.what == 1) {
                deleteIsSucess.deletFiled();
            } else if (msg.what == 2) {
                deleteIsSucess.deletError();
            }
        }
    };

    public interface DeleteIsSucess {
        void deleteSucess();

        void deletFiled();

        void deletError();
    }

    //所有的缓存自动添加缓存时间
    //构建构造函数，方便不想添加数据库的时候使用
    private NetSQLiteCacheManager() {

    }

    public static synchronized NetSQLiteCacheManager getInstance() {
        if (netSQLiteCacheManager == null) {
            netSQLiteCacheManager = new NetSQLiteCacheManager();
        }
        return netSQLiteCacheManager;
    }

    public NetSQLiteCacheManager init(Context context, String dbName) {
        String[] column = {URL, DATA, CREATTIME, LOSTTIME};
        int dbVersionName = 1;
        this.context = context;
        dbManager = DBManager.getInstance(context,dbName, dbVersionName, TABLE, column);
        return getInstance();
    }

    /**
     * 插入缓存
     */
    public Boolean insertNetCache(String key, String value, long loseTime) throws DBCacheException {
        String[] column = {URL, DATA, CREATTIME, LOSTTIME};
        String[] line = {key, value, System.currentTimeMillis() + "", loseTime + ""};
        if (key == null || value == null) {
            throw new NullPointerException(" key or value is null");
        }
        Boolean result = false;
        try {
            result = dbManager.insertData(TABLE, column, line);
        } catch (DBException e) {
            throw new DBCacheException("insertNetCache:" + e.getMessage());
        }
        if (result) {
            return true;
        } else {
            return false;
        }
    }
//    --------------------------------------------------------------------------------------------------------获取缓存数据

    /**
     * 获取缓存数据
     *
     * @return
     */
    public String getNetCache(String key) throws DBCacheException {
        String data = "";
        try {
            data = dbManager.getData(TABLE, URL, key, DATA);
        } catch (DBException e) {
            throw new DBCacheException("getNetCache:" + e.getMessage());
        }
        return data;
    }

    /**
     * 获取某个缓存的创建时间
     *
     * @return
     */
    public String getCreatTime(String key) throws DBCacheException {
        String table = TABLE;
        String result = "";
        try {
            result = dbManager.getData(TABLE, URL, key, CREATTIME);
        } catch (DBException e) {
            throw new DBCacheException("getCreatTime:" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取某个缓存的失效时间
     *
     * @return
     */
    public String getLoseTime(String key) throws DBCacheException {
        String result = "";
        try {
            result = dbManager.getData(TABLE, URL, key, LOSTTIME);
        } catch (DBException e) {
            throw new DBCacheException("getLoseTime:" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取数据库中存储的所有缓存时间
     *
     * @return
     */
    public List<String> getCreatTime() throws DBCacheException {
        List<String> list = new ArrayList<>();
        try {
            list = dbManager.getSelectData(TABLE, CREATTIME);
        } catch (DBException e) {
            throw new DBCacheException("getCreatTime:" + e.getMessage());
        }
        return list;
    }

    /**
     * 获取数据库中存储的所有失效时间
     *
     * @return
     */
    public List<String> getLoseTime() throws DBCacheException {
        List<String> list = new ArrayList<>();
        try {
            list = dbManager.getSelectData(TABLE, LOSTTIME);
        } catch (DBException e) {
            throw new DBCacheException("getLoseTime:" + e.getMessage());
        }
        return list;

    }


    //表格缓存的条数
    public long getNetCacheCount() throws DBCacheException {
        long result = 0;
        try {
            result = dbManager.getDataCount(TABLE);
        } catch (DBException e) {
            throw new DBCacheException("getNetCacheCount:" + e.getMessage());
        }
        return result;
    }

//    --------------------------------------------------------------------------------------------------------更新缓存数据

    /**
     * 更新缓存
     */
    public boolean updateNetCache(String key, String value, long loseTime) {
        if (key == null || value == null) {
            throw new NullPointerException("table is null");
        }
        String table = TABLE;
        //先更新时间再更新数据
        try {
            if (dbManager.update(table, URL, key, CREATTIME, System.currentTimeMillis() + "") &&
                    dbManager.update(table, URL, key, LOSTTIME, loseTime + "") && dbManager.update(table, URL, key, DATA, value)) {
                return true;
            } else {
                return false;
            }
        } catch (DBException e) {
            LogUtils.e(e.getMessage());
            return false;
        }
    }

    /**
     * 更新某项缓存的失效时间
     */
    public boolean updateLoseTime(String key, long loseTime) {
        if (key == null) {
            throw new NullPointerException("table is null");
        }
        String table = TABLE;
        //先更新时间再更新数据
        try {
            if (dbManager.update(table, URL, key, LOSTTIME, loseTime + "")) {
                return true;
            }
            return false;
        } catch (DBException e) {
            LogUtils.e(e.getMessage());
            return false;
        }
    }

//    --------------------------------------------------------------------------------------------------------获取缓存的大小

    /**
     * 获取单个数据库缓存的大小，结尾带B,KB，MB,TB
     *
     * @param context
     * @param dbName
     */
    public String getNetCacheSize(Context context, String dbName) {
        try {
            return FileManager.getAutoFileOrFilesSize(context.getDatabasePath(dbName).getAbsolutePath());
        } catch (IOFileException e) {
            LogUtils.e(e.getMessage());
            return "获取失败";
        }
    }

    /**
     * 获取单个数据库的大小带字节B
     *
     * @param context
     * @param dbName
     */
    public double getNetCacheSizeB(Context context, String dbName) {
        try {
            return FileManager.getFileOrFilesSize(context.getDatabasePath(dbName).getAbsolutePath());
        } catch (IOFileException e) {
            LogUtils.e(e.getMessage());
            return 0;
        }
    }

    /**
     * 获取多个数据库的大小
     *
     * @param context
     * @param dbName
     */
    public String getNetCacheSize(Context context, String[] dbName) {
        long size = 0;
        if (dbName == null || dbName.length <= 0) {
            return "0B";
        }
        for (int i = 0; i < dbName.length; i++) {
            size += getNetCacheSizeB(context, dbName[i]);
        }
        return FileManager.FormetFileSize(size);
    }

    /**
     * 获取所有数据库缓存的大小
     *
     * @param context
     */
    public String getNetCacheSize(Context context) {
        long size = 0;
        List<String> db = DBUtils.getDb(context);
        if (db == null || db.size() <= 0) {
            return "0B";
        }
        for (int i = 0; i < db.size(); i++) {
            size += getNetCacheSizeB(context, db.get(i));
        }
        return FileManager.FormetFileSize(size);
    }
//    --------------------------------------------------------------------------------------------------------清理缓存数据

    /**
     * 清理缓存  整行
     *
     * @param key
     */
    public void deleteNetCache(String key) throws DBCacheException {
        if (key == null) {
            throw new NullPointerException(" key is null");
        }
        String table = TABLE;
        String url = URL;
        try {
            dbManager.delete(table, url, key);
        } catch (DBException e) {
            throw new DBCacheException(e.getMessage());
        }
    }


    /**
     * 清理所有超时的缓存   要测试一下
     */
    public void deleteLoseTimeNetCache(final DeleteIsSucess deleteIsSucess) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> creatTime = null;
                List<String> loseTime = null;
                try {
                    creatTime = getCreatTime();
                    loseTime = getLoseTime();
                } catch (DBCacheException e) {
                    Message message = new Message();
                    message.obj = deleteIsSucess;
                    message.what = 2;
                    mHandler.handleMessage(message);
                    return;
                }

                String table = TABLE;
                String url = CREATTIME;
                if (creatTime != null && loseTime != null && creatTime.size() > 0 && loseTime.size() > 0) {
                    for (int i = 0; i < creatTime.size(); i++) {
                        LogUtils.e("创建失效时间===" + (System.currentTimeMillis() - Long.parseLong(creatTime.get(i))));
                        LogUtils.e("失效时间===" + Long.parseLong(loseTime.get(i)));
                        if ((System.currentTimeMillis() - Long.parseLong(creatTime.get(i))) > Long.parseLong(loseTime.get(i))) {
                            try {
                                dbManager.delete(table, url, creatTime.get(i));
                            } catch (DBException e) {
                                Message message = new Message();
                                message.obj = deleteIsSucess;
                                message.what = 1;
                                mHandler.handleMessage(message);
                                LogUtils.e(e.getMessage());
                                return;
                            }
                        }
                    }
                    Message message = new Message();
                    message.obj = deleteIsSucess;
                    message.what = 0;
                    mHandler.handleMessage(message);
                } else {
                    Message message = new Message();
                    message.obj = deleteIsSucess;
                    message.what = 2;
                    mHandler.handleMessage(message);
                    return;
                }

            }
        }).start();


    }


    /**
     * 清理单个数据库缓存
     */

    public void deleteDbSize(Context context, String dbName) {
        DBUtils.deleteDB(context, dbName);
    }

    /**
     * 清理所有数据库缓存
     */

    public void deleteDbSize(Context context) {
        List<String> db = DBUtils.getDb(context);
        if (db == null || db.size() <= 0) {
            return;
        }
        for (int i = 0; i < db.size(); i++) {
            DBUtils.deleteDB(context, db.get(i));
        }
    }


}
