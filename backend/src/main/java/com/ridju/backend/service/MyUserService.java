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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MyUserService {

    private final MyUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public MyUserService(
            MyUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public UserDTO createNewUser(CreateUserDTO user) {
        Role userRole = roleRepository.findByName(ERole.USER.label).get();
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("user with the username " + user.getUsername() + " already exists");
        }
        MyUser newUser = new MyUser();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setRoles(Arrays.asList(userRole));
        return new UserDTO(userRepository.save(newUser));
    }

    public List<UserDTO> getAllUsers() {
        List<UserDTO> userlist = new ArrayList<>();
        for (MyUser user : userRepository.findAll()) {
            userlist.add(new UserDTO(user));
        }
        return userlist;
    }

    public UserDTO getUserById(long id) {
        MyUser user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " could not be found"));

        return new UserDTO(user);
    }

    public UserDTO updateUser(long id, UpdateUserDTO userInfo) {
        MyUser user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " could not be found"));

        if (!userInfo.getEmail().isEmpty())
            user.setEmail(userInfo.getEmail());

        if (!userInfo.getPassword().isEmpty())
            user.setPassword(passwordEncoder.encode(userInfo.getPassword()));

        if (!userInfo.getRole().getName().isEmpty())
            user.setRoles(Arrays.asList(userInfo.getRole()));

        userRepository.save(user);
        return new UserDTO(user);
    }
}
