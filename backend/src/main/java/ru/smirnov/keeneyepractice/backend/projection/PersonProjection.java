package ru.smirnov.keeneyepractice.backend.projection;

import java.time.LocalDate;

public interface PersonProjection {

    Long getId();
    String getLastname();
    String getFirstname();
    String getParentname();
    LocalDate getBirthDate();
    String getPhoneNumber();
    String getEmail();

    String getUsername();

}
