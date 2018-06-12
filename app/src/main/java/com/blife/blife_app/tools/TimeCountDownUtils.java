package com.blife.blife_app.tools;

import android.os.Handler;
import android.os.Message;

import com.blife.blife_app.application.BlifeApplication;
import com.blife.blife_app.tools.interfaceUtil.InterfaceTimeCountDown;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.logcat.LogcatManager;

/**
 * Created by w on 2016/9/7.
 */
public class TimeCountDownUtils {
    private static TimeCountDownUtils INSTANCE;
    private long StartTime, EndTime, CurrentTime;
    private Handler UIHandler;
    private InterfaceTimeCountDown interfaceTimeCountDown;
    private final int TIME_PROGRESS = 1;
    private final int TIME_END = 2;
    private static boolean isRunning = false;

    private long result_second = 0, result_minute = 0, result_hour = 0, result_day = 0;

    private TimeCountDownUtils() {
        UIHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case TIME_PROGRESS:
                        LogcatManager.e(BlifeApplication.AppContext, "SUPERBONUS", "倒计时进行中..." + result_day + "--" + result_hour + "--" + result_minute + "--" + result_second);
                        if (interfaceTimeCountDown != null) {
                            interfaceTimeCountDown.time(result_day, result_hour, result_minute, result_second);
                        }
                        break;
                    case TIME_END:
                        LogcatManager.e(BlifeApplication.AppContext, "SUPERBONUS", "倒计时结束" + result_day + "--" + result_hour + "--" + result_minute + "--" + result_second);
                        UIHandler.removeCallbacks(runnable);
                        break;
                }
            }
        };
    }

    public static TimeCountDownUtils getInstance() {
        if (INSTANCE == null) {
            isRunning = false;
            INSTANCE = new TimeCountDownUtils();
        }
        return INSTANCE;
    }

    public void setTime(long startTime, long endTime, long currentTime) {
        this.StartTime = startTime;
        this.EndTime = endTime;
        this.CurrentTime = currentTime;
    }

    public void start(InterfaceTimeCountDown interfaceTimeCountDown) {
        LogcatManager.e(BlifeApplication.AppContext, "SUPERBONUS", "倒计时开始");
        this.interfaceTimeCountDown = interfaceTimeCountDown;
        if (CurrentTime <= 0 || EndTime <= 0 || StartTime <= 0) {
            UIHandler.sendEmptyMessage(TIME_PROGRESS);
        }
        if (StartTime <= CurrentTime && CurrentTime < EndTime) {//进行中
            if (!isRunning) {
                UIHandler.post(runnable);
            }
        } else {
            if (CurrentTime < StartTime) {//未开始
                dealTime(EndTime - CurrentTime);
                UIHandler.postDelayed(runnable, StartTime - CurrentTime);
            } else {//已结束
                dealTime(0);
            }
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isRunning = true;
            countDown();
        }
    };

    /**
     * 倒计时
     */
    private void countDown() {
        long timeSecond = EndTime - CurrentTime;
        if (timeSecond > 0) {
            dealTime(timeSecond);
            CurrentTime++;
            UIHandler.removeCallbacks(runnable);
            UIHandler.postDelayed(runnable, 1000);
        } else {
            dealTime(timeSecond);
            UIHandler.sendEmptyMessage(TIME_END);
        }

    }

    /**
     * 处理时间
     *
     * @param second
     */
    private void dealTime(long second) {
        //279500
        result_second = second % 60;//20
        if (result_second < 0) {
            result_second = 0;
        }
        long minute = second / 60;
        if (minute > 0) {
            result_minute = minute % 60;//38
            long hour = minute / 60;
            if (hour > 0) {
                result_hour = hour % 24;//5
                long day = hour / 24;
                if (day > 0) {
                    result_day = day;//3
                } else {
                    result_day = 0;
                }
            } else {
                result_hour = 0;
            }
        } else {
            result_minute = 0;
        }
        UIHandler.sendEmptyMessage(TIME_PROGRESS);
    }

}
