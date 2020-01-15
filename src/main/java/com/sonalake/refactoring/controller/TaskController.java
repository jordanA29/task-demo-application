package com.sonalake.refactoring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonalake.refactoring.model.Task;
import com.sonalake.refactoring.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

  //  /**
//   * Search for tasks - returns values in a list of  {"id": 4324324, "user": "billybob", "task": "do this", "due": "tyyyy-MM-dd", "isCompleted" : true}
//   *
//   * @param user      optional - if set then only tasks for this user are returned - if the user doesn't exist return nothing
//   * @param dateAfter optional - if set then only tasks _after_ this date are returned
//   * @param sortBy    sort by the field, in the form: fieldName:asc , fieldName:desc, fieldName (defaults to ascending) - optional, defaults to id:asc
//   * @return
//   * @throws IOException
//   */
//  @RequestMapping(method = RequestMethod.GET, path = "tasks")
//  public List<TaskDTO> search(
//    @RequestParam(required = false) String user,
//    @RequestParam(required = false) String dateAfter,
//    @RequestParam(required = false) String sortBy,
//    @RequestParam(required = false) Boolean includeCompleted
//  ) throws IOException {
//
//    return findAll(Task.class)
//      .stream()
//      .map(this::taskToDTO)
//      .filter(t -> null == user || t.user.equals(user))
//      .filter(t -> null == dateAfter || toDate(dateAfter).before(toDate(t.due)))
//      .filter(t -> Boolean.TRUE.equals(includeCompleted) || !t.isCompleted)
//      .sorted((o1, o2) -> sort(sortBy, o1, o2))
//      .collect(Collectors.toList());
//
//  }

    @GetMapping("/tasks")
    ResponseEntity<List<Task>> getAllTasks() {
      return new ResponseEntity<>(taskService.findAll(), HttpStatus.OK);
    }
}

