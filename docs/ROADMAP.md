# Contribution targets

This is a small experimental launcher. Changes must preserve Java/API 19 support and the working back/exit behavior on the reference device.

Good first contributions:

- Review the 114 initial Italian/English interface strings, or complete one of the six partial locale files. Preserve placeholders; include a rendering check and distinguish native review from a machine draft.
- Report controller mappings for a precisely identified encoder/board. Document both players, confirm/back, credit/start and exit behavior.
- Improve layout under long titles, low-resolution displays and CJK fallback fonts, with before/after interface captures using fictional content.
- Make content/emulator/IME integration values configurable behind safe defaults. Include migration and back-navigation checks; do not silently change canonical catalog IDs.
- Investigate the startup black handoff or Wi-Fi authentication behavior on matching hardware. Separate player/layer or driver findings from launcher features; no unreviewed firmware flash procedure.
- Suggest original-author game releases with distribution terms and an exact compatibility report. Link to the authorized release rather than attaching game payloads.

Out of scope for the first source release: universal Pandora firmware, Android upgrade, PS2 emulation, ROM redistribution, arbitrary remote access, or a promise that every private catalog entry is fully playable.
