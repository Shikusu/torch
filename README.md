# 🔦 Torch

A minimal, elegant flashlight app for Android — rear flash, front-screen flash, and SOS Morse code mode in one clean interface.

---

## Features

- **Rear Flash** — toggles your device's camera flashlight on/off with a single tap
- **Front Screen Flash** — turns the screen full white at max brightness for use as a soft front fill light
- **SOS Mode** — transmits the international SOS signal (· · · — — — · · ·) via the rear flash
- **Swipe Selector** — drag or tap to switch between rear and front mode; snaps with a smooth 200ms animation

---

## Screenshots

<img width="1016" height="2048" alt="screen" src="https://github.com/user-attachments/assets/f3c64c16-0e8d-4ec0-b2ec-a3fb0a1d76fb" />


---

## Getting Started

### Requirements

- Android 7.0 (API 24) or higher
- A device with a camera (flash is optional — the front screen mode works on any device)

### Build & Run

1. Clone the repo:
   ```bash
   git clone https://github.com/Shikusu/torch.git
   cd torch
   ```

2. Open in **Android Studio**.

3. Build and run on a device or emulator:
   ```bash
   ./gradlew assembleDebug
   ```

### Permissions

The app requests a single permission:

| Permission | Reason |
| `CAMERA`   | Required to control the flashlight via Camera2 API |

---

## Project Structure

```
app/src/main/
├── java/shi/application/torch/
│   ├── MainActivity.java               # UI logic, flash and screen toggle
│   └── methods/
│       ├── FrontScreenSettings.java    # Screen brightness + white background
│       └── MorseFlashLightManager.java # SOS pattern on a background thread
└── res/
    ├── layout/activity_main.xml        # Main UI layout
    ├── drawable/                        # Vector drawables (dial states, selector, SOS button)
    └── values/                          # Colors, strings, themes
```

### Key Classes

**`MainActivity`**
Handles all UI interactions: the on/off dial, the front/rear mode selector, and the SOS button. Manages `isFlashOn`, `isScreenBright`, and `isSOSRunning` state flags.

**`MorseFlashLightManager`**
Runs the SOS sequence (`· · · — — — · · ·`) on a dedicated `HandlerThread` so the UI stays responsive. Accepts a completion callback `Runnable` to notify the activity when the sequence finishes.

**`FrontScreenSettings`**
Sets screen brightness to maximum and the window background to white to simulate a front-facing fill flash. Restores system defaults on disable.

---

## How SOS Works

The SOS pattern follows standard Morse code timing (1 unit = 250ms):

```
· · ·  — — —  · · ·
1  1  1  3  3  3  1  1  1
```

- Dot = flash for 1 unit (250ms)
- Dash = flash for 3 units (750ms)
- Inter-symbol gap = 1 unit silence
- Inter-group gap = 2 unit silence
- End pause = 7 unit silence before callback fires

The sequence is non-repeating. The SOS button is disabled if the rear flash is already on, or if front mode is active.

---

## Tech Stack

- **Language:** Java
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 36
- **Flash API:** [`CameraManager`](https://developer.android.com/reference/android/hardware/camera2/CameraManager) (Camera2)
- **Build:** Gradle with Kotlin DSL (`build.gradle.kts`)

---

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you'd like to change.

---

## License

[MIT](LICENSE)
