package com.example.testingLogIn.Controllers;

import com.example.testingLogIn.ModelDTO.SubjectDTO;
import com.example.testingLogIn.Models.Subject;
import com.example.testingLogIn.Services.SubjectServices;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author magno
 */

@RequestMapping("/subject")
@Controller
public class SubjectController {
    
    @Autowired
    private SubjectServices subjectService;
    
    @GetMapping("/gradeLevel/{gradeLevel}")
    public ResponseEntity<List<SubjectDTO>> getSubjectByGradeLevel(@PathVariable String gradeLevel){
        try{
            return new ResponseEntity<>(subjectService.getSubjectByGrade(gradeLevel),HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
    @GetMapping("/section/{sectionId}")
    public ResponseEntity<List<SubjectDTO>> getSubjectBySection(@PathVariable int sectionId){
        try{
            return new ResponseEntity<>(subjectService.getSectionSubjects(sectionId),HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<SubjectDTO>> getAllSubjects(){
        try{
            return new ResponseEntity<>(subjectService.getAllSubjects(),HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
    @GetMapping("/{subjectNumber}")
    public ResponseEntity<SubjectDTO> getSubjectByNumber(@PathVariable int subjectNumber){
        try{
            SubjectDTO sub= subjectService.getSubject(subjectNumber);
            if(sub != null)
                return new ResponseEntity<>(sub,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
    @GetMapping("/name/{subjectName}")
    public ResponseEntity<Subject> getByName(@PathVariable String subjectName){
        return new ResponseEntity<>(subjectService.getByName(subjectName),HttpStatus.OK);
    }
    
    @PostMapping("/add")
    public ResponseEntity<String> addNewSubject(@RequestBody SubjectDTO subject){
        try{
            if(subjectService.addNewSubject(subject.getLevelId(), subject.getSubjectName(), subject.isWillApplyNow()))
                return new ResponseEntity<>("New Subject Added Successfully",HttpStatus.OK);
            else
                return new ResponseEntity<>("Subject Name Already Exist in the Selected Grade Level",HttpStatus.CONFLICT);
        }catch(NullPointerException npe){
            return new ResponseEntity<>("Grade Level does not exist",HttpStatus.NOT_FOUND);
        }catch(Exception e){
            return new ResponseEntity<>("Process Failed",HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/updated")
    public ResponseEntity<String> updateSubject(@RequestBody SubjectDTO subject){
        try{
        if(subjectService.updateSubjectDescription(subject))
            return new ResponseEntity<>("Subject Updated Successfully",HttpStatus.OK);
        else
            return new ResponseEntity<>("Subject Name already used",HttpStatus.NOT_ACCEPTABLE);
        }catch(NullPointerException e){
            return new ResponseEntity<>("Selected subject not found",HttpStatus.NOT_FOUND);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Process Failed",HttpStatus.CONFLICT);
        }
    }
    
    @DeleteMapping("/delete/{subjectNumber}")
    public ResponseEntity<String> deleteSubject(@PathVariable int subjectNumber){
        try {
            subjectService.deleteSubject(subjectNumber);
            return new ResponseEntity<>("Subject Delete Successfully", HttpStatus.OK);
        }catch (NullPointerException npe){
                return new ResponseEntity<>("Subject Not Found. Try Again.",HttpStatus.NOT_FOUND);
        }catch(Exception e){
            return new ResponseEntity<>("Process Errror",HttpStatus.CONFLICT);
        }
    }
}
