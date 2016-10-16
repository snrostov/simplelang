package org.srostov.simplelang

import org.srostov.simplelang.visitor.ConstantPropagator
import org.srostov.simplelang.visitor.eval

interface TplEntry


fun applyTpl(tpl: List<TplEntry>, data: List<*>) {

}

fun main(args: Array<String>) {
    val f = UserFun("f", 2) {
        val (i, n) = inputs
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

    val accept = fCall.accept(ConstantPropagator(), 1)

    val message = fCall.eval()
    println(message)
}

fun test(int: Int) {
    var x = ""

    for (it in 1..int) {
        x += "number $it!, "
    }

    println(x)
}

fun test_recursive(i: Int, max: Int): String =
        if (i < max) "number $i, " + test_recursive(i + 1, max) else ""

fun test_recursive2(i: Int, max: Int, result: String): String =
        if (i < max) test_recursive2(i + 1, max, result + "number $i, ") else result