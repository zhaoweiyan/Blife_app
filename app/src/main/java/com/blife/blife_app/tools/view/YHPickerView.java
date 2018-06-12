package com.blife.blife_app.tools.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Administrator on 2015/11/23.
 */
public class YHPickerView extends LinearLayout {

    private PickerItemView pv_year;
    private PickerItemView pv_month;
    private PickerItemView pv_day;
    private PickerItemView pv_hour;

    private List<Integer> list_year = new ArrayList<Integer>();
    private List<Integer> list_month = new ArrayList<Integer>();
    private List<Integer> list_day = new ArrayList<Integer>();
    private List<Integer> list_hour = new ArrayList<Integer>();

    private int[] month1 = {1, 3, 5, 7, 8, 10, 11};// 每月31天
    private int[] month2 = {4, 6, 9, 12};// 每月30天

    private int START_YEAR = 1990;
    private int END_YEAR = 2038;
    private int MONTH = 12;
    private int DAY = 31;

    private int HOUR = 23;

    private String currYear = "1990";
    private String currMonth = "01";
    private String currDay = "01";
    private String currHour = "00";

    private onScrollListener mScrollListener;

    // 是否显示双位数
    private boolean isDoubleNum = true;
    // 滑动年份与月份，日期是否回归1
    private boolean isDayToOne = false;

    public YHPickerView(Context context) {
        super(context);
        init(context);
    }

    public YHPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);
        initData();
        initView(context);

    }

    // 初始化视图
    private void initView(Context context) {
        pv_year = new PickerItemView(context, "年");
        pv_month = new PickerItemView(context, "月");
        pv_day = new PickerItemView(context, "日");
        pv_hour = new PickerItemView(context, "时");

        setDate(pv_year, LL(list_year));
        setDate(pv_month, LL(list_month));
        setDateInit(currYear, currMonth);
        setDate(pv_day, LL(list_day));
        setDate(pv_hour, LL(list_hour));
        addView(pv_year);
        addView(pv_month);
        addView(pv_day);
        addView(pv_hour);

        initListener();
    }

    // 测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        for (int index = 0; index < getChildCount(); index++) {
            View view = getChildAt(index);
            view.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() / getChildCount(), MeasureSpec.getMode(widthMeasureSpec)),
                    MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.getMode(heightMeasureSpec)));
        }
    }

    // 设置数据
    private void setDate(PickerItemView pickerView, List<Integer> list) {
        if (list != null && list.size() > 0) {
            pickerView.setData(TT(list));
        }
    }

    // 初始化数据
    private void initData() {
        // 年份
        for (int i = START_YEAR; i <= END_YEAR; i++) {
            list_year.add(i);
        }
        // 月份
        for (int i = 1; i <= MONTH; i++) {
            list_month.add(i);
        }
        // 日期
        for (int i = 1; i <= DAY; i++) {
            list_day.add(i);
        }
        // 小时
        for (int i = 0; i <= HOUR; i++) {
            list_hour.add(i);
        }
    }

    // 数组排序（后半段与前半段交换位置）
    public List<Integer> LL(List<Integer> list) {
        List<Integer> L = new ArrayList<Integer>();
        int all = list.size();
        int item = all / 2;
        if (all % 2 == 1) {
            item = item + 1;
        }
        for (int i = item; i < all; i++) {
            L.add(list.get(i));
        }
        for (int i = 0; i < item; i++) {
            L.add(list.get(i));
        }
        return L;
    }

    // 生成天数列表
    private void initDayList() {
        list_day.clear();
        for (int i = 1; i <= DAY; i++) {
            list_day.add(i);
        }
        setDate(pv_day, LL(list_day));
    }

    public void setDayToOne(boolean flag) {
        isDayToOne = flag;
    }

    // 根据年份和月份生成天数列表
    private void initMD(int year, int month) {
        if (isLeapYear(year)) {
            initMonth_Day(month, 29);
        } else {
            initMonth_Day(month, 28);
        }
        initDayList();
        if (!isDayToOne) {
            if (DAY > toInteger(currDay)) {
                adapterData(pv_day, currDay, list_day);
            } else {
                currDay = toString(DAY);
                adapterData(pv_day, toString(DAY), list_day);
            }
        } else {
            if (isDoubleNum) {
                currDay = "01";
            } else {
                currDay = "1";
            }
        }
    }

    // 根据月份生成当月的天数
    private void initMonth_Day(int month, int day) {
        if (month == 2) {
            DAY = day;
        } else if (is31(month)) {
            DAY = 31;
        } else if (is30(month)) {
            DAY = 30;
        }
    }

    // 判断每月是否为31天
    private boolean is31(int month) {
        for (int i = 0; i < month1.length; i++) {
            if (month == month1[i]) {
                return true;
            }
        }
        return false;
    }

    // 判断每月是否为30天
    private boolean is30(int month) {
        for (int i = 0; i < month2.length; i++) {
            if (month == month2[i]) {
                return true;
            }
        }
        return false;
    }

    // 是否为闰年
    private boolean isLeapYear(int year) {
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    // int数组转换String数组
    private List<String> TT(List<Integer> list) {
        List<String> L = new ArrayList<String>();
        for (int temp : list) {
            L.add(toDouble(temp));
        }
        return L;
    }

    public void setDoubleNum(boolean flag) {
        isDoubleNum = flag;
    }

    // 生成双位数
    public String toDouble(int num) {
        String DoubleNum;
        if (isDoubleNum) {
            if (num < 10) {
                DoubleNum = "0" + num;
            } else {
                DoubleNum = String.valueOf(num);
            }
        } else {
            DoubleNum = toString(num);
        }
        return DoubleNum;
    }

    // 字符串转换int
    private int toInteger(String text) {
        if (!TextUtils.isEmpty(text)) {
            return Integer.valueOf(text);
        }
        return 0;
    }

    private String toString(int i) {
        if (i >= 0) {
            return "" + i;
        }
        return "";
    }

    // PickerView监听
    private void initListener() {
        pv_year.setOnSelectListener(new PickerItemView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                currYear = text;
                initMD(toInteger(currYear), toInteger(currMonth));
                currSelect();
            }
        });
        pv_month.setOnSelectListener(new PickerItemView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                currMonth = text;
                initMD(toInteger(currYear), toInteger(currMonth));
                currSelect();
            }
        });
        pv_day.setOnSelectListener(new PickerItemView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                currDay = text;
                currSelect();
            }
        });
        pv_hour.setOnSelectListener(new PickerItemView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                currHour = text;
                currSelect();
            }
        });
    }

    private void currSelect() {
        if (mScrollListener != null) {
            mScrollListener.onScroll(currYear, currMonth, currDay, currHour);
        }
    }

    public void setOnScrollListener(onScrollListener scrollListener) {
        mScrollListener = scrollListener;
    }

    public interface onScrollListener {
        void onScroll(String year, String month, String day, String hour);
    }

    public void setData(String year, String month, String day, String hour) {
        currYear = year;
        currMonth = month;
        currDay = day;
        currHour = hour;
        adapterData(pv_year, currYear, list_year);
        adapterData(pv_month, currMonth, list_month);
        setDateInit(year, month);
        adapterData(pv_day, currDay, list_day);
        adapterData(pv_hour, currHour, list_hour);
    }

    public void viewCurrentData() {
        Calendar calendar = Calendar.getInstance();  //获取当前时间
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        setData(toDouble(year), toDouble(month), toDouble(day), toDouble(hour));
    }

    public void viewCurrentAddHour(int addHour) {
        Calendar calendar = Calendar.getInstance();  //获取当前时间
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = (calendar.get(Calendar.HOUR_OF_DAY) + addHour);
        setData(toDouble(year), toDouble(month), toDouble(day), toDouble(hour));
    }

    // 数组根据传入的值，重新排序
    private void adapterData(PickerItemView pickerView, String text, List<Integer> list) {
        List<String> L = new ArrayList<String>();
        int cur = 0;
        int len = list.size();
        int middle = len / 2;
        int num = Integer.valueOf(text);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == num) {
                cur = i;
                break;
            }
        }
        if (cur < middle) {
            if (len % 2 == 1) {
                middle = middle + 1;
            }
            for (int i = cur + middle; i < len; i++) {
                L.add(toDouble(list.get(i)));
            }
            for (int i = 0; i < cur; i++) {
                L.add(toDouble(list.get(i)));
            }
            for (int i = cur; i < cur + middle; i++) {
                L.add(toDouble(list.get(i)));
            }
        } else if (cur > middle) {
            for (int i = cur - middle; i < cur; i++) {
                L.add(toDouble(list.get(i)));
            }
            for (int i = cur; i < len; i++) {
                L.add(toDouble(list.get(i)));
            }
            for (int i = 0; i < cur - middle; i++) {
                L.add(toDouble(list.get(i)));
            }
        } else if (cur == middle) {
            L = TT(list);
        }
        pickerView.setData(L);
    }

    private void setDateInit(String year, String month) {

        if (isLeapYear(toInteger(year))) {
            initMonth_Day(toInteger(month), 29);
        } else {
            initMonth_Day(toInteger(month), 28);
        }
        list_day.clear();
        for (int i = 1; i <= DAY; i++) {
            list_day.add(i);
        }
    }

    public String getCurrentYear() {
        return currYear;
    }

    public String getCurrentMonth() {
        return currMonth;
    }

    public String getCurrentDay() {
        return currDay;
    }

    public String getCurrentHour() {
        return currHour;
    }

}
