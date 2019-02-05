package me.nuwan.seofficial.Model;

public class TimeTableSubject {

    private String code, name, start, end, hall, lec;

    public TimeTableSubject() {
    }

    public TimeTableSubject(String code, String name, String start, String end, String hall, String lec) {
        this.code = code;
        this.name = name;
        this.start = start;
        this.end = end;
        this.hall = hall;
        this.lec = lec;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public String getLec() {
        return lec;
    }

    public void setLec(String lec) {
        this.lec = lec;
    }
}
