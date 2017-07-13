package com.cc.api.rest;

import com.cc.api.dao.AccountDao;
import com.cc.api.entity.AccountDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/account-service")
public class AccountController {

    @Autowired
    private AccountDao accountDao;

    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET)
    public ResponseEntity<AccountDetails> retrieve(@PathVariable long id) throws Exception {
        AccountDetails acctDetails = accountDao.getAccountById(id);
        return new ResponseEntity(acctDetails, HttpStatus.OK);
    }

    @RequestMapping(value = "/user-account")
    public ResponseEntity<AccountDetails> getAccountByUserId(@RequestParam long userid) throws Exception {
        AccountDetails acctDetails = accountDao.getAccountByUserId(userid);
        return new ResponseEntity(acctDetails, HttpStatus.OK);
    }

    @RequestMapping(value = "/exist")
    public ResponseEntity<Boolean> isAccountExist(@RequestParam long acctnum) throws Exception {
        int val = accountDao.isAccountExist(acctnum);
        return new ResponseEntity(val == 1, HttpStatus.OK);
    }

}
