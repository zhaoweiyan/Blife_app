package com.blife.blife_app.utils.sort;

import java.util.Comparator;

/**
 * Created by w on 2016/8/12.
 */
public class ClassComparator implements Comparator<SortInterface> {

    public static final int STRING = 1;
    public static final int INT = 2;
    private int TYPE;

    private ClassComparator() {
    }

    public ClassComparator(int type) {
        this.TYPE = type;
    }


    @Override
    public int compare(SortInterface lhs, SortInterface rhs) {
        if (TYPE == STRING) {
//            return ((String) lhs.getSortKey()).compareTo(((String) rhs.getSortKey()));
            return SortUtils.converterToSpell((String) lhs.getSortKey()).compareTo(SortUtils.converterToSpell((String) rhs.getSortKey()));
        }
        if (TYPE == INT) {
            int l = ((int) lhs.getSortKey());
            int r = ((int) rhs.getSortKey());
            if (l > r) return 1;
            if (l < r) return -1;
        }
        return 0;
    }
}
