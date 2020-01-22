package com.sonalake.refactoring.web.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.sonalake.refactoring.dto.TaskDTO;
import com.sonalake.refactoring.web.exception.TaskNotFoundException;
import com.sonalake.refactoring.model.Task;
import com.sonalake.refactoring.service.TaskService;
import com.sonalake.refactoring.service.UserService;
import com.sonalake.refactoring.web.specification.TaskFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class TaskController {

  @Value("${application.host}")
  private String appHost;

  @Autowired
  private TaskService taskService;

  @Autowired
  private UserService userService;

  @GetMapping("/tasks")
  @ResponseBody
  public List<TaskDTO> getAllTasks(@RequestParam(required = false, defaultValue = "id:asc") String sortBy, TaskFilter filter)  {
    List<Task> tasks = taskService.getTaskList(filter, sortBy);
    return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
  }

  @GetMapping(path = "/tasks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<TaskDTO> getTaskById(@PathVariable("id") Long id) throws TaskNotFoundException {
    Task found = taskService.findById(id);
    return ResponseEntity.ok().body(convertToDTO(found));
  }

  @PostMapping(path = "/tasks")
  @ResponseBody
  public ResponseEntity createTask(@Valid @RequestBody final TaskDTO taskDTO) throws JsonProcessingException {
    Task task = convertToEntity(taskDTO);
    Task taskCreated = taskService.createTask(task);
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Location", appHost + "/tasks/" + taskCreated.getId());
    return ResponseEntity.ok()
      .headers(responseHeaders).build();
  }

  @DeleteMapping(path = "/tasks/{id}")
  @ResponseBody
  public TaskDTO deleteTask(@PathVariable("id") Long id) throws TaskNotFoundException {
    Task deleted = taskService.delete(id);
    return convertToDTO(deleted);
  }

  @PutMapping(path = "tasks/{id}/complete")
  public TaskDTO complete(@PathVariable Long id) throws TaskNotFoundException {
    Task completed = taskService.completeTask(id);
    return convertToDTO(completed);
  }

  private TaskDTO convertToDTO(Task task) {
    TaskDTO taskDTO = new TaskDTO();
    taskDTO.setId(task.getId());
    taskDTO.setUser(task.getUser().getName());
    taskDTO.setTask(task.getTask());
    taskDTO.setDue(LocalDate.parse(task.getDue()));
    taskDTO.setIsCompleted(task.getIsCompleted());
    return taskDTO;
  }


  private Task convertToEntity(TaskDTO dto) {
    Task task = new Task();
    task.setUser(userService.findUserByNameOrCreateUser(dto.getUser()));
    task.setIsCompleted(dto.getIsCompleted());
    task.setDue(dto.getDue().toString());
    task.setTask(dto.getTask());
    return task;
  }


}



