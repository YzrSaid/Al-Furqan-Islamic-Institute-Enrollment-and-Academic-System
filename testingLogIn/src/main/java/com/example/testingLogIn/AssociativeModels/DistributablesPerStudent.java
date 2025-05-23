package com.example.testingLogIn.AssociativeModels;

import com.example.testingLogIn.ModelDTO.DistributablePerStudentDTO;
import com.example.testingLogIn.Models.Distributable;
import com.example.testingLogIn.Models.SchoolYearSemester;
import com.example.testingLogIn.Models.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class DistributablesPerStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int distributionId;

    @ManyToOne
    @JoinColumn(name = "student", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "item", nullable = false)
    private DistributablesPerGrade item;

    private boolean isReceived;
    private LocalDate dateReceived;
    private boolean isNotDeleted;

    @ManyToOne
    @JoinColumn(name = "semester", nullable = false)
    private SchoolYearSemester sem;

    public static DistributablesPerStudent build(DistributablesPerGrade item,Student student, SchoolYearSemester sem){
        return builder()
                .item(item)
                .student(student)
                .sem(sem)
                .dateReceived(null)
                .isReceived(false)
                .isNotDeleted(true)
                .build();
    }

    public DistributablePerStudentDTO DTOmapper(){
        return DistributablePerStudentDTO.builder()
                .distId(distributionId)
                .student(student.DTOmapper())
                .gradeLevel(item.getGradeLevel())
                .dateReceived(dateReceived)
                .isReceived(isReceived)
                .itemName(item.getItem().getItemName())
                .build();
    }
}
