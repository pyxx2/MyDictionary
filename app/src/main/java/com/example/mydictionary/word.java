package com.example.mydictionary;

public class word {
    int id;
    String chinese;
    String english;
    int times;
    public word(String chinese, String english,int times,int id){
        super();
        this.chinese=chinese;
        this.english=english;
        this.times=times;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
