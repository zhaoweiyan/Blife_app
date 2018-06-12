package com.blife.blife_app.utils.sort;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by w on 2016/8/3.
 */
public class SortUtils {

    public static List<SortInterface> sort(List<SortInterface> list) {
        List<SortInterface> mList = new ArrayList<>();
        if (list == null) {
            return mList;
        }
        if (list.size() < 0) {
            return list;
        }
        int type = -1;
        SortInterface sortInterface = list.get(0);
        Object obj = sortInterface.getSortKey();
        if (obj == null) {
            return list;
        }
        if (obj instanceof String) {
            if (TextUtils.isEmpty(((String) obj))) {
                return list;
            }
            type = ClassComparator.STRING;
        }
        if (obj instanceof Integer) {
            type = ClassComparator.INT;
        }
        if (type < 0) {
            return list;
        }
        ClassComparator comparator = new ClassComparator(type);
        Collections.sort(list, comparator);
        for (SortInterface sort : list) {
            mList.add(sort);
        }
        return mList;
    }


    public static List<Map<String, Object>> sort(List<Map<String, Object>> mapList, String key) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (mapList == null) {
            return list;
        }
        if (TextUtils.isEmpty(key)) {
            return mapList;
        }
        if (mapList.size() < 0) {
            return mapList;
        }
        List<SortInterface> beanList = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            Object value = map.get(key);
            SortBean sortBean = new SortBean();
            sortBean.setKey(value);
            sortBean.setObject(map);
            beanList.add(sortBean);
        }
        List<SortInterface> resultList = sort(beanList);
        for (SortInterface bean : resultList) {
            list.add((Map<String, Object>) bean.getObject());
        }
        return list;
    }

    public static void sortString(List<String> list) {
        Collections.sort(list, new StringComparator());
    }

    public static void sortInteger(List<Integer> list) {
        Collections.sort(list, new IntegerComparator());
    }

    /**
     * 汉字转换位汉语拼音，英文字符不变
     *
     * @param chines 汉字
     * @return 拼音
     */
    public static String converterToSpell(String chines) {
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }

}
