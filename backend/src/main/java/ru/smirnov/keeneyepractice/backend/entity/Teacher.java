package ru.smirnov.keeneyepractice.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "teachers")
@Data
public class Teacher extends Person {

    @OneToMany(mappedBy = "teacher")
    @JsonManagedReference
    private List<Group> groups;

}
