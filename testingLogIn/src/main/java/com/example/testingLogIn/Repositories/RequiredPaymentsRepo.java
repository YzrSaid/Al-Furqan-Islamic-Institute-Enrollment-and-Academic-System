package com.example.testingLogIn.Repositories;

import com.example.testingLogIn.Models.RequiredFees;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 * @author magno
 */
@Repository
public interface RequiredPaymentsRepo extends JpaRepository<RequiredFees, Integer>{

    @Query("SELECT rp FROM RequiredFees rp " +
            "WHERE LOWER(rp.name) LIKE :paymentName")
    Optional<RequiredFees> findByName(@Param("paymentName") String paymentName);

    @Modifying
    @Transactional
    @Query("UPDATE RequiredFees rp " +
           "SET rp.isNotDeleted = false " +
           "WHERE LOWER(rp.name) = LOWER(:paymentName)")
    void deleteRequiredPaymentByName(@Param("paymentName") String paymentName);

    @Modifying
    @Transactional
    @Query("UPDATE RequiredFees rp " +
            "SET rp.isCurrentlyActive = true")
    void setRequiredFeesActive();
    
}
