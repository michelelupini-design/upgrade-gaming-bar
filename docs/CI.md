# Prepared checks

The workflow runs the local model/navigation/language/source contracts with JDK 17 on Ubuntu 24.04 through PowerShell. It uses read-only repository permission, does not persist checkout credentials and runs on pull_request (not pull_request_target). Checkout v7 and setup-java v6 are pinned to verified upstream tag commits.

The workflow runs on GitHub for pushes and pull requests. Check the Actions tab for the result of the exact commit being reviewed; a local pass alone is not a hosted CI result. APK compilation and physical-device acceptance remain separate checks.
