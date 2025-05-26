package com.hotelbooking.booking_service.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingEvent {
    private UUID bookingId;
    private String userId;
    private UUID roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String status;
}
