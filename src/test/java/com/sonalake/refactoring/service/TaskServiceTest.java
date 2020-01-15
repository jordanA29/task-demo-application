package com.sonalake.refactoring.service;


import com.sonalake.refactoring.model.Task;
import com.sonalake.refactoring.repository.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;


@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {

  @Mock
  private TaskRepository taskRepository;

  @InjectMocks
  private TaskService taskService;
  @Test
  public void whenFindAll_thenReturnTaskList() {
    Task taskSample = new Task(1L, 1L, "code review", "2020-07-01", false);
    List<Task> expectTasks = Arrays.asList(taskSample);
    doReturn(expectTasks).when(taskRepository).findAll();
    List <Task> actualTasks = taskService.findAll();
    assertThat(actualTasks).isEqualTo(expectTasks);
  }
}
