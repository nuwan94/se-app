package me.nuwan.seofficial.Model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by NSA94 on 02/03/2018.
 */

public class User implements Serializable {
    public static User currentUser;

    private String dob, gpa, img, lang, lat, mob, name, sno, type;

    public User() {
    }

    public User(String dob, String gpa, String img, String lang, String lat, String mob, String name, String sno, String type) {
        this.dob = dob;
        this.gpa = gpa;
        this.img = img;
        this.lang = lang;
        this.lat = lat;
        this.mob = mob;
        this.name = name;
        this.sno = sno;
        this.type = type;
    }

    public String getDob() {
        return dob;
    }

    public String FormattedDob() {
        return Common.birthdayFormat.format(new Date(Long.parseLong(getDob())));
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

    public String ShortName() {
        String[] nameArr = getName().split(" ");
        return (nameArr.length > 1) ? nameArr[0] + " " + nameArr[1] : nameArr[0];
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int dayCount() {

        if (!this.getDob().equals("")) {
            Calendar today = Calendar.getInstance();
            Calendar birthday = Calendar.getInstance();
            birthday.setTime(new Date(Long.parseLong(this.getDob())));
            birthday.set(Calendar.YEAR,
                    (today.get(Calendar.DAY_OF_YEAR) < birthday.get(Calendar.DAY_OF_YEAR)) ? today.get(Calendar.YEAR) : today.get(Calendar.YEAR) + 1);

            if (today.get(Calendar.YEAR) == birthday.get(Calendar.YEAR)) {

                return Math.abs(today.get(Calendar.DAY_OF_YEAR) - birthday.get(Calendar.DAY_OF_YEAR));

            } else {

                if (birthday.get(Calendar.YEAR) > today.get(Calendar.YEAR)) {
                    Calendar temp = today;
                    today = birthday;
                    birthday = temp;
                }

                int additionalDays = 0;

                int startDateOriginalYearDays = today.get(Calendar.DAY_OF_YEAR);

                while (today.get(Calendar.YEAR) > birthday.get(Calendar.YEAR)) {
                    today.add(Calendar.YEAR, -1);
                    additionalDays += today.getActualMaximum(Calendar.DAY_OF_YEAR);
                }

                return additionalDays - birthday.get(Calendar.DAY_OF_YEAR) + startDateOriginalYearDays;
            }
        } else {
            return 500;
        }

    }


}
