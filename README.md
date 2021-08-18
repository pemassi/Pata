# Pata
[![Release](https://jitpack.io/v/pemassi/Pata.svg)](https://jitpack.io/#pemassi/Pata)
![Gradle CI](https://github.com/pemassi/DataModelBuilder/actions/workflows/gradle-ci.yml/badge.svg)

Data Model Builder for Kotlin(JVM)

## Why did you made this?
Many projects usually use pre-made protocols(ex. HTTP, WebSocket), but sometimes, a developer needs to make their own protocols that communicate on TCP/IP layer, especially, `Fixed-Length Protocol`. With this library, the developers are able to define the `Protocol Data Model(aka. Data Model)`, serialize from `data model` to `String`, deserialize from `String` to `data model` with fewer efforts. Also, it has useful side functions such as detailed logging.

## This library supports Korean also
Especially `Korean` spend 2 bytes on `EUC-KR` encoding or more than that on the other encoding, also this feature is followed to some other languages. Because of this, many Korean developers are having a hard time dealing with counting length for `Fixed-Length Protocol`. This library will help with this without any action.

## How to use?
1. Define your data model
```kotlin
data class KotlinDataClassModel(
    @FixedDataField(order = 1, name = "Item Name",  size = 10)
    var itemName: String = "",

    @FixedDataField(order = 2, name ="Price", size = 5)
    var price: Int = 0,

    @FixedDataField(order = 3, name ="Currency", size = 3)
    var currency: String = "",
): FixedLengthPataModel<String>(
    modelCharset = Charsets.UTF_8,
    paddingMode = PaddingMode.LENIENT
)
```

You can easily define `Data Field` with `FixedDataField` annotation, the non-annotated data field will be ignored, so you could add a not-related variable also.

2. Try to serialize into `String`
> Code
```kotlin
val model = KotlinDataClassModel().apply {
    itemName = "Ice Cream"
    price = 1200
    currency = "KRW"
}

val pata = Pata()
val serialized = pata.serialize<KotlinDataClassModel, FixedLengthPataModel<String>, String>(model)

println("[$serialized]")
```

> Console
```console
[Ice Cream 01200KRW]
```
The way to serialize the `Object` into `String` is similar to `Gson`. The fields will be automatically padded when size is less than the defined size. (There is pre-defined serializing rule into `Pata` class. You might need to make own rule for some types. Please check `io.pemassi.pata.interfaces.PataDataFieldSerializer` interface for information`.)

3. Try to deserialize from `String`
> Code
```kotlin
val recvData = "Keyboard  00010USD"
val deserializedModel = Pata().deserialize<String, FixedLengthPataModel<String>, KotlinDataClassModel>(recvData)
println(deserializedModel.toLog())
```

> Console
```console
'KotlinDataClassModel' Fixed Length Data Model
Name(Variable Name) | Expected Size | Actual Size | Value     
--------------------|---------------|-------------|-----------
Item Name(itemName) |            10 |           8 | [Keyboard]
       Price(price) |             5 |           2 | [10]      
 Currency(currency) |             3 |           3 | [USD]   
```

The way to deseralize the `String` into `Object` is also similar to `Gson`. `Pata` has pretty-log feature, so you could print data model into console to debug data. (Again, you might need to make own rule for some types. Please check `io.pemassi.pata.interfaces.PataDataFieldDeserializer` interface for information.)

## What about speed?
For now, I will not care about speed to focus on making some other features.

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
    implementation 'com.github.pemassi:Pata:2.0.2'
}
```
