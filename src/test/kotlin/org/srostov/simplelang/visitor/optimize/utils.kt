package org.srostov.simplelang.visitor.optimize

import org.junit.Assert
import org.srostov.simplelang.FunCall
import org.srostov.simplelang.visitor.toStr

open class BaseTest {
    fun resource(s: String): String = javaClass.getResource("${javaClass.simpleName}/$s.txt").readText()
}

fun String.trimLines(): String =
        lineSequence().
                joinToString(separator = "\n", transform = String::trimEnd)

fun testInline(call: FunCall, depth: Int, excepted: String) {
    val actual = call
            .inlineRecursive(depth)
            .accept(ConstantPropagator(), Unit)
            .toStr()
            .trimLines()
            .trim()

    Assert.assertEquals(
            excepted.trim(),
            actual
    )
}