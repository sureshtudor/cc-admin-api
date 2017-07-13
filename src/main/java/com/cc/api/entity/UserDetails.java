package com.cc.api.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDetails implements RowMapper {

    private int acctnum;

    private int losid;

    private String comment;

    private String recaptcha;

    private User user;

    public UserDetails() {
        user = new User();
    }

    public int getAcctnum() {
        return acctnum;
    }

    public void setAcctnum(int acctnum) {
        this.acctnum = acctnum;
    }

    public int getLosid() {
        return losid;
    }

    public void setLosid(int losid) {
        this.losid = losid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRecaptcha() {
        return recaptcha;
    }

    public void setRecaptcha(String recaptcha) {
        this.recaptcha = recaptcha;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Object mapRow(ResultSet rs, int rownum) throws SQLException {
        UserDetails user = new UserDetails();
        user.getUser().setUserid(rs.getInt("USERID"));
        user.getUser().setUsername(rs.getString("USERNAME"));
        user.getUser().setFirstname(rs.getString("FIRSTNAME"));
        user.getUser().setLastname(rs.getString("LASTNAME"));
        user.getUser().setEmail(rs.getString("EMAIL"));
        user.getUser().setIsactive(rs.getShort("ISACTIVE"));
        user.getUser().setIslockedout(rs.getShort("ISLOCKEDOUT"));
        user.setComment(rs.getString("COMMENTS"));
        return user;
    }
}
