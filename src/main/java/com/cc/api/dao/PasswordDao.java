package com.cc.api.dao;

import com.cc.api.entity.PasswordDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
public class PasswordDao implements SqlQueries {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PasswordDetails getPasswordByUserId(long userid) {
        PasswordDetails pwd = (PasswordDetails) jdbcTemplate.queryForObject(
                SQL_GET_PASSWORD_DETAILS, new Object[]{userid}, new PasswordDetails());
        return pwd;
    }

    public void resetPasswordByUserId(long userid) {
        jdbcTemplate.update("call PKG_RP_ADMIN.PR_RESET_PASSWORD(?)", userid);
    }

    public void extendPasswordExpiry(long passwordid) {
        jdbcTemplate.update("call PKG_RP_ADMIN.PR_EXTEND_PASSWORD_EXPIRY(?)", passwordid);
    }

    public boolean isFACQualifiedPassword(long userid, String password) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_RP_PASSWORD_UTIL").withFunctionName("FN_IS_FAC_QUALIFIED_PASSWORD");
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("USER_ID", userid)
                .addValue("NEW_PASSWORD", password);
        return call.executeFunction(BigDecimal.class, in).intValue() == 1;
    }

    public boolean authenticateUser(long userid, String password) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_RP_PASSWORD_UTIL").withFunctionName("FN_AUTHENTICATE_USER");
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("USER_ID", userid)
                .addValue("V_PASSWORD", password);
        return call.executeFunction(BigDecimal.class, in).intValue() == 1;
    }

    public void savePassword(long userid, String password) {
        jdbcTemplate.update("call PKG_RP_PASSWORD_UTIL.PR_SAVE_PASSWORD(?, ?)", userid, password);
    }

    public void emailResetPasswordLink(long userid, String url) {
        jdbcTemplate.update("call PKG_RP_PASSWORD_UTIL.PR_EMAIL_RESET_PASSWORD_LINK(?, ?)", userid, url);
    }

}

