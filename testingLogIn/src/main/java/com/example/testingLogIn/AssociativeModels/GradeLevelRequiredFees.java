package com.example.testingLogIn.AssociativeModels;

import com.example.testingLogIn.ModelDTO.GradeLevelToRequiredPaymentDTO;
import com.example.testingLogIn.Models.GradeLevel;
import com.example.testingLogIn.Models.RequiredFees;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

/**
 *
 * @author magno
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class GradeLevelRequiredFees {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int connectionId;
    
    @ManyToOne
    @JoinColumn(name = "requiredFee")
    private RequiredFees requiredFee;
    
    @ManyToOne
    @JoinColumn(name = "gradeLevel")
    private GradeLevel gradeLevel;
    
    private boolean isNotDeleted;
    
    public GradeLevelToRequiredPaymentDTO DTOmapper(){
        return GradeLevelToRequiredPaymentDTO.builder()
                            .connectionId(connectionId)
                            .requiredFeeId(requiredFee.getId())
                            .requiredFeeName(requiredFee.getName())
                            .gradeLevelId(gradeLevel.getLevelId())
                            .gradeLevelName(gradeLevel.getLevelName())
                            .requiredAmount(requiredFee.getRequiredAmount())
                            .isNotDeleted(isNotDeleted)
                            .build();
    }

    public static GradeLevelRequiredFees build(GradeLevel gradeLevel, RequiredFees fee){
        return builder()
                .requiredFee(fee)
                .gradeLevel(gradeLevel)
                .isNotDeleted(true)
                .build();
    }
}
