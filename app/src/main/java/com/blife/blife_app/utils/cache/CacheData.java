package com.blife.blife_app.utils.cache;


import com.blife.blife_app.utils.db.newdb.annotations.Id;
import com.blife.blife_app.utils.db.newdb.annotations.Table;

import java.io.Serializable;

/**
 * Created by Somebody on 2016/8/1.
 */
@Table(name = "MMMM")
public class CacheData implements Serializable {
    @Id
    private int id;
    private int cacheType;
    private String url;
    private String data;
    private Long loseTime;
    private Long creatTime;
    private Long clearTime;

    public CacheData() {
    }

    public CacheData(String url, String data, Long loseTime, Long creatTime, Long clearTime, int cacheType) {
        this.url = url;
        this.data = data;
        this.loseTime = loseTime;
        this.creatTime = creatTime;
        this.clearTime = clearTime;
        this.cacheType = cacheType;
    }

    public CacheData(int id, String url, String data, Long loseTime, Long creatTime, Long clearTime) {
        this.id = id;
        this.url = url;
        this.data = data;
        this.loseTime = loseTime;
        this.creatTime = creatTime;
        this.clearTime = clearTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getLoseTime() {
        return loseTime;
    }

    public void setLoseTime(Long loseTime) {
        this.loseTime = loseTime;
    }

    public Long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Long creatTime) {
        this.creatTime = creatTime;
    }

    public Long getClearTime() {
        return clearTime;
    }

    public void setClearTime(Long clearTime) {
        this.clearTime = clearTime;
    }

    public int getCacheType() {
        return cacheType;
    }

    public void setCacheType(int cacheType) {
        this.cacheType = cacheType;
    }

    @Override
    public String toString() {
        return "CacheData{" +
                "cacheType=" + cacheType +
                ", id=" + id +
                ", url='" + url + '\'' +
                ", data='" + data + '\'' +
                ", loseTime=" + loseTime +
                ", creatTime=" + creatTime +
                ", clearTime=" + clearTime +
                '}';
    }
}
