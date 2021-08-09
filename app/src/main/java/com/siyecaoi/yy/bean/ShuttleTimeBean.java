package com.siyecaoi.yy.bean;

public class ShuttleTimeBean {
    public String msg;
    public int code;
    public ShuttleTimeData data;
    public String sys;

    public static class ShuttleTimeData{
        public int shuttleNum;
        public int shuttleGold;
        public int isShow;
    }
}
