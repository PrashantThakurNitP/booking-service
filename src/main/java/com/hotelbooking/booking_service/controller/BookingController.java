package com.hotelbooking.booking_service.controller;

import com.hotelbooking.booking_service.dto.CreateBookingRequest;
import com.hotelbooking.booking_service.entity.Booking;
import com.hotelbooking.booking_service.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    ResponseEntity<Booking> createBooking(@Valid @RequestBody CreateBookingRequest bookingRequest){
      return new ResponseEntity<>(bookingService.createBooking(bookingRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{bookingId}")
    ResponseEntity<Booking> getBookingById(@PathVariable UUID bookingId){
       return ResponseEntity.ok(bookingService.getBooking(bookingId));
    }
    @GetMapping("/user/{userId}")
    ResponseEntity<List<Booking>> getBookingForUser(@PathVariable UUID userId){
        return ResponseEntity.ok(bookingService.getBookingByUser(userId));
    }
}
