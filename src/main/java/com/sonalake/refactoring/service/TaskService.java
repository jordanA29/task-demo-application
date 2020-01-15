package com.sonalake.refactoring.service;

import com.sonalake.refactoring.model.Task;
import com.sonalake.refactoring.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
  private TaskRepository taskRepository;

  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  public List<Task> findAll() {

    return this.taskRepository.findAll();
  }

  public void deleteAll() {
    this.taskRepository.deleteAll();;
  }
}
