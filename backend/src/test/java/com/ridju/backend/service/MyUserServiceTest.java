package com.ridju.backend.service;

import com.ridju.backend.domain.dto.LoginDTO;
import com.ridju.backend.domain.dto.UserDTO;
import com.ridju.backend.domain.exceptions.UserAlreadyExistsException;
import com.ridju.backend.domain.model.MyUser;
import com.ridju.backend.domain.model.Role;
import com.ridju.backend.domain.util.ERole;
import com.ridju.backend.repository.MyUserRepository;
import com.ridju.backend.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;


class MyUserServiceTest {

    private MyUserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private MyUserService userService;

    private String userName = "testuser";
    private String email = "testuser@test.com";
    private String password = "testuserpassword";

    @BeforeEach
    void initUserCase() {
        this.userRepository = Mockito.mock(MyUserRepository.class);
        this.roleRepository = Mockito.mock(RoleRepository.class);
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.userService = new MyUserService(userRepository, passwordEncoder, roleRepository);
    }

    @Test
    void givenNewUser_whenUserInputValid_thenSuccess() throws Exception{
        //given
        LoginDTO loginDTO = new LoginDTO(userName, email, password, password);

        //when
        Mockito
            .when(roleRepository.findByName(Mockito.any(String.class)))
            .thenReturn(Optional.of(new Role(ERole.USER.label)));
        Mockito
            .when(userRepository.save(Mockito.any(MyUser.class)))
            .then(returnsFirstArg());

        UserDTO newUser = this.userService.createNewUser(loginDTO);

        //then
        assertThat(newUser).isNotNull();
        assertEquals(userName, newUser.getUsername());
        assertEquals(email, newUser.getEmail());
        assertEquals(ERole.USER.label, newUser.getRole());
    }

    @Test
    void givenNewUser_whenUserAlreadyExist_thenFail() throws Exception{
        //given
        LoginDTO loginDTO = new LoginDTO(userName, email, password, password);

        //when
        Mockito
                .when(roleRepository.findByName(Mockito.any(String.class)))
                .thenReturn(Optional.of(new Role(ERole.USER.label)));

        Mockito
                .when(userRepository.existsByUsername(Mockito.any(String.class)))
                .thenReturn(Boolean.TRUE);

        //then
        UserAlreadyExistsException ex = assertThrows(UserAlreadyExistsException.class,
                () -> userService.createNewUser(loginDTO));
        assertEquals("user with the username " + this.userName + " already exists", ex.getMessage());
    }
}