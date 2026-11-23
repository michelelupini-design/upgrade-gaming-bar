$ErrorActionPreference='Stop'
$project=$PSScriptRoot
$output=Join-Path $project 'build/tests'
New-Item -ItemType Directory -Force $output | Out-Null
$modelSources=@('UiDictionary','MenuModel','MenuActions','SearchKeyboard','ArcadeFeedback') | ForEach-Object { Join-Path $project ('src/com/upgrade/pandora/'+$_+'.java') }
$tests=Get-ChildItem "$project/tests" -Filter '*.java' | ForEach-Object FullName
& javac -encoding UTF-8 -d $output $modelSources $tests
if ($LASTEXITCODE -ne 0) { throw 'Test compilation failed' }
foreach ($name in @('ArcadeFeedbackTest','BackDestinationTest','ConsoleNavigationTest','LanguageCatalogTest','MenuBackTest','MenuModelTest','MenuSearchTest','SearchKeyboardTest')) {
 & java -cp $output $name
 if ($LASTEXITCODE -ne 0) { throw "Test failed: $name" }
}
& java -cp $output CommunityContractTest "$project/assets/i18n"
if ($LASTEXITCODE -ne 0) { throw 'Language/search contracts failed' }
& python "$project/tests/check-source-contracts.py"
if ($LASTEXITCODE -ne 0) { throw 'Source export contracts failed' }
