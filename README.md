# Overview

A basic Bible for use on mobile phones and a POC for use of KMP to create a multiplatform Bible app.
This project is a WIP instigated partly as a technology investigation but also with the aim of creating a simple Bible app for my daughters to use.

Here is a demo with an iPhone on the left and Android device on the right.

https://github.com/user-attachments/assets/beaf0f68-7917-4fcd-9dbc-6d4f29f9624d


# Sword Bible Modules

The only Bible currently used is BSB as a Sword module.  To support [Sword](https://www.crosswire.org/sword/index.jsp) modules a simple Kotlin library 
has been created to download the module, expand it and convert the OSIS to HTML.  

KMP is focused on the use of Kotlin for common code and does not natively support Java or C++ libraries so JSword could 
not be used.  It might have been possible to use JSword for Android and C++ Sword for iOS but the 
decision was made to begin creating a simple Kotlin based Sword (loosely based on JSword).

[JSword](https://www.crosswire.org/jsword/)'s use of InputStream, ZipInputStream, File, SAX parser, Apache Http client for downloads and the Classloader for translations is not supported.
In place of the above are [OKIO](https://github.com/square/okio), [KTOR](https://ktor.io/) and XML Pull Parser ([ktxml](https://github.com/kobjects/ktxml)).  
Additionally [koin](https://insert-koin.io/) is used for dependency injection. 
A single cross-platform UI is enabled by use of Multiplatform Jetpack Compose and compose-webview-multiplatform.

# Kotlin Multiplatform

This is a Kotlin Multiplatform project targeting Android and iOS (desktop maybe be added).

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
