package com.hotelbooking.booking_service.repository;

import com.hotelbooking.booking_service.entity.Booking;
import com.hotelbooking.booking_service.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
  List<Booking> findByUserId(String userId);
}
