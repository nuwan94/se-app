package me.nuwan.seofficial.Model;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class Person implements Comparable {

    private String sno;
    private String name;
    private String mob;
    private String dob;
    private String img;
    private String lat,lang;
    private String gpa;

    public Person(String sno, String name, String mob, String dob, String img, String lat, String lang, String gpa) {
        this.sno = sno;
        this.name = name;
        this.mob = mob;
        this.dob = dob;
        this.img = img;
        this.lat = lat;
        this.lang = lang;
        this.gpa = gpa;
    }

    public Person() {
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return Integer.parseInt(this.sno) - Integer.parseInt(((Person)o).getSno());
    }
}
