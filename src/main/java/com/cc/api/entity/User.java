package com.cc.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VW_USER_LIST")
public class User {
    @Id
    private long userid;

    private String username;

    private String firstname;

    private String lastname;

    private String email;
    // Note: UI needs boolean active, lockedout fields. Hence, mask int values.
    @JsonIgnore
    private int isactive;
    @JsonIgnore
    private int islockedout;

    public User() { }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIsactive() {
        return isactive;
    }

    public void setIsactive(int isactive) {
        this.isactive = isactive;
    }

    public boolean getActive() {
        return isactive == 1;
    }

    public void setActive(boolean active) {
        this.isactive = active ? 1 : 0;
    }

    public int getIslockedout() {
        return islockedout;
    }

    public void setIslockedout(int islockedout) {
        this.islockedout = islockedout;
    }

    public boolean getLockedout() {
        return islockedout == 1;
    }

    public void setLockedout(boolean lockedout) {
        this.islockedout = lockedout ? 1 : 0;
    }

}
