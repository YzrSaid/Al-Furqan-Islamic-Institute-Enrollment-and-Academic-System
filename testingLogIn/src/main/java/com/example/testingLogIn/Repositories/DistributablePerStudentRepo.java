package com.example.testingLogIn.Repositories;

import com.example.testingLogIn.AssociativeModels.DistributablesPerStudent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributablePerStudentRepo extends JpaRepository<DistributablesPerStudent,Integer> {
    @Query("SELECT d FROM DistributablesPerStudent d " +
            "JOIN d.student stud " +
            "WHERE (:isClaimed IS NULL OR d.isReceived = :isClaimed) " +
            "AND (:itemId IS NULL OR d.item.item.itemId = :itemId) " +
            "AND LOWER(stud.fullName) LIKE :student")
    Page<DistributablesPerStudent> getStudentDistPage(String student,Boolean isClaimed,Integer itemId,Pageable pageable);
}