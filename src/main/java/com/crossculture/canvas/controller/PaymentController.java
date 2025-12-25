package com.crossculture.canvas.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.crossculture.canvas.model.Booking;
import com.crossculture.canvas.model.Payment;
import com.crossculture.canvas.service.BookingService;
import com.crossculture.canvas.service.PaymentService;
import com.crossculture.canvas.service.NotificationService;

@Slf4j
@Controller
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final NotificationService notificationService;

    @Value("${upi.id:divya@okhdfcbank}")
    private String upiId;

    @Value("${upi.name:CrossCultureCanvas}")
    private String upiName;

    /* 1.  Show payment page with G-Pay / UPI deep-link + QR fallback */
    @GetMapping("/pay/{bookingId}/{amount}")
    public String paymentPage(@PathVariable Long bookingId,
                              @PathVariable Long amount, // paise
                              Model model) {

        Booking booking = bookingService.getBookingById(bookingId)
                              .orElseThrow(() -> new IllegalArgumentException("Invalid booking"));

        // create / update payment row
        Payment payment = paymentService.findByBookingId(bookingId)
                              .orElse(Payment.builder()
                                        .booking(booking)
                                        .amountINR(amount / 100.0)
                                        .currency("INR")
                                        .build());
        payment.setStatus(Payment.Status.CREATED);
        paymentService.save(payment);

        // build clean UPI deep-link (rupees as integer, no decimals)
        long rupees = amount / 100;
        String encodedPn = upiName.replace(" ", "%20");
        String upiLink = String.format(
            "upi://pay?pa=%s&pn=%s&am=%d&cu=INR&tn=Booking-%d",
            upiId, encodedPn, rupees, bookingId);

        log.info("UPI link generated: {}", upiLink);

        model.addAttribute("bookingId", bookingId);
        model.addAttribute("upiLink", upiLink);
        model.addAttribute("amount", rupees);   // show ₹
        return "payment";   // QR + G-Pay button page
    }

    /* 2.  Manual confirm – mark booking paid */
    @GetMapping("/success/{bookingId}")
    public String success(@PathVariable Long bookingId) {
        Payment payment = paymentService.findByBookingId(bookingId)
                              .orElseThrow(() -> new IllegalArgumentException("No payment found"));
        payment.setStatus(Payment.Status.PAID);
        paymentService.save(payment);

        // notify venue owner
        notificationService.createPaymentReceivedNotification(
                payment.getBooking().getVenue().getOwner(),
                payment.getAmountINR());

        return "redirect:/bookings/my-bookings?paid=true";
    }
}