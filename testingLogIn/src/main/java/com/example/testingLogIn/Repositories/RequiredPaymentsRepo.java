package com.example.testingLogIn.Repositories;

import com.example.testingLogIn.Models.RequiredPayments;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author magno
 */
@Repository
public interface RequiredPaymentsRepo extends JpaRepository<RequiredPayments, Integer>{
    @Modifying
    @Transactional
    @Query("UPDATE RequiredPayments rp " +
           "SET rp.isNotDeleted = false " +
           "WHERE LOWER(rp.name) = LOWER(:paymentName)")
    void deleteRequiredPaymentByName(@Param("paymentName") String paymentName);
}
