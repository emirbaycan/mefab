# mefab

A modern, customizable Floating Action Button (FAB) menu library for Android, supporting drag, overlay, and expandable multi-action layouts.

[![Android](https://img.shields.io/badge/platform-Android-green.svg)](https://developer.android.com/)
[![License](https://img.shields.io/github/license/emirbaycan/mefab)](LICENSE)

---

## Features

* 🟢 **Expandable FAB**: Fan-style or edge menu support
* 🟢 **Drag & Overlay**: Can float above any app, or be embedded in your layout
* 🟢 **Custom Icons**: Default and custom icons supported
* 🟢 **Smooth Animations**: Expand/collapse with vector drawable support
* 🟢 **Modular & Lightweight**: Library-first, dependency-light

---

## Installation

Add to your `build.gradle` (Module) file:

```groovy
implementation 'io.github.emirbaycan:mefab:1.0.4'
```

> **Note:** Library is compatible with Android API 23+.

---

## Usage

### XML

```xml
<io.emirbaycan.mefab.MovableExpandedFloatingActionButton
    android:id="@+id/me_fab"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:edges="@menu/fabs_menu"/>
```

### Programmatically

```kotlin
FloatingFabOverlayManager.showOverlay(context)
```

### Overlay Mode

For "system overlay" (floating above all apps), request the `SYSTEM_ALERT_WINDOW` permission and use `FloatingFabOverlayManager`.

---

## Demo

<img src="https://raw.githubusercontent.com/emirbaycan/mefab/main/screenshots/showcase.gif" width="300"/>

---

## Testing

Run instrumented UI tests via Android Studio or CLI:

```sh
./gradlew connectedAndroidTest
```

or run `FabOverlayInstrumentedTest` for overlay integration test.

---

## Contributing

Pull requests and issues are welcome!
Please open an issue for suggestions or bugs.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Author

* Emir Baycan [@emirbaycan](https://github.com/emirbaycan)

---

## Advanced

* **Customizing fan angles, edges, and overlay behaviors:** See Wiki (coming soon).
* **Want to publish your own version?** Fork and edit the `ext` Gradle variables.

---
