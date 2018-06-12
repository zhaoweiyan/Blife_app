package com.blife.blife_app.tools.rollviewpager;

import java.lang.reflect.Field;
import java.util.List;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.tools.BitmapHelp;
import com.blife.blife_app.utils.cache.ImageCache;
import com.blife.blife_app.utils.net.Imageloader.ImageLoader;
import com.blife.blife_app.utils.util.LogUtils;
import com.lidroid.xutils.BitmapUtils;

public class RollViewPager extends ViewPager {
    //放置轮播图图片的集合
    private List<String> imgUrlList;
    //放置轮播图文字的集合
    private List<String> titleList;
    //放置轮播图文字的控件
//    private TextView top_news_title;

    private MyPagerAdapter myPagerAdapter;

    private int currentPosition = 0;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (currentPosition == 0) {
                RollViewPager.this.setCurrentItem(currentPosition, false);
//                RollViewPager.this.setCurrentItem(currentPosition);
            } else {
                RollViewPager.this.setCurrentItem(currentPosition);
            }

            roll();
        }
    };
    private RunnableTask runnableTask;

    private OnViewClickListener onViewClickListener;
    private int downX;
    private int downY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取按下的事件,点中位置
                //在按下的时候,轮播图所在父控件不能去拦截事件,如果拦截则轮播图不能去做轮转操作
                getParent().requestDisallowInterceptTouchEvent(true);
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                //移动的事件
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                if (Math.abs(moveX - downX) > Math.abs(moveY - downY)) {
                    //X轴的偏移量大于Y轴偏移量,轮播图轮转,模块翻转
                    //由右向左滑动(moveX-downX<0)
                    if (moveX - downX < 0 && getCurrentItem() == getAdapter().getCount() - 1) {
                        //告诉自己，不允许打断事件，是的
                        requestDisallowInterceptTouchEvent(true);
                    } else if (moveX - downX < 0 && getCurrentItem() < getAdapter().getCount() - 1) {
                        //告诉自己，不允许打断事件，是的
                        requestDisallowInterceptTouchEvent(true);
                    }
                    //由左向右滑动(moveX-downX>0)
                    else if (moveX - downX > 0 && getCurrentItem() == 0) {
                        //告诉自己，不允许打断事件，是的
                        requestDisallowInterceptTouchEvent(true);
                    } else if (moveX - downX > 0 && getCurrentItem() > 0) {
                        //轮播图图片翻转图片,父控件就不需要去拦截事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                } else {
                    //告诉父控件，不允许打断事件是false
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) ev.getX();
                int upY = (int) ev.getY();
                if (Math.abs(upX - downX) > Math.abs(upY - downY)) {
                    //X轴的偏移量大于Y轴偏移量,轮播图轮转,模块翻转
                    //由右向左滑动(moveX-downX<0)
                    if (upX - downX < 0 && getCurrentItem() == getAdapter().getCount() - 1&&imgUrlList.size()>0) {
                        currentPosition = (currentPosition + 1) % imgUrlList.size();
                        RollViewPager.this.setCurrentItem(currentPosition, false);
                    } else if (upX - downX < 0 && getCurrentItem() < getAdapter().getCount() - 1&&imgUrlList.size()>0) {
                        currentPosition = (currentPosition + 1) % imgUrlList.size();
                        RollViewPager.this.setCurrentItem(currentPosition);
                    }
                    //由左向右滑动(moveX-downX>0)
                    else if (upX - downX > 0 && getCurrentItem() == 0) {
                        currentPosition = imgUrlList.size() - 1;
                        RollViewPager.this.setCurrentItem(currentPosition, false);
                    } else if (upX - downX > 0 && getCurrentItem() > 0&&imgUrlList.size()>0) {
                        currentPosition = (currentPosition - 1) % imgUrlList.size();
                        RollViewPager.this.setCurrentItem(currentPosition);
                    }
                }

                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    //当界面移除出去的时候，调用的方法
    @Override
    protected void onDetachedFromWindow() {
        //将handler维护的任务以及消息，移除掉
        handler.removeCallbacksAndMessages(null);
        super.onDetachedFromWindow();
    }

    public RollViewPager(Context context, final List<View> dotList) {
        super(context);
        runnableTask = new RunnableTask();
        //当轮播图图片发生改变的时候,点选中的图片需要替换,描述文字需要修改
        setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                //文字去做替换
//                top_news_title.setText(titleList.get(arg0));
                //选中点需要去做状态的切换
                if(dotList.size()>0){
                    for (int i = 0; i < dotList.size(); i++) {
                        View view = dotList.get(i);
                        if (i == arg0) {
                            view.setBackgroundResource(R.mipmap.round_solid);
                        } else {
                            view.setBackgroundResource(R.mipmap.round_trans);
                        }
                    }
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    //定义一个接口
    public interface OnViewClickListener {
        //未实现的方法
        void onViewClick(int position);
    }

    //传递一个实现了接口的类的对象进来
    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }

//    //初始化轮播图文字,以及显示文字控件
//    public void initTitle(List<String> titleList, TextView top_news_title) {
//        if (titleList != null && top_news_title != null && titleList.size() > 0) {
//            top_news_title.setText(titleList.get(0));
//
//            this.titleList = titleList;
//            this.top_news_title = top_news_title;
//        }
//    }

    //接受轮播图图片所在的集合
    public void initImageUrl(List<String> imgUrlList) {
        this.imgUrlList = imgUrlList;
//        currentPosition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imgUrlList.size();
    }

    class RunnableTask implements Runnable {
        public void run() {
            //0,1,2,3,0,1,2,3
            if(imgUrlList.size()>0){
                currentPosition = (currentPosition + 1) % imgUrlList.size();
                LogUtils.e("");
                //然viewpager向后翻页的过程
                //发送了一个空消息
                handler.obtainMessage().sendToTarget();
            }
        }

        ;
    }


    public void roll() {
        //1,给当前的自定义的viewpager设置数据适配器
        if (myPagerAdapter == null) {
            myPagerAdapter = new MyPagerAdapter();
            setAdapter(myPagerAdapter);
            LogUtils.e("轮播%%%%");
        } else {
//            LogUtils.e("轮播**");
            myPagerAdapter.notifyDataSetChanged();
        }
        //2,让viewpager中的图片按一定的时间间隔,滚动起来,handler维护一个延时的任务
        handler.postDelayed(runnableTask, 3000);
    }

    class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imgUrlList.size();
//            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            //将ImageView所在的布局文件转换成view对象,并且添加到viewpager内部
            View view = View.inflate(getContext(), R.layout.viewpager_item, null);
            ImageView image = (ImageView) view.findViewById(R.id.image);
//            //image需要去放置一张网络连接地址指向的图片
            BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils(getContext());
            bitmapUtils.display(image, imgUrlList.get(position));
//            bitmapUtils.display(image, imgUrlList.get(position % imgUrlList.size()));
//            ImageLoader.getInstance().loadImage(imgUrlList.get(position), image, true);
           /**ViewPager和内部view的交互的过程
            //在view上面按下不移动就做抬起(按下事件,抬起事件)
            //1,按下的事件,首先有ViewPager捕获,捕获完成后将此事件传递给内部的view,view在获取按下事件后,就去做相应
            //2,抬起的事件,首先由ViewPager捕获,捕获完成后将此事件传递给内部的view,view在获取抬起事件后,也去做相应

            //在view上面按下移动再抬起(按下事件,移动事件,抬起事件)
            //1,按下的事件,首先由ViewPager捕获,捕获完成后将此事件传递给内部的view,view在获取按下事件后,就去做相应

            //2,移动是一个连续的过程,所以移动事件会多次触发,移动事件首先由ViewPager捕获,然后传递给内部的view,
            //view在接受到了这个事件以后,开始去做响应,直到移动距离达到一定值,或者达到一定加速度的时候,view就不再响应移动事件了
            //,转而触发对应View的Action_cancel的事件,一旦Action_cancel事件被触发,view就再也不响应后续的事件了.

            //3,后续的事件包含了移动已经抬起动作,因为view不响应移动以及抬起,所以将后续的事件,都还原给夫控件去做响应,
            即还原给了viewpager做响应
            */
            view.setOnTouchListener(new OnTouchListener() {
                private int downX;
                private int downTime;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //1,从哪里点下,哪里抬起(按下点的坐标和抬起点的坐标一致)
                    //2,按下抬起的时间差应该是小于一个定值(500毫秒)
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            downX = (int) event.getX();
                            downTime = (int) System.currentTimeMillis();
                            //手指点中轮播图中某一张图片的事件,图片就不去做翻转
                            handler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_UP:
                            int upX = (int) event.getX();
                            int upTime = (int) System.currentTimeMillis();
                            if (upX == downX && upTime - downTime < 500) {
                                //满足点击事件的触发条件,点击事件的业务逻辑操作
                                //当前的自定义控件要给多个用户去使用,每一个用户在点击完轮播图后做的逻辑操作都不一致,
                                //所以需要将业务逻辑放置到具体使用自定义控件的用户手上去做编写(回调)
                                //1,首先编写一个接口
                                //2,接口内编写一个未实现的业务逻辑方法
                                //3,传递一个实现了接口的类(实现之前的业务逻辑方法)的对象进来
                                //4,拿到实现了接口的类的对象,去调用已经实现好的业务逻辑方法
                                if (onViewClickListener != null) {
                                    onViewClickListener.onViewClick(position);
                                }
//                                LogUtils.e("up 点击事件");
                            }
                            roll();
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            roll();
                            break;

                    }
                    return true;
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
