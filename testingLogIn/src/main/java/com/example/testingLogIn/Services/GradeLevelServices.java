package com.example.testingLogIn.Services;

import com.example.testingLogIn.Models.GradeLevel;
import com.example.testingLogIn.Repositories.GradeLevelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradeLevelServices {

    @Autowired
    GradeLevelRepo gradeLevelRepo;

    public boolean addNewGradeLevel(String levelName){
        if(getByName(levelName) == null){
            GradeLevel newGrade = new GradeLevel();
            newGrade.setNotDeleted(true);
            newGrade.setLevelName(levelName);
            gradeLevelRepo.save(newGrade);
            return true;
        }else
            return false;
    }

    public List<GradeLevel> getAllGradeLevels(){
        return gradeLevelRepo.findAll().stream()
                             .filter(GradeLevel::isNotDeleted)
                             .collect(Collectors.toList());
    }
    public GradeLevel getGradeLevel(int levelId){
        return gradeLevelRepo.findAll().stream()
                             .filter(gradelvl -> gradelvl.getLevelId() == levelId && gradelvl.isNotDeleted())
                             .findFirst().orElse(null);
    }
    
    public boolean updateGradeLevel(GradeLevel gradeLevel){
        GradeLevel updated = gradeLevelRepo.findAll().stream()
                                           .filter(gradeL -> gradeL.getLevelId() == gradeLevel.getLevelId() &&
                                                             gradeL.isNotDeleted())
                                           .findFirst().orElse(null);
        
        if(updated !=null){
            updated.setLevelName(gradeLevel.getLevelName());
            gradeLevelRepo.save(updated);
            return true;
        }else
            return false;
    }
    
    public boolean deleteGradeLevel(int levelId){
        GradeLevel todelete = gradeLevelRepo.findAll().stream()
                                           .filter(gradeL -> gradeL.getLevelId() == levelId &&
                                                             gradeL.isNotDeleted())
                                           .findFirst().orElse(null);
        
        if(todelete !=null){
            todelete.setNotDeleted(false);
            gradeLevelRepo.save(todelete);
            return true;
        }else
            return false;
    }
    
    public GradeLevel getByName(String levelname){
        return gradeLevelRepo.findAll().stream()
                      .filter(gradeLevel -> gradeLevel.getLevelName().equals(levelname) && 
                                            gradeLevel.isNotDeleted())
                      .findFirst().orElse(null);
    }
}
