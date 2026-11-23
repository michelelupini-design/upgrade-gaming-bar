# Prepared checks

The workflow runs the local model/navigation/language/source contracts with JDK 17 on Ubuntu 24.04 through PowerShell. It uses read-only repository permission, does not persist checkout credentials and runs on pull_request (not pull_request_target). Checkout v7 and setup-java v6 are pinned to verified upstream tag commits.

The workflow is prepared locally. Its first hosted GitHub run remains pending publication; a local pass is not a claim that hosted CI passed. APK compilation and physical-device acceptance are separate checks.
