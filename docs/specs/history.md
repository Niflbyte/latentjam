---
id: latentjam-history
type: module-design
status: draft
title: Listening history
parent: latentjam-architecture
---

## Responsibility

`core/history` records listening sessions and aggregates, and supports For You impression tracking. The application connects it to playback events. This description is inferred and unconfirmed.

## Boundary

History remains on device and feeds optional personalization and statistics; it is not uploaded or used to train the shipped models. It depends on shared SMART types rather than owning the player or library scanner.
