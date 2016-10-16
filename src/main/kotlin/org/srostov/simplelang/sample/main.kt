package org.srostov.simplelang.sample

import org.srostov.simplelang.*
import org.srostov.simplelang.visitor.eval
import org.srostov.simplelang.visitor.optimize.ConstantPropagator
import org.srostov.simplelang.visitor.toStr

fun main(args: Array<String>) {
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

    val fCall = f(ConstExpr(1), ConstExpr(10))

    val simplified = fCall.accept(ConstantPropagator(), 1)

    val message = fCall.eval()

    println(fCall.toStr())
    println(message)

    println(simplified.toStr())
    println(simplified.eval())
}