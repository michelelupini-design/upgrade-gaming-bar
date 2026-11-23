# Observed integration, not a compatibility promise

Read directly from the running console on 22 November 2026:

| Item | Observed value |
|---|---|
| Android model / board property | n200 / n200 |
| Platform / CPU hardware string | meson8 / Amlogic Meson8 |
| Android / API | 4.4.2 / 19 |
| Kernel | 3.10.33 |
| ABI | armeabi-v7a |
| CPU | ARMv7, part 0xc09; exposed CPU range 0–3 |
| OS-visible memory | 814948 kB |
| Current output | 1280 × 720 landscape |
| Printed motherboard revision | Not freshly verified; do not infer it from the marketing name |

The console was sold with a Pandora-branded interface, but the community launcher is independent. Many different devices share similar branding. A matching label alone does not establish matching storage, kernel, controllers or firmware.

The production integration uses a two-player arcade control panel exposed through keyboard/gamepad events. `MenuActions.java`, `SearchKeyboard.java` and the existing navigation tests document the accepted event behavior. Button names in the guide describe this particular physical panel; other encoders need their own mapping and test.

RetroArch 1.22.2 ARM32 and its cores are installed independently in internal Android storage. The launcher starts RetroArch through an explicit activity intent. Core binaries, input profiles and BIOS must suit the user's independently prepared installation. The production 78%-sized gameplay window and exit shortcut are RetroArch configuration decisions, not features provided by copying this launcher alone.

For the private storage migration, the microSD is formatted exFAT, tested over its available capacity and then populated with the working content. The original partition layout is retained. A private boot adapter exposes the mounted microSD at the previous compatible content path, preserving existing catalog/configuration references. The generic build does not format media, flash firmware or install a root-maintenance service.

Wi-Fi has an intermittent history on the observed hardware; successful preview rendering does not prove network stability. The shop's network, maintenance PC and 32-inch display remain site acceptance items. Do not publish personal SSIDs, addresses, passwords or keys in a compatibility report.
