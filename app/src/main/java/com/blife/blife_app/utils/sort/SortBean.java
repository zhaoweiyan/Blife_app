package com.blife.blife_app.utils.sort;

/**
 * Created by w on 2016/8/3.
 */
public class SortBean implements SortInterface {

    private Object Key;
    private Object object;

    public void setKey(Object key) {
        Key = key;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public Object getSortKey() {
        return Key;
    }

    @Override
    public Object getObject() {
        return object;
    }

}
