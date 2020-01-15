package com.sonalake.refactoring.fixtures;


import com.sonalake.refactoring.model.Task;
import com.sonalake.refactoring.model.User;
import com.sonalake.refactoring.repository.UserRepository;
import com.sonalake.refactoring.service.TaskService;
import com.sonalake.refactoring.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ClearController {

  private UserService userService;
  private TaskService taskService;

  public ClearController(
    UserService userService, TaskService taskService
  ) {
    this.userService = userService;
    this.taskService = taskService;
  }

  @DeleteMapping(path = "clear")
  public void clear() throws IOException {
    this.userService.deleteAll();
    this.taskService.deleteAll();
  }

}
