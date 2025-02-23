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
    private String sectionName;
    private DayOfWeek day;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private boolean isNotDeleted;

    @Override
    public String toString() {
        return "ScheduleDTO{" +
                "scheduleNumber=" + scheduleNumber +
                ", teacherName='" + teacherName + '\'' +
                ", subject='" + subject + '\'' +
                ", sectionName='" + sectionName + '\'' +
                ", day=" + day +
                ", timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                ", isNotDeleted=" + isNotDeleted +
                '}';
    }
}
