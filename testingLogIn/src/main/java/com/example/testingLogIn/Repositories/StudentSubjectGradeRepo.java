package com.example.testingLogIn.Repositories;

import com.example.testingLogIn.AssociativeModels.StudentSubjectGrade;
import java.util.List;
import java.util.Optional;

import com.example.testingLogIn.CustomObjects.EvaluationStatus;
import com.example.testingLogIn.CustomObjects.FailedStudents;
import com.example.testingLogIn.CustomObjects.PassedStudents;
import com.example.testingLogIn.Models.SchoolYearSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
            "WHERE sg.isNotDeleted " +
            "AND stud.studentId = :studentId " +
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
            "WHERE sg.isNotDeleted " +
            "AND sec.number = :sectionId "+
            "AND s.sySemNumber = :semId")
    List<StudentSubjectGrade> getSectionGradesByCurrentSem(
            @Param("sectionId") int sectionId,
            @Param("semId") int semId);

    @Query("SELECT sg FROM StudentSubjectGrade sg "+
            "JOIN sg.student s "+
            "WHERE sg.isNotDeleted " +
            "AND s.studentId = :studentId " +
            "AND sg.semester.sySemNumber = :sem")
    List<StudentSubjectGrade> getGradesByStudent(@Param("studentId") int studentId, int sem);

    @Query("SELECT sg.semester FROM StudentSubjectGrade sg "+
            "JOIN sg.student s "+
            "WHERE sg.isNotDeleted " +
            "AND s.studentId = :studentId " +
            "GROUP BY sg.semester")
    List<SchoolYearSemester> getStudentSemAttended(int studentId);

    @Query("SELECT sg FROM StudentSubjectGrade sg "+
            "JOIN sg.section sec "+
            "JOIN sg.student s "+
            "JOIN sg.semester sem "+
            "WHERE sg.isNotDeleted " +
            "AND s.studentId = :studentId "+
            "AND sec.level.levelId = :levelId " +
            "ORDER BY sem DESC")
    List<StudentSubjectGrade> getGradesByStudentGradeLevel(@Param("studentId") int studentId,@Param("levelId") int levelid);
    
    @Query("SELECT sg FROM StudentSubjectGrade sg "+
            "WHERE sg.isNotDeleted "+
            "AND sg.section.number = :sectionId " +
            "AND sg.subject.subjectNumber = :subjectId "+
            "AND sg.semester.sySemNumber = :semId")
    List<StudentSubjectGrade> getGradesBySectionSubjectSem(
            @Param("sectionId") int sectionId,
            @Param("subjectId") int subjectId,
            @Param("semId") int semId);

    @Query("SELECT COUNT(sg) FROM StudentSubjectGrade sg " +
            "WHERE sg.isNotDeleted " +
            "AND sg.subjectGrade IS NOT NULL " +
            "AND sg.semester.sySemNumber = :semId "+
            "AND sg.section.number = :sectionId "+
            "AND sg.subject.subjectNumber = :subjectId")
    Integer getTotalGraded(
            @Param("sectionId") int sectionId,
            @Param("subjectId") int subjectId,
            @Param("semId") int semId);

    @Query("""
    SELECT NEW com.example.testingLogIn.CustomObjects.EvaluationStatus(s.teacher,sg.section,sg.subject)
    FROM StudentSubjectGrade sg
    LEFT JOIN Schedule s ON sg.subject.subjectNumber = s.subject.subjectNumber
    LEFT JOIN s.teacher t
    WHERE (:sectionNum IS NULL OR sg.section.number = :sectionNum)
    AND sg.semester.sySemNumber = :semId
    GROUP BY s.teacher, sg.subject, sg.section.number
    """)
    List<EvaluationStatus> findSectionSubjects(@Param("sectionNum") Integer sectionNum, int semId);

    @Query("""
    SELECT NEW com.example.testingLogIn.CustomObjects.EvaluationStatus(s.teacher,sg.section,sg.subject)
    FROM StudentSubjectGrade sg
    LEFT JOIN Schedule s ON sg.subject.subjectNumber = s.subject.subjectNumber
    LEFT JOIN s.teacher t
    WHERE sg.subjectGrade IS NULL
    AND sg.semester.sySemNumber = :semId
    GROUP BY sg.section.number, sg.subject
    """)
    List<EvaluationStatus> findWithUngradedGrades(int semId);

    @Query("SELECT sub FROM StudentSubjectGrade sub " +
            "JOIN sub.subject subj " +
            "JOIN sub.semester sem " +
            "WHERE subj.subjectNumber = :subjectId " +
            "AND sem.sySemNumber = :semId")
    List<StudentSubjectGrade> findBySemAndSubject(int subjectId, int semId);

    @Modifying
    @Transactional
    @Query("UPDATE StudentSubjectGrade sg SET sg.isNotDeleted = false " +
            "WHERE sg.subject.subjectNumber = :subjectId " +
            "AND sg.semester.sySemNumber = :semId")
    void deleteStudentSubjectGrade(int subjectId, int semId);

    @Query("""
            SELECT NEW com.example.testingLogIn.CustomObjects.PassedStudents(stud, ssg.section.level) 
            FROM StudentSubjectGrade ssg
            JOIN ssg.student stud
            JOIN ssg.section.level lvl
            WHERE ssg.semester.sySemNumber = :semId
            AND (:levelId IS NULL OR lvl.levelId = :levelId)
            GROUP BY stud, lvl
            HAVING AVG(COALESCE(ssg.subjectGrade, 0)) >= 50
            """)
    List<PassedStudents> findPassedStudents(int semId, Integer levelId);

    @Query("""
            SELECT NEW com.example.testingLogIn.CustomObjects.FailedStudents(stud, ssg.section.level) 
            FROM StudentSubjectGrade ssg
            JOIN ssg.student stud
            JOIN ssg.section.level lvl
            WHERE ssg.semester.sySemNumber = :semId
            AND (:levelId IS NULL OR lvl.levelId = :levelId)
            GROUP BY stud, lvl
            HAVING AVG(COALESCE(ssg.subjectGrade, 0)) < 50
            """)
    List<FailedStudents> findFailedStudents(int semId, Integer levelId);

    @Query("""
            SELECT COUNT(ssg) FROM StudentSubjectGrade ssg
            JOIN ssg.semester sem
            WHERE sem.sySemNumber = :semId
            AND ssg.subjectGrade IS NULL
            """)
    Optional<Long> countUngraded(int semId);
}
