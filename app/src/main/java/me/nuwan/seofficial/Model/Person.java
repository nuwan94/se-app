package me.nuwan.seofficial.Model;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Date;

public class Person extends User implements Comparable {

    private String sno;
    private String name;
    private String mob;
    private String dob;
    private String img;
    private String lat, lang;
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


    @Override
    public int compareTo(@NonNull Object o) {

        switch (Common.sortPeopleType) {
            case "Name":
                int res = String.CASE_INSENSITIVE_ORDER.compare(this.name, ((Person) o).getName());
                return (res != 0) ? res : this.name.compareTo(((Person) o).getName());

//            case "Days to Bday":
//                return this.dayCount() - ((Person) o).dayCount();

            default: // Index Sort
                return Integer.parseInt(this.sno) - Integer.parseInt(((Person) o).getSno());

        }

    }
}
