package com.example.testingLogIn.CustomObjects;

import com.example.testingLogIn.Models.RequiredFees;
import java.util.Map;
import lombok.*;

/**
 *
 * @author magno
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentPaymentView {
    private int studentId;
    private String studentDisplayId;
    private String studentFirstName;
    private String studentLastName;
    private String studentMiddleName;
    
    private Map<RequiredFees,String> feeStatus;
}
