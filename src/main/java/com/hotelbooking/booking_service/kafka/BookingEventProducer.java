package com.hotelbooking.booking_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.hotelbooking.common.event.BookingEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingEventProducer {
    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;
    private static final String TOPIC_NAME = "booking-events";

    public void sendBookingEvent(BookingEvent bookingEvent){
        log.info("Sending Event for booking {}",bookingEvent.getBookingId());
        kafkaTemplate.send(TOPIC_NAME, bookingEvent);
    }
}
