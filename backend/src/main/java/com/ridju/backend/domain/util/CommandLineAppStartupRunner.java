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

    private final MyUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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
    public void run(String... args) throws Exception {

        if (!roleRepository.existsByName(ERole.ADMIN.label)) {
            roleRepository.save(new Role(ERole.ADMIN.label));
        }

        if (!roleRepository.existsByName(ERole.STAFF.label)) {
            roleRepository.save(new Role(ERole.STAFF.label));
        }

        if (!roleRepository.existsByName(ERole.USER.label)) {
            roleRepository.save(new Role(ERole.USER.label));
        }

        if (!userRepository.existsByUsername("admin")) {
            Role adminRole = roleRepository.findByName(ERole.ADMIN.label).get();
            MyUser admin = new MyUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setEmail("admin@admin.com");
            admin.setRoles(Arrays.asList(adminRole));
            userRepository.save(admin);
        }
    }
}
