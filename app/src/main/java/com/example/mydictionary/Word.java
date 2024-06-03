package com.example.mydictionary;

public class Word {
    String chinese;
    String english;
    public Word(String english, String chinese){
        super();
        this.chinese=chinese;
        this.english=english;
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
                '}';
    }

}
