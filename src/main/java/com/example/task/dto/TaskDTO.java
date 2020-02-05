package com.example.task.dto;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class TaskDTO {

  private Long id;

  @NotNull(message = "User can't be empty")
  private String user;

  @NotNull(message = "Task can't be empty")
  private String task;

  @NotNull(message = "Due can't be empty")
  @Future(message = "Due can't be in the past")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate due;

  private boolean isCompleted;

  public TaskDTO() {

  }

  public TaskDTO(String user, String task, LocalDate due) {
    this.user = user;
    this.task = task;
    this.due = due;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getTask() {
    return task;
  }

  public void setTask(String task) {
    this.task = task;
  }

  public LocalDate getDue() {
    return due;
  }

  public void setDue(LocalDate due) {
    this.due = due;
  }

  public boolean getIsCompleted() {
    return isCompleted;
  }

  public void setIsCompleted(boolean completed) {
    isCompleted = completed;
  }

}
