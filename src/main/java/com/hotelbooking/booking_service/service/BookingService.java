package com.hotelbooking.booking_service.service;

import com.hotelbooking.booking_service.dto.CreateBookingRequest;
import com.hotelbooking.booking_service.entity.Booking;
import com.hotelbooking.booking_service.entity.BookingStatus;
import com.hotelbooking.booking_service.kafka.BookingEventProducer;
import com.hotelbooking.booking_service.repository.BookingRepository;
import com.hotelbooking.common.event.BookingEvent;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    private final BookingRepository bookingRepository; // final just helps enforce constructor injection.
   private final BookingEventProducer bookingEventProducer;
   private final RoomAvailabilityHashBasedService roomAvailabilityService;

    public Booking createBooking(CreateBookingRequest bookingRequest){
        if(!roomAvailabilityService.isAvailable(bookingRequest.getRoomId(), bookingRequest.getCheckIn(), bookingRequest.getCheckOut())){
            throw new IllegalStateException("Room is unavailable");
        }
        log.info("Room is available");
        Booking booking = new Booking();
        booking.setId(UUID.randomUUID());
        booking.setUserId(bookingRequest.getUserId());
        booking.setRoomId(bookingRequest.getRoomId());
        booking.setCheckIn(bookingRequest.getCheckIn());
        booking.setCheckOut(bookingRequest.getCheckOut());
        booking.setStatus(BookingStatus.PENDING);
        BookingEvent bookingEvent = BookingEvent.builder()
                .bookingId(booking.getId())
                .userId(booking.getUserId())
                .roomId(booking.getRoomId())
                .checkIn(booking.getCheckIn())
                .checkOut(booking.getCheckOut())
                .status(booking.getStatus().name())
                .build();
        bookingEventProducer.sendBookingEvent(bookingEvent);
        roomAvailabilityService.markAsBooked(bookingRequest.getRoomId(), bookingRequest.getCheckIn(), bookingRequest.getCheckOut());
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingByUser(String userId){
        return bookingRepository.findByUserId(userId);
    }

    public Booking getBooking(UUID bookingId){
        return bookingRepository.findById(bookingId)
                .orElseThrow(()->new EntityNotFoundException("Booking Not Found"));
    }

    public Booking cancelBooking(UUID bookingId, String userId) throws AccessDeniedException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new EntityNotFoundException("Booking Not Found"));
        if(!booking.getUserId().equals(userId)){
            throw new AccessDeniedException("You are not authorized to cancel this booking");
        }
        if(booking.getStatus().equals(BookingStatus.CANCELLED)){
            throw new IllegalStateException("Booking is already cancelled");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        BookingEvent event = BookingEvent.builder()
                .bookingId(booking.getId())
                .userId(booking.getUserId())
                .roomId(booking.getRoomId())
                .checkIn(booking.getCheckIn())
                .checkOut(booking.getCheckOut())
                .status(booking.getStatus().name())
                .build();

        bookingEventProducer.sendBookingEvent(event);
        roomAvailabilityService.cancelBooking(booking.getRoomId(), booking.getCheckIn(), booking.getCheckOut());
        return  bookingRepository.save(booking);
    }
}
