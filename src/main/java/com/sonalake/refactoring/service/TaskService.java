package com.sonalake.refactoring.service;

import com.sonalake.refactoring.web.exception.TaskNotFoundException;
import com.sonalake.refactoring.model.Task;
import com.sonalake.refactoring.repository.TaskRepository;
import com.sonalake.refactoring.web.specification.TaskFilter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {
  private TaskRepository taskRepository;

  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  public List<Task> getTaskList(TaskFilter filter, String sortBy) {

    Sort sort = extractSortObject(sortBy);
    return taskRepository.findAll(filter, sort);
  }

  @Transactional
  public void deleteAll() {
    this.taskRepository.deleteAll();
  }

  @Transactional
  public Task createTask(Task task) {
      return this.taskRepository.save(task);

  }

  @Transactional(readOnly = true)
  public Task findById(Long id) throws TaskNotFoundException {
      Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Cannot find Task with id " +id));
      return task;
  }

  @Transactional
  public Task delete(Long id) throws TaskNotFoundException {
    Task task = findById(id);
    taskRepository.delete(task);
    return task;
  }

  @Transactional
  public Task completeTask(Long id) throws TaskNotFoundException {
    Task task = findById(id);
    task.setIsCompleted(true);
    return taskRepository.save(task);
  }


  private Sort extractSortObject(String sortBy) {
    String sortField = sortBy.contains(":") ? sortBy.substring(0, sortBy.indexOf(":")) : sortBy;
    String direction = sortBy.contains(":") && sortBy.substring(sortBy.indexOf(":") + 1).equals("desc") ? "DESC" : "ASC";
    Sort sort = Sort.by(Sort.Direction.valueOf(direction), sortField);
    return sort;
  }
}
