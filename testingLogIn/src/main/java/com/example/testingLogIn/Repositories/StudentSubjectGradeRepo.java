package com.example.testingLogIn.Repositories;

import com.example.testingLogIn.AssociativeModels.StudentSubjectGrade;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author magno
 */
@Repository
public interface StudentSubjectGradeRepo extends JpaRepository<StudentSubjectGrade, Integer> {
    
    @Query("SELECT CASE WHEN EXISTS ( " +
           "SELECT 1 FROM StudentSubjectGrade sg " +
            "JOIN sg.student stud "+
            "JOIN sg.section.level lvl "+
            "JOIN sg.semester sem "+
            "WHERE stud.studentId = :studentId " +
            "AND lvl.levelId = :gradeLevelId " +
            "GROUP BY sem " +
            "HAVING AVG(sg.subjectGrade) > 49" +
           ") THEN true ELSE false END")
    boolean didStudentPassed(
        @Param("studentId") int studentId,
        @Param("gradeLevelId") int gradeLevelId);

    @Query("SELECT sg FROM StudentSubjectGrade sg "+
            "JOIN sg.section sec "+
            "JOIN sg.semester s "+
            "WHERE sec.number = :sectionId "+
            "AND s.sySemNumber = :semId")
    List<StudentSubjectGrade> getSectionGradesByCurrentSem(
            @Param("sectionId") int sectionId,
            @Param("semId") int semId);

    @Query("SELECT sg FROM StudentSubjectGrade sg "+
            "JOIN sg.student s "+
            "WHERE s.studentId = :studentId")
    List<StudentSubjectGrade> getGradesByStudent(@Param("studentId") int studentId);

    @Query("SELECT sg FROM StudentSubjectGrade sg "+
            "JOIN sg.section sec "+
            "JOIN sg.student s "+
            "JOIN sg.semester sem "+
            "WHERE s.studentId = :studentId "+
            "AND sec.level.levelId = :levelId " +
            "ORDER BY sem DESC")
    List<StudentSubjectGrade> getGradesByStudentGradeLevel(@Param("studentId") int studentId,@Param("levelId") int levelid);
    
    @Query("SELECT sg FROM StudentSubjectGrade sg "+
            "WHERE sg.section.number = :sectionId "+
            "AND sg.subject.subjectNumber = :subjectId "+
            "AND sg.semester.sySemNumber = :semId")
    List<StudentSubjectGrade> getGradesBySectionSubjectSem(
            @Param("sectionId") int sectionId,
            @Param("subjectId") int subjectId,
            @Param("semId") int semId);

    @Query("SELECT COUNT(sg) FROM StudentSubjectGrade sg " +
            "WHERE sg.subjectGrade IS NOT NULL " +
            "AND sg.semester.sySemNumber = :semId "+
            "AND sg.section.number = :sectionId "+
            "AND sg.subject.subjectNumber = :subjectId")
    Integer getTotalGraded(
            @Param("sectionId") int sectionId,
            @Param("subjectId") int subjectId,
            @Param("semId") int semId);
}
