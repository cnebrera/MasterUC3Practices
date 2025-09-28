# Lesson 4: Last-Mile Messaging System

## Overview
This project demonstrates real-time price updates using WebSocket and Long Polling technologies in Spring Boot.

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
java -jar target/Lesson4-1.0.0-SNAPSHOT.jar

# Run with custom sleep time
java -jar target/Lesson4-1.0.0-SNAPSHOT.jar 1000
```

## Access the Application
Once started, open your browser and go to:
- **Main Application:** http://localhost:8080
- **Long Polling API:** http://localhost:8080/prices/long-polling
- **WebSocket:** ws://localhost:8080/prices/websocket

## Features
- **WebSocket:** Real-time bidirectional communication
- **Long Polling:** HTTP-based real-time updates
- **Live Statistics:** Messages per second, elapsed time, total messages
- **Interactive UI:** Switch between WebSocket and Long Polling modes

## Learning Objectives
Students will implement TODOs to understand:
- Spring Boot configuration and dependency injection
- WebSocket vs Long Polling differences
- Real-time messaging patterns
- Client-server communication protocols
