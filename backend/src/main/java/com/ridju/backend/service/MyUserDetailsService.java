package com.ridju.backend.service;

import com.ridju.backend.domain.model.MyUser;
import com.ridju.backend.domain.util.MyUserDetails;
import com.ridju.backend.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private MyUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username" + username + "not found"));
        return new MyUserDetails(user);
    }
}
