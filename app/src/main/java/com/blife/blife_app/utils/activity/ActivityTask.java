package com.blife.blife_app.utils.activity;

import android.app.Activity;

import com.blife.blife_app.utils.logcat.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w on 2016/8/4.
 */
public class ActivityTask {

    private List<Activity> activityList;
    private List<Activity> ListActivity;
    private Activity currentActivity;
    private static ActivityTask activityTask;

    /**
     * 获取单利实例
     *
     * @return ActivityTask 实例
     */
    public static synchronized ActivityTask newInstance() {
        if (activityTask == null) {
            activityTask = new ActivityTask();
        }
        return activityTask;
    }

    private ActivityTask() {
        if (activityList == null) {
            activityList = new ArrayList<>();
        }
        if (ListActivity == null) {
            ListActivity = new ArrayList<>();
        }
    }

    /**
     * 添加Activity
     *
     * @param activity 当前Activity
     */
    public void addActivity(Activity activity) {
        if (activity != null) {
            if (currentActivity != null)
                currentActivity = null;
            currentActivity = activity;
            activityList.add(activity);
        }
    }

    /**
     * 指定当前Activity(onResume调用)
     *
     * @param activity 当前Activity
     */
    public void ActivityResume(Activity activity) {
        if (currentActivity != null)
            currentActivity = null;
        currentActivity = activity;
        for (Activity item : activityList) {
            L.e("TAG", "ITEM---:" + item.getClass().getName());
        }
    }

    /**
     * 获取当前Activity
     *
     * @return 当前Activity
     */
    public Activity getCurrentActivity() {
        return currentActivity;
    }

    /**
     * 结束多个activity
     */
    public void FinishAllAddActivity() {
        if (ListActivity.size() > 0) {
            for (Activity activity : ListActivity) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
            ListActivity.clear();
        }
    }

    public void ActivityDestroy(Activity activity) {
        if (activityList.size() > 0) {
            activityList.remove(activity);
            for (Activity item : activityList) {
                L.e("TAG", "ITEM---:" + item.getClass().getName());
            }
        }

    }

    public void addFinishActivity(Activity activity) {
        if (ListActivity != null) {
            ListActivity.add(activity);
        }
    }

    /**
     * 结束指定Activity
     *
     * @param activity 指定界面
     */
    public void finishActivity(Activity activity) {
        if (!activity.isFinishing()) {
            activity.finish();
        }
    }

    /**
     * 结束所有的Activity
     */
    public void finishAllActivity() {
        if (activityList != null && activityList.size() > 0) {
            for (Activity activity : activityList) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
            activityList.clear();
        }
    }

}
