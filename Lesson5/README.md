# Lesson 5: Last-Mile Messaging System

## Overview
This project demonstrates real-time price updates using WebSocket and Long Polling technologies in Spring Boot.

## Prerequisites

### SSL Configuration
The application is configured to run with HTTPS/WSS. You'll need to set up the necessary SSL components before running the application. Check the `application.properties` file to understand what's required.

## How to Run

### Option 1: Spring Boot Maven Plugin (Recommended)
```bash
# Start the application
mvn spring-boot:run

# Start with custom sleep time
mvn spring-boot:run -Dspring-boot.run.arguments="1000"
```

### Option 2: Java Command
```bash
# Compile the project
mvn clean compile

# Run the application
java -jar target/Lesson5-1.0.0-SNAPSHOT.jar

# Run with custom sleep time
java -jar target/Lesson5-1.0.0-SNAPSHOT.jar 1000
```

### Option 3: Run Cryptographic Benchmark
```bash
# Run the cryptographic performance benchmark
mvn compile exec:java
```

This will execute the `Benchmark` class which compares AES (symmetric) and RSA (asymmetric) encryption performance.

## Access the Application
Once started, open your browser and go to:
- **Main Application:** https://localhost:8443
- **Long Polling API:** https://localhost:8443/prices/long-polling
- **WebSocket:** wss://localhost:8443/prices/websocket

**Note:** The application uses HTTPS/WSS for secure communication. Your browser may show a security warning due to the self-signed certificate - this is normal for development and you can safely proceed.

## Features
- **WebSocket:** Real-time bidirectional communication
- **Long Polling:** HTTP-based real-time updates
- **Live Statistics:** Messages per second, elapsed time, total messages
- **Interactive UI:** Switch between WebSocket and Long Polling modes

## Learning Objectives
Students will implement TODOs to understand:
- HTTPS/WSS secure communication setup
- SSL certificate generation and keystore management
- Cryptographic performance analysis (AES vs RSA)
