# License and provenance review — 23 November 2026

Publisher: Q11 Space Otherfold Games. Product: UpGrade Gaming Bar. The owner explicitly confirmed use of the complete Q11 mark and stated authorization to use the UpGrade brand. Brand permission is specified in BRAND-POLICY.md.

This is a component inventory and publication checklist, not a claim that the entire console, existing game collection or vendor firmware has been cleared for redistribution.

| Component | Evidence / status | Community preparation |
|---|---|---|
| UpGrade launcher Java, build/test scripts and documentation | Project-authored source; source review finds no bundled emulator implementation | MIT applied with owner confirmation, 23 November 2026. |
| VT323 font | SIL Open Font License 1.1, included copyright/license notice | Included unmodified with `assets/theme/fonts/VT323-OFL.txt`. |
| Complete Q11 Space Otherfold Games publisher mark | Owner explicitly selected the existing complete mark, 22 November 2026 | Original full mark retained as a reference; UI uses an owner-requested transparent horizontal arcade adaptation. See BRAND-POLICY.md for official redistribution and distinct fork identity. |
| UpGrade lettering, original button atlas and control-panel illustration | Project generation records; guide derived from owner's device photo; no vendor logo or game character present in retained illustration | Button atlas and control-panel illustration: CC BY 4.0, scoped in ASSET-LICENSES.md. UpGrade brand lettering remains under separate brand permission. |
| New four-scene background atlas and app startup image | Newly generated original project artwork, 22 November 2026, without copied vendor artwork or game characters | Background atlas: CC BY 4.0. Branded app-opening image: separate brand permission. No exclusive-rights or trademark clearance claim. |
| Original Pandora logos, GameMenu/Launcher graphics and firmware boot animation | No affirmative redistribution permission established for this exact vendor/model | Excluded. The preinstalled firmware remains on the device and is not a repository dependency to distribute. No assumption that buying a license is necessary or available. |
| Third-party platform wordmark images | Multiple source themes have different licensing/provenance; brand rights remain separate | Excluded from this preparation. Platforms are identified by text. |
| RetroArch | Upstream `COPYING` is GNU GPL version 3; other notices may apply | External application, not bundled. Any later binary distribution requires its corresponding obligations and notices. |
| Libretro emulator cores | Component/file-specific licenses; one license cannot cover all cores | No core binaries or source vendored. Verify the exact versions before any later bundle. |
| PandoryTool | Separate community project; upstream publishes GPL version 2 | Not bundled or used as a license for Pandora vendor material. Its license does not establish rights to the vendor's logo or firmware. |
| ROMs, BIOS, game screenshots and production catalog | Separate rights and provenance | Excluded; empty catalog supplied. Compatibility reports must not distribute game payloads. |
| Maintenance keys, private signing identity, Wi-Fi settings, backups and disk images | Device/private operational material | Excluded. No production credentials are required by the build. |

Primary references checked:

- [PandoryTool source and license](https://github.com/TeamPandory/pandorytool)
- [RetroArch COPYING](https://github.com/libretro/RetroArch/blob/master/COPYING)
- [VT323 OFL notice](https://github.com/google/fonts/blob/main/ofl/vt323/OFL.txt)
- [MAME 2016 upstream, component licensing context](https://github.com/libretro/mame2016-libretro)
- [GitHub repository licensing](https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/customizing-your-repository/licensing-a-repository)

Owner approved MIT code, scoped CC BY 4.0 non-brand artwork, OFL font and separate brand conditions on 23 November 2026. The exact source tree is checked before publishing; no emulator/game payloads or production-signed binaries are included.
