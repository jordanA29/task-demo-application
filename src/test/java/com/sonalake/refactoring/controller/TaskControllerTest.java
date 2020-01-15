package com.sonalake.refactoring.controller;

import com.sonalake.refactoring.model.Task;
import com.sonalake.refactoring.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {


  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TaskService taskService;

  @Test
  public void it_shouldGetAllTasks() throws Exception {

    List<Task> taskList = new ArrayList<>();
    taskList.add(new Task(1L, 1L, "code review", "2020-07-01", false));
    when(taskService.findAll()).thenReturn(taskList);

    this.mockMvc.perform(get("/tasks"))
      .andExpect(status().isOk())
      .andExpect((content().json("[{'id': 1, 'userId': 1, 'due': '2020-07-01', 'isCompleted': false}]")));
  }


}
