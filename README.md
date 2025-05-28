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
