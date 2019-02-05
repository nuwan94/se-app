package me.nuwan.seofficial.Model;

import android.support.annotation.NonNull;

public class Rank implements Comparable {

    private String name;
    private String score;

    public Rank() {
    }

    public Rank(String name, String score) {
        this.name = name;
        this.score = score;
    }

    public String getShortName() {
        String[] nameArr = getName().split(" ");
        return (nameArr.length > 1) ? nameArr[0] + " " + nameArr[1] : nameArr[0];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return Integer.parseInt(this.score) - Integer.parseInt(((Rank) o).getScore());
    }
}
