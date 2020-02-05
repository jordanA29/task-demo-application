package com.example.task.fixtures;


import com.example.task.service.TaskService;
import com.example.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

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
