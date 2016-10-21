package org.srostov.simplelang.visitor

import org.srostov.simplelang.*
import org.srostov.simplelang.visitor.base.PrinterBase
import org.srostov.simplelang.visitor.base.print

class Printer : PrinterBase() {
    override fun visitConst(x: ConstExpr, a: Unit) {
        appendConst(x.v)
    }

    override fun visitIf(x: If, a: Unit) {
        append("\n")
        line {
            append("if (")
            appendExpr(x.condition)
            append(") {")
        }
        indent {
            line {
                appendExpr(x._then)
            }
        }
        line {
            append("} else {")
        }
        indent {
            line {
                appendExpr(x._else)
            }
        }
        appendLineIndent()
        append("}")
    }

    override fun visitOp(x: Operator.Call, a: Unit) {
        x.op.toString(x.inputs, this)
    }

    override fun visitUserFun(x: UserFun.Call, a: Unit) {
        append(x.f.name)
        append("(")
        x.inputs.forEachIndexed { i, expr ->
            if (i > 0) append(", ")
            appendExpr(expr)
        }
        append(")")
    }

    override fun visitUserFunInput(x: UserFun.Arg, a: Unit) {
        append("arg")
        append(x.i.toString())
    }

    override fun visitCycle(x: Cycle.Call, a: Unit) {
        x.cycle.vals.forEachIndexed { i, it ->
            line {
                append("var ")
                append(it.name)
                append(" = ")
                appendExpr(x.inputs[i])
            }
        }
        line {
            append("do { ")
        }
        indent {
            x.cycle.vals.forEach {
                line {
                    append(it.name)
                    append(" = ")
                    appendExpr(it.result)
                }
            }
        }
        line {
            append("} while (")
            appendExpr(x.cycle.condition)
            append(")")
        }
    }

    override fun visitCycleVar(x: Cycle.Var, a: Unit) {
        append(x.name)
    }

    override fun visitUnknownExpr(x: UnknownExpr, a: Unit) {
        append(x.name)
    }
}

fun Expr.toStr() = print(Printer())
