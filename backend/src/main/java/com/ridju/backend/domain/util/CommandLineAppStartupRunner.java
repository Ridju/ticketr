package com.ridju.backend.domain.util;

import com.ridju.backend.domain.model.MyUser;
import com.ridju.backend.domain.model.Role;
import com.ridju.backend.repository.MyUserRepository;
import com.ridju.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private MyUserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CommandLineAppStartupRunner(
            MyUserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception{

//        if(!roleRepository.existsByName(ERole.SUPERUSER.label)) {
//            roleRepository.save(new Role(ERole.SUPERUSER.label));
//        }
//
//        if(!roleRepository.existsByName(ERole.STAFF.label)) {
//            roleRepository.save(new Role(ERole.STAFF.label));
//        }
//
//        if(!roleRepository.existsByName(ERole.USER.label)) {
//            roleRepository.save(new Role(ERole.USER.label));
//        }

        if(!userRepository.existsByUsername("admin")) {
            MyUser admin = new MyUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setEmail("admin@admin.com");
            admin.setRoles(Arrays.asList(new Role(ERole.ADMIN.label)));
            userRepository.save(admin);
        }
    }
}
