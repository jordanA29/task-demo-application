package com.sonalake.refactoring.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Task {
    @Id
    @GeneratedValue
    public Long id;

  public Long userId;

  public String task;
  public String due;
  public boolean isCompleted;
  public Task(Long id, Long userId, String task, String due, boolean isCompleted) {
    this.id = id;
    this.userId = userId;
    this.task = task;
    this.due = due;
    this.isCompleted = isCompleted;
  }

  public Task() {

  }
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getTask() {
    return task;
  }

  public void setTask(String task) {
    this.task = task;
  }

  public String getDue() {
    return due;
  }

  public void setDue(String due) {
    this.due = due;
  }

  public boolean isCompleted() {
    return isCompleted;
  }

  public void setCompleted(boolean completed) {
    isCompleted = completed;
  }
}
