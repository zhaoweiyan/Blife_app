package com.blife.blife_app.tools.pulltorefresh.pulltorefreshview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ListAdapter;

import com.blife.blife_app.R;
import com.blife.blife_app.tools.pulltorefresh.baseview.PullToRefreshLayout;
import com.blife.blife_app.tools.pulltorefresh.baseview.PullableListView;

/**
 * Created by Administrator on 2015/12/17.
 */
public class PullToRefreshListView extends PullToRefreshLayout {

    private PullableListView listView;
    private Context mContext;

    public PullToRefreshListView(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle, Context mContext) {
        super(context, attrs, defStyle);
        this.mContext = mContext;
        initView(context);
    }

    public void initView(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.pulltorefresh_listview, this);
        listView = (PullableListView) findViewById(R.id.content_view);
    }

    public void setAdapter(ListAdapter adapter) {
        if (listView != null) {
            listView.setAdapter(adapter);
        }
    }

    /**
     * 获取ListView示例
     *
     * @return PullableListView
     */
    public PullableListView getListView() {
        if (listView != null) {
            return listView;
        }
        return null;
    }

    public void refreshFinish() {
        refreshFinish(SUCCEED);
    }

    public void loadFinish() {
        loadmoreFinish(SUCCEED);
    }

    public void setCanRefresh(boolean flag) {
        if (listView != null)
            listView.setCanRefresh(flag);
    }

    public void setCanLoadMore(boolean flag) {
        if (listView != null)
            listView.setCanLoader(flag);
    }
}
