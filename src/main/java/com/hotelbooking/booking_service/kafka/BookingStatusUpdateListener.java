package com.hotelbooking.booking_service.kafka;


import com.hotelbooking.booking_service.entity.Booking;
import com.hotelbooking.booking_service.repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.hotelbooking.booking_service.entity.BookingStatus;
import com.hotelbooking.common.event.BookingEvent;

@Component
@RequiredArgsConstructor
@Slf4j

public class BookingStatusUpdateListener {

    private final BookingRepository bookingRepository;
    @KafkaListener(topics = "booking-confirmation-events", groupId = "booking-service")
    public void handleBookingStatusUpdate(BookingEvent event) {
        log.info("Received booking confirmation event: {}", event);

        Booking booking = bookingRepository.findById(event.getBookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found: " + event.getBookingId()));

        switch (event.getStatus()) {
            case "CONFIRMED" -> booking.setStatus(BookingStatus.CONFIRMED);
            case "REJECTED" -> booking.setStatus(BookingStatus.REJECTED);
            default -> {
                log.warn("Unknown booking status: {}", event.getStatus());
                return;
            }
        }

        bookingRepository.save(booking);
        log.info("Booking {} updated to status: {}", booking.getId(), booking.getStatus());
    }
}
