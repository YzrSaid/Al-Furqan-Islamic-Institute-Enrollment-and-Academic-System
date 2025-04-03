package com.example.testingLogIn.WebsiteConfiguration;

import com.example.testingLogIn.Enums.Semester;
import com.example.testingLogIn.Models.GradeLevel;
import com.example.testingLogIn.Repositories.GradeLevelRepo;
import com.example.testingLogIn.Services.sySemesterServices;
import com.example.testingLogIn.WebsiteSecurityConfiguration.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.RedirectView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequestMapping
@Controller
@ControllerAdvice
public class UIController {
    @Autowired
    private SchoolProfileRepo schoolProfileRepo;
    @Autowired
    private sySemesterServices semService;
    @Autowired
    private GradeLevelRepo gradeLevelRepo;

    @PutMapping("/website-config/update")
    public ResponseEntity<String> updateSchoolInterface(@RequestBody WebsiteProfile profile) throws FileNotFoundException {
        try{
            byte[] newPicBytes = Optional.ofNullable(profile.getLogoBase64()).map(b -> Base64.getDecoder().decode(profile.getLogoBase64())).orElse(null);
            if(newPicBytes != null){
                SchoolProfile schoolPhoto = schoolProfileRepo.findById("SchoolLogo").orElse(new SchoolProfile("SchoolLogo"));
                schoolPhoto.setKey_value(newPicBytes);
                schoolProfileRepo.save(schoolPhoto);
            }
            if(profile.getGraduatingLevel() != null){
                System.out.println(profile.getGraduatingLevel());
                byte [] gradeLevelByte;
                try(ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOut = new ObjectOutputStream(byteStream)){
                    GradeLevel glvl = gradeLevelRepo.findById(profile.getGraduatingLevel()).orElse(null);
                    assert glvl != null;
                    System.out.println(glvl.getLevelName());
                    objectOut.writeObject(glvl);
                    gradeLevelByte = byteStream.toByteArray();
                    SchoolProfile schoolGraduatingLevel = schoolProfileRepo.findById("GraduatingLevel").orElse(new SchoolProfile("GraduatingLevel"));
                    schoolGraduatingLevel.setKey_value(gradeLevelByte);
                    schoolProfileRepo.save(schoolGraduatingLevel);
                }catch (IOException ioe){
                    //do nothing
                }
            }

            SchoolProfile schoolName = schoolProfileRepo.findById("SchoolName").orElse(new SchoolProfile("SchoolName"));
            SchoolProfile schoolAddress = schoolProfileRepo.findById("SchoolAddress").orElse(new SchoolProfile("SchoolAddress"));
            SchoolProfile schoolEmail = schoolProfileRepo.findById("SchoolEmail").orElse(new SchoolProfile("SchoolEmail"));
            SchoolProfile schoolContactNum = schoolProfileRepo.findById("SchoolContactNum").orElse(new SchoolProfile("SchoolContactNum"));

            schoolName.setKey_value(profile.getSchoolName().getBytes(StandardCharsets.UTF_8));
            schoolAddress.setKey_value(profile.getSchoolAddress().getBytes(StandardCharsets.UTF_8));
            schoolEmail.setKey_value(profile.getSchoolEmail().getBytes(StandardCharsets.UTF_8));
            schoolContactNum.setKey_value(profile.getSchoolContact().getBytes(StandardCharsets.UTF_8));
            schoolProfileRepo.saveAll(List.of(schoolName,schoolAddress,schoolEmail,schoolContactNum));

            return new ResponseEntity<>("School Profile edited successfully", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Transaction failed. Unexpected Error", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/website-logo")
    public ResponseEntity<byte[]> getDynamicImage() throws IOException {
        byte[] imageBytes = schoolProfileRepo.findById("SchoolLogo").map(SchoolProfile::getKey_value).orElse(null);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }

    @GetMapping("/graduatingLevel")
    public ResponseEntity<?> getGraduatingLevel(){
        try{
            return new ResponseEntity<>(Objects.requireNonNull(schoolProfileRepo.findById("GraduatingLevel").map(glvl -> {
                GradeLevel graduatingLevel;
                try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(glvl.getKey_value()))) {
                    graduatingLevel = (GradeLevel) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    return null;
                }
                return graduatingLevel;
            }).orElseThrow(NullPointerException::new)),HttpStatus.OK);
        }catch (NullPointerException npe){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @ModelAttribute("schoolNum")
    public String getSchoolContact(){
        return schoolProfileRepo.findById("SchoolContactNum").map(name -> new String(name.getKey_value())).orElse("Contact Number Not Set");
    }

    @ModelAttribute("schoolName")
    public String getSchoolName(){
        return schoolProfileRepo.findById("SchoolName").map(name -> new String(name.getKey_value())).orElse("School Name Not Set");
    }

    @ModelAttribute("schoolEmail")
    public String getSchoolEmail(){
        return schoolProfileRepo.findById("SchoolEmail").map(name -> new String(name.getKey_value())).orElse("School Email Not Available");
    }

    @ModelAttribute("schoolAddress")
    public String getSchoolAddress(){
        return schoolProfileRepo.findById("SchoolAddress").map(name -> new String(name.getKey_value())).orElse("Contact Info Not Available");
    }

    @ModelAttribute("userRole")
    public String getUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserModel user) {
            return user.getRole().toString();
        }

        return "GUEST";
    }

    @ModelAttribute("userFullName")
    public String getUserFullName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserModel user) {
            return user.getFirstname() + " " + user.getLastname();
        }
        return "UNKNOWN";
    }

    @ModelAttribute("userFirstName")
    public String getUserFirstName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserModel user) {
            return user.getFirstname(); 
        }
        return "UNKNOWN";
    }

    @ModelAttribute("currentSY")
    public String currentSchoolYear() {
        try {
            return semService.getCurrentActive().getSchoolYear().getSchoolYear();
        } catch (Exception e) {
            return "NOT FOUND";
        }
    }

    @ModelAttribute("currentSem")
    public String currentSem() {
        try {
            return semService.getCurrentActive().getSem() == Semester.First ? "First" : "Second";
        } catch (Exception e) {
            return "NOT FOUND";
        }
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public RedirectView handleNoHandlerFoundException(NoHandlerFoundException ex,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "The requested resource was not found.");
        return new RedirectView("/home"); // Redirect to a custom error page
    }
}
