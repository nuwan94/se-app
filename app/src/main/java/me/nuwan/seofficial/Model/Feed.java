package me.nuwan.seofficial.Model;

import android.content.Intent;
import android.support.annotation.NonNull;

import me.nuwan.seofficial.R;

public class Feed {

    private String title, by, desc, type, uid, time;

    public Feed() {
    }

    public Feed(String title, String desc, String type, String by, String uid, String time) {
        this.title = title;
        this.by = by;
        this.desc = desc;
        this.type = type;
        this.uid = uid;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public int getImage() {
        int i;
        switch (type) {
            case "alert":
                i = R.mipmap.alarm;
                break;
            case "note":
                i = R.mipmap.open_book;
                break;
            default:
                i = R.mipmap.se_logo;
        }
        return i;
    }

    public void setType(String type) {
        this.type = type;
    }

}
