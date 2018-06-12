package com.blife.blife_app.utils.cache;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.blife.blife_app.utils.db.newdb.NewDbManger;
import com.blife.blife_app.utils.db.newdb.sql.Sql;
import com.blife.blife_app.utils.db.newdb.sql.SqlFactory;
import com.blife.blife_app.utils.db.newdb.sql.WhereSql;
import com.blife.blife_app.utils.db.olddb.DBUtils;
import com.blife.blife_app.utils.exception.cacheException.DBCacheException;
import com.blife.blife_app.utils.exception.filexception.IOFileException;
import com.blife.blife_app.utils.file.FileManager;
import com.blife.blife_app.utils.util.LogUtils;

import java.util.List;

//储存表格的数据库缓存

/**
 * Created by Somebody on 2016/7/28.
 */
public class SQLiteCacheManager {
    public static final int OFFLINE_CACHE_LOSE_TIME = -1;
    public static final int FOREVER_CACHE_LOSE_TIME = 100 * 12 * 30 * 24 * 60 * 60 * 1000;
    private static SQLiteCacheManager netSQLiteCacheManager;
    private static NewDbManger dbManager;
    private String LOSTTIME = "loseTime";
    private String CREATTIME = "creatTime";
    private String CLEARTIME = "clearTime";
    private String URL = "url";
    private String DATA = "data";
    private String CACHETYPE = "cacheType";
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
    private SQLiteCacheManager() {

    }

    public static synchronized SQLiteCacheManager getInstance(Context context) {
        if (netSQLiteCacheManager == null) {
            netSQLiteCacheManager = new SQLiteCacheManager();
        }
        dbManager = NewDbManger.getInstance(context);
        return netSQLiteCacheManager;
    }

    /**
     * 插入缓存
     */
    public void insertNetCache(final String url, final String data, final long loseTime, final int cacheType) throws DBCacheException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long nowtime = System.currentTimeMillis();
                    List<CacheData> listNetCache = getListNetCache(url);
                    if (listNetCache != null && listNetCache.size() > 0) {
                        for (int i = 0; i < listNetCache.size(); i++) {
                            deleteNetCache(url);
                        }
                    }
                    CacheData cacheData = new CacheData(url, data, loseTime, nowtime, loseTime + nowtime, cacheType);
                    if (dbManager.insert(cacheData)) {
                    } else {
                        LogUtils.e("insert url  false" + url);
                    }
                } catch (DBCacheException e) {

                }


            }
        }).start();

    }
//    --------------------------------------------------------------------------------------------------------获取缓存数据

    /**
     * 获取缓存数据
     *
     * @return
     */
    public String getNetCache(String url) throws DBCacheException {
        String data = "";
        if (getClearTime(url) < System.currentTimeMillis() && getLoseTime(url) != OFFLINE_CACHE_LOSE_TIME) {
            deleteNetCache(url);
            return data;
        }
        Sql where = SqlFactory.find(CacheData.class).where(URL, "=", url);
        List<CacheData> datas = dbManager.executeQuery(where);
        if (datas != null && datas.size() > 0) {
            data = datas.get(0).getData();
        } else {
            data = "";
        }
        return data;
    }

    /**
     * 获取缓存数据
     *
     * @return
     */
    protected List<CacheData> getListNetCache(String url) throws DBCacheException {
        if (getClearTime(url) < System.currentTimeMillis()) {
            deleteNetCache(url);
            return null;
        }
        Sql where = SqlFactory.find(CacheData.class).where(URL, "=", url);
        List<CacheData> datas = dbManager.executeQuery(where);
        return datas;
    }

    /**
     * 获取某个缓存的创建时间
     *
     * @return
     */
    public long getCreatTime(String url) throws DBCacheException {
        long result = 0;
        Sql where = SqlFactory.find(CacheData.class).where(URL, "=", url);
        List<CacheData> datas = dbManager.executeQuery(where);
        if (datas != null && datas.size() > 0) {
            result = datas.get(0).getCreatTime();
        } else {
            result = 0;
        }
        return result;
    }

    /**
     * 获取某个缓存的失效时间
     *
     * @return
     */
    public long getLoseTime(String url) throws DBCacheException {
        long result = 0;
        Sql where = SqlFactory.find(CacheData.class).where(URL, "=", url);
        List<CacheData> datas = dbManager.executeQuery(where);
        if (datas != null && datas.size() > 0) {
            result = datas.get(0).getLoseTime();
        } else {
            result = 0;
        }
        return result;
    }

    /**
     * 获取某个缓存的清理时间
     *
     * @return
     */
    public long getClearTime(String url) throws DBCacheException {
        long result = 0;
        Sql where = SqlFactory.find(CacheData.class).where(URL, "=", url);
        List<CacheData> datas = dbManager.executeQuery(where);
        if (datas != null && datas.size() > 0) {
            result = datas.get(0).getClearTime();
        } else {
            result = 0;
        }
        return result;
    }

    /**
     * 获取数据库中存储的所有缓存实体
     *
     * @return
     */
    public List<CacheData> getData() throws DBCacheException {
        List<CacheData> cacheDatas = dbManager.findAll(CacheData.class);
        if (cacheDatas != null && cacheDatas.size() > 0) {
            return cacheDatas;
        } else {
            return null;
        }
    }


    //表格缓存的条数
    public long getNetCacheCount() throws DBCacheException {
        long result = 0;
        result = dbManager.count(CacheData.class);
        return result;
    }

//    --------------------------------------------------------------------------------------------------------更新缓存数据

    /**
     * 更新缓存
     */
    public boolean updateNetCache(String url, String data, long loseTime, int cachetype) {
        long nowtime = System.currentTimeMillis();
        WhereSql sql = SqlFactory.update(CacheData.class, new String[]{DATA, LOSTTIME, CREATTIME, CLEARTIME, CACHETYPE}, new Object[]{data, loseTime, nowtime, loseTime + nowtime, cachetype}).where(URL, "=", url);
        boolean execute = dbManager.execute(sql);
        return execute;
    }

    /**
     * 更新某项缓存的失效时间
     */
    public boolean updateLoseTime(String url, long loseTime) {
        long nowtime = System.currentTimeMillis();
        WhereSql sql = SqlFactory.update(CacheData.class, new String[]{LOSTTIME, CLEARTIME}, new Object[]{loseTime, loseTime + nowtime}).where(URL, "=", url);
        boolean execute = dbManager.execute(sql);
        return execute;
    }

//    --------------------------------------------------------------------------------------------------------获取缓存的大小

    public String getCacheSize(Context context) {
        return getNetCacheSize(context, NewDbManger.DEFAULT_DBNAME);
    }

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
     * @param url
     */
    public Boolean deleteNetCache(String url) throws DBCacheException {
        WhereSql sql = SqlFactory.delete(CacheData.class).where(URL, "=", url);
        boolean execute = dbManager.execute(sql);
        return execute;
    }

    /**
     * 清理所有超时的缓存
     */
    public void deleteLoseTimeNetCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WhereSql sql = SqlFactory.delete(CacheData.class).where(CLEARTIME, "<", System.currentTimeMillis()).and(CACHETYPE, ">=", 0);
                boolean execute = dbManager.execute(sql);
            }
        }).start();
    }


    /**
     * 清理所有超时的缓存   要测试一下
     */
    public void deleteLoseTimeNetCache(final DeleteIsSucess deleteIsSucess) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WhereSql sql = SqlFactory.delete(CacheData.class).where(CLEARTIME, "<", System.currentTimeMillis()).and(CACHETYPE, ">=", 0);
                boolean execute = dbManager.execute(sql);
                if (execute) {
                    Message message = new Message();
                    message.obj = deleteIsSucess;
                    message.what = 0;
                    mHandler.sendMessageAtTime(message, 3000);
                } else {
                    Message message = new Message();
                    message.obj = deleteIsSucess;
                    message.what = 1;
                    mHandler.sendMessageAtTime(message, 3000);
                    return;
                }

            }
        }).start();
    }

    /**
     * 清理所有的缓存   要测试一下
     */
    public void deleteAllNetCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WhereSql sql = SqlFactory.delete(CacheData.class).where(CLEARTIME, ">", 0).and(CACHETYPE, ">=", 0);
                boolean execute = dbManager.execute(sql);
            }
        }).start();
    }


    /**
     * 清理所有的缓存   要测试一下
     */
    public void deleteAllNetCache(final DeleteIsSucess deleteIsSucess) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WhereSql sql = SqlFactory.delete(CacheData.class).where(CLEARTIME, ">", 0).and(CACHETYPE, ">=", 0);
                boolean execute = dbManager.execute(sql);
                if (execute) {
                    Message message = new Message();
                    message.obj = deleteIsSucess;
                    message.what = 0;
                    mHandler.sendMessageAtTime(message, 3000);
                } else {
                    Message message = new Message();
                    message.obj = deleteIsSucess;
                    message.what = 1;
                    mHandler.sendMessageAtTime(message, 3000);
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
        netSQLiteCacheManager.deleteAllNetCache(new DeleteIsSucess() {
            @Override
            public void deleteSucess() {

            }

            @Override
            public void deletFiled() {

            }

            @Override
            public void deletError() {

            }
        });

    }

    /**
     * 清理所有数据库缓存，清理完再插入需要重新创建数据库
     */

    public void deleteDbSize(Context context) {
        List<String> db = DBUtils.getDb(context);
        if (db == null || db.size() <= 0) {
            return;
        }
        for (int i = 0; i < db.size(); i++) {
            DBUtils.deleteDB(context, db.get(i));
        }
        netSQLiteCacheManager.deleteAllNetCache(new DeleteIsSucess() {
            @Override
            public void deleteSucess() {

            }

            @Override
            public void deletFiled() {

            }

            @Override
            public void deletError() {

            }
        });
    }


}
