package com.example.testingLogIn.Services;

import com.example.testingLogIn.Enums.Role;
import com.example.testingLogIn.ModelDTO.SectionDTO;
import com.example.testingLogIn.ModelDTO.UserDTO;
import com.example.testingLogIn.Models.GradeLevel;
import com.example.testingLogIn.Models.Section;
import com.example.testingLogIn.Repositories.GradeLevelRepo;
import com.example.testingLogIn.Repositories.ScheduleRepo;
import com.example.testingLogIn.Repositories.SectionRepo;
import com.example.testingLogIn.WebsiteSecurityConfiguration.UserModel;
import com.example.testingLogIn.WebsiteSecurityConfiguration.UserRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author magno
 */
@Service
public class SectionServices {
    
    private final GradeLevelRepo gradeRepo;
    private final SectionRepo sectionRepo;
    private final UserRepo userRepo;
    private final ScheduleRepo schedRepo;

    @Autowired
    public SectionServices(GradeLevelRepo gradeRepo, SectionRepo sectionRepo, UserRepo userRepo, ScheduleRepo schedRepo) {
        this.gradeRepo = gradeRepo;
        this.sectionRepo = sectionRepo;
        this.userRepo = userRepo;
        this.schedRepo = schedRepo;
    }
    //Get by Grade Level Name
    public List<SectionDTO> getSectionsByLevel(String gradeLevel){
        return sectionRepo.findAll().stream()
                          .filter(section -> section.getLevel().getLevelName().toLowerCase()
                                                    .equals(gradeLevel.toLowerCase())
                                            && section.isNotDeleted())
                            .map(Section::toSectionDTO)
                            .collect(Collectors.toList());
    }
    
    //FOR ADDING NEW SECTION
    public int addSection(SectionDTO sectionDTO){
        UserModel user= getUserByFullName(sectionDTO.getAdviserName());
        GradeLevel gradeLevel = getGradeLevel(sectionDTO.getGradeLevelName());
        int result = user == null ?             1:
                     gradeLevel == null ?       2:
                     doesTeacherHaveAdvisory
                                 (user) ?       3:
                     getSectionByName
        (sectionDTO.getSectionName()
                .toLowerCase()) != null      ?  4:
                                                5;

        if (result == 5) {
            Section newSection = Section.builder()
                    .adviser(user)
                    .level(gradeLevel)
                    .sectionName(sectionDTO.getSectionName())
                    .capacity(sectionDTO.getCapacity())
                    .isNotDeleted(true)
                    .build();
            sectionRepo.save(newSection);
            return 5;
        }
        return result;
    }

    public List<SectionDTO> getAllSections(){
        return sectionRepo.findAll().stream()
                          .filter(section -> section.isNotDeleted() && section.getLevel().isNotDeleted())
                          .map(Section::toSectionDTO)
                          .peek(sec -> sec.setSubSchedCount(schedRepo.getUniqueSubjectCountBySection(sec.getNumber())))
                          .collect(Collectors.toList());
    }

    public Section getSectionById(int sectionNumber){
        return sectionRepo.findById(sectionNumber).orElse(null);
    }

    public SectionDTO getSection(int sectionNumber){
        Section sec = sectionRepo.findById(sectionNumber).orElse(null);
        
        if(sec != null && sec.isNotDeleted())
            return sec.toSectionDTO();
        
        return null;                     
    }
    
    //UPDATE SECTION RECORD
    public boolean updateSection(SectionDTO sectionDTO){
        Section toUpdate = sectionRepo.findById(sectionDTO.getNumber()).orElse(null);
        if(toUpdate != null){
            toUpdate.setLevel(getGradeLevel(sectionDTO.getGradeLevelName()));
            toUpdate.setAdviser(getUserByFullName(sectionDTO.getAdviserName()));
            toUpdate.setSectionName(sectionDTO.getSectionName());
            toUpdate.setCapacity(sectionDTO.getCapacity());
            toUpdate.setNotDeleted(true);
            sectionRepo.save(toUpdate);
            
            return true;
        }
        return false;
    }
    
    //DELETING SECTION RECORD
    public boolean deleteSection(int sectionNumber){
        Section todelete = sectionRepo.findById(sectionNumber).orElse(null);
        
        if(todelete != null && todelete.isNotDeleted()){
            todelete.setNotDeleted(false);
            sectionRepo.save(todelete);
            return true;
        }
        return false;
    }
    
    //Return the List of Teachers that has no advisory class
    public List<UserDTO > getNoAdvisoryTeachers(){
        List<Integer> adviserStaffIds= new ArrayList<>();
        sectionRepo.findAll().stream()
                   .filter(Section::isNotDeleted)
                   .forEach(section -> adviserStaffIds.add(section.getAdviser().getStaffId()));
        
        return userRepo.findAll().stream()
                   .filter(teacher -> teacher.getRole().equals(Role.TEACHER) && !adviserStaffIds.contains(teacher.getStaffId()))
                   .map(UserModel::mapperDTO)
                   .collect(Collectors.toList());
    }
    
    public SectionDTO getSectionByNameDTO(String sectionName){
    return getSectionByName(sectionName).toSectionDTO();
    }
    
    public Section getSectionByName(String sectionName){
        return sectionRepo.findAll().stream()
                          .filter(section -> section.isNotDeleted() &&
                                            section.getSectionName().toLowerCase().equalsIgnoreCase(sectionName))
                          .findFirst().orElse(null);
    }
    
    private boolean doesTeacherHaveAdvisory(UserModel staff){
        return sectionRepo.findAll().stream()
                          .filter(advisory -> advisory.getAdviser().getStaffId() == staff.getStaffId() && 
                                              advisory.isNotDeleted())
                          .findFirst().orElse(null) !=null;
    }
    
    private UserModel getUserByFullName(String teacherName){
        return userRepo.findAll().stream()
                       .filter(user -> teacherName.contains(user.getFirstname()) &&
                                       teacherName.contains(user.getLastname()))
                       .findFirst().orElse(null);
    }
    
    private GradeLevel getGradeLevel(String levelName){
        return gradeRepo.findAll().stream()
                        .filter(grade -> levelName.equals(grade.getLevelName()) && 
                                         grade.isNotDeleted())
                        .findFirst().orElse(null);
    }
    
}
