package com.ridju.backend.controller;

import com.ridju.backend.domain.dto.AuthenticationRequest;
import com.ridju.backend.domain.dto.AuthenticationResponse;
import com.ridju.backend.domain.dto.LoginDTO;
import com.ridju.backend.domain.dto.UserDTO;
import com.ridju.backend.domain.exceptions.PasswordsDontMatchException;
import com.ridju.backend.domain.jwt.JwtUtil;
import com.ridju.backend.domain.model.MyUser;
import com.ridju.backend.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController()
public class UserController {

    private MyUserService userService;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtUtil jwtTokenUtil;

    @Autowired
    public UserController(
        MyUserService userService,
        AuthenticationManager authenticationManager,
        UserDetailsService userDetailsService,
        JwtUtil jwtTokenUtil
    ){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest,
            HttpServletResponse response
    ) throws Exception {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    ));
        }catch (Exception e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        Cookie cookie = new Cookie("refreshToken", jwtTokenUtil.generateToken(userDetails));
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/user")
    public ResponseEntity<UserDTO> createUser(
            @RequestBody LoginDTO loginDTO,
            HttpServletResponse response
    ) throws Exception {
        if(!loginDTO.getPassword().equals(loginDTO.getCheckpassword()))
            throw new PasswordsDontMatchException("Passwords dont match");
        UserDTO user = userService.createNewUser(loginDTO);
        return ResponseEntity.ok(user);
    }
}
