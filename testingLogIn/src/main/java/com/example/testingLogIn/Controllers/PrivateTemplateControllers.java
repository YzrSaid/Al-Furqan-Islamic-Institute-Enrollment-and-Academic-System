package com.example.testingLogIn.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
@Controller
public class PrivateTemplateControllers {

    @GetMapping("/home")
    public String getDashboard(){
        return "dashboard";
    }

    @GetMapping("/listing")
    public String getListing(){
        return "listing";
    }

    @GetMapping("/gradelevelmaintenance")
    public String getGradeLevelMaintenance(){
        return "grade-level-maintenance";
    }
}
