package com.blife.blife_app.utils.sort;

import java.util.Comparator;

/**
 * Created by w on 2016/8/3.
 */
public class StringComparator implements Comparator<String> {

    @Override
    public int compare(String lhs, String rhs) {
        return SortUtils.converterToSpell(lhs).compareTo(SortUtils.converterToSpell(rhs));
    }
}
