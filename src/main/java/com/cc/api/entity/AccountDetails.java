package com.cc.api.entity;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDetails implements RowMapper {

    private int acctnum;

    private String acctname;

    private String custname;

    private int acctstatus;

    public AccountDetails() {
    }

    public int getAcctnum() {
        return acctnum;
    }

    public void setAcctnum(int acctnum) {
        this.acctnum = acctnum;
    }

    public String getAcctname() {
        return acctname;
    }

    public void setAcctname(String acctname) {
        this.acctname = acctname;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public int getAcctstatus() {
        return acctstatus;
    }

    public void setAcctstatus(int acctstatus) {
        this.acctstatus = acctstatus;
    }

    @Override
    public Object mapRow(ResultSet rs, int rownum) throws SQLException {
        AccountDetails acct = new AccountDetails();
        acct.setAcctnum(rs.getInt("ACCTNUM"));
        acct.setAcctstatus(rs.getInt("STATUSENUM"));
        acct.setAcctname(rs.getString("ACCTNAME"));
        acct.setCustname(rs.getString("CUSTNUM"));
        return acct;
    }
}
