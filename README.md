# Football Card Quiz

A native **Android** football **quiz and learning** app built with **Kotlin** and **Jetpack Compose**. Answer questions about football rules, player positions, referee cards, and match situations, choose a difficulty, and track your best scores locally. A small, secondary **Match Schedule** screen can display upcoming matches from the football-data.org API as an optional reference.

The app is deliberately simple and stable: working code over complex architecture.

---

## 1. Project description

Football Card Quiz is a practical football quiz app. Its main purpose is answering football quiz questions and saving best results locally on the device. The visual identity is a bold **"Yellow Football Quiz Cards"** style — strong yellow, black, and white with referee yellow/red card accents.

The **Match Schedule** feature is a secondary convenience only. The app is **not** a live-score clone, a betting app, a streaming app, a sports-news feed, or an official football app.

## 2. Features

- Six quiz categories: Football Rules, Player Positions, Referee Cards, Match Situations, Field & Equipment, General Football (plus a Mixed quiz).
- Three difficulty levels: Easy, Medium, Hard.
- A local, offline question bank of **54 original questions** (18 Easy, 18 Medium, 18 Hard).
- One-question-at-a-time quiz with answer confirmation, correct/incorrect feedback, and an explanation after each answer.
- Result screen with score percentage, correct/incorrect counts, and a score-based message.
- Best scores tracked locally (overall, per difficulty, per category) and latest result.
- Score history in reverse chronological order, with delete and clear-all (confirmation).
- Review answers screen (with an option to show only wrong answers).
- Secondary Match Schedule screen backed by football-data.org API v4, with caching and demo fallback.
- Full onboarding and settings, including quiz preferences, match settings, disclaimers, and a privacy note.

## 3. Quiz disclaimer

> Football Card Quiz is a football quiz and learning app. Questions and answers are educational and simplified. The app is not an official football rules source, referee certification tool, or professional coaching tool.

This note appears in onboarding, in Settings, and here in the README.

## 4. Match schedule API disclaimer

> Match data is provided by football-data.org. Availability, accuracy, competitions, and update frequency depend on the API provider and the current API plan.

This note appears on the Match Schedule screen, in Settings, and here in the README.

## 5. football-data.org API v4

The Match Schedule feature uses the free **football-data.org API v4**.

- Base URL: `https://api.football-data.org/v4`
- Endpoint used: `GET /matches`
- Auth header: `X-Auth-Token: <your token>`

Only the **matches** endpoint is used. The app never calls odds, predictions, bookmaker, betting, or live-betting endpoints. Register for a free token at <https://www.football-data.org/client/register>.

## 6. Match Schedule default 10-day API window

By default the app requests a **10-day window**: from **today** through **today + 9 days**.

- `dateFrom` = today (local device date, format `YYYY-MM-DD`)
- `dateTo` = today + 9 days (`YYYY-MM-DD`)
- Example (if today is `2026-07-08`): `GET /matches?dateFrom=2026-07-08&dateTo=2026-07-17`

Dates are computed locally from the device date; nothing is hardcoded. The app does not request only today's matches by default, and never requests a very large range. There is no automatic background refresh — data updates only on a manual **Refresh** or once per app open when the cache is empty.

## 7. local.properties setup

Copy `local.properties.example` to `local.properties` and fill in your values:

```
sdk.dir=/path/to/your/Android/sdk
FOOTBALL_DATA_API_TOKEN=your_api_token_here
FOOTBALL_API_BASE_URL=https://api.football-data.org/v4
```

These values are exposed to the app through generated `BuildConfig` fields:

- `BuildConfig.FOOTBALL_DATA_API_TOKEN`
- `BuildConfig.FOOTBALL_API_BASE_URL`

If `FOOTBALL_DATA_API_TOKEN` is missing, empty, or still equals `your_api_token_here`, the app automatically uses **local demo match data** and shows a friendly message — the rest of the app keeps working.

## 8. Never commit API tokens

`local.properties` is listed in `.gitignore` and must **never** be committed. Do not put a real token in source code, README, screenshots, tests, or CI logs. In CI the token is provided only through the optional `FOOTBALL_DATA_API_TOKEN` GitHub Secret and is redacted from logs.

## 9. No ads / no analytics / no payments

The app contains **no ads, no analytics, and no payment/billing** code or SDKs of any kind.

## 10. No betting / no odds / no bookmakers

There is **no betting, no odds, no bookmaker data, and no gambling language** anywhere in the app. It is not a betting or casino product.

## 11. No official logos

The app uses **no official club or league logos** and no copyrighted imagery. Competition and team names appear only as plain text when returned by the API.

## 12. No VaideBet branding

The color mood was inspired only by a yellow/black palette. The app uses **no VaideBet name, logo, or brand identity**, and no sportsbook/betting-slip/deposit UI.

## 13. No account registration

There is **no account, no login, and no registration**. Nothing is required to use the app.

## 14. Local storage

All user data (quiz results, best scores, settings, onboarding flag, match schedule settings, cached matches, last API update time and error, last requested `dateFrom`/`dateTo`) is stored **locally on the device only**. There is no cloud sync and no backend created by the app.

## 15. DataStore

Local storage uses **Jetpack DataStore Preferences**, holding the entire app state as a single JSON string serialized with **kotlinx.serialization**. Reads merge with default values; empty, partial, or corrupted JSON safely falls back to valid defaults so the app never crashes because of storage.

## 16. Internet usage

The app uses the internet **only** to load football match data from football-data.org for the secondary Match Schedule screen. The quiz itself is fully offline.

## 17. INTERNET permission

The manifest declares a single permission, `android.permission.INTERNET`, used only for the Match Schedule API. No other permissions are requested.

## 18. No location permission
The app does not use or request location.

## 19. No notification permission
The app does not use notifications or push notifications.

## 20. No sensors
The app does not use any device sensors.

## 21. No Google Fit
The app does not integrate Google Fit.

## 22. No Health Connect
The app does not integrate Health Connect.

## 23. No wearable integration
The app does not integrate with wearables.

## 24. No automatic tracking
The app performs no automatic activity tracking of any kind. Match data refreshes only on user action (or once per app open if the cache is empty).

## 25. Quiz categories

Football Rules, Player Positions, Referee Cards, Match Situations, Field & Equipment, and General Football. A Mixed quiz draws from all categories. You can also start a quiz for a single category.

## 26. Difficulty levels

Easy (basic), Medium (moderately challenging), and Hard (more specific). Each session uses the selected difficulty, and results store it.

## 27. Question bank

Questions live in `data/local/QuestionBank.kt` as original, simplified content. **No real player names, no official club names, and no official league branding** are used. The bank contains at least 45 questions (54 total: 18 per difficulty). If a category/difficulty has too few questions, the app safely falls back to a broader pool.

## 28. Quiz session

A session shows one question at a time (default 10 questions). You select an answer, confirm it (it cannot be changed afterward), read the explanation, then continue. On the last question the app computes the score and saves the result locally.

## 29. Result screen

Shows score percentage, correct and incorrect counts, category, difficulty, and a message:

- 0–39%: "Keep practicing the rules."
- 40–69%: "Good effort. Review the explanations."
- 70–89%: "Strong football knowledge."
- 90–100%: "Excellent quiz result."

It offers Retry, Review answers, and Go to history. No reward/prize language is used.

## 30. Score history

A reverse-chronological list of past results with date, time, category, difficulty, score, and correct/total. You can open a result's review, delete a single result, or clear all history with confirmation. Empty state prompts you to start your first quiz.

## 31. Best scores

Best overall, best per difficulty, best per category, and the latest result — all stored locally, no cloud, no account.

## 32. Match Schedule (secondary feature)

An optional reference screen showing upcoming matches for the default 10-day window. It shows team names, date, time, competition name/code, status, and score when the API provides one. It never shows odds, bookmakers, predictions, logos, player photos, or streaming links. The Match Schedule intentionally stays visually secondary and never dominates the app identity.

## 33. Match cache

The latest successful API response is normalized and cached locally, along with the last updated time and the requested `dateFrom`/`dateTo`. Cached matches are shown instantly on open and whenever the API is unavailable. Corrupted cache JSON falls back safely.

## 34. Demo data fallback

If there is no API token (or demo data is enabled, or the API is disabled), the app shows built-in **demo matches** with fictional team names. Demo data is also used as a last resort if the API fails and there is no cache.

## 35. App icon concept

A custom adaptive icon: a rounded square strong-yellow background with a black quiz-card symbol, small yellow and red referee-card chips, and a white football dot. It stays readable at small sizes. No default Android icon, no official logos, no player photos, no betting symbols. (Adaptive icon for API 26+, plain-vector fallback for API 24–25.)

## 36. Splash screen concept

A custom splash (via the AndroidX SplashScreen API): strong-yellow background with the centered black quiz-card logo and a red/yellow referee-card accent. No default Android splash, no heavy assets.

## 37. Visual style concept

Bold, sporty, sharp, clean, practical, modern, quiz-card inspired, Android-friendly — and explicitly non-gambling, non-casino, and non-official.

## 38. Yellow Football Quiz Cards

The palette centers on strong yellow (`#FFC72C`) with black/graphite contrast and white content cards. Yellow highlights selection and primary actions; red is used only for red-card visuals and incorrect answers; green only for correct answers and pitch accents. No casino gold, neon glow, heavy gradients, or betting-slip layout.

## 39. Layout uniqueness

The app avoids the generic "mascot → title → subtitle → stat card → stacked buttons → settings" template. The Home screen leads with a yellow hero header and best-score panel, a horizontally scrolling row of referee-card-style quick-play cards, category chips, a latest-result card, dark shortcut tiles, and a small secondary Match Schedule card.

## 40. Open the project in Android Studio

1. Install Android Studio (a recent stable version supporting AGP 8.6 and compile SDK 35).
2. `File → Open` and select the project root (the folder containing `settings.gradle.kts`).
3. On first sync, Android Studio generates the Gradle wrapper jar automatically and downloads dependencies.
4. Create `local.properties` (see section 7) if Android Studio has not already set `sdk.dir`.

> Note: this repository ships the wrapper scripts (`gradlew`, `gradlew.bat`) and `gradle-wrapper.properties`, but not the binary `gradle-wrapper.jar`. Android Studio creates it on first sync; alternatively run `gradle wrapper --gradle-version 8.9` once with a locally installed Gradle. See `gradle/wrapper/README.txt`.

## 41. Configure the API token

Add your token to `local.properties`:

```
FOOTBALL_DATA_API_TOKEN=paste_your_real_token_here
FOOTBALL_API_BASE_URL=https://api.football-data.org/v4
```

Without a real token, the app runs on demo data. The token is read at build time and exposed via `BuildConfig` — it is never hardcoded in source.

## 42. How to build Android

Debug build:

```
./gradlew :app:assembleDebug
```

Release build (requires signing config, see section 43–44):

```
./gradlew :app:assembleRelease   # signed APK
./gradlew :app:bundleRelease     # signed AAB (Play upload target)
```

**Google Play upload target is the `.aab` only**, not the APK.

## 43. Generate a PKCS12 keystore

Create a real PKCS12 keystore (never a debug key for release):

```
keytool -genkeypair -v -storetype PKCS12 -keystore football-card-quiz-release-key.p12 -alias football_card_quiz_key -keyalg RSA -keysize 2048 -validity 10000
```

Use the **same password** for the keystore and the key. Keep the `.p12` file and its passwords out of the repository (they are git-ignored).

For local release builds, create a `keystore.properties` file in the project root (git-ignored):

```
storeFile=/absolute/path/to/football-card-quiz-release-key.p12
storePassword=your_password
keyAlias=football_card_quiz_key
keyPassword=your_password
```

The app's `signingConfigs.release` reads from environment variables first (CI) and falls back to `keystore.properties` (local). The release build type uses `signingConfigs.release`, so the first release build is already signed.

## 44. Add GitHub Secrets

For CI signing, add these repository secrets:

- `ANDROID_KEYSTORE_BASE64` — your `.p12` keystore, base64-encoded (`base64 -i football-card-quiz-release-key.p12 | pbcopy` on macOS, or `base64 -w0` on Linux).
- `ANDROID_KEYSTORE_PASSWORD`
- `ANDROID_KEY_ALIAS`
- `ANDROID_KEY_PASSWORD`
- `FOOTBALL_DATA_API_TOKEN` — optional; if omitted the build still succeeds and the app uses demo data.

Keystore files and passwords must never be committed.

## 45. GitHub Actions

`.github/workflows/android-build.yml` runs on push to `main` (and manual dispatch). It:

1. Checks out the repo and sets up **JDK 17**.
2. Installs Android SDK Platform 35 and Build Tools 35.0.0 (`sdkmanager "platforms;android-35" "build-tools;35.0.0"`).
3. Sets up Gradle 8.9 and generates the wrapper if needed.
4. Creates `local.properties` from the optional `FOOTBALL_DATA_API_TOKEN` secret (or a safe placeholder).
5. Decodes the keystore from `ANDROID_KEYSTORE_BASE64`.
6. Builds the **signed release APK** and **signed release AAB**.
7. Verifies the APK signature with `apksigner verify --print-certs`, prints the certificate, and **fails if the output contains `CN=Android Debug`** (prevents Play rejection from a debug-signed artifact).
8. Uploads the APK and AAB as workflow artifacts.

No emulator smoke-test is run on CI (kept fast and stable). CI proves the build and signing only — see section 50 for local launch verification.

## 46. Google Play compatibility

- Targets **API 35** (`compileSdk = 35`, `targetSdk = 35`) — not 34.
- Release artifacts are signed with a real PKCS12 keystore, verified in CI against debug certificates.
- Upload the **`.aab`** to Google Play.

## 47. Android API 35 notes

The project uses AGP 8.6.1 and Kotlin 2.0.20, which support compile/target SDK 35. `minSdk` is 24.

## 48. 16 KB page size compatibility

The app ships no native libraries of its own. `packaging.jniLibs.useLegacyPackaging = false` keeps any transitively bundled native libraries uncompressed and aligned, which is compatible with Android 15+/16 KB memory page sizes.

## 49. Release optimization

Follow this order for stability:

1. First verify a **non-minified** release build (`isMinifyEnabled = false`, `isShrinkResources = false`) launches cleanly.
2. Then enable **R8 / shrink** (`isMinifyEnabled = true`, `isShrinkResources = true`) using `proguard-android-optimize.txt` + `proguard-rules.pro` and re-test launch.

The shipped `proguard-rules.pro` keeps kotlinx.serialization serializers, all `data.model` classes, API DTOs, and Retrofit/OkHttp. The current `build.gradle.kts` has minify enabled; if you want to validate the non-minified path first, temporarily set both flags to `false`.

## 50. Local launch verification checklist

CI success is not proof the app launches. Before release, install the release APK and check `adb logcat` for a clean start:

```
adb install app/build/outputs/apk/release/app-release.apk
adb logcat
```

Confirm there are no `ClassNotFoundException`, `NoSuchMethodError`, serialization, DataStore parse, or missing-navigation-argument crashes. Manually test:

- First launch with empty storage; complete onboarding.
- Start Easy, Medium, and Hard quizzes; select each category; complete a quiz.
- View the result screen; review wrong answers; open score history; delete a result; clear history.
- Check best scores; reset best scores.
- Open Match Schedule with no API token (demo) and with a token (live).
- Confirm the default Match Schedule request uses today → today + 9 days.
- Refresh matches manually; simulate an API failure; check cached matches; clear match cache.
- Reset all local data; relaunch; launch in airplane mode.
- Verify the release APK signature; confirm only the INTERNET permission is present.

## 51. Privacy note

> Football Card Quiz stores quiz results, best scores, settings, and cached match data on this device. The app uses internet only to load football match data from football-data.org. No account, no ads, no analytics, no payments, no Firebase, no location, no notifications, no sensors, no Google Fit, and no Health Connect.

---

## Project structure

```
FootballCardQuiz/
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── gradlew / gradlew.bat
├── gradle/wrapper/gradle-wrapper.properties
├── local.properties.example
├── .gitignore
├── README.md
├── .github/workflows/android-build.yml
└── app/
    ├── build.gradle.kts
    ├── proguard-rules.pro
    └── src/main/
        ├── AndroidManifest.xml
        ├── res/ (colors, themes, adaptive icon, splash logo, backup rules)
        └── java/com/footballcardquiz/app/
            ├── FootballCardQuizApp.kt        (Application + DI container)
            ├── MainActivity.kt               (splash, theme, nav host)
            ├── util/AppDateTime.kt
            ├── data/model/Models.kt          (all @Serializable models + enums)
            ├── data/local/AppDataStore.kt    (DataStore + safe defaults)
            ├── data/local/QuestionBank.kt    (54 offline questions)
            ├── data/remote/                  (API, DTOs, repository, demo data)
            ├── viewmodel/                     (Main, Quiz, MatchSchedule + factory)
            └── ui/
                ├── theme/                     (Yellow Football Quiz Cards palette)
                ├── components/                (header, chips, cards, badges)
                ├── navigation/                (routes + NavHost)
                └── screens/                   (all 12 screens)
```

## Technology stack

Kotlin · Jetpack Compose · Material 3 · Navigation Compose · Kotlin Coroutines · ViewModel · DataStore Preferences · kotlinx.serialization · Retrofit · OkHttp · Gradle Kotlin DSL.

Architecture is simple MVVM: one local storage repository (`AppDataStore`), one isolated API repository (`FootballDataRepository`), ViewModels per concern, and sealed UI-state classes where useful.
