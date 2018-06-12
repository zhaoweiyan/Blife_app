package com.blife.blife_app.tools.rollviewpager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blife.blife_app.R;

public class TurnImagePager {
    private Context context;

    //轮播图对应点放置的线性布局
    private List<String> list;
    //放置图片链接地址的集合
    private List<String> imgUrlList = new ArrayList<String>();
    //定义放置view对象的集合
    private List<View> dotList = new ArrayList<View>();

    private View layout_roll_view;

    private LinearLayout top_news_viewpager;
    private LinearLayout dots_ll;

    private InterfaceTurnImageItemClick itemClick;

    public TurnImagePager(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }

    public View initView() {
        //1,引入轮播图所在的布局文件(包含了轮播图所在线性布局,点,文字)
        layout_roll_view = View.inflate(context, R.layout.layout_roll_view, null);
        top_news_viewpager = (LinearLayout) layout_roll_view.findViewById(R.id.top_news_viewpager);
        dots_ll = (LinearLayout) layout_roll_view.findViewById(R.id.dots_ll);
        return layout_roll_view;
    }

    public void initData() {
        processData();
    }

    private void processData() {
        imgUrlList = list;
        //点的构建过程
        initDot();

        //构建独立管理轮播图展示逻辑的RollViewPager
        RollViewPager rollViewPager = new RollViewPager(context, dotList);
        //设置轮播图需要用到的图片
        rollViewPager.initImageUrl(imgUrlList);
        //构建轮播图以及轮播图滚动方法
        rollViewPager.roll();

        rollViewPager.setOnViewClickListener(new RollViewPager.OnViewClickListener() {
            @Override
            public void onViewClick(int position) {
                if (itemClick != null) {
                    itemClick.onItemClick(position);
                }
                //点击事件的具体的业务逻辑
            }
        });
        //轮播图的对象,需要添加到轮播图所在的线性布局当中
        top_news_viewpager.removeAllViews();
        top_news_viewpager.addView(rollViewPager);
    }

    public void setItemClick(InterfaceTurnImageItemClick itemClick) {
        this.itemClick = itemClick;
    }

    private void initDot() {
        //图片有几张,点就有几个,对应的点需要放置到点所在的线性布局dots_ll中去
        dots_ll.removeAllViews();
        dotList.clear();
        if(imgUrlList.size()>0){
            for (int i = 0; i < imgUrlList.size(); i++) {
                View view = new View(context);
                if (i == 0) {
                    //构建第一个点,将第一个点默认设置成高亮的
                    view.setBackgroundResource(R.mipmap.round_solid);
                } else {
                    //其余的点都是非高亮显示
                    view.setBackgroundResource(R.mipmap.round_trans);
                }
                //1,view对象宽高为多少,因为view在线性布局内部,所以宽高规则由线性布局提供
                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(15, 15);
                //2,每一个view对象间间距
                layoutParams.setMargins(0, 0, 6, 0);

                //3,线性布局添加view的过程,需要带上以上提供出来的规则
                dots_ll.addView(view, layoutParams);
                dotList.add(view);
            }
        }

    }
}
