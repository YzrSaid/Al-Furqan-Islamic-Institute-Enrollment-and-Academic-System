package com.example.testingLogIn.Repositories;

import com.example.testingLogIn.CustomObjects.EvaluationStatus;
import com.example.testingLogIn.CustomObjects.SubjectSectionCount;
import com.example.testingLogIn.Models.Schedule;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepo extends JpaRepository<Schedule, Integer>{
    
    @Query("SELECT COUNT(*) > 0 from Schedule s "+
            "WHERE s.isNotDeleted = true "+
            "AND s.section.isNotDeleted "+
            "AND s.teacher.staffId = :teacherId "+
            "AND s.day = :dayOfWeek "+
            "AND (:schedNum IS NULL OR s.scheduleNumber != :schedNum) "+
            "AND ((s.timeStart <= :timeStart AND s.timeEnd > :timeStart) "+
            "OR (s.timeStart > :timeStart AND s.timeStart < :timeEnd))")
    public boolean isTeacherConflict(
            @Param("schedNum") Integer schedNum,
            @Param("teacherId") int teacherId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("timeStart") LocalTime timeStart,
            @Param("timeEnd") LocalTime timeEnd);
    
    @Query( "SELECT s FROM Schedule s "+
            "WHERE s.isNotDeleted " +
            "AND s.section.isNotDeleted "+
            "AND s.teacher.staffId = :teacherId")
    List<Schedule> findTeacherchedules(@Param("teacherId") int teacherId);
    
    @Query("SELECT COUNT(*) > 0 from Schedule s "+
            "WHERE s.isNotDeleted = true "+
            "AND s.section.isNotDeleted "+
            "AND s.section.number = :sectionNum "+
            "AND s.day = :dayOfWeek "+
            "AND (:schedNum IS NULL OR s.scheduleNumber != :schedNum) "+
            "AND ((s.timeStart <= :timeStart AND s.timeEnd > :timeStart) "+
            "OR (s.timeStart > :timeStart AND s.timeStart < :timeEnd))")
    boolean isSectionConflict(
            @Param("schedNum") Integer schedNum,
            @Param("sectionNum") int sectionNum,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("timeStart") LocalTime timeStart,
            @Param("timeEnd") LocalTime timeEnd);
    
    @Query("SELECT s FROM Schedule s "+
           "WHERE s.isNotDeleted = true "+
            "AND s.section.isNotDeleted "+
           "AND s.section.number = :sectionNum")
    List<Schedule> findSectionSchedules(@Param("sectionNum") int sectionNum);
    
    @Query("SELECT s FROM Schedule s WHERE s.isNotDeleted = TRUE AND s.section.number = :sectionId AND s.section.isNotDeleted AND s.teacher.staffId != :teacherId AND s.subject.subjectNumber = :subjectId")
    List<Schedule> findSubjectSectionSchedule(@Param("subjectId") int subjectId, @Param("sectionId") int sectionId, @Param("teacherId") int teacherId);//check if subject is being taught by another teacher
    
    @Query("SELECT COUNT(DISTINCT s.subject.subjectNumber) FROM Schedule s WHERE s.isNotDeleted = TRUE AND s.section.isNotDeleted AND s.section.number = :sectionId")
    Integer getUniqueSubjectCountBySection(@Param("sectionId") int sectionId);
    
    @Query("SELECT NEW com.example.testingLogIn.CustomObjects.SubjectSectionCount(sc.subject,COUNT(DISTINCT sc.section)) FROM Schedule sc " + 
           "WHERE sc.isNotDeleted = TRUE " +
            "AND sc.section.isNotDeleted "+
           "AND sc.teacher.staffId = :teacherId "+
           "GROUP BY sc.subject")
    List<SubjectSectionCount> findTeacherSubjectAndSectionCount(@Param("teacherId") int teacherId);
    
    @Query("SELECT sc FROM Schedule sc " + 
           "WHERE sc.isNotDeleted = TRUE " +
            "AND sc.section.isNotDeleted "+
           "AND sc.teacher.staffId = :teacherId " + 
           "AND sc.subject.subjectNumber = :subjectId " + 
           "GROUP BY sc.section")
    List<Schedule> findByTeacherSubject(@Param("teacherId") int teacherId, @Param("subjectId") int subjectId);
    @Query("SELECT COUNT(sc) FROM Schedule sc " +
            "WHERE sc.isNotDeleted = TRUE " +
            "AND sc.section.isNotDeleted "+
            "AND sc.teacher.staffId = :teacherId " +
            "AND sc.subject.subjectNumber = :subjectId")
    Integer countTeacherSubjectSched(@Param("teacherId") int userId, @Param("subjectId") int subjectId);
}
