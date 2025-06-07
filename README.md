# Booking Service

## 📅 Overview

The **Booking Service** manages room bookings and availability for hotels. It ensures transactional consistency, availability checks via Redis, and publishes booking events using Kafka.

## 🚀 Features

- Book a hotel room for given dates
- Cancel a booking
- Real-time room availability check via Redis
- Kafka event publishing on successful bookings

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Redis**
- **Kafka**
- **PostgreSQL**
- **Swagger (OpenAPI)**
- **Lombok**
- **Maven**

## 📦 Modules

- **Booking Management** – Handles create & cancel operations
- **Redis Store** – Fast in-memory availability checks
- **Kafka Publisher** – Publishes booking events
- **Database Layer** – Stores booking history and metadata

## ⚙️ Setup

1. Start Kafka, Zookeeper, PostgreSQL, and Redis.
2. Update application properties with your configurations.
See all topic in kafka

   docker run -it --rm \
   --network="host" \
   bitnami/kafka:latest \
   kafka-topics.sh --bootstrap-server localhost:9092 --list

Describe Topic booking-events

docker run -it --rm \
--network="host" \
bitnami/kafka:latest \
kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic booking-events




Delete the topic:

docker run -it --rm \
--network="host" \
bitnami/kafka:latest \
kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic booking-events

To Delete a Topic:

docker run -it --rm \
--network="host" \
bitnami/kafka:latest \
kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic booking-events


To Consume Messages from the Topic
docker run -it --rm \
--network="host" \
bitnami/kafka:latest \
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic booking-events --from-beginning
