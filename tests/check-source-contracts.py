from pathlib import Path
import re,json
root=Path(__file__).resolve().parents[1]
source=root/'src/com/upgrade/pandora'
resources=json.loads((root/'docs/string-source-map.json').read_text(encoding='utf-8'))
texts={p.name:p.read_text(encoding='utf-8') for p in source.glob('*.java')}
for name,text in texts.items():
 for key in re.findall(r'UiText\.(?:get|format)\("([a-z0-9_.]+)"',text):assert key in resources,(name,key)
main=texts['MainActivity.java']
assert '.putString("category","Cerca un gioco")' in main
assert 'j.optString("category","Classici")' in main
assert 'editSearch("CANCELLA")' in main
assert 'putString("category",UiText' not in main
assert 'editSearch(UiText' not in main
assert not any(x in ''.join(texts.values()) for x in ('original-GameMenu.png','pandora-saga-original.png'))
assert 'name.equals(' not in texts['ConsoleSettingsActivity.java'], 'Saved networks must not be selected by hard-coded personal names'
assert json.loads((root/'assets/catalogo.json').read_text(encoding='utf-8'))['games']==[]
for p in root.rglob('*'):
 if p.is_file() and 'build' not in p.relative_to(root).parts:
  assert p.suffix.lower() not in {'.bin','.gba','.gb','.gbc','.nes','.sfc','.smc','.pce','.iso','.chd','.so','.img','.p12','.apk'},p
print('SOURCE_CONTRACTS_OK: stable IDs, keyed strings, generic saved-network selection, no vendor graphics or ROM payloads')
