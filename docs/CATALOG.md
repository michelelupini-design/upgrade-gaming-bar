# Catalog contract

The shipped catalog is empty. A local catalog is a UTF-8 JSON object with `version: 2` and a `games` array. Do not submit real ROM files or BIOS to the repository.

Example structure with a fictional, non-bundled title:

```json
{
  "version": 2,
  "games": [{
    "id": "example-local-game",
    "title": "Example local game",
    "category": "Classici",
    "players": 1,
    "path": "/storage/external_storage/sda1/Pandora-Prototipo/example/example.nes",
    "core": "fceumm_libretro_android.so",
    "thumbnail": "",
    "catalog_visibility": "main"
  }]
}
```

This example does not include a game. Paths and core names must match an independently prepared installation. The reader rejects duplicate/empty IDs, traversal paths, unsupported path roots and malformed core names. Keep canonical category IDs unchanged when translating the interface. Game titles and identifiers are content, not interface strings.

The catalog reader has a 1 MiB safety limit. Gallery images are separate local files with at most two previews per game in the current model. Do not confuse entry count with verified full playability or permission to redistribute a title.
