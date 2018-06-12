package com.blife.blife_app.tools.pulltorefresh.pulltorefreshview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.blife.blife_app.R;
import com.blife.blife_app.tools.pulltorefresh.baseview.PullToRefreshLayout;
import com.blife.blife_app.tools.pulltorefresh.baseview.PullableRecyclerView;


/**
 * Created by Administrator on 2015/12/17.
 */
public class PullToRefreshRecyclerView extends PullToRefreshLayout {

    private PullableRecyclerView recyclerView;
    private Context mContext;

    public PullToRefreshRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public void initView(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.pulltorefresh_recyclerview, this);
        recyclerView = (PullableRecyclerView) findViewById(R.id.content_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (recyclerView != null) {
            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * 获取ListView示例
     *
     * @return PullableRecyclerView
     */
    public PullableRecyclerView getListView() {
        if (recyclerView != null) {
            return recyclerView;
        }
        return null;
    }

    /**
     * 操作结束
     */
    public void Finish() {
        if (recyclerView == null)
            return;
        if (state == REFRESHING) {
            refreshFinish(SUCCEED);
        } else if (state == LOADING) {
            loadmoreFinish(SUCCEED);
        } else {
            refreshFinish(SUCCEED);
            loadmoreFinish(SUCCEED);
        }
    }

    /**
     * 操作结束
     */
    public void FinishFailed() {
        if (recyclerView == null)
            return;
        if (state == REFRESHING) {
            refreshFinish(FAIL);
        } else if (state == LOADING) {
            loadmoreFinish(FAIL);
        } else {
            refreshFinish(FAIL);
            loadmoreFinish(FAIL);
        }
    }

    public void setCanRefresh(boolean flag) {
        if (recyclerView != null)
            recyclerView.setCanRefresh(flag);
    }

    public void setCanLoadMore(boolean flag) {
        if (recyclerView != null)
            recyclerView.setCanLoader(flag);
    }
}
