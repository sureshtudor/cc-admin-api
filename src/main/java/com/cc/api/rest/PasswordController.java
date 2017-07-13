package com.cc.api.rest;

import com.cc.api.dao.PasswordDao;
import com.cc.api.entity.PasswordDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/password-service")
public class PasswordController implements ResponseMessages {

    @Autowired
    private PasswordDao passwordDao;

    private static final Logger logger = LoggerFactory.getLogger(PasswordController.class);

    @RequestMapping(value = "/current-pwd")
    public ResponseEntity<PasswordDetails> getCurrentPasswordByUserId(@RequestParam long userid) throws Exception {
        PasswordDetails pwdDetails = passwordDao.getPasswordByUserId(userid);
        return new ResponseEntity(pwdDetails, HttpStatus.OK);
    }

    @RequestMapping(value = "/extend-pwd")
    public ResponseEntity<String> extendPasswordExpiry(@RequestParam long pwdid) throws Exception {
        logger.info("Extend password expiry, pwdid: " + pwdid);
        passwordDao.extendPasswordExpiry(pwdid);
        return new ResponseEntity(PWD_EXTEND_SUCCESS, HttpStatus.OK);
    }

    @RequestMapping(value = "/reset-pwd")
    public ResponseEntity<String> resetPasswordByUserId(@RequestParam long userid) throws Exception {
        logger.info("Reset password, userid: " + userid);
        passwordDao.resetPasswordByUserId(userid);
        return new ResponseEntity(PWD_REST_SUCCESS, HttpStatus.OK);
    }

}
