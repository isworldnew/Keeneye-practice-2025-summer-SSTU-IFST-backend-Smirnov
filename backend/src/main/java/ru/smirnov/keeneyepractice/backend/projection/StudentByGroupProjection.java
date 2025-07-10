package ru.smirnov.keeneyepractice.backend.projection;

public interface StudentByGroupProjection extends PersonProjection {

    Long getGroupId();
    Long getTeacherId();

}
