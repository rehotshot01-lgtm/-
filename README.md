# OfflineAI — ПРАВДА (MVP foundation)

Android/Kotlin starter based on the supplied technical specification.

## Included in this starter
- Jetpack Compose UI with navigation-like sections
- Room database entities for universes, truths, agents, tasks, evaluations, experiments, discoveries, access grants, rollback snapshots, artifacts, memory and audit log
- Repository + ViewModel + StateFlow
- TaskEngine that converts a user request into a simple structured task
- ModelAdapter interface and a clearly labeled deterministic demo adapter
- Verification gate that rejects candidates that fail configured checks
- Versioned candidate/baseline state and audit logging
- Strict offline manifest: **no INTERNET permission**

## Important limitations
This is a foundation/MVP scaffold, not a finished general-purpose AI. The demo adapter does not run an LLM. The sandbox, secret benchmark, adversarial engine ("Помпеї"), true model inference, export/import UI, and robust rollback snapshots still require implementation. They are marked TODO in the source. Do not represent them as implemented.

## Open
Open this folder in Android Studio. Use JDK 17 and Android SDK installed by Android Studio. Let Gradle sync, then run the `app` configuration on an emulator/device.

## First milestone
1. Build and run the UI.
2. Verify Room persistence.
3. Replace `DemoModelAdapter` with a local inference backend through `ModelAdapter`.
4. Implement an OS-isolated sandbox before enabling arbitrary code execution.
5. Add immutable CORE and private SECRET benchmark gates before any candidate can become active.
