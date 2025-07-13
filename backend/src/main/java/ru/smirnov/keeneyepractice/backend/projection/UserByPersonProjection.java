package ru.smirnov.keeneyepractice.backend.projection;

import java.time.LocalDate;

public interface UserByPersonProjection {

    Long getId();
    Long getUserId();
    String getLastname();
    String getFirstname();
    String getParentname();
    LocalDate getBirthDate();
    String getPhoneNumber();
    String getEmail();

    String getUsername();
    String getRole();
    Boolean getEnabled();

}
