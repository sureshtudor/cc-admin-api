package com.cc.api.entity;

public class ChangePwdModel {

    private String token;

    private String username;

    private String password;

    private String newpassword1;

    private String newpassword2;

    public ChangePwdModel() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewpassword1() {
        return newpassword1;
    }

    public void setNewpassword1(String newpassword1) {
        this.newpassword1 = newpassword1;
    }

    public String getNewpassword2() {
        return newpassword2;
    }

    public void setNewpassword2(String newpassword2) {
        this.newpassword2 = newpassword2;
    }
}
