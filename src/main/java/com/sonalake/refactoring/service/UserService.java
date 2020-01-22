package com.sonalake.refactoring.service;


import com.sonalake.refactoring.model.User;
import com.sonalake.refactoring.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

  private UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public void deleteAll() {
    this.userRepository.deleteAll();
  }

  public List<User> getUserList() {
    return this.userRepository.findAll();
  }

  public User findUserByNameOrCreateUser(String name) {
    User user = findByName(name).isPresent() ? findByName(name).get() : createUser(name);
    return user;
  }

  public Optional<User> findByName(String name) {
    return this.userRepository.findByName(name);
  }


  public User createUser(String name) {
    User user = new User();
    user.setName(name);
    return this.userRepository.save(user);
  }
}
