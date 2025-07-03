package ru.smirnov.keeneyepractice.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Builder
@Setter @Getter
public class AuthUser implements UserDetails {
    private String password;
    private String username;
    private boolean enabled;
    private List<SimpleGrantedAuthority> authorities;

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
}