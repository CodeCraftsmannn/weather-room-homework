# Weather Room

Android homework sample that demonstrates the `Retrofit → Repository → Room → Flow → ViewModel → Compose` data path. The sample requests current weather for Ankara from Open-Meteo and caches the result in Room.

## Open the project

Open this folder in Android Studio and allow Gradle sync. Use JDK 17 and install Android SDK 36. Run the `app` configuration on an emulator or Android device with internet access.

This workspace did not include an Android SDK or Gradle installation, so the project could not be built or run here.

## API key

Open-Meteo's forecast endpoint used here does not require an API key. The Gradle configuration still demonstrates reading `WEATHER_API_KEY` from the root `local.properties` and generating `BuildConfig.WEATHER_API_KEY`, for use if you later switch to a key-protected provider. `local.properties` is gitignored; never add a real key to source code or commit it.

## Persistence check for the submission

1. Launch the app and tap **Refresh**; a weather row should appear.
2. Force stop the app from Android settings (or swipe it away and force stop it).
3. Reopen the app. The previously saved Ankara row is loaded from Room immediately and remains visible, even if the network request fails.

The error state includes a **Retry** action. Both initial load and retry use `onAction` in the ViewModel.
