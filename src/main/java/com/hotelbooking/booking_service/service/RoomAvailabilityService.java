package com.hotelbooking.booking_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomAvailabilityService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String PREFIX = "room_availability";

    public boolean isAvailable(UUID roomId, LocalDate checkIn, LocalDate checkOut){
        for(LocalDate date = checkIn; !date.isAfter(checkOut); date = date.plusDays(1)){
            String key = PREFIX + roomId + ":" + date.toString();
            if("booked".equals(redisTemplate.opsForValue().get(key))){
                return false;
            }
        }
        return true;

    }

  public void markAsBooked(UUID roomId, LocalDate checkIn, LocalDate checkOut) {
    for (LocalDate date = checkIn; !date.isAfter(checkOut); date = date.plusDays(1)) {
      String key = PREFIX + roomId + ":" + date.toString();
      redisTemplate.opsForValue().set(key, "booked");
         }
    }


  public void cancelBooking(UUID roomId, LocalDate checkIn, LocalDate checkOut){
      for (LocalDate date = checkIn; !date.isAfter(checkOut); date = date.plusDays(1)) {
          String key = PREFIX + roomId + ":" + date.toString();
          redisTemplate.delete(key);
      }

  }
}
