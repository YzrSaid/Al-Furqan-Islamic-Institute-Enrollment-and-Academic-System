/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.testingLogIn.ModelDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author magno
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeLevelToRequiredPaymentDTO {
    private int connectionId;
    private int requiredFeeId;
    private String requiredFeeName;
    
    private int gradeLevelId;
    private String gradeLevelName;
    
    private double requiredAmount;
    private boolean isNotDeleted;
}
