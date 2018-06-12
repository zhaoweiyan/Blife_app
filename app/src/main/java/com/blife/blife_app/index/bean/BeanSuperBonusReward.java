package com.blife.blife_app.index.bean;

/**
 * Created by w on 2016/10/18.
 */
public class BeanSuperBonusReward {

    private BeanSuperBonusReward reward_info;
    private boolean joined;
    private int reward_amount;

    public BeanSuperBonusReward getReward_info() {
        return reward_info;
    }

    public void setReward_info(BeanSuperBonusReward reward_info) {
        this.reward_info = reward_info;
    }

    public boolean isJoined() {
        return joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    public int getReward_amount() {
        return reward_amount;
    }

    public void setReward_amount(int reward_amount) {
        this.reward_amount = reward_amount;
    }
}
