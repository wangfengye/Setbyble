package com.ascend.wangfeng.setbyble;

/**
 * Created by fengye on 2017/6/22.
 * email 1040441325@qq.com
 */

public class SetBean {
    public static final int TYPE_ONE_TO_ONE=0;
    public static final int TYPE_ONE_TO_MORE=1;
    private int type;
    private String hd;
    private String bd;
    private String[] list;

    public SetBean() {
    }

    public SetBean(int type, String hd, String bd) {
        this.type = type;
        this.hd = hd;
        this.bd = bd;
    }

    public SetBean(int type, String hd, String[] list) {
        this.type = type;
        this.hd = hd;
        this.list = list;
    }

    public String getHd() {
        return hd;
    }

    public void setHd(String hd) {
        this.hd = hd;
    }

    public String getBd() {
        return bd;
    }

    public void setBd(String bd) {
        this.bd = bd;
    }

    public String[] getList() {
        return list;
    }

    public void setList(String[] list) {
        this.list = list;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
