package com.ridju.backend.service;

import com.ridju.backend.domain.dto.LoginDTO;
import com.ridju.backend.domain.dto.UserDTO;
import com.ridju.backend.domain.exceptions.UserAlreadyExistsException;
import com.ridju.backend.domain.model.MyUser;
import com.ridju.backend.domain.model.Role;
import com.ridju.backend.domain.util.ERole;
import com.ridju.backend.repository.MyUserRepository;
import com.ridju.backend.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MyUserService {

    private MyUserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    public MyUserService(
            MyUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public UserDTO createNewUser(LoginDTO user) {
        Role userRole = roleRepository.findByName(ERole.USER.label).get();
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("user with the username " + user.getUsername() + " already exists");
        }
        MyUser newUser = new MyUser();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setRoles(Arrays.asList(userRole));
        return new UserDTO(userRepository.save(newUser));
    }
}
