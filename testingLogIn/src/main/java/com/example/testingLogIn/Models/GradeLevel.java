package com.example.testingLogIn.Models;

import com.example.testingLogIn.ModelDTO.GradeLevelDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int levelId;
    private String levelName;
    private boolean isNotDeleted;

    @ManyToOne
    @JoinColumn(name = "preRequisite", nullable = true)
    @JsonIgnore // Prevents infinite recursion
    private GradeLevel preRequisite;
    
    public GradeLevelDTO mapperDTO(){
        return GradeLevelDTO.builder()
                            .isNotDeleted(isNotDeleted)
                            .levelId(levelId)
                            .levelName(levelName)
                            .preRequisite(preRequisite.getLevelName())
                            .build();
    }
}
