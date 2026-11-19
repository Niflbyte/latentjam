---
id: latentjam-library
type: module-design
status: draft
title: Music library
parent: latentjam-architecture
---

## Responsibility

`core/library` provides the music library contract, platform scanning, metadata and lyrics, playlists and catalogue grouping. It supports tag editing and local backup-related data. This description is inferred and unconfirmed.

## Boundary

Android uses MediaStore; iOS combines imported files and Music-library entries. Music-library entries are read-only. Supported file tag writes are designed to leave either the original or edited file after interruption, not a mixture; consult the codec implementations and tests before modifying this guarantee. Playback and recommendation consume library data rather than replacing its platform sources.
