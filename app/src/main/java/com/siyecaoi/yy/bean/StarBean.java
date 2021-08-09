package com.siyecaoi.yy.bean;

import java.util.ArrayList;

public class StarBean {
    public String msg;
    public int code;
    public String sys;
    public StarDataBean data;

    public static class StarDataBean{
        public ArrayList<LuckBean> luckList;
        public ArrayList<HeadImgBean> headImgList;
        public int coherence;
        public int isTakePartIn;
        public int peopleNum;
        public long countdown;
        public long countdown0;
        public String gold;
        public long logGoldTotal;
        public long logGold;
    }

    public static class LuckBean{
        public int coherence;
        public int logGoldTotal;
        public String imgTx;
        public String nickname;
        public String liang;
        public String usercoding;
    }

    public static class HeadImgBean{
        public int isWin;
        public String lotteryHeadImg;

        public HeadImgBean(){}

        public HeadImgBean(int isWin, String lotteryHeadImg) {
            this.isWin = isWin;
            this.lotteryHeadImg = lotteryHeadImg;
        }
    }
}
