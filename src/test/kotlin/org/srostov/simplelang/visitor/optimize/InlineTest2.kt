package org.srostov.simplelang.visitor.optimize

import org.junit.Assert
import org.junit.Test
import org.srostov.simplelang.*
import org.srostov.simplelang.visitor.toStr

class InlineTest2 : BaseTest() {
    val f = UserFun("f", "i", "n", "result") {
        val (i, n, result) = this.args
        val f = this

        If(
                i less n,
                _then = f(
                        i + 1.asConst,
                        n,
                        result append "number ".asConst
                                append i
                                append ", ".asConst
                ),
                _else = result
        )
    }

    val call = f(0.asConst, UnknownExpr("N"), "".asConst)

    @Test
    fun test0() = Assert.assertEquals(resource("0"), f.result.toStr())

    @Test
    fun test1() = testInline(call, 0, resource("1"))

    @Test
    fun test5() = testInline(call, 5, resource("5"))
}