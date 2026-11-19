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

## Loudness normalization

Normalization is opt-in and uses measured integrated loudness and true peak. Its mode selects attenuation only (the default), attenuation and amplification, or amplification only. The mode is device-local and is not included in local backups. Positive gain is limited to the available measured true-peak headroom below −1 dBTP and to +24 dB; this protects the original dynamics without a normalization limiter, so tracks with high peaks can remain below the loudness target. Equalization and overlapping tracks have their own downstream clipping protection.
