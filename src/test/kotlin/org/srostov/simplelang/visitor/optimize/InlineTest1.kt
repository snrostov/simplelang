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
                "number ".asConst
                        append i
                        append ", ".asConst
                        append f(i + 1.asConst, n),
                "".asConst
        )
    }

    val call = f(0.asConst, UnknownExpr("N"))

    @Test
    fun test0() = Assert.assertEquals(resource("0"), f.result.toStr())

    @Test
    fun test1() = testInline(call, 0, resource("1"))

    @Test
    fun test5() = testInline(call, 5, resource("5"))
}