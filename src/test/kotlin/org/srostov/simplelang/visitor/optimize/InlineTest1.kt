package org.srostov.simplelang.visitor.optimize

import org.junit.Assert
import org.junit.Test
import org.srostov.simplelang.*
import org.srostov.simplelang.visitor.toStr

class InlineTest1 : BaseTest() {
    val f: UserFun = UserFun("f", "i", "n") {
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

    val call = f(ConstExpr(0), UnknownExpr("N"))

    @Test
    fun test0() = Assert.assertEquals(resource("0"), f.result.toStr())

    @Test
    fun test1() = testInline(call, 0, resource("1"))

    @Test
    fun test5() = testInline(call, 5, resource("5"))
}