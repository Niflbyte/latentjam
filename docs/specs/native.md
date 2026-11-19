---
id: latentjam-native
type: module-design
status: draft
title: Native inference map and audio
parent: latentjam-architecture
---

## Responsibility

`core/ort-ops`, `core/map-native`, and `core/audio-dsp` provide native acceleration for inference operators, map computation and audio processing respectively. They are grouped here as native integration boundaries; the grouping is inferred and unconfirmed.

## Boundary

Shared Kotlin coordinates the work; platform integrations load or link the corresponding native code. Android uses NDK/CMake modules, while iOS links native pods and audio components. See [runtime tooling](../../tools/runtime/README.md) and [audio DSP](../../core/audio-dsp/README.md) for build and verification details.
