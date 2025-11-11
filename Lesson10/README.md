# FIX Protocol Exercise - Lesson 10

## 📁 What's in This Folder

This is a **FIX Protocol exercise** using QuickFIX/J library to implement financial market data communication. The exercise is divided into two parts:

- ✅ **Part 1:** Basic FIX session setup and message handling
- ✅ **Part 2:** Market data provider and subscriber implementation

## 🎯 Learning Objectives

By completing this exercise, you will learn:
1. ✅ FIX protocol message structure
2. ✅ QuickFIX/J library usage
3. ✅ Session management (acceptor/initiator)
4. ✅ Market data subscription patterns
5. ✅ Financial messaging systems

## 🚀 Quick Start

### Prerequisites
- **Java 21** or higher
- **Maven 3.6+**

Check your versions:
```bash
java -version    # Should show version 21
mvn -version     # Should show 3.6+
```

### Running the Components

**Terminal 1 - Start Market Data Provider (Acceptor):**
```bash
mvn exec:java@acceptor
```
Starts the market data provider that will send price updates.

**Terminal 2 - Start Market Data Subscriber (Initiator):**
```bash
mvn exec:java@initiator
```
Starts the subscriber that requests and receives market data.

### What You Should See

When both components are running successfully, you should see:
- Session creation and logon messages
- Market data request being sent (from Initiator)
- Price updates being received (on Initiator)
- No error messages in the console

## 📋 TODO Items to Complete

### Part 1: Basic FIX Session Setup

**AcceptorRunner.java:**
- **TODO 1.1:** Load acceptor configuration file using `getClassLoader().getResource()`
- **TODO 1.2:** Create SessionSettings from configuration file using `SessionSettings` constructor with `FileInputStream`

**InitiatorRunner.java:**
- **TODO 1.3:** Load initiator configuration file using `getClassLoader().getResource()`
- **TODO 1.4:** Create SessionSettings from configuration file using `SessionSettings` constructor with `FileInputStream`

**AcceptorApp.java:**
- **TODO 1.5:** Log the message type when receiving any message using `message.getHeader().getString(MsgType.FIELD)`

**InitiatorApp.java:**
- **TODO 1.6:** Log received message type using `message.getHeader().getString(MsgType.FIELD)`

### Part 2: Market Data Implementation

**InitiatorApp.java:**
- **TODO 2.1:** Add TSLA and NVDA symbols to your market data request
- **TODO 2.4:** Extract price (MDEntryPx) and symbol (Symbol) from incremental refresh messages

**AcceptorApp.java:**
- **TODO 2.2:** Extract MDReqID value from the MarketDataRequest message

**MarketDataService.java:**
- **TODO 2.3:** Set the MDEntryPx field with the price value for the symbol from the prices map

## 🛠️ Development Workflow

1. **Edit code** to complete a TODO
2. **Compile:** `mvn compile`
3. **Test manually:** Run acceptor + initiator
4. **Fix errors** if any
5. **Repeat** until everything works

## 📦 Project Structure

```
src/main/java/es/uc3m/fintech/lesson10/
├── AcceptorRunner.java      # Part 1: TODO 1.1, 1.2
├── AcceptorApp.java         # Part 1: TODO 1.5, Part 2: TODO 2.2
├── InitiatorRunner.java     # Part 1: TODO 1.3, 1.4
├── InitiatorApp.java        # Part 1: TODO 1.6, Part 2: TODO 2.1, 2.4
└── MarketDataService.java   # Part 2: TODO 2.3

src/main/resources/
├── acceptor.cfg             # Acceptor configuration
└── initiator.cfg            # Initiator configuration
```

## 💡 Key FIX Concepts Covered

- **Sessions:** Acceptor vs. Initiator
- **Messages:** MarketDataRequest, MarketDataIncrementalRefresh
- **Fields:** MDReqID, Symbol, MDEntryPx, etc.
- **Groups:** NoMDEntries, NoRelatedSym
- **Session Management:** Logon, Logout, Heartbeat

## ⚠️ Common Issues

### Port Already in Use
If you see "Address already in use" error:
```bash
# Find and kill process using port 10000
lsof -ti:10000 | xargs kill -9
```

### Compilation Issues
If you get compilation errors:
```bash
# Clean and rebuild
mvn clean compile
```

## 🎯 Tips for Success

- **Always start the acceptor FIRST** (it's the server)
- **Then start the initiator** (it's the client)
- **Wait 2-3 seconds** between starting components
- **Use Ctrl+C** to stop running components
- **Check logs** if something doesn't work
- **Read the TODO comments carefully** - they contain hints!
- **Complete TODOs in order** - Part 1 before Part 2

## 📚 Available Commands

| Command | What It Does |
|---------|--------------|
| `mvn compile` | Compile the code |
| `mvn clean` | Clean build artifacts |
| `mvn exec:java@acceptor` | Run acceptor |
| `mvn exec:java@initiator` | Run initiator |

---

**Happy coding!** 🎉

Complete the TODOs in order, test each part, and you'll have a working FIX protocol implementation!

