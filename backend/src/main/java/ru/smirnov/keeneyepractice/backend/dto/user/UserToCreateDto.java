package ru.smirnov.keeneyepractice.backend.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smirnov.keeneyepractice.backend.dto.basic.IncomingPersonDto;
import ru.smirnov.keeneyepractice.backend.entity.auxiliary.Role;

@Data @NoArgsConstructor @AllArgsConstructor
public class UserToCreateDto extends IncomingPersonDto {

    @NotNull @NotBlank @NotEmpty
    // нужно ещё обязательно проверить её уникальность
    private String username;

    @NotNull @NotBlank @NotEmpty
    @Size(min = 10, message = "Password's size should be >= 10")
    private String rawPassword;

    @NotNull @NotBlank @NotEmpty
    private String role;

    @NotNull
    private Boolean enabled;

}
