package com.example.mydictionary;

public class Word {
    String chinese;
    String english;
    int times;

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public Word(String english, String chinese,int times){
        super();
        this.chinese=chinese;
        this.english=english;
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
    public String toString() {
        return "Word{" +
                "chinese='" + chinese + '\'' +
                ", english='" + english + '\'' +
                ", times=" + times +
                '}';
    }

}
