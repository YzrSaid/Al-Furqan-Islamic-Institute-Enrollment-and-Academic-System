package com.example.testingLogIn.Controllers;

import com.example.testingLogIn.CustomObjects.EnrollmentHandler;
import com.example.testingLogIn.CustomObjects.StudentPaymentForm;
import com.example.testingLogIn.ModelDTO.EnrollmentDTO;
import com.example.testingLogIn.CustomObjects.EnrollmentPaymentView;
import com.example.testingLogIn.ModelDTO.StudentDTO;
import com.example.testingLogIn.Models.Enrollment;
import com.example.testingLogIn.PagedResponse.EnrollmentDTOPage;
import com.example.testingLogIn.Repositories.EnrollmentRepo;
import com.example.testingLogIn.Services.EnrollmentServices;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author magno
 */
@RequestMapping("/enrollment")
@Controller
public class EnrollmentController {
    
    @Autowired
    private EnrollmentServices enrollmentService;

    @PostMapping("/add/student/{studentId}")
    public ResponseEntity<String> addExistingStudentToListing(@PathVariable Integer studentId){
        try{
            if(enrollmentService.addStudentToListing(studentId))
                return new ResponseEntity<>("Student Successfully Added To Listing",HttpStatus.OK);
            else
                return new ResponseEntity<>("Student Is Already In Enrollment Process",HttpStatus.CONFLICT);
        }catch(NullPointerException npe){
            return new ResponseEntity<>("Student Record Not Found",HttpStatus.NOT_FOUND);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Transaction Failed",HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/all/paged/{status}")
    public ResponseEntity<EnrollmentDTOPage> getEnrollmentRecordsByStatusPage(@PathVariable String status,
                                                                              @RequestParam(required = false,defaultValue = "1") Integer pageNo,
                                                                              @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                                                              @RequestParam(required = false) String sortBy,
                                                                              @RequestParam(required = false) String search){
        try{
            return new ResponseEntity<>(enrollmentService.getAllEnrollmentPage(status,pageNo,pageSize,sortBy,search),HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
    @GetMapping("/{enrollmentId}")
    public ResponseEntity<EnrollmentDTO> getEnrollmentRecord(@PathVariable int enrollmentId){
        try{
            return new ResponseEntity<>(enrollmentService.getEnrollment(enrollmentId),HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
    @GetMapping("/paymentView/{enrollmentId}")
    public ResponseEntity<StudentPaymentForm> getEnrollmentPaymentView(@PathVariable int enrollmentId){
        try{
            return new ResponseEntity<>(enrollmentService.getStudentPaymentStatus(enrollmentId),HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
    @PutMapping("/add/assessment/{enrollmentId}/{gradeLevelId}")
    public ResponseEntity<String> proceedToAssessment(@PathVariable int enrollmentId, @PathVariable int gradeLevelId){
        try{
            int result = enrollmentService.addToAssessment(enrollmentId, gradeLevelId);
            return switch (result) {
                case 1 -> new ResponseEntity<>("Enrollment Record Not Found",HttpStatus.NOT_FOUND);
                case 2 -> new ResponseEntity<>("Grade Level Record Not Found",HttpStatus.NOT_FOUND);
                default -> new ResponseEntity<>("Enrollment Record Successfully Moved To Assessment",HttpStatus.OK);
            };
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Transaction Failed. Try Again.",HttpStatus.CONFLICT);
        }
    }
    
    @PutMapping("/add/payment/{enrollmentId}/{sectionNumber}")
    public ResponseEntity<String> proceedToPayment(@PathVariable int enrollmentId, @PathVariable int sectionNumber){
        try{
            int result = enrollmentService.addToPayment(enrollmentId, sectionNumber);

            return switch (result) {
                case 1 -> new ResponseEntity<>("Enrollment Record Not Found",HttpStatus.NOT_FOUND);
                case 2 -> new ResponseEntity<>("Section Record Not Found",HttpStatus.NOT_FOUND);
                default -> new ResponseEntity<>("Enrollment Record Successfully Moved To Payment",HttpStatus.OK);
            };
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Transaction Failed. Try Again.",HttpStatus.CONFLICT);
        }
    }
    
    @PutMapping("/add/enrolled/{enrollmentId}")
    public ResponseEntity<String> proceedToEnrolled(@PathVariable int enrollmentId){
        try{
            int result = enrollmentService.addToEnrolled(enrollmentId);

            return switch (result) {
                case 1 -> new ResponseEntity<>("Enrollment Record Not Found",HttpStatus.NOT_FOUND);
                default -> new ResponseEntity<>("Student Successfully Enrolled",HttpStatus.OK);
            };
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Transaction Failed. Try Again.",HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/cancel/{enrollmentId}")
    public ResponseEntity<String> cancelEnrollment(@PathVariable int enrollmentId,
                                                   @RequestParam(required = false,defaultValue = "false") Boolean undo){
        try{
            boolean result = enrollmentService.cancelEnrollment(enrollmentId,undo);
            return new ResponseEntity<>("Enrollment Cancelled Successfully",HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Transaction Failed. Try Again.",HttpStatus.CONFLICT);
        }
    }
    
}
