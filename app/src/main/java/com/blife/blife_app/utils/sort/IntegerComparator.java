package com.blife.blife_app.utils.sort;

import java.util.Comparator;

/**
 * Created by w on 2016/8/3.
 */
public class IntegerComparator implements Comparator<Integer> {

    @Override
    public int compare(Integer lhs, Integer rhs) {
        if (lhs > rhs) return 1;
        if (lhs < rhs) return -1;
        return 0;
    }

}
