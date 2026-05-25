# Apple Maps Redirect

Open Apple Maps links on Android without getting stuck in the browser.

Apple Maps Redirect catches `maps.apple.com` links and shared Apple Maps text, converts the location into a Google Maps-compatible link, then lets you open it with Google Maps, Waze, or another installed maps app that handles `geo:` links.

## What It Does

- Opens Apple Maps links from chats, email, browsers, and shared text.
- Converts coordinates, addresses, place searches, and directions destinations.
- Shows Android's app chooser so you can pick Google Maps, Waze, Uber, or another compatible app.
- Includes a manual converter screen for pasting Apple Maps links directly.
- Works without accounts, tracking, analytics, or background services.

## Install

The easiest path is to install the APK from the project's GitHub Releases page:

1. Open the latest release: <https://github.com/stanley-projects/AppleMapsRedirect/releases/latest>
2. Download the APK attached to that release.
3. On your Android device, allow installation from your browser or file manager when prompted.
4. Open an Apple Maps link and choose Apple Maps Redirect.

Android may warn that the APK is from an unknown source. That is expected for apps installed outside the Play Store.

## Use

After installing, tap an Apple Maps link such as:

```text
https://maps.apple.com/?ll=37.7749,-122.4194&q=San%20Francisco
```

Android should offer Apple Maps Redirect as an option. Pick it, then choose the maps app you want to use.

You can also open Apple Maps Redirect from your launcher, paste a link, and copy or open the converted Google Maps link.

## Supported Links

Apple Maps Redirect currently understands:

- `ll=latitude,longitude`
- `daddr=latitude,longitude` or `daddr=address`
- `address=...`
- `q=...`
- `saddr=...`
- `/place/<name>` style paths
- Short links or redirect links when the device has network access

## Build From Source

Requirements:

- Android Studio or Android SDK
- JDK 17 or newer

Build a debug APK:

```powershell
.\gradlew.bat assembleDebug
```

The APK will be written to:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Privacy

Apple Maps Redirect does not collect, store, or transmit analytics. It may use the network only to resolve Apple Maps redirect links so the final destination can be parsed.

## Project Page

Visit the project front page at <https://stanley-projects.github.io/AppleMapsRedirect/>.
