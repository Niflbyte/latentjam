---
id: latentjam-application
type: module-design
status: draft
title: Application and platform hosts
parent: latentjam-architecture
---

## Responsibility

`composeApp` owns the shared Compose UI and application composition root; `androidApp` packages the Android app, while `iosApp` hosts the Kotlin framework in Swift. This description is inferred and unconfirmed.

## Boundary

The scoped `AppGraph` wires the core modules and platform implementations. Android exposes OS media integrations including widgets, tile and automotive browsing; the iOS host installs native providers and registers background map work. The hosts should not replace the core modules' domain responsibilities.
