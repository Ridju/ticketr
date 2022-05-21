package com.ridju.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridju.backend.domain.dto.LoginDTO;
import com.ridju.backend.service.MyUserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void givenUser_whenCreateUser_thenReturnNewUser() throws Exception {
        LoginDTO user = new LoginDTO();
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
    void givenDifferentPasswords_whenCreateUser_thenFailWith4xx() throws Exception {
        LoginDTO user = new LoginDTO();
        user.setUsername(this.userName);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setCheckpassword(this.password + "123");

        mockMvc.perform(post("/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().is4xxClientError());
    }
}