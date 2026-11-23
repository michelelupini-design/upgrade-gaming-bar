# Try the interface without replacing a working system

1. Read the verified hardware boundary. The app is tested on one n200/meson8 Android 4.4.2 installation, not every Pandora-branded box.
2. Build with `-Preview`. The output package is `com.upgrade.arcade.preview`, development-signed, with no HOME category. Install that preview using your already authorized Android tooling.
3. Launch the preview from Android. It has no bundled games. Test the UI, keyboard, guide and languages before considering content integration.
4. Run the local checks and record the exact board, Android/API, controller and display. Remove the preview when finished.

The preview does not format storage, flash firmware or create a remote-maintenance service. It reads a separate preview content directory. An empty preview is expected; it is not evidence that your existing games were removed.

Production integration still assumes a particular content root, RetroArch package/activity and protected core path. The `IME` launch extra also names the input service observed on the reference installation. These values must be reviewed for another board. Making integration paths/input profiles configurable is a contribution target, not an existing universal feature.

The non-preview development APK uses the legacy production package identifier but a separate signing identity. It cannot update a privately signed production build. Do not uninstall a working launcher, clear its data or choose a new HOME just to bypass a signature mismatch.

The tested microSD compatibility adapter, production input/exit configuration, startup-video integration and private SSH setup are not included as a universal installation package. Keep a verified backup and a way to restore the previous HOME before any device-specific integration.
