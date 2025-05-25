package com.hotelbooking.booking_service.service;

import com.hotelbooking.booking_service.dto.CreateBookingRequest;
import com.hotelbooking.booking_service.entity.Booking;
import com.hotelbooking.booking_service.entity.BookingStatus;
import com.hotelbooking.booking_service.kafka.BookingEvent;
import com.hotelbooking.booking_service.kafka.BookingEventProducer;
import com.hotelbooking.booking_service.repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
   private final BookingEventProducer bookingEventProducer;

    public Booking createBooking(CreateBookingRequest bookingRequest){
        Booking booking = new Booking();
        booking.setId(UUID.randomUUID());
        booking.setUserId(bookingRequest.getUserId());
        booking.setRoomId(bookingRequest.getRoomId());
        booking.setCheckIn(bookingRequest.getCheckIn());
        booking.setCheckOut(bookingRequest.getCheckOut());
        booking.setStatus(BookingStatus.PENDING);
        BookingEvent bookingEvent = BookingEvent.builder()
                .bookingId(booking.getId())
                .roomId(booking.getRoomId())
                .roomId(booking.getRoomId())
                .checkIn(booking.getCheckIn())
                .checkOut(booking.getCheckOut())
                .status(booking.getStatus().name())
                .build();
        bookingEventProducer.sendBookingEvent(bookingEvent);
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingByUser(UUID userId){
        return bookingRepository.findByUserId(userId);
    }

    public Booking getBooking(UUID bookingId){
        return bookingRepository.findById(bookingId)
                .orElseThrow(()->new EntityNotFoundException("Booking Not Found"));
    }
}
