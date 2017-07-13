package com.cc.api.dao;

import com.cc.api.entity.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao implements SqlQueries {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createUser(UserDetails user) {
        jdbcTemplate.update("call PKG_RP_ADMIN.PR_CREATE_USER(?, ?, ?, ?, ?, ?, ?, ?)",
                user.getAcctnum(), user.getLosid(), user.getComment(), user.getUser().getUserid(),
                user.getUser().getUsername(), user.getUser().getFirstname(), user.getUser().getLastname(),
                user.getUser().getEmail());
    }

    public void updateUser(UserDetails user) {
        jdbcTemplate.update("call PKG_RP_ADMIN.PR_UPDATE_USER(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                user.getAcctnum(), user.getLosid(), user.getComment(), user.getUser().getUserid(),
                user.getUser().getUsername(), user.getUser().getFirstname(), user.getUser().getLastname(),
                user.getUser().getEmail(), user.getUser().getIsactive(), user.getUser().getIslockedout());
    }

    public void deleteUser(long userid) {
        jdbcTemplate.update("call PKG_RP_ADMIN.PR_DELETE_USER(?)", userid);
    }

    public UserDetails getUser(long userid) {
        UserDetails user = (UserDetails) jdbcTemplate.queryForObject(
                SQL_GET_USER_BY_ID, new Object[]{userid}, new UserDetails());
        // get acct number
        user.setAcctnum(jdbcTemplate.queryForObject(SQL_GET_ACCT_NUM_BY_USERID,
                new Object[]{userid}, Integer.class));
        // get losid
        user.setLosid(jdbcTemplate.queryForObject(SQL_GET_LOSID_BY_USERID,
                new Object[]{userid}, Integer.class));
        return user;
    }

    public long getUserIdByUsername(String username) {
        return jdbcTemplate.queryForObject(SQL_GET_PWD_RESET_USERID,
                new Object[]{username.toLowerCase()}, Long.class);
    }

    public int isUsernameExist(String username) {
        return jdbcTemplate.queryForObject(SQL_IS_USERNAME_EXIST, new Object[]{username}, Integer.class);
    }

}

