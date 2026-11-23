# Validation status — 23 November 2026

## Launcher and clean build

The source targets Java 7 bytecode / Android API 19, built with JDK 17, D8 and legacy v1 signing. The build creates a local development-only key; it never accesses the production identity. The development preview has a distinct package, no HOME category and an empty content root.

Local checks cover navigation/back destinations, button feedback, sorting/search, language fallback, UTF-8, format placeholders, accent normalization, preserved CJK characters and stable canonical category/action IDs. Source checks enforce an empty catalog and excluded payloads. A GitHub test workflow is prepared; remote CI is not claimed to have run before publication.

The isolated preview previously exercised English, Simplified Chinese and Japanese rendering, the English search keyboard and the illustrated guide. This is not a native-speaker review of every locale or a physical-panel test in every language.

## Reference device

On the n200/meson8 Android 4.4.2 reference device, the production multilingual launcher is installed. The replacement microSD passed a full available-capacity write/read check and content hash verification. Native card boot with USB removed succeeded. Representative catalog launch and normal injected exit passed for PlayStation Tetrade, SNES Axelay and PC Engine Pac-Land.

Owner gameplay checks on selected games confirmed the physical panel, audio and return to the launcher. Automated launch success does not establish complete playability, every level, or all 484 private main-catalog entries.

## Startup presentation

Three original branded scenes completed native player tests. Next-boot selection was checked for 24 choices, no immediate repetition, and same-boot idempotence. The device-specific startup path is separate from the portable app and has preserved originals/rollback copies.

A real owner report exposed a gap in the first tests: native playback had exited and HOME was resumed, yet the Amlogic video plane retained its last frame above Android. Cleanup now explicitly clears that plane after returning HOME. A regression checked active video during playback and cleared video afterward, followed by a real reboot. The owner confirmed visible UpGrade and working joystick. A roughly two-second black handoff remains; it is not claimed seamless.

## Network and remaining site checks

Wi-Fi without Ethernet passed a checked transfer, five minutes without maintenance traffic, a controlled reconnect and a physical power-off/cold boot. Earlier authentication failures remain unresolved history; these checks do not establish indefinite reliability. The reference LAN path remains a recovery option.

The venue's network, maintenance PC and 32-inch display still need on-site acceptance. Other board revisions, encoders and Android builds are unverified. No Android/bootloader upgrade or universal recovery installer is included.
