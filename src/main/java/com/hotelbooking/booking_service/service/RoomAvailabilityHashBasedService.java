package com.hotelbooking.booking_service.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class RoomAvailabilityHashBasedService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PREFIX = "availability";
    private static final long TTL = 60 * 60 * 24 * 30; // 30 days in seconds

    public boolean isAvailable(UUID roomId, LocalDate checkIn, LocalDate checkOut) {
        for (LocalDate date = checkIn; !date.isAfter(checkOut); date = date.plusDays(1)) {
            String key = getKey(roomId, date);
            Object status = redisTemplate.opsForHash().get(key, "status");
            log.info("Room availability for key {} status {}", key, status);
            if ("booked".equals(status)) {
                return false;
            }
        }
        return true;
    }

    public void markAsBooked(UUID roomId, LocalDate checkIn, LocalDate checkOut) {
        for (LocalDate date = checkIn; !date.isAfter(checkOut); date = date.plusDays(1)) {
            String key = getKey(roomId, date);
            redisTemplate.opsForHash().put(key, "status", "booked");
            log.info("Mark booked for key {} status", key);
            redisTemplate.expire(key, Duration.ofSeconds(TTL));
        }
    }

    public void cancelBooking(UUID roomId, LocalDate checkIn, LocalDate checkOut) {
        for (LocalDate date = checkIn; !date.isAfter(checkOut); date = date.plusDays(1)) {
            String key = getKey(roomId, date);
            log.info("Cancel booking for key {} status", key);
            redisTemplate.delete(key);
        }
    }

    private String getKey(UUID roomId, LocalDate date) {
        return String.format("%s:%s:%s", PREFIX, roomId.toString(), date);
    }
}

