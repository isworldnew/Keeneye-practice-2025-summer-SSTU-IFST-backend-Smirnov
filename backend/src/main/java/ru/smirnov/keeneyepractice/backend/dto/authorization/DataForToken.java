package ru.smirnov.keeneyepractice.backend.dto.authorization;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Builder
@Data
public class DataForToken implements UserDetails {

    // то, что требуется реализовать
    private String username;
    private String password;
    private boolean enabled;
    private List<SimpleGrantedAuthority> authorities;

    // дополнительные нужные мне поля
    private Long userId;
    private String role;
    private Long entityId;

}
