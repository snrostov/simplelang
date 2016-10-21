package org.srostov.simplelang.visitor.base

import org.srostov.simplelang.Expr

abstract class PrinterBase : ExprVisitor<Unit, Unit> {
    val result: StringBuilder = StringBuilder()
    var indent = 0

    inline fun indent(x: () -> Unit) {
        indent++
        x()
        indent--
    }

    inline fun line(x: () -> Unit) {
        appendLineIndent()
        x()
        append("\n")
    }

    fun appendLineIndent() {
        (0..indent-1).forEach {
            result.append("  ")
        }
    }

    fun appendExpr(x: Expr) {
        x.accept(this, Unit)
    }

    fun append(x: String) {
        result.append(x)
    }

    fun appendConst(x: Any) {
        if (x is String) {
            append("\"")
            append(x) // todo: escape
            append("\"")
        } else {
            append(x.toString())
        }
    }
}

fun Expr.print(p: PrinterBase): String {
    accept(p, Unit)
    return p.result.toString();
}

