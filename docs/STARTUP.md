# Startup presentation: reference-device notes

The reference console supports an original native boot video. Three replacement UpGrade/Q11 scenes (neon arcade, cosmic portal and red/blue control deck) were prepared, with next-boot random selection that avoids consecutive repetition.

Each lasts 13 seconds, within the prior presentation length, and uses 1280x720 H264 Constrained Baseline 3.1, 30 fps and silent AAC. A small animated indeterminate sweep and AVVIO IN CORSO explain the wait. It is not a measured percentage and does not add a minimum delay to the launcher.

These notes describe a tested private integration, not an installer supplied by this app. The native player and bootloader were not replaced; the device's boot-video path points to data-managed presentations. Originals and the prior startup procedure were preserved. The system partition returned to read-only after the one-time path update.

Important hardware finding: native player exit does not prove Android is visible. On this Amlogic implementation the last video frame could remain above a resumed HOME activity. The private startup/preview cleanup sets the video blackout policy and clears the video plane after handing control to HOME. This was checked at runtime and through a real reboot; the owner confirmed menu and joystick operation. Approximately two seconds of black transition were observed and remain a known cosmetic issue.

Do not copy board-specific root scripts to another device without inspecting its player, mount layout and video plane behavior. The portable launcher preview does not change system media, install maintenance services, or run these private scripts. The public tree contains no original vendor video, device backups or credentials.
