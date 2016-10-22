package org.srostov.simplelang.visitor.optimize

open class BaseTest {
    fun resource(s: String): String = javaClass.getResource("${javaClass.simpleName}/$s.txt").readText()
}

fun String.trimLines(): String =
        lineSequence().
                joinToString(separator = "\n", transform = String::trimEnd)

