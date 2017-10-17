package com.ascend.wangfeng.setbyble;

import java.util.Arrays;

/**
 * Created by fengye on 2017/6/22.
 * email 1040441325@qq.com
 */

public class SetBean {
    public static final String YES="YES";
    public static final String NO="NO";
    private int type;
    private String hd;
    private String bd;
    private String[] list;
    private Boolean hasDialog =false;

    public Boolean getHasDialog() {
        return hasDialog;
    }

    public void setHasDialog(Boolean hasDialog) {
        this.hasDialog = hasDialog;
    }

    public SetBean() {
    }

    public SetBean(String hd, String bd) {
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

    @Override
    public String toString() {
        return "SetBean{" +
                "type=" + type +
                ", hd='" + hd + '\'' +
                ", bd='" + bd + '\'' +
                ", list=" + Arrays.toString(list) +
                '}';
    }
}
