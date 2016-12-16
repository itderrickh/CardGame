package com.itderrickh.cardgame.helpers;

/**
 * Created by derrickheinemann on 12/15/16.
 */

public class Score {
    private int userid;
    private int score;

    public Score(int userid, int score) {
        this.userid = userid;
        this.score = score;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
