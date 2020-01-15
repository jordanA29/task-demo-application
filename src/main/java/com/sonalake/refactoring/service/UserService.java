package com.sonalake.refactoring.service;


import com.sonalake.refactoring.model.User;
import com.sonalake.refactoring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {


  @Autowired
  private UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void deleteAll() {
    this.userRepository.deleteAll();;
  }

  public List<User> findAll() {
    return this.userRepository.findAll();
  }
}
