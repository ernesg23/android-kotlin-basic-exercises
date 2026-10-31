# Android Studio & Kotlin Fundamentals - Basic Activities

A collection of three core mobile development exercises built using **Android Studio** and **Kotlin**, designed to demonstrate foundational user interface manipulation, event handling, logic implementation, and state management.

## Activities Included

### 1. Button Click & TextView Interaction
* **Description:** A simple interactive screen featuring a button and a `TextView`. When the user clicks the button, the text inside the `TextView` dynamically changes.
* **Key Concepts:** Event listeners (`setOnClickListener`), View binding/references, and UI state updates.

### 2. Basic Arithmetic Calculator (Only Addition)
* **Description:** An input-driven interface containing two text inputs (`EditText`) for numbers, an operation button, and a result container. It reads both inputs, performs the addition operation, and displays the result.
* **Key Concepts:** Input validation, parsing text to numeric types (`Int` / `Double`), and dynamic output rendering.

### 3. Login Authentication with Attempt Limiter
* **Description:** A security-focused screen handling a username, password, and a submit button. It validates credentials against a hardcoded string. 
* **Security Feature:** Tracks failed login attempts. If a user fails more than 3 times, the inputs are temporarily disabled, and a cooldown timer/wait period is enforced before allowing another attempt.
* **Key Concepts:** State tracking, conditional logic, UI disabling/enabling (`isEnabled = false`), and basic asynchronous handling or timers.

## Prerequisites & Tech Stack
* **IDE:** [Android Studio](https://developer.android.com/studio)
* **Language:** Kotlin
