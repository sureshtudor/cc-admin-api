package com.cc.api.entity;

public class SecurityQnA {

    private long userid;

    private short question1;

    private String answer1;

    private short question2;

    private String answer2;

    private short question3;

    private String answer3;

    public SecurityQnA() {
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public short getQuestion1() {
        return question1;
    }

    public void setQuestion1(short question1) {
        this.question1 = question1;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public short getQuestion2() {
        return question2;
    }

    public void setQuestion2(short question2) {
        this.question2 = question2;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public short getQuestion3() {
        return question3;
    }

    public void setQuestion3(short question3) {
        this.question3 = question3;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }
}
