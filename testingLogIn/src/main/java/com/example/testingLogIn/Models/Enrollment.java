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
    private int enrollmentId;
    
    @ManyToOne
    @JoinColumn(name = "student")
    private Student student;
    
    private String remarks;
    
    @ManyToOne
    @JoinColumn(name = "semester")
    private SchoolYearSemester SYSemester;
    
    @ManyToOne
    @JoinColumn(name = "gradeToEnroll")
    private GradeLevel gradeLevelToEnroll;
    
    @ManyToOne
    @JoinColumn(name = "sectionToEnroll")
    private Section sectionToEnroll;
    
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus enrollmentStatus;
    
    private boolean isQualified;
    private boolean isNotDeleted;
    
    public EnrollmentDTO DTOmapper(){
        return EnrollmentDTO.builder()
                            .enrollmentId(enrollmentId)
                            .studentFirstName(student.getFirstName())
                            .studentLastName(student.getLastName())
                            .schoolYear(SYSemester.getSchoolYear().getSchoolYear())
                            .semester(SYSemester.getSem())
                            .gradeLevelToEnroll(gradeLevelToEnroll != null ? gradeLevelToEnroll.getLevelName() : null)
                            .sectionName(sectionToEnroll != null ? sectionToEnroll.getSectionName() : null)
                            .enrollmentStatus(enrollmentStatus)
                            .remarks(remarks)
                            .isQualified(isQualified)
                            .isNotDeleted(isNotDeleted)
                            .build();
    }
    
}
