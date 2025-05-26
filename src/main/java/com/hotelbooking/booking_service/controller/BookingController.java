package com.hotelbooking.booking_service.controller;

import com.hotelbooking.booking_service.dto.CreateBookingRequest;
import com.hotelbooking.booking_service.entity.Booking;
import com.hotelbooking.booking_service.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    ResponseEntity<Booking> createBooking(@AuthenticationPrincipal String userId , @Valid @RequestBody CreateBookingRequest bookingRequest){
        log.info("Create Booking for user id {}",userId);
        bookingRequest.setUserId(userId);
        return new ResponseEntity<>(bookingService.createBooking(bookingRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{bookingId}")
    ResponseEntity<Booking> getBookingById(@PathVariable UUID bookingId){
       return ResponseEntity.ok(bookingService.getBooking(bookingId));
    }
    @GetMapping("/user/{userId}")
    ResponseEntity<List<Booking>> getBookingForUser(@PathVariable String userId){
        return ResponseEntity.ok(bookingService.getBookingByUser(userId));
    }
    @GetMapping("/my")
    public ResponseEntity<List<Booking>> getMyBookings(@AuthenticationPrincipal String userId) {
        log.info("User id {}",userId);
        return ResponseEntity.ok(bookingService.getBookingByUser(userId));
    }

    @PostMapping("/{bookingId}/cancel")
    ResponseEntity<Booking> cancelBooking(@PathVariable UUID bookingId, @AuthenticationPrincipal String userId) throws AccessDeniedException {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId,userId));
    }

}
