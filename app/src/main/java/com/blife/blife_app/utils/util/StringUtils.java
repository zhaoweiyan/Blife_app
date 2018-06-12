package com.blife.blife_app.utils.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/7/31.
 */
public class StringUtils {
    //将新字符串放进数组
    public static String[] insert(String[] arr, String str) {
        int size = arr.length;

        String[] tmp = new String[size + 1];

        System.arraycopy(arr, 0, tmp, 0, size);

        tmp[size] = str;

        return tmp;
    }

    //字符串是否为空
    public static boolean isImpty(String str) {
        if (str == null || str.length() == 0 || TextUtils.isEmpty(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 在给定的字符串中，用新的字符替换所有旧的字符
     *
     * @param string  给定的字符串
     * @param oldchar 旧的字符
     * @param newchar 新的字符
     * @return 替换后的字符串
     */
    public static String replace(String string, char oldchar, char newchar) {
        char chars[] = string.toCharArray();
        for (int w = 0; w < chars.length; w++) {
            if (chars[w] == oldchar) {
                chars[w] = newchar;
            }
        }
        return new String(chars);
    }

    /**
     * 把给定的字符串用给定的字符分割(字符串含有这个分割的字符)
     *
     * @param string 给定的字符串
     * @param ch     给定的字符
     * @return 分割后的字符串数组
     */
    public static String[] split(String string, char ch) {
        ArrayList<String> stringList = new ArrayList<String>();
        char chars[] = string.toCharArray();
        int nextStart = 0;
        for (int w = 0; w < chars.length; w++) {
            if (ch == chars[w]) {
                stringList.add(new String(chars, nextStart, w - nextStart));
                nextStart = w + 1;
                if (nextStart ==
                        chars.length) {    //当最后一位是分割符的话，就再添加一个空的字符串到分割数组中去
                    stringList.add("");
                }
            }
        }
        if (nextStart <
                chars.length) {    //如果最后一位不是分隔符的话，就将最后一个分割符到最后一个字符中间的左右字符串作为一个字符串添加到分割数组中去
            stringList.add(new String(chars, nextStart,
                    chars.length - 1 - nextStart + 1));
        }
        return stringList.toArray(new String[stringList.size()]);
    }

    /**
     * 截取并保留标志位之前的字符串
     *
     * @param str
     * @param expr 分隔符
     * @return
     */
    public static String substringBefore(String str, String expr) {
        if (isImpty(str) || expr == null) {
            return str;
        }
        if (expr.length() == 0) {
            return "";
        }
        int pos = str.indexOf(expr);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * 截取并保留标志位之后的字符串
     *
     * @param str
     * @param expr 分隔符
     * @return
     */
    public static String substringAfter(String str, String expr) {
        if (isImpty(str)) {
            return str;
        }
        if (expr == null) {
            return "";
        }
        int pos = str.indexOf(expr);
        if (pos == -1) {
            return "";
        }
        return str.substring(pos + expr.length());
    }

    /**
     * 截取并保留最后一个标志位之前的字符串
     *
     * @param str
     * @param expr 分隔符
     * @return
     */
    public static String substringBeforeLast(String str, String expr) {
        if (isImpty(str) || isImpty(expr)) {
            return str;
        }
        int pos = str.lastIndexOf(expr);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * 截取并保留最后一个标志位之后的字符串
     *
     * @param str
     * @param expr 分隔符
     * @return
     */
    public static String substringAfterLast(String str, String expr) {
        if (isImpty(str)) {
            return str;
        }
        if (isImpty(expr)) {
            return "";
        }
        int pos = str.lastIndexOf(expr);
        if (pos == -1 || pos == (str.length() - expr.length())) {
            return "";
        }
        return str.substring(pos + expr.length());
    }

    /**
     * 把字符串的中间n位用“*”号代替
     *
     * @param str 要代替的字符串
     * @return
     */

    public static String replaceMidString(String str, int n) {
        String sub = "";
        String houSub = "";
        if (str == null || str.length() <= n || str.length() == 0) {
            return "*";
        }
        if (n == 0) {
            return str;
        }
        int above = (str.length() - n) / 2;
        try {
            sub = str.substring(0, above);
            if (above + n >= str.length()) {
                houSub = str.substring(0, str.length());
            } else {
                houSub = str.substring(above + n, str.length());
            }
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < n; i++) {
                sb = sb.append("*");
            }
            sub = sub + sb.toString() + houSub;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sub;
    }

    /**
     * 把字符串的后n位用“*”号代替
     *
     * @param str 要代替的字符串
     * @param n   代替的位数
     * @return
     */

    public static String replaceBelowString(String str, int n) {
        String sub = "";
        if (str == null || str.length() < n || str.length() == 0) {
            return "*";
        }
        if (n == 0) {
            return str;
        }
        try {
            sub = str.substring(0, str.length() - n);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < n; i++) {
                sb = sb.append("*");
            }
            sub += sb.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sub;
    }

    /**
     * 把n位后的字符串用“*”号代替
     *
     * @param str 要代替的字符串
     * @param n   代替的位数
     * @return
     */

    public static String replaceStringBelow(String str, int n) {
        String sub = "";
        if (str == null || str.length() < n || str.length() == 0) {
            return "*";
        }
        if (n == 0) {
            return str;
        }
        try {
            sub = str.substring(0, n);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < str.length() - n; i++) {
                sb = sb.append("*");
            }
            sub += sb.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sub;
    }


    //保留两个小数点
    public static String getTwoPoint(String number) {
        try {
            if (number.contains(",")) {
                number = number.replace(",", "");
            }
            number = StringUtils.getSubnumber(number);
            BigDecimal f1 = new BigDecimal(number.trim());
            BigDecimal f2 = BigDecimal.valueOf(100);
            String numberValue = String.format("%.2f", f1.divide(f2));
            return numberValue;
        } catch (Exception e) {
            return "0";
        }
    }

    //截取所有的数字
    public static String getNumber(String str) {
        str = str.trim();
        String str2 = "";
        if (str != null && !"".equals(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    str2 += str.charAt(i);
                }
            }
        }
        return str2;
    }

    //截取字符串都是数字，只能找到正数
    public static String isNumerPlus(String str) {
        if (str == null) {
            return "";
        }
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher isNum = pattern.matcher(str);

        return isNum.replaceAll("").trim();
    }

    //截取字符串中的数字,不能单独提取,同上
    public static String getSubnumber(String str) {
        if (str == null) {
            return "";
        }
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str.trim());
        return m.replaceAll("").trim();
    }

    //截取字符串都是数字，正数可带小数点
    public static String isNumerPoint(String str) {
        Pattern pattern = Pattern.compile("[^(.)0-9]");
        Matcher isNum = pattern.matcher(str);
        return isNum.replaceAll("").trim();
    }


    //判断是否大于100元
    public static boolean isMoreThousand(String number) {
        number = StringUtils.isNumerPoint(number);
        try {
            int value = Integer.parseInt(number.trim());
            if (value >= 100 * 100) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    // 正则表达式校验真实姓名
    public static boolean regularRealName(String realName) {
        Pattern p = Pattern.compile("^([\\u4e00-\\u9fa5]+|([a-zA-Z]+\\s?)+)$");
        Matcher m = p.matcher(realName);
        boolean temp = m.matches();
        return temp;
    }

    // 正则表达式校验手机号
    public static boolean regularTel(String tel) {
//        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Pattern p = Pattern.compile("\\d{11}$");  //电话号码的验证，11位数字
        Matcher m = p.matcher(tel);
        boolean temp = m.matches();
        return temp;
    }

    // 正则表达式校验数字字母
    public static boolean regularNumberPwd(String tel) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9]{6,20}$");
        Matcher m = p.matcher(tel);
        boolean temp = m.matches();
        return temp;
    }

    public static Handler showInputHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            EditText view = (EditText) msg.obj;
            view.requestFocus();
            view.setSelection(view.getText().toString().length());
            InputMethodManager im = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.showSoftInput(view, 0);
        }
    };

    /**
     * 弹出软键盘
     *
     * @param et
     */
    public static void showSoftInput(EditText et) {
        if (et != null) {
            et.setFocusable(true);
            et.setFocusableInTouchMode(true);
            et.requestFocus();
            Message msg = new Message();
            msg.obj = et;
            showInputHandler.sendMessageDelayed(msg, 400);
        }
    }

    /**
     * 验证邮箱格式
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    //验证网址格式
    public static boolean isHttp(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (url.contains("http://") ||
                url.contains("https://")
                        && url.indexOf("http://") == 0 ||
                url.indexOf("https://") == 0) {
            return true;
        }
        return false;
    }

    public static SpannableStringBuilder setDiffColorText(String[] texts, int[] colors) {
        String text = "";
        for (String s : texts) {
            text = text + s;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        int current = 0;
        for (int i = 0; i < texts.length; i++) {
            //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(colors[i]);
            builder.setSpan(foregroundColorSpan, current, current + texts[i].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            current = current + texts[i].length();
        }
        return builder;
    }

    public static SpannableStringBuilder setDiffColorText(String text, int[][] index, int[] colors) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        for (int i = 0; i < colors.length; i++) {
            //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(colors[i]);
            builder.setSpan(foregroundColorSpan, index[i][0], index[i][1], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    public static String dealMoney(String money, int tag) {
        return dealMoney(money, tag, 2);
    }

    public static String dealMoney(String money, int tag, int scale) {
        if (TextUtils.isEmpty(money)) {
            return "0.00";
        }
        if (tag <= 0) {
            return money;
        }
        if (tag % 10 != 0) {
            return money;
        }
        boolean flag = true;
        int num = tag;
        while (flag) {
            num = num / 10;
            if (num < 10) {
                flag = false;
            }
        }
        if (num != 1) {
            return money;
        }
        int t = (tag + "").substring(1).length();
        int len = money.length();
        StringBuilder builder = new StringBuilder();
        if (len <= t) {
            builder.append("0.");
            for (int i = 0; i < (t - len); i++) {
                builder.append("0");
            }
            builder.append(money);
        } else {
            String m1 = money.substring(0, len - t);
            String m2 = money.substring(len - t, len - t + scale);
            builder.append(m1);
            builder.append(".");
            builder.append(m2);
        }
        return builder.toString();
    }

    public static String hideText(String text, int hideStart, int hideLength) {
        StringBuffer stringBuffer = new StringBuffer();
        if (hideStart >= text.length()) {
            return text;
        }
        stringBuffer.append(text.substring(0, hideStart));
        if (text.length() > hideStart + hideLength) {
            for (int i = 0; i < hideLength; i++) {
                stringBuffer.append("*");
            }
            stringBuffer.append(text.substring(hideStart + hideLength));
        } else {
            for (int i = 0; i < text.length() - hideStart; i++) {
                stringBuffer.append("*");
            }
        }
        return stringBuffer.toString();
    }

    public static boolean isNum(String text) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(text);
        if (m.matches()) {
            return true;
        }
        return false;
    }

    public static boolean isEnglish(String text) {
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(text);
        if (m.matches()) {
            return true;
        }
        return false;
    }

}
