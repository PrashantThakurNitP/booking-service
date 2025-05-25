package com.hotelbooking.booking_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateBookingRequest {
    @NotNull
    private String userId;
    @NotNull
    private UUID roomId;
    @NotNull
    private LocalDate checkIn;
    @NotNull
    private LocalDate checkOut;
}
