# Bridge — Open Apple Maps Links on Android

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/platform-Android%205.0%2B-3DDC84?logo=android&logoColor=white)](https://github.com/stanley-projects/Bridge/releases/latest)
[![Latest Release](https://img.shields.io/github/v/release/stanley-projects/Bridge?label=download&color=2f9d50)](https://github.com/stanley-projects/Bridge/releases/latest)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![No Tracking](https://img.shields.io/badge/no%20tracking-100%25-success)](#privacy)

**Bridge is a free, open-source Android utility that opens `maps.apple.com` links in Google Maps, Waze, or any other maps app of your choosing.** It runs silently in the background, intercepting Apple Maps links the moment you tap them — so iPhone-shared locations stop dead-ending in your mobile browser.

> Project home: <https://stanley-projects.github.io/Bridge/> · Download: <https://github.com/stanley-projects/Bridge/releases/latest>

---

## What is Bridge?

Bridge is an Android app that solves a single, very specific problem: when someone with an iPhone shares an Apple Maps link with you, Android has no native way to open it in your preferred maps app. The link opens in your browser, sits on a useless Apple Maps webpage, and offers no obvious path to navigation.

Bridge fixes that. After a one-time setup, every `maps.apple.com` link you tap routes silently through Bridge and opens directly in **Google Maps**, **Waze**, or any other maps app on your device that handles `geo:` links. No accounts, no analytics, no tracking, no background services.

## Key Features

- **Intercepts Apple Maps links automatically** from chats, email, browsers, and shared text on Android.
- **Converts every common Apple Maps URL format** — coordinates, addresses, place searches, directions destinations, named places, and short/redirect links.
- **Opens any maps app you have installed** — Google Maps, Waze, HERE WeGo, OsmAnd, Organic Maps, Magic Earth, or any app that supports Android's standard `geo:` intent.
- **Manual paste-and-convert screen** for the rare case where a link arrives as plain text inside an in-app webview.
- **Privacy-first**: no accounts, no analytics, no background services, no data collection. The network is only used to resolve Apple's short/redirect links.
- **Tiny footprint** — under 5 MB installed, runs on Android 5.0 (Lollipop) and newer.
- **Open source** under the [MIT License](LICENSE).

## Install

The fastest way to install Bridge on your Android device:

1. Open the latest release: <https://github.com/stanley-projects/Bridge/releases/latest>
2. Download the `Bridge-v*.apk` file attached to that release.
3. On your Android device, allow installation from your browser or file manager when prompted.

Android may warn that the APK is from an unknown source. That is expected for apps installed outside the Play Store. The APK is signed with a stable release key, so future updates installed from this repo will upgrade cleanly without reinstall.

## Set Up Auto-Bridging

For Bridge to open Apple Maps links automatically — without a chooser appearing every time — Android needs your one-time permission. The first time you launch Bridge, you'll see a welcome screen with an **Enable auto-bridging** button. Tap it; Android opens a settings page; switch on `maps.apple.com` under "Supported web addresses". Done — from then on, every Apple Maps link you tap routes silently through Bridge to your preferred maps app.

If you skip the onboarding, the main screen shows a small "Auto-bridging is off — tap to set up" prompt that links to the same settings page. You can run the setup whenever you like.

### Why does Android require this?

Bridge handles links to `maps.apple.com`, a domain it doesn't own. Android requires users to opt in per domain for any app that handles third-party links — otherwise a malicious app could silently hijack links to `paypal.com`, `youtube.com`, etc. This is a platform-level security boundary, not a Bridge limitation; every link-handling app of this type works the same way.

## Use

After setup, just tap any Apple Maps link. It opens in your preferred maps app silently.

You can also open Bridge from your launcher and paste a link manually:

```text
https://maps.apple.com/?ll=37.7749,-122.4194&q=San%20Francisco
```

This is useful for the rare case where a link arrives inside a chat app's in-app webview (some messaging apps render links in their own browser and never hand them to Android's intent system).

## Supported Apple Maps URL Formats

Bridge understands every common Apple Maps URL shape, including the legacy and modern formats:

| Format | Example |
|---|---|
| Coordinates (legacy) | `?ll=37.7749,-122.4194` |
| Coordinates (modern) | `?coordinate=37.7749,-122.4194` |
| Named location | `?coordinate=...&name=Golden+Gate+Bridge` |
| Address search | `?address=1+Infinite+Loop` |
| Free-text query | `?q=Tokyo+Station` |
| Directions destination | `?daddr=Yosemite+Valley` |
| Directions origin | `?saddr=...` |
| Place path | `/place/<name>` |
| Short / redirect links | `maps.apple.com/?...` short forms that resolve over the network |

When both a coordinate and a name are present, Bridge passes the coordinate for routing and preserves the name as a Google Maps label.

## Build From Source

Requirements:

- Android Studio Iguana or newer, or the Android SDK with command-line tools
- JDK 17 or newer
- Gradle 8.7+ (bundled wrapper handles this automatically)

Build a debug APK:

```powershell
.\gradlew.bat assembleDebug
```

The APK will be written to:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Frequently Asked Questions

### How do I open an Apple Maps link on Android?

Install Bridge from <https://github.com/stanley-projects/Bridge/releases/latest>, complete the one-time auto-bridging setup, and from then on every Apple Maps link you tap on your Android phone will open in Google Maps, Waze, or whichever maps app you prefer.

### How do I convert an Apple Maps link to Google Maps?

Open Bridge from your launcher, paste the `maps.apple.com` URL into the field, and tap **Convert**. Bridge produces a `geo:` link that opens in any installed maps app. Auto-bridging does the same thing silently when you tap a link directly.

### Does Bridge work with Waze, HERE WeGo, OsmAnd, or other navigation apps?

Yes. Bridge emits a standard Android `geo:` intent that any compliant maps app can handle. Android's app chooser lets you pick which one opens the link, and you can set a default.

### Does Bridge require an internet connection?

Only for Apple's short / redirect URLs, which must be resolved over the network. Direct `maps.apple.com/?ll=...` links with coordinates already in the URL work fully offline.

### Does Bridge collect data, analytics, or track location?

No. Bridge has no analytics SDK, no crash reporter, no background services, no account system, and no telemetry. It does not request location permission. The only network call it ever makes is to resolve an Apple short/redirect URL when one is shared with you.

### Is Bridge on the Google Play Store?

Not currently. Install the signed APK directly from the [GitHub Releases](https://github.com/stanley-projects/Bridge/releases/latest) page.

### Is Bridge open source?

Yes — released under the [MIT License](LICENSE). The full Kotlin source for the parser, both activities, and the onboarding flow lives in this repository.

### Will Bridge work on Android 5, 6, or 7?

Yes. Bridge targets minimum SDK 21 (Android 5.0 Lollipop) and runs on every Android version up through Android 15 and beyond.

### Is Bridge affiliated with Apple or Google?

No. Bridge is an independent open-source utility. It is not affiliated with, endorsed by, or sponsored by Apple Inc. or Google LLC.

## Privacy

Bridge does not collect, store, or transmit analytics, telemetry, or any personal data. It may use the network only to resolve Apple Maps short / redirect links so the final destination can be parsed. Nothing leaves your device beyond that one HTTP request to `maps.apple.com`.

## License

[MIT License](LICENSE) — see the LICENSE file for full text. You are free to use, modify, redistribute, and fork Bridge for any purpose, commercial or otherwise.

## Project Links

- **Project page:** <https://stanley-projects.github.io/Bridge/>
- **Latest release / download:** <https://github.com/stanley-projects/Bridge/releases/latest>
- **Source code:** <https://github.com/stanley-projects/Bridge>
- **Issue tracker:** <https://github.com/stanley-projects/Bridge/issues>

---

<sub>Keywords: open Apple Maps links on Android, Apple Maps to Google Maps Android, maps.apple.com Android, Apple Maps redirect Android, Apple Maps Waze Android, convert Apple Maps link, Android Apple Maps opener, iPhone shared location Android, geo URI handler, Android link handler for maps.apple.com.</sub>
