# Android-Messenger Chat App.
A real-time one-to-one chat application for Android built using
Kotlin and Firebase, following MVVM Clean Architecture principles.

## Features
- Email & Password Authentication (Sign up / Login)
- Real-time one-to-one messaging
- Contact list showing all registered users
- Auto-generated chat room ID per user pair
- Message timestamps
- Persistent login using DataStore Preferences

##  Architecture
This project follows MVVM Clean Architecture with clear separation of layers:

- **Presentation** — Activities, ViewModels, Adapters
- **Domain**       — Use Cases, Repository Interfaces
- **Data**         — Firebase Repository Implementations, Models

## 🛠️ Tech Stack
| Layer | Technology |
|---|---|
| Language | Kotlin |
| Architecture | MVVM + Clean Architecture |
| Authentication | Firebase Auth |
| Database | Firebase Realtime Database |
| Dependency Injection | Hilt |
| Local Storage | DataStore Preferences |
| UI | ViewBinding, RecyclerView |

## 📁 Project Structure
