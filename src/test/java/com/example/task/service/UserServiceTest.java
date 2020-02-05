package com.example.task.service;

import com.example.task.model.User;
import com.example.task.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Test
  public void whenFindAll_UsersPresent_thenReturnUserList() {
    User user1 = new User("Arch");
    User user2 = new User( "Bob");
    List<User> expectedUsers = Arrays.asList(user1, user2);
    when(userRepository.findAll()).thenReturn(expectedUsers);
    List <User> actualUsers = userService.getUserList();
    verify(userRepository, times(1)).findAll();
    assertThat(actualUsers).isEqualTo(expectedUsers);
  }

  @Test
  public void whenFindAll_UsersNotPresent_thenReturnEmptyList() {
    List<User> expectedTasks = new ArrayList<>();
    when(userRepository.findAll()).thenReturn(expectedTasks);
    List <User> actualUsers = userService.getUserList();
    verify(userRepository, times(1)).findAll();
    assertThat(actualUsers).isEqualTo(expectedTasks);
  }

  @Test
  public void whenCreateUser_thenReturnCreatedUser() {
    User expectedUser = new User("Arch");
    when(userRepository.save(expectedUser)).thenReturn(expectedUser);
    User actualUser = userService.createUser(expectedUser.getName());
    verify(userRepository, times(1)).save(actualUser);
    assertThat(actualUser).isEqualTo(expectedUser);
  }

  @Test
  public void whenFindByName_UserNotFound_thenReturnOptionalNotPresent() {
    String searchName = "Bob";
    when(userRepository.findByName(searchName)).thenReturn(Optional.empty());
    Optional<User> userOpt = userService.findByName(searchName);
    verify(userRepository, times(1)).findByName(searchName);
    assertThat(userOpt).isEmpty();
  }

  @Test
  public void whenFindByName_UserFound_thenReturnOptionalPresent() {
    String searchName = "Bob";
    User user = new User(searchName);
    when(userRepository.findByName(user.getName())).thenReturn(Optional.of(user));
    Optional<User> userOpt = userService.findByName(searchName);
    verify(userRepository, times(1)).findByName(searchName);
    assertThat(userOpt).isEqualTo(Optional.of(user));
  }
}
