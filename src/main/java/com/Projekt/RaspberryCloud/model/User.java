package com.Projekt.RaspberryCloud.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    // private String role;
    private String email;
    private boolean adminFlag;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        if (adminFlag) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Methode implementieren
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Methode implementieren
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Methode implementieren
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Methode implementieren
        return true;
    }
}