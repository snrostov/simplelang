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
                        i plus ConstExpr(1),
                        n,
                        result append ConstExpr("number ")
                                append i
                                append ConstExpr(", ")
                ),
                _else = ConstExpr("")
        )
    }

    val call = f(ConstExpr(0), UnknownExpr("N"), ConstExpr(""))

    @Test
    fun test0() = Assert.assertEquals(resource("0"), f.result.toStr())

    @Test
    fun test1() = testInline(call, 0, resource("1"))

    @Test
    fun test5() = testInline(call, 5, resource("5"))
}