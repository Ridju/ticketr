package com.ridju.backend.domain.util;

import com.ridju.backend.domain.model.MyUser;
import com.ridju.backend.domain.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final boolean active;
    private final List<GrantedAuthority> authorityList;

    public MyUserDetails(MyUser user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.active = true;
        this.authorityList = new ArrayList<>();
        for (Role role : user.getRoles()) {
            this.authorityList.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorityList;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
