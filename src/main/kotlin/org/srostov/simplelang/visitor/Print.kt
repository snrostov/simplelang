package org.srostov.simplelang.visitor

import org.srostov.simplelang.*
import org.srostov.simplelang.visitor.base.PrinterBase
import org.srostov.simplelang.visitor.base.print

class Printer : PrinterBase() {
    override fun visitConst(x: ConstExpr, a: Unit) {
        appendConst(x.v)
    }

    override fun visitFunCall(x: FunCall, a: Unit) {
        if (x.f is UserFun) {
            append(x.f.name)
            append("(")
            x.inputs.forEachIndexed { i, expr ->
                if (i > 0) append(", ")
                appendExpr(expr)
            }
            append(")")
        } else {
            (x.f as BaseFun).print(x.inputs, this)
        }
    }

    override fun visitUserFunInput(x: UserFun.Arg, a: Unit) {
        append(x.name)
    }

    override fun visitUnknownExpr(x: UnknownExpr, a: Unit) {
        append(x.name)
    }
}

fun Expr.toStr() = print(Printer())
