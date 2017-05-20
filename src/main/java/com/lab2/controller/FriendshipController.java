package com.lab2.controller;

import com.lab2.dao.FriendshipDao;
import com.lab2.dao.impl.HibernateFriendshipDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/friends/{id1}/{id2}")
@Controller
public class FriendshipController {

    private FriendshipDao friendshipDao;
    public FriendshipController() {
        friendshipDao = new HibernateFriendshipDao();
    }

/* ----------------------------------------------- Add a friend ----------------------------------------------- */
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> addFriends(@PathVariable("id1") long userId1, @PathVariable("id2") long userId2) {
        try {
            friendshipDao.create(userId1, userId2);
System.out.println("FriendshipController.addFriends():-- Added Friend " +userId1+ "::"+userId2);
        } catch (Exception e) {
System.out.println("Exception in adding friend.");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

/* ---------------------------------------------- Remove a friend ---------------------------------------------- */
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> removeFriends(@PathVariable("id1") long userId1, @PathVariable("id2") long userId2) {
        try {
            friendshipDao.delete(userId1, userId2);
System.out.println("Deleting Friendship "+userId1+"::"+userId2);
        } catch (Exception e) {
System.out.println("Exception in deleting friendship");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}