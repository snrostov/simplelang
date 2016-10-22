package org.srostov.simplelang.visitor.optimize

import org.srostov.simplelang.ConstExpr
import org.srostov.simplelang.Expr
import org.srostov.simplelang.FunCall
import org.srostov.simplelang.visitor.base.Transformer
import org.srostov.simplelang.visitor.eval

class ConstantPropagator : Transformer<Unit>() {
    override fun visitFunCall(x: FunCall, a: Unit): Expr {
        val inputs = transform(x.inputs, a)
        return if (isConsts(inputs)) ConstExpr(x.eval()) else FunCall(x.f, inputs)
    }
}

private fun isConsts(inputs: List<Expr>): Boolean {
    inputs.forEach {
        if (it !is ConstExpr) return false
    }

    return true
}