package ru.smirnov.keeneyepractice.backend.entity.auxiliary;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
public enum Role {
    // @JsonFormat(shape = JsonFormat.Shape.STRING)
    STUDENT, TEACHER, ADMIN;
}
