# Community preparation

1. Inventory provenance and observed hardware. Exclude vendor graphics, ROMs, BIOS, device secrets and private recovery material. Record unresolved license choices rather than inventing permission.
2. Add a keyed language dictionary with UTF-8 files, Italian/English initial coverage, six contributor locales, language selection and fallback. Preserve canonical category/action identifiers. Test missing keys, placeholder consistency, accent/CJK searches and language-independent filtering.
3. Replace vendor graphics with the original atlas/branding and text platform labels. Keep the established interaction and visual layout. Check long strings and CJK font fallback.
4. Add build instructions and an isolated preview package. Compile for API 19 and sign preview with an isolated development key. Never overwrite production or upload private signing material.
5. Inspect the preview on the real device in a bounded visual pass, correct observed defects and record the result. Keep the microSD transfer based on the previous accepted release until its own physical acceptance.
6. Deliver the local source tree and license/compatibility documentation. Do not create or publish a GitHub repository during this preparation.
