package com.sonalake.refactoring.controller;


import com.sonalake.refactoring.model.User;
import com.sonalake.refactoring.service.UserService;
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

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Test
  public void it_shouldGetAllUsers() throws Exception {

    List<User> userList = new ArrayList<>();
    userList.add(new User(1L, "billybob"));
    when(userService.findAll()).thenReturn(userList);

    this.mockMvc.perform(get("/users"))
      .andExpect(status().isOk())
      .andExpect((content().json("[{'id': 1, 'name': 'billybob'}]")));
  }

}
