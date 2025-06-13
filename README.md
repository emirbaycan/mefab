# MeFab

[![GitHub stars](https://img.shields.io/github/stars/emirbaycan/mefab.svg?style=flat-square)](https://github.com/emirbaycan/mefab/stargazers)
[![GitHub license](https://img.shields.io/github/license/emirbaycan/mefab.svg?style=flat-square)](https://github.com/emirbaycan/mefab/blob/main/LICENSE)

A modern and customizable **Floating Action Button (FAB) Overlay** library for Android.
Designed to be imported and integrated quickly into your projects.

---

## Features

* Easy and fast integration
* Modern, sleek, and dynamic FAB overlay design
* Drag & drop support
* Minimal dependencies, lightweight setup

---

## Installation

### 1. Add the Module to Your Project

Import this library directly into your project.

**A. Clone the repository:**

```bash
git clone https://github.com/emirbaycan/mefab.git
```

or

**B. Add as a Git submodule:**

```bash
git submodule add https://github.com/emirbaycan/mefab.git
```

---

### 2. Add to build.gradle

In your `settings.gradle` or the project-level `build.gradle`, include the module:

```gradle
include ':mefab'
project(':mefab').projectDir = new File(rootDir, 'mefab')
```

---

### 3. Add as a Dependency

In your `app/build.gradle` file, add:

```gradle
implementation project(':mefab')
```

---

## Usage

### Quick Example

Add the following code in your Activity to show the FAB overlay and set up menu actions:

```java
private void setupFabOverlay() {
    io.emirbaycan.mefab.utils.FloatingFabOverlayManager.INSTANCE.showOverlay(this);
    MovableExpandedFloatingActionButton fabOverlay =
            io.emirbaycan.mefab.utils.FloatingFabOverlayManager.INSTANCE.getFabView();
    if (fabOverlay != null) {
        fabOverlay.setMenu(R.menu.fabs_menu); // Attach your menu XML
        fabOverlay.setOnEdgeFabClickListener(new OnEdgeFabClickListener(fabId -> {
            Log.d("EdgeFab", "Clicked FAB id=" + fabId);
            if (fabId == R.id.fab_record) {
                // Your record action
            } else if (fabId == R.id.fab_screenshot) {
                // Your screenshot action
            } else if (fabId == R.id.fab_brush) {
                // Your brush action
            } else if (fabId == R.id.fab_live) {
                // Your live action
            }
            return null;
        }));
    }
}
```

#### Example menu (res/menu/fabs\_menu.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/fab_record"
        android:icon="@drawable/icon_record_start"
        android:title="Record" />
    <item
        android:id="@+id/fab_brush"
        android:icon="@drawable/icon_brush"
        android:title="Brush"/>
    <item
        android:id="@+id/fab_screenshot"
        android:icon="@drawable/btn_screenshot"
        android:title="Screenshot"/>
    <item
        android:id="@+id/fab_live"
        android:icon="@drawable/icon_live"
        android:title="Live"/>
</menu>
```

* **Note:** You must create the appropriate menu XML file in your `res/menu` directory, and provide the required drawable resources.

For more details and advanced usage, see [docs/usage.md](docs/usage.md).
(Documentation is being developed)

---

## Developer Notes

* Minimum Android SDK: **21**
* Make sure to request the necessary permissions (such as SYSTEM\_ALERT\_WINDOW) for overlays.
* The library can be opened and closed directly within your app, no external dependencies required.
* Please report bugs or feature requests in the Issues section.

---

## Contributing

You can contribute by submitting a PR or sharing your suggestions in the [Issues](https://github.com/emirbaycan/mefab/issues) section.

---

## License

[MIT License](LICENSE)

---
