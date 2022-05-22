package com.ridju.backend.service;

import com.ridju.backend.domain.dto.CreateUserDTO;
import com.ridju.backend.domain.dto.UpdateUserDTO;
import com.ridju.backend.domain.dto.UserDTO;
import com.ridju.backend.domain.exceptions.UserAlreadyExistsException;
import com.ridju.backend.domain.exceptions.UserNotFoundException;
import com.ridju.backend.domain.model.MyUser;
import com.ridju.backend.domain.model.Role;
import com.ridju.backend.domain.util.ERole;
import com.ridju.backend.repository.MyUserRepository;
import com.ridju.backend.repository.RoleRepository;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        CreateUserDTO createUserDTO = new CreateUserDTO(userName, email, password, password);

        //when
        Mockito
            .when(roleRepository.findByName(Mockito.any(String.class)))
            .thenReturn(Optional.of(new Role(ERole.USER.label)));
        Mockito
            .when(userRepository.save(Mockito.any(MyUser.class)))
            .then(returnsFirstArg());

        UserDTO newUser = this.userService.createNewUser(createUserDTO);

        //then
        assertThat(newUser).isNotNull();
        assertEquals(userName, newUser.getUsername());
        assertEquals(email, newUser.getEmail());
        assertEquals(ERole.USER.label, newUser.getRole());
    }

    @Test
    void givenNewUser_whenUserAlreadyExist_thenFail() throws Exception{
        //given
        CreateUserDTO createUserDTO = new CreateUserDTO(userName, email, password, password);

        //when
        Mockito
                .when(roleRepository.findByName(Mockito.any(String.class)))
                .thenReturn(Optional.of(new Role(ERole.USER.label)));

        Mockito
                .when(userRepository.existsByUsername(Mockito.any(String.class)))
                .thenReturn(Boolean.TRUE);

        //then
        UserAlreadyExistsException ex = assertThrows(UserAlreadyExistsException.class,
                () -> userService.createNewUser(createUserDTO));
        assertEquals("user with the username " + this.userName + " already exists", ex.getMessage());
    }

    @Test
    void givenUsers_whenGetAllUsers_thenReturnAllUsers() throws Exception{
        //given
        MyUser user = new MyUser();
        user.setPassword(this.password);
        user.setUsername(this.userName);
        user.setRoles(Arrays.asList(new Role(ERole.USER.label)));
        user.setEmail(this.email);
        List<MyUser> userlist = new ArrayList<>();
        userlist.add(user);
        userlist.add(user);
        userlist.add(user);
        userlist.add(user);
        userlist.add(user);

        //when
        Mockito
                .when(userRepository.findAll())
                .thenReturn(userlist);
        List<UserDTO> returnlist = userService.getAllUsers();

        //then
        assertEquals(returnlist.size(), 5);
    }

    @Test
    void givenUser_whenGetUserById_thenReturnThisUser() throws Exception{
        //given
        MyUser user = new MyUser();
        user.setPassword(this.password);
        user.setUsername(this.userName);
        user.setRoles(Arrays.asList(new Role(ERole.USER.label)));
        user.setEmail(this.email);
        long id = 1;
        //when
        Mockito
                .when(userRepository.findById(id))
                .thenReturn(Optional.of(user));
        UserDTO returnUser = userService.getUserById(id);

        //then
        assertEquals(returnUser.getUsername(), this.userName);
        assertEquals(returnUser.getEmail(), this.email);
        assertEquals(returnUser.getRole(), ERole.USER.label);
    }

    @Test
    void givenUserWithUnkownId_whenDoNotExist_thenFail() throws Exception{
        //given
        MyUser user = new MyUser();
        user.setPassword(this.password);
        user.setUsername(this.userName);
        user.setRoles(Arrays.asList(new Role(ERole.USER.label)));
        user.setEmail(this.email);
        long id = 1;
        //when
        Mockito
                .when(userRepository.findById(id))
                .thenReturn(Optional.ofNullable(null));

        //then
        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(id));
        assertEquals("User with id " + id + " could not be found", ex.getMessage());
    }

    @Test
    void givenUpdatedUser_whenUpdateUser_thenSucceed() throws Exception{
        //given
        UpdateUserDTO updateUser = new UpdateUserDTO();
        updateUser.setPassword("new" + this.password);
        updateUser.setRole(new Role(ERole.USER.label));
        updateUser.setEmail("new" + this.email);

        MyUser user = new MyUser();
        user.setPassword(this.password);
        user.setUsername(this.userName);
        user.setRoles(Arrays.asList(new Role(ERole.USER.label)));
        user.setEmail(this.email);
        long id = 1;
        //when
        Mockito
                .when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        UserDTO returnUser = userService.updateUser(id, updateUser);

        //then
        assertEquals(returnUser.getEmail(), "new" + this.email);
    }

    @Test
    void givenUserDoesNotExist_whenUpdateUser_thenFail() throws Exception{
        //given
        UpdateUserDTO updateUser = new UpdateUserDTO();
        updateUser.setPassword("new" + this.password);
        updateUser.setRole(new Role(ERole.USER.label));
        updateUser.setEmail("new" + this.email);

        MyUser user = new MyUser();
        user.setPassword(this.password);
        user.setUsername(this.userName);
        user.setRoles(Arrays.asList(new Role(ERole.USER.label)));
        user.setEmail(this.email);
        long id = 1;

        //when
        Mockito
                .when(userRepository.findById(id))
                .thenReturn(Optional.ofNullable(null));

        //then
        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(id, updateUser));
        assertEquals("User with id " + id + " could not be found", ex.getMessage());
    }

}