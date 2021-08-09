package com.siyecaoi.yy.model;

import java.util.List;

public class StarAllBean {
    /**
     * {
     *         "code": 2021,
     *         "data": {
     *             "imgTx": "https://hw29115726.obs.cn-east-3.myhuaweicloud.com/admin/8c923fbd5613470283d277aa8e93bf37.jpg",
     *             "nickname": "这是昵称",
     *             "num": 8888,
     *             "type": 2,
     *             "rids":[
     *                  "11111",
     *                  "22222"
     *             ]
     *         }
     *     }
     */
    public int code;
    public DataBean data;

    public static class DataBean{
        public String imgTx;
        public String nickname;
        public String num;
        public String effect;
        public List<String> rids;
    }
}
