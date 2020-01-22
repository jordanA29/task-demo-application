package com.sonalake.refactoring.fixtures;


import com.sonalake.refactoring.model.Task;
import com.sonalake.refactoring.model.User;
import com.sonalake.refactoring.repository.UserRepository;
import com.sonalake.refactoring.service.TaskService;
import com.sonalake.refactoring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ClearController {

  @Autowired
  private UserService userService;
  @Autowired
  private TaskService taskService;


  @DeleteMapping(path = "clear")
  public void clear() {
    this.userService.deleteAll();
    this.taskService.deleteAll();
  }

}
