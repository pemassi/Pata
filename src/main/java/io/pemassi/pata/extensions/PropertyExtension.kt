package io.pemassi.pata.extensions

import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.*
import kotlin.reflect.jvm.javaType

fun KProperty<*>.isEnum(): Boolean = (this.returnType.classifier as KClass<*>).isSubclassOf(Enum::class)


enum class Test
{
    A, B, C
}

enum class Test2
{
    A, B, C
}

class Test3<T> {

}

class Test4(
    val test3: Test3<String>
)

fun main()
{
    println(Test4::class.memberProperties.first())
}
