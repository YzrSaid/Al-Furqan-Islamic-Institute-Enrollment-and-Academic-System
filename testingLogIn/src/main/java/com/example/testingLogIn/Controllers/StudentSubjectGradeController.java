package com.example.testingLogIn.Controllers;

import com.example.testingLogIn.ModelDTO.StudentSubjectGradeDTO;
import com.example.testingLogIn.Services.StudentSubjectGradeServices;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author magno
 */
@RequestMapping("/student-grades")
@Controller
public class StudentSubjectGradeController {
    
    @Autowired
    private StudentSubjectGradeServices ssgService;
    
    @GetMapping("/section/{sectionId}")
    public ResponseEntity<Map<String,List<StudentSubjectGradeDTO>>> getStudentGradesBySubjectCurrentSem(@PathVariable int sectionId){
        try{
            return new ResponseEntity<>(ssgService.getStudentGradesBySection(sectionId),HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
    @GetMapping("/section-records/{sectionId}/{subjectId}")
    public ResponseEntity<List<StudentSubjectGradeDTO>> getGradesBySectionSubject(@PathVariable int sectionId, @PathVariable int subjectId){
        try{
            return new ResponseEntity<>(ssgService.getStudentsGradeBySectionSubject(sectionId,subjectId),HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Map<String,List<StudentSubjectGradeDTO>>> getStudentGrades(@PathVariable int studentId){
        try{
            return new ResponseEntity<>(ssgService.getStudentGradesBySemester(studentId),HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
