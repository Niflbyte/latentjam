---
id: latentjam-smart
type: module-design
status: draft
title: Similarity and recommendation engine
parent: latentjam-architecture
---

## Responsibility

`core/smart` owns on-device embeddings, vector stores, retrieval and SMART queue planning, as well as components for semantic search and map features. Its scoring chain balances seed affinity, smooth transitions and variety; optional habits and playlist affinities influence ranking. Inferred from code and README; unconfirmed.

## Boundary

The shared engine orchestrates platform decoding and inference rather than owning a platform player or scanning a library. It uses 960-dimensional audio and 384-dimensional metadata representations alongside offline artist knowledge. Until audio coverage is sufficient, it ranks using trusted metadata rather than random shuffle; it abstains if trustworthy metadata is unavailable. See [README: How SMART works](../../README.md#how-smart-works) for signal and ranking details.
