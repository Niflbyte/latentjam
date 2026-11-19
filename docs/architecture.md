---
id: latentjam-architecture
type: architecture-design
status: draft
title: System architecture
parent: latentjam-goal
---

## Drivers

Shared Kotlin implements product behavior and Compose UI; platform code supplies media access, playback, audio decoding and native inference. This draft is inferred from the implementation and README, not separately confirmed.

## Topology and dependencies

`composeApp` composes the SMART, library, playback and history modules through a scoped Koin application. `androidApp` packages `composeApp`; the iOS Swift host embeds the Kotlin framework and installs native providers. Library, playback and history depend on SMART; Android playback additionally depends on library and audio DSP. SMART's Android implementation uses native inference operators and map support. Library's history dependency is test-only. See [module specifications](specs/).

## Decisions and invariants

- Android's application module is a packaging shell: AGP 9's application plugin cannot be combined with the Kotlin Multiplatform setup used by the shared UI. The activity lives in `composeApp`.
- Indexing and recommendation run locally; platform seams provide the actual media and ONNX operations. The map has a separate worker from the confined SMART engine worker.
- Android release runtime substitution verifies the ORT version, exact models, operator list and artifact hashes; a release guard rejects the stock ARM64 runtime unless explicitly requested. See [build guide](development.md).

## Platform constraints

Android reads MediaStore and uses Media3; iOS uses imported files and the Music library, with distinct playback paths. Music-library entries are read-only and do not support imported-file audio effects. Detailed external entry points are recorded in [the 0.9.4 audit](entry-points-094.md); its findings are historical.
