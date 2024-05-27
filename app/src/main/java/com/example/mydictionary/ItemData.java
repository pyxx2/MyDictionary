package com.example.mydictionary;

public class ItemData {
    String chinese;
    String english;
    int times;

    public ItemData(String chinese, String english, int times) {
        this.chinese = chinese;
        this.english = english;
        this.times=times;
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
