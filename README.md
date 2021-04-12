# Data Model Builder
![Release](https://jitpack.io/v/pemassi/DataModelBuilder.svg)
![Gradle CI](https://github.com/pemassi/DataModelBuilder/actions/workflows/gradle-ci.yml/badge.svg)

Data Model Builder for Kotlin(JVM)

## Setup
Add the JitPack repository in your build.gradle (top level module):
```gradle
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

And add next dependencies in the build.gradle of the module:
```gradle
dependencies {
    implementation 'com.github.pemassi:DataModelBuilder:0.0.1-SNAPSHOT'
}
```
