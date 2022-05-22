package com.ridju.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridju.backend.domain.dto.CreateUserDTO;
import com.ridju.backend.domain.dto.UpdateUserDTO;
import com.ridju.backend.domain.dto.UserDTO;
import com.ridju.backend.domain.model.Role;
import com.ridju.backend.domain.util.ERole;
import com.ridju.backend.service.MyUserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private String userName = "testuser";
    private String email = "testuser@test.com";
    private String password = "testuserpassword";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MyUserService userService;

    @Test
    @WithMockUser(username = "admin", roles={"ADMIN"})
    public void givenUser_whenCreateUser_thenReturnNewUser() throws Exception {
        CreateUserDTO user = new CreateUserDTO();
        user.setUsername(this.userName);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setCheckpassword(this.password);

        mockMvc.perform(post("/user")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles={"ADMIN"})
    public void givenDifferentPasswords_whenCreateUser_thenFailWith4xx() throws Exception {
        CreateUserDTO user = new CreateUserDTO();
        user.setUsername(this.userName);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setCheckpassword(this.password + "123");

        mockMvc.perform(post("/user")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "admin", roles={"ADMIN"})
    public void givenArrayOfUserDTOs_whenGetAllUsers_thenReturnAllUsers() throws Exception {
        //given
        List<UserDTO> userlist = new ArrayList<>();
        UserDTO user = new UserDTO();
        user.setId(1);
        user.setUsername(this.userName);
        user.setEmail(this.email);
        user.setRole(ERole.USER.label);
        userlist.add(user);
        userlist.add(user);
        userlist.add(user);

        //when
        Mockito.when(userService.getAllUsers()).thenReturn(userlist);

        //then
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)));
    }

    @Test
    @WithMockUser(username = "admin", roles={"USER"})
    public void givenArrayOfUserDTOs_whenUserIsNotAdmin_thenShouldFail() throws Exception {
        //given
        List<UserDTO> userlist = new ArrayList<>();
        UserDTO user = new UserDTO();
        user.setId(1);
        user.setUsername(this.userName);
        user.setEmail(this.email);
        user.setRole(ERole.USER.label);
        userlist.add(user);
        //when
        Mockito.when(userService.getAllUsers()).thenReturn(userlist);

        //then
        mockMvc.perform(get("/users"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "admin", roles={"ADMIN"})
    public void givenOneUser_whenGetUserById_thenReturnThisOneUser() throws Exception {
        //given
        UserDTO user = new UserDTO();
        user.setId(1);
        user.setUsername(this.userName);
        user.setEmail(this.email);
        user.setRole(ERole.USER.label);

        //when
        Mockito.when(userService.getUserById(1)).thenReturn(user);

        //then
        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is(this.userName)))
                .andExpect(jsonPath("$.email", is(this.email)))
                .andExpect(jsonPath("$.role", is(ERole.USER.label)));
    }

    @Test
    @WithMockUser(username = "admin", roles={"USER"})
    public void givenUserWithUSERRole_whenGetUserById_thenShouldFail() throws Exception {
        //given
        UserDTO user = new UserDTO();
        user.setId(1);
        user.setUsername(this.userName);
        user.setEmail(this.email);
        user.setRole(ERole.USER.label);

        //when
        Mockito.when(userService.getUserById(1)).thenReturn(user);

        //then
        mockMvc.perform(get("/user/1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "admin", roles={"ADMIN"})
    public void givenChangedUser_whenUpdateUser_thenShouldReturnUpdatedUser() throws Exception {
        //given
        UserDTO user = new UserDTO();
        user.setId(1);
        user.setUsername(this.userName);
        user.setEmail(this.email);
        user.setRole(ERole.USER.label);

        UpdateUserDTO updateUser = new UpdateUserDTO();
        updateUser.setUsername(this.userName);
        updateUser.setPassword(this.password);
        updateUser.setRole(new Role(ERole.USER.label));
        updateUser.setEmail(this.email);

        //when
        Mockito.when(userService.updateUser(1, updateUser)).thenReturn(user);

        //then
        mockMvc.perform(put("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles={"USER"})
    public void givenUserWithRoleUSER_whenUpdateUser_thenShouldFail() throws Exception {
        //given
        UserDTO user = new UserDTO();
        user.setId(1);
        user.setUsername(this.userName);
        user.setEmail(this.email);
        user.setRole(ERole.USER.label);

        UpdateUserDTO updateUser = new UpdateUserDTO();
        updateUser.setUsername(this.userName);
        updateUser.setPassword(this.password);
        updateUser.setRole(new Role(ERole.USER.label));
        updateUser.setEmail(this.email);

        //when
        Mockito.when(userService.updateUser(1, updateUser)).thenReturn(user);

        //then
        mockMvc.perform(put("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "admin", roles={"ADMIN"})
    public void givenUpdateRoleToADMIN_whenUpdateUser_thenShouldFail() throws Exception {
        //given
        UserDTO user = new UserDTO();
        user.setId(1);
        user.setUsername(this.userName);
        user.setEmail(this.email);
        user.setRole(ERole.USER.label);

        UpdateUserDTO updateUser = new UpdateUserDTO();
        updateUser.setUsername(this.userName);
        updateUser.setPassword(this.password);
        updateUser.setRole(new Role(ERole.ADMIN.label));
        updateUser.setEmail(this.email);

        //when
        Mockito.when(userService.updateUser(1, updateUser)).thenReturn(user);

        //then
        mockMvc.perform(put("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().is4xxClientError());
    }
}