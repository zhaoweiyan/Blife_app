package com.blife.blife_app.index.bean;

        import java.util.List;

/**
 * Created by Somebody on 2016/8/25.
 */
public class BeanHomeTurnImagesList {

    private List<BeanHomeTurnImages> list;

    public BeanHomeTurnImagesList(List<BeanHomeTurnImages> beanHomeTurnImagesList) {
        this.list = beanHomeTurnImagesList;
    }

    public BeanHomeTurnImagesList() {
    }

    public List<BeanHomeTurnImages> getBeanHomeTurnImagesList() {
        return list;
    }

    public void setBeanHomeTurnImagesList(List<BeanHomeTurnImages> beanHomeTurnImagesList) {
        this.list = beanHomeTurnImagesList;
    }

    @Override
    public String toString() {
        return "BeanHomeTurnImagesList{" +
                "beanHomeTurnImagesList=" + list +
                '}';
    }
}
