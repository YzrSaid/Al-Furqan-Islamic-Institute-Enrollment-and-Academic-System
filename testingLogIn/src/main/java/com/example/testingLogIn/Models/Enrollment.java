package com.example.testingLogIn.Models;

import com.example.testingLogIn.Enums.EnrollmentStatus;
import jakarta.persistence.Entity;
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
    
    private EnrollmentStatus enrollmentStatus;
    
    @ManyToOne
    @JoinColumn(name = "gradeToEnroll")
    private Section section;
    
}
