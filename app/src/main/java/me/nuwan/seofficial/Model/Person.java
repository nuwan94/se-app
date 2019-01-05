package me.nuwan.seofficial.Model;

import android.support.annotation.NonNull;

public class Person implements Comparable {

    private String peopleSNO;
    private String peopleName;
    private String peopleMobileNumber;
    private String peopleDOB;
    private String peopleImage;

    public Person() {
    }

    public Person(String peopleSNO, String peopleName, String peopleMobileNumber, String peopleDOB, String peopleImage) {
        this.peopleSNO = peopleSNO;
        this.peopleName = peopleName;
        this.peopleMobileNumber = peopleMobileNumber;
        this.peopleDOB = peopleDOB;
        this.peopleImage = peopleImage;
    }

    public String getPeopleSNO() {
        return peopleSNO;
    }

    public void setPeopleSNO(String peopleSNO) {
        this.peopleSNO = peopleSNO;
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    public String getPeopleMobileNumber() {
        return peopleMobileNumber;
    }

    public void setPeopleMobileNumber(String peopleMobileNumber) {
        this.peopleMobileNumber = peopleMobileNumber;
    }

    public String getPeopleDOB() {
        return peopleDOB;
    }

    public void setPeopleDOB(String peopleDOB) {
        this.peopleDOB = peopleDOB;
    }

    public String getPeopleImage() {
        return peopleImage;
    }

    public void setPeopleImage(String peopleImage) {
        this.peopleImage = peopleImage;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return Integer.parseInt(this.peopleSNO) - Integer.parseInt(((Person)o).getPeopleSNO());
    }
}
