package com.siyecaoi.yy.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/2/11.
 */

public class RandomNumberBean implements Serializable {

    /**
     * msg :
     * code : 1
     * data : List[Integer]
     * sys : 1518317997925
     */

    private String msg;
    private int code;
    private ArrayList<Integer> data;
    private long sys;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<Integer> getData() {
        return data;
    }

    public void setData(ArrayList<Integer> data) {
        this.data = data;
    }

    public long getSys() {
        return sys;
    }

    public void setSys(long sys) {
        this.sys = sys;
    }



}
