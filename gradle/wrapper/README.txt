This project ships the Gradle wrapper scripts (gradlew / gradlew.bat) and
gradle-wrapper.properties, but NOT the binary gradle-wrapper.jar.

To generate gradle-wrapper.jar, do ONE of the following:
  1) Open the project in Android Studio — it creates the wrapper jar on first sync.
  2) Or run once with a locally installed Gradle 8.9:
        gradle wrapper --gradle-version 8.9

After that, ./gradlew (or gradlew.bat) will work normally.
The provided GitHub Actions workflow generates the wrapper automatically.
