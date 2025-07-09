package ru.smirnov.keeneyepractice.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "students")
@Data
public class Student extends Person {

    @OneToMany(mappedBy = "student")
    @JsonManagedReference
    private List<StudentByGroup> studentsByGroups;

}
