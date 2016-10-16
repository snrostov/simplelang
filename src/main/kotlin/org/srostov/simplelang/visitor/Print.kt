package org.srostov.simplelang.visitor

import org.srostov.simplelang.*
import org.srostov.simplelang.visitor.base.PrinterBase

class Printer : PrinterBase() {
    override fun visitConst(x: ConstExpr, a: Unit) {
        appendConst(x.v)
    }

    override fun visitIf(x: If, a: Unit) {
        append("if (")
        append(x)
        append(") ")
        append(x._then)
        append(" else ")
        append(x._else)
    }

    override fun visitOp(x: Operator.Call, a: Unit) {
        append(x.op.toString(x.inputs))
    }

    override fun visitUserFun(x: UserFun.Call, a: Unit) {
        append(x.f.name)
        append("(")
        x.inputs.forEachIndexed { i, expr ->
            if (i > 0) append(",")
            append(expr)
        }
        append(")")
    }

    override fun visitUserFunInput(x: UserFun.Input, a: Unit) {
        append("arg")
        append(x.i.toString())
    }

    override fun visitCycle(x: Cycle.Call, a: Unit) {
        x.cycle.vals.forEachIndexed { i, it ->
            line {
                append("var ")
                append(it.name)
                append(" = ")
                append(x.inputs[i])
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
                    append(it.result)
                }
            }
        }
        line {
            append("} while (")
            append(x.cycle.condition)
            append(")")
        }
    }

    override fun visitCycleVar(x: Cycle.Var, a: Unit) {
        append(x.name)
    }
}

