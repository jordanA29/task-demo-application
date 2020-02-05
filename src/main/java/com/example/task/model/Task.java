package com.example.task.model;


import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", nullable = false)
  private User user;
  @NonNull
  private String task;
  @NonNull
  private String due;
  @NonNull
  private boolean isCompleted;

  public Task() {

  }

  public Task(User user, String task, String due, boolean isCompleted) {
    this.user = user;
    this.task = task;
    this.due = due;
    this.isCompleted = isCompleted;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public boolean getIsCompleted() {
    return isCompleted;
  }

  public void setIsCompleted(boolean completed) {
    isCompleted = completed;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Task task1 = (Task) o;
    return isCompleted == task1.isCompleted &&
      Objects.equals(id, task1.id) &&
      Objects.equals(user, task1.user) &&
      Objects.equals(task, task1.task) &&
      Objects.equals(due, task1.due);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, user, task, due, isCompleted);
  }
}
