package com.itderrickh.cardgame.helpers;

/**
 * Created by derrickheinemann on 12/9/16.
 */
public class Bid {
    private int userid;
    private int value;

    public Bid(int userid, int value) {
        this.userid = userid;
        this.value = value;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
