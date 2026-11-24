# Weather Room

Android homework sample that demonstrates the `Retrofit → Repository → Room → Flow → ViewModel → Compose` data path. The sample requests current weather for Ankara from Open-Meteo and caches the result in Room.

## Open the project

Open this folder in Android Studio and allow Gradle sync. Use Android Studio's bundled JDK and install Android SDK 37. Run the `app` configuration on an emulator or Android device with internet access.

## API key

Open-Meteo's forecast endpoint used here does not require an API key. The Gradle configuration still demonstrates reading `WEATHER_API_KEY` from the root `local.properties` and generating `BuildConfig.WEATHER_API_KEY`, for use if you later switch to a key-protected provider. `local.properties` is gitignored; never add a real key to source code or commit it.

## Persistence check for the submission

1. Launch the app and tap **Refresh**; a weather row should appear.
2. Force stop the app from Android settings (or swipe it away and force stop it).
3. Reopen the app. The previously saved Ankara row is loaded from Room immediately and remains visible, even if the network request fails.

The error state includes a **Retry** action. Both initial load and retry use `onAction` in the ViewModel.

## Build check

`./gradlew :app:assembleDebug` completed successfully in the development workspace. To verify live API access and persistence after a force stop, run the app on an emulator or device.
