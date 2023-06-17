# Example Gradle-based project to use Java 21 with Loom features

## Steps

* Download _early-access_ build from [https://jdk.java.net/21/](https://jdk.java.net/21/)
* In [gradle.properties](gradle.properties), set `org.gradle.java.installations.paths` with the downloaded location.
* `./gradlew test` to check if everything is working properly.
* Check [src/test/kotlin/org/pedrofelix/FirstTests.kt](src/test/kotlin/org/pedrofelix/FirstTests.kt) for usage
examples.
