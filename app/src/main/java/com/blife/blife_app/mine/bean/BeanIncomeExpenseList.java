package com.blife.blife_app.mine.bean;

import java.util.List;

/**
 * Created by Somebody on 2016/9/19.
 */
public class BeanIncomeExpenseList {
    private List<BeanIncomeExpense> list;

    public BeanIncomeExpenseList() {
    }

    public BeanIncomeExpenseList(List<BeanIncomeExpense> list) {
        this.list = list;
    }

    public List<BeanIncomeExpense> getList() {
        return list;
    }

    public void setList(List<BeanIncomeExpense> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "BeanIncomeExpenseList{" +
                "list=" + list +
                '}';
    }
}
