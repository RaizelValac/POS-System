T_T

---

# 🛒 Offline POS System (Point of Sale)

A robust, offline-first Android application designed for small businesses to manage inventory and process sales without an internet connection. Built using **Modern Android Development (MAD)** standards: Kotlin, Jetpack Compose, and Room Database.

## 📱 Features

* **Inventory Management:** Add, edit, and delete products with real-time stock tracking.
* **Transaction Processing:** "Sell" items with automatic price calculation.
* **Safe Stock Deduction:** Uses Atomic Transactions to ensure inventory is only reduced if a sale is successfully recorded.
* **Real-time Dashboard:** Live updates for Total Revenue and Inventory Count using Reactive Streams (`Flow`).
* **Sales History:** View a log of all past transactions.

## 🛠 Tech Stack

* **Language:** [Kotlin](https://kotlinlang.org/)
* **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material3)
* **Architecture:** MVVM (Model-View-ViewModel) with Clean Architecture principles.
* **Dependency Injection:** [Dagger Hilt](https://dagger.dev/hilt/)
* **Local Database:** [Room](https://developer.android.com/training/data-storage/room) (SQLite)
* **Concurrency:** Coroutines & Kotlin Flow.

## 🏗 Architecture & Design Decisions

This project follows a strict **Unidirectional Data Flow (UDF)** and **Separation of Concerns**.

### 1. Data Layer (Room & DAOs)

The app uses two distinct entities: `InventoryEntity` (Mutable state) and `SalesEntity` (Immutable history).

* **Key Senior Implementation:** The `sellItem` function in `SalesDao`.
* *Why:* To prevent data corruption (e.g., stock reducing but sale failing).
* *How:* We used the `@Transaction` annotation to wrap both the `INSERT` into Sales and `UPDATE` of Inventory into a single atomic operation.



### 2. Domain/Repository Layer

The `POSRepository` acts as the single source of truth. It abstracts the database operations from the UI, ensuring that the ViewModel never interacts with the DAO directly.

### 3. UI Layer (Compose & ViewModel)

* **State Management:** The UI observes a single `UiState` data class exposed by the ViewModel. This ensures the UI is always consistent with the database.
* **Reactive Updates:** We use `Flow` from the database all the way to the UI. If an item is sold, the "Inventory List" and "Total Revenue" on the dashboard update instantly without manual refreshes.

## 📂 Project Structure

```text
com.example.posapplication
├── data
│   └── local           # Room Entities (Tables) and DAOs
├── di                  # Hilt Dependency Injection Modules
├── repo                # Repository Layer (Data Logic)
├── ui
│   ├── screens         # Compose Screens (Sales, Inventory)
│   └── POSApp.kt       # Navigation Host
├── viewmodel           # ViewModel (State Holders)
└── MainActivity.kt     # Entry Point

```

## 🚀 How to Run

1. **Clone the repository.**
2. Open in **Android Studio** (Ladybug or newer recommended).
3. Sync Gradle files.
4. Run on an Emulator or Physical Device.
* *Note:* No internet connection is required.



## 🔮 Future Improvements

* **Cloud Sync:** Integrate `WorkManager` to sync local sales data to Firestore when online.
* **Barcode Scanning:** Use CameraX to scan items instead of tapping them in the list.
* **Receipt Printing:** Integrate Bluetooth thermal printer SDK.
