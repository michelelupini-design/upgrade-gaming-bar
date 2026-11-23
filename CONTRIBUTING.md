# Contributing

This is an experimental launcher. Code contributions are submitted under MIT; artwork contributions must identify their rights and applicable scope. Read LICENSE, ASSET-LICENSES.md and BRAND-POLICY.md before including assets. Start with docs/ROADMAP.md for bounded contribution targets.

For translations, edit the UTF-8 file under `assets/i18n/`. Keep keys stable, preserve `%s` / `%d` parameters and use `\n` for a deliberate line break. Missing entries fall back to English. Do not duplicate English strings merely to make a locale look complete. Native-language review and a screenshot on the target display are useful; mark which checks you actually performed.

Italian/English have initial full coverage. Spanish, Portuguese, German, French, Simplified Chinese and Japanese are partial starters. CJK font rendering and normalized searches are supported; a joystick-operated CJK input method is a separate contribution. Never translate canonical model/filter/action IDs or ROM names as a side effect of interface localization.

For compatibility reports, include the printed board revision when known, Android/API version, CPU/ABI, launcher version, RetroArch/core version, storage layout, controller type and the exact action that fails. Separate file validation, automated launch and actual human play. Include a minimal reproducible example with content you have permission to share.

For code changes, retain Java 7 / API 19 compatibility and the established back/exit controls. Run the source and language contract tests. Explain any change to storage paths or canonical IDs, and test language switching without losing favorites or changing filters.

Do not submit ROMs, BIOS, vendor firmware, personal network names/passwords, private keys, production signing identities, full device backups or copyrighted game screenshots. Link to the original author's authorized release where relevant.
