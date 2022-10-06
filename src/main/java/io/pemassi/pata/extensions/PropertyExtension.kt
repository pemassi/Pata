package io.pemassi.pata.extensions

import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType

fun KProperty<*>.isEnum(): Boolean = (this.returnType.classifier as KClass<*>).isSubclassOf(Enum::class)


enum class Test
{
    A, B, C
}

fun main()
{
    println(Test::class.starProjectedType.isSubtypeOf(Enum::class.starProjectedType))
}
