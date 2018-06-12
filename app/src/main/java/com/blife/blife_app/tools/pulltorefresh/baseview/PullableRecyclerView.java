package com.blife.blife_app.tools.pulltorefresh.baseview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by w on 2016/6/18.
 */
public class PullableRecyclerView extends RecyclerView implements Pullable {

    private boolean canRefresh = true;
    private boolean canLoader = true;

    public PullableRecyclerView(Context context) {
        super(context);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        if (canRefresh) {
            if (getAdapter().getItemCount() == 0) {
                // 没有item的时候也可以下拉刷新
                return true;
            } else if (((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition() == 0
                    && getChildAt(0).getTop() >= 0) {
                // 滑到ListView的顶部了
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    @Override
    public boolean canPullUp() {
        if (canLoader) {
            if (getAdapter().getItemCount() == 0) {
                // 没有item的时候也可以上拉加载
                return false;
            } else if (((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition() == (getAdapter().getItemCount() - 1)) {
                // 滑到底部了
                if (getChildAt(((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition() -
                        ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition()) != null
                        && getChildAt(
                        ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition()
                                - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition()).getBottom()
                        <= getMeasuredHeight()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 设置是否可执行刷新操作
     *
     * @param flag
     */
    public void setCanRefresh(boolean flag) {
        canRefresh = flag;
    }

    /**
     * 设置是否可执行加载操作
     *
     * @param flag
     */
    public void setCanLoader(boolean flag) {
        canLoader = flag;
    }
}
