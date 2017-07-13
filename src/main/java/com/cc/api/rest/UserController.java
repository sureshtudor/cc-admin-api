package com.cc.api.rest;

import com.cc.api.dao.UserDao;
import com.cc.api.entity.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user-service")
public class UserController implements ResponseMessages {

    @Autowired
    private UserDao userDao;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserDetails> retrieve(@PathVariable long id) throws Exception {
        logger.info("Get User: " + id);
        UserDetails userDetails = userDao.getUser(id);
        return new ResponseEntity(userDetails, HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<String> create(@RequestBody UserDetails user) throws Exception {
        logger.info("Create new user");
        userDao.createUser(user);
        return new ResponseEntity(USER_CREATED_SUCCESS, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> update(@PathVariable long id, @RequestBody UserDetails user) throws Exception {
        logger.info("Update user");
        userDao.updateUser(user);
        return new ResponseEntity(USER_UPDATED_SUCCESS, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable long id) throws Exception {
        logger.info("Delete User: " + id);
        userDao.deleteUser(id);
        return new ResponseEntity(USER_DELETED_SUCCESS, HttpStatus.OK);
    }

    @RequestMapping(value = "/exist")
    public ResponseEntity<Boolean> isAccountExist(@RequestParam String username) throws Exception {
        int val = userDao.isUsernameExist(username);
        return new ResponseEntity(val == 1, HttpStatus.OK);
    }

}
