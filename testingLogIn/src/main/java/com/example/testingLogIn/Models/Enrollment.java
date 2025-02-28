package com.example.testingLogIn.Models;

import com.example.testingLogIn.Enums.EnrollmentStatus;
import com.example.testingLogIn.ModelDTO.EnrollmentDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author magno
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int enrollmentNum;
    
    @ManyToOne
    @JoinColumn(name = "student")
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "semester")
    private SchoolYearSemester SYSemester;
    
    @ManyToOne
    @JoinColumn(name = "gradeToEnroll")
    private GradeLevel gradeLevel;
    
    @ManyToOne
    @JoinColumn(name = "sectionToEnroll")
    private Section section;
    
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus enrollmentStatus;
    
    private boolean isNotDeleted;
    
    public EnrollmentDTO DTOmapper(){
        return EnrollmentDTO.builder()
                            .enrollmentId(enrollmentNum)
                            .studentFirstName(student.getFirstName())
                            .studentLastName(student.getLastName())
                            .schoolYear(SYSemester.getSchoolYear().getSchoolYear())
                            .semester(SYSemester.getSem())
                            .gradeLevel(gradeLevel != null ? gradeLevel.getLevelName() : null)
                            .sectionName(section != null ? section.getSectionName() : null)
                            .enrollmentStatus(enrollmentStatus)
                            .isNotDeleted(isNotDeleted)
                            .build();
    }
    
}
