package com.example.messager.user.model;

import java.util.Collection;

import javax.persistence.*;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;


@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private boolean enabled;
    private boolean locked;
    private boolean expired;
    private boolean credentialsExpired;
    private String role;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return expired;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return locked;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsExpired;
    }
    
    @Override
    public String getUsername() {
        return email;
    }
}
