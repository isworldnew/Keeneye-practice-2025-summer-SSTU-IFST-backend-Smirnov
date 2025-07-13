package ru.smirnov.keeneyepractice.backend.projection;

import java.time.LocalDate;

public interface UserByPersonProjection {

    Long getId(); // параметр
    Long getUserId();
    String getLastname();
    String getFirstname();
    String getParentname();
    LocalDate getBirthDate();
    String getPhoneNumber();
    String getEmail();

    String getUsername();
    String getRole(); // параметр
    Boolean getEnabled();

}
