package com.crossculture.canvas.repository;

import com.crossculture.canvas.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRazorpayOrderId(String orderId);

    @Query("SELECT p FROM Payment p JOIN p.booking b WHERE b.id = :bookingId")
    Optional<Payment> findByBookingId(Long bookingId);
}