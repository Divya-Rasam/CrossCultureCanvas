package com.crossculture.canvas.service;

import com.crossculture.canvas.model.Payment;
import com.crossculture.canvas.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment save(Payment payment) {
        if (payment.getCreatedAt() == null) {
            payment.setCreatedAt(java.time.LocalDateTime.now());
        }
        return paymentRepository.save(payment);
    }

    public Optional<Payment> findByOrderId(String orderId) {
        return paymentRepository.findByRazorpayOrderId(orderId);
    }

    public Optional<Payment> findByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }
}