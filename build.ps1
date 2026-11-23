param(
 [Parameter(Mandatory=$true)][string]$AndroidJar,
 [Parameter(Mandatory=$true)][string]$BuildTools,
 [switch]$Preview
)
$ErrorActionPreference='Stop'
$project=$PSScriptRoot
$build=Join-Path $project 'build'
New-Item -ItemType Directory -Force "$build/classes","$build/dex","$build/generated" | Out-Null
$manifest=Get-Content -LiteralPath "$project/AndroidManifest.xml" -Raw
if ($Preview) {
 $manifest=$manifest.Replace('package="com.upgrade.pandora"','package="com.upgrade.arcade.preview"').Replace('android:label="UpGrade Gaming Bar"','android:label="Q11 UpGrade Preview"')
 $manifest=$manifest.Replace('android:name=".ConsoleSettingsActivity"','android:name="com.upgrade.pandora.ConsoleSettingsActivity"').Replace('android:name=".MainActivity"','android:name="com.upgrade.pandora.MainActivity"')
 $manifest=$manifest.Replace('<intent-filter><action android:name="android.intent.action.MAIN"/><category android:name="android.intent.category.HOME"/><category android:name="android.intent.category.DEFAULT"/></intent-filter>','')
}
$manifestPath=Join-Path $build 'AndroidManifest.xml'
[IO.File]::WriteAllText($manifestPath,$manifest,[Text.UTF8Encoding]::new($false))
$aapt=Join-Path $BuildTools 'aapt.exe'
& $aapt package -f -m -M $manifestPath -I $AndroidJar -S "$project/res" -A "$project/assets" -J "$build/generated" -F "$build/resources.apk"
if ($LASTEXITCODE -ne 0) { throw 'Resource packaging failed' }
$sources=@(Get-ChildItem "$project/src" -Recurse -Filter '*.java' | ForEach-Object FullName)+@(Get-ChildItem "$build/generated" -Recurse -Filter '*.java' | ForEach-Object FullName)
if ($Preview) {
 # The isolated visual preview uses an empty public catalog, never private ROMs.
 $previewSource=Join-Path $build 'preview-src'
 New-Item -ItemType Directory -Force $previewSource | Out-Null
 $main=Get-Content -LiteralPath "$project/src/com/upgrade/pandora/MainActivity.java" -Raw
 $main=$main.Replace('/storage/external_storage/sda1/Pandora-Prototipo/','/storage/external_storage/sda1/UpGrade-Community-Preview/')
 [IO.File]::WriteAllText("$previewSource/MainActivity.java",$main,[Text.UTF8Encoding]::new($false))
 $sources=@($sources | Where-Object { [IO.Path]::GetFileName($_) -ne 'MainActivity.java' })+@("$previewSource/MainActivity.java")
}
& javac -encoding UTF-8 -source 7 -target 7 -Xlint:-options -bootclasspath $AndroidJar -d "$build/classes" $sources
if ($LASTEXITCODE -ne 0) { throw 'Java compilation failed' }
& jar cf "$build/classes.jar" -C "$build/classes" .
& java -cp "$BuildTools/lib/d8.jar" com.android.tools.r8.D8 --min-api 19 --lib $AndroidJar --output "$build/dex" "$build/classes.jar"
if ($LASTEXITCODE -ne 0) { throw 'DEX conversion failed' }
Copy-Item -LiteralPath "$build/resources.apk" -Destination "$build/unsigned.apk" -Force
Add-Type -AssemblyName System.IO.Compression.FileSystem
$archive=[IO.Compression.ZipFile]::Open("$build/unsigned.apk",[IO.Compression.ZipArchiveMode]::Update)
try { [IO.Compression.ZipFileExtensions]::CreateEntryFromFile($archive,"$build/dex/classes.dex",'classes.dex') | Out-Null } finally { $archive.Dispose() }
& "$BuildTools/zipalign.exe" -f 4 "$build/unsigned.apk" "$build/aligned.apk"
if ($LASTEXITCODE -ne 0) { throw 'APK alignment failed' }
# This isolated development key never replaces the production signing identity.
$debugKey=Join-Path $build 'development-only.p12'
if (-not (Test-Path -LiteralPath $debugKey)) {
 & keytool -genkeypair -keystore $debugKey -storetype PKCS12 -storepass android -keypass android -alias androiddebugkey -dname 'CN=UpGrade Development,O=Development,C=IT' -keyalg RSA -keysize 2048 -validity 3650
 if ($LASTEXITCODE -ne 0) { throw 'Development key generation failed' }
}
$output=Join-Path $build $(if($Preview){'UpGrade-Preview.apk'}else{'UpGrade-Community-development.apk'})
& "$BuildTools/apksigner.bat" sign --ks $debugKey --ks-key-alias androiddebugkey --ks-pass pass:android --min-sdk-version 19 --v1-signing-enabled true --v2-signing-enabled false --v3-signing-enabled false --out $output "$build/aligned.apk"
if ($LASTEXITCODE -ne 0) { throw 'APK signing failed' }
& "$BuildTools/apksigner.bat" verify --min-sdk-version 19 $output
if ($LASTEXITCODE -ne 0) { throw 'APK signature verification failed' }
Write-Output "Built development APK: $output"
