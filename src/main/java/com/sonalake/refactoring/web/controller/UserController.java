package com.sonalake.refactoring.web.controller;

import com.sonalake.refactoring.model.User;
import com.sonalake.refactoring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping("/users")
  ResponseEntity<List<User>> getAllUsers() {
    return new ResponseEntity<>(userService.getUserList(), HttpStatus.OK);
  }

}
