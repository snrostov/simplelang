package org.srostov.simplelang.visitor.optimize

import org.junit.Assert
import org.junit.Test
import org.srostov.simplelang.*
import org.srostov.simplelang.visitor.toStr

class UserFunInlinerTest {
    @Test
    fun test() {
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

        val fCall = f(ConstExpr(1), UnknownExpr("N"))

        // Src
        Assert.assertEquals(
                """if (arg0 < arg1) "number " append arg0 append ", " append f(arg0 + 1,arg1) else """"",
                f.result.toStr()
        )

        // First call inlined and constant propagated:
        val inlined = fCall.inline()
        val inlined2 = inlined.accept(ConstantPropagator())
        Assert.assertEquals(
                """if (1 < N) "number 1, " append f(2,N) else """"",
                inlined2.toStr()
        )
    }
}