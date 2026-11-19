# Development and operations

This guide indexes the checkout's working procedures. The build scripts and linked detailed guides remain authoritative for exact flags and current toolchain versions.

## Prerequisites and build

Use JDK 17 or newer and an Android SDK; CI uses Java 21. Gradle wrapper 9.4.1 is pinned with a distribution checksum. Kotlin/Native downloads a toolchain on first use; the project recommends 16 GB RAM. iOS additionally requires macOS, Xcode and CocoaPods.

```sh
./gradlew :androidApp:assembleDebug
./gradlew testAndroidHostTest
./gradlew :build-logic:test
cd iosApp && pod install
xcodebuild -workspace iosApp.xcworkspace -scheme iosApp -sdk iphonesimulator build
```

The release task is `./gradlew :androidApp:assembleRelease`, but **normally requires a locally built reduced ARM64 ONNX Runtime**. `settings.gradle.kts` verifies its manifest against the exact ORT version, models, operator list and artifact hashes; a release gate rejects an unintended stock ARM64 runtime. Explicit `-Platentjam.stockOrt=true` selects the stock runtime instead. Debug and tests use stock ORT. See [runtime instructions](../tools/runtime/README.md). This release gate qualifies the simpler stock-runtime wording in the root README.

A local release uses the host debug signing key by default; it is not a distribution-signed release. `-Platentjam.unsigned=true` produces unsigned output and `-Platentjam.fdroid=true` disables signing. Release packaging also needs Python 3 and Android SDK `dexdump` for the DEX register limit gate. Use `python3 tools/release/resolve-apk.py --variant release --abi arm64-v8a` to locate the actual artifact rather than assuming an output path. Details: [tools README](../tools/README.md).

## Tests and fixtures

The host test task also runs Kotlin/Native identifier checks. Optional tests require environment variables and fixtures: `SMART_PARITY_FIXTURE`, `SMART_SIM_INPUT`, `TAG_REAL_FILES`, `TAG_REAL_FILES_OUT`, `ID3_REAL_FILES`, and `REAL_AUDIO_FILE`. Gradle does not track these variables: append `--rerun` when changing them. The [contributing section](../README.md#contributing) describes each fixture and its constraints.

[Application CI](../.github/workflows/app.yml) runs Android host tests, tooling and native checks, build/packaging checks and an iOS simulator build and tests on macOS. [Compact runtime CI](../.github/workflows/compact-release.yml) verifies the reduced runtime separately. [Fuzzing](../tools/fuzz/README.md) is manually dispatched; its checked-in seeds also run in host tests. [Device benchmarks](../tools/benchmark/README.md) are opt-in with `-Platentjam.benchmarks=true` and require suitable hardware and a prepared library.

## Release and distribution

The [release workflow](../.github/workflows/release.yml) uses `release/<version>` pushes for dry runs and `v*` tags for release builds; signing is protected by the GitHub release environment. F-Droid uses an independent signing key: switching between F-Droid and GitHub APKs requires backup, uninstall, reinstall and restore. The iOS IPA is unsigned and needs sideloading/re-signing. The maintainer release helper is `tools/release/release.py`; consult its help and the workflow before invoking operations that change tags or publish artifacts.

The website is generated with `python3 tools/build_site.py`; Pages deployment is manually dispatched. The AltStore feed is generated after publishing an IPA by `tools/release/altstore-source.py`; see [tools](../tools/README.md) and [website](../website/README.md).

## Privacy and licensing

See [privacy policy](../PRIVACY.md), [license](../LICENSE), [notice](../NOTICE), and the [model license](../androidApp/src/main/assets/ml/LICENSE-MODEL.txt). The root README identifies unresolved public-release rights reviews for two bundled model components; investigate those before redistributing. Fixture media and fuzz artifacts can contain personal data.
