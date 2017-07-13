package com.cc.api.entity;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordDetails implements RowMapper {

    private int pwdid;

    private int type;

    private String expiry;

    public PasswordDetails() {
    }

    public int getPwdid() {
        return pwdid;
    }

    public void setPwdid(int pwdid) {
        this.pwdid = pwdid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    @Override
    public Object mapRow(ResultSet rs, int rownum) throws SQLException {
        PasswordDetails pwd = new PasswordDetails();
        pwd.setPwdid(rs.getInt("PASSWORDID"));
        pwd.setType(rs.getInt("ISTEMPORARY"));
        pwd.setExpiry(rs.getString("EXPIRYDATE"));
        return pwd;
    }
}
