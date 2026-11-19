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
