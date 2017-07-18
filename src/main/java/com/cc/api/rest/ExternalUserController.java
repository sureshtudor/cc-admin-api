package com.cc.api.rest;

import com.cc.api.dao.PasswordDao;
import com.cc.api.dao.SecretQuestionDao;
import com.cc.api.dao.UserDao;
import com.cc.api.entity.ChangePwdModel;
import com.cc.api.entity.KeyValuePair;
import com.cc.api.entity.SecurityQnA;
import com.cc.api.entity.UserDetails;
import com.cc.api.util.AESEncoder;
import com.cc.api.util.RecaptchaValidator;
import com.cc.api.util.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/external")
public class ExternalUserController implements ResponseMessages {

    @Value("${pwm.token_timeout_mins}")
    private int FORGOT_PWD_TIMEOUT_MINS;
    private static final Logger logger = LoggerFactory.getLogger(ExternalUserController.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordDao passwordDao;

    @Autowired
    private SecretQuestionDao secretQuestionDao;

    @Autowired
    private RecaptchaValidator recaptchaValidator;

    @RequestMapping(value = "/create-user", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestBody UserDetails user) throws Exception {
        logger.info("External, Create new user: " + user.getUser().getUsername());

        // I'm not robot check.
        recaptchaValidation(user.getRecaptcha());

        userDao.createUser(user);
        logger.info("User created successfully.");

        return new ResponseEntity(USER_CREATED_SUCCESS, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/reset-pwd")
    public ResponseEntity<String> reset(@RequestParam String recaptcha, @RequestParam String username,
                                        @RequestParam String url) throws Exception {
        logger.info("External, reset password: " + username);

        // I'm not robot check.
        recaptchaValidation(recaptcha);

        try {
            // validate username
            logger.info("Reset password, validate username: " + username);
            long userid = userDao.getUserIdByUsername(username);

            // generate reset password token.
            logger.info("Generate token");
            String token = username + ":" + System.currentTimeMillis();
            token = AESEncoder.encryptAndUrlEncode(token);

            // generate change password email.
            String resetPwdUrl = url.concat("?token=").concat(token);
            logger.info("Email reset password. URL: " + resetPwdUrl);
            passwordDao.emailResetPasswordLink(userid, resetPwdUrl);
        } catch (EmptyResultDataAccessException se) {
            logger.warn("Reset Password Error, invalid/inactive username: " + username);
            throw new RestException(PWM_INVALID_USER);
        }

        logger.info("Reset password success, username: " + username);
        return new ResponseEntity(PWM_PWD_REST_SUCCESS, HttpStatus.OK);
    }

    @RequestMapping(value = "/change-pwd", method = RequestMethod.POST)
    public ResponseEntity<String> reset(@RequestParam String recaptcha, @RequestBody ChangePwdModel model) throws Exception {

        logger.info("External, change password: " + model.getUsername());

        // I'm not robot check.
        recaptchaValidation(recaptcha);

        try {
            boolean isTokenAuth = (model.getToken() != null && !model.getToken().isEmpty());

            // get the username from token, if available?
            if (isTokenAuth)
                model.setUsername(validateTokenAndGetUsername(model.getToken()));

            // validate username
            logger.info("Change password, validate username: " + model.getUsername());
            long userid = userDao.getUserIdByUsername(model.getUsername());

            // authenticate user if password provided.
            if (!isTokenAuth && !passwordDao.authenticateUser(userid, model.getPassword()))
                throw new RestException(PWM_INVALID_USER_PWD);

            // validate password.
            if (!model.getNewpassword1().equals(model.getNewpassword2()))
                throw new RestException(PWM_NEW_PWD_NOT_MATCH);

            if (!passwordDao.isFACQualifiedPassword(userid, model.getNewpassword1()))
                throw new RestException(PWM_PWD_RULE_FAIL);

            // generate change password email.
            logger.info("Save password, username: " + model.getUsername());
            passwordDao.savePassword(userid, model.getNewpassword1());
        } catch (EmptyResultDataAccessException se) {
            logger.warn("Change Password Error, invalid/inactive username: " + model.getUsername());
            throw new RestException(PWM_INVALID_USER);
        }

        logger.info("Change password success, username: " + model.getUsername());
        return new ResponseEntity(PWM_PWD_CHANGE_SUCCESS, HttpStatus.OK);
    }

    @RequestMapping(value = "/security-profile", method = RequestMethod.POST)
    public ResponseEntity<String> createSecurityProfile(@RequestBody SecurityQnA model) throws Exception {
        logger.info("External, setup security profile: " + model.getUserid());
        secretQuestionDao.createSecrurityProfile(model);
        logger.info("security profile created successfully.");
        return new ResponseEntity(SECURITY_PROFILE_CREATED, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/security-profile/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteSecurityProfile(@PathVariable long id) throws Exception {
        secretQuestionDao.removeSecrurityProfile(id);
        return new ResponseEntity(SECURITY_PROFILE_DELETED, HttpStatus.OK);
    }

    @RequestMapping(value = "/has-security-profile/{id}")
    public ResponseEntity<Boolean> hasSecurityProfile(@PathVariable long id) throws Exception {
        return new ResponseEntity(secretQuestionDao.hasSecurityProfile(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/security-challenge/{id}")
    public ResponseEntity<KeyValuePair> getChallengeQuestion(@PathVariable long id) throws Exception {
        KeyValuePair pair = secretQuestionDao.getChallengeQuestion(id);
        return new ResponseEntity(pair, HttpStatus.OK);
    }

    @RequestMapping(value = "/security-challenge/{id}", method = RequestMethod.POST)
    public ResponseEntity<Boolean> validateChallengeAnswer(@PathVariable long id, @RequestBody KeyValuePair body) throws Exception {
        return new ResponseEntity(secretQuestionDao.validateAnswer(id, body), HttpStatus.OK);
    }

    // I'm not robot check.
    private void recaptchaValidation(String response) throws RestException {
        try {
            if (!recaptchaValidator.validate(response)) {
                logger.info("Robot check failed.");
                throw new RestException(INVALID_RECAPTCHA);
            }
        } catch (IOException e) {
            logger.warn("Robot check exception: ", e);
            throw new RestException(INVALID_RECAPTCHA);
        }
    }

    private String validateTokenAndGetUsername(String token) throws Exception {
        String username;

        try {
            token = AESEncoder.decryptAndUrlDecode(token);
            String[] keys = token.split(":");
            username = keys[0];

            // check if the reset within allowed interval?
            long diff = System.currentTimeMillis() - Long.parseLong(keys[1]);
            long diffMinutes = diff / (60 * 1000) % 60;

            if (diffMinutes > FORGOT_PWD_TIMEOUT_MINS) {
                logger.error("Reset password link timeout!");
                throw new Exception(PWM_AUTH_TOKEN_TIMEOUT);
            }
        } catch (Exception e) {
            logger.error("Authentication token error!", e);
            throw new Exception(PWM_AUTH_TOKEN_ERROR);
        }
        return username;
    }

}
