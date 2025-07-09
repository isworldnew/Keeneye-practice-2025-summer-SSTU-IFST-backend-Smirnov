package ru.smirnov.keeneyepractice.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.keeneyepractice.backend.entity.auxiliary.Person;

import java.util.List;

@Entity
@Table(name = "students")
@Data
public class Student extends Person {

    @OneToMany(mappedBy = "student")
    @JsonManagedReference
    private List<StudentByGroup> studentsByGroups;

}
