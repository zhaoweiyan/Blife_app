package com.blife.blife_app.utils.util;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by zhao on 2016/8/5.
 */
public class NumberUtils {
    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;


    //已Double类型做的工具，如果需要，可以再写一个已float类型做的工具

    //这个类不能实例化
    private NumberUtils() {
    }
    /** */
    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
    /** */
    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }
    /** */
    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
    /** */
    /**
     * 提供精确的乘法运算。解决5.02*100出现得数位501.999999994的情况
     *
     * @param v1 被乘数
     * @return 两个参数的积
     */
    public static long mul(String v1) {
        String numerPoint = StringUtils.isNumerPoint(v1);
        BigDecimal f1 = new BigDecimal(numerPoint);
        BigDecimal f2 = BigDecimal.valueOf(100);
        String s1 = f1.multiply(f2) + "";
        String[] split = s1.split("\\.");
        return Long.parseLong(split[0]);
    }

    /** */
    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /** */
    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    //保留两个小数点
    public static String getTwoPoint(String number) {
        try {
            if (number.contains(",")) {
                number = number.replace(",", "");
            }
            if (number.contains("元")) {
                number = number.replace("元", "");
            }
            BigDecimal f1 = new BigDecimal(number.trim());
            BigDecimal f2 = BigDecimal.valueOf(100);
            String numberValue = String.format("%.2f", f1.divide(f2));
            return numberValue;
        } catch (Exception e) {
            return "0";
        }
    }


    /** */
    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        return NumberUtils.div(v, 1, scale);
    }

    //返回小数点的位数
    public static int getRoundNumber(double v) {
        BigDecimal b = new BigDecimal(Double.toString(v));
        return b.scale();
    }

    //返回比较值  返回值    -1 小于   0 等于    1 大于
    public static int getCompareToNumber(Double v1, Double v2) throws NullPointerException {
        BigDecimal a = new BigDecimal(Double.toString(v1));
        BigDecimal b = new BigDecimal(Double.toString(v2));
        return a.compareTo(b);
    }

    //需要转换成什么样的类型值，只需要bigDecimal.intValue或者bigDecimal.doubleValue即可
    public static Double bigDecimalToDouble(BigDecimal bigDecimal) {
        return bigDecimal.doubleValue();
    }

    //返回最大值
    public static Double getCompareToMax(Double v1, Double v2) {
        BigDecimal a = new BigDecimal(Double.toString(v1));
        BigDecimal b = new BigDecimal(Double.toString(v2));
        return a.max(b).doubleValue();
    }

    //返回最小值
    public static Double getCompareToMin(Double v1, Double v2) {
        BigDecimal a = new BigDecimal(Double.toString(v1));
        BigDecimal b = new BigDecimal(Double.toString(v2));
        return a.min(b).doubleValue();
    }

    //返回绝对值
    public static Double getCompareToAbs(Double v) {
        BigDecimal a = new BigDecimal(Double.toString(v));
        return a.abs().doubleValue();
    }

    //返回相反数
    public static Double getCompareToNegate(Double v) {
        BigDecimal a = new BigDecimal(Double.toString(v));
        return a.negate().doubleValue();
    }

    /**
     * 获取随机数
     *
     * @param length
     * @return
     */
    public static String getRandom(int length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            result += ((int) (Math.random() * 10)) + "";
        }
        return result;
    }

}
