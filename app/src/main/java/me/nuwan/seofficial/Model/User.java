package me.nuwan.seofficial.Model;

/**
 * Created by NSA94 on 02/03/2018.
 */

public class User {
    public static User currentUser;

    private String dob,gpa,img,lang,lat,mob,name,sno;

    public User() {
    }

    public User(String dob, String gpa, String img, String lang, String lat, String mob, String name, String sno) {
        this.dob = dob;
        this.gpa = gpa;
        this.img = img;
        this.lang = lang;
        this.lat = lat;
        this.mob = mob;
        this.name = name;
        this.sno = sno;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }
}
