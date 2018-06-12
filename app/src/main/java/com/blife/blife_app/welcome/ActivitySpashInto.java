package com.blife.blife_app.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.login.activity.ActivityPasswordLogin;

import java.util.ArrayList;
import java.util.List;

public class ActivitySpashInto extends Activity {

    ViewPager mViewPager;
    public boolean misScrolled;
    //导航页图片资源
    private ImageView dots_1, dots_2, dots_3;
    private TextView navi_into;
    private RelativeLayout rl_splsh_out;
    private int isInto = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_into);
        mViewPager = (ViewPager) findViewById(R.id.viewFlipper1);
        rl_splsh_out = (RelativeLayout) findViewById(R.id.rl_splsh_out);

        dots_1 = (ImageView) findViewById(R.id.dots_1);
        dots_2 = (ImageView) findViewById(R.id.dots_2);
        dots_3 = (ImageView) findViewById(R.id.dots_3);
        navi_into = (TextView) findViewById(R.id.navi_into);
        initWithPageGuideMode();
        initClick();
    }


    private void initWithPageGuideMode() {

        List<View> list = new ArrayList<View>();

        ImageView img0 = new ImageView(this);
        ImageView img1 = new ImageView(this);
        ImageView img2 = new ImageView(this);

        img0.setImageResource(R.mipmap.navi_first);
        img0.setScaleType(ImageView.ScaleType.FIT_XY);
        img1.setImageResource(R.mipmap.navi_two);
        img1.setScaleType(ImageView.ScaleType.FIT_XY);
        img2.setImageResource(R.mipmap.navi_three);
        img2.setScaleType(ImageView.ScaleType.FIT_XY);

        list.add(img0);
        list.add(img1);
        list.add(img2);

        ViewPagerGuideAdapter adapter = new ViewPagerGuideAdapter(list);

        mViewPager.setAdapter(adapter);

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                if (arg0 == 0) {
                    dots_1.setBackgroundResource(R.mipmap.navi_arrow);
                    dots_2.setBackgroundResource(R.mipmap.navi_no_dots);
                    dots_3.setBackgroundResource(R.mipmap.navi_no_dots);
                    navi_into.setVisibility(View.GONE);
                } else if (arg0 == 1) {
                    dots_1.setBackgroundResource(R.mipmap.navi_no_dots);
                    dots_2.setBackgroundResource(R.mipmap.navi_arrow);
                    dots_3.setBackgroundResource(R.mipmap.navi_no_dots);
                    navi_into.setVisibility(View.GONE);
                }
                if (arg0 == 2) {
                    dots_1.setBackgroundResource(R.mipmap.navi_no_dots);
                    dots_2.setBackgroundResource(R.mipmap.navi_no_dots);
                    dots_3.setBackgroundResource(R.mipmap.navi_now_dots);
                    AnimationSet animationSet = new AnimationSet(true);
                    //创建一个AlphaAnimation对象，参数从完全的透明度，到完全的不透明
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                    //设置动画执行的时间
                    alphaAnimation.setDuration(1000);
                    //将alphaAnimation对象添加到AnimationSet当中
                    animationSet.addAnimation(alphaAnimation);
                    //使用ImageView的startAnimation方法执行动画
                    navi_into.startAnimation(animationSet);
                    navi_into.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        //正在滑动   pager处于正在拖拽中
                        misScrolled = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        //pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程
                        misScrolled = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        //空闲状态  pager处于空闲状态
                        misScrolled = true;
                        break;
                }
            }
        });
    }

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Intent intent = new Intent(ActivitySpashInto.this, ActivityPasswordLogin.class);
                startActivity(intent);
                finish();
            }
        }
    };

    private void initClick() {
        navi_into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInto == 0) {
                    AnimationSet animationSet = new AnimationSet(true);
                    //创建一个AlphaAnimation对象，参数从完全的透明度，到完全的不透明
                    AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                    //设置动画执行的时间
                    alphaAnimation.setDuration(1500);
                    Animation scaleAnimation = new
                            ScaleAnimation(1f, 2f, 1f, 2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    //将alphaAnimation对象添加到AnimationSet当中
                    scaleAnimation.setDuration(1500);
                    animationSet.addAnimation(alphaAnimation);
                    animationSet.addAnimation(scaleAnimation);
                    //使用ImageView的startAnimation方法执行动画
                    rl_splsh_out.startAnimation(animationSet);
                    mHandler.sendEmptyMessageDelayed(0, 1000);
                    isInto++;
                }

//                overridePendingTransition( R.animator.alpa_out,R.drawable.scale_enter);//切换Activity的过渡动画
            }
        });
    }

    class ViewPagerGuideAdapter extends PagerAdapter {

        // 界面列表
        private List<View> views;

        public ViewPagerGuideAdapter(List<View> views) {
            this.views = views;
        }

        // 销毁position位置的界面
        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(views.get(position));
        }

        // 获得当前界面数
        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }

            return 0;
        }

        // 初始化position位置的界面
        @Override
        public Object instantiateItem(View view, int position) {

            ((ViewPager) view).addView(views.get(position), 0);

            return views.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }

}































