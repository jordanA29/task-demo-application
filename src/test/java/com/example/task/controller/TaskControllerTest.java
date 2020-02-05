package com.example.task.controller;

import com.example.task.model.Task;
import com.example.task.model.User;
import com.example.task.service.TaskService;
import com.example.task.service.UserService;
import com.example.task.web.controller.TaskController;
import com.example.task.web.specification.TaskFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.task.dto.TaskDTO;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

  private ObjectMapper mapper = new ObjectMapper();

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TaskService taskService;

  @MockBean
  private UserService userService;

  private JacksonTester<TaskDTO> dtoJacksonTester;

  @Autowired
  private ObjectMapper objectMapper;
  private User user;
  private Task t1;
  private Task t2;


  @Before
  public void setup() {

    JacksonTester.initFields(this, objectMapper);
    this.user = new User( "bob");
    this.t1 = new Task(user, "code review", "2020-07-01", false);
    this.t2 = new Task(user, "code review", "2020-07-01", false);
  }

  @Test
  public void whenGetAllTasks_TasksFound_thenReturnFoundTaskEntries() throws Exception {
    List<Task> taskList = new ArrayList<>();
    taskList.add(t1);

    when(taskService.getTaskList(any(TaskFilter.class), eq("id:asc"))).thenReturn(taskList);

    mockMvc.perform(get("/tasks")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].task", is(t1.getTask())))
      .andExpect(jsonPath("$[0].due", is(t1.getDue())))
      .andExpect(jsonPath("$[0].user", is(t1.getUser().getName())))
      .andExpect(jsonPath("$[0].isCompleted", is(t1.getIsCompleted())));
    verify(taskService, times(1)).getTaskList(any(TaskFilter.class),eq("id:asc"));
  }

  @Test
  public void whenCreateTask_ValidTask_thenAddTaskEntryAndReturnAddedEntryLocation() throws Exception {

    TaskDTO taskDTO = new TaskDTO("bob", "code review", LocalDate.of(2020, 07, 01));
    Task taskCreated = new Task();
    taskCreated.setId(1L);
    String  taskDTOJson = dtoJacksonTester.write(taskDTO).getJson();
    when(taskService.createTask(any(Task.class))).thenReturn(taskCreated);

    mockMvc.perform(post("/tasks")
      .contentType(MediaType.APPLICATION_JSON)
      .content(taskDTOJson))
      .andExpect(status().isOk())
      .andExpect(redirectedUrlPattern("http://*/tasks/" + taskCreated.getId()))
      .andReturn();
    verify(taskService).createTask(any(Task.class));
  }

  @Test
  public void whenCreateTask_NotValidTask_thenThrowsBadRequestException() throws Exception {
    TaskDTO taskDTO = new TaskDTO("bob", "code review", LocalDate.of(2019, 07, 01));
    String  taskDTOJson = dtoJacksonTester.write(taskDTO).getJson();
    String error = mockMvc.perform(post("/tasks")
      .contentType(MediaType.APPLICATION_JSON)
      .content(taskDTOJson))
      .andExpect(status().isBadRequest())
      .andReturn().getResolvedException().getMessage();
    assertThat(StringUtils.contains(error, "Due can't be in the past"));

    verify(taskService, never()).createTask(any(Task.class));
  }

}
