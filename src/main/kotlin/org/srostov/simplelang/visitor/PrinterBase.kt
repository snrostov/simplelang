package org.srostov.simplelang.visitor

import org.srostov.simplelang.Expr
import org.srostov.simplelang.ExprVisitor

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
        result.append("\n")
        (0..indent).forEach {
            result.append(" ")
        }
    }

    fun append(x: Expr) {
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

