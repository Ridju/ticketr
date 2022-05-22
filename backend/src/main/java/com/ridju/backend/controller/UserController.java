package com.ridju.backend.controller;

import com.ridju.backend.domain.dto.*;
import com.ridju.backend.domain.exceptions.PasswordsDontMatchException;
import com.ridju.backend.domain.exceptions.UserRoleChangeNotAllowedException;
import com.ridju.backend.domain.jwt.JwtUtil;
import com.ridju.backend.domain.util.ERole;
import com.ridju.backend.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController()
public class UserController {

    private final MyUserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtTokenUtil;

    @Autowired
    public UserController(
            MyUserService userService,
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtUtil jwtTokenUtil
    ) {
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
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    ));
        } catch (Exception e) {
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
            @RequestBody CreateUserDTO createUserDTO,
            HttpServletResponse response
    ) throws Exception {
        if (!createUserDTO.getPassword().equals(createUserDTO.getCheckpassword()))
            throw new PasswordsDontMatchException("Passwords dont match");
        UserDTO user = userService.createNewUser(createUserDTO);
        return ResponseEntity.ok(user);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() throws Exception {
        List<UserDTO> returnList = userService.getAllUsers();
        return ResponseEntity.ok(returnList);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/user/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable long id, @RequestBody UpdateUserDTO userInfo) {
        if (userInfo.getRole().getName().equals(ERole.ADMIN.label)) {
            throw new UserRoleChangeNotAllowedException("Cannot assign role ADMIN to anyone");
        }
        UserDTO updatedUser = userService.updateUser(id, userInfo);
        return ResponseEntity.ok(updatedUser);
    }
}
