package com.cc.api.dao;

import com.cc.api.entity.AccountDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDao implements SqlQueries {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int isAccountExist(long acctnum) {
        return jdbcTemplate.queryForObject(SQL_IS_ACCTNUM_EXIST, new Object[]{acctnum}, Integer.class);
    }

    public AccountDetails getAccountById(long id) {
        AccountDetails acct = (AccountDetails) jdbcTemplate.queryForObject(
                SQL_GET_ACCT_BY_ID, new Object[]{id}, new AccountDetails());
        return acct;
    }

    public AccountDetails getAccountByUserId(long userid) {
        AccountDetails acct = (AccountDetails) jdbcTemplate.queryForObject(
                SQL_GET_ACCT_BY_USERID, new Object[]{userid}, new AccountDetails());
        return acct;
    }

}

