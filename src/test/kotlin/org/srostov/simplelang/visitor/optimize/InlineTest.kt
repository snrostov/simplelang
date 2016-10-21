package org.srostov.simplelang.visitor.optimize

import org.junit.Assert
import org.junit.Test
import org.srostov.simplelang.*
import org.srostov.simplelang.visitor.toStr

class InlineTest {
    val f = UserFun("f", 2) {
        val (i, n) = this.args
        val f = this

        If(
                i less n,
                _then = ConstExpr("number ")
                        append i
                        append ConstExpr(", ")
                        append f(i plus ConstExpr(1), n),
                _else = ConstExpr("")
        )
    }

    private fun inline(depth: Int, excepted: String) {
        val call = f(ConstExpr(0), UnknownExpr("N"))
                .inlineRecursive(depth)
                .accept(ConstantPropagator())
                .toStr()
                .trimLines()
                .trim()

        Assert.assertEquals(
                excepted.trim(),
                call
        )
    }

    @Test
    fun test() {
        resource("inline-0.txt")

        Assert.assertEquals(resource("inline-0.txt"), f.result.toStr())

        inline(0, resource("inline-1.txt"))

        inline(5, resource("inline-5.txt"))
    }

    private fun resource(s: String) = javaClass.getResource(s).readText()
}

private fun String.trimLines(): String =
        lineSequence().
                joinToString(separator = "\n", transform = String::trimEnd)
