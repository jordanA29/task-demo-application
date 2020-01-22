package com.sonalake.refactoring.service;


import com.sonalake.refactoring.web.exception.TaskNotFoundException;
import com.sonalake.refactoring.model.Task;
import com.sonalake.refactoring.model.User;
import com.sonalake.refactoring.repository.TaskRepository;
import com.sonalake.refactoring.web.specification.TaskFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {


  @Mock
  private TaskRepository taskRepository;

  @InjectMocks
  private TaskService taskService;

  @InjectMocks
  private UserService userService;

  private User user;
  private Task t1;
  private Task t2;
  @Before
  public void setUp() throws Exception {
    this.user = new User( "bob");
    this.t1 = new Task(user, "code review", "2020-07-01", false);
    this.t2 = new Task(user, "code review", "2020-07-01", false);


  }

  @Test
  public void whenCreateTask_thenReturnCreatedTask(){
    when(taskRepository.save(t1)).thenReturn(t1);
    Task actualTask = taskService.createTask(t1);
    verify(taskRepository, times(1)).save(t1);
    assertThat(actualTask).isEqualTo(t1);
  }

  @Test
  public void whenFindAll_NoFilter_thenReturnTaskList() {
    TaskFilter taskFilter = new TaskFilter();
    Sort sort = new Sort(Sort.Direction.DESC, "id");
    List<Task> expectedTasks = Arrays.asList(t1, t2);
    when(taskRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(expectedTasks);
    List <Task> actualTasks = taskService.getTaskList(taskFilter, "id:desc");
    verify(taskRepository, times(1)).findAll(taskFilter, sort);
    assertThat(actualTasks).isEqualTo(expectedTasks);
  }

  @Test
  public void whenFindById_TaskFound_thenReturnEntryFound() throws TaskNotFoundException {
    when(taskRepository.findById(t1.getId())).thenReturn(Optional.of(t1));
    Task actual = taskService.findById(t1.getId());
    verify(taskRepository, times(1)).findById(t1.getId());
    assertThat(actual).isEqualTo(t1);
  }

  @Test
  public void whenFindById_TaskNotFound_thenThrowsTaskNotFoundException() {
    when(taskRepository.findById(t1.getId())).thenReturn(Optional.empty());
    Throwable thrown = catchThrowable(() -> {
      taskService.findById(t1.getId());
    });
    verify(taskRepository, times(1)).findById(t1.getId());
    assertThat(thrown)
      .isInstanceOf(TaskNotFoundException.class)
      .hasMessageContaining("Cannot find Task with id "+ t1.getId());
  }

  @Test
  public void whenDelete_TaskFound_thenDeleteCalledWithTask() throws TaskNotFoundException {
    when(taskRepository.findById(t1.getId())).thenReturn(Optional.of(t1));
    taskService.delete(t1.getId());
    verify(taskRepository, times(1)).delete(t1);
  }

  @Test
  public void whenDelete_TaskNotFound_thenThrowsTaskNotFoundException() throws TaskNotFoundException {
    when(taskRepository.findById(t1.getId())).thenReturn(Optional.empty());
    Throwable thrown = catchThrowable(() -> {
      taskService.findById(t1.getId());
    });
    verify(taskRepository, never()).delete(t1);
    assertThat(thrown)
      .isInstanceOf(TaskNotFoundException.class)
      .hasMessageContaining("Cannot find Task with id "+ t1.getId());
  }

  @Test
  public void whenCompleteTask_TaskFound_thenReturnTaskCompleted() throws TaskNotFoundException {
    Task expectedCompleted = new Task(user, "code review", "2020-07-01", true);
    when(taskRepository.findById(t1.getId())).thenReturn(Optional.of(t1));
    when(taskRepository.save(t1)).thenReturn(expectedCompleted);
    Task actualCompleted = taskService.completeTask(t1.getId());
    verify(taskRepository, times(1)).save(t1);
    assertThat(actualCompleted.getIsCompleted()).isTrue();
  }

  @Test
  public void whenComplete_TaskNotFound_thenThrowsTaskNotFoundException() throws TaskNotFoundException {
    when(taskRepository.findById(t1.getId())).thenReturn(Optional.empty());
    Throwable thrown = catchThrowable(() -> {
      taskService.findById(t1.getId());
    });
    verify(taskRepository, never()).save(t1);
    assertThat(thrown)
      .isInstanceOf(TaskNotFoundException.class)
      .hasMessageContaining("Cannot find Task with id "+ t1.getId());
  }
}
