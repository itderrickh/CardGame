package com.itderrickh.cardgame.helpers;

public class GameUser {
    private String email;
    private int id;
    private int gameid;
    private int userid;

    public GameUser(String email, int id, int gameid, int userid) {
        this.email = email;
        this.id = id;
        this.gameid = gameid;
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
