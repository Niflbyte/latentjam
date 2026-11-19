---
id: latentjam-playback
type: module-design
status: draft
title: Playback and queue
parent: latentjam-architecture
---

## Responsibility

`core/playback` owns player and queue behavior, persistence and platform playback integrations, including audio effects where supported. This description is inferred and unconfirmed.

## Boundary

Android uses Media3/ExoPlayer and integrates with the library and native audio DSP. iOS plays imported files through AVAudioEngine and Music-library items through MPMusicPlayerController; crossfade, equalizer and normalization are limited to imported files. Platform media controls and entry points are described in the [entry-point audit](../entry-points-094.md).

Normalization gain changes during playback ramp smoothly over a user-configurable duration, defaulting to three seconds and never shorter than 0.4 seconds. The settings slider offers 0.4–15 seconds in 0.1-second increments. The preference is stored in milliseconds on the device and is excluded from local backups. Restoring a backup leaves the device's preference unchanged; legacy version 13 and 14 ramp records remain readable.
