package com.example.testingLogIn.ModelDTO;

import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleDTO {
    private int scheduleNumber;
    private String teacherName;
    private String subject;
    private int subjectId;
    private String sectionName;
    private DayOfWeek day;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private boolean isNotDeleted;
}
