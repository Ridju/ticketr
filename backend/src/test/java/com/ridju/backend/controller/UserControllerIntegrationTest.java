package com.ridju.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridju.backend.domain.dto.CreateUserDTO;
import com.ridju.backend.domain.util.ERole;
import com.ridju.backend.repository.MyUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    private String userName = "testuser";
    private String email = "testuser@test.com";
    private String password = "testuserpassword";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MyUserRepository userRepository;

    @BeforeEach
    void setup(){
        userRepository.deleteAll();
    }


    @Test
    @WithMockUser(username = "admin", roles={"ADMIN"})
    public void givenUserObject_whenCreateUser_thenReturnSavedEmployee() throws Exception {
        CreateUserDTO login = new CreateUserDTO();
        login.setUsername(this.userName);
        login.setEmail(this.email);
        login.setPassword(this.password);
        login.setCheckpassword(this.password);

        ResultActions response = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)));

        response
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.username", is(login.getUsername())))
            .andExpect(jsonPath("$.email", is(login.getEmail())))
            .andExpect(jsonPath("$.role", is(ERole.USER.label)));
    }

    @Test
    @WithMockUser(username = "user", roles={"USER"})
    public void givenUserAccount_whenCreateUser_thenReturnNotAllowed() throws Exception {
        CreateUserDTO login = new CreateUserDTO();
        login.setUsername(this.userName);
        login.setEmail(this.email);
        login.setPassword(this.password);
        login.setCheckpassword(this.password);

        ResultActions response = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)));

        response.andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "user", roles={"ADMIN"})
    public void givenUserAccount_whenGetAllUsers_thenReturnAllUsers() throws Exception {
        CreateUserDTO login = new CreateUserDTO();
        login.setUsername(this.userName);
        login.setEmail(this.email);
        login.setPassword(this.password);
        login.setCheckpassword(this.password);

        ResultActions response = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)));

        ResultActions getAllResponse = mockMvc.perform(get("/users"));

        getAllResponse.andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "user", roles={"USER"})
    public void givenUserWithRoleUSER_whenGetAllUsers_thenFail() throws Exception {
        CreateUserDTO login = new CreateUserDTO();
        login.setUsername(this.userName);
        login.setEmail(this.email);
        login.setPassword(this.password);
        login.setCheckpassword(this.password);

        ResultActions response = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)));

        ResultActions getAllResponse = mockMvc.perform(get("/users"));

        getAllResponse.andExpect(status().is4xxClientError());
    }
}
