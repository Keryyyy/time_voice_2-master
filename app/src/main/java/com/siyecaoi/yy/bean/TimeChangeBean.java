package com.siyecaoi.yy.bean;

import java.util.List;

public class TimeChangeBean {
    public String msg;
    public int code;
    public String sys;
    public List<RecordBean> data;


    public static class RecordBean{
        public String imgTx;
        public String nickname;
        public int isLiang;
        public String usercoding;
        public String num;
    }
}
