$projectPath = "C:\DEV\Lechgar\DrissLechgarRepo\SeleniumScripts\downloads\TP-10 ( id  164 )\TP10-Client-REST-Android-Retrofit-XML-JSON--main"
$appPath = "$projectPath\app"
$srcMainJava = "$appPath\src\main\java"
$oldPackagePath = "$srcMainJava\ma\projet\restclient"
$newPackagePath = "$srcMainJava\com\youssef\android\restclient"

# 1. Create New Package Structure
New-Item -ItemType Directory -Force -Path "$newPackagePath\data\api" | Out-Null
New-Item -ItemType Directory -Force -Path "$newPackagePath\data\repository" | Out-Null
New-Item -ItemType Directory -Force -Path "$newPackagePath\model" | Out-Null
New-Item -ItemType Directory -Force -Path "$newPackagePath\ui\adapter" | Out-Null
New-Item -ItemType Directory -Force -Path "$newPackagePath\config" | Out-Null

# 2. Move and Rename Core Files
# MainActivity -> BankDashboardActivity
if (Test-Path "$oldPackagePath\MainActivity.java") {
    Move-Item "$oldPackagePath\MainActivity.java" "$newPackagePath\BankDashboardActivity.java" -Force
}

# Entities
if (Test-Path "$oldPackagePath\entities\Compte.java") {
    Move-Item "$oldPackagePath\entities\Compte.java" "$newPackagePath\model\Account.java" -Force
}
# Move other entities if they exist
Get-ChildItem "$oldPackagePath\entities" -Filter "*.java" | Where-Object {$_.Name -ne "Compte.java"} | ForEach-Object {
    Move-Item $_.FullName "$newPackagePath\model" -Force
}

# Adapters
if (Test-Path "$oldPackagePath\adapter\CompteAdapter.java") {
    Move-Item "$oldPackagePath\adapter\CompteAdapter.java" "$newPackagePath\ui\adapter\AccountAdapter.java" -Force
}

# Repositories (if any file based repo logic exists, usually in 'repository' pkg)
if (Test-Path "$oldPackagePath\repository\CompteRepository.java") {
    Move-Item "$oldPackagePath\repository\CompteRepository.java" "$newPackagePath\data\repository\AccountRepository.java" -Force
}

# API Interfaces
# Assuming CompteApi.java exists or similar in 'api' package
if (Test-Path "$oldPackagePath\api") {
    Get-ChildItem "$oldPackagePath\api" -Filter "*.java" | ForEach-Object {
        $newName = $_.Name.Replace("Compte", "Account") # Rename CompteApi -> AccountApi
        Move-Item $_.FullName "$newPackagePath\data\api\$newName" -Force
    }
}

# Config
if (Test-Path "$oldPackagePath\config") {
     Get-ChildItem "$oldPackagePath\config" -Filter "*.java" | ForEach-Object {
        Move-Item $_.FullName "$newPackagePath\config" -Force
    }
}

# 3. Remove Old Directories
Remove-Item "$srcMainJava\ma" -Recurse -Force -ErrorAction SilentlyContinue

# 4. Content Replacement (Recursive in new path)
$filesToUpdate = Get-ChildItem -Path "$newPackagePath" -Recurse -Filter "*.java"

foreach ($file in $filesToUpdate) {
    $content = Get-Content $file.FullName -Raw

    # Package Declarations
    $content = $content -replace "package ma.projet.restclient;", "package com.youssef.android.restclient;"
    $content = $content -replace "package ma.projet.restclient.entities;", "package com.youssef.android.restclient.model;"
    $content = $content -replace "package ma.projet.restclient.adapter;", "package com.youssef.android.restclient.ui.adapter;"
    $content = $content -replace "package ma.projet.restclient.repository;", "package com.youssef.android.restclient.data.repository;"
    $content = $content -replace "package ma.projet.restclient.api;", "package com.youssef.android.restclient.data.api;"
    $content = $content -replace "package ma.projet.restclient.config;", "package com.youssef.android.restclient.config;"

    # Imports
    $content = $content -replace "import ma.projet.restclient.entities.Compte;", "import com.youssef.android.restclient.model.Account;"
    $content = $content -replace "import ma.projet.restclient.adapter.CompteAdapter;", "import com.youssef.android.restclient.ui.adapter.AccountAdapter;"
    $content = $content -replace "import ma.projet.restclient.api.*", "import com.youssef.android.restclient.data.api.*"
    $content = $content -replace "import ma.projet.restclient.config.*", "import com.youssef.android.restclient.config.*"
    $content = $content -replace "import ma.projet.restclient.R;", "import com.youssef.android.restclient.R;"

    # Class & Variable Names
    $content = $content -replace "MainActivity", "BankDashboardActivity"
    $content = $content -replace "CompteAdapter", "AccountAdapter"
    $content = $content -replace "CompteRepository", "AccountRepository"
    $content = $content -replace "Compte", "Account" # Careful replacement
    $content = $content -replace "compte", "account" # Variable case

    Set-Content -Path $file.FullName -Value $content
}

# 5. Update Manifest
$manifestPath = "$appPath\src\main\AndroidManifest.xml"
if (Test-Path $manifestPath) {
    $xml = Get-Content $manifestPath -Raw
    $xml = $xml -replace 'android:name=".MainActivity"', 'android:name=".BankDashboardActivity"'
    Set-Content -Path $manifestPath -Value $xml
}

# 6. Update Build.gradle.kts
$gradlePath = "$appPath\build.gradle.kts"
if (Test-Path $gradlePath) {
    $gradle = Get-Content $gradlePath -Raw
    $gradle = $gradle -replace 'namespace = "ma.projet.restclient"', 'namespace = "com.youssef.android.restclient"'
    $gradle = $gradle -replace 'applicationId = "ma.projet.restclient"', 'applicationId = "com.youssef.android.restclient"'
    Set-Content -Path $gradlePath -Value $gradle
}

# 7. Create README
$readmeContent = "# Bank Android Client

## Overview
A native Android application (Java/Retrofit) for managing bank accounts, communicating with a REST API.
Refactored by **Youssef Bahaddou**.

## Features
- **Retrofit Client**: Consumes REST APIs (JSON & XML support).
- **MVVM Pattern**: Organized into Data, Model, and UI layers.
- **RecyclerView**: Displays account lists efficiently.

## Configuration
- **API Base URL**: Configured in `config/ApiConfig.java` (or similar).

## Build
\`\`\`bash
./gradlew assembleDebug
\`\`\`

## Author
Youssef Bahaddou
"
Set-Content -Path "$projectPath\README.md" -Value $readmeContent

Write-Host "TP-10 Refactoring Complete!"
