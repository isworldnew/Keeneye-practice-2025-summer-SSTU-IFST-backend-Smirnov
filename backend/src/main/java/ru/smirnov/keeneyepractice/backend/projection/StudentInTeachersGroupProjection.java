package ru.smirnov.keeneyepractice.backend.projection;

// вспомогательная проекция, нужная просто для проверки,
// есть ли в группе конкретного преподавателя тот или иной студент
public interface StudentInTeachersGroupProjection {

    Long getTeacherId();
    Long getGroupId();
    Long getStudentId();

}
