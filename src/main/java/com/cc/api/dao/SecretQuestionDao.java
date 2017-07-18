package com.cc.api.dao;

import com.cc.api.entity.KeyValuePair;
import com.cc.api.entity.SecurityQnA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SecretQuestionDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean hasSecurityProfile(long userid) {
        List<SqlParameter> paramList = new ArrayList<>();
        paramList.add(new SqlParameter("N_USERID", Types.INTEGER));
        paramList.add(new SqlOutParameter("N_RETURN", Types.INTEGER));

        final String procedureCall = "{call PKG_RP_ADMIN.PR_HAS_SECURITY_PROFILE(?, ?)}";
        Map<String, Object> resultMap = jdbcTemplate.call(connection -> {
            CallableStatement callableStatement = connection.prepareCall(procedureCall);
            callableStatement.setLong(1, userid);
            callableStatement.registerOutParameter(2, Types.INTEGER);
            return callableStatement;
        }, paramList);

        return ((int) resultMap.get("N_RETURN")) == 1;
    }

    public void createSecrurityProfile(SecurityQnA model) {
        jdbcTemplate.update("call PKG_RP_ADMIN.PR_CREATE_SECURITY_PROFILE(?, ?, ?, ?, ?, ?, ?)",
                model.getUserid(), model.getQuestion1(), model.getAnswer1(), model.getQuestion2(),
                model.getAnswer2(), model.getQuestion3(), model.getAnswer3());
    }

    public void removeSecrurityProfile(long userid) {
        jdbcTemplate.update("call PKG_RP_ADMIN.PR_DELETE_SECURITY_PROFILE(?)", userid);
    }

    public KeyValuePair getChallengeQuestion(long userid) {
        List<SqlParameter> paramList = new ArrayList<>();
        paramList.add(new SqlParameter("N_USERID", Types.INTEGER));
        paramList.add(new SqlOutParameter("N_QUESTIONID", Types.INTEGER));
        paramList.add(new SqlOutParameter("V_QUESTION", Types.VARCHAR));

        final String procedureCall = "{call PKG_RP_ADMIN.PR_GET_CHALLENGE_QUESTION(?, ?, ?)}";
        Map<String, Object> resultMap = jdbcTemplate.call(connection -> {
            CallableStatement callableStatement = connection.prepareCall(procedureCall);
            callableStatement.setLong(1, userid);
            callableStatement.registerOutParameter(2, Types.INTEGER);
            callableStatement.registerOutParameter(3, Types.VARCHAR);
            return callableStatement;
        }, paramList);

        return new KeyValuePair((int) resultMap.get("N_QUESTIONID"), (String) resultMap.get("V_QUESTION"));
    }

    public boolean validateAnswer(long userid, KeyValuePair pair) {
        List<SqlParameter> paramList = new ArrayList<>();
        paramList.add(new SqlParameter("N_USERID", Types.INTEGER));
        paramList.add(new SqlParameter("N_QUESTIONID", Types.INTEGER));
        paramList.add(new SqlParameter("V_ANSWER", Types.VARCHAR));
        paramList.add(new SqlOutParameter("IS_SUCCESS", Types.INTEGER));

        final String procedureCall = "{call PKG_RP_ADMIN.PR_VALIDATE_ANSWER(?, ?, ?, ?)}";
        Map<String, Object> resultMap = jdbcTemplate.call(connection -> {
            CallableStatement callableStatement = connection.prepareCall(procedureCall);
            callableStatement.setLong(1, userid);
            callableStatement.setInt(2, pair.getKey());
            callableStatement.setString(3, pair.getValue());
            callableStatement.registerOutParameter(4, Types.INTEGER);
            return callableStatement;
        }, paramList);

        return ((int) resultMap.get("IS_SUCCESS")) == 1;
    }
}
