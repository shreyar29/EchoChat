# 💬 EchoChat – Java Socket-Based Chat Application

EchoChat is a simple and interactive client–server chat application built using **Java**, **Swing**, and **Socket Programming**.  
It allows users to log in, connect to a central server, chat in real time, and send files.  
This project highlights networking fundamentals, multithreading, and Java GUI development.

---

## 🚀 Features
  ✔️ Simple login interface (Swing GUI)  
  ✔️ Real-time one-to-one messaging  
  ✔️ Supports multiple connected users  
  ✔️ File transfer support  
  ✔️ Lightweight and easy to run  
  ✔️ Beginner-friendly Java networking implementation  

---

## 🛠️ Tech Stack
  ⭐ **Java (JDK 8+)** — Core programming language  
  ⭐ **Java Swing** — For GUI components  
  ⭐ **TCP Sockets** — Client–Server communication  
  ⭐ **Multithreading** — Handles multiple clients concurrently  
  ⭐ **I/O Streams** — File transfer system  
  ⭐ **Eclipse IDE** — For running and managing the project  

---

## 📂 Project Structure
```bash
EchoChat/
│
├── src/com/echochat/
│ ├── Server.java              # Main server program
│ ├── Client.java              # Client program
│ ├── ClientHandler.java       # Handles each connected client
│ ├── LoginGUI.java            # Login interface
│ ├── ChatGUI.java             # Main chat window
│ └── FileTransfer.java        # Handles sending/receiving files
│
├── users.txt                  # Stores login credentials
├── LICENSE                    # MIT License
└── README.md                  # Documentation

```

## 📥 How to Clone & Run the Project

### 1️⃣ Clone the repository
```bash
git clone https://github.com/shreyar29/EchoChat.git
cd EchoChat
```
2️⃣ Compile the Code
```bash
javac src/com/echochat/*.java
```
3️⃣ Run the Server
```bash
java com.echochat.Server
```
4️⃣ Run the Client (open a new terminal for each client)
```bash
java com.echochat.Client
```
🧪 For Developers / Contributors
🔹 Create a new branch
```bash
git checkout -b feature-name
```
🔹 Stage & commit changes
```bash
git add .
git commit -m "Describe your update"
```
🔹 Push the branch
```bash
git push origin feature-name
```
📜 License

This project is licensed under the MIT License.

You are free to use, modify, and distribute this software with proper attribution.

❤️ Author

Shreya R
